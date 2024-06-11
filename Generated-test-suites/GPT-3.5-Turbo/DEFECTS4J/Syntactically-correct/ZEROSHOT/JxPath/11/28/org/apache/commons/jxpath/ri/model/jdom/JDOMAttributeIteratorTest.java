import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator iterator;

    @Before
    public void setup() {
        // Create parent NodePointer
        Element element = new Element("testElement");
        parent = new JDOMNodePointer(element);

        // Create attributes
        Attribute attribute1 = new Attribute("attr1", "value1");
        Attribute attribute2 = new Attribute("attr2", "value2");
        Attribute attribute3 = new Attribute("attr3", "value3");
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);
        attributes.add(attribute3);

        // Create JDOMAttributeIterator
        iterator = new JDOMAttributeIterator(parent, new QName(null, "*"));

        // Set attributes as private field of JDOMAttributeIterator
        iterator.attributes = attributes;
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
        Assert.assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPositionValid() {
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        boolean result = iterator.setPosition(5);
        Assert.assertFalse(result);
        Assert.assertEquals(5, iterator.getPosition());
    }
}