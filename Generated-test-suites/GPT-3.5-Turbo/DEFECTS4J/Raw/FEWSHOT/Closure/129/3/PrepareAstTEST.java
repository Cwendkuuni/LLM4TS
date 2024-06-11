package com.google.javascript.jscomp;
import static org.junit.Assert.*;

import org.junit.Test;

public class PrepareAstTest {
	@Test
	public void testProcess() {
		Compiler compiler = new Compiler();
		Node externs = new Node(Token.EMPTY);
		Node root = new Node(Token.EMPTY);
		PrepareAst prepareAst = new PrepareAst(compiler);
		prepareAst.process(externs, root);
	}

	@Test
	public void testNormalizeNodeTypes() {
		PrepareAst prepareAst = new PrepareAst(null);
		Node n = new Node(Token.EMPTY);
		prepareAst.normalizeNodeTypes(n);
	}

	@Test
	public void testNormalizeBlocks() {
		PrepareAst prepareAst = new PrepareAst(null);
		Node n = new Node(Token.EMPTY);
		prepareAst.normalizeBlocks(n);
	}

	@Test
	public void testPrepareAnnotations() {
		PrepareAst.PrepareAnnotations prepareAnnotations = new PrepareAst.PrepareAnnotations();

		NodeTraversal t = new NodeTraversal(null, null);
		Node n = new Node(Token.EMPTY);
		Node parent = new Node(Token.EMPTY);

		assertTrue(prepareAnnotations.shouldTraverse(t, n, parent));
		prepareAnnotations.visit(t, n, parent);
	}
}