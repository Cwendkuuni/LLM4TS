package com.google.javascript.jscomp;

import com.google.common.base.FunctionToBlockMutator;
import com.google.common.base.Preconditions;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;
import junit.framework.TestCase;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Tests for {@link FunctionToBlockMutator} class.
 */
public class FunctionToBlockMutatorTest extends TestCase {

  private AbstractCompiler compiler;
  private Supplier<String> safeNameIdSupplier;

  @Override
  protected void setUp() throws Exception {
    super.setUp();
    this.compiler = new Compiler();
    this.safeNameIdSupplier = () -> String.valueOf(System.currentTimeMillis());
  }

  public void testFunctionToBlockMutator_SimpleFunctionCall() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newString("testFunction")).cloneTree();
    String resultName = "result";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node mutatedBlock = mutator.mutate(fnName, fnNode, callNode, resultName, false, false);

    // Check that the function body has been cloned and modified.
    Node originalFunctionBody = NodeUtil.getFunctionBody(fnNode);
    Node mutatedFunctionBody = NodeUtil.getFunctionBody(fnNode);
    assertEquals(mutatedFunctionBody, mutatedBlock);
    assertNotSame(originalFunctionBody, mutatedFunctionBody);

    // Check that the local variable names are unique.
    NodeTraversal.traverse(compiler, fnNode, new NodeNameTraverser());
    assertTrue(NodeNameTraversalResult.isNameUnique("testVariable1", mutatedFunctionBody));
    assertTrue(NodeNameTraversalResult.isNameUnique("testVariable2", mutatedFunctionBody));

    // Check that the arguments have been inlined and aliased.
    Set<String> arguments = Sets.newHashSet("arg1", "arg2");
    LinkedHashMap<String, Node> argsMap = Maps.newLinkedHashMap();
    argsMap.put("arg1", NodeUtil.newNumberNode(1));
    argsMap.put("arg2", NodeUtil.newString("arg2"));
    Node argumentBlock = FunctionArgumentInjector.inject(mutatedFunctionBody, null, argsMap);
    assertNotNull(argumentBlock);
    assertEquals(mutatedFunctionBody, argumentBlock);

    // Check that the result value is set if needed.
    Node resultNode = mutatedBlock.getChildAtIndex(0);
    assertEquals(Token.ASSIGN, resultNode.getType());
    Node resultValueNode = resultNode.getFirstChild();
    assertEquals(Token.UNDEF, resultValueNode.getType());

    // Check that the block can be injected into another statement.
    Node injectableBlock = NodeUtil.newBlockNode(mutatedBlock);
    NodeStatement injectableStatement = new NodeStatement(injectableBlock);
    NodeStatement originalStatement = new NodeStatement(NodeUtil.newCallNode(NodeUtil.newString("testFunction")));
    NodeStatement transformedStatement = new NodeStatement(injectableStatement);
    assertEquals(originalStatement, transformedStatement);
  }

  public void testFunctionToBlockMutator_FunctionCallInLoop() {
    String fnName = "testFunction";
    Node fnNode = NodeUtil.newFunctionNode(fnName, null, null, null);
    Node callNode = NodeUtil.newCallNode(NodeUtil.newString("testFunction")).cloneTree();
    String resultName = "result";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);
    Node mutatedBlock = mutator.mutate(fnName, fnNode, callNode, resultName, false, true);

    // Check that the function body has been cloned and modified.
    Node originalFunctionBody = NodeUtil.getFunctionBody(fnNode);
    Node mutatedFunctionBody = NodeUtil.getFunctionBody(fnNode);
    assertEquals(mutatedFunctionBody, mutatedBlock);
    assertNotSame(originalFunctionBody, mutatedFunctionBody);

    // Check that the local variable names are unique.
    NodeTraversal.traverse(compiler, fnNode, new NodeNameTraverser());
    assertTrue(NodeNameTraversalResult.isNameUnique("testVariable1", mutatedFunctionBody));
    assertTrue(NodeNameTraversalResult.isNameUnique("testVariable2", mutatedFunctionBody));

    // Check that the arguments have been inlined and aliased.
    Set<String> arguments = Sets.newHashSet("arg1", "arg2");
    LinkedHashMap<String, Node> argsMap = Maps.newLinkedHashMap();
    argsMap.put("arg1", NodeUtil.newNumberNode(1));
    argsMap.put("arg2", NodeUtil.newString("arg2"));
    Node argumentBlock = FunctionArgumentInjector.inject(mutatedFunctionBody, null, argsMap);
    assertNotNull(argumentBlock);
    assertEquals(mutatedFunctionBody, argumentBlock);

    // Check that the result value is set if needed.
    Node resultNode = mutatedBlock.getChildAtIndex(0);
    assertEquals(Token.ASSIGN, resultNode.getType());
    Node resultValueNode = resultNode.getFirstChild();
    assertEquals(Token.UNDEF, resultValueNode.getType());

    // Check that the VAR declarations are initialized.
    Node varDeclarations = mutatedFunctionBody.getFirstChild();
    Node varDeclaration = varDeclarations.getFirstChild();
    Node varName = varDeclaration.getFirstChild();
    Node varInitialization = varDeclaration.getLastChild();
    assertEquals(Token.VAR, varDeclaration.getType());
    assertEquals(Token.ASSIGN, varInitialization.getType());
    assertEquals(Token.UNDEF, varInitialization.getFirstChild().getType());

    // Check that the block can be injected into another statement.
    Node injectableBlock = NodeUtil.newBlockNode(mutatedBlock);
    NodeStatement injectableStatement = new NodeStatement(injectableBlock);
    NodeStatement originalStatement = new NodeStatement(NodeUtil.newCallNode(NodeUtil.newString("testFunction")));
    NodeStatement transformedStatement = new NodeStatement(injectableStatement);
    assertEquals(originalStatement, transformedStatement);
  }

  private static class NodeNameTraverser implements NodeTraversingFunction {
    @Override
    public void traverse(Node node, Node visitor) {
      if (NodeUtil.isVar(node)) {
        String name = node.getFirstChild().getString();
        NodeNameTraversalResult result = NodeNameTraversalResult.getResult(name, visitor);
        assertTrue(result.isSuccess());
      }
      for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
        traverse(child, visitor);
      }
    }
  }

  private static class NodeNameTraversalResult {
    public static NodeNameTraversalResult getResult(String name, Node visitor) {
      NodeNameTraversalResult result = new NodeNameTraversalResult();
      result.name = name;
      result.visitor = visitor;
      result.isSuccess = true;
      result.unique = NodeNameTraversalResult.isNameUnique(name, visitor);
      return result;
    }

    public String name;
    public Node visitor;
    public boolean isSuccess;
    public boolean unique;

    public static boolean isNameUnique(String name, Node visitor) {
      Node foundName = NodeUtil.findFirstChildWithName(visitor, name);
      return foundName == null;
    }
  }
}