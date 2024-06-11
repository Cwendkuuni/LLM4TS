package com.google.javascript.jscomp;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TestName;
import org.junit.rules.TestRule;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Scope;
import com.google.javascript.rhino.NodeTraversal;

public class InlineObjectLiteralsTest {

  @Rule
  public TestRule name = new TestName();

  @Rule
  public TestRule print = new TestRule() {
    @Override
    public Statement apply(Statement base, Description description) {
      return new DoReturnStatement(new PrintStatement(base));
    }
  };

  private static AbstractCompiler createCompiler() {
    return new AbstractCompiler() {
      @Override
      public CompilerOptions getOptions() {
        CompilerOptions options = new CompilerOptions();
        options.setWarningLevel(CompilerOptions.WarningLevel.QUIRKS);
        return options;
      }
    };
  }

  @Test
  public void testInlineSimpleObject() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "");
    String source = "function f() { var obj = {a: 1, b: 2}; }";

    Scope scope = new Scope();
    Node externs = Node.newText("", null);
    Node root = Node.newFunction(Node.newIdentifier("f"), Node.newList(), Node.newList(), Node.newBlock(scope));
    NodeTraversal traversal = NodeTraversal.create(root, null);

    pass.process(externs, root);

    Node obj = traversal.getScope().getVar("obj");
    assertNotNull(obj);
    assertTrue(obj.getNumChildren() > 0);

    for (int i = 0; i < obj.getNumChildren(); ++i) {
      Node child = obj.getChildAtIndex(i);
      assertTrue(child.getType() == Token.VARDECL);
      assertTrue(child.getName().startsWith("JSCompiler_object_inline_"));
    }

    Node f = root.getFirstChild();
    assertNotNull(f);
    assertNotNull(f.getFirstChild());

    Node body = f.getFirstChild();
    assertNotNull(body);
    assertNotNull(body.getLastChild());

    assertTrue(body.getLastChild().getType() == Token.RETURN);
    Node returnNode = body.getLastChild();
    assertNotNull(returnNode);

    Node returnValue = returnNode.getFirstChild();
    assertNotNull(returnValue);
    assertTrue(returnValue.getNumChildren() > 0);

    for (int i = 0; i < returnValue.getNumChildren(); ++i) {
      Node child = returnValue.getChildAtIndex(i);
      assertTrue(child.getType() == Token.ASSIGN);
      assertTrue(child.getName().startsWith("JSCompiler_object_inline_"));
    }
  }

  @Test
  public void testInlineNestedObject() {
    AbstractCompiler compiler = createCompiler();
    CompilerPass pass = new InlineObjectLiterals(compiler, () -> "");
    String source = "function f() { var obj = {a: 1, b: {c: 2}}; }";

    Scope scope = new Scope();
    Node externs = Node.newText("", null);
    Node root = Node.newFunction(Node.newIdentifier("f"), Node.newList(), Node.newList(), Node.newBlock(scope));
    NodeTraversal traversal = NodeTraversal.create(root, null);

    pass.process(externs, root);

    Node obj = traversal.getScope().getVar("obj");
    assertNotNull(obj);
    assertTrue(obj.getNumChildren() > 0);

    for (int i = 0; i < obj.getNumChildren(); ++i) {
      Node child = obj.getChildAtIndex(i);
      assertTrue(child.getType() == Token.VARDECL);
      assertTrue(child.getName().startsWith("JSCompiler_object_inline_"));

      if (i == 0) {
        Node a = child.getFirstChild();
        assertTrue(a.getType() == Token.VARDECL);
        assertTrue(a.getName().startsWith("JSCompiler_object_inline_"));

        Node avalue = a.getFirstChild();
        assertTrue(avalue.getType() == Token.NUMBER);
        assertTrue(avalue.getInt() == 1);
      } else {
        Node b = child.getFirstChild();
        assertTrue(b.getType() == Token.OBJECTLIT);

        for (int j = 0; j < b.getNumChildren(); ++j) {
          Node child2 = b.getChildAtIndex(j);
          assertTrue(child2.getType() == Token.PAIR);

          Node key = child2.getFirstChild();
          assertTrue(key.getType() == Token.NAME);
          assertTrue(key.getString().equals("c"));

          Node value = child2.getLastChild();
          assertTrue(value.getType() == Token.NUMBER);
          assertTrue(value.getInt() == 2);
        }
      }
    }
  }
}