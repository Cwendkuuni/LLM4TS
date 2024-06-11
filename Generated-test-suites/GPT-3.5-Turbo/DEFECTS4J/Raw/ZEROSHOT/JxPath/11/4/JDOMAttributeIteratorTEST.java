import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    
    private JDOMAttributeIterator jdomAttributeIterator;
    private NodePointer nodePointer;
    
    @Before
    public void setUp() {
        Element element = new Element("testElement", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        element.setAttribute("attr4", "value4");
        nodePointer = new JDOMAttributePointer(null, null);
        jdomAttributeIterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
    }
    
    @Test
    public void testGetNodePointer() {
        jdomAttributeIterator.setPosition(1);
        NodePointer result = jdomAttributeIterator.getNodePointer();
        assertNotNull(result);
        assertTrue(result instanceof JDOMAttributePointer);
        assertEquals("attr1", ((JDOMAttributePointer)result).getAttribute().getName());
        assertEquals("value1", ((JDOMAttributePointer)result).getAttribute().getValue());
    }
    
    @Test
    public void testGetPosition() {
        assertEquals(0, jdomAttributeIterator.getPosition());
        jdomAttributeIterator.setPosition(3);
        assertEquals(3, jdomAttributeIterator.getPosition());
    }
    
    @Test
    public void testSetPosition() {
        assertFalse(jdomAttributeIterator.setPosition(0));
        assertTrue(jdomAttributeIterator.setPosition(2));
        assertEquals(2, jdomAttributeIterator.getPosition());
        assertFalse(jdomAttributeIterator.setPosition(5));
    }
}