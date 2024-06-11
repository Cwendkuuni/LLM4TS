import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 @Test
 public void testUnionContext() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 assertEquals(0, unionContext.getDocumentOrder());
 assertTrue(unionContext.getNodeSet() instanceof BasicNodeSet);

 UnionContext unionContext1 = new UnionContext(null, new EvalContext[] { new EvalContext(null, new BasicNodeSet()) });
 assertEquals(1, unionContext1.getDocumentOrder());
 BasicNodeSet nodeSet1 = (BasicNodeSet) unionContext1.getNodeSet();
 assertTrue(nodeSet1.isEmpty());

 EvalContext context1 = new EvalContext(null, new BasicNodeSet());
 context1.add(new NodePointer(1));
 context1.add(new NodePointer(2));
 UnionContext unionContext2 = new UnionContext(null, new EvalContext[] { context1 });
 assertEquals(1, unionContext2.getDocumentOrder());
 BasicNodeSet nodeSet2 = (BasicNodeSet) unionContext2.getNodeSet();
 assertFalse(nodeSet2.isEmpty());
 assertEquals(2, nodeSet2.size());
 NodePointer nodePointer1 = nodeSet2.get(0);
 assertEquals(1, nodePointer1.getNodeNumber());
 NodePointer nodePointer2 = nodeSet2.get(1);
 assertEquals(2, nodePointer2.getNodeNumber());

 UnionContext unionContext3 = new UnionContext(unionContext2, new EvalContext[] { new EvalContext(null, new BasicNodeSet()) });
 assertEquals(1, unionContext3.getDocumentOrder());
 BasicNodeSet nodeSet3 = (BasicNodeSet) unionContext3.getNodeSet();
 assertFalse(nodeSet3.isEmpty());
 assertEquals(3, nodeSet3.size());
 NodePointer nodePointer3 = nodeSet3.get(2);
 assertNull(nodePointer3); // new context should not contain nodes from previous contexts
 }

 @Test
 public void testSetPosition() {
 UnionContext unionContext = new UnionContext(null, new EvalContext[0]);
 unionContext.setPosition(0);
 assertFalse(unionContext.nextSet());
 unionContext.setPosition(1);
 assertFalse(unionContext.nextSet());
 unionContext.setPosition(0);
 assertTrue(unionContext.nextSet());
 UnionContext unionContext1 = new UnionContext(null, new EvalContext[] { new EvalContext(null, new BasicNodeSet()) });
 unionContext1.setPosition(0);
 unionContext1.nextSet();
 unionContext1.nextNode();
 unionContext1.nextNode();
 unionContext1.setPosition(0);
 assertTrue(unionContext1.nextSet());
 unionContext1.nextNode();
 unionContext1.nextNode();
 unionContext1.nextNode();
 unionContext1.nextNode();
 unionContext1.nextNode();
 assertFalse(unionContext1.nextNode());
 }
}