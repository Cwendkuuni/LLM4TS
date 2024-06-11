package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.common.collect.Maps;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.Reference;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.IR;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RunWith(Parameterized.class)
public class InlineObjectLiteralsTest {

  private final AbstractCompiler compiler;
  private final Supplier<String> safeNameIdSupplier;
  private final Node root;
  private final Scope scope;
  private final String objectLiteral;

  public InlineObjectLiteralsTest(String objectLiteral) {
    this.compiler = null; // Replace this with a mock or real compiler instance.
    this.safeNameIdSupplier = () -> "";
    this.root = Node.root();
    this.scope = new Scope(root, null);
    this.objectLiteral = objectLiteral;
  }

  @Parameterized.Parameters
  public static List<Object[]> data() {
    return Arrays.asList(new Object[][] {
        {""},
        {"{foo: 'bar'}"},
        {"{foo: 1, bar: 2}"},
        {"{foo: {bar: {baz: 1}}}"},
        {"{foo: 1, bar: {baz: 1}}"},
        {"{foo: 1, bar: {baz: 1, qux: 2}}"},
        {"{foo: null}"},
        {"{foo: function() {}}"},
        {"{foo: function() {}}['bar']"}
    });
  }

  @Test
  public void testInlineObjectLiterals() {
    Var v = new Var("obj", IR.VAR_DECL, null, this.scope);
    this.scope.addVar(v);

    Node objectNode = NodeUtil.parseTree(this.objectLiteral);
    Node assignment = IR.assign(v, objectNode);

    InlineObjectLiterals pass = new InlineObjectLiterals(this.compiler, this.safeNameIdSupplier);
    pass.process(this.root, this.root);

    // Check if the object literal has been replaced with separate variable assignments
    for (Node child = objectNode.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      String varName = "JSCompiler_object_inline_" + key;

      Node varNode = NodeUtil.findChildByPredicate(this.root, (Node node) -> NodeUtil.isVar(node) && node.getName().equals(varName));
      assertNotNull(varNode);

      Node valueNode = NodeUtil.findChildByPredicate(objectNode, (Node node) -> node.isName() && node.getString().equals(key));
      assertNotNull(valueNode);

      Node assignmentToNewVar = NodeUtil.findChildByPredicate(this.root, (Node node) -> node.getParent() == varNode && node.isAssign());
      assertNotNull(assignmentToNewVar);

      Node expectedValue = NodeUtil.newLiteralNode(child.getFirstChild().getValue());
      Node actualValue = assignmentToNewVar.getRightChild();
      assertEquals(expectedValue, actualValue);
    }

    // Check if the original object literal variable has been removed
    assertNull(NodeUtil.findChildByPredicate(this.root, (Node node) -> node.isVar() && node.getName().equals("obj")));

    // Check if the object literal variable's assignments have been kept
    for (Node child = this.root.getFirstChild(); child != null; child = child.getNext()) {
      if (child.isAssign()) {
        Node leftHandSide = child.getLeftChild();
        if (leftHandSide.isVar() && leftHandSide.getName().equals("JSCompiler_object_inline_foo")) {
          Node rightHandSide = child.getRightChild();
          Node expectedValue = NodeUtil.newLiteralNode("bar");
          Node actualValue = rightHandSide;
          assertEquals(expectedValue, actualValue);
        }
      }
    }

    // Check if the object literal's keys have been expanded as separate variables
    for (Node child = objectNode.getFirstChild(); child != null; child = child.getNext()) {
      String key = child.getString();
      Node keyNode = NodeUtil.findChildByPredicate(this.root, (Node node) -> node.isName() && node.getString().equals(key));
      assertNotNull(keyNode);
    }
  }
}