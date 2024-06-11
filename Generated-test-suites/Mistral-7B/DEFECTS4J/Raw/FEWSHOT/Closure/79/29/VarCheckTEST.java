package com.google.javascript.jscomp;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.google.common.collect.Sets;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.util.Set;

public class VarCheckTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void testProcess_noExterns() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("function foo() {}");
        check.process(null, root);

        Set<String> declaredVars = Sets.newHashSet();
        Scope globalScope = check.compiler.getGlobalScope();
        for (Scope.Var var : globalScope.getVariables()) {
            declaredVars.add(var.name);
        }

        assertSame(declaredVars, Sets.newHashSet("foo"));
    }

    @Test
    public void testProcess_externs_noDuplicates() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var foo = 42;");
        Node root = Node.newText("function foo() {}");

        check.process(externs, root);

        assertSame(Sets.newHashSet(), check.varsToDeclareInExterns);
    }

    @Test
    public void testProcess_externs_duplicates() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var foo = 42;");
        Node root = Node.newText("function foo() {}", Node.newText("var foo = 42;"));

        check.process(externs, root);

        assertTrue(check.varsToDeclareInExterns.isEmpty());
    }

    @Test
    public void testProcess_externs_undeclared() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        assertFalse(check.varsToDeclareInExterns.isEmpty());
        assertEquals(Sets.newHashSet("bar"), check.varsToDeclareInExterns);
    }

    @Test
    public void testProcess_externs_undeclared_sanityCheck() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, true);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        thrown.expect(IllegalStateException.class);
        check.process(externs, root);
    }

    @Test
    public void testVisit_undeclared_global() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("var foo = 42;");

        Scope scope = new Scope();
        NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
        Node n = traversal.getLastProcessedNode();

        check.visit(NodeTraversal.getTraverser(traversal), n, null);

        assertNull(check.varsToDeclareInExterns.iterator().next());
    }

    @Test
    public void testVisit_undeclared_local() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("function foo() { var bar = 42; }");

        Scope scope = new Scope();
        NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
        Node n = traversal.getLastProcessedNode();

        check.visit(NodeTraversal.getTraverser(traversal), n, null);

        assertSame(Sets.newHashSet("bar"), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_undeclared_function() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("function foo() { function bar() {} }");

        Scope scope = new Scope();
        NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
        Node n = traversal.getLastProcessedNode();

        check.visit(NodeTraversal.getTraverser(traversal), n, null);

        assertSame(Sets.newHashSet("bar"), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_undeclared_functionExpression() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("function foo() { var bar = function() {} }");

        Scope scope = new Scope();
        NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
        Node n = traversal.getLastProcessedNode();

        check.visit(NodeTraversal.getTraverser(traversal), n, null);

        assertSame(Sets.newHashSet("bar"), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_declared() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node root = Node.newText("function foo() { var bar = 42; }");

        Scope scope = new Scope();
        NodeTraversal traversal = new NodeTraversal(compiler, root, scope);
        Node n = traversal.getLastProcessedNode();

        check.visit(NodeTraversal.getTraverser(traversal), n, null);

        assertSame(Sets.newHashSet(), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_undefined_extern() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(Sets.newHashSet("bar"), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_undefined_extern_sanityCheck() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, true);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        thrown.expect(IllegalStateException.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);
    }

    @Test
    public void testVisit_undefined_extern_strict() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);
        check.strictExternCheck = true;

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, thrown.getValue());
    }

    @Test
    public void testVisit_declared_extern() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertSame(Sets.newHashSet(), check.varsToDeclareInExterns);
    }

    @Test
    public void testVisit_declared_extern_strict() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);
        check.strictExternCheck = true;

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { var bar = 42; }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.STRICT_MODULE_DEP_ERROR, thrown.getValue());
    }

    @Test
    public void testVisit_undeclared_extern_moduleDep() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { module 'bar' {} }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, thrown.getValue());
    }

    @Test
    public void testVisit_undeclared_extern_moduleDep_strict() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, true);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { module 'bar' {} }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.VIOLATED_MODULE_DEP_ERROR, thrown.getValue());
    }

    @Test
    public void testVisit_undeclared_extern_moduleDep_missed() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, false);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { module 'baz' {} }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, thrown.getValue());
    }

    @Test
    public void testVisit_undeclared_extern_moduleDep_missed_strict() {
        Compiler compiler = new Compiler();
        VarCheck check = new VarCheck(compiler, true);

        Node externs = Node.newText("var bar = 42;");
        Node root = Node.newText("function foo() { module 'baz' {} }");

        check.process(externs, root);

        thrown.expect(Diagnostic.DiagnosticType.class);
        check.visit(NodeTraversal.getTraverser(traversal(externs, root)),
            NodeUtil.getVarNodeByName(root, "bar"), null);

        assertEquals(VarCheck.MISSING_MODULE_DEP_ERROR, thrown.getValue());
    }

    private NodeTraversal traversal(Node externs, Node root) {
        Scope scope = new Scope();
        return new NodeTraversal(compiler, externs, root, scope);
    }
}