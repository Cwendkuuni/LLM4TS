package com.google.javascript.jscomp;

import org.junit.Test;
import static org.junit.Assert.*;

import com.google.common.base.Supplier;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.FunctionToBlockMutator;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.rhino.Node;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return Integer.toString(counter++);
      }
    };

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node functionNode = Node.newFunctionNode(
        new Node(Token.FUNCTION, null, null, null), null);
    String functionName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node callNode = Node.newCallNode(null, functionNode, Lists.newArrayList());

    Node mutatedNode = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return Integer.toString(counter++);
      }
    };

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node functionNode = Node.newFunctionNode(
        new Node(Token.FUNCTION, null, null, null), null);
    String functionName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node arg1 = Node.newLiteralNode(1);
    Node arg2 = Node.newLiteralNode(2);
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", arg1);
    argMap.put("arg2", arg2);

    Node callNode = Node.newCallNode(null, functionNode, Lists.newArrayList(arg1, arg2));

    Node mutatedNode = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);
  }

  @Test
  public void testMutateFunctionWithAliases() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return Integer.toString(counter++);
      }
    };

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node functionNode = Node.newFunctionNode(
        new Node(Token.FUNCTION, null, null, null), null);
    String functionName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Set<String> namesToAlias = Lists.newArrayList("arg1", "arg2").asImmutableList().spliterator().collect(Maps::newLinkedHashMap);

    Node arg1 = Node.newLiteralNode(1);
    Node arg2 = Node.newLiteralNode(2);
    Map<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", arg1);
    argMap.put("arg2", arg2);

    Node callNode = Node.newCallNode(null, functionNode, Lists.newArrayList(arg1, arg2));

    Node mutatedNode = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    Node mutatedFunctionNode = NodeUtil.getFunctionBody(mutatedNode);
    assertNotNull(mutatedFunctionNode);

    List<Node> varNodes = NodeUtil.selectNodes(mutatedFunctionNode, NodeUtil.isVarNode());
    assertEquals(4, varNodes.size());

    String alias1 = "arg1_";
    Node alias1Node = null;
    for (Node varNode : varNodes) {
      String name = varNode.getFirstChild().getString();
      if (name.equals("arg1")) {
        alias1Node = varNode;
        break;
      }
      if (name.startsWith(alias1)) {
        alias1Node = varNode;
        break;
      }
    }
    assertNotNull(alias1Node);

    String alias2 = "arg2_";
    Node alias2Node = null;
    for (Node varNode : varNodes) {
      String name = varNode.getFirstChild().getString();
      if (name.equals("arg2")) {
        alias2Node = varNode;
        break;
      }
      if (name.startsWith(alias2)) {
        alias2Node = varNode;
        break;
      }
    }
    assertNotNull(alias2Node);
  }

  @Test
  public void testMutateFunctionWithLabel() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        return Integer.toString(counter++);
      }
    };

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node functionNode = Node.newFunctionNode(
        new Node(Token.FUNCTION, null, null, null), null);
    String functionName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node labelNameSupplier = new FunctionToBlockMutator.LabelNameSupplier(safeNameIdSupplier);
    String labelName = functionToBlockMutator.getLabelNameForFunction(functionName);

    Node callNode = Node.newCallNode(null, functionNode, Lists.newArrayList());

    Node mutatedNode = functionToBlockMutator.mutate(
        functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(mutatedNode);

    Node mutatedFunctionNode = NodeUtil.getFunctionBody(mutatedNode);
    assertNotNull(mutatedFunctionNode);

    Node labelNode = null;
    for (Node node = mutatedFunctionNode; node != null; node = node.getNext()) {
      if (node.getType() == Token.LABEL) {
        labelNode = node;
        break;
      }
    }
    assertNotNull(labelNode);
    assertEquals(labelName, labelNode.getFirstChild().getString());
  }

  @Test(expected = RuntimeException.class)
  public void testMutateFunctionWithException() {
    AbstractCompiler compiler = new AbstractCompiler();
    Supplier<String> safeNameIdSupplier = new Supplier<String>() {
      private int counter = 0;

      @Override
      public String get() {
        throw new RuntimeException();
      }
    };

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        compiler, safeNameIdSupplier);

    Node functionNode = Node.newFunctionNode(
        new Node(Token.FUNCTION, null, null, null), null);
    String functionName = "testFunction";
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Node callNode = Node.newCallNode(null, functionNode, Lists.newArrayList());

    try {
      functionToBlockMutator.mutate(
          functionName, functionNode, callNode, resultName, needsDefaultResult, isCallInLoop);
      fail("Expected a runtime exception");
    } catch (RuntimeException e) {
      // Expected
    }
  }
}