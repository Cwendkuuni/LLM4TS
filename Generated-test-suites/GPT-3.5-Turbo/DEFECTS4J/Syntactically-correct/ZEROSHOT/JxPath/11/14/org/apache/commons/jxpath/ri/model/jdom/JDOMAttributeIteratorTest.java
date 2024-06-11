import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Attribute;
import org.jdom.Namespace;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {

        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new JDOMNodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        Attribute attribute = (Attribute) element.getAttributes().get(0);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals(attribute, nodePointer.getNode());
    }

    @Test
    public void testGetPosition() {

        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new JDOMNodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {

        Element element = new Element("test");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parent = new JDOMNodePointer(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("*"));

        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());
    }
}