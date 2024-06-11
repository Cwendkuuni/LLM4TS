package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.common.collect.ImmutableMap;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.CompilerPassManager;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.Node;

@RunWith(JUnit4.class)
public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() throws Exception {
    String input = "function foo() {" +
        "  var obj = {a: 1, b: 2};" +
        "  return obj.a + obj.b;" +
        "}";

    Compiler compiler = new Compiler();
    CompilerPassManager passManager = new CompilerPassManager(compiler);
    passManager.addPass(new InlineObjectLiterals(compiler, () -> "test_"))
        .enable();

    Node root = NodePrinter.parseTree(input);
    passManager.process(root, null);

    assertNotNull(root.getFirstChild());
    assertNotNull(root.getFirstChild().getFirstChild());
    assertNotNull(root.getFirstChild().getFirstChild().getFirstChild());
    assertEquals("test_a", root.getFirstChild().getFirstChild().getFirstChild().getString());
    assertNotNull(root.getFirstChild().getFirstChild().getNext());
    assertNotNull(root.getFirstChild().getFirstChild().getNext().getFirstChild());
    assertEquals("test_b", root.getFirstChild().getFirstChild().getNext().getFirstChild().getString());

    Scope scope = root.getFirstChild().getScope();
    assertEquals(2, scope.getVars().size());
    for (int i = 0; i < scope.getVars().size(); i++) {
      Var var = scope.getVars().get(i);
      assertNotNull(var.name);
      assertEquals(1, var.getInitializers().size());
      Node initializer = var.getInitializers().get(0);
      assertNotNull(initializer);
      assertEquals(Token.ASSIGN, initializer.getNodeType());
      assertNotNull(initializer.getFirstChild());
      Node assignmentValue = initializer.getFirstChild();
      assertNotNull(assignmentValue);
      assertEquals(Token.OBJECT_LITERAL, assignmentValue.getNodeType());
      List<Node> keys = Lists.newArrayList();
      List<Node> values = Lists.newArrayList();
      NodeUtil.visit(assignmentValue, new NodeUtil.Visitor<Node>() {
        @Override
        public void visit(Node node) {
          if (node.isName()) {
            keys.add(node);
          } else if (node.isObjectLit()) {
            values.add(node);
          }
        }
      }, NodeUtil.MATCH_NOT_FUNCTION);
      assertEquals(2, keys.size());
      assertEquals(2, values.size());
      Map<String, String> varmap = ImmutableMap.of(
          keys.get(0).getString(), "test_" + keys.get(0).getString(),
          keys.get(1).getString(), "test_" + keys.get(1).getString());
      for (int j = 0; j < keys.size(); j++) {
        String key = keys.get(j).getString();
        String valueKey = varmap.get(key);
        Node value = values.get(j);
        Node valueVar = scope.getVar(valueKey);
        assertNotNull(valueVar);
        assertNotNull(valueVar.getInitializers());
        assertEquals(1, valueVar.getInitializers().size());
        Node valueVarInitializer = valueVar.getInitializers().get(0);
        assertEquals(Token.ASSIGN, valueVarInitializer.getNodeType());
        assertNotNull(valueVarInitializer.getFirstChild());
        assertNotNull(valueVarInitializer.getFirstChild().getFirstChild());
        assertEquals(value.getFirstChild().getString(),
            valueVarInitializer.getFirstChild().getFirstChild().getString());
      }
    }

    Node foo = root.getLastChild();
    assertNotNull(foo);
    assertEquals(Token.FUNCTION, foo.getNodeType());
    Node body = foo.getFirstChild();
    assertNotNull(body);
    assertEquals(Token.BLOCK, body.getNodeType());
    Node varObj = body.getFirstChild();
    assertNotNull(varObj);
    assertEquals(Token.VAR, varObj.getNodeType());
    Map<String, Node> variables = Maps.newHashMap();
    NodeUtil.visit(varObj, new NodeUtil.Visitor<Node>() {
      @Override
      public void visit(Node node) {
        variables.put(node.getString(), node);
      }
    }, NodeUtil.MATCH_NOT_FUNCTION);
    assertEquals(2, variables.size());
    Node varA = variables.get("a");
    assertNotNull(varA);
    Node varB = variables.get("b");
    assertNotNull(varB);
    Node add = body.getChildAtIndex(1);
    assertNotNull(add);
    assertEquals(Token.ADD, add.getNodeType());
    Node a = add.getFirstChild();
    assertNotNull(a);
    assertEquals(Token.GET_PROP, a.getNodeType());
    assertEquals(varA, a.getParent());
    Node b = add.getLastChild();
    assertNotNull(b);
    assertEquals(Token.GET_PROP, b.getNodeType());
    assertEquals(varB, b.getParent());

    Node result = body.getChildAtIndex(2);
    assertNotNull(result);
    assertEquals(Token.RETURN, result.getNodeType());
    Node returnValue = result.getFirstChild();
    assertNotNull(returnValue);
    assertEquals(Token.ADD, returnValue.getNodeType());
    assertEquals(a, returnValue.getChildAtIndex(0));
    assertEquals(b, returnValue.getChildAtIndex(1));

    String output = NodePrinter.printTree(root);
    assertEquals("function foo() {" +
        "  var test_a = 1;" +
        "  var test_b = 2;" +
        "  return test_a + test_b;" +
        "}",
        output);
  }
}