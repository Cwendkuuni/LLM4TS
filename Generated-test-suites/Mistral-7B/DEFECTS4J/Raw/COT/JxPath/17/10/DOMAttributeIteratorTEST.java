import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Document;
import org.w3c.dom.DOMParser;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private NodeIterator iterator;
    private Document doc;

    @Before
    public void setUp() throws Exception {
        String xml = "<root xmlns:x='http://test.com'>< elem1 attr1='val1' attr2='val2'/>< elem2 xmlns:y='http://test.com' attr3='val3'/>< elem3 attr1='val4'/></root>";
        DOMParser parser = new DOMParser();
        parser.parseFromString(xml, null);
        doc = parser.getDocument();
        iterator = new DOMAttributeIterator(new NodePointer(doc.getDocumentElement()), new QName("*"));
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition_validPosition() {
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPosition_invalidPosition() {
        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testGetNodePointer() {
        Attr attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr1", attr.getName().getNodeValue());
        Assert.assertEquals("val1", attr.getValue());
        Assert.assertEquals("/root/elem1", iterator.getNodePointer().getPathString());

        iterator.next();
        attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr2", attr.getName().getNodeValue());
        Assert.assertEquals("val2", attr.getValue());
        Assert.assertEquals("/root/elem1", iterator.getNodePointer().getPathString());

        for (int i = 0; i < 5; i++) {
            iterator.next();
        }
        attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("attr1", attr.getName().getNodeValue());
        Assert.assertEquals("val4", attr.getValue());
        Assert.assertEquals("/root/elem3", iterator.getNodePointer().getPathString());
    }

    @Test
    public void testNext() {
        for (int i = 0; i < 5; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testHasNext() {
        Assert.assertTrue(iterator.hasNext());
        for (int i = 0; i < 5; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithName() {
        iterator = new DOMAttributeIterator(new NodePointer(doc.getDocumentElement()), new QName("attr1"));
        Assert.assertTrue(iterator.hasNext());
        Attr attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("val1", attr.getValue());

        iterator.next();
        attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("val2", attr.getValue());

        for (int i = 0; i < 3; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWithNamespace() {
        iterator = new DOMAttributeIterator(new NodePointer(doc.getDocumentElement()), new QName("x:attr1"));
        Assert.assertTrue(iterator.hasNext());
        Attr attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("val1", attr.getValue());

        iterator.next();
        attr = (Attr) iterator.getNodePointer().getNode();
        Assert.assertEquals("val3", attr.getValue());

        for (int i = 0; i < 2; i++) {
            iterator.next();
        }
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = NoSuchElementException.class)
    public void testIteratorAfterEnd() {
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
        iterator.next();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIteratorWithInvalidName() {
        iterator = new DOMAttributeIterator(new NodePointer(doc.getDocumentElement()), new QName("invalidName"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalArgumentArgument.class)
    public void testIteratorWithNullName() {
        iterator = new DOMAttributeIterator(new NodePointer(doc.getDocumentElement()), null);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWhenParentIsNull() {
        NodePointer parent = null;
        iterator = new DOMAttributeIterator(parent, new QName("*"));
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testIteratorWhenParentIsEmpty() {
        NodePointer parent = new NodePointer(new Document());
        iterator = new DOMAttributeIterator(parent, new QName("*"));
        Assert.assertFalse(iterator.hasNext());
    }
}