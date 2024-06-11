package com.google.javascript.jscomp.test;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static com.google.javascript.jscomp.NodeTraversal.AbstractVisitor.SYNCHRONOUS;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

public class CheckGlobalThisTest {
  private static AbstractCompiler createCompiler() {
    return new Compiler();
  }

  @Test
  public void testShouldTraverse() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);
    NodeTraversal traversal = new NodeTraversal(checker, SYNCHRONOUS);

    Node root = new Node(Token.SCRIPT, null);

    // Don't traverse scripts
    assertTrue(checker.shouldTraverse(traversal, root, null));

    Node functionNoJsDoc = new Node(Token.FUNCTION, null);
    Node functionWithJsDoc = new Node(Token.FUNCTION, null);
    functionWithJsDoc.setJSDocInfo(new JSDocInfo("functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));

    functionNoJsDoc.setFirstChild(new Node(Token.IDENT, "functionName"));
    functionWithJsDoc.setFirstChild(functionNoJsDoc);

    assertTrue(checker.shouldTraverse(traversal, functionWithJsDoc, root));
    assertFalse(checker.shouldTraverse(traversal, functionNoJsDoc, root));

    assertTrue(checker.shouldTraverse(traversal, functionNoJsDoc, new Node(Token.BLOCK, null)));
    assertFalse(checker.shouldTraverse(traversal, functionNoJsDoc, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.FUNCTION, null))));

    Node assign = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.THIS, null));
    assertTrue(checker.shouldTraverse(traversal, assign, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), root)));
  }

  @Test
  public void testVisit() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);
    NodeTraversal traversal = new NodeTraversal(checker, SYNCHRONOUS);

    Node root = new Node(Token.SCRIPT, null);

    Node functionNoJsDoc = new Node(Token.FUNCTION, null);
    functionNoJsDoc.setFirstChild(new Node(Token.IDENT, "functionName"));

    Node functionWithJsDoc = new Node(Token.FUNCTION, null);
    functionWithJsDoc.setJSDocInfo(new JSDocInfo("functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionWithJsDoc.setFirstChild(functionNoJsDoc);

    Node assign = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.THIS, null));

    Node script = new Node(Token.SCRIPT, null);
    script.setFirstChild(functionWithJsDoc);
    script.setLastChild(assign);

    Node traversedNodes = new Node(Token.BLOCK, null);
    traversal.traverse(script, traversedNodes, null);

    assertFalse(traversedNodes.getFirstChild().equals(functionNoJsDoc));
    assertFalse(traversedNodes.getLastChild().equals(assign));

    assertTrue(traversedNodes.getFirstChild().equals(functionWithJsDoc));
  }

  @Test
  public void testShouldReportThis() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);

    Node assign = new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.THIS, null));

    assertTrue(checker.shouldReportThis(assign, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.THIS, null))));
    assertFalse(checker.shouldReportThis(assign, new Node(Token.ASSIGN, null, new Node(Token.IDENT, "a"), new Node(Token.IDENT, "b"))));
  }

  @Test
  public void testGetFunctionJsDocInfo() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);

    Node functionNoJsDoc = new Node(Token.FUNCTION, null);
    functionNoJsDoc.setFirstChild(new Node(Token.IDENT, "functionName"));

    Node functionWithJsDoc = new Node(Token.FUNCTION, null);
    functionWithJsDoc.setJSDocInfo(new JSDocInfo("functionName", null, null, null, null, null, null, null, null, null, null, null, null, null, null, null));
    functionWithJsDoc.setFirstChild(functionNoJsDoc);

    JSDocInfo result = checker.getFunctionJsDocInfo(functionWithJsDoc);
    assertEquals(result, functionWithJsDoc.getJSDocInfo());

    result = checker.getFunctionJsDocInfo(functionNoJsDoc);
    assertNull(result);
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeProperty() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);

    Node function = new Node(Token.FUNCTION, null);
    function.setFirstChild(new Node(Token.IDENT, "functionName"));

    Node prototype = new Node(Token.GETPROP, null, new Node(Token.IDENT, "functionName"), new Node(Token.STRING, "prototype"));
    function.setLastChild(prototype);

    Node script = new Node(Token.SCRIPT, null);
    script.setFirstChild(function);

    assertFalse(checker.shouldTraverse(node -> node, script, null));
  }

  @Test
  public void testShouldTraverseFunctionWithPrototypeSubproperty() {
    CheckGlobalThis checker = new CheckGlobalThis(createCompiler(WARNING), CHECK_LEVEL_WARNING);

    Node function = new Node(Token.FUNCTION, null);
    function.setFirstChild(new Node(Token.IDENT, "functionName"));

    Node prototype = new Node(Token.GETPROP, null, new Node(Token.IDENT, "functionName"), new Node(Token.STRING, "prototype"));
    Node subproperty = new Node(Token.GETPROP, null, prototype, new Node(Token.STRING, "subProperty"));
    function.setLastChild(subproperty);

    Node script = new Node(Token.SCRIPT, null);
    script.setFirstChild(function);

    assertFalse(checker.shouldTraverse(node -> node, script, null));
  }

  private AbstractCompiler createCompiler(CheckLevel level) {
    return new Compiler() {
      @Override
      public CheckLevel getLevel() {
        return level;
      }
    };
  }

  private static class Compiler extends AbstractCompiler {
    @Override
    public DiagnosticType getDefaultDiagnosticType() {
      return DiagnosticType.error(JSC_UNUSED_EXPRESSION, "unreachable statement");
    }
  }

  private static JSDocInfo assertEquals(JSDocInfo expected, JSDocInfo actual) {
    if (expected == null) {
      assertNull(actual);
    } else {
      assertEquals(expected.getName(), actual.getName());
      assertEquals(expected.getCallSignature(), actual.getCallSignature());
      assertEquals(expected.getReturnType(), actual.getReturnType());
      assertEquals(expected.getParameters(), actual.getParameters());
      assertEquals(expected.getAnnotations(), actual.getAnnotations());
      assertEquals(expected.getAccess(), actual.getAccess());
      assertEquals(expected.getStatic(), actual.getStatic());
      assertEquals(expected.getFinal(), actual.getFinal());
      assertEquals(expected.getDescription(), actual.getDescription());
      assertEquals(expected.getJsDocVersion(), actual.getJsDocVersion());
    }

    return actual;
  }

  private static void assertEquals(Object expected, Object actual) {
    if (expected == null) {
      assertNull(actual);
    } else if (expected instanceof Number && actual instanceof Number) {
      assertEquals((int) expected, (int) actual);
    } else if (expected instanceof String && actual instanceof String) {
      assertEquals((String) expected, (String) actual);
    } else if (expected instanceof Node && actual instanceof Node) {
      assertEqualsNode((Node) expected, (Node) actual);
    } else {
      fail("Unexpected comparison type: " + expected.getClass().getName());
    }
  }

  private static void assertEqualsNode(Node expected, Node actual) {
    assertEquals(expected.getType(), actual.getType());
    assertEquals(expected.getFirstChild(), actual.getFirstChild());
    assertEquals(expected.getLastChild(), actual.getLastChild());
  }
}