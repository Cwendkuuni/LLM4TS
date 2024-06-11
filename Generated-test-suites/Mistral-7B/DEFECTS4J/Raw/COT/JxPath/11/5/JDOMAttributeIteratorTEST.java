import org.apache.commons.jxpath.ri.model.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * JDOMAttributeIteratorTest class for testing JDOMAttributeIterator methods
 */
public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute", "testValue", "testNamespace");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        Assert.assertEquals(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute")), attributeIterator.getNodePointer());
        Assert.assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("*", null, null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        testElement.addAttribute("testAttribute3", "testValue3");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute3")));

        for (int i = 0; i < attributeIterator.getSize(); i++) {
            Assert.assertEquals(expectedNodes.get(i), attributeIterator.getNodePointer());
            expectedNodes.remove(i);
            attributeIterator.next();
        }
        Assert.assertEquals(0, attributeIterator.getSize());
    }

    @Test(expected = IllegalStateException.class)
    public void testJDOMAttributeIterator_EmptyList() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);
    }

    @Test
    public void testJDOMAttributeIterator_Namespace() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", "xmlns");
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addNamespaceDeclaration("xmlns", "http://www.w3.org/2000/xmlns/");
        testElement.addAttribute("xml:testAttribute", "testValue");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        Assert.assertEquals(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("xml:testAttribute")), attributeIterator.getNodePointer());
        Assert.assertEquals(1, attributeIterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_OutOfBounds() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();
        attributeIterator.next();

        Assert.assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SetPosition() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")));

        attributeIterator.next();
        Assert.assertEquals(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")), attributeIterator.getNodePointer());
        Assert.assertEquals(1, attributeIterator.getPosition());

        attributeIterator.setPosition(0);
        Assert.assertEquals(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")), attributeIterator.getNodePointer());
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_Size() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")));

        for (int i = 0; i < attributeIterator.getSize(); i++) {
            Assert.assertEquals(expectedNodes.get(i), attributeIterator.getNodePointer());
            expectedNodes.remove(i);
            attributeIterator.next();
        }
        Assert.assertEquals(0, attributeIterator.getSize());
    }

    @Test
    public void testJDOMAttributeIterator_GetPosition() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")));

        for (int i = 0; i < attributeIterator.getSize(); i++) {
            Assert.assertEquals(expectedNodes.get(i), attributeIterator.getNodePointer());
            expectedNodes.remove(i);
            attributeIterator.next();
        }
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_Next() {
        NodePointer parentPointer = new JDOMPointerTest.DummyNodePointer();
        QName qName = new QName("test", "testNamespace", null);
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parentPointer, qName);

        Element testElement = new Element("testElement");
        testElement.addAttribute("testAttribute1", "testValue1");
        testElement.addAttribute("testAttribute2", "testValue2");
        NodePointer testParentPointer = new JDOMPointerTest.DummyNodePointer(testElement);

        List<NodePointer> expectedNodes = new ArrayList<>();
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute1")));
        expectedNodes.add(new JDOMAttributePointer(testParentPointer, testElement.getAttribute("testAttribute2")));

        for (int i = 0; i < attributeIterator.getSize(); i++) {
            Assert.assertEquals(expectedNodes.get(i), attributeIterator.getNodePointer());
            expectedNodes.remove(i);
            attributeIterator.next();
        }
        Assert.assertEquals(0, attributeIterator.getPosition());
    }
}