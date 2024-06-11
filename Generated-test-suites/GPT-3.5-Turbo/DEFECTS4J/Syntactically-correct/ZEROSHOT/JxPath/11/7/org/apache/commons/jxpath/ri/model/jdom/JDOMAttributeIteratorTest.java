import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {
    
    private Element parent;
    private QName name;
    private JDOMAttributeIterator iterator;
    
    @Before
    public void setUp() {
        parent = new Element("parent");
        name = new QName(null, "attribute");
        iterator = new JDOMAttributeIterator(new JDOMNodePointer(parent), name);
    }
    
    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertTrue(nodePointer instanceof JDOMAttributePointer);
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(2));
        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }
}