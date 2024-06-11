package com.google.javascript.jscomp;

import static org.junit.Assert.*;
import org.junit.Test;

public class PrepareAstTest {
	@Test
	public void testNormalizeNodeTypes() {
		AbstractCompiler compiler = new AbstractCompiler();
		PrepareAst prepareAst = new PrepareAst(compiler);
		
		Node root = new Node(Token.ROOT);
		Node externs = new Node(Token.EXTERN);
		
		prepareAst.process(externs, root);
		
		// Assert the changes made in normalizeNodeTypes method
		// Check if all blocks are added to IF, WHILE, DO, etc.
		assertEquals(Token.BLOCK, root.getChild(0).getType());
		assertEquals(Token.BLOCK, root.getChild(1).getChild(1).getType());
		assertEquals(Token.BLOCK, root.getChild(2).getType());
	}
	
	@Test
	public void testPrepareAnnotations() {
		AbstractCompiler compiler = new AbstractCompiler();
		PrepareAst prepareAst = new PrepareAst(compiler, false);
		
		Node root = new Node(Token.ROOT);
		Node externs = new Node(Token.EXTERN);
		
		prepareAst.process(externs, root);
		
		// Assert the changes made in PrepareAnnotations method
		// Check if all calls without explicit "this" are annotated as free calls
		assertTrue(root.getChild(0).getBooleanProp(Node.FREE_CALL));
		assertTrue(root.getChild(1).getChild(1).getBooleanProp(Node.DIRECT_EVAL));
		
		// Check if dispatchers are annotated correctly
		assertTrue(root.getChild(2).getChild(0).getBooleanProp(Node.IS_DISPATCHER));
	}
}