import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(null, null, null, null, null, 0);
        QName name = new QName("test", "test", null);
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        String prefix1 = "test";
        String prefix2 = null;
        String localName1 = "test";
        String localName2 = "xmlns";
        String ns1 = "testNS";
        String ns2 = null;

        boolean result1 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", "test", ns1));
        Assert.assertTrue(result1);

        boolean result2 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", "*", null));
        Assert.assertTrue(result2);

        boolean result3 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", localName1, null));
        Assert.assertTrue(result3);

        boolean result4 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", localName1, ns1));
        Assert.assertTrue(result4);

        boolean result5 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName(prefix1, localName1, ns1));
        Assert.assertTrue(result5);

        boolean result6 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName(prefix1, localName1, ns2));
        Assert.assertFalse(result6);

        boolean result7 = DOMAttributeIterator.testAttr(new Attr("test", "xmlns:test"), new QName("test", "xmlns:test", ns1));
        Assert.assertTrue(result7);

        boolean result8 = DOMAttributeIterator.testAttr(new Attr("xmlns:test", "test"), new QName("test", "xmlns:test", ns1));
        Assert.assertTrue(result8);

        boolean result9 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", localName2, ns1));
        Assert.assertFalse(result9);

        boolean result10 = DOMAttributeIterator.testAttr(new Attr("test", null), new QName("test", localName1, ns1));
        Assert.assertFalse(result10);

        boolean result11 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName(prefix2, localName1, ns1));
        Assert.assertFalse(result11);

        boolean result12 = DOMAttributeIterator.testAttr(new Attr("test", "test"), new QName("test", "test", null));
        Assert.assertFalse(result12);
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("test");
        Attr attr1 = new Attr("test1", "test1");
        Attr attr2 = new Attr("test2", "test2");
        Attr attr3 = new Attr("xmlns:test", "test3");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        element.setAttributeNode(attr3);

        QName name1 = new QName("test", "test1", null);
        QName name2 = new QName("test", "*", null);
        QName name3 = new QName("test", "test2", null);
        QName name4 = new QName("test", "test2", "testNS");
        QName name5 = new QName("test", "xmlns:test", null);
        QName name6 = new QName("test", "xmlns:test", "testNS");

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null, null, null, 0), null);

        Attr result1 = iterator.getAttribute(name1);
        Assert.assertNotNull(result1);
        Assert.assertEquals("test1", result1.getValue());

        Attr result2 = iterator.getAttribute(name2);
        Assert.assertNotNull(result2);
        Assert.assertEquals("test2", result2.getValue());

        Attr result3 = iterator.getAttribute(name3);
        Assert.assertNotNull(result3);
        Assert.assertEquals("test2", result3.getValue());

        Attr result4 = iterator.getAttribute(name4);
        Assert.assertNotNull(result4);
        Assert.assertEquals("test2", result4.getValue());

        Attr result5 = iterator.getAttribute(name5);
        Assert.assertNotNull(result5);
        Assert.assertEquals("test3", result5.getValue());

        Attr result6 = iterator.getAttribute(name6);
        Assert.assertNotNull(result6);
        Assert.assertEquals("test3", result6.getValue());

        Attr result7 = iterator.getAttribute(new QName("test", "test4", "testNS"));
        Assert.assertNull(result7);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetPositionOutOfBounds() {
        Element element = new Element("test");
        Attr attr1 = new Attr("test1", "test1");
        Attr attr2 = new Attr("test2", "test2");
        Attr attr3 = new Attr("xmlns:test", "test3");
        element.setAttributeNode(attr1);
        element.setAttributeNode(attr2);
        element.setAttributeNode(attr3);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null, null, null, 0), null);

        iterator.setPosition(-1);
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("test");
        Attr attr1 = new Attr("test1", "test1");
        Attr attr2 = new Attr("test2", "test2");
        Attr attr3 = new Attr("xmlns:test", "test3");
        element.setAttachmentNode(attr1);
        element.setAttachmentNode(attr2);
        element.setAttachmentNode(attr3);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null, null, null, 0), null);

        iterator.setPosition(1);
        Attr result1 = iterator.getNodePointer().getNode();
        Assert.assertEquals(attr2, result1);

        iterator.setPosition(3);
        Attr result2 = iterator.getNodePointer().getNode();
        Assert.assertEquals(attr3, result2);
    }

    @Test
    public void testNodePointer() {
        Element element = new Element("test");
        Attr attr1 = new Attr("test1", "test1");
        Attr attr2 = new Attr("test2", "test2");
        Attr attr3 = new Attr("xmlns:test", "test3");
        element.setAttachmentNode(attr1);
        element.setAttachmentNode(attr2);
        element.setAttachmentNode(attr3);

        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, null, null, null, 0), null);

        NodePointer result1 = iterator.getNodePointer();
        DOMAttributePointer expected1 = new DOMAttributePointer(new NodePointer(element, null, null, null, null, 0), attr1);
        Assert.assertEquals(expected1, result1);

        NodePointer result2 = iterator.getNodePointer();
        DOMAttributePointer expected2 = new DOMAttributePointer(new NodePointer(element, null, null, null, null, 0), attr2);
        Assert.assertEquals(expected2, result2);

        NodePointer result3 = iterator.getNodePointer();
        DOMAttributePointer expected3 = new DOMAttributePointer(new NodePointer(element, null, null, null, null, 0), attr3);
        Assert.assertEquals(expected3, result3);
    }
}