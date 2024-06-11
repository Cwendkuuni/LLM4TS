package org.apache.commons.jxpath.ri.axes;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class AttributeContextTest {

    private Document doc;
    private EvalContext parentContext;
    private AttributeContext attributeContext;

    @Before
    public void setUp() throws Exception {
        doc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element root = doc.createElement("root");
        root.setAttribute("test", "testValue");
        doc.appendChild(root);
        parentContext = new EvalContext(null, null);
        parentContext.setDocument(doc);
        parentContext.setCurrentNodePointer(root);
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
    }

    @Test
    public void testNextNodeWithValidAttributeTest() {
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.createAttributeNode("test", "test"));;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("test", actualNodePointer.getNodeName().getLocalPart());
        assertEquals("testValue", actualNodePointer.getNodeValue());
        assertEquals("root", actualNodePointer.getParentNode().getNodeName().getLocalPart());
    }

    @Test
    public void testNextNodeWithNonExistentAttributeTest() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithValidPositionTest() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(1);
        NodePointer expectedNodePointer = new NodePointer(doc.createAttributeNode("test", "test"));;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testSetPositionWithInvalidPositionTest() {
        attributeContext.nextNode();
        attributeContext.nextNode();
        attributeContext.setPosition(3);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPositionWithStartPositionTest() {
        attributeContext.reset();
        attributeContext.setPosition(0);
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testNextNodeWithDifferentAttributeTest() {
        Element childElement = doc.createElement("child");
        root.appendChild(childElement);
        childElement.setAttribute("test2", "testValue2");
        parentContext.setCurrentNodePointer(childElement);
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("test2"));
        attributeContext.nextNode();
        NodePointer expectedNodePointer = new NodePointer(doc.createAttributeNode("test2", "test2"));;
        NodePointer actualNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
        assertEquals("test2", actualNodePointer.getNodeName().getLocalPart());
        assertEquals("testValue2", actualNodePointer.getNodeValue());
    }

    @Test
    public void testNextNodeWithEmptyParentNodeTest() {
        parentContext.setCurrentNodePointer(doc.createDocumentType("root", null, null));
        attributeContext = new AttributeContext(parentContext, new NodeNameTest("test"));
        assertFalse(attributeContext.nextNode());
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNodeWithNullNodeTest() {
        attributeContext = new AttributeContext(parentContext, null);
        attributeContext.nextNode();
    }
}