import static org.junit.Assert.*;
import static com.google.javascript.jscomp.Aliases.*;
import static com.google.javascript.jscomp.Externs.*;
import static com.google.javascript.jscomp.NodeTraversal.ScopedCallback.*;
import static com.google.javascript.rhino.SourcePosition.*;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.HotSwapCompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.PreprocessorSymbolTable;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.jscomp.Traversal;
import com.google.javascript.jscomp.Traversal.AliasUsage;
import com.google.javascript.jscomp.Traversal.AliasedNode;
import com.google.javascript.rhino.SourcePosition;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@RunWith(JUnitParamsRunner.class)
public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); })");
    scopedAliases.process(externs, root);

    Node result = root.getChildAtIndex(0);
    assertEquals(GOOG_DOM_CREATE_ELEMENT, result.getType());
    assertEquals("goog.dom.createElement", result.getFirstChild().getString());
    assertEquals(DIV, result.getFirstChild().getLastChild().getFirstChild().getType());
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); })");
    scopedAliases.hotSwapScript(root, null);

    Node result = root.getChildAtIndex(0);
    assertEquals("goog.dom.createElement", result.getString());

    assertEquals(1, scopedAliases.getAliasDefinitionsInOrder().size());
    assertEquals(1, scopedAliases.getScopeCalls().size());
    assertEquals(1, scopedAliases.getAliasUsages().size());
  }

  @Test
  @Parameters(method = "aliasParameters")
  public void testApplyAlias(String aliasName, String expectedAlias) {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { var " + aliasName + " = " + expectedAlias + "; })");
    scopedAliases.hotSwapScript(root, null);

    Node aliasDefinition = scopedAliases.getAliasDefinitionsInOrder().get(0);
    assertEquals(expectedAlias, aliasDefinition.getLastChild().getString());
  }

  @Test
  public void testRemoveAliasDefinition() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); var GOOG_DOM = dom; dom.createElement(GOOG_DOM.DIV); })");
    scopedAliases.hotSwapScript(root, null);

    List<Node> aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(2, aliasDefinitions.size());

    Node domAliasDefinition = aliasDefinitions.get(0);
    Node googDomAliasDefinition = aliasDefinitions.get(1);

    assertEquals("dom", domAliasDefinition.getLastChild().getString());
    assertEquals("goog.dom", googDomAliasDefinition.getLastChild().getString());

    scopedAliases.hotSwapScript(root, null);

    aliasDefinitions = scopedAliases.getAliasDefinitionsInOrder();
    assertEquals(1, aliasDefinitions.size());

    Node newDomAliasDefinition = aliasDefinitions.get(0);
    assertEquals("dom", newDomAliasDefinition.getLastChild().getString());

    assertEquals("goog.dom", googDomAliasDefinition.getString());
    assertFalse(googDomAliasDefinition.getParent().getParent() instanceof Node);
  }

  @Test
  public void testCollapseScope() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); })");
    scopedAliases.hotSwapScript(root, null);

    Node scopeCall = scopedAliases.getScopeCalls().get(0);
    Node scopeClosureBlock = scopeCall.getLastChild();

    assertEquals(1, scopeClosureBlock.getChildCount());
    assertEquals("goog.dom.createElement", scopeClosureBlock.getChildAtIndex(0).getString());

    Node newRoot = makeNode("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); goog.scope(function() { goog.dom.createElement(DIV); }) })");
    scopedAliases.hotSwapScript(newRoot, null);

    scopeCall = scopedAliases.getScopeCalls().get(0);
    scopeClosureBlock = scopeCall.getLastChild();

    assertEquals(1, scopeClosureBlock.getChildCount());
    assertEquals("goog.dom.createElement", scopeClosureBlock.getChildAtIndex(0).getString());

    Node newScopeCall = scopeCall.getLastChild();
    Node newScopeClosureBlock = newScopeCall.getLastChild();

    assertEquals(0, newScopeClosureBlock.getChildCount());
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeAtRoot() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() {})");
    scopedAliases.process(externs, root);

    fail("Expected an error for goog.scope at the root.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithParameters() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function(p) {})");
    scopedAliases.process(externs, root);

    fail("Expected an error for goog.scope with parameters.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithReturn() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { return 1; })");
    scopedAliases.process(externs, root);

    fail("Expected an error for goog.scope with a return statement.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithThrow() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { throw new Error('Test Error'); })");
    scopedAliases.process(externs, root);

    fail("Expected an error for goog.scope with a throw statement.");
  }

  @Test(expected = AssertionError.class)
  public void testGoogScopeWithThis() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new AliasTransformationHandler() {};

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);

    Node root = makeNode("goog.scope(function() { this.test = 1; })");
    scopedAliases.process(externs, root);

    fail("Expected an error for goog.scope with 'this'.");
  }

  private static Node makeNode(String code) {
    return new JSCompiler().parseTree(code, new SourceFile("test.js", 1, 1), null);
  }

  private static List<Object[]> aliasParameters() {
    return Lists.newArrayList(
        new Object[]{"dom", "goog.dom"},
        new Object[]{"DIV", "goog.dom.TagName.DIV"},
        new Object[]{"GOOG_DOM", "goog.dom"},
        new Object[]{"goog", "com.google"},
        new Object[]{"JSDocInfo", "com.google.javascript.rhino.JSDocInfo"}
    );
  }
}