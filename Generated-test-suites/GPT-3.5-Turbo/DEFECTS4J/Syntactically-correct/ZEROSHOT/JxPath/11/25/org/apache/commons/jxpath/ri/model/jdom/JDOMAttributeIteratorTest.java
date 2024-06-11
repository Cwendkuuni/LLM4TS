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
import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private JDOMAttributeIterator attributeIterator;
    private NodePointer parent;
    private List<Attribute> attributes;

    @Before
    public void setup() {
        parent = new NodePointer() {
            @Override
            public Object getNode() {
                return new Element("testElement");
            }
        };
        attributes = new ArrayList<>();
        attributes.add(new Attribute("attr1", "value1", Namespace.NO_NAMESPACE));
        attributes.add(new Attribute("attr2", "value2", Namespace.NO_NAMESPACE));
        attributes.add(new Attribute("attr3", "value3", Namespace.NO_NAMESPACE));
        attributeIterator = new JDOMAttributeIterator(parent, new QName("attribute"));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals(attributes.get(0), attributePointer.getAttribute());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertTrue(attributeIterator.setPosition(1));
        Assert.assertEquals(1, attributeIterator.getPosition());
        NodePointer nodePointer = attributeIterator.getNodePointer();
        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) nodePointer;
        Assert.assertEquals(attributes.get(0), attributePointer.getAttribute());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Assert.assertFalse(attributeIterator.setPosition(5));
        Assert.assertEquals(0, attributeIterator.getPosition());
    }
}