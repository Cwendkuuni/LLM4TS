package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;

import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class JDOMAttributeIteratorTest {

    @Mock
    private NodePointer parent;

    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setUp() {
        attributeIterator = new JDOMAttributeIterator(parent, new QName("", ""));
    }

    @Test
    public void testGetNodePointer() {
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_ValidPosition_ShouldReturnTrue() {
        boolean result = attributeIterator.setPosition(1);
        assertEquals(true, result);
        assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_InvalidPosition_ShouldReturnFalse() {
        boolean result = attributeIterator.setPosition(0);
        assertEquals(false, result);
        assertEquals(0, attributeIterator.getPosition());
    }
}