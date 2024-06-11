import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private Document document;
    private Element rootElement;
    private JXPathContext context;

    @Before
    public void setup() {
        document = new Document();
        rootElement = new Element("root");
        document.setRootElement(rootElement);
        context = JXPathContext.newContext(rootElement);
    }

    @Test
    public void testGetNodePointer_ElementWithOneMatchingAttribute_ReturnsCorrectNodePointer() {
        Attribute attribute = new Attribute("attribute", "value");
        rootElement.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));
        NodePointer nodePointer = iterator.getNodePointer();

        assertEquals(attribute, ((JDOMAttributePointer)nodePointer).getAttribute());
    }

    @Test
    public void testGetNodePointer_ElementWithMultipleMatchingAttributes_ReturnsCorrectNodePointerForEachPosition() {
        Attribute attribute1 = new Attribute("attribute", "value1");
        Attribute attribute2 = new Attribute("attribute", "value2");
        rootElement.setAttribute(attribute1);
        rootElement.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertTrue(iterator.setPosition(1));
        NodePointer nodePointer1 = iterator.getNodePointer();
        assertEquals(attribute1, ((JDOMAttributePointer)nodePointer1).getAttribute());

        assertTrue(iterator.setPosition(2));
        NodePointer nodePointer2 = iterator.getNodePointer();
        assertEquals(attribute2, ((JDOMAttributePointer)nodePointer2).getAttribute());
    }

    @Test
    public void testGetNodePointer_ElementWithNoMatchingAttribute_ReturnsNullNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition_SetPositionWithinValidRange_ReturnsTrue() {
        Attribute attribute1 = new Attribute("attribute", "value1");
        Attribute attribute2 = new Attribute("attribute", "value2");
        rootElement.setAttribute(attribute1);
        rootElement.setAttribute(attribute2);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
    }

    @Test
    public void testGetPosition_SetPositionOutsideValidRange_ReturnsFalse() {
        Attribute attribute = new Attribute("attribute", "value");
        rootElement.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertFalse(iterator.setPosition(0));
        assertFalse(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testGetPosition_SetPositionWithoutMatchedAttribute_ReturnsFalse() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testGetPosition_NoAttributes_ReturnsFalse() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testGetPosition_NullAttributes_ReturnsFalse() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        iterator.setPosition(1); // Ensure position > 0

        iterator.attributes = null;

        assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testGetPosition_SetPositionAtBoundary_ReturnsTrue() {
        Attribute attribute = new Attribute("attribute", "value");
        rootElement.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(1)); // Same position, should return true
    }

    @Test
    public void testGetPosition_SetPositionBeforeBoundary_ReturnsTrue() {
        Attribute attribute = new Attribute("attribute", "value");
        rootElement.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(0)); // Before boundary, should return true
    }

    @Test
    public void testGetPosition_SetPositionAfterBoundary_ReturnsFalse() {
        Attribute attribute = new Attribute("attribute", "value");
        rootElement.setAttribute(attribute);
        
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("attribute"));

        assertTrue(iterator.setPosition(1));
        assertFalse(iterator.setPosition(2)); // After boundary, should return false
    }

    @Test
    public void testGetPosition_AttributeWithNamespace_ReturnsTrue() {
        Namespace ns = Namespace.getNamespace("ns", "http://namespace.com");
        Attribute attribute = new Attribute("ns:attribute", "value", ns);
        rootElement.setAttribute(attribute);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(context.getPointer(rootElement), new QName("ns:attribute"));

        assertTrue(iterator.setPosition(1));
    }
}