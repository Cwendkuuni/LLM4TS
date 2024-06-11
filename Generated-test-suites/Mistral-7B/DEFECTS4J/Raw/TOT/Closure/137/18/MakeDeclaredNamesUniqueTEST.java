package com.google.javascript.jscomp.test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.MakeDeclaredNamesUnique;
import com.google.javascript.jscomp.NodeTraversal.AbstractPostOrderCallback;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;
import org.junit.Test;
import static org.junit.Assert.*;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    String jsCode = "function f() {" +
        "  var a = 1;" +
        "  function g() {" +
        "    var a = 2;" +
        "  }" +
        "  var b = 3;" +
        "  try {" +
        "    var c = 4;" +
        "  } catch (e) {" +
        "    var c = 5;" +
        "  }" +
        "  function h() {" +
        "    var a = 6;" +
        "  }" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    List<String> expectedUniqueNames = Lists.newArrayList(
        "a_0",
        "g_0_a_1",
        "b_0",
        "c_0",
        "h_0_a_2");

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    assertEquals(expectedUniqueNames.size(), actualUniqueNames.size());
    Iterator<String> iterator = expectedUniqueNames.iterator();
    for (String expectedUniqueName : expectedUniqueNames) {
      String actualUniqueName = iterator.next();
      assertTrue(actualUniqueNames.contains(actualUniqueName));
      assertEquals(expectedUniqueName, actualUniqueName);
    }
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithGlobalVariables() {
    String jsCode = "var a = 1;" +
        "function f() {" +
        "  var a = 2;" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    ImmutableSet<String> expectedUniqueNames = ImmutableSet.of("a_1", "f_0_a_2");
    assertEquals(expectedUniqueNames.size(), actualUniqueNames.size());
    for (String expectedUniqueName : expectedUniqueNames) {
      assertTrue(actualUniqueNames.contains(expectedUniqueName));
    }
  }

  @Test
  public void testMakeDeclaredNamesUniqueWithGlobalFunction() {
    String jsCode = "function f() {" +
        "  return 1;" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    assertTrue(actualUniqueNames.isEmpty());
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithObjectProperty() {
    String jsCode = "var o = {" +
        "  a: 1" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    fail("Object properties should not be renamed.");
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithInvalidName() {
    String jsCode = "function f() {" +
        "  var 1a = 1;" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    fail("Invalid names should not be renamed.");
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithDuplicateNameInGlobalScope() {
    String jsCode = "var a = 1;" +
        "function f() {" +
        "  var a = 2;" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    fail("Duplicate global names should cause an error.");
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithDuplicateNameInFunctionScope() {
    String jsCode = "function f() {" +
        "  var a = 1;" +
        "  function g() {" +
        "    var a = 2;" +
        "  }" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    fail("Duplicate function names should cause an error.");
  }

  @Test(expected = AssertionError.class)
  public void testMakeDeclaredNamesUniqueWithDuplicateNameInBlockScope() {
    String jsCode = "function f() {" +
        "  var a = 1;" +
        "  try {" +
        "    var a = 2;" +
        "  } catch (e) {" +
        "    var a = 3;" +
        "  }" +
        "}";

    Node ast = new TokenStream(jsCode).parse();
    MakeDeclaredNamesUnique renamer = new MakeDeclaredNamesUnique();
    NodeTraversal traverser = new NodeTraversal(ast);
    traverser.setScopedCallback(renamer);
    traverser.traverse();

    Set<String> actualUniqueNames = Sets.newHashSet();
    for (Node node = ast.getFirstChild(); node != null; node = node.getNext()) {
      if (node.getType() == Token.NAME) {
        String uniqueName = ((MakeDeclaredNamesUnique) renamer).getReplacementName(node.getString());
        actualUniqueNames.add(uniqueName);
      }
    }

    fail("Duplicate block names should cause an error.");
  }
}