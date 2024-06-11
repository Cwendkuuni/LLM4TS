package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

@RunWith(Parameterized.class)
public class FunctionRewriterTest {

  private final Node inputNode;
  private final Node expectedOutput;

  public FunctionRewriterTest(Node inputNode, Node expectedOutput) {
    this.inputNode = inputNode;
    this.expectedOutput = expectedOutput;
  }

  @Parameterized.Parameters(name = "inputNode={0}, expectedOutput={1}")
  public static Collection<Object[]> data() {
    return Arrays.asList(new Object[][]{
        // Test for empty function
        new Object[] {
            Node.newFunction(Node.newName(""), Node.newList(), Node.newList()),
            Node.newCall(Node.newString("JSCompiler_emptyFn"), null)
        },

        // Test for identity function
        new Object[] {
            Node.newFunction(Node.newList(Node.newName("a")), Node.newList(),
                             Node.newList(Node.newReturn(Node.newName("a")))),
            Node.newCall(Node.newString("JSCompiler_identityFn"), Node.newName("a"))
        },

        // Test for getter function
        new Object[] {
            Node.newFunction(Node.newList(Node.newName("this")), Node.newList(),
                             Node.newList(Node.newReturn(Node.newGetProp(
                                 Node.newGetProp(Node.newThis(), Node.newName("b")))))),
            Node.newCall(Node.newString("JSCompiler_get"), Node.newName("b"))
        },

        // Test for setter function
        new Object[] {
            Node.newFunction(Node.newList(Node.newName("value"), Node.newName("this")),
                             Node.newList(),
                             Node.newList(Node.newExpressionStatement(
                                 Node.newAssign(Node.newGetProp(Node.newThis(), Node.newName("b")),
                                               Node.newName("value"))))),
            Node.newCall(Node.newString("JSCompiler_set"), Node.newName("b"))
        },

        // Test for return constant function
        new Object[] {
            Node.newFunction(Node.newList(), Node.newList(),
                             Node.newList(Node.newReturn(Node.newNumber(10)))),
            Node.newCall(Node.newString("JSCompiler_returnArg"), Node.newNumber(10))
        },

        // Test for return complex constant function
        new Object[] {
            Node.newFunction(Node.newList(), Node.newList(),
                             Node.newList(Node.newReturn(
                                 Node.newObjectLiteral(Node.newProperty(
                                     Node.newName("a"), Node.newNumber(1)),
                                     Node.newProperty(Node.newName("b"), Node.newNumber(2)))))),
            Node.newCall(Node.newString("JSCompiler_returnArg"),
                         Node.newObjectLiteral(Node.newProperty(Node.newName("a"), Node.newNumber(1)),
                                              Node.newProperty(Node.newName("b"), Node.newNumber(2))))
        }
    });
  }

  @Test
  public void testFunctionRewriter() {
    FunctionRewriter functionRewriter = new FunctionRewriter(new Compiler() {
      @Override
      public Node parseSyntheticCode(String code, int lineno, int charno) {
        return super.parseSyntheticCode(code, lineno, charno);
      }

      @Override
      public Node getNodeForCodeInsertion(Node parent) {
        return null;
      }

      @Override
      public void reportCodeChange() {
        // Do nothing
      }
    }.frontendApi);

    Node root = Node.newExpressionStatement(Node.newAssign(
        Node.newGetProp(Node.newThis(), Node.newName("x")),
        Node.newCall(Node.newName("FunctionRewriter"), Node.newList(),
                     Node.newList(inputNode))));

    NodeTraversal nodeTraversal = new NodeTraversal(functionRewriter, root);
    nodeTraversal.traverse();

    assertEquals(expectedOutput, nodeTraversal.getRoot().getLastChild());
  }
}