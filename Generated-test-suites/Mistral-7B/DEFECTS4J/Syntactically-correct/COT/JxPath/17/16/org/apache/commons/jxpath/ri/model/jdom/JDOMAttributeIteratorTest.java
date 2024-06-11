import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr1"));

        Assert.assertEquals(root.getAttribute("attr1"), iterator.getNodePointer().getNode());
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        Element root = new Element("root");
        root.addAttribute("attr1", "value1");
        root.addAttribute("attr2", "value2");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        Assert.assertEquals(root.getAttribute("attr1"), iterator.getNodePointer().getNode());
        Assert.assertEquals(1, iterator.getPosition());

        Assert.assertEquals(root.getAttribute("attr2"), iterator.getNodePointer().getNode());
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyList() {
        Element root = new Element("root");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test(expected = IllegalStateException.class)
    public void testJDOMAttributeIterator_InvalidPosition() {
        Element root = new Element("root");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        iterator.setPosition(10);
    }

    @Test
    public void testJDOMAttributeIterator_Namespace() {
        Element root = new Element("root");
        Element child = new Element("child");
        child.addNamespaceDeclaration("ns", "http://test.com");
        root.addNamespaceDeclaration("ns", "http://test.com");
        root.addElement("attr1", "value1").addNamespace("ns");
        root.addElement("child").addAttribute("attr2", "value2", "ns");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("ns", "attr2"));

        Assert.assertEquals(child.getAttribute("attr2"), iterator.getNodePointer().getNode());
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        Element root = new Element("root");
        NodePointer parent = new JDOMAttributePointer(null, root);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }
}