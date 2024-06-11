package org.apache.commons.cli;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.util.List;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {
    private NodePointer parent;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<root xmlns:xsi=\"http://www.w3.org/2001/XMLSchema-instance\" xmlns:my=\"my-namespace\" xmlns:my2=\"my2-namespace\">\n" +
                "<element my:attr1=\"value1\" my2:attr2=\"value2\" xmlns:attr=\"attr-namespace\" xsi:id=\"id1\">\n" +
                "<sub-element attr:attr3=\"value3\"/>\n" +
                "</element>\n" +
                "</root>";

        Document doc = javax.xml.parsers.DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new org.apache.commons.io.IOUtils.ByteArrayInputStream(xml.getBytes()));
        Element root = (Element) doc.getDocumentElement();
        this.parent = new DOMPointers.DOMPointerImpl(doc, root);
    }

    @Test
    public void testEmptyIterator() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(null, null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testEmptyNode() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMPointers.DOMPointerImpl(null, null), null);
        assertNull(iterator.getNodePointer());
        assertEquals(0, iterator.getPosition());
        assertFalse(iterator.hasNext());
    }

    @Test
    public void testSingleAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(this.parent, new QName("my", "attr1"));
        assertEquals(1, iterator.getPosition());
        NodePointer np = iterator.next();
        assertSame(this.parent, np.getParent());
        Attr attr = (Attr) np.getNode();
        assertEquals("value1", attr.getNodeValue());
        assertEquals("value1", iterator.getCurrentValue());
        assertNull(iterator.next());
    }

    @Test
    public void testMultipleAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(this.parent, new QName("*"));
        List list = new ArrayList();
        list.add(new QName("my", "attr1"));
        list.add(new QName("my2", "attr2"));
        list.add(new QName("attr", "attr3"));

        for (int i = 0; i < list.size(); i++) {
            NodePointer np = iterator.next();
            Attr attr = (Attr) np.getNode();
            QName name = new QName(attr.getNamespaceURI(), attr.getNodeName());
            assertEquals(list.get(i), name);
            assertEquals(attr.getNodeValue(), iterator.getCurrentValue());
        }
        assertNull(iterator.next());
    }

    @Test
    public void testFilteringByName() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(this.parent, new QName("my", "attr1"));
        NodePointer np = iterator.next();
        assertSame(this.parent, np.getParent());
        Attr attr = (Attr) np.getNode();
        assertEquals("value1", attr.getNodeValue());
        assertEquals("value1", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("my2", "attr2"));
        np = iterator.next();
        assertSame(this.parent, np.getParent());
        attr = (Attr) np.getNode();
        assertEquals("value2", attr.getNodeValue());
        assertEquals("value2", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("attr", "attr3"));
        np = iterator.next();
        assertSame(this.parent, np.getParent());
        attr = (Attr) np.getNode();
        assertEquals("value3", attr.getNodeValue());
        assertEquals("value3", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("my", "nonExistent"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }

    @Test
    public void testFilteringByNamespace() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(this.parent, new QName(null, null, "xmlns"));
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());

        iterator = new DOMAttributeIterator(this.parent, new QName("xmlns", "xmlns:xsi"));
        Attr attr = (Attr) iterator.next().getNode();
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", attr.getNamespaceURI());
        assertEquals("xmlns:xsi", attr.getNodeName());
        assertEquals("http://www.w3.org/2001/XMLSchema-instance", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("xmlns", "xmlns:my"));
        attr = (Attr) iterator.next().getNode();
        assertEquals("my-namespace", attr.getNamespaceURI());
        assertEquals("xmlns:my", attr.getNodeName());
        assertEquals("my-namespace", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("xmlns", "xmlns:my2"));
        attr = (Attr) iterator.next().getNode();
        assertEquals("my2-namespace", attr.getNamespaceURI());
        assertEquals("xmlns:my2", attr.getNodeName());
        assertEquals("my2-namespace", iterator.getCurrentValue());

        iterator = new DOMAttributeIterator(this.parent, new QName("attr", "attr3"));
        attr = (Attr) iterator.next().getNode();
        assertNull(attr.getNamespaceURI());
        assertEquals("attr-namespace", attr.getLocalName());
        assertEquals("attr-namespace:attr3", iterator.getCurrentValue());
    }
}