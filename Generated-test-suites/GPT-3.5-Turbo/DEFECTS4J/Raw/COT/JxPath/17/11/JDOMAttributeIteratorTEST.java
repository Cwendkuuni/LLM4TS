import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
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

    private Element parentElement;
    private NodePointer parent;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup() {
        parentElement = new Element("parent");
        parent = new JDOMAttributePointer(parentElement);
        attributeIterator = new JDOMAttributeIterator(parent, new QName("*"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
    }

    @Test
    public void testGetPosition() {
        int position = attributeIterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        boolean result = attributeIterator.setPosition(2);
        Assert.assertTrue(result);

        result = attributeIterator.setPosition(0);
        Assert.assertFalse(result);
    }
}