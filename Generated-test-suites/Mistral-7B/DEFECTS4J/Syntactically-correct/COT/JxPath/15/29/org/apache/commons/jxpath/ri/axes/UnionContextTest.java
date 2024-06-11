import org.apache.commons.jxpath.BasicNodeSet;
import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class UnionContextTest {

 private EvalContext context1, context2;
 private UnionContext unionContext;

 @Before
 public void setUp() {
 context1 = new EvalContext(new BasicNodeSet());
 context2 = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(new EvalContext[]{context1, context2}, null);
 }

 @Test
 public void testGetDocumentOrder() {
 assertFalse(unionContext.getDocumentOrder() < 0);
 assertTrue(unionContext.getDocumentOrder() > 0);
 }

 @Test
 public void testSetPosition_whenPositionIsValid_returnsTrue() {
 int position = 0;
 assertTrue(unionContext.setPosition(position));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_whenPositionIsNegative_throwsException() {
 int position = -1;
 assertFalse(unionContext.setPosition(position));
 }

 @Test
 public void testSetPosition_whenPositionIsGreaterThanSize_returnsFalse() {
 int size = ((BasicNodeSet) unionContext.getNodeSet()).size();
 int position = size + 1;
 assertFalse(unionContext.setPosition(position));
 }

 @Test
 public void testSetPosition_whenContextsArePrepared_returnsTrue() {
 unionContext.setPosition(0);
 unionContext.prepare();
 int position = 1;
 assertTrue(unionContext.setPosition(position));
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_whenContextsAreNotPrepared_throwsException() {
 unionContext.setPosition(0);
 int position = 1;
 assertFalse(unionContext.setPosition(position));
 }

 @Test
 public void testCurrentNodePointer_whenContextIsNotEmpty_returnsCurrentNodePointer() {
 unionContext.nextSet();
 NodePointer expected = unionContext.getCurrentNodePointer();
 NodePointer actual = unionContext.nextNode();
 assertEquals(expected, actual);
 }

 @Test
 public void testCurrentNodePointer_whenContextIsEmpty_returnsNull() {
 assertNull(unionContext.currentNodePointer());
 }

 @Test
 public void testCurrentNodePointer_whenContextHasReachedEnd_returnsNull() {
 unionContext.nextSet();
 unionContext.nextNode();
 unionContext.nextNode();
 assertNull(unionContext.currentNodePointer());
 }

 private void addNodePointerToContext(EvalContext context, int numberOfNodes) {
 for (int i = 0; i < numberOfNodes; i++) {
 context.nextNode();
 }
 }

 @Test
 public void testSetPosition_whenTwoContextsWithDifferentNumberOfNodes_returnsTrue() {
 addNodePointerToContext(context1, 3);
 addNodePointerToContext(context2, 1);
 unionContext.nextSet();
 unionContext.nextNode();
 int position = 1;
 assertTrue(unionContext.setPosition(position));
 }

 @Test
 public void testSetPosition_whenTwoContextsWithSameNodeAtGivenPosition_returnsTrue() {
 addNodePointerToContext(context1, 2);
 addNodePointerToContext(context2, 2);
 unionContext.nextSet();
 int position = 0;
 assertTrue(unionContext.setPosition(position));
 NodePointer expected = context1.getCurrentNodePointer();
 NodePointer actual = unionContext.currentNodePointer();
 assertEquals(expected, actual);
 }

 @Test
 public void testSetPosition_whenTwoContextsWithSameNodeAtGivenPositionButDifferentContext_returnsTrue() {
 addNodePointerToContext(context1, 2);
 addNodePointerToContext(context2, 2);
 unionContext.nextSet();
 int position = 0;
 assertTrue(unionContext.setPosition(position));
 NodePointer expectedContext1 = context1.getCurrentNodePointer();
 NodePointer expectedContext2 = context2.getCurrentNodePointer();
 NodePointer actual = unionContext.currentNodePointer();
 assertNotNull(actual);
 assertEquals(expectedContext1, expectedContext2);
 assertEquals(expectedContext1, actual);
 }

 @Test
 public void testSetPosition_whenTwoContextsWithDifferentNodesAtGivenPosition_returnsTrue() {
 addNodePointerToContext(context1, 2);
 addNodePointerToContext(context2, 3);
 unionContext.nextSet();
 int position = 0;
 assertTrue(unionContext.setPosition(position));
 NodePointer expectedContext1 = context1.getCurrentNodePointer();
 NodePointer expectedContext2 = context2.getCurrentNodePointer();
 NodePointer actual = unionContext.currentNodePointer();
 assertNotNull(actual);
 assertNotEquals(expectedContext1, expectedContext2);
 assertNotEquals(expectedContext1, actual);
 assertNotEquals(expectedContext2, actual);
 }

}