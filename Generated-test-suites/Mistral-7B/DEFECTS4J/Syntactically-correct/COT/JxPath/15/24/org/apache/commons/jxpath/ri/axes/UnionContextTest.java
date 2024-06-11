import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.axes.UnionContext;
import org.apache.commons.jxpath.ri.NodeSetContext;

public class UnionContextTest {

 private EvalContext context1, context2;
 private NodeSetContext nodeSetContext;
 private UnionContext unionContext;

 @Before
 public void setup() {
 // Create EvalContexts for testing
 context1 = new EvalContext(new BasicNodeSet()) {
 @Override
 public boolean nextSet() {
 return true;
 }

 @Override
 public boolean nextNode() {
 return true;
 }
 };
 context2 = new EvalContext(new BasicNodeSet()) {
 @Override
 public boolean nextSet() {
 return true;
 }

 @Override
 public boolean nextNode() {
 return true;
 }
 };

 // Create NodeSetContext for testing
 nodeSetContext = new NodeSetContext(new BasicNodeSet()) {
 @Override
 public boolean setPosition(int position) {
 return true;
 }
 };

 // Create UnionContext with given EvalContexts
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 }

 @Test
 public void testGetDocumentOrder() {
 assertEquals(1, unionContext.getDocumentOrder());
 }

 @Test
 public void testSetPosition_whenContextsAreEmpty_shouldReturnFalse() {
 assertFalse(unionContext.setPosition(0));
 }

 @Test
 public void testSetPosition_whenContextsAreNotEmpty_shouldReturnTrue() {
 // Given: UnionContext with two contexts
 // When: Setting position
 // Then: Position should be set correctly
 unionContext.setPosition(0);
 assertTrue(unionContext.nextNode());
 unionContext.setPosition(1);
 assertTrue(unionContext.nextNode());
 }

 @Test(expected = IllegalStateException.class)
 public void testSetPosition_whenContextsAreNotPrepared_shouldThrowException() {
 // Given: UnionContext with two contexts that are not prepared
 // When: Setting position
 // Then: An IllegalStateException should be thrown
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 assertFalse(unionContext.nextSet());
 assertFalse(unionContext.nextNode());
 assertFalse(unionContext.nextSet());
 assertFalse(unionContext.setPosition(0));
 }

 @Test
 public void testSetPosition_whenPositionIsOutOfBounds_shouldReturnFalse() {
 // Given: UnionContext with two contexts
 // When: Setting position to a position greater than the total number of nodes in the contexts
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet();
 unionContext.nextNode(); // Move to the first node of the first context
 unionContext.nextSet(); // Move to the next context
 unionContext.nextNode(); // Move to the first node of the second context
 int totalNodes = unionContext.getDocumentOrder() * 2; // Total number of nodes in both contexts
 unionContext.setPosition(totalNodes);
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsEqualToCurrentPosition_shouldReturnFalse() {
 // Given: UnionContext with two contexts and the current position is at the end of a context
 // When: Setting position to the current position
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet();
 unionContext.nextNode(); // Move to the first node of the first context
 unionContext.nextSet(); // Move to the next context
 unionContext.nextNode(); // Move to the first node of the second context
 assertFalse(unionContext.setPosition(unionContext.getDocumentOrder()));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsEqualToANodeInTheFirstContext_shouldReturnFalse() {
 // Given: UnionContext with two contexts and the position is at a node in the first context
 // When: Setting position to that position
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet();
 unionContext.nextNode(); // Move to the first node of the first context
 int position = unionContext.getDocumentOrder() - 1;
 assertFalse(unionContext.setPosition(position));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsEqualToANodeInTheSecondContext_shouldReturnFalse() {
 // Given: UnionContext with two contexts and the position is at a node in the second context
 // When: Setting position to that position
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet(); // Move to the first context
 unionContext.nextNode(); // Move to the first node of the first context
 unionContext.nextSet(); // Move to the next context
 unionContext.nextNode(); // Move to the first node of the second context
 int position = unionContext.getDocumentOrder();
 assertFalse(unionContext.setPosition(position));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsEqualToANodeThatHasBeenVisitedBefore_shouldReturnFalse() {
 // Given: UnionContext with two contexts and the position is at a node that has been visited before
 // When: Setting position to that position
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet(); // Move to the first context
 unionContext.nextNode(); // Move to the first node of the first context
 unionContext.nextSet(); // Move to the next context
 unionContext.nextNode(); // Move to the first node of the second context
 unionContext.nextNode(); // Move to the second node of the second context
 int position = unionContext.getDocumentOrder() * 2 - 2;
 assertFalse(unionContext.setPosition(position));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsEqualToANodeThatHasBeenVisitedAfterTheCurrentNode_shouldReturnFalse() {
 // Given: UnionContext with two contexts and the position is at a node that has been visited after the current node
 // When: Setting position to that position
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 unionContext.nextSet(); // Move to the first context
 unionContext.nextNode(); // Move to the first node of the first context
 unionContext.nextSet(); // Move to the next context
 unionContext.nextNode(); // Move to the second node of the second context
 unionContext.nextNode(); // Move to the third node of the second context
 int position = unionContext.getDocumentOrder() * 2 - 1;
 assertFalse(unionContext.setPosition(position));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsGreaterThanTheTotalNumberOfNodesInBothContexts_shouldReturnFalse() {
 // Given: UnionContext with two contexts
 // When: Setting position to a position greater than the total number of nodes in both contexts
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 int totalNodes = unionContext.getDocumentOrder() * 2;
 assertFalse(unionContext.setPosition(totalNodes + 1));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenPositionIsLessThanZero_shouldReturnFalse() {
 // Given: UnionContext with two contexts
 // When: Setting position to a position less than zero
 // Then: Position should not be set and nextNode should return false
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2 });
 int position = -1;
 assertFalse(unionContext.setPosition(position));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenContextsContainDuplicateNodes_shouldReturnFalse() {
 // Given: UnionContext with two contexts that contain duplicate nodes
 // When: Setting position to a position that corresponds to a duplicate node
 // Then: Position should not be set and nextNode should return false
 EvalContext context3 = new EvalContext(new BasicNodeSet()) {
 @Override
 public boolean nextSet() {
 return true;
 }

 @Override
 public boolean nextNode() {
 return true;
 }
 };
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context2, context3 });
 int totalNodes = unionContext.getDocumentOrder() * 3;
 assertFalse(unionContext.setPosition(totalNodes / 2));
 assertFalse(unionContext.nextNode());
 }

 @Test
 public void testSetPosition_whenContextsContainDuplicateNodes_shouldReturnFalse_2() {
 // Given: UnionContext with two contexts that contain duplicate nodes
 // When: Setting position to a position that corresponds to a duplicate node
 // Then: Position should not be set and nextNode should return false
 EvalContext context3 = new EvalContext(new BasicNodeSet()) {
 @Override
 public boolean nextSet() {
 return true;
 }

 @Override
 public boolean nextNode() {
 return true;
 }
 };
 unionContext = new UnionContext(nodeSetContext, new EvalContext[] { context1, context3, context2 });
 int totalNodes = unionContext.getDocumentOrder() * 3;
 assertFalse(unionContext.setPosition(totalNodes / 2));
 assertFalse(unionContext.nextNode());
 }
}