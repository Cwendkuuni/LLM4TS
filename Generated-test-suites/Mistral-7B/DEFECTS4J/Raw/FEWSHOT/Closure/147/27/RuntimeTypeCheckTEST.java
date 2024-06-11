package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.Sets;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.jscomp.RuntimeTypeCheck;
import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.rhino.NodeTraversal;
import com.google.javascript.rhino.jstype.FunctionType;
import com.google.javascript.rhino.jstype.JSType;
import com.google.javascript.rhino.jstype.ObjectType;
import com.google.javascript.rhino.jstype.UnionType;

import java.util.Collection;
import java.util.Comparator;
import java.util.TreeSet;

/**
 * Tests for {@link RuntimeTypeCheck} class.
 */
public class RuntimeTypeCheckTest {
    private AbstractCompiler compiler;
    private RuntimeTypeCheck runtimeTypeCheck;

    @Before
    public void setUp() {
        compiler = new AbstractCompiler() {};
        runtimeTypeCheck = new RuntimeTypeCheck(compiler, null);
    }

    @Test
    public void testProcess_NoTypeChecks() {
        Node root = Node.newTextNode("function foo() {}");
        runtimeTypeCheck.process(null, root);
        assertNull(findCheckTypeCallNode(root));
    }

    @Test
    public void testProcess_SimpleFunctions() {
        Node root = Node.newTextNode(
                "function add(x, y) { return x + y; }");
        runtimeTypeCheck.process(null, root);
        Node addFunction = findFunctionNode(root, "add");
        Node checkNode = findCheckTypeCallNode(addFunction);
        assertSame(addFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithMultipleParameters() {
        Node root = Node.newTextNode(
                "function multiply(x, y, z) { return x * y * z; }");
        runtimeTypeCheck.process(null, root);
        Node multiplyFunction = findFunctionNode(root, "multiply");
        Node checkNode = findCheckTypeCallNode(multiplyFunction);
        assertSame(multiplyFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithVoidReturnType() {
        Node root = Node.newTextNode(
                "function log() { console.log('Hello, world!'); }");
        runtimeTypeCheck.process(null, root);
        Node logFunction = findFunctionNode(root, "log");
        Node checkNode = findCheckTypeCallNode(logFunction);
        assertNull(checkNode);
    }

    @Test
    public void testProcess_FunctionsWithInstanceTypes() {
        Node root = Node.newTextNode(
                "function Foo() {} function Bar() { this.x = 1; } " +
                        "Bar.prototype.doSomething = function() {}; " +
                        "function baz(x) { x.doSomething(); }");
        runtimeTypeCheck.process(null, root);
        Node bazFunction = findFunctionNode(root, "baz");
        Node checkNode = findCheckTypeCallNode(bazFunction.getFirstChild());
        assertSame(bazFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithInterfaceTypes() {
        Node root = Node.newTextNode(
                "interface I { x: number; } " +
                        "class Foo implements I { x: number; } " +
                        "function foo(x: I) {}");
        runtimeTypeCheck.process(null, root);
        Node fooFunction = findFunctionNode(root, "foo");
        Node checkNode = findCheckTypeCallNode(fooFunction);
        assertSame(fooFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithUnionTypes() {
        Node root = Node.newTextNode(
                "function foo(x: number | string) {}");
        runtimeTypeCheck.process(null, root);
        Node fooFunction = findFunctionNode(root, "foo");
        Node arrayNode = (Node) findCheckTypeCallNode(fooFunction).getLastChild();
        Collection<Node> checkerNodes = ImmutableList.of(
                (Node) arrayNode.getFirstChild(),
                (Node) arrayNode.getLastChild());
        TreeSet<JSType> expectedTypes = Sets.newTreeSet(ALPHA);
        Iterables.addAll(expectedTypes, JSType.NUMBER_TYPE, JSType.STRING_TYPE);
        assertTypesMatch(expectedTypes, checkerNodes);
    }

    @Test
    public void testProcess_FunctionsWithNullType() {
        Node root = Node.newTextNode(
                "function foo(x: null) {}");
        runtimeTypeCheck.process(null, root);
        Node fooFunction = findFunctionNode(root, "foo");
        Node checkNode = findCheckTypeCallNode(fooFunction);
        assertSame(fooFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithExternedTypes() {
        Node root = Node.newTextNode(
                "// @externs: foo.js\n" +
                        "function foo() {} // @type {number}\n" +
                        "function bar() {} // @type {string}\n" +
                        "function baz(x: foo) {}");
        runtimeTypeCheck.process(null, root);
        Node bazFunction = findFunctionNode(root, "baz");
        Node checkNode = findCheckTypeCallNode(bazFunction.getFirstChild());
        assertSame(bazFunction.getFirstChild(), checkNode);
    }

    @Test
    public void testProcess_FunctionsWithMultipleExternedTypes() {
        Node root = Node.newTextNode(
                "// @externs: foo.js\n" +
                        "function foo() {} // @type {number}\n" +
                        "// @externs: bar.js\n" +
                        "function bar() {} // @type {string}\n" +
                        "function baz(x: foo, y: bar) {}");
        runtimeTypeCheck.process(null, root);
        Node bazFunction = findFunctionNode(root, "baz");
        Node arrayNode = (Node) findCheckTypeCallNode(bazFunction.getFirstChild());
        Collection<Node> checkerNodes = ImmutableList.of(
                (Node) arrayNode.getFirstChild(),
                (Node) arrayNode.getLastChild());
        TreeSet<JSType> expectedTypes = Sets.newTreeSet(ALPHA);
        Iterables.addAll(expectedTypes, JSType.NUMBER_TYPE, JSType.STRING_TYPE);
        assertTypesMatch(expectedTypes, checkerNodes);
    }

    private Node findFunctionNode(Node root, String name) {
        NodeTraversal traversal = NodeTraversal.create(compiler);
        Node node = traversal.findFunction(root, name);
        assertNotNull(node);
        return node;
    }

    private Node findCheckTypeCallNode(Node node) {
        Node checkTypeCallNode = null;
        for (Node child = node.getFirstChild(); child != null;
                child = child.getNext()) {
            if (child instanceof Node && child.getType() == Token.CALL &&
                    NodeUtil.isQualifiedName(
                            ((Node) child).getFirstChild(),
                            "jscomp.typecheck.checkType")) {
                checkTypeCallNode = child;
                break;
            }
        }
        return checkTypeCallNode;
    }

    private void assertTypesMatch(Collection<JSType> expectedTypes,
            Collection<Node> checkerNodes) {
        TreeSet<JSType> actualTypes = new TreeSet<>();
        for (Node checkerNode : checkerNodes) {
            JSType type = ((Node) checkerNode.getLastChild())
                    .getJSType();
            assertFalse(type.isNullType(), "Null type should not be present.");
            actualTypes.add(type);
        }
        Comparator<JSType> comparator = ALPHA;
        assertTrue(comparator.equals(expectedTypes.compareTo(actualTypes)));
    }
}