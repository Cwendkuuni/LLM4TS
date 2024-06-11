import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

public class ScopedAliasesTest {
	private ScopedAliases scopedAliases;
	
	@Before
	public void setUp() {
		scopedAliases = new ScopedAliases();
	}
	
	@Test
	public void testEnterScope() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node();
		
		scopedAliases.enterScope(t);
		
		assertEquals(t.getCurrentNode().getParent(), n);
	}
	
	@Test
	public void testExitScope() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node();
		
		scopedAliases.exitScope(t);
		
		assertEquals(t.getScopeDepth(), null);
	}
	
	@Test
	public void testShouldTraverse_shouldReturnFalse() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node();
		Node parent = new Node();
		
		Boolean result = scopedAliased.shouldTraverse(t, n, parent);
		
		assertEquals(result, null);
	}
	
	@Test
	public void testVisit_whenIsCallToScopeMethod() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node();
		Node parent = new Node();
		
		Boolean result = scopedAliases.visit(t, n, parent);
		
		assertEquals(result, null);
	}
	
	@Test
	public void testVisit_whenGetScopeDepthEqualsTwo() {
		NodeTraversal t = new NodeTraversal();
		Node n = new Node();
		Node parent = new Node();
		
		Boolean result = scopedAliases.visit(t, n, parent);
		
		assertEquals(result, null);
	}
	
	@Test
	public void testFixTypeNode_whenTypeNodeIsString() {
		Node typeNode = new Node();
		
		scopedAliases.fixTypeNode(typeNode);
		
		assertEquals(typeNode.getFirstChild(), child);
	}
	
	@Test
	public void testFixTypeNode_whenChildIsNull() {
		Node typeNode = new Node();
		
		scopedAliases.fixTypeNode(typeNode);
		
		assertEquals(typeNode.getFirstChild(), null);
	}
}