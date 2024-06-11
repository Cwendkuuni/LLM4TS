import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator iterator;

    @Before
    public void setUp() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        iterator = new JDOMAttributeIterator(new NodePointer(element), new QName("attr1"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr1", attributePointer.getNode().getName());
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.getPosition());
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals("attr1", attributePointer.getNode().getName());
        Assert.assertEquals("value1", attributePointer.getNode().getValue());
    }
}