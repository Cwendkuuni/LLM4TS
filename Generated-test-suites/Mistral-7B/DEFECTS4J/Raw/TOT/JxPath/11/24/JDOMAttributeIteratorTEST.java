import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

public class JDOMAttributeIteratorTest {

    private NodePointer parentNode;
    private Element testElement;

    @BeforeEach
    public void init() {
        testElement = new Element("root");
        Namespace xmlNamespace = Namespace.getXmlNamespace();
        testElement.addNamespaceDeclaration(xmlNamespace.getPrefix(), xmlNamespace.getURI());
        testElement.addAttribute("xml:id", "testId", xmlNamespace);
        testElement.addAttribute("otherAttribute", "otherValue");
        parentNode = new NodePointer(testElement);
    }

    @Test
    public void testSingleAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("xml", "*"));
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer, "First attribute pointer should not be null.");
        assertEquals("testId", attributePointer.getName().getName(), "First attribute name should be 'testId'.");
        assertEquals("testId", attributePointer.getAttribute().getValue(), "First attribute value should be 'testId'.");
    }

    @Test
    public void testMultipleAttributes() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("*", "*"));
        List<JDOMAttributePointer> attributePointers = new ArrayList<>();

        while (iterator.hasNext()) {
            JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
            attributePointers.add(attributePointer);
            iterator.next();
        }

        assertEquals(2, attributePointers.size(), "There should be 2 attributes.");
        JDOMAttributePointer xmlIdPointer = attributePointers.get(0);
        JDOMAttributePointer otherAttributePointer = attributePointers.get(1);
        assertEquals("testId", xmlIdPointer.getAttribute().getValue(), "First attribute value should be 'testId'.");
        assertEquals("otherValue", otherAttributePointer.getAttribute().getValue(), "Second attribute value should be 'otherValue'.");
    }

    @Test
    public void testEmptyElement() {
        Element testEmptyElement = new Element("emptyElement");
        NodePointer emptyNodePointer = new NodePointer(testEmptyElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(emptyNodePointer, new QName("*", "*"));
        assertNull(iterator.getNodePointer(), "Iterator should return null when there are no attributes.");
    }

    @Test
    public void testNullParentNode() {
        NodePointer nullParentNode = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nullParentNode, new QName("*", "*"));
        assertNull(iterator.getNodePointer(), "Iterator should return null when parent node is null.");
    }

    @Test
    public void testNullName() {
        NodePointer parentNode = new NodePointer(testElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, null);
        assertNull(iterator.getNodePointer(), "Iterator should return null when name is null.");
    }

    @Test
    public void testWrongPrefix() {
        NodePointer parentNode = new NodePointer(testElement);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentNode, new QName("foo", "wrongPrefix"));
        assertNull(iterator.getNodePointer(), "Iterator should return null when prefix is wrong.");
    }
}