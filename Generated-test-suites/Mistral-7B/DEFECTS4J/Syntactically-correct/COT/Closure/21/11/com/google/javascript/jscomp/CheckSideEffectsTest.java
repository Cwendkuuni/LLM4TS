package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.rhino.Token.*;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import com.google.common.collect.Lists;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CheckerFactory;
import com.google.javascript.jscomp.Compiler;
import com.google.javascript.jscomp.DefaultCheckerFactory;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeOperations;
import org.junit.Before;
import org.junit.Test;
import org.mockito.MockitoAnnotations;

import java.util.List;

/**
 * Test class for CheckSideEffects class.
 */
public class CheckSideEffectsTest {

  private static final String PROTECTOR_FN = "JSCOMPILER_PRESERVE";
  private Compiler compiler;
  private CheckerFactory checkerFactory;
  private Parser parser;

  @Before
  public void setUp() {
    MockitoAnnotations.initMocks(this);
    checkerFactory = new DefaultCheckerFactory();
    compiler = checkerFactory.getCheckerToCompile(new String[]{"test.js"}, new CheckLevel[]{WARNING});
    parser = new Parser(compiler);
  }

  /**
   * Test method for process() of CheckSideEffects class.
   */
  @Test
  public void testProcess() {
    String testCode =
        "function test() {" +
            "var s = \"this string is \";" +
            "var a = [];" +
            "for (var i = 0; i < 10; i++) {" +
            "  a[i] = i + 1;" +
            "}" +
            "s += 'continued on the next line but you forgot the +';" +
            "x == foo();" +
            "foo();;" +
            "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  /**
   * Test method for visit() of CheckSideEffects class with simple assignments.
   */
  @Test
  public void testSimpleAssignments() {
    String testCode = "function test() {" +
        "var a = 1;" +
        "var b = 2;" +
        "var c = a;" +
        "a = b;" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  /**
   * Test method for visit() of CheckSideEffects class with useless code.
   */
  @Test
  public void testUselessCode() {
    String testCode = "function test() {" +
        "x = 5;" +
        ";;" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(EXPR_STATEMENT, problemNode.getType());
  }

  /**
   * Test method for visit() of CheckSideEffects class with side effect free code.
   */
  @Test
  public void testSideEffectFreeCode() {
    String testCode = "function test() {" +
        "return 2 * 3;" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  /**
   * Test method for visit() of CheckSideEffects class with stray semicolons.
   */
  @Test
  public void testStraySemicolons() {
    String testCode = "function test() {" +
        "x = 5;" +
        ";;" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(BLOCK, problemNode.getType());
  }

  /**
   * Test method for visit() of CheckSideEffects class with for loop.
   */
  @Test
  public void testForLoop() {
    String testCode = "function test() {" +
        "for (var i = 0; i < 10; i++) {" +
        "}" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);
    NodeTraversal.traverse(compiler, ast, checkSideEffects);
    assertEquals(0, checkSideEffects.problemNodes.size());
  }

  /**
   * Test method for protectSideEffects() of CheckSideEffects class.
   */
  @Test
  public void testProtectSideEffects() {
    String testCode = "function test() {" +
        "var s = \"this string is \";" +
        "s += 'continued on the next line but you forgot the +';" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    CheckSideEffects checkSideEffects = new CheckSideEffects(compiler, new CheckLevel(WARNING), true);

    checkSideEffects.protectSideEffects();

    assertEquals(1, checkSideEffects.problemNodes.size());

    Node problemNode = checkSideEffects.problemNodes.get(0);
    assertEquals(STRING_LITERAL, problemNode.getType());

    Node replacement = checkSideEffects.problemNodes.get(0).getLastChild();
    assertEquals(CALL_EXPRESSION, replacement.getType());
  }

  /**
   * Test method for StripProtection class.
   */
  @Test
  public void testStripProtection() {
    String testCode = "function test() {" +
        "function JSCOMPILER_PRESERVE() {" +
        "  return 1;" +
        "}" +
        "JSCOMPILER_PRESERVE();" +
        "}";
    Node ast = parser.parse(testCode, null, null);
    StripProtection stripProtection = new StripProtection(compiler);
    NodeTraversal.traverse(compiler, ast, stripProtection);
    assertEquals(0, ast.getChildCount());
  }
}