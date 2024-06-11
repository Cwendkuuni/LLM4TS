import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.model.Axis;
import org.apache.commons.jxpath.ri.model.QName;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class AttributeContextTest {

 private EvalContext parentContext;
 private NodeTest nodeTest;
 private AttributeContext attributeContext;

 @Test
 public void testAttributeContextConstruction() {
 // Given
 parentContext = mock(EvalContext.class);
 nodeTest = mock(NodeTest.class);

 // When
 AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);

 // Then
 Assert.assertSame(parentContext, attributeContext.getParentContext());
 Assert.assertSame(nodeTest, attributeContext.getNodeTest());
 }

 @Test
 public void testReset() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);

 // When
 attributeContext.reset();

 // Then
 Assert.assertFalse(attributeContext.isStarted());
 Assert.assertNull(attributeContext.getIterator());
 Assert.assertNull(attributeContext.getCurrentNodePointer());
 }

 @Test
 public void testSetPositionWithInvalidPosition() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);

 // When & Then
 Assert.assertFalse(attributeContext.setPosition(-1));
 }

 @Test
 public void testSetPositionWithValidPosition() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 int position = 1;

 // When
 attributeContext.reset();
 NodePointer currentNodePointer = mock(NodePointer.class);
 when(attributeContext.nextNode()).thenReturn(true).thenReturn(true);
 when(attributeContext.getCurrentNodePointer()).thenReturn(currentNodePointer);
 doReturn(position).when(currentNodePointer).getPosition();

 // When
 attributeContext.setPosition(position);

 // Then
 Assert.assertTrue(attributeContext.getCurrentPosition() == position);
 }

 @Test
 public void testNextNodeWithNoAttributeNameTest() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);

 // When
 AttributeException exception = assertThrows(AttributeException.class, () -> attributeContext.nextNode());

 // Then
 Assert.assertEquals("No attribute test provided", exception.getMessage());
 }

 @Test
 public void testNextNodeWithNoAttributeNameIterator() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 AttributeException exception = null;

 // When
 while (!attributeContext.nextNode()) {
 // Do nothing
 }

 // Then
 Assert.assertNull(exception);
 }

 @Test
 public void testNextNodeWithAttributeNameIteratorWithNoNextNode() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator iterator = mock(NodeIterator.class);
 AttributeException exception = null;
 when(iterator.setPosition(anyInt())).thenThrow(new NoSuchElementException());

 // When
 attributeContext = new AttributeContext(null, null);
 attributeContext.iterator = iterator;

 // When & Then
 exception = assertThrows(AttributeException.class, () -> attributeContext.nextNode());
 Assert.assertEquals("No next node on the iterator", exception.getMessage());
 }

 @Test
 public void testNextNodeWithAttributeNameIteratorWithNextNode() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator iterator = mock(NodeIterator.class);
 NodePointer nodePointer = mock(NodePointer.class);
 when(iterator.setPosition(anyInt())).thenReturn(true);
 when(iterator.getNodePointer()).thenReturn(nodePointer);
 when(nodePointer.getNodeName()).thenReturn(new QName("test"));

 // When
 attributeContext = new AttributeContext(null, null);
 attributeContext.iterator = iterator;

 // When
 attributeContext.nextNode();
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();

 // Then
 Assert.assertSame(nodePointer, currentNodePointer);
 }

 @Test
 public void testGetCurrentNodePointerWithNoCurrentNode() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);

 // When
 NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();

 // Then
 Assert.assertNull(currentNodePointer);
 }

 @Test
 public void testGetCurrentNodePointerWithCurrentNode() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodePointer currentNodePointer = mock(NodePointer.class);
 when(attributeContext.getCurrentNodePointer()).thenReturn(currentNodePointer);

 // When
 NodePointer result = attributeContext.getCurrentNodePointer();

 // Then
 Assert.assertSame(currentNodePointer, result);
 }

 @Test
 public void testGetParentContext() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 EvalContext parentContext = mock(EvalContext.class);
 when(attributeContext.getParentContext()).thenReturn(parentContext);

 // When
 EvalContext result = attributeContext.getParentContext();

 // Then
 Assert.assertSame(parentContext, result);
 }

 @Test
 public void testSetPositionWithInvalidPositionWhenIteratorExists() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator iterator = mock(NodeIterator.class);
 AttributeException exception = null;
 when(iterator.setPosition(anyInt())).thenThrow(new IllegalStateException());

 // When
 attributeContext = new AttributeContext(null, null);
 attributeContext.iterator = iterator;
 int position = -1;

 // When & Then
 exception = assertThrows(AttributeException.class, () -> attributeContext.setPosition(position));
 Assert.assertEquals("Position cannot be set when an AttributeIterator exists", exception.getMessage());
 }

 @Test
 public void testNextNodeWithIteratorPositionOutOfBounds() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 NodeIterator iterator = mock(NodeIterator.class);
 when(iterator.hasNext()).thenReturn(false);
 when(iterator.setPosition(anyInt())).thenReturn(true);

 // When
 attributeContext = new AttributeContext(null, null);
 attributeContext.iterator = iterator;

 // When & Then
 AttributeException exception = assertThrows(AttributeException.class, () -> attributeContext.nextNode());
 Assert.assertEquals("No next node on the iterator", exception.getMessage());
 }

 @Test
 public void testNextNodeWithInvalidAxisName() {
 // Given
 AttributeContext attributeContext = new AttributeContext(null, null);
 AttributeException exception = null;

 // When
 exception = assertThrows(AttributeException.class, () -> attributeContext.nextNode(Axis.CHILD));

 // Then
 Assert.assertEquals("Invalid axis name", exception.getMessage());
 }
}