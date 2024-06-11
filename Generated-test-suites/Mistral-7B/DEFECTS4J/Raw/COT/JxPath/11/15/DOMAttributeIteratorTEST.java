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

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        String prefix = "xmlns";
        String localName = "xmlns";
        String testPrefix = "xmlns";
        String testLocalName = "xmlns";
        String testNS = "http://www.w3.org/2000/xmlns/";
        String nodePrefix = null;
        String nodeLocalName = "test";

        boolean result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(testPrefix, testLocalName, testNS));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName("", nodeLocalName, null));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(prefix, localName, null));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(null, localName, null));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(prefix, testLocalName, null));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(null, testLocalName, null));
        Assert.assertFalse(result);

        nodePrefix = "test";
        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(nodePrefix, localName, null));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(prefix, localName, testNS));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(null, testLocalName, testNS));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(prefix, testLocalName, null));
        Assert.assertTrue(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test", null), new QName(null, testLocalName, testNS));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test1", null), new QName(prefix, testLocalName, null));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test1", null), new QName(null, testLocalName, null));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test1", null), new QName(prefix, "test2", null));
        Assert.assertFalse(result);

        result = DOMAttributeIterator.testAttr(new Attr("", "test1", null), new QName(null, "test2", null));
        Assert.assertFalse(result);
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("test");
        QName name = new QName("xmlns:test", "test", "http://www.w3.org/2000/xmlns/");
        Attr attr = new Attr("test", "test", null);
        element.setAttributeNode(attr);

        NodePointer parent = new NodePointer(null, element, null, null, 0);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr result = iterator.getAttribute(element, name);

        Assert.assertSame(attr, result);

        name = new QName("", "test", null);
        result = iterator.getAttribute(element, name);
        Assert.assertNull(result);
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(1);
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertFalse(iterator.setPosition(-1));
        Assert.assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testgetNodePointer() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(1);

        NodePointer result = iterator.getNodePointer();
        Assert.assertNotNull(result);
        Assert.assertEquals(iterator.getPosition(), ((DOMAttributePointer) result).getPosition());

        result = iterator.getNodePointer();
        Assert.assertNotNull(result);
        Assert.assertEquals(iterator.getPosition(), ((DOMAttributePointer) result).getPosition());

        iterator.setPosition(0);
        result = iterator.getNodePointer();
        Assert.assertNotNull(result);
        Assert.assertEquals(iterator.getPosition(), ((DOMAttributePointer) result).getPosition());
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Assert.assertEquals(0, iterator.getPosition());

        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());

        iterator.setPosition(2);
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPositionNegative() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        iterator.setPosition(-1);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPositionGreaterThanSize() {
        NodePointer parent = new NodePointer(null, null, null, null, 0);
        QName name = new QName("", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        iterator.setPosition(3);
    }
}