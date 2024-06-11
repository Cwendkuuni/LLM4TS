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
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attr1 = new Attr("test1", "value1");
        Attr attr2 = new Attr("test2", "value2");
        iterator.attributes.add(attr1);
        iterator.attributes.add(attr2);
        NodePointer expected = new DOMAttributePointer(parent, attr1);
        NodePointer actual = iterator.getNodePointer();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attr1 = new Attr("test1", "value1");
        Attr attr2 = new Attr("test2", "value2");
        iterator.attributes.add(attr1);
        iterator.attributes.add(attr2);
        int expected = 1;
        int actual = iterator.getPosition();
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition1() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attr1 = new Attr("test1", "value1");
        Attr attr2 = new Attr("test2", "value2");
        iterator.attributes.add(attr1);
        iterator.attributes.add(attr2);
        boolean expected = true;
        boolean actual = iterator.setPosition(1);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testSetPosition2() {
        NodePointer parent = new NodePointer(null, null, 0);
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Attr attr1 = new Attr("test1", "value1");
        Attr attr2 = new Attr("test2", "value2");
        iterator.attributes.add(attr1);
        iterator.attributes.add(attr2);
        boolean expected = false;
        boolean actual = iterator.setPosition(3);
        Assert.assertEquals(expected, actual);
    }

    @Test
    public void testTestAttr() {
        String testPrefix1 = "xmlns";
        String testPrefix2 = null;
        String testLocalName1 = "xmlns";
        String testLocalName2 = "test";
        String nodePrefix1 = "xmlns";
        String nodePrefix2 = null;
        String nodeLocalName1 = "xmlns";
        String nodeLocalName2 = "test";
        String testNS1 = "http://test.com";
        String testNS2 = null;
        String nodeNS1 = "http://test.com";
        String nodeNS2 = null;
        boolean expected1 = false;
        boolean expected2 = false;
        boolean expected3 = false;
        boolean expected4 = true;
        boolean actual1 = DOMAttributeIterator.testAttr(new Attr("xmlns:test", "value"), new QName("test", testNS1));
        boolean actual2 = DOMAttributeIterator.testAttr(new Attr("test", "value"), new QName("test", testNS1));
        boolean actual3 = DOMAttributeIterator.testAttr(new Attr("xmlns:test", "value"), new QName("test", null));
        boolean actual4 = DOMAttributeIterator.testAttr(new Attr("test", "value"), new QName("test", null));
        Assert.assertEquals(expected1, actual1);
        Assert.assertEquals(expected2, actual2);
        Assert.assertEquals(expected3, actual3);
        Assert.assertEquals(expected4, actual4);
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("test");
        QName name = new QName("test", null, "http://test.com");
        Attr attr = new Attr("test", "value");
        element.setAttributeNode(attr);
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, 0), name);
        Attr actual = iterator.getAttribute(element, name);
        Attr expected = attr;
        Assert.assertEquals(expected, actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeNullElement() {
        QName name = new QName("test", null, "http://test.com");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(null, null, 0), name);
        iterator.getAttribute(null, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeNullName() {
        Element element = new Element("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new NodePointer(element, null, 0), null);
        iterator.getAttribute(element, null);
    }

    @Test
    public void testEqualsStrings() {
        String s1 = "test1";
        String s2 = "test1";
        String s3 = "test2";
        boolean expected = true;
        boolean actual = DOMAttributeIterator.equalStrings(s1, s2);
        Assert.assertEquals(expected, actual);
        Assert.assertEquals(expected, DOMAttributeIterator.equalStrings(s1, s1));
        Assert.assertEquals(false, DOMAttributeIterator.equalStrings(s1, s3));
    }
}