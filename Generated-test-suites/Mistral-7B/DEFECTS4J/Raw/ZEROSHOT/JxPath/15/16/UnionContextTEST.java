import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext unionContext;
 private BasicNodeSet nodeSet;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB });
 nodeSet = (BasicNodeSet) unionContext.getNodeSet();
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPositionWithSingleContext() {
 BasicNodeSet nodeSetA = (BasicNodeSet) contextA.getNodeSet();
 nodeSetA.add(new NodePointer(new Object() {}.getClass().getName(), 1));
 unionContext.setPosition(0);
 assertEquals(1, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(1, nodeSet.size());
}

 @Test
 public void testSetPositionWithMultipleContexts() {
 contextB.nextSet();
 contextB.nextNode();
 contextB.getCurrentNodePointer().setNodeNumber(2);

 unionContext.setPosition(0);
 assertEquals(1, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(1, nodeSet.size());

 unionContext.setPosition(1);
 assertEquals(2, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(2, nodeSet.size());

 unionContext.nextNode();
 assertEquals(2, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(3, nodeSet.size());
}

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPositionOutOfBounds() {
 unionContext.setPosition(2);
}

 @Test
 public void testNextNodeWithSingleContext() {
 unionContext.nextNode();
 assertEquals(1, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(1, nodeSet.size());
}

 @Test
 public void testNextNodeWithMultipleContexts() {
 contextB.nextSet();
 contextB.nextNode();
 contextB.getCurrentNodePointer().setNodeNumber(2);

 unionContext.nextNode();
 assertEquals(1, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(1, nodeSet.size());

 unionContext.nextNode();
 assertEquals(2, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(3, nodeSet.size());

 unionContext.nextNode();
 assertEquals(2, unionContext.getCurrentNodePointer().getNodeNumber());
 assertEquals(3, nodeSet.size());
}

 @Test(expected = NoSuchElementException.class)
 public void testNextNodeAfterLastNode() {
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode();
 unionContext.nextNode();
}
}