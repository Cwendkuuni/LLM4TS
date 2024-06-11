import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.axes.NodeSetContext;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;

public class UnionContextTest {

 private EvalContext context1, context2;
 private UnionContext unionContext;

 @Before
 public void setUp() {
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(new NodeSetContext(null, new BasicNodeSet()), new EvalContext[] { context1, context2 });
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition_whenBothContextsHaveNodes_shouldReturnTrue() {
 context1.add(new NodePointer(1));
 context2.add(new NodePointer(2));
 unionContext.setPosition(0); // Position in union context should be 0, as it's the first node in either context
 assertTrue(unionContext.nextNode()); // First node from context1
 assertTrue(unionContext.nextNode()); // First node from context2
 }

 @Test
 public void testSetPosition_whenBothContextsAreEmpty_shouldReturnFalse() {
 assertFalse(unionContext.nextNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_whenNotPrepared_shouldThrowException() {
 unionContext.setPosition(1);
 }

 @Test
 public void testSetPosition_whenPositionIsGreaterThanTotalNodes_shouldReturnFalse() {
 context1.add(new NodePointer(1));
 context2.add(new NodePointer(2));
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode(); // Position in union context should be 2
 assertFalse(unionContext.nextNode()); // Position is greater than the total number of nodes
 }

 @Test
 public void testSetPosition_whenPositionIsLessThanZero_shouldReturnFalse() {
 context1.add(new NodePointer(1));
 context2.add(new NodePointer(2));
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode(); // Position in union context should be 2
 assertFalse(unionContext.setPosition(-1)); // Position is less than zero
 }
}