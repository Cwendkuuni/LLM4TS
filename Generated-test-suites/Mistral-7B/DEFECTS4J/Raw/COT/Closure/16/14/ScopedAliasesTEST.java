import static org.junit.Assert.*;
import static com.google.javascript.rhino.NodeType.*;
import static com.google.javascript.jscomp.ScopedAliases.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.*;
import com.google.javascript.rhino.*;

import java.util.Arrays;
import java.util.List;

public class ScopedAliasesTest {

  @Test
  public void testProcess() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new DefaultAliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = NodeFactory.createScript("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    scopedAliases.process(null, root);

    // TODO: Add assertions for the transformed code.
  }

  @Test
  public void testHotSwapScript() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new DefaultAliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = NodeFactory.createScript("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    scopedAliases.hotSwapScript(root, null);

    // TODO: Add assertions for the transformed code.
  }

  @Test
  public void testIsCallToScopeMethod() {
    Node node = NodeFactory.createCall(Token.NAME, "goog.scope");
    assertTrue(ScopedAliases.isCallToScopeMethod(node));

    node.setFirstChild(NodeFactory.createString("invalid"));
    assertFalse(ScopedAliases.isCallToScopeMethod(node));
  }

  @Test
  public void testApplyAlias() {
    Node aliasReference = NodeFactory.createName("goog.x");
    Node aliasDefinition = NodeFactory.createName("java.lang.String");

    ScopedAliases.AliasedNode aliasedNode = new ScopedAliases.AliasedNode(aliasReference, aliasDefinition);
    aliasedNode.applyAlias();

    assertEquals(aliasDefinition, aliasReference.getParent());
  }

  @Test
  public void testTraverse() {
    AbstractCompiler compiler = new Compiler();
    PreprocessorSymbolTable preprocessorSymbolTable = new PreprocessorSymbolTable();
    AliasTransformationHandler transformationHandler = new DefaultAliasTransformationHandler();

    ScopedAliases scopedAliases = new ScopedAliases(compiler, preprocessorSymbolTable, transformationHandler);
    Node root = NodeFactory.createScript("goog.scope(function() { var dom = goog.dom; var DIV = dom.TagName.DIV; dom.createElement(DIV); });");

    scopedAliases.traverse(root, null);

    // TODO: Add assertions for the traversed nodes.
  }

  @Test
  public void testFindAliases() {
    // TODO: Implement testFindAliases method.
  }

  @Test
  public void testFindNamespaceShadows() {
    // TODO: Implement testFindNamespaceShadows method.
  }

  @Test
  public void testRenameNamespaceShadows() {
    // TODO: Implement testRenameNamespaceShadows method.
  }

  @Test
  public void testValidateScopeCall() {
    Node node = NodeFactory.createCall(Token.NAME, SCOPING_METHOD_NAME);
    Node parent = NodeFactory.createBlock();
    parent.addChild(node);

    ScopedAliases.validateScopeCall(null, node, parent);

    // TODO: Add assertions for the validation result.
  }
}