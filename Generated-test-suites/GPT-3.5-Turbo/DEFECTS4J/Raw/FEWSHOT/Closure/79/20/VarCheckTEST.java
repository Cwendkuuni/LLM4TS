package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import com.google.javascript.rhino.Node;
import junit.framework.TestCase;
import org.junit.Before;
import org.junit.Test;

public class VarCheckTest extends TestCase {

    private VarCheck varCheck;
    private AbstractCompiler compiler;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        varCheck = new VarCheck(compiler);
    }

    @Test
    public void testProcess() {
        Node externs = new Node(Token.EXTERN_FUNCTION);

        Node root = new Node(Token.ROOT);
        Node varNode = new Node(Token.VAR, new Node(Token.NAME, "foo"));
        root.addChildToBack(varNode);

        varCheck.process(externs, root);

        verify(compiler).getModuleGraph();
    }

    @Test
    public void testVisit_variableDeclaredElsewhere() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.VAR, new Node(Token.NAME, "foo"));

        varCheck.visit(t, n, parent);

        Node synthesizedExterns = varCheck.getSynthesizedExternsRoot();
        verify(synthesizedExterns).addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "foo")));
    }

    @Test
    public void testVisit_varNotDeclared() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.VAR, new Node(Token.NAME, "bar"));

        varCheck.visit(t, n, parent);

        verify(t).report(n, VarCheck.UNDEFINED_VAR_ERROR, "foo");
        Node synthesizedExterns = varCheck.getSynthesizedExternsRoot();
        verify(synthesizedExterns).addChildToBack(new Node(Token.VAR, new Node(Token.NAME, "foo")));
    }

    @Test
    public void testVisit_currInputEqVarInput() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.VAR, new Node(Token.NAME, "foo"));

        CompilerInput currInput = new CompilerInput(null);
        CompilerInput varInput = new CompilerInput(null);
        t.setInput(currInput);
        Scope.Var var = new Scope.Var("foo", null, varInput);
        NodeUtil.setIntProp(n, Node.IS_CONSTANT_NAME, 1);
        NodeUtil.setBooleanProp(n, Node.IS_CONSTANT_VAR, true);

        varCheck.visit(t, n, parent);

        assertNull(varCheck.getSynthesizedExternsInput());
    }

    @Test
    public void testVisit_currInputNull() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME, "foo");
        Node parent = new Node(Token.VAR, new Node(Token.NAME, "foo"));

        CompilerInput currInput = new CompilerInput(null);
        CompilerInput varInput = new CompilerInput(null);
        t.setInput(currInput);
        Scope.Var var = new Scope.Var("foo", null, varInput);
        currInput = null;
        varInput = null;
        NodeUtil.setIntProp(n, Node.IS_CONSTANT_NAME, 1);
        NodeUtil.setBooleanProp(n, Node.IS_CONSTANT_VAR, true);

        varCheck.visit(t, n, parent);

        assertNull(varCheck.getSynthesizedExternsInput());
    }

    @Test
    public void testVisit_varNameEmpty() {
        NodeTraversal t = mock(NodeTraversal.class);
        Node n = new Node(Token.NAME, "");
        Node parent = new Node(Token.FUNCTION);

        varCheck.visit(t, n, parent);

        verify(t).report(n, VarCheck.INVALID_FUNCTION_DECL);
    }
}