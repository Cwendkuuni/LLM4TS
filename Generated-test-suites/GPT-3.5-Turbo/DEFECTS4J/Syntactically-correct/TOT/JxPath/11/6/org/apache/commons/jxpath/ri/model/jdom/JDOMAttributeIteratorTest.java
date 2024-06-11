import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;

    @Before
    public void setup() {
        Element element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        parent = new NodePointer(element);
    }

    @Test
    public void testGetNodePointer_CurrentAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr2"));
        NodePointer pointer = iterator.getNodePointer();

        Assert.assertNotNull(pointer);
        Assert.assertEquals(Attribute.class, pointer.getNode().getClass());
        Assert.assertEquals("attr2", ((Attribute) pointer.getNode()).getName());
        Assert.assertEquals("value2", ((Attribute) pointer.getNode()).getValue());
    }

    @Test
    public void testGetNodePointer_NullAttribute() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr4"));
        NodePointer pointer = iterator.getNodePointer();

        Assert.assertNull(pointer);
    }

    @Test
    public void testGetPosition_InitialPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testGetPosition_SetPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr2"));

        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testGetPosition_InvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testSetPosition_AttributesExist() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(2, iterator.getPosition());

        NodePointer pointer = iterator.getNodePointer();
        Assert.assertNotNull(pointer);
        Assert.assertEquals(Attribute.class, pointer.getNode().getClass());
        Assert.assertEquals("attr2", ((Attribute) pointer.getNode()).getName());
        Assert.assertEquals("value2", ((Attribute) pointer.getNode()).getValue());
    }

    @Test
    public void testSetPosition_NoAttributesExist() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr4"));

        Assert.assertFalse(iterator.setPosition(1));
        Assert.assertEquals(0, iterator.getPosition());

        NodePointer pointer = iterator.getNodePointer();
        Assert.assertNull(pointer);
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("attr1"));

        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertEquals(0, iterator.getPosition());

        NodePointer pointer = iterator.getNodePointer();
        Assert.assertNull(pointer);
    }
}