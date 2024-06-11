package com.google.javascript.jscomp.test;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.jscomp.Scope.Var;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import java.util.List;
import java.util.Map;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    Compiler compiler = new Compiler();
    InlineObjectLiterals inlineObjectLiterals =
        new InlineObjectLiterals(compiler, () -> "temp_var_");

    String sourceCode =
        "function foo() {" +
            "var obj = {" +
            "  a: 1," +
            "  b: 2," +
            "  c: {'d': 3}" +
            "};" +
            "var x = obj.a;" +
            "var y = obj.b;" +
            "var z = obj.c.d;" +
            "}" +
            "foo();";

    Node externs = Node.newTextNode(sourceCode);
    Node root = Node.newBlock(null);
    inlineObjectLiterals.process(externs, root);

    Behavior behavior = inlineObjectLiterals.new InliningBehavior();
    ReferenceCollection referenceCollection = new ReferenceCollectingCallback(compiler, behavior).process(externs, root);

    for (Reference reference : referenceCollection.references) {
      Node referenceNode = reference.getNode();
      if (referenceNode.getType() != Token.GETPROP) {
        continue;
      }

      Node objectNode = referenceNode.getParent();
      String propertyName = referenceNode.getLastChild().getString();

      Var variable = objectNode.getParent().getVar(propertyName);
      Reference declaration = referenceCollection.getDeclarationForVariable(variable);

      assertNull(declaration);

      Map<String, Node> initialValues = behavior.initvals;
      Node initialValueNode = initialValues.get(propertyName);

      if (initialValueNode == null) {
        continue;
      }

      // Check that the variable was assigned to a constant value.
      Node assignedValue = variable.getInitialValue();
      Node valueNode = assignedValue != null ? assignedValue : variable.getNode();
      Node expectedValueNode = initialValueNode;

      NodeUtil.assertTreeEquals(expectedValueNode, valueNode);
    }

    // Check that the object literals were inlined and expanded.
    Node fooNode = NodeUtil.findNodeByName(root, "foo");
    Node fooFunctionNode = fooNode.getLastChild();

    Node bodyNode = fooFunctionNode.getFirstChild();
    List<Node> statements = NodeUtil.asList(bodyNode.getChildren());

    // The first statement is the var declaration for 'obj'.
    Var objVariable = statements.get(0).getFirstChild();
    Node objInitialValueNode = statements.get(0).getLastChild();

    Map<String, String> variables = behavior.varmap;
    String objVariableName = variables.get("obj");
    String objVariableValue = variables.get("obj_a");
    String objVariableValueB = variables.get("obj_b");
    String objVariableValueC = variables.get("obj_c");
    String objVariableValueCD = variables.get("obj_c_d");

    Node objVariableNode = NodeUtil.findNodeByName(root, objVariableName);
    Node objInitialValueNode = objVariableNode.getFirstChild();

    Node objInitialValueNodeA = NodeUtil.findNodeByName(root, objVariableValue);
    Node objInitialValueNodeB = NodeUtil.findNodeByName(root, objVariableValueB);
    Node objInitialValueNodeC = NodeUtil.findNodeByName(root, objVariableValueC);
    Node objInitialValueNodeCD = NodeUtil.findNodeByName(root, objVariableValueCD);

    NodeUtil.assertTreeEquals(objInitialValueNodeA, objInitialValueNodeA);
    NodeUtil.assertTreeEquals(objInitialValueNodeB, objInitialValueNodeB);
    NodeUtil.assertTreeEquals(objInitialValueNodeC, objInitialValueNodeC);
    NodeUtil.assertTreeEquals(objInitialValueNodeCD, objInitialValueNodeCD);

    Node objAssignmentNode = statements.get(1);
    Node objAssignmentValueNode = objAssignmentNode.getLastChild();

    NodeUtil.assertTreeEquals(objInitialValueNodeA, objAssignmentValueNode);

    Node xAssignmentNode = statements.get(2);
    Node xAssignmentValueNode = xAssignmentNode.getLastChild();

    assertThat(xAssignmentValueNode).isInstanceOf(Node.class);
    NodeUtil.assertTreeEquals(Node.newIntLiteral(1), xAssignmentValueNode);

    Node yAssignmentNode = statements.get(3);
    Node yAssignmentValueNode = yAssignmentNode.getLastChild();

    assertThat(yAssignmentValueNode).isInstanceOf(Node.class);
    NodeUtil.assertTreeEquals(Node.newIntLiteral(2), yAssignmentValueNode);

    Node zAssignmentNode = statements.get(4);
    Node zAssignmentValueNode = zAssignmentNode.getLastChild();

    assertThat(zAssignmentValueNode).isInstanceOf(Node.class);
    NodeUtil.assertTreeEquals(Node.newIntLiteral(3), zAssignmentValueNode);
  }
}