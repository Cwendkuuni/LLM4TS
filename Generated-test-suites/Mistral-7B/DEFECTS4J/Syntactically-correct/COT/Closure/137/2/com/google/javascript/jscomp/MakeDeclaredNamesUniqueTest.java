package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import org.junit.Test;

import com.google.common.collect.ImmutableSet;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.Renamer;
import com.google.javascript.jscomp.test_util.TreeUtil;

public class MakeDeclaredNamesUniqueTest {

  @Test
  public void testMakeDeclaredNamesUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo() { var x = 1; var x = 2; }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("function foo() { var x_2 = 1; var x_1 = 2; }", TreeUtil.treeToString(node));
  }

  @Test
  public void testGlobalDeclaredNamesUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("var x = 1; var x = 2;");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("var x_1 = 1; var x_2 = 2;", TreeUtil.treeToString(node));
  }

  @Test
  public void testFunctionDeclaredNamesUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo() { var x = 1; } function foo() { var x = 2; }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("function foo() { var x_1 = 1; } function foo() { var x_2 = 2; }", TreeUtil.treeToString(node));
  }

  @Test
  public void testNestedFunctionDeclaredNamesUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo() { function bar() { var x = 1; } function bar() { var x = 2; } }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("function foo() { function bar() { var x_1 = 1; } function bar() { var x_2 = 2; } }", TreeUtil.treeToString(node));
  }

  @Test
  public void testFunctionWithParametersUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo(x) { function bar(x) { } }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("function foo(x_1) { function bar(x_2) { } }", TreeUtil.treeToString(node));
  }

  @Test
  public void testFunctionWithParametersOverwritten() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo(x) { function bar(x) { } function bar(x) { } }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("function foo(x_1) { function bar(x_1) { } function bar(x_2) { } }", TreeUtil.treeToString(node));
  }

  @Test(expected = RuntimeException.class)
  public void testFunctionWithParentFunctionDeclaredName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("function foo() { var x = 1; function foo() { } }");
    makeDeclaredNamesUnique.traverse(node, null);
  }

  @Test
  public void testCatchBlockWithDeclaredNameUnique() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("try { } catch (e) { var x = 1; } catch (e) { var x = 2; }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("try { } catch (e_1) { var x_1 = 1; } catch (e_2) { var x_2 = 2; }", TreeUtil.treeToString(node));
  }

  @Test
  public void testCatchBlockWithDeclaredNameOverwritten() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("try { } catch (e) { var x = 1; } catch (e) { var x = 2; }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("try { } catch (e_1) { var x_1 = 1; } catch (e_2) { var x_2 = 2; }", TreeUtil.treeToString(node));
  }

  @Test
  public void testCatchBlockWithParentFunctionDeclaredName() {
    MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();

    Node node = TreeUtil.parseString("try { function foo() { } } catch (e) { var x = 1; }");
    makeDeclaredNamesUnique.traverse(node, null);
    assertEquals("try { function foo_1() { } } catch (e) { var x_1 = 1; }", TreeUtil.treeToString(node));
  }
}