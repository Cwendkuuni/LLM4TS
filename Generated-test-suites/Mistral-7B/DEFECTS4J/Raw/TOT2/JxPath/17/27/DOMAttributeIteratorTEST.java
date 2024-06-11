package org.apache.commons.jxpath.ri.model.dom.test;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;
import org.apache.commons.jxpath.ri.DOMPoint;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMTest;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest extends TestCase {

    private Test DOMTest;

    public DOMAttributeIteratorTest(String name) {
        super(name);
    }

    protected void setUp() throws Exception {
        DOMTest = new DOMTest();
        DOMTest.setUp();
    }

    protected void tearDown() throws Exception {
        DOMTest.tearDown();
    }

    public static Test suite() {
        return new TestSuite(DOMAttributeIteratorTest.class);
    }

    // Test for constructor
    public static void testConstructorWithValidArguments(Test test) {
        new DOMAttributeIteratorTest("testConstructorWithValidArguments") {
            @Override
            protected void runTest() throws Exception {
                testConstructorWithValidArguments(DOMTest);
            }
        }.run();
    }

    // Test for getNodePointer()
    public static void testgetNodePointerWithValidPosition(Test test) {
        new DOMAttributeIteratorTest("testgetNodePointerWithValidPosition") {
            @Override
            protected void runTest() throws Exception {
                testgetNodePointerWithValidPosition(DOMTest);
            }
        }.run();
    }

    public static void testgetNodePointerWithInvalidPosition(Test test) {
        new DOMAttributeIteratorTest("testgetNodePointerWithInvalidPosition") {
            @Override
            protected void runTest() throws Exception {
                testgetNodePointerWithInvalidPosition(DOMTest);
            }
        }.run();
    }

    // Test for testAttr(Attr attr)
    public static void testTestAttrWithValidArguments(Test test) {
        new DOMAttributeIteratorTest("testTestAttrWithValidArguments") {
            @Override
            protected void runTest() throws Exception {
                testTestAttrWithValidArguments(DOMTest);
            }
        }.run();
    }

    public static void testTestAttrWithInvalidArguments(Test test) {
        new DOMAttributeIteratorTest("testTestAttrWithInvalidArguments") {
            @Override
            protected void runTest() throws Exception {
                testTestAttrWithInvalidArguments(DOMTest);
            }
        }.run();
    }

    // Test for getAttribute(Element element, QName name)
    public static void testGetAttributeWithValidArguments(Test test) {
        new DOMAttributeIteratorTest("testGetAttributeWithValidArguments") {
            @Override
            protected void runTest() throws Exception {
                testGetAttributeWithValidArguments(DOMTest);
            }
        }.run();
    }

    public static void testGetAttributeWithInvalidArguments(Test test) {
        new DOMAttributeIteratorTest("testGetAttributeWithInvalidArguments") {
            @Override
            protected void runTest() throws Exception {
                testGetAttributeWithInvalidArguments(DOMTest);
            }
        }.run();
    }

    // Constructor test
    public void testConstructorWithValidArguments(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = new QName("test", "test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        // check the size of the iterator
        assertEquals(1, iterator.getSize());
        // check if the first attribute is the expected one
        Attr attribute = iterator.getNodePointer().getNode();
        assertEquals("test", attribute.getNodeName());
    }

    // getNodePointer() test
    public void testNodePointerWithValidPosition(DOMTest DOMTest) {
        DOMAttributeIterator iterator = setupDOMAttributeIterator(DOMTest);
        NodePointer nodePointer = iterator.getNodePointer();
        assertEquals(DOMTest.getDocument(), nodePointer.getNode().getOwnerDocument());
    }

    public void testNodePointerWithInvalidPosition(DOMTest DOMTest) {
        DOMAttributeIterator iterator = setupDOMAttributeIterator(DOMTest);
        iterator.setPosition(iterator.getSize() + 1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNull(nodePointer);
    }

    // testAttr(Attr attr) test
    public void testTestAttrWithValidArguments(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = new QName("xmlns:xlink", "type");
        Element element = DOMTest.getDocument().createElement("element");
        Attr attr = element.createAttributeNS("xmlns:xlink", "type");
        String attrValue = "simple";
        attr.setValue(attrValue);
        element.setAttributeNodeNS(attr);

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertTrue(iterator.testAttr(attr));
    }

    public void testTestAttrWithInvalidArguments(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = null;
        Element element = DOMTest.getDocument().createElement("element");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertFalse(iterator.testAttr(null));
    }

    // getAttribute(Element element, QName name) test
    public void testGetAttributeWithValidArguments(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = new QName("test", "test");
        Element element = DOMTest.getDocument().createElement("element");
        element.setAttribute("test", "testValue");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        Attr attribute = iterator.getNodePointer().getNode();
        assertEquals("testValue", attribute.getNodeValue());
    }

    public void testGetAttributeWithInvalidArguments(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = null;
        Element element = DOMTest.getDocument().createElement("element");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        assertNull(iterator.getNodePointer());
    }

    // Helper methods
    public DOMAttributeIterator setupDOMAttributeIterator(DOMTest DOMTest) {
        NodePointer parentPointer = new DOMPoint(DOMTest.getDocument(), new QName("root"));
        QName name = new QName("test", "test");
        Element element = DOMTest.getDocument().createElement("element");
        element.setAttribute("test", "testValue");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parentPointer, name);
        return iterator;
    }
}