package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.AliasTransformation;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.SourcePosition;
import com.google.javascript.rhino.Token;

public class ScopedAliasesTest {
  private AbstractCompiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;
  private HotSwapCompilerPass scopedAliases;

  @Before
  public void setup() {
    CompilerOptions options = new CompilerOptions();
    AliasTransformation aliasTransformation = new AliasTransformation();
    this.transformationHandler = new AliasTransformationHandler(aliasTransformation);
    this.compiler = new Compiler(options, null, transformationHandler);
    this.preprocessorSymbolTable = null;
    this.scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
  }

  @Test
  public void testScopedAliases() {
    String testCode = "goog.scope(function() {" +
        "var x = goog.x;" +
        "goog.scope(function() {" +
        "var y = goog.x;" +
        "});" +
        "});";

    Node root = Parser.parse(testCode, "test.js", null);
    this.scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = this.scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = this.scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = this.scopedAliases.getAliasUsages();

    assertEquals(2, aliasDefinitionsInOrder.size());
    assertEquals("goog.x", aliasDefinitionsInOrder.get(0).getString());
    assertEquals("goog", aliasDefinitionsInOrder.get(1).getString());

    assertEquals(2, scopeCalls.size());
    assertEquals(0, scopeCalls.get(0).getChildCount());
    assertEquals(1, scopeCalls.get(1).getChildCount());

    assertEquals(2, aliasUsages.size());

    AliasUsage aliasUsage1 = aliasUsages.get(0);
    AliasUsage aliasUsage2 = aliasUsages.get(1);

    if (aliasUsage1 instanceof AliasedNode) {
      AliasedNode aliasedNode1 = (AliasedNode) aliasUsage1;
      assertEquals("goog.x", aliasedNode1.aliasReference.getString());
      assertEquals("goog.x", aliasedNode1.aliasDefinition.getString());
    }

    if (aliasUsage2 instanceof AliasedTypeNode) {
      AliasedTypeNode aliasedTypeNode1 = (AliasedTypeNode) aliasUsage2;
      assertEquals("goog", aliasedTypeNode1.typeReference.getString());
      assertEquals("goog", aliasedTypeNode1.aliasName);
    }
  }

  @Test
  public void testInvalidScopedAliases() {
    String testCode = "goog.scope(function() {" +
        "this.x = goog.x;" +
        "return goog.x;" +
        "});";

    Node root = Parser.parse(testCode, "test.js", null);
    this.scopedAliases.process(null, root);

    List<Node> aliasDefinitionsInOrder = this.scopedAliases.getAliasDefinitionsInOrder();
    List<Node> scopeCalls = this.scopedAliases.getScopeCalls();
    List<AliasUsage> aliasUsages = this.scopedAliases.getAliasUsages();

    assertEquals(0, aliasDefinitionsInOrder.size());
    assertEquals(1, scopeCalls.size());
    assertEquals(0, aliasUsages.size());

    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    SourcePosition<AliasTransformation> position =
        this.scopedAliases.getSourceRegion(scopeCall);

    DiagnosticType thisError = DiagnosticType.error("JSC_THIS_IN_SCOPE");
    DiagnosticType returnError = DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN");

    assertEquals(thisError, this.compiler.getDiagnostics().getError(position));
    assertEquals(returnError, this.compiler.getDiagnostics().getError(position.getNext()));
  }
}