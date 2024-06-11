import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private static NodePointer parent;
    private static Attribute attribute;
    private static JDOMAttributeIterator attributeIterator;

    @BeforeClass
    public static void setupBeforeClass() {
        NodePointer nodePointer = new NodePointer();
        attribute = new Attribute("test", "value");
        JDOMAttributePointer attributePointer = new JDOMAttributePointer(nodePointer, attribute);
        attributeIterator = new JDOMAttributeIterator(nodePointer, QName.valueOf("test"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer pointer = attributeIterator.getNodePointer();
        assertNotNull(pointer);
        assertTrue(pointer instanceof JDOMAttributePointer);
        assertEquals(attribute, ((JDOMAttributePointer) pointer).getAttribute());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
        attributeIterator.setPosition(1);
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionValid() {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());
        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        assertFalse(attributeIterator.setPosition(0));
        assertEquals(0, attributeIterator.getPosition());
        assertFalse(attributeIterator.setPosition(3));
        assertEquals(0, attributeIterator.getPosition());
    }
}