package org.junit.tests;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setup() {
        parentContext = new EvalContext(null);
        nodeTest = new NodeNameTest(new QName("test:attribute"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    // Test methods for AttributeContext
    // ... Expert 1, 2, and 3's test methods here ...

    // Test method for attributeIterator()
    @Test
    public void testAttributeIterator() {
        // Typical use case
        EvalContext parentContext = new EvalContext(null);
        NodeTest nodeTest = new NodeNameTest(new QName("test:parent"));
        EvalContext parentContextWithAttribute = new AttributeContext(parentContext, nodeTest);
        NodePointer parentNodePointer = parentContextWithAttribute.getCurrentNodePointer();
        NodeIterator nodeIterator = parentNodePointer.attributeIterator(new QName("test:attribute"));
        NodePointer nodePointer = nodeIterator.nextNodePointer();
        Assert.assertNotNull(nodePointer);

        // Edge case: null parentContext
        parentContext = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> parentContextWithAttribute.iterator = parentNodePointer.attributeIterator(new QName("test:attribute")));

        // Error scenario: null nodeTest
        nodeTest = null;
        attributeContext = new AttributeContext(parentContext, nodeTest);
        Assert.assertThrows(IllegalArgumentException.class, () -> attributeContext.nextNode());

        // Error scenario: null parentContextWithAttribute
        parentContextWithAttribute = null;
        Assert.assertThrows(IllegalArgumentException.class, () -> parentNodePointer.attributeIterator(new QName("test:attribute")));
    }
}