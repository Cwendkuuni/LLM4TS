package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testContextualRenamer() {
    String inputCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "function bar() {" +
        "var z = 3, x = 4;" +
        "}" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = new TokenStream(new TokenStream.ErrorReporter() {}).parse(inputCode, null, null);
    AbstractCompiler compiler = new Compiler();
    compiler.setErrorReporter(new Compiler.DefaultErrorReporter());
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, node);
    nodeTraversal.traverse(makeDeclaredNamesUnique);

    Set<String> expectedNames = new HashSet<>();
    expectedNames.add("x");
    expectedNames.add("y");
    expectedNames.add("z");

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(expectedNames.contains(name));
      }
    }
  }

  @Test
  public void testInlineRenamer() {
    String inputCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "function bar() {" +
        "var z = 3, x = 4;" +
        "}" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique(new InlineRenamer(
        () -> "_" + Integer.toString(System.identityHashCode(this)), "_renamed_", true));

    Node node = new TokenStream(new TokenStream.ErrorReporter() {}).parse(inputCode, null, null);
    AbstractCompiler compiler = new Compiler();
    compiler.setErrorReporter(new Compiler.DefaultErrorReporter());
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, node);
    nodeTraversal.traverse(makeDeclaredNamesUnique);

    for (Node child = node.getFirstChild(); child != null; child = child.getNext()) {
      if (child.getType() == Token.NAME) {
        String name = child.getString();
        assertTrue(name.matches("^[^_]++_renamed_[0-9]+$"));
      }
    }
  }

  @Test
  public void testContextualRenamerInverter() {
    String inputCode = "function foo() {" +
        "var x = 1, y = 2;" +
        "function bar() {" +
        "var z = 3, x = 4;" +
        "}" +
        "}";

    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    AbstractCompiler compiler = new Compiler();
    compiler.setErrorReporter(new Compiler.DefaultErrorReporter());
    Node node = new TokenStream(new TokenStream.ErrorReporter() {}).parse(inputCode, null, null);
    NodeTraversal nodeTraversal = new NodeTraversal(compiler, node);
    nodeTraversal.traverse(makeDeclaredNamesUnique);

    ContextualRenameInverter contextualRenameInverter = new ContextualRenameInverter(compiler);
    contextualRenameInverter.process(new Node(), node);

    assertEquals("function foo() {" +
        "var x = 1, y = 2;" +
        "function bar() {" +
        "var z = 3, x = 4;" +
        "}" +
        "}", node.toString());
  }
}