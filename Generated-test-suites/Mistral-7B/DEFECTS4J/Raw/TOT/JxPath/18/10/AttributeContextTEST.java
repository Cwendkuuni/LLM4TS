package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.axes.AttributeContext;
import org.apache.commons.jxpath.ri.axes.AttributeContextImpl;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.test.AbstractJXPathTest;
import org.junit.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

/**
 * Test cases for AttributeContext class.
 */
public class AttributeContextTest extends AbstractJXPathTest {

    @Test
    public void testAttributeContext_EmptyNode() {
        EvalContext parentContext = new EvalContext(null);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest(""));
        assertFalse("nextNode should return false for empty node", attributeContext.nextNode());
    }

    @Test
    public void testAttributeContext_AttributeExists() {
        String xml = "<root attr='value'></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr"));
        assertFalse("nextNode should return false before first attribute", attributeContext.nextNode());
        NodePointer attributeNodePointer = attributeContext.nextNode();
        assertNotNull("Current node pointer should not be null", attributeNodePointer);
        QName attributeName = attributeNodePointer.getName();
        assertEquals("attribute name should be 'attr'", attributeName.getLocalPart(), "attr");
        assertEquals("attribute value should be 'value'", attributeNodePointer.getValue(), "value");
        assertFalse("nextNode should return false after first attribute", attributeContext.nextNode());
    }

    @Test
    public void testAttributeContext_AttributeDoesNotExist() {
        String xml = "<root></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("nonExistentAttribute"));
        assertFalse("nextNode should return false for non-existent attribute", attributeContext.nextNode());
    }

    @Test
    public void testAttributeContext_NextNode() {
        String xml = "<root attr1='value1' attr2='value2'></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr1"));
        NodePointer firstAttributeNodePointer = attributeContext.nextNode();
        assertNotNull("Current node pointer should not be null", firstAttributeNodePointer);
        assertEquals("First attribute name should be 'attr1'", firstAttributeNodePointer.getName().getLocalPart(), "attr1");
        assertEquals("First attribute value should be 'value1'", firstAttributeNodePointer.getValue(), "value1");
        NodePointer secondAttributeNodePointer = attributeContext.nextNode();
        assertNotNull("Current node pointer should not be null", secondAttributeNodePointer);
        assertEquals("Second attribute name should be 'attr2'", secondAttributeNodePointer.getName().getLocalPart(), "attr2");
        assertEquals("Second attribute value should be 'value2'", secondAttributeNodePointer.getValue(), "value2");
        assertFalse("nextNode should return false after last attribute", attributeContext.nextNode());
    }

    @Test
    public void testAttributeContext_Reset() {
        String xml = "<root attr1='value1' attr2='value2'></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr1"));
        NodePointer firstAttributeNodePointer = attributeContext.nextNode();
        assertNotNull("Current node pointer should not be null", firstAttributeNodePointer);
        attributeContext.reset();
        assertNull("Current node pointer should be null after reset", attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testAttributeContext_SetPosition() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr2"));
        NodePointer secondAttributeNodePointer = attributeContext.nextNode();
        assertNotNull("Current node pointer should not be null", secondAttributeNodePointer);
        int position = 1;
        attributeContext.setPosition(position);
        NodePointer expectedNodePointer = secondAttributeNodePointer;
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals("Current node pointer should match expected node pointer", currentNodePointer, expectedNodePointer);
    }

    @Test
    public void testAttributeContext_SetPosition_OutOfBounds() {
        String xml = "<root attr1='value1' attr2='value2' attr3='value3'></root>";
        NodePointer rootNodePointer = documentBuilder.buildDocument(xml).getDocumentElement();
        EvalContext parentContext = new EvalContext(rootNodePointer);
        AttributeContext attributeContext = new AttributeContext(parentContext, new NodeNameTest("attr2"));
        int position = 4;
        attributeContext.setPosition(position);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull("Current node pointer should be null when position is out of bounds", currentNodePointer);
    }
}