import org.apache.commons.jxpath.ri.model.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer("", null, null, null);
        QName name = new QName("", "", "testName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testTestAttr() {
        Attr attr = new Attr("testPrefix", "testValue");
        QName testName = new QName("testPrefix", "", "testLocalName");
        Assert.assertTrue(DOMAttributeIterator.testAttr(attr, testName));

        attr.setPrefix("nullPrefix");
        testName.setPrefix("nullPrefix");
        Assert.assertTrue(DOMAttributeIterator.testAttr(attr, testName));

        attr.setPrefix("xmlns");
        Assert.assertFalse(DOMAttributeIterator.testAttr(attr, testName));

        attr.setPrefix("xmlns");
        testName.setPrefix("xmlns");
        Assert.assertTrue(DOMAttributeIterator.testAttr(attr, testName));

        attr.setPrefix("xmlns");
        testName.setPrefix("testPrefix");
        testName.setNamespaceURI("testNamespace");
        Assert.assertTrue(DOMAttributeIterator.testAttr(attr, testName));
    }

    @Test
    public void testGetAttribute() {
        Element element = new Element("root");

        Attr attr1 = new Attr("testPrefix1", "testValue1");
        attr1.setPrefix("testPrefix1");
        element.setAttributeNode(attr1);

        Attr attr2 = new Attr("testPrefix2", "testValue2");
        attr2.setPrefix("testPrefix2");
        attr2.setNamespaceURI("testNamespace1");
        element.setAttributeNodeNS(attr2, "test:testLocalName");

        QName name1 = new QName("testPrefix1", "", "testLocalName1");
        QName name2 = new QName("testPrefix2", "testNamespace1", "testLocalName1");

        Attr returnedAttr1 = DOMAttributeIterator.getAttribute(element, name1);
        Assert.assertNotNull(returnedAttr1);
        Assert.assertEquals(attr1, returnedAttr1);

        Attr returnedAttr2 = DOMAttributeIterator.getAttribute(element, name2);
        Assert.assertNotNull(returnedAttr2);
        Assert.assertEquals(attr2, returnedAttr2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithNullElement() {
        Element nullElement = null;
        QName name = new QName("testPrefix", "", "testLocalName");
        DOMAttributeIterator.getAttribute(nullElement, name);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetAttributeWithNullName() {
        Element element = new Element("root");
        QName nullName = null;
        DOMAttributeIterator.getAttribute(element, nullName);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer parent = new NodePointer("", null, null, null);
        QName name = new QName("testPrefix", "", "testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attr attr1 = new Attr("testPrefix1", "testValue1");
        attr1.setPrefix("testPrefix1");
        Attr attr2 = new Attr("testPrefix2", "testValue2");
        attr2.setPrefix("testPrefix2");
        attr2.setNamespaceURI("testNamespace1");
        element.setAttributeNode(attr1);
        element.setAttributeNodeNS(attr2, "test:testLocalName");

        iterator.setPosition(1);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
        Assert.assertEquals(attr2, ((DOMAttributePointer) nodePointer).getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testGetNodePointerWithInvalidPosition() {
        NodePointer parent = new NodePointer("", null, null, null);
        QName name = new QName("testPrefix", "", "testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attr attr1 = new Attr("testPrefix1", "testValue1");
        attr1.setPrefix("testPrefix1");
        Attr attr2 = new Attr("testPrefix2", "testValue2");
        attr2.setPrefix("testPrefix2");
        attr2.setNamespaceURI("testNamespace1");
        element.setAttributeNode(attr1);
        element.setAttributeNodeNS(attr2, "test:testLocalName");

        iterator.setPosition(3);
        iterator.getNodePointer();
    }

    @Test
    public void testSetPosition() {
        NodePointer parent = new NodePointer("", null, null, null);
        QName name = new QName("testPrefix", "", "testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attr attr1 = new Attr("testPrefix1", "testValue1");
        attr1.setPrefix("testPrefix1");
        Attr attr2 = new Attr("testPrefix2", "testValue2");
        attr2.setPrefix("testPrefix2");
        attr2.setNamespaceURI("testNamespace1");
        element.setAttributeNode(attr1);
        element.setAttributeNodeNS(attr2, "test:testLocalName");

        iterator.setPosition(2);
        Assert.assertTrue(iterator.setPosition(2));

        iterator.setPosition(1);
        Assert.assertTrue(iterator.setPosition(1));

        iterator.setPosition(0);
        Assert.assertTrue(iterator.setPosition(0));

        iterator.setPosition(3);
        Assert.assertFalse(iterator.setPosition(3));

        iterator.setPosition(-1);
        Assert.assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testGetPosition() {
        NodePointer parent = new NodePointer("", null, null, null);
        QName name = new QName("testPrefix", "", "testLocalName");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);

        Element element = new Element("root");
        Attr attr1 = new Attr("testPrefix1", "testValue1");
        attr1.setPrefix("testPrefix1");
        Attr attr2 = new Attr("testPrefix2", "testValue2");
        attr2.setPrefix("testPrefix2");
        attr2.setNamespaceURI("testNamespace1");
        element.setAttributeNode(attr1);
        element.setAttributeNodeNS(attr2, "test:testLocalName");

        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }
}