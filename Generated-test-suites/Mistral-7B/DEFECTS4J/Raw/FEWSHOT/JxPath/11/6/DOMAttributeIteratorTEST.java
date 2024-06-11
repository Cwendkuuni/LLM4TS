package org.apache.commons.cli;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    private Element testElement;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        String xml = "<root xmlns:x='http://test' xmlns:y='http://another-test'><a attr1='value1' attr2='value2' /></root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document doc = null;
        try {
            doc = factory.newDocumentBuilder().parse(new InputSource(new StringReader(xml)));
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        testElement = (Element) doc.getElementsByTagName("root").item(0);
    }

    @Test
    public void testIteratorEmpty() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(null), null);
        assertNull(iterator.getNodePointer());
        assertNull(iterator.nextNode());
    }

    @Test
    public void testIteratorSingleAttribute() {
        DOMAttributeIterator iterator = new DOMAttribeIterator(new DOMPoint(testElement), new QName("", "attr1"));
        assertNotNull(iterator.getNodePointer());
        assertNotNull(iterator.nextNode());
    }

    @Test
    public void testIteratorMultipleAttributes() {
        DOMAttributeIterator iterator = new DOMAttribeIterator(new DOMPoint(testElement), new QName("", "*"));
        int position = 0;
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.nextNode();
            assertEquals("value1", nodePointer.getNode().getNodeValue());
            position++;
        }
        assertEquals(2, position);

        position = 0;
        iterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("x", "*"));
        while (iterator.hasNext()) {
            NodePointer nodePointer = iterator.nextNode();
            assertEquals("xmlns:x", nodePointer.getNode().getNodeName());
            position++;
        }
        assertEquals(1, position);
    }

    @Test
    public void testIteratorNamespaces() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("", "xmlns"));
        assertNull(iterator.nextNode());

        iterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("y", "xmlns"));
        assertNull(iterator.nextNode());

        iterator = new DOMAttributeIterator(new DOMPoint(testElement), new QName("", "xmlns:x"));
        assertNotNull(iterator.nextNode());
        assertEquals("http://test", iterator.getNodePointer().getNode().getNodeValue());
    }
}