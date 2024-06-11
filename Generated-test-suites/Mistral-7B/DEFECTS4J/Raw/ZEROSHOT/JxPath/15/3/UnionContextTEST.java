import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private UnionContext unionContext;
 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext contextC;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet(), null);
 contextB = new EvalContext(new BasicNodeSet(), null);
 contextC = new EvalContext(new BasicNodeSet(), null);
 unionContext = new UnionContext(null, new EvalContext[] { contextA, contextB, contextC });
 }

 @Test
 public void testGetDocumentOrder() {
 assertTrue(unionContext.getDocumentOrder() > 0);
 }

 @Test
 public void testSetPosition_whenPositionIsOutsideOfContexts_returnsFalse() {
 final int position = -1;
 assertFalse(unionContext.setPosition(position));
 }

 @Test
 public void testSetPosition_whenPositionIsInsideContextA_returnsTrue() {
 contextA.add(new NodePointer("nodeA1"));
 contextA.add(new NodePointer("nodeA2"));
 contextB.add(new NodePointer("nodeB1"));
 unionContext.setPosition(0);
 assertTrue(unionContext.setPosition(0));
 }

 @Test
 public void testSetPosition_whenPositionIsInsideContextB_returnsTrue() {
 contextA.add(new NodePointer("nodeA1"));
 contextB.add(new NodePointer("nodeB1"));
 contextB.add(new NodePointer("nodeB2"));
 unionContext.setPosition(2);
 assertTrue(unionContext.setPosition(2));
 }

 @Test
 public void testSetPosition_whenPositionIsInsideContextC_returnsTrue() {
 contextA.add(new NodePointer("nodeA1"));
 contextB.add(new NodePointer("nodeB1"));
 contextC.add(new NodePointer("nodeC1"));
 unionContext.setPosition(3);
 assertTrue(unionContext.setPosition(3));
 }

 @Test
 public void testSetPosition_whenPositionIsInsideUnionContext_returnsTrue() {
 contextA.add(new NodePointer("nodeA1"));
 contextB.add(new NodePointer("nodeB1"));
 contextC.add(new NodePointer("nodeC1"));
 unionContext.setPosition(1); // position in the union context is the sum of positions in the contexts
 assertTrue(unionContext.setPosition(1));
 }
}