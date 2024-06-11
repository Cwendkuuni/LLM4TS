package com.google.javascript.jscomp;

import com.google.common.collect.ImmutableSet;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import java.util.Map;

import static com.google.javascript.rhino.NodeType.FUNCTION;
import static com.google.javascript.rhino.NodeType.NAME;
import static com.google.javascript.rhino.NodeType.VAR;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MakeDeclaredNamesUniqueTest {

    private MakeDeclaredNamesUnique makeDeclaredNamesUnique;
    @Mock
    private NodeTraversal nodeTraversal;
    @Mock
    private AbstractCompiler abstractCompiler;
    @Mock
    private Node node;
    @Mock
    private Scope scope;
    @Mock
    private Variable variable;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        when(node.getType()).thenAnswer(invocation -> invocation.getArgument(0));
        when(node.getParentNode()).thenReturn(null);
        when(node.getFirstChild()).thenReturn(mock(Node.class));
        when(node.getLastChild()).thenReturn(mock(Node.class));
        when(node.getString()).thenReturn("testName");
        when(scope.getVar(Mockito.anyString())).thenReturn(variable);
        when(scope.getRootNode()).thenReturn(mock(Node.class));
        when(variable.getNameNode()).thenReturn(node);
        when(variable.isGlobal()).thenReturn(false);
    }

    @Test
    public void testEnterScope() {
        makeDeclaredNamesUnique.enterScope(nodeTraversal);
        assertEquals(makeDeclaredNamesUnique.rootRenamer, nodeStack.peek());
    }

    @Test
    public void testExitScope() {
        makeDeclaredNamesUnique.enterScope(nodeTraversal);
        makeDeclaredNamesUnique.exitScope(nodeTraversal);
        assertEquals(nodeStack.size(), 0);
    }

    @Test
    public void testShouldTraverseFunctionDeclaration() {
        when(node.getType()).thenReturn(VAR);
        makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, node, null);
        verify(nodeTraversal, times(1)).enterScope(makeDeclaredNamesUnique);
    }

    @Test
    public void testShouldTraverseFunction() {
        when(node.getType()).thenReturn(FUNCTION);
        makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, node, null);
        verify(nodeTraversal, times(1)).enterScope(makeDeclaredNamesUnique);
    }

    @Test
    public void testShouldTraverseCatchBlock() {
        when(node.getType()).thenReturn(TOKEN.CATCH);
        makeDeclaredNamesUnique.shouldTraverse(nodeTraversal, node, null);
        verify(nodeTraversal, times(1)).enterScope(makeDeclaredNamesUnique);
    }

    @Test
    public void testVisitNameDeclaration() {
        when(node.getType()).thenReturn(NAME);
        makeDeclaredNamesUnique.visit(nodeTraversal, node, null);
        verify(node, times(1)).setString("testName$0");
    }

    @Test
    public void testVisitFunctionDeclaration() {
        when(node.getType()).thenReturn(FUNCTION);
        makeDeclaredNamesUnique.visit(nodeTraversal, node, null);
        verify(nodeStack, times(1)).pop();
    }

    @Test
    public void testVisitCatchBlock() {
        when(node.getType()).thenReturn(TOKEN.CATCH);
        makeDeclaredNamesUnique.visit(nodeTraversal, node, null);
        verify(nodeStack, times(1)).pop();
    }

    @Test
    public void testFindDeclaredNames() {
        Node functionNode = mock(Node.class);
        Node varNode1 = mock(Node.class);
        Node varNode2 = mock(Node.class);
        when(functionNode.getFirstChild()).thenReturn(varNode1);
        when(functionNode.getLastChild()).thenReturn(varNode2);
        when(node.getType()).thenReturn(FUNCTION);

        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.findDeclaredNames(functionNode, null, null);

        verify(varNode1, times(1)).setString("testName$0");
        verify(varNode2, times(1)).setString("testName$1");
    }

    @Test
    public void testAddDeclaredName() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.addDeclaredName("testName");
        Map<String, String> declarations = makeDeclaredNamesUnique.declarations;
        assertEquals("testName$0", declarations.get("testName"));
    }

    @Test
    public void testGetReplacementName() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.addDeclaredName("testName");
        String replacementName = makeDeclaredNamesUnique.getReplacementName("testName");
        assertEquals("testName$0", replacementName);
    }

    @Test
    public void testStripConstIfReplaced() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        makeDeclaredNamesUnique.addDeclaredName("testName");
        boolean result = makeDeclaredNamesUnique.stripConstIfReplaced();
        assertFalse(result);
    }

    @Test
    public void testForChildScope() {
        MakeDeclaredNamesUnique makeDeclaredNamesUnique = new MakeDeclaredNamesUnique();
        MakeDeclaredNamesUnique childRenamer = makeDeclaredNamesUnique.forChildScope();
        assertEquals(makeDeclaredNamesUnique.class, childRenamer.getClass());
    }

    private Deque<Renamer> nodeStack = new ArrayDeque<>();
}