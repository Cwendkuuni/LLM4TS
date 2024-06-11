package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

public class MakeDeclaredNamesUniqueTest {

  private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
  private Renamer rootRenamer;

  @Before
  public void setUp() {
    makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
    rootRenamer = makeDeclaredNamesUnique.rootRenamer;
  }

  @Test
  public void testEmpty() {
    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader("")), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);
    assertTrue(makeDeclaredNamesUnique.nameStack.isEmpty());
  }

  @Test
  public void testGlobalScope() {
    String code = "function foo() {} var x = 1; function bar() {} var y = 1; function qux() {}";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(3, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo", fooRenamer.getReplacementName("foo"));
    assertNull(fooRenamer.getReplacementName("x"));
    assertNull(fooRenamer.getReplacementName("y"));
    assertEquals("bar", nameStack.get(1).getReplacementName("bar"));
    assertNull(nameStack.get(1).getReplacementName("x"));
    assertNull(nameStack.get(1).getReplacementName("y"));
    assertEquals("qux", nameStack.get(2).getReplacementName("qux"));
    assertNull(nameStack.get(2).getReplacementName("x"));
    assertNull(nameStack.get(2).getReplacementName("y"));
  }

  @Test
  public void testFunctionScope() {
    String code = "function foo() { var x = 1; function bar() {} }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(2, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertNull(fooRenamer.getReplacementName("x"));
    assertEquals("bar$0", nameStack.get(1).getReplacementName("bar"));
    assertNull(nameStack.get(1).getReplacementName("x"));
  }

  @Test
  public void testFunctionScopeWithArguments() {
    String code = "function foo(x, y) { var z = 1; function bar(a, b) {} }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(4, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertNull(fooRenamer.getReplacementName("x"));
    assertEquals("x$0", fooRenamer.getReplacementName("x"));
    assertNull(fooRenamer.getReplacementName("y"));
    assertEquals("y$0", fooRenamer.getReplacementName("y"));
    assertEquals("z$0", nameStack.get(1).getReplacementName("z"));
    assertEquals("bar$1", nameStack.get(2).getReplacementName("bar"));
    assertNull(nameStack.get(2).getReplacementName("a"));
    assertNull(nameStack.get(2).getReplacementName("b"));
  }

  @Test
  public void testFunctionWithRecursiveCall() {
    String code = "function foo(x) { if (x > 0) { return foo(x - 1); } }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(1, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
  }

  @Test
  public void testFunctionInsideFunction() {
    String code = "function foo() { function bar() {} }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(1, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertEquals("bar$0", fooRenamer.getReplacementName("bar"));
  }

  @Test
  public void testVariablesWithSameNameInDifferentScopes() {
    String code = "function foo() { var x = 1; } function bar() { var x = 2; }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(2, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertNull(fooRenamer.getReplacementName("x"));
    assertEquals("x$1", nameStack.get(1).getReplacementName("x"));
  }

  @Test
  public void testVariablesWithSameNameInSameScope() {
    String code = "function foo() { var x = 1; var x = 2; }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(1, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0$1", fooRenamer.getReplacementName("x"));
  }

  @Test
  public void testFunctionWithCatchClause() {
    String code = "function foo() { try { throw new Error(); } catch (e) {} }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(1, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertEquals("e$0", fooRenamer.getReplacementName("e"));
  }

  @Test
  public void testFunctionWithMultipleCatchClauses() {
    String code = "function foo() { try { throw new Error(); } catch (e1) {} catch (e2) {} }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(2, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertEquals("e1$0", fooRenamer.getReplacementName("e1"));
    assertEquals("e2$0", nameStack.get(1).getReplacementName("e2"));
  }

  @Test
  public void testFunctionWithVariableInsideCatchClause() {
    String code = "function foo() { try { var x = 1; throw new Error(); } catch (e) { var y = x; } }";

    NodeTraversal nodeTraversal = new NodeTraversal(new TokenStream(new StringReader(code)), null, null);
    makeDeclaredNamesUnique.traverse(nodeTraversal);

    List<Renamer> nameStack = makeDeclaredNamesUnique.nameStack;
    assertEquals(2, nameStack.size());
    Renamer fooRenamer = nameStack.get(0);
    assertEquals("foo$0", fooRenamer.getReplacementName("foo"));
    assertEquals("x$0", fooRenamer.getReplacementName("x"));
    assertEquals("e$1", nameStack.get(1).getReplacementName("e"));
    assertEquals("y$1", nameStack.get(1).getReplacementName("y"));
  }
}

class StringReader extends java.io.Reader {
  private final String string;

  public StringReader(String string) {
    this.string = string;
  }

  @Override
  public int read(char[] cbuf, int off, int len) throws IOException {
    return string.regionMatches(true, off, cbuf, 0, Math.min(len, string.length() - off)) ? len : -1;
  }

  @Override
  public void close() throws IOException {
  }
}