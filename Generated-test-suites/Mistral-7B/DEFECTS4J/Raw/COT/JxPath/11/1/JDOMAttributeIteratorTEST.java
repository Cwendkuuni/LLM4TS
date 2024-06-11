import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    @Test
    public void testConstructorWithNamespaceAndAttributeName() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute = new Attribute("attributeName", "attributeValue", xmlNamespace);
        element.addAttribute(attribute);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "xml:attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute);
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithNamespaceAndWildcardAttributeName() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer1 = new JDOMAttributePointer(parent, attribute1);
        Assert.assertEquals(expectedNodePointer1, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        NodePointer actualNodePointer2 = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer2 = new JDOMAttributePointer(parent, attribute2);
        Assert.assertEquals(expectedNodePointer2, actualNodePointer2);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithNoNamespaceAndAttributeName() {
        Element element = new Element("root", "root");
        String lname = "attributeName";
        Attribute attribute = new Attribute(lname, "attributeValue");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", lname);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute);
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testConstructorWithNoNamespaceAndWildcardAttributeName() {
        Element element = new Element("root", "root");
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value");
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value");
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer1 = new JDOMAttributePointer(parent, attribute1);
        Assert.assertEquals(expectedNodePointer1, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        NodePointer actualNodePointer2 = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer2 = new JDOMAttributePointer(parent, attribute2);
        Assert.assertEquals(expectedNodePointer2, actualNodePointer2);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test(expected = IllegalStateException.class)
    public void testConstructorWithNullParent() {
        NodePointer parent = null;
        QName qName = new QName("", "");
        new JDOMAttributeIterator(parent, qName);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructorWithNullQName() {
        NodePointer parent = new JDOMSupportedNodePointer(new Element("root", "root"));
        new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetNodePointerWithFirstAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute = new Attribute("attributeName", "attributeValue", xmlNamespace);
        element.addAttribute(attribute);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "xml:attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute);
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());
    }

    @Test
    public void testGetNodePointerWithLastAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer2 = new JDOMAttributePointer(parent, attribute2);
        Assert.assertEquals(expectedNodePointer2, actualNodePointer);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetNodePointerWithMiddleAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        Attribute attribute3 = new Attribute("attribute3Name", "attribute3Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer1 = new JDOMAttributePointer(parent, attribute1);
        Assert.assertEquals(expectedNodePointer1, actualNodePointer);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        NodePointer actualNodePointer2 = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer2 = new JDOMAttributePointer(parent, attribute2);
        Assert.assertEquals(expectedNodePointer2, actualNodePointer2);
        Assert.assertTrue(iterator.hasNext());

        iterator.next();
        NodePointer actualNodePointer3 = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer3 = new JDOMAttributePointer(parent, attribute3);
        Assert.assertEquals(expectedNodePointer3, actualNodePointer3);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testGetPositionWithFirstAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute = new Attribute("attributeName", "attributeValue", xmlNamespace);
        element.addAttribute(attribute);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "xml:attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        int actualPosition = iterator.getPosition();
        int expectedPosition = 1;
        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testGetPositionWithLastAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.next();
        iterator.next();

        int actualPosition = iterator.getPosition();
        int expectedPosition = 3;
        Assert.assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testSetPositionWithFirstAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute = new Attribute("attributeName", "attributeValue", xmlNamespace);
        element.addAttribute(attribute);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "xml:attributeName");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.next();

        boolean actualCanMoveBack = iterator.setPosition(0);
        boolean expectedCanMoveBack = true;
        Assert.assertEquals(expectedCanMoveBack, actualCanMoveBack);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute);
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testSetPositionWithLastAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.next();
        iterator.next();

        boolean actualCanMoveForward = iterator.setPosition(2);
        boolean expectedCanMoveForward = false;
        Assert.assertEquals(expectedCanMoveForward, actualCanMoveForward);
    }

    @Test
    public void testSetPositionWithMiddleAttribute() {
        Element element = new Element("root", "root");
        String prefix = "xml";
        Namespace xmlNamespace = Namespace.XML_NAMESPACE;
        Attribute attribute1 = new Attribute("attribute1Name", "attribute1Value", xmlNamespace);
        Attribute attribute2 = new Attribute("attribute2Name", "attribute2Value", xmlNamespace);
        Attribute attribute3 = new Attribute("attribute3Name", "attribute3Value", xmlNamespace);
        element.addAttribute(attribute1);
        element.addAttribute(attribute2);
        element.addAttribute(attribute3);

        NodePointer parent = new JDOMSupportedNodePointer(element);
        QName qName = new QName("", "*");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qName);

        iterator.next();
        iterator.next();

        boolean actualCanMoveBackward = iterator.setPosition(1);
        boolean expectedCanMoveBackward = true;
        Assert.assertEquals(expectedCanMoveBackward, actualCanMoveBackward);

        NodePointer actualNodePointer = iterator.getNodePointer();
        JDOMAttributePointer expectedNodePointer = new JDOMAttributePointer(parent, attribute2);
        Assert.assertEquals(expectedNodePointer, actualNodePointer);
    }
}