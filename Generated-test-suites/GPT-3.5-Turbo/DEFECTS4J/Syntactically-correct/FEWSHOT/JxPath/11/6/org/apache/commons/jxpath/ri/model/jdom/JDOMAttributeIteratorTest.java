import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;
    private Element element;
    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        name = mock(QName.class);
        element = mock(Element.class);
        iterator = new JDOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        // Test when parent.getNode() is null
        when(parent.getNode()).thenReturn(null);
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.attributes);

        // Test when parent.getNode() is not instance of Element
        when(parent.getNode()).thenReturn(new Object());
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.attributes);
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(iterator.attributes.size() + 1));

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        // Test when attributes is null
        iterator.attributes = null;
        assertNull(iterator.getNodePointer());

        // Test when position is 0
        iterator.position = 0;
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
        assertEquals(JDOMAttributePointer.class, iterator.getNodePointer().getClass());

        // Test when position is not 0
        iterator.position = 1;
        assertEquals(1, iterator.getPosition());
        assertEquals(JDOMAttributePointer.class, iterator.getNodePointer().getClass());
    }
}