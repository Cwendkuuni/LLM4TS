package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions.AliasTransformation;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import javax.annotation.Nullable;

import org.junit.Before;
import org.junit.Test;

/**
 * Test class for ScopedAliases.
 */
public class ScopedAliasesTest {

  private Compiler compiler;
  private PreprocessorSymbolTable preprocessorSymbolTable;
  private AliasTransformationHandler transformationHandler;

  @Before
  public void setUp() {
    this.compiler = new Compiler();
    this.preprocessorSymbolTable = new PreprocessorSymbolTable();
    this.transformationHandler = new DummyAliasTransformationHandler();
  }

  @Test
  public void testScopedAliases() {
    String code = "goog.scope(function() {" +
        "  goog.dom = {};" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.dom.createElement(DIV);" +
        "});";

    Node rootNode = this.compiler.parse(code, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(this.compiler, this.preprocessorSymbolTable, this.transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that there are no errors.
    assertTrue(scopedAliases.hasErrors() == false);

    // Check that the alias was defined and applied correctly.
    Var aliasVar = this.preprocessorSymbolTable.getVar("goog.dom");
    assertSame(aliasVar, scopedAliases.aliases.get("goog.dom"));
    assertSame(aliasVar.getNode(), rootNode.getChildAtIndex(3));

    // Check that the alias was used correctly.
    Node aliasUsageNode = rootNode.getChildAtIndex(5);
    assertTrue(aliasUsageNode.getChildAtIndex(0).isQualifiedName());
    assertSame(aliasVar.getNode(), aliasUsageNode.getChildAtIndex(0).getLastChild());

    // Check that the alias was removed from the scope.
    assertNull(aliasVar.getNode().getParent());

    // Check that the scope was collapsed correctly.
    assertSame(rootNode.getChildAtIndex(1), rootNode.getChildAtIndex(4));
  }

  @Test
  public void testScopedAliasesWithErrors() {
    String code = "goog.scope(function() {" +
        "  goog.dom = {};" +
        "  var DIV = goog.dom.TagName.DIV;" +
        "  goog.scope(function() {" +
        "    goog.dom.createElement(DIV);" +
        "  });" +
        "});";

    Node rootNode = this.compiler.parse(code, null, null);

    ScopedAliases scopedAliases = new ScopedAliases(this.compiler, this.preprocessorSymbolTable, this.transformationHandler);
    scopedAliases.process(null, rootNode);

    // Check that there are errors.
    assertTrue(scopedAliases.hasErrors() == true);

    // Check that there is a single error for the nested goog.scope call.
    assertSame(scopedAliases.getAliasDefinitionsInOrder().size(), 0);
    assertSame(scopedAliases.getAliasUsages().size(), 0);
    assertSame(scopedAliases.getScopeCalls().size(), 1);

    Node scopeCallNode = scopedAliases.getScopeCalls().get(0);
    DiagnosticType diagnosticType = DiagnosticType.error(
        "JSC_GOOG_SCOPE_USED_IMPROPERLY",
        "The call to goog.scope must be alone in a single statement.");
    assertEquals(diagnosticType, this.compiler.getDiagnostics().getError(
        scopeCallNode.getSourcePosition()));
  }
}

class DummyAliasTransformationHandler implements AliasTransformationHandler {
  @Override
  public AliasTransformation logAliasTransformation(
      String sourceFileName, SourcePosition<AliasTransformation> position) {
    return new AliasTransformation();
  }
}