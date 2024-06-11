package org.apache.commons.jxpath.ri.axes;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class AttributeContextTest {
    private EvalContext parentContext;
    private NodeTest nodeTest;
    private AttributeContext attributeContext;

    @Before
    public void setUp() {
        parentContext = Mockito.mock(EvalContext.class);
        nodeTest = Mockito.mock(NodeNameTest.class);
        attributeContext = new AttributeContext(parentContext, nodeTest);
    }

    @Test
    public void testConstructor() {
        assertEquals(parentContext, attributeContext.getParentContext());
        assertEquals(nodeTest, attributeContext.getNodeTest());
    }

    @Test
    public void testGetCurrentNodePointer() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        Mockito.when(nodePointer.attributeIterator(Mockito.any(QName.class))).thenReturn(new AttributeIteratorMock());

        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertNull(currentNodePointer);

        attributeContext.nextNode();
        NodePointer expectedNodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(iterator.getNodePointer()).thenReturn(expectedNodePointer);
        assertEquals(expectedNodePointer, currentNodePointer);
    }

    @Test
    public void testReset() {
        attributeContext.nextNode();
        attributeContext.reset();
        assertNull(attributeContext.getCurrentNodePointer());
    }

    @Test
    public void testSetPosition() {
        attributeContext.nextNode();
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        QName name = Mockito.mock(QName.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(nodePointer.attributeIterator(name)).thenReturn(iterator);

        attributeContext.setPosition(1);
        Mockito.verify(iterator, Mockito.times(1)).setPosition(1);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(iterator.getNodePointer(), currentNodePointer);
    }

    @Test
    public void testNextNode() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        Mockito.when(parentContext.getCurrentNodePointer()).thenReturn(nodePointer);
        QName name = Mockito.mock(QName.class);
        NodeIterator iterator = Mockito.mock(NodeIterator.class);
        Mockito.when(nodePointer.attributeIterator(name)).thenReturn(iterator);

        attributeContext.nextNode();
        Mockito.verify(iterator, Mockito.times(1)).setPosition(1);
        NodePointer currentNodePointer = attributeContext.getCurrentNodePointer();
        assertEquals(iterator.getNodePointer(), currentNodePointer);

        Mockito.when(iterator.getNodePointer()).thenReturn(null);
        attributeContext.nextNode();
        assertNull(attributeContext.getCurrentNodePointer());
    }
}

class AttributeIteratorMock implements NodeIterator {
    int position = 0;

    @Override
    public boolean hasNext() {
        return position < 10;
    }

    @Override
    public boolean hasPrevious() {
        return position > 0;
    }

    @Override
    public int getPosition() {
        return position;
    }

    @Override
    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    public NodeIterator iterator() {
        return this;
    }

    @Override
    public boolean nextNode() {
        position++;
        return true;
    }

    @Override
    public boolean previousNode() {
        position--;
        return true;
    }

    @Override
    public NodePointer getNodePointer() {
        return null;
    }
}