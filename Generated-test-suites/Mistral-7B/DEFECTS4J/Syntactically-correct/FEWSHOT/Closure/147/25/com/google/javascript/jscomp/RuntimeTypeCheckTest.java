package com.google.javascript.jscomp;

import static com.google.common.truth.Truth.assertThat;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CompilerInput;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.TypeChecker;
import com.google.javascript.rhino.NodeUtil;

import java.io.IOException;
import java.util.stream.Stream;

public class RuntimeTypeCheckTest {

  @Test
  public void testAddMarkers() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    String logFunction = "logFunction";

    when(compiler.getInput("Foo.js")).thenReturn(new CompilerInput("Foo.js", ""));
    when(compiler.getInput("Bar.js")).thenReturn(new CompilerInput("Bar.js", ""));

    Node root = NodeUtil.parseJavaScript("function Foo() {}", null);
    NodeTraversal.traverse(compiler, root, new RuntimeTypeCheck(compiler, logFunction));

    Node fooConstructor = NodeUtil.getFunction("Foo");
    Node fooPrototype = NodeUtil.getPrototype(fooConstructor);
    Node fooInstanceMarker = NodeUtil.getGetter(fooPrototype, "instance_of__Foo");
    assertSame(fooInstanceMarker, fooPrototype.getChild(fooPrototype.getNumChildren() - 1));

    Node barConstructor = NodeUtil.getFunction("Bar");
    Node barPrototype = NodeUtil.getPrototype(barConstructor);
    Node barInterfaceMarker = NodeUtil.getGetter(barPrototype, "implements__I");
    assertNull(barPrototype.getChild(barPrototype.getNumChildren() - 1));

    Node barInterface = NodeUtil.newQualifiedNameNode("I", -1, -1);
    NodeBar interfaceNode = NodeUtil.getFunction("Bar", NodeUtil.newList(
        NodeUtil.newPair("I", barInterface),
        NodeUtil.newPair("Foo", fooConstructor)
    ));

    NodeTraversal.traverse(compiler, root, new RuntimeTypeCheck(compiler, logFunction));

    Node barPrototypeAfter = NodeUtil.getPrototype(barConstructor);
    Node barInterfaceMarker = NodeUtil.getGetter(barPrototypeAfter, "implements__I");
    assertSame(barInterfaceMarker, barPrototypeAfter.getChild(barPrototypeAfter.getNumChildren() - 1));
  }

  @Test
  public void testAddChecks() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    when(compiler.getCodingConvention()).thenReturn(new CodingConvention());

    Node root = NodeUtil.parseJavaScript("function Foo() { return null; }", null);
    NodeTraversal.traverse(compiler, root, new RuntimeTypeCheck(compiler, null));

    Node fooFunction = NodeUtil.getFunction("Foo");
    Node fooConstructor = NodeUtil.getConstructor(fooFunction);
    Node fooReturn = fooFunction.getLastChild();

    Node checkNode = NodeUtil.getChildByFunctionName(fooFunction, "checkType");
    Node paramCheckerNode = NodeUtil.getChildByFunctionName(checkNode, "valueChecker");
    Node nullCheckerNode = NodeUtil.getChildByFunctionName(paramCheckerNode, "nullChecker");

    Node expectedNullChecker = NodeUtil.newCall(
        NodeUtil.getQualifiedNameNode("jscomp.typecheck.nullChecker"),
        nullReturnValue);

    assertSame(expectedNullChecker, nullCheckerNode);

    Node expectedCheckTypeCall = NodeUtil.newCall(
        NodeUtil.getQualifiedNameNode("jscomp.typecheck.checkType"),
        nullReturnValue,
        NodeUtil.newList(NodeUtil.newCall(nullCheckerNode, nullReturnValue))
    );

    assertSame(expectedCheckTypeCall, checkNode);
  }

  @Test
  public void testAddBoilerplateCode() throws IOException {
    AbstractCompiler compiler = mock(AbstractCompiler.class);
    String logFunction = "logFunction";

    when(compiler.getInput("Foo.js")).thenReturn(new CompilerInput("Foo.js", ""));
    when(compiler.getInput("Bar.js")).thenReturn(new CompilerInput("Bar.js", ""));

    Node root = NodeUtil.parseJavaScript("function Foo() {}", null);
    NodeTraversal.traverse(compiler, root, new RuntimeTypeCheck(compiler, logFunction));

    Node expectedBoilerplate = NodeUtil.parseJavaScript(
        "function checkType(expr, checkers) {" +
            "// ... checkType implementation" +
        "}\n" +
            "function logFunction(warning, expr) {}\n" +
            "function nullChecker(value) {\n" +
            "  return value === null || value === undefined;\n" +
            "}\n", null);

    Node actualBoilerplate = NodeUtil.getChildByFunctionName(
        NodeUtil.getChildByFunctionName(root, "Foo"), "checkType");

    NodeUtil.assertTreeEquals(expectedBoilerplate, actualBoilerplate);
  }
}

class CodingConvention {
  // Empty for testing purposes
}