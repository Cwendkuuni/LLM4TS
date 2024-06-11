import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorSingleNode() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.removeAttribute("attr2");

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("attr1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(JDOMAttributePointer.class, attributePointer.getClass());

        Attribute attribute = (Attribute) attributePointer.getNode();
        Assert.assertEquals("attr1", attribute.getName());
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testJDOMAttributeIteratorMultipleAttributes() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer1 = iterator.getNodePointer();
        NodePointer attributePointer2 = iterator.getNodePointer();
        NodePointer attributePointer3 = iterator.getNodePointer();
        NodePointer attributePointer4 = iterator.getNodePointer();

        Assert.assertNotNull(attributePointer1);
        Assert.assertNotNull(attributePointer2);
        Assert.assertNotNull(attributePointer3);
        Assert.assertNull(attributePointer4);

        Assert.assertEquals(JDOMAttributePointer.class, attributePointer1.getClass());
        Assert.assertEquals(JDOMAttributePointer.class, attributePointer2.getClass());
        Assert.assertEquals(JDOMAttributePointer.class, attributePointer3.getClass());

        Attribute attribute1 = (Attribute) attributePointer1.getNode();
        Attribute attribute2 = (Attribute) attributePointer2.getNode();
        Attribute attribute3 = (Attribute) attributePointer3.getNode();

        Assert.assertEquals("attr1", attribute1.getName());
        Assert.assertEquals("value1", attribute1.getValue());
        Assert.assertEquals("attr2", attribute2.getName());
        Assert.assertEquals("value2", attribute2.getValue());
        Assert.assertEquals("attr3", attribute3.getName());
        Assert.assertEquals("value3", attribute3.getValue());
    }

    @Test
    public void testJDOMAttributeIteratorInvalidNamespace() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("invalidPrefix:attr1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNull(attributePointer);
    }

    @Test
    public void testJDOMAttributeIteratorEmptyParentElement() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("attr1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNull(attributePointer);
    }

    @Test
    public void testJDOMAttributeIteratorEmptyAttributeName() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNull(attributePointer);
    }

    @Test
    public void testJDOMAttributeIteratorPosition() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);

        Assert.assertEquals(0, iterator.getPosition());

        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertEquals(1, iterator.getPosition());

        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertEquals(2, iterator.getPosition());

        Assert.assertTrue(iterator.setPosition(3));
        Assert.assertEquals(3, iterator.getPosition());

        Assert.assertFalse(iterator.setPosition(4));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIteratorEmptyAttributes() {
        Element element = new Element("test", Namespace.NO_NAMESPACE);

        NodePointer parentPointer = new JDOMElementPointer(element);
        QName attributeName = new QName("*");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNull(attributePointer);
    }

    @Test
    public void testJDOMAttributeIteratorNullParent() {
        NodePointer parentPointer = null;
        QName attributeName = new QName("attr1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentPointer, attributeName);
        NodePointer attributePointer = iterator.getNodePointer();

        Assert.assertNull(attributePointer);
    }

}