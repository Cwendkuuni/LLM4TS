package com.google.javascript.jscomp;

import static org.junit.Assert.*;

import com.google.common.collect.ImmutableList;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.CompilerPass;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.FunctionType;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.NodeUtil;
import com.google.javascript.rhino.ObjectType;
import com.google.javascript.rhino.Token;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.util.Comparator;
import java.util.Set;
import java.util.TreeSet;

@RunWith(MockitoJUnitRunner.class)
public class RuntimeTypeCheckTest {

  @Mock
  private AbstractCompiler compiler;

  @InjectMocks
  private RuntimeTypeCheck runtimeTypeCheck;

  @Before
  public void setUp() throws IOException {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void testProcess_NoFunction() {
    Node root = Node.newText("var x = 10;");
    Node externs = Node.newText("/** @const */ var Foo = {};");

    runtimeTypeCheck.process(externs, root);

    // No change is expected as there's no function in the root node.
    assertEquals(root.toString(), Node.newText("var x = 10;").toString());
    assertEquals(externs.toString(), Node.newText("/** @const */ var Foo = {};").toString());
  }

  @Test
  public void testProcess_FunctionWithExpectedType() {
    String jsCode = "function Foo(x) { return x * 2; }";
    Node root = Node.newText(jsCode);
    Node externs = Node.newText("");

    CompilerInput compilerInput = new CompilerInput("test.js", jsCode);
    CompilerInput externsInput = new CompilerInput("test_externs.js", "");

    when(compiler.getInput(eq("test.js"))).thenReturn(compilerInput);
    when(compiler.getInput(eq("test_externs.js"))).thenReturn(externsInput);

    runtimeTypeCheck.process(externs, root);

    String expectedCode =
        "function Foo(x) {" +
            "checkType(x, [valueChecker('number')]);" +
            "return x * 2;" +
            "}";

    Node actualCode = runtimeTypeCheck.getNodeForCodeInsertion(null);

    assertEquals(expectedCode, Node.newText(actualCode.toString()).toString());
  }

  @Test
  public void testProcess_FunctionWithUnionTypeParameter() {
    String jsCode = "function Foo(x) {" +
        "if (x instanceof String) {" +
        "  return x.length;" +
        "} else if (x instanceof Number) {" +
        "  return x.toString().length;" +
        "} else {" +
        "  throw new Error('Invalid argument type');" +
        "}";
    Node root = Node.newText(jsCode);
    Node externs = Node.newText("");

    CompilerInput compilerInput = new CompilerInput("test.js", jsCode);
    CompilerInput externsInput = new CompilerInput("test_externs.js", "");

    when(compiler.getInput(eq("test.js"))).thenReturn(compilerInput);
    when(compiler.getInput(eq("test_externs.js"))).thenReturn(externsInput);

    runtimeTypeCheck.process(externs, root);

    String expectedCode =
        "function Foo(x) {" +
            "checkType(x, [valueChecker('string'), valueChecker('number')]);" +
            "if (x instanceof String) {" +
            "  return x.length;" +
            "} else if (typeof x == 'number') {" +
            "  checkType(x, [valueChecker('number')]);" +
            "  return x.toString().length;" +
            "} else {" +
            "  throw new Error('Invalid argument type');" +
            "}" +
            "}";

    Node actualCode = runtimeTypeCheck.getNodeForCodeInsertion(null);

    assertEquals(expectedCode, Node.newText(actualCode.toString()).toString());
  }

  @Test
  public void testProcess_FunctionWithInstanceTypeParameter() {
    String jsCode = "function Foo(x) {" +
        "if (x instanceof MyClass) {" +
        "  return x.someMethod();" +
        "} else {" +
        "  throw new Error('Invalid argument type');" +
        "}" +
        "}";
    Node root = Node.newText(jsCode);
    Node externs = Node.newText("");

    FunctionType myClassType = new FunctionType(
        "MyClass", ImmutableList.of(), new ObjectType("Object"));

    CompilerInput compilerInput = new CompilerInput("test.js", jsCode);
    CompilerInput externsInput = new CompilerInput("test_externs.js", "");

    when(compiler.getInput(eq("test.js"))).thenReturn(compilerInput);
    when(compiler.getInput(eq("test_externs.js"))).thenReturn(externsInput);

    // Mock the checker
    Node checkerNode = NodeUtil.newNamedFunction("checkType", null);
    Node checkerCallNode = new Node(Token.CALL, checkerNode, null);

    Node instanceCheckNode = new Node(Token.INSTANCEOF, Node.newThis(), NodeUtil.newQualifiedNameNode("MyClass", -1, -1));
    Node markerNode = new Node(Token.GETPROP, Node.newThis(), Node.newString("prototype"));
    markerNode = new Node(Token.GETELEM, markerNode, Node.newString("instance_of__MyClass"));

    Node expectedCode =
        "function Foo(x) {" +
            "checkType(x, [instanceChecker(MyClass)]);" +
            "if (x instanceof MyClass) {" +
            "  return x.someMethod();" +
            "} else {" +
            "  throw new Error('Invalid argument type');" +
            "}" +
            "}";

    Node actualCode = Node.newText("");

    // Perform the processing and create the expected code
    runtimeTypeCheck.process(externs, root);
    actualCode = runtimeTypeCheck.getNodeForCodeInsertion(null);

    // Replace the mocked checker node with the actual implementation
    Node actualCheckerNode = new Node(Token.GETELEM, checkerCallNode, 0);
    actualCheckerNode = new Node(Token.CALL, actualCheckerNode, NodeUtil.newQualifiedNameNode("jscomp.typecheck.instanceChecker", -1, -1), NodeUtil.newQualifiedNameNode("MyClass", -1, -1));
    actualCode = new Node(Token.ASSIGN, actualCode, Node.newText(actualCode.toString()).replace(checkerCallNode.toString(), actualCheckerNode.toString()));

    assertEquals(expectedCode, Node.newText(actualCode.toString()).toString());
  }

  @Test(expected = RuntimeException.class)
  public void testProcess_FunctionWithUncheckedType() {
    String jsCode = "function Foo() {}";
    Node root = Node.newText(jsCode);
    Node externs = Node.newText("");

    runtimeTypeCheck.process(externs, root);

    fail("The function 'Foo' should not be processed since it doesn't have any checked types.");
  }

  @Test(expected = IllegalArgumentException.class)
  public void testProcess_NullCompiler() {
    Node root = Node.newText("");
    Node externs = Node.newText("");

    runtimeTypeCheck.process(externs, root);

    fail("The 'compiler' argument should not be null.");
  }

  @Test
  public void testAddBoilerplateCode() throws IOException {
    String boilerplateCode = getClass().getResource("js/runtime_type_check.js").toString();
    String expectedCode = boilerplateCode.replace("%%LOG%%", "function(warning, expr) {}");

    Node actualCode = runtimeTypeCheck.addBoilerplateCode(compiler, "function(warning, expr) {}", null);

    assertEquals(expectedCode, Node.newText(actualCode.toString()).toString());
  }
}