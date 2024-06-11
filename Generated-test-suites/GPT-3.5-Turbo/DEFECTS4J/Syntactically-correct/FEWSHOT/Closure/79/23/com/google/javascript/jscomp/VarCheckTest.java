package com.google.javascript.jscomp;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;

public class VarCheckTest {

    private VarCheck varCheck;
    private AbstractCompiler compiler;
    private Node externs;
    private Node root;

    @Before
    public void setUp() {
        compiler = mock(AbstractCompiler.class);
        varCheck = new VarCheck(compiler);
        externs = mock(Node.class);
        root = mock(Node.class);
    }

    @Test
    public void testProcess() {
        CompilerInput input = mock(CompilerInput.class);
        NodeTraversal traversal = mock(NodeTraversal.class);

        when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
        when(compiler.getErrorLevel(JSError.make("", 0, 0, VarCheck.UNDEFINED_EXTERN_VAR_ERROR))).thenReturn(CheckLevel.ERROR);
        when(compiler.getModuleGraph()).thenReturn(mock(JSModuleGraph.class));
        when(compiler.newExternInput("{SyntheticVarsDeclar}")).thenReturn(input);
        when(input.getAstRoot(compiler)).thenReturn(mock(Node.class));

        varCheck.process(externs, root);

        verify(NodeTraversal.class).traverse(compiler, externs, varCheck.getNameRefInExternsCheck());
        verify(NodeTraversal.class).traverseRoots(compiler, Lists.newArrayList(externs, root), varCheck);
        verify(compiler).getModuleGraph();
    }

    @Test
    public void testVisit() {
        NodeTraversal traversal = mock(NodeTraversal.class);
        Scope.Var var = mock(Scope.Var.class);
        CompilerInput input = mock(CompilerInput.class);
        CompilerInput currInput = mock(CompilerInput.class);
        JSModule currModule = mock(JSModule.class);
        JSModule varModule = mock(JSModule.class);
        JSModuleGraph moduleGraph = mock(JSModuleGraph.class);
        Node n = mock(Node.class);
        Node parent = mock(Node.class);
        Scope scope = mock(Scope.class);

        when(traversal.getScope()).thenReturn(scope);
        when(traversal.getInput()).thenReturn(currInput);
        when(currInput.getModule()).thenReturn(currModule);
        when(var.input).thenReturn(input);
        when(compiler.getCodingConvention()).thenReturn(mock(CodingConvention.class));
        when(compiler.getErrorLevel(JSError.make("", 0, 0, VarCheck.UNDEFINED_EXTERN_VAR_ERROR))).thenReturn(CheckLevel.ERROR);
        when(compiler.getModuleGraph()).thenReturn(moduleGraph);
        when(var.input.getModule()).thenReturn(varModule);
        when(compiler.newExternInput("{SyntheticVarsDeclar}")).thenReturn(input);
        when(input.getAstRoot(compiler)).thenReturn(mock(Node.class));
        when(n.getType()).thenReturn(Token.NAME);
        when(parent.getType()).thenReturn(Token.VAR);
        when(n.getString()).thenReturn("foo");
        when(var.input).thenReturn(input);

        varCheck.visit(traversal, n, parent);

        verify(compiler).getModuleGraph();
    }
}