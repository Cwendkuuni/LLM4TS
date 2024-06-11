import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setUp() {
        NodePointer parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        when(parent.getNode()).thenReturn(element);
        domAttributeIterator = new DOMAttributeIterator(parent, new QName("attribute"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals(DOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = domAttributeIterator.setPosition(1);
        assertEquals(true, result);
    }
}