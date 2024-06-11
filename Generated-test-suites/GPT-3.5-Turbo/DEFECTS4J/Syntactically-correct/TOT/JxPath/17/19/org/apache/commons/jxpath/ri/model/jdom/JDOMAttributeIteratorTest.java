import static org.junit.Assert.assertEquals;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Element element;
    private NodePointer nodePointer;

    @Before
    public void setup() {
        element = new Element("root");
        element.addNamespaceDeclaration(Namespace.XML_NAMESPACE);
        element.addNamespaceDeclaration(Namespace.getNamespace("test", "http://example.com"));

        element.setAttribute(new Attribute("attr1", "value1"));
        element.setAttribute(new Attribute("attr2", "", Namespace.XML_NAMESPACE));
        element.setAttribute(new Attribute("attr3", "1", Namespace.getNamespace("test", "http://example.com")));

        JXPathContext context = JXPathContext.newContext(node);
        nodePointer = context.getPointer("/");
    }

    @Test
    public void testGetNodePointer_PositionWithinBounds_ReturnsCorrectPointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        iterator.setPosition(1);
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr1", pointer.getName());
        assertEquals("value1", pointer.getValue().getText());
        
        iterator.setPosition(3);
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals("attr3", pointer.getName());
        assertEquals("1", pointer.getValue().getText());
    }

    @Test
    public void testGetNodePointer_PositionOutOfBounds_ReturnsNull() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        iterator.setPosition(0);
        JDOMAttributePointer pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals(null, pointer);
        
        iterator.setPosition(4);
        pointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertEquals(null, pointer);
    }

    @Test
    public void testGetPosition_ValidPosition_ReturnsCorrectPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        iterator.setPosition(1);
        int position = iterator.getPosition();
        assertEquals(1, position);
        
        iterator.setPosition(3);
        position = iterator.getPosition();
        assertEquals(3, position);
    }

    @Test
    public void testGetPosition_InvalidPosition_ReturnsZero() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        iterator.setPosition(0);
        int position = iterator.getPosition();
        assertEquals(0, position);
        
        iterator.setPosition(4);
        position = iterator.getPosition();
        assertEquals(0, position);
    }

    @Test
    public void testSetPosition_ValidPosition_ReturnsTrue() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        boolean result = iterator.setPosition(1);
        assertEquals(true, result);
        
        result = iterator.setPosition(3);
        assertEquals(true, result);
    }

    @Test
    public void testSetPosition_InvalidPosition_ReturnsFalse() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*"));
        
        boolean result = iterator.setPosition(0);
        assertEquals(false, result);
        
        result = iterator.setPosition(4);
        assertEquals(false, result);
    }

}