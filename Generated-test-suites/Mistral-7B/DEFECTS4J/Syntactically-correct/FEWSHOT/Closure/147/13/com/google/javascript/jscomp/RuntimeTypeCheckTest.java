package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Iterables;
import com.google.common.io.CharStreams;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.Token;
import com.google.javascript.rhino.jstype.JSType;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import javax.annotation.Nullable;

@RunWith(JUnit4.class)
public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() throws Exception {
    String testContent = "class MyClass {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "}\n" +
        "function foo(x) {\n" +
        "  return x;\n" +
        "}" +
        "MyClass.prototype.test = function() {\n" +
        "  return 'test';\n" +
        "};";

    Node root = NodeUtil.parse(testContent);
    Node externs = Node.newNull();

    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node myClass = NodeUtil.getFunction("MyClass")
        .getLastChild()
        .getLastChild();

    Node marker = Iterables.getFirst(myClass.getChildren(), null);
    assertNotNull(marker);
    assertEquals("prototype", marker.getNext().getString());
    assertEquals("instance_of__MyClass", marker.getString());

    Node foo = NodeUtil.getFunction("foo");
    marker = Iterables.getFirst(foo.getLastChild().getChildren(), null);
    assertNotNull(marker);
    assertEquals("Function", marker.getString());
    assertEquals("foo", marker.getNext().getString());
  }

  @Test
  public void testAddChecks() throws Exception {
    String testContent = "class MyClass {\n" +
        "  constructor(x: number) {\n" +
        "  }\n" +
        "}\n" +
        "function foo(x: number) {\n" +
        "  return x + 1;\n" +
        "}" +
        "function bar(x: MyClass) {\n" +
        "  return x.test();\n" +
        "}";

    Node root = NodeUtil.parse(testContent);
    Node externs = Node.newNull();

    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node myClass = NodeUtil.getFunction("MyClass")
        .getLastChild()
        .getLastChild();

    Node constructor = myClass.getFirstChild();
    Node parameter = constructor.getFirstChild();
    Node checker = Iterables.getFirst(constructor.getChildren(), null);
    assertNotNull(checker);
    assertEquals("number", checker.getNext().getString());
    assertEquals("instance_of__MyClass", checker.getString());

    Node foo = NodeUtil.getFunction("foo");
    Node parameterNode = foo.getFirstChild();
    Node checkNode = Iterables.getFirst(parameterNode.getChildren(), null);
    assertNotNull(checkNode);
    assertEquals("number", checkNode.getNext().getString());
    assertEquals("valueChecker", checkNode.getString());

    Node bar = NodeUtil.getFunction("bar");
    Node checkerNode = Iterables.getFirst(bar.getLastChild().getChildren(), null);
    assertNotNull(checkerNode);
    assertEquals("MyClass", checkerNode.getString());
    assertEquals("classChecker", checkerNode.getNext().getString());
  }

  @Test
  public void testAddBoilerplateCode() throws Exception {
    String testContent = "class MyClass {\n" +
        "  constructor() {\n" +
        "  }\n" +
        "}\n" +
        "function foo(x) {\n" +
        "  return x;\n" +
        "}";

    Node root = NodeUtil.parse(testContent);
    Node externs = Node.newNull();

    AbstractCompiler compiler = new Compiler();
    RuntimeTypeCheck runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    runtimeTypeCheck.process(externs, root);

    Node boilerplateCode = NodeUtil.getFunction("jscomp_runtimeTypeCheck_");
    Node checkType = boilerplateCode.getFirstChild();
    Node logFunction = boilerplateCode.getLastChild();

    // The logFunction should not be present if not provided.
    if (logFunction != null) {
      fail("logFunction should not be present.");
    }

    Node checkNode = Iterables.getFirst(checkType.getChildren(), null);
    assertNotNull(checkNode);
    assertEquals("checkType", checkNode.getString());

    Node arrayNode = checkNode.getNext();
    Node checkerNode = arrayNode.getFirstChild();
    Node valueChecker = checkerNode.getNext();

    assertNotNull(valueChecker);
    assertEquals("valueChecker", valueChecker.getString());

    Node classChecker = arrayNode.getLastChild();
    assertNotNull(classChecker);
    assertEquals("classChecker", classChecker.getString());
  }
}