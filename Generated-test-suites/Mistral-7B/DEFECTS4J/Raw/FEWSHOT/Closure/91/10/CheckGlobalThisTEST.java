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

import java.util.Arrays;

public class CheckGlobalThisTest {

    @Test
    public void testShouldTraverse_function() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(new Node(Token.IDENT, "functionName")));

        Node parentNode1 = new Node(Token.SCRIPT, Arrays.asList(functionNode));
        Node parentNode2 = new Node(Token.BLOCK, Arrays.asList(new Node(Token.ASSIGN, Arrays.asList(functionNode, new Node(Token.IDENT, "someVariable")))));

        assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode1));
        assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode2));
    }

    @Test
    public void testShouldTraverse_functionWithJSDoc() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName"),
            new Node(Token.ATKEYWORD, "@constructor")
        ));

        Node parentNode = new Node(Token.SCRIPT, Arrays.asList(functionNode));

        assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldTraverse_functionWithThisJSDoc() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName"),
            new Node(Token.ATKEYWORD, "@this")
        ));

        Node parentNode = new Node(Token.SCRIPT, Arrays.asList(functionNode));

        assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldTraverse_functionWithNoLogicalPlaceForThisAnnotation() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName")
        ));

        Node parentNode = new Node(Token.BLOCK, Arrays.asList(new Node(Token.ASSIGN, Arrays.asList(functionNode, new Node(Token.IDENT, "someVariable")))));

        assertTrue(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldNotTraverse_constructor() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node constructorNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "constructorName"),
            new Node(Token.ATKEYWORD, "@constructor")
        ));

        Node parentNode = new Node(Token.SCRIPT, Arrays.asList(constructorNode));

        assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), constructorNode, parentNode));
    }

    @Test
    public void testShouldNotTraverse_functionWithOverrideAnnotation() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName"),
            new Node(Token.ATKEYWORD, "@override")
        ));

        Node parentNode = new Node(Token.SCRIPT, Arrays.asList(functionNode));

        assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldNotTraverse_prototypePropertyAccess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName")
        ));

        Node prototypeNode = new Node(Token.GETPROP, Arrays.asList(new Node(Token.THIS), new Node(Token.IDENT, "prototype")));
        Node parentNode = new Node(Token.BLOCK, Arrays.asList(prototypeNode, functionNode));

        assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldNotTraverse_prototypePropertyAccessDeep() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName")
        ));

        Node prototypeNode1 = new Node(Token.GETPROP, Arrays.asList(new Node(Token.THIS), new Node(Token.IDENT, "prototype")));
        Node prototypeNode2 = new Node(Token.GETPROP, Arrays.asList(prototypeNode1, new Node(Token.IDENT, "someProperty")));
        Node parentNode = new Node(Token.BLOCK, Arrays.asList(prototypeNode2, functionNode));

        assertFalse(checker.shouldTraverse(mock(NodeTraversal.class), functionNode, parentNode));
    }

    @Test
    public void testShouldReportThis_functionWithPropertyAccess() {
        AbstractCompiler compiler = mock(AbstractCompiler.class);
        CheckLevel level = CheckLevel.WARNS;
        CheckGlobalThis checker = new CheckGlobalThis(compiler, level);

        Node functionNode = new Node(Token.FUNCTION, Arrays.asList(
            new Node(Token.IDENT, "functionName")
        ));

        Node parentNode = new Node(Token.BLOCK, Arrays.asList(
            new Node(Token.GETPROP, Arrays.asList(functionNode, new Node(Token.IDENT, "prototype"))),
            functionNode
        ));

        assertTrue(checker.shouldReportThis(parentNode, functionNode));
    }
}