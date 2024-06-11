import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NamedNodeMap;
import org.junit.Assert;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIterator_whenCreateNewInstanceWithValidArguments_thenNewInstanceShouldBeCreated() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        assertTrue(iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testGetNodePointer_whenFirstAttributeIsReturned_thenCorrectNodePointerShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Attr attributeMock = new Attr("testName", "testValue");
        List<Attr> attributesMock = new ArrayList<>();
        attributesMock.add(attributeMock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = attributesMock;

        NodePointer nodePointer = iterator.getNodePointer();
        DOMAttributePointer expectedNodePointer = new DOMAttributePointer(parentMock, attributeMock);
        assertSame(expectedNodePointer, nodePointer);
    }

    @Test
    public void testGetNodePointer_whenLastAttributeIsReturned_thenCorrectNodePointerShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Attr attribute1Mock = new Attr("testName1", "testValue1");
        Attr attribute2Mock = new Attr("testName2", "testValue2");
        List<Attr> attributesMock = new ArrayList<>();
        attributesMock.add(attribute1Mock);
        attributesMock.add(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = attributesMock;

        NodePointer nodePointer = iterator.getNodePointer();
        NodePointer expectedNodePointer = new DOMAttributePointer(parentMock, attribute2Mock);
        assertSame(expectedNodePointer, nodePointer);
    }

    @Test
    public void testGetNodePointer_whenInvalidPositionIsProvided_thenNullShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = new ArrayList<>();

        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    @Test
    public void testSetPosition_whenValidPositionIsProvided_thenPositionShouldBeSet() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = new ArrayList<>();

        assertTrue(iterator.setPosition(1));
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_whenInvalidPositionIsProvided_thenFalseShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = new ArrayList<>();

        assertFalse(iterator.setPosition(-1));
    }

    @Test
    public void testSetPosition_whenLastPositionIsProvided_thenTrueShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Attr attribute1Mock = new Attr("testName1", "testValue1");
        Attr attribute2Mock = new Attr("testName2", "testValue2");
        List<Attr> attributesMock = new ArrayList<>();
        attributesMock.add(attribute1Mock);
        attributesMock.add(attribute2Mock);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentMock, nameMock);
        iterator.attributes = attributesMock;

        assertTrue(iterator.setPosition(2));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTestAttr_whenInvalidAttributeIsProvided_thenIllegalArgumentExceptionShouldBeThrown() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Attr attributeMock = new Attr("testName", "testValue");

        DOMAttributeIterator.testAttr(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTestAttr_whenNullAttributeIsProvided_thenIllegalArgumentExceptionShouldBeThrown() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");

        DOMAttributeIterator.testAttr(null);
    }

    @Test
    public void testTestAttr_whenValidAttributeIsProvided_thenTrueShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Attr attributeMock = new Attr("testName", "testValue");

        assertTrue(DOMAttributeIterator.testAttr(attributeMock));
    }

    @Test
    public void testTestAttr_whenInvalidAttributePrefixIsProvided_thenFalseShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName:testName", "testPrefix");
        Attr attributeMock = new Attr("testName:testName", "testValue");

        assertFalse(DOMAttributeIterator.testAttr(attributeMock));
    }

    @Test
    public void testTestAttr_whenValidAttributePrefixIsProvided_thenTrueShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName:testName", "testPrefix");
        Attr attributeMock = new Attr("testName:testName", "testValue");
        String expectedNamespaceURI = "testNamespaceURI";

        parentMock.setNamespaceURI("testNamespaceURI");
        assertTrue(DOMAttributeIterator.testAttr(attributeMock));
    }

    @Test(expected = NullPointerException.class)
    public void testGetAttribute_whenNullElementIsProvided_thenNullPointerExceptionShouldBeThrown() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");

        DOMAttributeIterator.getAttribute(null, nameMock);
    }

    @Test
    public void testGetAttribute_whenValidElementAndNameIsProvided_thenCorrectAttrShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Element elementMock = new Element("testElement");
        Attr attributeMock = new Attr("testName", "testValue");
        elementMock.setAttributeNode(attributeMock);

        Attr expectedAttr = DOMAttributeIterator.getAttribute((Element) parentMock.getNode(), nameMock);
        assertSame(expectedAttr, attributeMock);
    }

    @Test
    public void testGetAttribute_whenValidElementAndNameNSIsProvided_thenCorrectAttrShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        String namespaceURI = "testNamespaceURI";
        QName nameMock = new QName("testName", null, namespaceURI);
        Element elementMock = new Element("testElement");
        Attr attributeMock = new Attr("testName", "testValue");
        elementMock.setAttributeNodeNS(attributeMock, namespaceURI);

        Attr expectedAttr = DOMAttributeIterator.getAttribute((Element) parentMock.getNode(), nameMock);
        assertSame(expectedAttr, attributeMock);
    }

    @Test
    public void testGetAttribute_whenValidElementAndNameIsNotPresent_thenNullShouldBeReturned() {
        NodePointer parentMock = new NodePointer(new Object() {}, 0);
        QName nameMock = new QName("testName", "testPrefix");
        Element elementMock = new Element("testElement");

        Attr expectedAttr = DOMAttributeIterator.getAttribute((Element) parentMock.getNode(), nameMock);
        assertNull(expectedAttr);
    }
}