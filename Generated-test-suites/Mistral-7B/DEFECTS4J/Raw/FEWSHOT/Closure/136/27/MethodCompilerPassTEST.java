package com.google.javascript.jscomp; // or the package where MethodCompilerPass is located

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Lists;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.rhino.Token;

import java.util.List;
import java.util.Set;

public class MethodCompilerPassTest {
    @Test
    public void testMethodCompilerPass() {
        // Test empty class
        MethodCompilerPass pass = new MethodCompilerPass(null);
        pass.process(null, null);
        assertEquals(Sets.newHashSet(), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(0, pass.methodDefinitions.size());

        // Test with simple method definitions
        String sourceCode = "function Foo() {}" +
                "function Foo.bar() {}" +
                "function Foo.prototype.qux = function() {}";
        AbstractCompiler compiler = new Compiler();
        Node root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet("bar", "qux"), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        Multimap<String, Node> expectedMethodDefinitions = HashMultimap.create();
        expectedMethodDefinitions.put("bar", root.getChild(1));
        expectedMethodDefinitions.put("qux", root.getChild(2).getChild(0));
        assertEquals(expectedMethodDefinitions, pass.methodDefinitions);

        // Test with method definitions from externs
        sourceCode = "extern function Foo() {}" +
                "extern function Foo.bar() {}";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(Node.toTree(sourceCode), root);
        assertEquals(Sets.newHashSet("bar"), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(0, pass.methodDefinitions.size());

        // Test with method definition without signature
        sourceCode = "extern function Foo.bar() {}";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(Node.toTree(sourceCode), root);
        assertEquals(Sets.newHashSet("bar"), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(0, pass.methodDefinitions.size());

        // Test with method definitions and method calls
        sourceCode = "function Foo() {" +
                "  this.bar = function() {};" +
                "  this.qux = function() {};" +
                "}";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet("bar", "qux"), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(2, pass.methodDefinitions.size());
        Node barMethod = pass.methodDefinitions.get("bar").getValue();
        Node quxMethod = pass.methodDefinitions.get("qux").getValue();
        assertEquals(Token.FUNCTION, barMethod.getType());
        assertEquals(Token.NAME, quxMethod.getType());
        assertEquals("Foo.bar", barMethod.getFirstChild().getString());
        assertEquals("Foo.prototype", quxMethod.getFirstChild(0).getString());
        assertEquals("qux", quxMethod.getLastChild().getString());

        // Test with function declarations and function expressions
        sourceCode = "function Foo() {" +
                "  function bar() {}" +
                "  var qux = function() {};" +
                "}" +
                "function Foo2() {" +
                "  function baz() {}" +
                "}" +
                "function Foo3() {" +
                "  function baz() {}" +
                "  function qux() {}" +
                "}" +
                "Foo3.prototype.quux = function() {}";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet("bar", "qux"), pass.externMethods);
        assertEquals(Sets.newHashSet("baz"), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(6, pass.methodDefinitions.size());
        Node barMethod2 = pass.methodDefinitions.get("bar").getValue();
        Node quxMethod2 = pass.methodDefinitions.get("qux").getValue();
        Node bazMethod1 = pass.methodDefinitions.get("baz").getValue();
        Node bazMethod2 = pass.methodDefinitions.get("baz").getValue();
        Node quuxMethod = pass.methodDefinitions.get("quux").getValue();
        assertEquals(Token.FUNCTION, barMethod2.getType());
        assertEquals(Token.NAME, quxMethod2.getType());
        assertEquals("Foo.bar", barMethod2.getFirstChild().getString());
        assertEquals("Foo.prototype", quxMethod2.getChild(0).getString());
        assertEquals("qux", quxMethod2.getLastChild().getString());
        assertEquals(Token.FUNCTION, bazMethod1.getType());
        assertEquals(Token.NAME, bazMethod2.getType());
        assertEquals("Foo3.baz", bazMethod1.getFirstChild().getString());
        assertEquals("Foo3.prototype", bazMethod1.getLastChild(0).getString());
        assertEquals(Token.VAR, bazMethod2.getType());
        assertEquals("baz", bazMethod2.getLastChild().getString());
        assertEquals(Token.FUNCTION, quuxMethod.getType());
        assertEquals("Foo3.prototype", quuxMethod.getFirstChild(0).getString());
        assertEquals("quux", quuxMethod.getLastChild().getString());

        // Test with method definitions and method calls with prototype chain
        sourceCode = "function Foo() {}" +
                "Foo.prototype.bar = function() {}" +
                "Foo.prototype.qux = function() {}" +
                "function Foo2() {}" +
                "Foo2.prototype = new Foo.prototype();" +
                "Foo2.prototype.qux = function() {}";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet("bar", "qux"), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(4, pass.methodDefinitions.size());
        Node barMethod3 = pass.methodDefinitions.get("bar").getValue();
        Node quxMethod3 = pass.methodDefinitions.get("qux").getValue();
        Node quxMethod4 = pass.methodDefinitions.get("qux").getValue();
        assertEquals(Token.FUNCTION, barMethod3.getType());
        assertEquals(Token.NAME, quxMethod3.getType());
        assertEquals("Foo.bar", barMethod3.getFirstChild().getString());
        assertEquals("Foo.prototype", quxMethod3.getChild(0).getString());
        assertEquals("qux", quxMethod3.getLastChild().getString());
        assertEquals(Token.FUNCTION, quxMethod4.getType());
        assertEquals("Foo2.prototype", quxMethod4.getFirstChild(0).getString());
        assertEquals("qux", quxMethod4.getLastChild().getString());

        // Test with invalid method definitions
        sourceCode = "function Foo() {}" +
                "Foo.prototype.qux = function() {}" +
                "function Foo.bar = function() {}"; // missing 'function' keyword
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet(), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(1, pass.methodDefinitions.size());
        Node quxMethod5 = pass.methodDefinitions.get("qux").getValue();
        assertEquals(Token.FUNCTION, quxMethod5.getType());
        assertEquals("Foo.prototype", quxMethod5.getFirstChild(0).getString());
        assertEquals("qux", quxMethod5.getLastChild().getString());
        assertNull(pass.methodDefinitions.get("bar"));

        // Test with method definitions and type errors
        sourceCode = "function Foo() {}" +
                "Foo.prototype.qux = function() {}" +
                "function Foo.bar = 42;";
        compiler = new Compiler();
        root = compiler.parse(sourceCode, null);
        pass = new MethodCompilerPass(compiler);
        pass.process(null, root);
        assertEquals(Sets.newHashSet(), pass.externMethods);
        assertEquals(Sets.newHashSet(), pass.externMethodsWithoutSignatures);
        assertEquals(Sets.newHashSet(), pass.nonMethodProperties);
        assertEquals(1, pass.methodDefinitions.size());
        Node quxMethod6 = pass.methodDefinitions.get("qux").getValue();
        assertEquals(Token.FUNCTION, quxMethod6.getType());
        assertEquals("Foo.prototype", quxMethod6.getFirstChild(0).getString());
        assertEquals("qux", quxMethod6.getLastChild().getString());
        assertNull(pass.methodDefinitions.get("bar"));
    }
}