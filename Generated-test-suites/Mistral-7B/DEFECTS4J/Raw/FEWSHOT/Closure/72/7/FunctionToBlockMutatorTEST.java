package com.google.javascript.jscomp;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.collect.Lists;
import com.google.javascript.jscomp.FunctionToBlockMutator.LabelNameSupplier;
import com.google.javascript.jscomp.Node;
import com.google.javascript.rhino.NodeUtil;

@RunWith(JUnit4.class)
public class FunctionToBlockMutatorTest {

  @Test
  public void testMutate() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW, fnName, null, null, NodeUtil.newBlock(
        Node.newVar(Node.NEW, "a"),
        Node.newVar(Node.NEW, "b"),
        Node.newReturn(Node.NEW, Node.newNumber(Node.NEW, 10))
    ));

    Node callNode = Node.newCall(Node.NEW, NodeUtil.newIdent(Node.NEW, "testFunction"),
        Node.newNumber(Node.NEW, 5),
        Node.newNumber(Node.NEW, 10)
    );

    String resultName = "result";
    boolean needsDefaultResult = true;
    boolean isCallInLoop = false;

    Node mutatedBlock = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    assertEquals(NodeUtil.newBlock(
        Node.newVar(Node.NEW, "a"),
        Node.newVar(Node.NEW, "b"),
        Node.newAssign(Node.NEW, NodeUtil.newIdent(Node.NEW, resultName), Node.newNumber(Node.NEW, 10)),
        Node.newBreak()
    ), mutatedBlock);
  }

  @Test
  public void testMutateWithArguments() {
    AbstractCompiler compiler = new AbstractCompiler() {};
    Supplier<String> safeNameIdSupplier = () -> "";

    FunctionToBlockMutator mutator = new FunctionToBlockMutator(compiler, safeNameIdSupplier);

    String fnName = "testFunction";
    Node fnNode = Node.newFunction(Node.NEW, fnName, ImmutableSet.of("x", "y"), null, NodeUtil.newBlock(
        Node.newVar(Node.NEW, "a"),
        Node.newVar(Node.NEW, "b"),
        Node.newVar(Node.NEW, "x"),
        Node.newVar(Node.NEW, "y"),
        Node.newReturn(Node.NEW, Node.newAdd(Node.NEW, NodeUtil.newIdent(Node.NEW, "a"), NodeUtil.newIdent(Node.NEW, "b")))
    ));

    Node callNode = Node.newCall(Node.NEW, NodeUtil.newIdent(Node.NEW, "testFunction"),
        NodeUtil.newArray(Node.NEW, Node.newNumber(Node.NEW, 5), Node.newNumber(Node.NEW, 10)),
        NodeUtil.newArray(Node.NEW, Node.newNumber(Node.NEW, 2), Node.newNumber(Node.NEW, 3))
    );

    String resultName = "result";
    boolean needsDefaultResult = false;
    boolean isCallInLoop = false;

    Map<String, Node> argMap = ImmutableMap.of(
        "x", Node.newNumber(Node.NEW, 1),
        "y", Node.newNumber(Node.NEW, 2)
    );
    Set<String> namesToAlias = ImmutableSet.of("x");

    Node mutatedBlock = mutator.mutate(fnName, fnNode, callNode, resultName, needsDefaultResult, isCallInLoop);

    List<Node> newVars = Lists.newLinkedList();
    newVars.add(Node.newVar(Node.NEW, "xAlias"));
    newVars.add(Node.newVar(Node.NEW, "y"));

    Node newFnNode = NodeUtil.newFunction(
        Node.NEW, fnName, ImmutableSet.of("xAlias", "y"), null,
        NodeUtil.newBlock(
            Node.newVar(Node.NEW, "a"),
            Node.newVar(Node.NEW, "b"),
            Node.newVar(Node.NEW, "xAlias"),
            Node.newAssign(Node.NEW, NodeUtil.newIdent(Node.NEW, "xAlias"), NodeUtil.newIdent(Node.NEW, "x")),
            Node.newAssign(Node.NEW, NodeUtil.newIdent(Node.NEW, "y"), NodeUtil.newIdent(Node.NEW, "y")),
            Node.newReturn(Node.NEW, Node.newAdd(Node.NEW, NodeUtil.newIdent(Node.NEW, "a"), NodeUtil.newIdent(Node.NEW, "b")))
        )
    );

    Node argNode = newFnNode.getFirstChild();
    for (int i = 0; i < argNode.getNumChildren(); i++) {
      Node arg = argNode.getChild(i);
      if (arg.getType() == Token.VAR) {
        String argName = arg.getFirstChild().getString();
        Node newArg = argMap.get(argName);
        arg.replaceChildren(newArg);
      }
    }

    Node aliasNode = newVars.get(0);
    Node aliasDecl = aliasNode.getFirstChild();
    Node aliasInit = aliasNode.getLastChild();

    Node newBlock = NodeUtil.getFunctionBody(mutatedBlock);
    Node newBlockRoot = NodeUtil.newBlock(newVars.toArray(new Node[newVars.size()]));
    newBlockRoot.addChildrenToBack(newBlock);

    Node result = NodeUtil.newAssign(Node.NEW, NodeUtil.newIdent(Node.NEW, resultName), NodeUtil.newIdent(Node.NEW, "result"));
    newBlockRoot.addChildToBack(result);

    LabelNameSupplier labelNameSupplier = new LabelNameSupplier(() -> "");
    String labelName = labelNameSupplier.get();
    Node label = Node.newLabel(Node.NEW, labelName);
    newBlockRoot.addChildToBack(label);
    newBlockRoot.addChildToBack(new Block(Node.NEW, NodeUtil.newStatementList(
        NodeUtil.newAssign(Node.NEW, NodeUtil.newIdent(Node.NEW, "xAlias"), NodeUtil.newIdent(Node.NEW, "x")),
        NodeUtil.newBreak(Node.NEW, labelName)
    )));

    Node expectedBlock = newFnNode;
    assertEquals(expectedBlock, newBlockRoot);
  }
}

class Block {
  Node node;

  Block(int type, Node node) {
    this.node = node;
  }
}