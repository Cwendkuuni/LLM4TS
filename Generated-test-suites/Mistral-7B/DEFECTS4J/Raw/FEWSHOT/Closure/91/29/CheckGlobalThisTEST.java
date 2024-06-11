package com.google.javascript.jscomp;

import static com.google.javascript.jscomp.CheckLevel.WARNING;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Callback;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.Token;

@RunWith(PowerMockRunner.class)
@PrepareForTest({CheckGlobalThis.class})
public class CheckGlobalThisTest {

  @Test
  public void testCheckGlobalThis() {
    AbstractCompiler compiler = PowerMockito.mock(AbstractCompiler.class);
    CheckGlobalThis checker = new CheckGlobalThis(compiler, CheckLevel.WARNING);

    Node root = new Node(Token.SCRIPT, "test", null);

    Node function1 = new Node(Token.FUNCTION, "function1", root);
    Node function1ReturnType = new Node(Token.IDENT, "function", function1);
    function1.setJSDocInfo(new JSDocInfo("function1", null, null, null, null, null, null, null, null));
    function1.setProperty(Token.FUNCTION_RETURNS, new Node(Token.TRUE, null));
    root.addChild(function1);

    Node function1Assignment = new Node(Token.ASSIGN, null, root);
    Node function1AssignmentLhs = new Node(Token.IDENT, "function1", function1Assignment);
    Node function1AssignmentRhs = new Node(Token.THIS, null, null);
    function1Assignment.setProperty(Token.ASSIGN_RHS, function1AssignmentRhs);
    function1Assignment.setProperty(Token.ASSIGN_LHS, function1AssignmentLhs);
    root.addChild(function1Assignment);

    Node function2 = new Node(Token.FUNCTION, "function2", root);
    Node function2ReturnType = new Node(Token.IDENT, "function", function2);
    function2.setJSDocInfo(new JSDocInfo("function2", null, null, null, null, null, null, null, null));
    root.addChild(function2);

    Node function2Prototype = new Node(Token.GETPROP, null, function2);
    Node function2PrototypeIdentifier = new Node(Token.IDENT, "prototype", function2Prototype);
    function2Prototype.setLastChild(function2PrototypeIdentifier);

    Node function2This = new Node(Token.THIS, null, null);
    Node function2PrototypeAssignment = new Node(Token.ASSIGN, null, function2Prototype);
    function2PrototypeAssignment.setProperty(Token.ASSIGN_RHS, function2This);
    function2PrototypeAssignment.setProperty(Token.ASSIGN_LHS, function2Prototype);
    root.addChild(function2PrototypeAssignment);

    Node function3 = new Node(Token.FUNCTION, "function3", root);
    Node function3ReturnType = new Node(Token.IDENT, "function", function3);
    function3.setJSDocInfo(new JSDocInfo("function3", null, null, null, null, null, null, null, null));
    Node function3Prototype = new Node(Token.GETPROP, null, function3);
    Node function3PrototypeIdentifier = new Node(Token.IDENT, "prototype", function3Prototype);
    function3Prototype.setLastChild(function3PrototypeIdentifier);
    Node function3This = new Node(Token.THIS, null, null);
    Node function3PrototypeAssignment = new Node(Token.ASSIGN, null, function3Prototype);
    function3PrototypeAssignment.setProperty(Token.ASSIGN_RHS, function3This);
    function3PrototypeAssignment.setProperty(Token.ASSIGN_LHS, function3Prototype);
    Node function3PrototypeProperty = new Node(Token.GETPROP, null, function3This);
    Node function3PrototypePropertyIdentifier = new Node(Token.IDENT, "x", function3PrototypeProperty);
    function3PrototypeProperty.setLastChild(function3PrototypePropertyIdentifier);
    function3PrototypeAssignment.setLastChild(function3PrototypeProperty);
    root.addChild(function3PrototypeAssignment);

    Node function4 = new Node(Token.FUNCTION, "function4", root);
    Node function4ReturnType = new Node(Token.IDENT, "function", function4);
    function4.setJSDocInfo(new JSDocInfo("function4", null, null, null, null, null, null, null, null));
    Node function4Prototype = new Node(Token.GETPROP, null, function4);
    Node function4PrototypeIdentifier = new Node(Token.IDENT, "prototype", function4Prototype);
    function4Prototype.setLastChild(function4PrototypeIdentifier);
    Node function4PrototypeProperty = new Node(Token.GETPROP, null, function4PrototypeIdentifier);
    Node function4PrototypePropertyIdentifier = new Node(Token.IDENT, "x", function4PrototypeProperty);
    function4PrototypeProperty.setLastChild(function4PrototypePropertyIdentifier);
    Node function4PrototypePropertyAssignment = new Node(Token.ASSIGN, null, function4PrototypeProperty);
    function4PrototypePropertyAssignment.setProperty(Token.ASSIGN_RHS, new Node(Token.NUMBER, "42", null));
    function4PrototypePropertyAssignment.setProperty(Token.ASSIGN_LHS, function4PrototypeProperty);
    root.addChild(function4PrototypePropertyAssignment);

    Node function5 = new Node(Token.FUNCTION, "function5", root);
    Node function5ReturnType = new Node(Token.IDENT, "function", function5);
    function5.setJSDocInfo(new JSDocInfo("function5", null, null, CheckLevel.WARNING, null, null, null, null, null));
    Node function5Prototype = new Node(Token.GETPROP, null, function5);
    Node function5PrototypeIdentifier = new Node(Token.IDENT, "prototype", function5Prototype);
    function5Prototype.setLastChild(function5PrototypeIdentifier);
    Node function5PrototypeProperty = new Node(Token.GETPROP, null, function5PrototypeIdentifier);
    Node function5PrototypePropertyIdentifier = new Node(Token.IDENT, "x", function5PrototypeProperty);
    function5PrototypeProperty.setLastChild(function5PrototypePropertyIdentifier);
    Node function5PrototypePropertyAssignment = new Node(Token.ASSIGN, null, function5PrototypeProperty);
    function5PrototypePropertyAssignment.setProperty(Token.ASSIGN_RHS, new Node(Token.NUMBER, "42", null));
    function5PrototypePropertyAssignment.setProperty(Token.ASSIGN_LHS, function5PrototypeProperty);
    root.addChild(function5PrototypePropertyAssignment);

    Node function6 = new Node(Token.FUNCTION, "function6", root);
    Node function6ReturnType = new Node(Token.IDENT, "function", function6);
    function6.setJSDocInfo(new JSDocInfo("function6", null, null, null, null, null, null, null, null));
    Node function6Prototype = new Node(Token.GETPROP, null, function6);
    Node function6PrototypeIdentifier = new Node(Token.IDENT, "prototype", function6Prototype);
    function6Prototype.setLastChild(function6PrototypeIdentifier);
    Node function6PrototypeProperty = new Node(Token.GETPROP, null, function6PrototypeIdentifier);
    Node function6PrototypePropertyIdentifier = new Node(Token.IDENT, "x", function6PrototypeProperty);
    function6PrototypeProperty.setLastChild(function6PrototypePropertyIdentifier);
    Node function6PrototypePropertyAssignment = new Node(Token.ASSIGN, null, function6PrototypeProperty);
    function6PrototypePropertyAssignment.setProperty(Token.ASSIGN_RHS, new Node(Token.IDENT, "global", null));
    function6PrototypePropertyAssignment.setProperty(Token.ASSIGN_LHS, function6PrototypeProperty);
    root.addChild(function6PrototypePropertyAssignment);

    NodeTraversal traversal = new NodeTraversal(root, checker);
    traversal.traverse();

    // Function1 assignment to this should report an error
    assertTrue(checker.level.isEnabled(WARNING));
    int function1ErrorIndex = root.getChildIndex(function1Assignment);
    assertEquals(1, root.getChildCount());
    assertEquals(1, function1ErrorIndex);
    assertEquals("JSC_USED_GLOBAL_THIS", root.getChild(function1ErrorIndex).getError().getCode());

    // Function2 prototype assignment to this should report an error
    int function2ErrorIndex = root.getChildIndex(function2PrototypeAssignment);
    assertEquals(2, root.getChildCount());
    assertEquals(1, function2ErrorIndex);
    assertEquals("JSC_USED_GLOBAL_THIS", root.getChild(function2ErrorIndex).getError().getCode());

    // Function3 prototype assignment to this should report an error
    int function3ErrorIndex = root.getChildIndex(function3PrototypeAssignment);
    assertEquals(3, root.getChildCount());
    assertEquals(1, function3ErrorIndex);
    assertEquals("JSC_USED_GLOBAL_THIS", root.getChild(function3ErrorIndex).getError().getCode());

    // Function5 prototype assignment to this should not report an error
    int function5ErrorIndex = root.getChildIndex(function5PrototypePropertyAssignment);
    assertEquals(3, root.getChildCount());
    assertEquals(2, function5ErrorIndex);
    assertNull(root.getChild(function5ErrorIndex).getError());

    // Function6 prototype assignment to global this should report an error
    int function6ErrorIndex = root.getChildIndex(function6PrototypePropertyAssignment);
    assertEquals(4, root.getChildCount());
    assertEquals(1, function6ErrorIndex);
    assertEquals("JSC_USED_GLOBAL_THIS", root.getChild(function6ErrorIndex).getError().getCode());
  }

  private void assertEquals(String expected, Node actual) {
    assertEquals(expected, actual.getString());
  }

  private void assertEquals(int expected, int actual) {
    assertEquals(expected, actual);
  }

  private void assertEquals(Node expected, Node actual) {
    assertEquals(expected, actual);
  }
}