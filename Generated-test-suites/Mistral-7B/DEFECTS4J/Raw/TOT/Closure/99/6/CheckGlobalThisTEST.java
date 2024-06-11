package com.google.javascript.jscomp.CheckGlobalThisTest;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.Checker;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerOptions;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.TestCompiler;
import com.google.javascript.rhino.JSDocInfo;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest {

  private static Node createFunctionWithThis(NodeFactory factory) {
    Node n = factory.createName("function");
    n = NodeUtil.setState(n, factory, Node.STATE_IS_FUNCTION_DEF);
    Node body = factory.createBlock();
    Node thisAccess = factory.createGetProp(factory.createThis(), "toString");
    Node returnNode = factory.createReturn(thisAccess);
    body.addChildren(returnNode);
    n.setBody(body);
    return n;
  }

  private static Node createAssignmentWithThis(NodeFactory factory) {
    Node lhs = factory.createName("a");
    Node rhs = factory.createThis();
    Node assign = factory.createAssign(lhs, rhs);
    return assign;
  }

  private static Node createPrototypePropertyAssignment(NodeFactory factory) {
    Node obj = factory.createObjectLiteral();
    Node prototype = factory.createGetProp(factory.createThis(), "prototype");
    Node prototypeProperty = factory.createName("x");
    Node rightSide = factory.createObjectLiteral();
    Node assignment = factory.createAssign(prototype, prototypeProperty, rightSide);
    obj.addChildren(assignment);
    return obj;
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithConstructor() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node node = createFunctionWithThis(factory);
    node = NodeUtil.setJSDocInfo(node, factory, new JSDocInfo("@constructor"));
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithThis() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node node = createFunctionWithThis(factory);
    node = NodeUtil.setJSDocInfo(node, factory, new JSDocInfo("@this", new String[] {"global"}));
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithNoAnnotation() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node node = createFunctionWithThis(factory);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(node);
    Node diagnostic = traversal.getDiagnostics().get(0);
    assertTrue(diagnostic.getError().getCode().equals("JSC_USED_GLOBAL_THIS"));
  }

  @Test
  public void testCheckGlobalThis_AssignmentToThis() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node lhs = factory.createName("a");
    Node rhs = createAssignmentWithThis(factory);
    Node assign = factory.createAssign(lhs, rhs);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(assign);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_PrototypePropertyAssignment() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node node = createPrototypePropertyAssignment(factory);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(node);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_AssignmentToPrototypeProperty() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node obj = factory.createObjectLiteral();
    Node prototype = factory.createGetProp(factory.createThis(), "prototype");
    Node prototypeProperty = factory.createName("x");
    Node assign = factory.createAssign(prototypeProperty, factory.createNumber(42));
    obj.addChildren(prototype, assign);
    Node lhs = factory.createGetProp(obj, "prototype", prototypeProperty);
    Node rhs = factory.createThis();
    Node assign2 = factory.createAssign(lhs, rhs);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(assign2);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithinPrototypeProperty() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node obj = factory.createObjectLiteral();
    Node prototype = factory.createGetProp(factory.createThis(), "prototype");
    Node prototypeProperty = factory.createName("x");
    Node functionNode = factory.createFunction(null, null, null);
    Node functionBody = factory.createBlock();
    Node thisAccess = factory.createGetProp(factory.createThis(), "toString");
    Node returnNode = factory.createReturn(thisAccess);
    functionBody.addChildren(returnNode);
    functionNode.setBody(functionBody);
    obj.addChildren(prototype, prototypeProperty, functionNode);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(obj);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionWithinPrototypeMethod() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node obj = factory.createObjectLiteral();
    Node prototype = factory.createGetProp(factory.createThis(), "prototype");
    Node prototypeProperty = factory.createName("x");
    Node functionNode = factory.createFunction(null, null, new String[] {"a"});
    Node functionBody = factory.createBlock();
    Node thisAccess = factory.createGetProp(factory.createThis(), "toString");
    Node getProp = factory.createGetProp(factory.createThis(), "x");
    Node add = factory.createAdd(thisAccess, getProp);
    Node returnNode = factory.createReturn(add);
    functionBody.addChildren(returnNode);
    functionNode.setBody(functionBody);
    obj.addChildren(prototype, prototypeProperty, functionNode);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(obj);
    assertTrue(traversal.getDiagnostics().isEmpty());
  }

  @Test
  public void testCheckGlobalThis_FunctionAnnotatedWithNoLogicalPlace() {
    TestCompiler tc = new TestCompiler();
    CompilerOptions options = new CompilerOptions();
    CheckLevel level = CheckLevel.WARNING;
    Checker checker = new Checker(tc.getContext(), options, level);
    AbstractCompiler compiler = checker.getCompiler();
    NodeFactory factory = compiler.getFactory();
    Node node = factory.createFunction(null, null, null);
    Node body = factory.createBlock();
    Node thisAccess = factory.createGetProp(factory.createThis(), "toString");
    Node returnNode = factory.createReturn(thisAccess);
    body.addChildren(returnNode);
    node.setBody(body);
    NodeTraversal traversal = new NodeTraversal(compiler, null, null);
    traversal.traverse(node);
    assertFalse(traversal.getDiagnostics().isEmpty());
  }
}