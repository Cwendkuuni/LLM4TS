package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.Token;

import java.util.logging.Logger;

public class UnreachableCodeEliminationTest {

	private final AbstractCompiler compiler = mock(AbstractCompiler.class);
	private UnreachableCodeElimination uce;
	private final Node externs = new Node(Token.BLOCK);
	private final Node root = new Node(Token.BLOCK);

	@Before
	public void setUp() {
		when(compiler.getLogger()).thenReturn(mock(Logger.class));
	}

	@Test
	public void testEnterScope() {
		uce = new UnreachableCodeElimination(compiler, true);
		NodeTraversal t = mock(NodeTraversal.class);
		Node root = new Node(Token.FUNCTION);
		uce.enterScope(t);
		verify(t, times(1)).getScope();
	}

	@Test
	public void testExitScope() {
		uce = new UnreachableCodeElimination(compiler, true);
		NodeTraversal t = mock(NodeTraversal.class);
		Node root = new Node(Token.FUNCTION);
		uce.enterScope(t);
		uce.exitScope(t);
		verify(t, times(1)).getScope();
	}

	@Test
	public void testProcess() {
		uce = new UnreachableCodeElimination(compiler, true);
		NodeTraversal.traverse(compiler, root, uce);
		verify(compiler, times(1)).reportCodeChange();
	}

	@Test
	public void testVisit() {
		uce = new UnreachableCodeElimination(compiler, true);
		NodeTraversal t = mock(NodeTraversal.class);
		Node n = new Node(Token.FUNCTION);
		Node parent = new Node(Token.BLOCK, n);
		uce.visit(t, n, parent);
		verify(compiler, times(1)).reportCodeChange();
	}

	@Test
	public void testTryRemoveUnconditionalBranching() {
		uce = new UnreachableCodeElimination(compiler, true);
		Node n = new Node(Token.BLOCK);
		Node result = uce.tryRemoveUnconditionalBranching(n);
		assertNull(result);
	}

	@Test
	public void testComputeFollowing() {
		uce = new UnreachableCodeElimination(compiler, true);
		Node n = new Node(Token.FUNCTION);
		Node result = uce.computeFollowing(n);
		assertNull(result);
	}

	@Test
	public void testRemoveDeadExprStatementSafely() {
		uce = new UnreachableCodeElimination(compiler, true);
		Node n = new Node(Token.BLOCK);
		Node parent = new Node(Token.BLOCK, n);
		uce.removeDeadExprStatementSafely(n);
		verify(compiler, times(1)).reportCodeChange();
		assertNull(n.getParent());
	}
}