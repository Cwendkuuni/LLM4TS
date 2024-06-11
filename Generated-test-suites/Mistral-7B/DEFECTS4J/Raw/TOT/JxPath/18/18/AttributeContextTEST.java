import org.apache.commons.jxpath.ri.TestUtil;
import org.apache.commons.jxpath.ri.model.Node;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {

    @InjectMocks
    private AttributeContext attributeContext;

    @SuppressWarnings("unchecked")
    private static Node testNode = Mockito.mock(Node.class);

    @SuppressWarnings("unchecked")
    private static NodeIterator attributeIterator = Mockito.mock(NodeIterator.class);

    @SuppressWarnings("unchecked")
    private static NodePointer testNodePointer = Mockito.mock(NodePointer.class);

    private static final String TEST_ATTRIBUTE_NAME = "testAttribute";

    @Test
    public void testNextNode_FindAttribute_WhenCalled_ReturnsNextAttribute() {
        // Given
        Mockito.when(testNode.getFirstAttribute(TEST_ATTRIBUTE_NAME)).thenReturn(testNodePointer);
        Mockito.when(testNodePointer.getNode()).thenReturn(testNode);
        Mockito.when(testNode.getAttributeValue(TEST_ATTRIBUTE_NAME)).thenReturn("testValue");
        Mockito.when(testNodeIterator.getNodePointer()).thenReturn(testNodePointer);
        Mockito.when(testNodeIterator.getNodeIterator(Mockito.any(NodeTest.class))).thenReturn(attributeIterator);
        Mockito.when(attributeIterator.hasNext()).thenReturn(true);
        Mockito.when(attributeIterator.nextNode()).thenReturn(testNode);

        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeTest.class);

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentPosition(0);

        // When
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        String attributeValue = (String) TestUtil.evaluate(attributeContext, "string(./@testAttribute)");

        // Then
        assertNotNull(currentNodePointer);
        assertEquals("testValue", attributeValue);
    }

    @Test
    public void testNextNode_NoAttributeFound_WhenCalled_ReturnsNull() {
        // Given
        Mockito.when(testNode.getFirstAttribute(TEST_ATTRIBUTE_NAME)).thenReturn(null);

        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeTest.class);

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentPosition(0);

        // When
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();

        // Then
        assertNull(currentNodePointer);
    }

    @Test
    public void testNextNode_ParentIteratorEmpty_WhenCalled_ReturnsFalse() {
        // Given
        Mockito.when(testNode.getFirstAttribute(TEST_ATTRIBUTE_NAME)).thenReturn(testNodePointer);
        Mockito.when(testNodePointer.getNode()).thenReturn(testNode);
        Mockito.when(testNodeIterator.getNodePointer()).thenReturn(testNodePointer);
        Mockito.when(attributeIterator.hasNext()).thenReturn(false);

        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeTest.class);

        attributeContext = new AttributeContext(parentContext, nodeTest);
        attributeContext.setCurrentPosition(0);

        // When
        boolean nextNode = attributeContext.nextNode();

        // Then
        assertFalse(nextNode);
    }

    @Test
    public void testSetPosition_PositionBeforeStart_WhenCalled_ThrowsException() {
        // Given
        EvalContext parentContext = Mockito.mock(EvalContext.class);
        NodeTest nodeTest = Mockito.mock(NodeTest.class);

        attributeContext = new AttributeContext(parentContext, nodeTest);

        // When, Then
        try {
            attributeContext.setPosition(-1);
            assertFalse(true);
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetPosition_PositionAfterEnd_WhenCalled_ThrowsException() {
        // Given
        List<Node> nodes = TestUtil.createNodes();
        Node lastNode = nodes.get(nodes.size() - 1);

        EvalContext parentContext = Mockito.mock(EvalContext.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodes.get(nodes.size() - 2));
        NodeTest nodeTest = Mockito.mock(NodeTest.class);

        attributeContext = new AttributeContext(parentContext, nodeTest);

        // When, Then
        try {
            attributeContext.setPosition(nodes.size());
            assertFalse(true);
        } catch (IndexOutOfBoundsException e) {
            // expected
        }
    }
}