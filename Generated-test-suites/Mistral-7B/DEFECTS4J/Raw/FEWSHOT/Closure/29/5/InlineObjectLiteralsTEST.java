package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

import org.junit.Test;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Behavior;
import com.google.javascript.jscomp.ReferenceCollectingCallback.Reference;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceCollection;
import com.google.javascript.jscomp.ReferenceCollectingCallback.ReferenceMap;
import com.google.javascript.rhino.Node;

public class InlineObjectLiteralsTest {

  @Test
  public void testInlineObjectLiterals() {
    // Given a simple object literal assignment
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  return obj.a;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has been inlined
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    Node varNode = NodeUtil.findFirstChildByPropertyName(block, "var");
    Node objNode = varNode.getNextSibling();
    Node objDef = objNode.getFirstChild();

    List<Node> assignments = Lists.newArrayList();
    Node cur = objDef;
    while (cur != null && cur.isAssign()) {
      assignments.add(cur);
      cur = cur.getNextSibling();
    }

    // Check that there is exactly one object literal assignment
    assertEquals(1, assignments.size());

    // Check that each variable has been assigned a value
    for (Node assignment : assignments) {
      Node leftHandSide = assignment.getFirstChild();
      Node rightHandSide = assignment.getNextSibling();

      // Check that the left-hand side is a simple variable declaration
      assertTrue(leftHandSide.isVar());

      // Check that the right-hand side is an object literal
      assertTrue(rightHandSide.isObjectLit());

      // Check that each key-value pair in the object literal has been
      // expanded into a separate variable assignment
      for (Node child = rightHandSide.getFirstChild(); child != null;
           child = child.getNext()) {
        String key = child.getString();
        Node keyNode = NodeUtil.newNameNode(key, null);
        Node valueNode = child.getNextSibling();

        Node varName = NodeUtil.findFirstChildByPropertyName(
            assignment, "VarDeclList")
            .getNextSibling()
            .getNextSibling();

        // Check that the variable has the correct name
        assertEquals(key, varName.getString());

        // Check that the variable has been assigned the correct value
        Node varValue = NodeUtil.findFirstChildByPropertyName(
            varName, "Initializer")
            .getFirstChild();
        assertEquals(valueNode.getFirstChild().getNumberValue(),
            varValue.getFirstChild().getNumberValue());
      }
    }

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    Node expectedReturnNode = NodeUtil.newNameNode("a", null);
    assertEquals(expectedReturnNode, returnNode);

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithSelfReference() {
    // Given a self-referential object literal assignment
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: function() { return obj.a; }" +
        "  };" +
        "  return obj.b();" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has not been inlined because of self-reference
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    Node expectedReturnNode = NodeUtil.newCallExprNode(
        NodeUtil.newNameNode("b", null),
        Lists.newArrayList(NodeUtil.newNameNode("a", null)));
    assertEquals(expectedReturnNode, returnNode);

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithCyclicReferences() {
    // Given a cyclic object literal assignment
    String sourceCode = "function f() {" +
        "  var obj1 = {" +
        "    a: 1," +
        "    b: obj2," +
        "  };" +
        "  var obj2 = {" +
        "    a: obj1," +
        "  };" +
        "  return obj1.a;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has not been inlined because of cyclic references
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    assertEquals(1, returnNode.getNumberValue());

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithNonAssignableReference() {
    // Given an object literal assignment with a non-assignable reference
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    getA: function() { return 1; }," +
        "    getB: function() { return obj.getA(); }" +
        "  };" +
        "  return obj.getB();" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has not been inlined because of non-assignable reference
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    assertEquals(1, returnNode.getNumberValue());

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithPropertyAccessInInitExpression() {
    // Given an object literal assignment with a property access in the initializer expression
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: { c: 1 }" +
        "  };" +
        "  return obj.b.c;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has been inlined with property access in the initializer expression
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    assertEquals(1, returnNode.getNumberValue());

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithGettersAndSetters() {
    // Given an object literal assignment with getters and setters
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    getA: function() { return 1; }," +
        "    setA: function(value) { }, " +
        "    getB: function() { return 1; }," +
        "    setB: function(value) { }, " +
        "    a: 1," +
        "    b: 2" +
        "  };" +
        "  obj.setA(3);" +
        "  obj.a = 4;" +
        "  obj.setB(5);" +
        "  obj.b = 6;" +
        "  return obj.a;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has been inlined with getters and setters
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    assertEquals(4, returnNode.getNumberValue());

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }

  @Test
  public void testInlineObjectLiteralWithMultipleAssignments() {
    // Given an object literal assignment with multiple assignments
    String sourceCode = "function f() {" +
        "  var obj = {" +
        "    a: 1," +
        "    b: { c: 1, d: 2 }" +
        "  };" +
        "  obj.a = 2;" +
        "  obj.b.c = 3;" +
        "  obj.b.d = 4;" +
        "  return obj.a;" +
        "}";

    AbstractCompiler compiler = new Compiler();
    InlineObjectLiterals pass = new InlineObjectLiterals(compiler, () -> null);

    Node root = AbstractCompiler.parseAndCompile(sourceCode, null);
    pass.process(null, root);

    // When we run the test
    Node result = compiler.getOptimizedCode();

    // Then the object literal has been inlined with multiple assignments
    Node expectedResult = new Node(Token.FUNCTION);
    expectedResult.addChildToFront(NodeUtil.newNameNode("f", null));
    expectedResult.addChildToFront(NodeUtil.newBlockNode());

    Node function = result.getFirstChild();
    Node block = function.getFirstChild();

    // Check that the function returns the correct value
    Node returnNode = NodeUtil.findFirstChildByPropertyName(
        block, "ReturnStatement")
        .getFirstChild();
    assertEquals(2, returnNode.getNumberValue());

    // Check that there are no stale variables
    ReferenceMap referenceMap = pass.getCallback().getReferenceMap();
    Set<Var> staleVars = pass.getCallback().getBehavior().getStaleVars();
    assertEquals(0, staleVars.size());
  }
}