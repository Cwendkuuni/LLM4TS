import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);
    }

    @Test
    public void testConstructor() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testConstructorWithPrefix() {
        name = new QName("prefix", "test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testConstructorWithNullPrefix() {
        name = new QName(null, "test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testConstructorWithWildcard() {
        name = new QName("*");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertNull(attributeIterator.attributes);
        assertEquals(0, attributeIterator.position);
    }

    @Test
    public void testGetPosition() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.position = 1;
        assertEquals(1, attributeIterator.getPosition());

        attributeIterator.position = 5;
        assertEquals(5, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionWithinBounds() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.attributes = Collections.singletonList(mock(Attribute.class));

        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));

        attributeIterator.attributes = Arrays.asList(mock(Attribute.class), mock(Attribute.class));

        assertTrue(attributeIterator.setPosition(1));
        assertTrue(attributeIterator.setPosition(2));
    }

    @Test
    public void testSetPositionOutOfBounds() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.attributes = Collections.singletonList(mock(Attribute.class));

        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(2));

        attributeIterator.attributes = Arrays.asList(mock(Attribute.class), mock(Attribute.class));

        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(3));
    }

    @Test
    public void testSetPositionWithEmptyList() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        assertFalse(attributeIterator.setPosition(1));
    }

    @Test
    public void testSetPositionWithNullAttributes() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.attributes = null;

        assertFalse(attributeIterator.setPosition(1));
    }

    @Test
    public void testSetPositionWithNullAttributesAndPosition0() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.attributes = null;

        assertFalse(attributeIterator.setPosition(0));
    }

    @Test
    public void testSetPositionWithNullAttributesAndPositionNegative() {
        name = new QName("test");
        attributeIterator = new JDOMAttributeIterator(parent, name);

        attributeIterator.attributes = null;

        assertFalse(attributeIterator.setPosition(-1));
    }
}