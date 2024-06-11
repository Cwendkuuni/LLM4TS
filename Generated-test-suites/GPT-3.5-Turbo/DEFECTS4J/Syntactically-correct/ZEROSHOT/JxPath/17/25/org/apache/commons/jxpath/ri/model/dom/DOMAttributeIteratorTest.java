import org.junit.Before;
import org.junit.Test;

import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.apache.commons.jxpath.ri.QName;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

public class DOMAttributeIteratorTest {
  
    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setup() {
        NodePointer parent = mock(NodePointer.class);
        QName name = new QName("testName");
        domAttributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = domAttributeIterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = domAttributeIterator.setPosition(1);
        assertEquals(false, result);
    }
}