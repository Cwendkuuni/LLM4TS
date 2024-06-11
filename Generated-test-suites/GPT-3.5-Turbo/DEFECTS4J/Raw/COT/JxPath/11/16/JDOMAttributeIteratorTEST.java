
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = null;
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertFalse(iterator.setPosition(1));
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("Elem");
        NodePointer parent = new NodePointer("node", element);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.getNodePointer());

        Namespace ns = element.getNamespace();
        QName attrName = new QName("attribute", ns.getPrefix());

        element.setAttribute(attrName.getName(), "value", ns);
        iterator = new JDOMAttributeIterator(parent, attrName);
        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(attrName.toString(), nodePointer.getName());
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("Elem");
        NodePointer parent = new NodePointer("node", element);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Element element = new Element("Elem");
        NodePointer parent = new NodePointer("node", element);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertFalse(iterator.setPosition(1));
    }
}
