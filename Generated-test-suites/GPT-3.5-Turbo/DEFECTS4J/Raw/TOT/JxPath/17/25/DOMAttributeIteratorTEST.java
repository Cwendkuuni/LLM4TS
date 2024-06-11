import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class DOMAttributeIteratorTest {
    @Mock
    private NodePointer nodePointer;

    @Mock
    private Node node;

    @Mock
    private Element element;

    @Mock
    private Attr attr1;

    @Mock
    private Attr attr2;

    @Mock
    private NamedNodeMap attributes;

    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);

        // Create test attributes
        when(attr1.getLocalName()).thenReturn("attr1");
        when(attr2.getLocalName()).thenReturn("attr2");

        // Mock parent node pointer
        when(nodePointer.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(node.getAttributes()).thenReturn(attributes);
        when(attributes.getLength()).thenReturn(2);
        when(attributes.item(0)).thenReturn(attr1);
        when(attributes.item(1)).thenReturn(attr2);

        // Create iterator
        iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        // Verify that the returned node pointer is correct
        assertNotNull(nodePointer);
        assertEquals(DOMAttributePointer.class, nodePointer.getClass());

        // Verify that the node pointer contains the correct attribute
        DOMAttributePointer attributePointer = (DOMAttributePointer) nodePointer;
        assertEquals(attr1, attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();

        // Verify that the initial position is 0
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean setPositionResult = iterator.setPosition(1);

        // Verify that the position is set successfully
        assertTrue(setPositionResult);

        // Verify that the position is set correctly
        int position = iterator.getPosition();
        assertEquals(1, position);
    }

    @Test
    public void testSetPosition_invalid() {
        boolean setPositionResult = iterator.setPosition(3);

        // Verify that the position cannot be set beyond the attributes size
        assertFalse(setPositionResult);
    }

    @Test
    public void testSetPosition_zero() {
        boolean setPositionResult = iterator.setPosition(0);

        // Verify that the position cannot be set to zero
        assertFalse(setPositionResult);
    }

    @Test
    public void testSetPosition_negative() {
        boolean setPositionResult = iterator.setPosition(-1);

        // Verify that the position cannot be set to a negative value
        assertFalse(setPositionResult);
    }

    @Test
    public void testAttributeFiltering() {
        // Create iterator with specific attribute name
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(nodePointer, new QName("attr1"));

        // Verify that the iterator only contains the specified attribute
        assertEquals(1, attributeIterator.attributes.size());
        assertEquals(attr1, attributeIterator.attributes.get(0));
    }

    @Test
    public void testNamespaceAttributeFiltering() {
        // Mock parent namespace resolver
        when(nodePointer.getNamespaceResolver()).thenReturn(mock(NamespaceResolver.class));

        // Create iterator with specific namespace attribute
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1", "uri"));

        // Verify that the iterator only contains the specified attribute
        assertEquals(1, attributeIterator.attributes.size());
        assertEquals(attr1, attributeIterator.attributes.get(0));
    }

    @Test
    public void testNullNamespaceAttributeFiltering() {
        // Mock parent namespace resolver
        when(nodePointer.getNamespaceResolver()).thenReturn(null);

        // Create iterator with specific namespace attribute
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1", "uri"));

        // Verify that the iterator only contains the specified attribute
        assertEquals(1, attributeIterator.attributes.size());
        assertEquals(attr1, attributeIterator.attributes.get(0));
    }

    @Test
    public void testInvalidNamespaceAttributeFiltering() {
        // Mock parent namespace resolver
        NamespaceResolver namespaceResolver = mock(NamespaceResolver.class);
        when(nodePointer.getNamespaceResolver()).thenReturn(namespaceResolver);
        when(namespaceResolver.getNamespaceURI("prefix")).thenReturn("invalidUri");

        // Create iterator with specific namespace attribute
        DOMAttributeIterator attributeIterator = new DOMAttributeIterator(nodePointer, new QName("prefix:attr1", "uri"));

        // Verify that the iterator does not contain any attributes
        assertEquals(0, attributeIterator.attributes.size());
    }
}