package com.google.javascript.jscomp.CheckGlobalThis;

import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.CompilerTestCase;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.Parser;
import com.google.javascript.rhino.NodeFactory;

public class CheckGlobalThisTest extends CompilerTestCase {

  private CheckGlobalThis checker;
  private AbstractCompiler compiler;
  private NodeFactory nodeFactory;
  private Parser parser;

  @Before
  public void setUp() {
    checker = new CheckGlobalThis(mock(AbstractCompiler.class), CheckLevel.WARNING);
    compiler = mock(AbstractCompiler.class);
    nodeFactory = new NodeFactory();
    parser = new Parser(nodeFactory, getSourceFile("test.js"));
    parser.parse(getSourceFile("test.js"));

    when(compiler.report).thenReturn(mock(VoidFunction.class));
  }

  @Test
  public void testSafeThisUsage() {
    Node functionNode = parser.getTree().getFirstChild().getLastChild();
    NodeTraversal traversal = createTraversal(functionNode, checker);
    checker.traverse(traversal, parser.getTree(), null);
    assertTrue("No warnings expected for safe 'this' usage.", checker.getNumWarnings() == 0);
  }

  @Test
  public void testUnsafePrototypePropertyAccess() {
    Node functionNode = parser.getTree().getFirstChild().getLastChild();
    Node objectNode = nodeFactory.createName("obj");
    Node propertyNode = nodeFactory.createGetProp(objectNode, "prototype");
    Node assignNode = nodeFactory.createAssign(propertyNode, functionNode);
    Node assignLhsNode = assignNode.getFirstChild();

    NodeTraversal traversal = createTraversal(assignLhsNode, checker);
    checker.traverse(traversal, parser.getTree(), null);
    assertTrue("One warning expected for unsafe prototype property access.", checker.getNumWarnings() == 1);
  }

  @Test
  public void testUnsafeLocalVariableAssignment() {
    Node functionNode = parser.getTree().getFirstChild().getLastChild();
    Node assignNode = nodeFactory.createAssign(
        nodeFactory.createName("a"), functionNode);
    Node thisNode = nodeFactory.createThis();

    NodeTraversal traversal = createTraversal(thisNode, checker);
    checker.traverse(traversal, parser.getTree(), null);
    assertTrue("One warning expected for unsafe local variable assignment.", checker.getNumWarnings() == 1);
  }

  @Test
  public void testNoWarningForConstructorOrFunctionAnnotatedWithThis() {
    String constructorCode = "function MyConstructor() { this.x = 1; }";
    Parser constructorParser = new Parser(nodeFactory, getSourceFile("MyConstructor.js"));
    constructorParser.parse(getSourceFile("MyConstructor.js"));

    CheckGlobalThis checkerForConstructor = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node constructorNode = constructorParser.getTree().getFirstChild();
    NodeTraversal constructorTraversal = createTraversal(constructorNode, checkerForConstructor);
    checkerForConstructor.traverse(constructorTraversal, constructorParser.getTree(), null);
    assertTrue("No warnings expected for a constructor.", checkerForConstructor.getNumWarnings() == 0);

    String functionAnnotatedWithThisCode = "/** @this {MyConstructor} */ function myFunction() { this.x = 1; }";
    Parser functionAnnotatedWithThisParser = new Parser(nodeFactory, getSourceFile("myFunction.js"));
    functionAnnotatedWithThisParser.parse(getSourceFile("myFunction.js"));

    CheckGlobalThis checkerForFunctionAnnotatedWithThis = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionAnnotatedWithThisNode = functionAnnotatedWithThisParser.getTree().getFirstChild();
    NodeTraversal functionAnnotatedWithThisTraversal = createTraversal(functionAnnotatedWithThisNode, checkerForFunctionAnnotatedWithThis);
    checkerForFunctionAnnotatedWithThis.traverse(functionAnnotatedWithThisTraversal, functionAnnotatedWithThisParser.getTree(), null);
    assertTrue("No warnings expected for a function annotated with @this.", checkerForFunctionAnnotatedWithThis.getNumWarnings() == 0);
  }

  @Test
  public void testNoWarningForFunctionWithNoPlaceToPutThisAnnotation() {
    String functionCode = "function myFunction() { this.x = 1; }";
    Parser functionParser = new Parser(nodeFactory, getSourceFile("myFunction.js"));
    functionParser.parse(getSourceFile("myFunction.js"));

    CheckGlobalThis checkerForFunctionWithNoPlaceToPutThisAnnotation = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node functionNode = functionParser.getTree().getFirstChild();
    NodeTraversal functionTraversal = createTraversal(functionNode, checkerForFunctionWithNoPlaceToPutThisAnnotation);
    checkerForFunctionWithNoPlaceToPutThisAnnotation.traverse(functionTraversal, functionParser.getTree(), null);
    assertTrue("No warnings expected for a function with no place to put the @this annotation.", checkerForFunctionWithNoPlaceToPutThisAnnotation.getNumWarnings() == 0);
  }

  @Test
  public void testNoWarningForFunctionAnnotatedWithConstructor() {
    String constructorCode = "function MyConstructor() { this.x = 1; }";
    Parser constructorParser = new Parser(nodeFactory, getSourceFile("MyConstructor.js"));
    constructorParser.parse(getSourceFile("MyConstructor.js"));

    String functionAnnotatedWithConstructorCode = "/** @constructor */ function myFunction() { this.x = 1; }";
    Parser functionAnnotatedWithConstructorParser = new Parser(nodeFactory, getSourceFile("myFunction.js"));
    functionAnnotatedWithConstructorParser.parse(getSourceFile("myFunction.js"));

    CheckGlobalThis checkerForFunctionAnnotatedWithConstructor = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node constructorNode = constructorParser.getTree().getFirstChild();
    Node functionAnnotatedWithConstructorNode = functionAnnotatedWithConstructorParser.getTree().getFirstChild();
    NodeTraversal functionAnnotatedWithConstructorTraversal = createTraversal(functionAnnotatedWithConstructorNode, checkerForFunctionAnnotatedWithConstructor);
    checkerForFunctionAnnotatedWithConstructor.traverse(functionAnnotatedWithConstructorTraversal, functionAnnotatedWithConstructorParser.getTree(), null);
    assertTrue("No warnings expected for a function annotated with @constructor.", checkerForFunctionAnnotatedWithConstructor.getNumWarnings() == 0);
  }

  @Test
  public void testNoWarningForPrototypeMethod() {
    String prototypeMethodCode = "function MyFunction() {} MyFunction.prototype.myMethod = function() { this.x = 1; }";
    Parser prototypeMethodParser = new Parser(nodeFactory, getSourceFile("MyFunction.js"));
    prototypeMethodParser.parse(getSourceFile("MyFunction.js"));

    CheckGlobalThis checkerForPrototypeMethod = new CheckGlobalThis(compiler, CheckLevel.WARNING);
    Node prototypeMethodNode = prototypeMethodParser.getTree().getFirstChild();
    Node myMethodNode = prototypeMethodNode.getLastChild();
    NodeTraversal myMethodTraversal = createTraversal(myMethodNode, checkerForPrototypeMethod);
    checkerForPrototypeMethod.traverse(myMethodTraversal, prototypeMethodParser.getTree(), null);
    assertTrue("No warnings expected for a prototype method.", checkerForPrototypeMethod.getNumWarnings() == 0);
  }

  private NodeTraversal createTraversal(Node startNode, CheckGlobalThis checker) {
    return new NodeTraversal(parser.getTree(), checker);
  }
}