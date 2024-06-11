import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Namespace;
import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class JDOMAttributeIteratorTest {

    private Element testElement;
    private NodePointer testParent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        String xmlNs = "xml";
        String testPrefix = "test";
        String testName = "testName";
        String testValue = "testValue";

        testElement = new Element("testElement");
        testElement.addNamespaceDeclaration(xmlNs, xmlNs);
        testElement.addNamespaceDeclaration(testPrefix, testPrefix);
        testElement.addAttribute(new Attribute(testPrefix + ":testName1", "value1"));
        testElement.addAttribute(new Attribute(testPrefix + ":testName2", "value2"));
        testElement.addAttribute(new Attribute(testName, "value3"));

        testParent = new NodePointer(testElement, null);
    }

    @Test
    public void testJDOMAttributeIteratorConstructionWithWildcardName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, null);
        List attributes = iterator.getAttributes();

        assertTrue(attributes.size() > 0);
        for (Object attribute : attributes) {
            Attribute attributeObj = (Attribute) attribute;
            assertTrue(attributeObj.getValue().getText().length() > 0);
        }
    }

    @Test
    public void testJDOMAttributeIteratorConstructionWithSpecifiedName() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(new QName(testPrefix, testName).getURI(), testName));
        List attributes = iterator.getAttributes();

        assertTrue(attributes.size() == 1);
        Attribute attributeObj = (Attribute) attributes.get(0);
        assertEquals(testValue, attributeObj.getValue().getText());
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointer() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));

        NodePointer nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer instanceof JDOMAttributePointer);
        assertEquals(testName, ((JDOMAttributePointer) nodePointer).getAttribute().getName());
        assertEquals(testValue, ((JDOMAttributePointer) nodePointer).getAttribute().getValue().getText());
    }

    @Test
    public void testJDOMAttributeIteratorGetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));
        iterator.setPosition(2);

        assertTrue(iterator.getPosition() == 2);
    }

    @Test
    public void testJDOMAttributeIteratorSetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));

        assertFalse(iterator.setPosition(-1));
        assertFalse(iterator.setPosition(4));

        iterator.setPosition(1);
        assertTrue(iterator.getPosition() == 1);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testJDOMAttributeIteratorAddAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));
        iterator.addAttribute(new Attribute(testPrefix + ":testName4", "value4"));
    }

    @Test
    public void testJDOMAttributeIteratorRemoveAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));
        iterator.remove();

        List attributes = iterator.getAttributes();
        assertTrue(attributes.size() == 2);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorHasNext() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));

        for (int i = 0; i < attributes.size(); i++) {
            iterator.next();
            assertTrue(iterator.hasNext());
        }

        assertFalse(iterator.hasNext());
    }

    @Test
    public void testJDOMAttributeIteratorHasPrevious() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(testParent, new QName(testPrefix, testName));

        for (int i = 0; i < attributes.size() - 1; i++) {
            iterator.next();
        }

        iterator.previous();
        assertTrue(iterator.hasPrevious());

        iterator.previous();
        assertFalse(iterator.hasPrevious());
    }
}