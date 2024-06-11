import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute = new Attr("xmlns:xml", "xml");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attribute);
        iterator.attributes = attributes;
        NodePointer expected = new DOMAttributePointer(parent, attribute);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        List<Attr> attributes = new ArrayList<>();
        iterator.attributes = attributes;
        iterator.setPosition(1);
        int expected = 1;
        int actual = iterator.getPosition();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        List<Attr> attributes = new ArrayList<>();
        iterator.attributes = attributes;
        boolean expected = true;
        boolean actual = iterator.setPosition(1);
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSetPositionInvalidPosition() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        List<Attr> attributes = new ArrayList<>();
        iterator.attributes = attributes;
        iterator.setPosition(0);
        iterator.setPosition(5);
    }

    @Test
    public void testNext() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute1 = new Attr("xmlns:xml", "xml");
        Attr attribute2 = new Attr("xml:lang", "en");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        iterator.attributes = attributes;

        NodePointer expected1 = new DOMAttributePointer(parent, attribute1);
        NodePointer actual1 = iterator.getNodePointer();
        Assert.assertEquals(expected1, actual1);

        iterator.next();

        NodePointer expected2 = new DOMAttributePointer(parent, attribute2);
        NodePointer actual2 = iterator.getNodePointer();
        Assert.assertEquals(expected2, actual2);
    }

    @Test(expected = NoSuchElementException.class)
    public void testNextEmptyIterator() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.next();
        iterator.next();
    }

    @Test
    public void testHasNext() {
        NodePointer parent = new NodePointer(null, null, null, 0);
        QName name = new QName("namespace:localName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attribute1 = new Attr("xmlns:xml", "xml");
        Attr attribute2 = new Attr("xml:lang", "en");
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        iterator.attributes = attributes;

        boolean expected1 = true;
        boolean actual1 = iterator.hasNext();
        Assert.assertEquals(expected1, actual1);

        iterator.next();

        boolean expected2 = false;
        boolean actual2 = iterator.hasNext();
        Assert.assertEquals(expected2, actual2);
    }
}