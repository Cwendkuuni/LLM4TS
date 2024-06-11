package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckLevel.WarningLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    CheckLevel level = new CheckLevel(WarningLevel.ALL);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

    NodeTraversal traversal = new NodeTraversal(null, null, checker);

    Node functionNode = Node.newFunctionNode(Token.FUNCTION, null, false, false, false, null, null, null, null, null);
    Node constructorFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, true, false, false, null, null, null, null, null);
    Node thisAnnotatedFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, false, false, false, null, null, null, null, Node.newIdentifierNode("this"));
    Node prototypeFunctionNode = Node.newFunctionNode(Token.FUNCTION, null, false, false, false, null, null, null, null, Node.newIdentifierNode("prototype"));
    Node prototypePropertyFunctionNode = Node.newCallNode(Token.GETPROP, null, Node.newIdentifierNode("prototype"), Node.newIdentifierNode("property"), null);

    Node assignNode = Node.newAssignNode(null, Node.newIdentifierNode("a"), Node.newThisNode());
    Node assignToPrototypeNode = Node.newAssignNode(null, Node.newIdentifierNode("prototype.a"), Node.newThisNode());
    Node assignToPrototypeSubPropertyNode = Node.newAssignNode(null, Node.newIdentifierNode("prototype.b.c"), Node.newThisNode());

    Node scriptNode = Node.newScriptNode(null);

    when(compiler.report(traversal.makeError(NodeUtil.firstChild(assignNode), level, DiagnosticType.warning(
            "JSC_USED_GLOBAL_THIS",
            "dangerous use of the global 'this' object"))))
        .thenReturn(null);

    traversal.traverse(scriptNode);

    Node functionWithJsDocNode = Node.newFunctionNode(Token.FUNCTION, Node.newJsDocInfo(null, null, null, null, null, null, null, null, null), false, false, false, null, null, null, null, null, null);
    JSDocInfo jsDoc = NodeUtil.getFunctionJsDocInfo(functionWithJsDocNode);

    traversal.traverse(functionNode);
    assertThat(checker.assignLhsChild).isNull();

    traversal.traverse(constructorFunctionNode);
    assertThat(checker.assignLhsChild).isNull();

    traversal.traverse(thisAnnotatedFunctionNode);
    assertThat(checker.assignLhsChild).isNull();

    traversal.traverse(prototypeFunctionNode);
    assertThat(checker.assignLhsChild).isNull();

    traversal.traverse(prototypePropertyFunctionNode);
    assertThat(checker.assignLhsChild).isNull();

    traversal.traverse(assignNode);
    assertThat(checker.assignLhsChild).isEqualTo(assignNode.getFirstChild());

    traversal.traverse(assignToPrototypeNode);
    assertNull(checker.assignLhsChild);

    traversal.traverse(assignToPrototypeSubPropertyNode);
    assertNull(checker.assignLhsChild);
  }
}