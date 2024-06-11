import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA, contextB;
 private EvalContext unionContext;

 @Test
 public void testUnionContext() {
 // Given: Two EvalContexts with nodes
 contextA = new EvalContext(new JXPathContext(new Object[]{"nodeA1", "nodeA2", "nodeA3"}));
 contextB = new EvalContext(new JXPathContext(new Object[]{"nodeB1", "nodeB2", "nodeB3", "nodeA3"}));

 // When: Creating UnionContext
 unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, null);

 // Then: UnionContext should contain all nodes from contextA and contextB
 assertEquals(6, unionContext.getLength());
 NodePointer[] nodes = new NodePointer[6];
 for (int i = 0; i < 3; i++) {
 nodes[i] = (NodePointer) contextA.getCurrentNode();
 unionContext.nextNode();
 nodes[3 + i] = (NodePointer) contextB.getCurrentNode();
 unionContext.nextNode();
 }
 assertArrayEquals(nodes, unionContext.getNodes());
 }

 @Test
 public void testUnionContext_emptyContextB() {
 // Given: Two EvalContexts, contextB is empty
 contextA = new EvalContext(new JXPathContext(new Object[]{"nodeA1", "nodeA2", "nodeA3"}));
 contextB = new EvalContext(new JXPathContext(new Object[0]));

 // When: Creating UnionContext
 unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, null);

 // Then: UnionContext should contain all nodes from contextA
 assertEquals(3, unionContext.getLength());
 NodePointer[] nodes = new NodePointer[3];
 for (int i = 0; i < 3; i++) {
 nodes[i] = (NodePointer) contextA.getCurrentNode();
 unionContext.nextNode();
 }
 assertArrayEquals(nodes, unionContext.getNodes());
 }

 @Test
 public void testUnionContext_sameContexts() {
 // Given: Two identical EvalContexts
 contextA = new EvalContext(new JXPathContext(new Object[]{"nodeA1", "nodeA2", "nodeA3"}));
 contextB = new EvalContext(new JXPathContext(new Object[]{"nodeA1", "nodeA2", "nodeA3"}));

 // When: Creating UnionContext
 unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, null);

 // Then: UnionContext should contain all nodes from contextA and contextB (duplicates removed)
 assertEquals(3, unionContext.getLength());
 NodePointer[] nodes = new NodePointer[3];
 for (int i = 0; i < 3; i++) {
 nodes[i] = (NodePointer) contextA.getCurrentNode();
 unionContext.nextNode();
 nodes[3 + i] = (NodePointer) contextB.getCurrentNode();
 unionContext.nextNode();
 }
 // Check that nodes are the same, not just equals
 for (int i = 0; i < 3; i++) {
 assertTrue(nodes[i].getSource() == nodes[i + 3].getSource());
 }
 assertArrayEquals(nodes, unionContext.getNodes());
 }

 @Test
 public void testUnionContext_documentOrder() {
 // Given: Two EvalContexts in different document orders
 contextA = new EvalContext(new JXPathContext(new Object[]{"nodeA1", "nodeA2", "nodeA3"}));
 contextB = new EvalContext(new JXPathContext(new Object[]{"nodeB1", "nodeB2", "nodeB3", "nodeA3"}));

 // When: Creating UnionContext
 unionContext = new UnionContext(new EvalContext[]{contextA, contextB}, null);

 // Then: UnionContext should follow document order of contextB
 int position = 0;
 while (unionContext.nextNode()) {
 if (contextB.getCurrentNode().getValue().equals("nodeB1")) {
 position++;
 }
 assertEquals(position, unionContext.getPosition());
 }
 }
}