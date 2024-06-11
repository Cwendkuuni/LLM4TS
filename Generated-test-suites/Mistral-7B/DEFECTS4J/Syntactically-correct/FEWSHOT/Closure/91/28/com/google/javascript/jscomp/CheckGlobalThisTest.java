package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraversePrototypeMethod() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node prototypeMethodNode = createNode("function prototypeMethod() { this.foo = 'bar'; }");

        assertTrue(checkGlobalThis.shouldTraverse(null, prototypeMethodNode, null));
    }

    @Test
    public void testShouldTraverseConstructor() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node constructorNode = createNode("function MyConstructor() { this.foo = 'bar'; }");

        assertTrue(checkGlobalThis.shouldTraverse(null, constructorNode, null));
    }

    @Test
    public void testShouldTraverseFunctionWithThisAnnotation() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node functionWithThisAnnotationNode = createNode(
                "/** @this {MyType} */ function MyFunction() { this.foo = 'bar'; }");

        assertTrue(checkGlobalThis.shouldTraverse(null, functionWithThisAnnotationNode, null));
    }

    @Test
    public void testShouldTraverseFunctionWithoutThisAnnotationButCanBeAnnotated() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node functionWithoutThisAnnotationNode = createNode("function MyFunction() { this.foo = 'bar'; }");

        assertTrue(checkGlobalThis.shouldTraverse(null, functionWithoutThisAnnotationNode, null));
    }

    @Test
    public void testShouldNotTraverseNonGlobalContext() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node nonGlobalContextNode = createNode("function MyFunction() { var that = this; }");

        assertFalse(checkGlobalThis.shouldTraverse(null, nonGlobalContextNode, null));
    }

    @Test
    public void testShouldTraverseLeftSideOfAssignment() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node assignmentNode = createNode("var a = this;");

        assertTrue(checkGlobalThis.shouldTraverse(null, assignmentNode, null));
    }

    @Test
    public void testShouldTraverseRightSideOfAssignmentToPrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node assignmentNode = createNode("var a = { b: this };");

        assertTrue(checkGlobalThis.shouldTraverse(null, assignmentNode, null));
    }

    @Test
    public void testShouldNotTraverseRightSideOfAssignmentToPrototypeProperty() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node assignmentNode = createNode("var a = { b: { c: this } };");

        assertFalse(checkGlobalThis.shouldTraverse(null, assignmentNode, null));
    }

    @Test
    public void testShouldNotTraverseNonAssignNodeOnRightSide() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node nonAssignNodeOnRightSide = createNode("var a = this + 1;");

        assertFalse(checkGlobalThis.shouldTraverse(null, nonAssignNodeOnRightSide, null));
    }

    @Test
    public void testShouldReportThisInsideFunction() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node functionNode = createNode("function evil() { this.foo = 'bar'; }");

        assertTrue(checkGlobalThis.shouldReportThis(functionNode, null));
    }

    @Test
    public void testShouldNotReportThisInsidePrototypeMethod() {
        CheckGlobalThis checkGlobalThis = new CheckGlobalThis(null, CheckLevel.WARNING);
        Node prototypeMethodNode = createNode("function prototypeMethod() { this.foo = this.foo + 'bar'; }");

        assertFalse(checkGlobalThis.shouldReportThis(prototypeMethodNode, null));
    }

    private Node createNode(String code) {
        RhinoCompiler compiler = new RhinoCompiler();
        return compiler.parseSingleStatement(code);
    }
}