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
 EqualssExpectedValue(unionContext.getDocumentOrder(), 1);
 }

 @Test
 public void testSetPosition_whenBothContextsHaveNodes_shouldReturnTrue() {
 contextA.add("a1");
 contextA.add("a2");
 contextB.add("b1");
 contextB.add("b2");
 contextA.nextSet();
 contextB.nextSet();

 assertTrue(unionContext.setPosition(0));
 assertEquals("a1", nodeSet.getNode(0).getValue());

 contextA.nextNode();
 contextB.nextNode();

 assertTrue(unionContext.setPosition(1));
 assertEquals("a2", nodeSet.getNode(1).getValue());

 contextB.nextNode(); // b2

 assertTrue(unionContext.setPosition(2));
 assertEquals("b1", nodeSet.getNode(2).getValue());
 }

 @Test
 public void testSetPosition_whenFirstContextHasNoNodes_shouldReturnFalse() {
 contextA.nextSet();
 contextB.add("b1");
 contextB.nextNode();

 assertFalse(unionContext.setPosition(0));
 }

 @Test
 public void testSetPosition_whenSecondContextHasNoNodes_shouldReturnFalse() {
 contextA.add("a1");
 contextA.add("a2");
 contextA.nextSet();
 contextB.nextNode();

 assertFalse(unionContext.setPosition(0));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_whenPositionIsOutOfBounds() {
 contextA.add("a1");
 contextA.add("a2");
 unionContext.setPosition(3);
 }

 private void EqualssExpectedValue(int actualValue, int expectedValue) {
 assertEquals("Unexpected document order value: " + actualValue, expectedValue, actualValue);
 }
}