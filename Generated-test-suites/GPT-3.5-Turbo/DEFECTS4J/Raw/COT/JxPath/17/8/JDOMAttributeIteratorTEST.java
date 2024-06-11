package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        parent = Mockito.mock(NodePointer.class);
        iterator = new JDOMAttributeIterator(parent, new QName());
    }

    @Test
    public void testGetNodePointer() {
        iterator.setPosition(1);
        NodePointer result = iterator.getNodePointer();
        Assert.assertNotNull(result);
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.getPosition());
    }
}