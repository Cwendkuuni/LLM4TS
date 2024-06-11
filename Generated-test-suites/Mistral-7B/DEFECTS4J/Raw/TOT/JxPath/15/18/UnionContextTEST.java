import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA, contextB;
 private UnionContext unionContext;

 @Before
 public void setUp() {
 // Setup contexts A and B
 contextA = new EvalContext(new Object(), new BasicNodeSet());
 contextB = new EvalContext(new Object(), new BasicNodeSet());

 // Prepare contexts A and B with some nodes
 contextA.pushContext("root", new NodePointer("A1"));
 contextA.pushContext("root/childA", new NodePointer("A2"));
 contextB.pushContext("root", new NodePointer("B1"));
 contextB.pushContext("root/childB", new NodePointer("B2"));
 }

 @Test
 public void testUnionContextWithEmptyContexts() {
 unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});
 assertFalse(unionContext.nextSet());
 }

 @Test
 public void testUnionContextWithSingleContext() {
 // Test the case when one context has nodes and the other one is empty
 unionContext = new UnionContext(null, new EvalContext[] {contextA, new EvalContext(new Object(), new BasicNodeSet())});
 assertTrue(unionContext.nextSet());
 assertEquals("A1", unionContext.getCurrentNode().getPath());
 assertTrue(unionContext.nextNode());
 assertEquals("A2", unionContext.getCurrentNode().getPath());

 // Test the case when the other context has nodes
 unionContext = new UnionContext(null, new EvalContext[] {new EvalContext(new Object(), new BasicNodeSet()), contextA});
 assertTrue(unionContext.nextSet());
 assertEquals("A1", unionContext.getCurrentNode().getPath());
 assertTrue(unionContext.nextNode());
 assertEquals("A2", unionContext.getCurrentNode().getPath());
 }

 @Test
 public void testUnionContextWithBothContextsWithNodes() {
 // Test the case when both contexts have nodes
 unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});
 assertTrue(unionContext.nextSet());
 assertEquals("A1", unionContext.getCurrentNode().getPath());
 assertTrue(unionContext.nextNode());
 assertEquals("A2", unionContext.getCurrentNode().getPath());

 assertTrue(unionContext.nextSet()); // Next set should contain nodes from contextB
 assertEquals("B1", unionContext.getCurrentNode().getPath());
 assertTrue(unionContext.nextNode());
 assertEquals("B2", unionContext.getCurrentNode().getPath());
 }

 @Test(expected = IllegalStateException.class)
 public void testUnionContextWhenBothContextsHaveSameNode() {
 // Test the case when both contexts have the same node
 contextA.pushContext("root", new NodePointer("A1"));
 contextB.pushContext("root", new NodePointer("A1"));
 unionContext = new UnionContext(null, new EvalContext[] {contextA, contextB});
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextSet(); // This should throw an exception
 }
}