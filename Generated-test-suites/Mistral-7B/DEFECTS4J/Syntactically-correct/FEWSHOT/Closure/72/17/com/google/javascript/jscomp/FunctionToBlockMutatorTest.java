package org.apache.commons.cli.jscomp; // Assuming the test class is inside the same package as FunctionToBlockMutator

import static com.google.common.base.Preconditions.checkState;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.Node;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.junit.Test;

public class FunctionToBlockMutatorTest {

  @Test
  public void testMutateFunction() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION_DECL, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL_EXPR, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoOpAbstractCompiler(), () -> "id");

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);
  }

  @Test
  public void testMutateFunctionWithArguments() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION_DECL, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL_EXPR, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    LinkedHashMap<String, Node> argMap = Maps.newLinkedHashMap();
    argMap.put("arg1", Node.newIdentifier("arg1"));
    argMap.put("arg2", Node.newIdentifier("arg2"));

    Set<String> namesToAlias = Set.of("arg1");

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoOpAbstractCompiler(), () -> "id");

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check if arguments were aliased and inlined
    Node inlineFnTemplateRoot = NodeUtil.getFunctionBody(newFnNode);
    for (Entry<String, Node> entry : argMap.entrySet()) {
      Node argNode = NodeUtil.findFirstChildOfType(inlineFnTemplateRoot,
          Node.newString(Token.NAME, entry.getKey()));
      assertNotNull(argNode);
      Node argAliasNode = NodeUtil.findFirstChildOfType(inlineFnTemplateRoot,
          Node.newIdentifier(entry.getKey()));
      assertNotNull(argAliasNode);
      checkState(argNode.getParent() == argAliasNode.getParent());
      checkState(argNode.getParent().getFirstChild() == argAliasNode);
    }
  }

  @Test
  public void testMutateFunctionInLoop() {
    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Token.FUNCTION_DECL, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL_EXPR, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = true;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoOpAbstractCompiler(), () -> "id");

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check if variables are initialized
    Node inlineFnTemplateRoot = NodeUtil.getFunctionBody(newFnNode);
    for (Node n = inlineFnTemplateRoot; n != null; n = n.getNext()) {
      if (NodeUtil.isVar(n)) {
        Node name = n.getFirstChild();
        if (name.getType() == Token.NAME && name.getString().startsWith("v_")) {
          Node value = n.getLastChild();
          if (value.getType() == Token.UNDEFINED) {
            Node parent = n.getParent();
            assertNotNull(parent);
            assertNotNull(parent.getFirstChild());
            parent.getFirstChild().addChildToBack(value);
          }
        }
      }
    }
  }

  @Test
  public void testMutateFunctionWithEmptyName() {
    String fnName = "";
    Node fnNode = Node.newFunction(Token.FUNCTION_DECL, null, null, null, null);
    Node callNode = Node.newCall(Token.CALL_EXPR, fnNode, null);
    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    FunctionToBlockMutator functionToBlockMutator = new FunctionToBlockMutator(
        new NoOpAbstractCompiler(), () -> "id");

    Node newFnNode = functionToBlockMutator.mutate(fnName, fnNode, callNode,
        resultName, needsDefaultResult, isCallInLoop);

    assertNotNull(newFnNode);

    // Check if a name is assigned
    String labelName = "JSCompiler_inline_label_" + functionToBlockMutator.safeNameIdSupplier.get();
    Node labelNode = NodeUtil.findFirstChildOfType(newFnNode, new Node(Token.LABEL));
    assertNotNull(labelNode);
    Node labelNameNode = labelNode.getFirstChild();
    assertNotNull(labelNameNode);
    assertNotNull(labelNameNode.getString().equals(labelName));
  }
}

class NoOpAbstractCompiler implements AbstractCompiler {
  @Override
  public Node getUniqueNameIdSupplier() {
    return null;
  }

  @Override
  public CodingConvention getCodingConvention() {
    return null;
  }
}