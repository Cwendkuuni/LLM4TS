import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorSingleNodeAttribute() {
        // Test for a single node attribute
        NodePointer parent = new JDOMAttributePointer();
        QName name = new QName("testAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.getNodePointer() != null);
        assertEquals(iterator.getPosition(), 1);
        assertTrue(iterator.setPosition(1));
    }

    @Test
    public void testJDOMAttributeIteratorMultipleNodeAttributes() {
        // Test for multiple node attributes
        NodePointer parent = new JDOMAttributePointer();
        QName name = new QName("*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertTrue(iterator.getNodePointer() != null);
        assertEquals(iterator.getPosition(), 1);
        assertTrue(iterator.setPosition(2));
    }

    @Test
    public void testJDOMAttributeIteratorNoAttributes() {
        // Test for no attributes
        NodePointer parent = new JDOMAttributePointer();
        QName name = new QName("nonExistentAttribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
        assertEquals(iterator.getPosition(), 0);
        assertFalse(iterator.setPosition(1));
    }
}