package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp. compiler.AbstractCompiler;

import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Tests for MethodCompilerPass class
 */
public class MethodCompilerPassTest {
  private static class TestCompiler extends AbstractCompiler {
    @Override
    public void reportErrors(List<Node> nodes, int start, int end, String message,
        Object... args) {
      // do nothing
    }
  }

  @Test
  public void testBasic() {
    TestCompiler compiler = new TestCompiler();

    MethodCompilerPass pass = new MethodCompilerPass(compiler);

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {};\n" +
        "function Foo2() {}\n" +
        "Foo2.prototype.baz = function() {};\n";

    Node root = RhinoParser.parse(sourceCode, null, null);

    pass.process(null, root);

    Set<String> expectedMethods = Sets.newHashSet("bar", "baz");
    assertEquals(expectedMethods, pass.externMethods);

    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("bar", pass.methodDefinitions.get("bar"));
    expectedMethodDefinitions.put("baz", pass.methodDefinitions.get("baz"));
    assertSame(expectedMethodDefinitions, pass.methodDefinitions);

    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(0, pass.nonMethodProperties.size());
  }

  @Test
  public void testExternMethodsWithoutSignatures() {
    TestCompiler compiler = new TestCompiler();

    MethodCompilerPass pass = new MethodCompilerPass(compiler);

    String sourceCode = "extern function Foo();\n" +
        "extern function Foo.bar();\n";

    Node root = RhinoParser.parse(sourceCode, null, null);

    pass.process(null, root);

    Set<String> expectedMethods = Sets.newHashSet("Foo");
    assertEquals(expectedMethods, pass.externMethods);

    Set<String> expectedMethodsWithoutSignatures = Sets.newHashSet("Foo");
    assertEquals(expectedMethodsWithoutSignatures, pass.externMethodsWithoutSignatures);

    assertEquals(0, pass.externMethods.size());
    assertEquals(1, pass.externMethodsWithoutSignatures.size());
  }

  @Test
  public void testNonMethodProperties() {
    TestCompiler compiler = new TestCompiler();

    MethodCompilerPass pass = new MethodCompilerPass(compiler);

    String sourceCode = "extern function Foo();\n" +
        "Foo.foo = 42;\n";

    Node root = RhinoParser.parse(sourceCode, null, null);

    pass.process(null, root);

    Set<String> expectedNonMethodProperties = Sets.newHashSet("foo");
    assertEquals(expectedNonMethodProperties, pass.nonMethodProperties);

    assertEquals(0, pass.externMethods.size());
    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(1, pass.nonMethodProperties.size());
  }

  @Test
  public void testMultipleBranches() {
    TestCompiler compiler = new TestCompiler();

    MethodCompilerPass pass = new MethodCompilerPass(compiler);

    String sourceCode = "function Foo() {}\n" +
        "Foo.prototype.bar = function() {}\n" +
        "function Foo2() {}\n" +
        "Foo2.prototype.baz = function() {}\n" +
        "function Foo3() {}\n" +
        "Foo3.prototype.qux = function() {}\n";

    Node root = RhinoParser.parse(sourceCode, null, null);

    pass.process(null, root);

    Set<String> expectedMethods = Sets.newHashSet("bar", "baz", "qux");
    assertEquals(expectedMethods, pass.externMethods);

    Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
    expectedMethodDefinitions.put("bar", pass.methodDefinitions.get("bar"));
    expectedMethodDefinitions.put("baz", pass.methodDefinitions.get("baz"));
    expectedMethodDefinitions.put("qux", pass.methodDefinitions.get("qux"));
    assertSame(expectedMethodDefinitions, pass.methodDefinitions);

    assertEquals(0, pass.externMethodsWithoutSignatures.size());
    assertEquals(0, pass.nonMethodProperties.size());
  }
}