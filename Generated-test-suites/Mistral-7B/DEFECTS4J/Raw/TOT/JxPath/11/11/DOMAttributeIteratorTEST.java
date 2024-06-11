package org.apache.commons.jxpath.ri.model.dom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.ProcessingInstruction;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

/**
 * Tests for {@link DOMAttributeIterator} class.
 */
public class DOMAttributeIteratorTest {

    private static final String XML_NS = "http://www.w3.org/2000/xmlns/";
    private static final String XPATH_NS = "http://www.w3.org/2005/xpath";
    private static final String PREFIX_A = "a";
    private static final String PREFIX_B = "b";
    private static final String LOCAL_NAME_XMLNS = "xmlns";
    private static final String LOCAL_NAME_XPATH = "xpath";
    private static final String LOCAL_NAME_C = "c";
    private static final String PREFIX_C = "c";
    private static final String ATTR_VALUE_A = "valueA";
    private static final String ATTR_VALUE_B = "valueB";
    private static final String ATTR_VALUE_C = "valueC";

    private Document document;
    private Element root;

    @Before
    public void setup() throws Exception {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        root = document.createElement("root");
        root.setAttributeNS(XML_NS, LOCAL_NAME_XMLNS, "http://example.com");
        root.setAttributeNS(XML_NS, LOCAL_NAME_XMLNS, "xpath", XPATH_NS);
        root.setAttributeNS(XPATH_NS, LOCAL_NAME_XPATH, "expression");
        root.setAttributeNode(createAttribute("a:a", ATTR_VALUE_A));
        root.setAttributeNode(createAttribute("b:b", ATTR_VALUE_B));
        root.setAttributeNode(createAttribute("c", ATTR_VALUE_C));
        document.appendChild(root);
    }

    /**
     * Test constructor.
     */
    @Test
    public void testConstructor() {
        NodePointer parent = new DOMPointNode(document, root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(PREFIX_A, LOCAL_NAME_A));
        assertEquals(DOMAttributeIterator.class, iterator.getClass());
    }

    /**
     * Test iteration through all attributes with given prefix and local name.
     */
    @Test
    public void testAttributeIteration() {
        NodePointer parent = new DOMPointNode(document, root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(PREFIX_A, LOCAL_NAME_A));
        NodePointer nodePointer;

        // Test first attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_A, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test second attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_B, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test last attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_C, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test next attribute after last attribute
        assertFalse(iterator.getNodePointer() != null);
    }

    /**
     * Test iteration through all attributes with given local name, regardless of prefix.
     */
    @Test
    public void testAttributeIterationWithLocalName() {
        NodePointer parent = new DOMPointNode(document, root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(LOCAL_NAME_C));
        NodePointer nodePointer;

        // Test first attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_C, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test second attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_A, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test last attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals(ATTR_VALUE_B, nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test next attribute after last attribute
        assertFalse(iterator.getNodePointer() != null);
    }

    /**
     * Test iteration through all attributes with given namespace URI and local name.
     */
    @Test
    public void testAttributeIterationWithNamespace() {
        NodePointer parent = new DOMPointNode(document, root);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(null, LOCAL_NAME_XPATH));
        NodePointer nodePointer;

        // Test first attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertEquals("expression", nodePointer.getNodeValue());
        assertTrue(iterator.setPosition(1));

        // Test next attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertNull(nodePointer.getNodeValue());

        // Test last attribute
        nodePointer = iterator.getNodePointer();
        assertTrue(nodePointer != null);
        assertNull(nodePointer.getNodeValue());

        // Test next attribute after last attribute
        assertFalse(iterator.getNodePointer() != null);
    }

    /**
     * Test iteration through empty node.
     */
    @Test
    public void testEmptyNode() {
        NodePointer parent = new DOMPointNode(document, document.createProcessingInstruction("xml", "version=\"1.0\""));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(LOCAL_NAME_A));
        assertFalse(iterator.getNodePointer() != null);
    }

    /**
     * Test iteration through node with no attributes.
     */
    @Test
    public void testNodeWithNoAttributes() {
        NodePointer parent = new DOMPointNode(document, document.createProcessingInstruction("xml", "version=\"1.0\""));
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, new QName(LOCAL_NAME_A));
        assertFalse(iterator.getNodePointer() != null);
    }

    private Attr createAttribute(String prefix, String localName) {
        Attr attr = document.createAttribute(localName);
        if (prefix != null) {
            attr.setNamespaceURI(XML_NS);
            attr.setPrefix(prefix);
        }
        attr.setValue("value");
        return attr;
    }
}