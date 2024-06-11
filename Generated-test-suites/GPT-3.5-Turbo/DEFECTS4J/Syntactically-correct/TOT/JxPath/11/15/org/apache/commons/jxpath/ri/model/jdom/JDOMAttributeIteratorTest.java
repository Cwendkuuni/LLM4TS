import org.junit.Assert;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;
import java.util.Collections;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer_NormalAttribute() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");

        NodePointer parent = new JDOMAttributePointer(null, element.getAttribute("attr1"));
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals("attr1", ((Attribute) nodePointer.getNode()).getName());
        Assert.assertEquals("value1", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetNodePointer_AttributeWithPrefix() {
        Element element = new Element("test");
        Namespace ns = Namespace.getNamespace("prefix", "http://example.com");
        element.setAttribute("prefix:attr2", "value2", ns);

        NodePointer parent = new JDOMAttributePointer(null, element.getAttribute("prefix:attr2", ns));
        QName name = new QName("prefix", "attr2");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals("prefix:attr2", ((Attribute) nodePointer.getNode()).getName());
        Assert.assertEquals("value2", ((Attribute) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMAttributePointer(null, element.getAttribute("attr1"));
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMAttributePointer(null, element.getAttribute("attr1"));
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(2);

        Assert.assertTrue(result);
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");

        NodePointer parent = new JDOMAttributePointer(null, element.getAttribute("attr1"));
        QName name = new QName("attr1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(3);

        Assert.assertFalse(result);
        Assert.assertEquals(0, iterator.getPosition());
    }

}