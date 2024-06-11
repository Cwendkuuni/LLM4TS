package org.apache.commons.jxpath.ri.axes.test;

import org.apache.commons.jxpath.ri.EvalContext;
import org.apache.commons.jxpath.ri.NodeTest;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.compiler.NodeNameTest;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
@PrepareForTest({AttributeContext.class})
public class AttributeContextTest {

    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.spy(EvalContext.class);
        nodeTest = Mockito.mock(NodeTest.class);
        PowerMockito.when(nodeTest.getNodeName()).thenReturn(new QName("test"));
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testAttributeContextConstructor() {
        Assert.assertNotNull(attributeContext);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testgetCurrentNodePointer() {
        Assert.assertNull(attributeContext.getCurrentNodePointer());
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        PowerMockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        PowerMockito.when(nodePointer.getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(Mockito.mock(NodeIterator.class));
        attributeContext.nextNode();
        Assert.assertSame(nodePointer, attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testReset() {
        PowerMockito.when(parentContext.getCurrentNodePointer()).thenReturn(null);
        attributeContext.reset();
        Assert.assertFalse(attributeContext.isSetStarted());
        Assert.assertNull(attributeContext.iterator);
        Assert.assertSame(parentContext, attributeContext.getParentContext());
        Assert.assertSame(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testSetPosition() {
        PowerMockito.when(parentContext.getCurrentNodePointer()).thenReturn(null);
        PowerMockito.when(parentContext.getPosition()).thenReturn(0);
        PowerMockito.when(parentContext.setPosition(Mockito.anyInt())).thenReturn(true);
        Assert.assertTrue(attributeContext.setPosition(1));
        PowerMockito.verify(parentContext).setPosition(1);
    }

    @Test
    public void testNextNode_NoAttributeFound() {
        PowerMockito.when(parentContext.getCurrentNodePointer())
                .thenReturn(Mockito.mock(NodePointer.class));
        PowerMockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(null);
        Assert.assertFalse(attributeContext.nextNode());
    }

    @Test
    public void testNextNode_AttributeFound() {
        PowerMockito.when(parentContext.getCurrentNodePointer())
                .thenReturn(Mockito.mock(NodePointer.class));
        NodeIterator nodeIterator = Mockito.mock(NodeIterator.class);
        PowerMockito.when(nodeIterator.getNodePointer())
                .thenReturn(Mockito.mock(NodePointer.class));
        PowerMockito.when(parentContext.getCurrentNodePointer().getAttributeIterator(Mockito.any(QName.class)))
                .thenReturn(nodeIterator);
        PowerMockito.when(nodeIterator.setPosition(Mockito.anyInt()))
                .thenReturn(true);
        PowerMockito.when(nodeIterator.hasNext())
                .thenReturn(true);
        PowerMockito.when(nodeIterator.nextNode())
                .thenReturn(Mockito.mock(NodePointer.class));
        attributeContext.nextNode();
        Assert.assertFalse(attributeContext.nextNode());
        PowerMockito.verify(nodeIterator).nextNode();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNextNode_NullNodeTest() {
        PowerMockito.when(parentContext.getCurrentNodePointer())
                .thenReturn(Mockito.mock(NodePointer.class));
        AttributeContext attributeContext = new AttributeContext(parentContext, null);
        attributeContext.nextNode();
    }

    @Test(expected = IllegalStateException.class)
    public void testNextNode_SetPositionCalledBeforeSetStarted() {
        PowerMockito.when(parentContext.getCurrentNodePointer())
                .thenReturn(Mockito.mock(NodePointer.class));
        AttributeContext attributeContext = new AttributeContext(parentContext, nodeTest);
        PowerMockito.when(attributeContext.iterator).thenReturn(Mockito.mock(NodeIterator.class));
        PowerMockito.when(attributeContext.iterator.hasNext())
                .thenReturn(true);
        PowerMockito.when(attributeContext.iterator.nextNode())
                .thenReturn(Mockito.mock(NodePointer.class));
        attributeContext.setPosition(1);
        attributeContext.nextNode();
    }
}