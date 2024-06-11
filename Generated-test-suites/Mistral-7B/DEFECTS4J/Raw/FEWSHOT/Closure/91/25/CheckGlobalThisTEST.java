package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.javascript.jscomp.AbstractCompiler;
import com.google.javascript.jscomp.CheckLevel;
import com.google.javascript.jscomp.DiagnosticType;
import com.google.javascript.jscomp.Node;
import com.google.javascript.jscomp.NodeTraversal;
import com.google.javascript.jscomp.NodeUtil;
import com.google.javascript.rhino.JSDocInfo;

public class CheckGlobalThisTest {
    @Test
    public void testShouldTraverse() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("function foo() { this.bar = 1; }");
        NodeTraversal traversal = createTraversal(root);

        assertTrue(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseConstructor() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("function Foo() { this.bar = 1; }");
        NodeTraversal traversal = createTraversal(root);

        assertFalse(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseFunctionWithJsDocAnnotation() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("/** @constructor */ function Foo() { this.bar = 1; }");
        NodeTraversal traversal = createTraversal(root);

        assertFalse(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseFunctionWithThisAnnotation() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("/** @this {Foo} */ function Foo() { this.bar = 1; }");
        NodeTraversal traversal = createTraversal(root);

        assertFalse(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseFunctionWithNoLogicalPlaceForThisAnnotation() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("function foo() { this.bar = 1; }");
        NodeTraversal traversal = createTraversal(root);

        assertTrue(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseAssignmentsToPrototypeProperties() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("var obj = {}; obj.prototype.foo = function() { this.bar = 1; };");
        NodeTraversal traversal = createTraversal(root);

        assertFalse(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldNotTraverseAssignmentsToPrototypeSubproperties() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("var obj = {}; obj.prototype.foo = { bar: function() { this.baz = 1; } };");
        NodeTraversal traversal = createTraversal(root);

        assertFalse(checker.shouldTraverse(traversal, null, root));
    }

    @Test
    public void testShouldReportThisOnLeftSideOfAssignment() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("var a = this;");
        NodeTraversal traversal = createTraversal(root);

        when(compiler.report(traversal.makeError(root, level, DiagnosticType.WARNING))).thenReturn(null);

        checker.visit(traversal, null, root);

        // compiler.report should have been called once
        verify(compiler, times(1)).report(traversal.makeError(root, level, DiagnosticType.WARNING));
    }

    @Test
    public void testShouldNotReportThisOnLeftSideOfAssignmentWhenAssignedToVariableOrObject() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("var a = this; var b = a;");
        NodeTraversal traversal = createTraversal(root);

        checker.visit(traversal, null, root);

        verify(compiler, never()).report(traversal.makeError(root, level, DiagnosticType.WARNING));
    }

    @Test
    public void testShouldReportThisOnLeftSideOfPropertyAccess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNING;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node root = createNode("var obj = { foo: this };");
        NodeTraversal traversal = createTraversal(root);

        when(compiler.report(traversal.makeError(root, level, DiagnosticType.WARNING))).thenReturn(null);

        checker.visit(traversal, null, root);

        // compiler.report should have been called once
        verify(compiler, times(1)).report(traversal.makeError(root, level, DiagnosticType.WARNING));
    }

    private Node createNode(String code) {
        return new RhinoParser().parse(code, null, null).getFirstChild();
    }

    private NodeTraversal createTraversal(Node node) {
        return new NodeTraversal(node);
    }
}