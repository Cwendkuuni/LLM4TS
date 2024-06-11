import static org.junit.Assert.*;
import static com.google.javascript.jscomp.CompilerOptions.AliasTransformation.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.SourceFile;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.NodeTraversal;
import com.google.javascript.rhino.SourcePosition;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

public class ScopedAliasesTest {
  @Test
  public void testScopedAliases() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(new AliasTransformation());
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitionsInOrder.size());
    Node aliasDefinition = aliasDefinitionsInOrder.get(0);
    assertEquals("goog.dom", aliasDefinition.getString());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof ScopedAliases.AliasedNode) {
      ScopedAliases.AliasedNode aliasedNode = (ScopedAliases.AliasedNode) aliasUsage;
      assertEquals("goog.dom", aliasedNode.aliasDefinition.getString());
      assertEquals("DOM", aliasedNode.aliasReference.getString());
    } else if (aliasUsage instanceof ScopedAliases.AliasedTypeNode) {
      ScopedAliases.AliasedTypeNode aliasedTypeNode = (ScopedAliases.AliasedTypeNode) aliasUsage;
      assertEquals("goog.dom", aliasedTypeNode.aliasDefinition.getString());
      assertEquals("goog.dom", aliasedTypeNode.typeReference.getString());
    } else {
      fail("Unexpected alias usage type.");
    }

    // Check there are no errors reported.
    assertEquals(0, compiler.getNumDiagnostics());

    // Check no code change was reported.
    assertFalse(compiler.hasCodeChange());

    // Check no code was changed.
    String originalCode = root.toSource();
    String compiledCode = source.getOutput();
    assertEquals(originalCode, compiledCode);
  }

  @Test
  public void testScopedAliasesBadParameters() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NEW_ALIAS);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node functionNode = scopeCall.getLastChild().getLastChild();
    assertEquals(1, functionNode.getChildCount());
    Node parametersNode = functionNode.getChildAtIndex(0);
    assertFalse(parametersNode.isFunction());
    assertEquals(2, parametersNode.getChildCount());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    // Check there is a errors reported for bad parameters.
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_HAS_BAD_PARAMETERS");
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error, diagnostics.get(0).getType());
    assertEquals(scopeCall.getSourcePosition(), diagnostics.get(0).getPosition());
  }

  @Test
  public void testScopedAliasesUsedReturn() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NEW_ALIAS);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node functionNode = scopeCall.getLastChild().getLastChild();
    assertEquals(1, functionNode.getChildCount());
    Node statementsNode = functionNode.getChildAtIndex(0);
    assertEquals(1, statementsNode.getChildCount());
    Node returnStatementNode = statementsNode.getChildAtIndex(0);
    assertEquals(Token.RETURN, returnStatementNode.getType());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    // Check there is an error reported for using return.
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_USES_RETURN");
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error, diagnostics.get(0).getType());
    assertEquals(returnStatementNode.getSourcePosition(), diagnostics.get(0).getPosition());
  }

  @Test
  public void testScopedAliasesUsedThrow() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NEW_ALIAS);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node functionNode = scopeCall.getLastChild().getLastChild();
    assertEquals(1, functionNode.getChildCount());
    Node statementsNode = functionNode.getChildAtIndex(0);
    assertEquals(1, statementsNode.getChildCount());
    Node throwStatementNode = statementsNode.getChildAtIndex(0);
    assertEquals(Token.THROW, throwStatementNode.getType());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    // Check there is an error reported for using throw.
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_USES_THROW");
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error, diagnostics.get(0).getType());
    assertEquals(throwStatementNode.getSourcePosition(), diagnostics.get(0).getPosition());
  }

  @Test
  public void testScopedAliasesUsedThis() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NEW_ALIAS);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node functionNode = scopeCall.getLastChild().getLastChild();
    assertEquals(1, functionNode.getChildCount());
    Node statementsNode = functionNode.getChildAtIndex(0);
    assertEquals(1, statementsNode.getChildCount());
    Node thisReferenceNode = statementsNode.getChildAtIndex(0);
    assertEquals(Token.THIS, thisReferenceNode.getType());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertTrue(aliasUsages.isEmpty());

    // Check there is an error reported for using this.
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_REFERENCES_THIS");
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error, diagnostics.get(0).getType());
    assertEquals(thisReferenceNode.getSourcePosition(), diagnostics.get(0).getPosition());
  }

  @Test
  public void testScopedAliasesRedefined() throws Exception {
    CompilerOptions options = new CompilerOptions();
    options.setAliasTransformation(NEW_ALIAS);
    options.setAliasTransformationHandler(new NoOpAliasTransformationHandler());

    SourceFile source = new SourceFile("test.js", "test");
    Node root = source.getAST();

    Compiler compiler = new Compiler();
    compiler.setOptionsForFile(options, source);
    HotSwapCompilerPass scopedAliasesPass = new ScopedAliases(compiler, null, null);
    scopedAliasesPass.process(null, root);

    Traversal traversal = scopedAliasesPass.getLastTraversal();

    List<Node> aliasDefinitionsInOrder = traversal.getAliasDefinitionsInOrder();
    assertTrue(aliasDefinitionsInOrder.isEmpty());

    List<Node> scopeCalls = traversal.getScopeCalls();
    assertEquals(1, scopeCalls.size());
    Node scopeCall = scopeCalls.get(0);
    assertEquals("goog.scope", scopeCall.getFirstChild().getString());
    Node functionNode = scopeCall.getLastChild().getLastChild();
    assertEquals(1, functionNode.getChildCount());
    Node statementsNode = functionNode.getChildAtIndex(0);
    assertEquals(2, statementsNode.getChildCount());
    Node assignStatementNode1 = statementsNode.getChildAtIndex(0);
    assertEquals(Token.ASSIGN, assignStatementNode1.getType());
    Node aliasReferenceNode1 = assignStatementNode1.getLastChild();
    assertEquals("x", aliasReferenceNode1.getString());
    Node aliasDefinitionNode1 = assignStatementNode1.getChildAtIndex(0);
    assertEquals("goog.x", aliasDefinitionNode1.getString());

    Node assignStatementNode2 = statementsNode.getChildAtIndex(1);
    assertEquals(Token.ASSIGN, assignStatementNode2.getType());
    Node aliasReferenceNode2 = assignStatementNode2.getLastChild();
    assertEquals("x", aliasReferenceNode2.getString());
    Node aliasDefinitionNode2 = assignStatementNode2.getChildAtIndex(0);
    assertEquals("goog.y", aliasDefinitionNode2.getString());

    List<AliasUsage> aliasUsages = traversal.getAliasUsages();
    assertEquals(1, aliasUsages.size());
    AliasUsage aliasUsage = aliasUsages.get(0);
    if (aliasUsage instanceof ScopedAliases.AliasedNode) {
      ScopedAliases.AliasedNode aliasedNode = (ScopedAliases.AliasedNode) aliasUsage;
      assertEquals("goog.x", aliasedNode.aliasDefinition.getString());
      assertEquals("x", aliasedNode.aliasReference.getString());
    } else if (aliasUsage instanceof ScopedAliases.AliasedTypeNode) {
      fail("Unexpected alias usage type.");
    } else {
      fail("Unexpected alias usage.");
    }

    // Check there is an error reported for alias redefined.
    DiagnosticType error = DiagnosticType.error("JSC_GOOG_SCOPE_ALIAS_REDEFINED", "x");
    List<Diagnostic> diagnostics = compiler.getDiagnostics();
    assertEquals(1, diagnostics.size());
    assertEquals(error, diagnostics.get(0).getType());
    assertEquals(assignStatementNode2.getSourcePosition(), diagnostics.get(0).getPosition());
  }

  private static class NoOpAliasTransformationHandler implements AliasTransformationHandler {
    @Override
    public void logAliasTransformation(String filename, SourcePosition position) {}
  }
}