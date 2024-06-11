
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {
    private NodePointer parent;
    private QName name;

    @Before
    public void setup() {
        Document document = new Document();
        Element parentElement = new Element("parent");
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3");
        parentElement.getAttributes().add(attribute1);
        parentElement.getAttributes().add(attribute2);
        parentElement.getAttributes().add(attribute3);
        document.addContent(parentElement);

        parent = new JDOMNodePointer(document.getRootElement());
        name = new QName("attr1");
    }

    @Test
    public void testConstructor() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(parent, attributeIterator.getParent());
        Assert.assertEquals(3, attributeIterator.getAttributes().size());
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer1 = attributeIterator.getNodePointer();
        Assert.assertEquals(parent, nodePointer1.getParent());
        Assert.assertTrue(nodePointer1 instanceof JDOMAttributePointer);
        Assert.assertEquals("attr1", ((JDOMAttributePointer) nodePointer1).getAttribute().getName());
    }

    @Test
    public void testGetPosition() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(0, attributeIterator.getPosition());
        attributeIterator.setPosition(2);
        Assert.assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        JDOMAttributeIterator attributeIterator = new JDOMAttributeIterator(parent, name);
        Assert.assertFalse(attributeIterator.setPosition(0));
        Assert.assertTrue(attributeIterator.setPosition(1));
        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(3));
        Assert.assertEquals(3, attributeIterator.getPosition());
        Assert.assertFalse(attributeIterator.setPosition(4));
    }
}
