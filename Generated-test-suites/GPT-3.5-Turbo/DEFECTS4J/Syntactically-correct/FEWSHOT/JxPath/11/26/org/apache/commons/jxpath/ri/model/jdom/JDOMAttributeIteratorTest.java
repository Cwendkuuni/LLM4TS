package org.apache.commons.jxpath.ri.model.jdom;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private Element element;
    private List<Attribute> attributes;

    @Before
    public void setup() {
        parent = Mockito.mock(NodePointer.class);
        name = new QName("test", "testNamespace");
        element = Mockito.mock(Element.class);
        attributes = new ArrayList<>();
    }

    @Test
    public void testConstructor() {
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(name.getPrefix()).thenReturn("");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(element.getAttributes()).thenReturn(attributes);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(parent, iterator.getParent());
    }

    @Test
    public void testGetPosition() {
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(name.getPrefix()).thenReturn("");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(element.getAttributes()).thenReturn(attributes);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(name.getPrefix()).thenReturn("");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(element.getAttributes()).thenReturn(attributes);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertTrue(iterator.setPosition(2));
    }

    @Test
    public void testGetNodePointer() {
        Mockito.when(parent.getNode()).thenReturn(element);
        Mockito.when(name.getPrefix()).thenReturn("");
        Mockito.when(name.getName()).thenReturn("*");
        Mockito.when(element.getAttributes()).thenReturn(attributes);

        JDOMAttributeIterator iterator = Mockito.spy(new JDOMAttributeIterator(parent, name));

        Assert.assertNull(iterator.getNodePointer());
    }
}