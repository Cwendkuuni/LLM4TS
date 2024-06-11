import org.apache.commons.jxpath.EvalContext;
import org.apache.commons.jxpath.NodePointer;
import org.apache.commons.jxpath.ri.model.BasicNodeSet;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class UnionContextTest {

 private UnionContext unionContext;
 private EvalContext contextA;
 private EvalContext contextB;
 private EvalContext parentContext;

 @Before
 public void setUp() {
 contextA = new EvalContext(new BasicNodeSet());
 contextB = new EvalContext(new BasicNodeSet());
 parentContext = new EvalContext(new BasicNodeSet());
 unionContext = new UnionContext(parentContext, new EvalContext[] { contextA, contextB });
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition_whenBothContextsHaveNodes_shouldReturnTrue() {
 contextA.add(new NodePointer(0, "A1"));
 contextB.add(new NodePointer(0, "B1"));
 unionContext.setPosition(0);
 assertTrue(unionContext.getNodeSet().contains(new NodePointer(0, "A1")) && unionContext.getNodeSet().contains(new NodePointer(0, "B1")));
 }

 @Test(expected = IndexOutOfBoundsException.class)
 public void testSetPosition_whenPositionIsBeyondLastElement_shouldThrowException() {
 contextA.add(new NodePointer(0, "A1"));
 contextB.add(new NodePointer(0, "B1"));
 unionContext.setPosition(unionContext.getNodeSet().size());
 }

 @Test
 public void testSetPosition_whenBothContextsAreEmpty_shouldReturnFalse() {
 unionContext.setPosition(0);
 assertFalse(unionContext.getNodeSet().isEmpty());
 }

 @Test
 public void testSetPosition_whenOneContextHasNodesAndAnotherIsEmpty_shouldReturnFalse() {
 contextA.add(new NodePointer(0, "A1"));
 unionContext.setPosition(0);
 assertFalse(unionContext.getNodeSet().isEmpty());
 }

 @Test
 public void testSetPosition_whenOneContextHasNodesAndAnotherHasNodes_shouldReturnTrue() {
 contextA.add(new NodePointer(0, "A1"));
 contextB.add(new NodePointer(0, "B1"));
 unionContext.setPosition(0);
 assertTrue(unionContext.getNodeSet().contains(new NodePointer(0, "A1")) && unionContext.getNodeSet().contains(new NodePointer(0, "B1")));
 }
}