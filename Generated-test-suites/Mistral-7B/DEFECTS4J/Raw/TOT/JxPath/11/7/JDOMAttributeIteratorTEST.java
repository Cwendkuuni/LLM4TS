import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer nodePointer = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(""));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithOneAttribute() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        String attributeValue = "value";

        NodePointer nodePointer = new JDOMPointer(new Element("root")
                .addAttribute(new Attribute(new QName(xmlns + ":test", prefix), localName, attributeValue)));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(localName, prefix));

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(localName, attributePointer.getNodeName().getName());
        Assert.assertEquals(prefix, attributePointer.getNodeName().getPrefix());
        Assert.assertEquals(attributeValue, attributePointer.getNodeValue().getValue());

        iterator.setPosition(2); // out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName1 = "test1";
        String attributeValue1 = "value1";
        String localName2 = "test2";
        String attributeValue2 = "value2";

        NodePointer nodePointer = new JDOMPointer(new Element("root")
                .addAttribute(new Attribute(new QName(xmlns + ":test1", prefix), localName1, attributeValue1))
                .addAttribute(new Attribute(new QName(xmlns + ":test2", prefix), localName2, attributeValue2)));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("*", prefix));

        NodePointer attributePointer1 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer1);
        Assert.assertEquals(localName1, attributePointer1.getNodeName().getName());
        Assert.assertEquals(prefix, attributePointer1.getNodeName().getPrefix());
        Assert.assertEquals(attributeValue1, attributePointer1.getNodeValue().getValue());

        iterator.setPosition(2);
        NodePointer attributePointer2 = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer2);
        Assert.assertEquals(localName2, attributePointer2.getNodeName().getName());
        Assert.assertEquals(prefix, attributePointer2.getNodeName().getPrefix());
        Assert.assertEquals(attributeValue2, attributePointer2.getNodeValue().getValue());

        iterator.setPosition(3); // out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNullName() {
        NodePointer nodePointer = new JDOMPointer(new Element("root"));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(null, null));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNullPrefix() {
        String localName = "test";

        NodePointer nodePointer = new JDOMPointer(new Element("root")
                .addAttribute(new Attribute(new QName(null, localName), localName, "value")));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(localName, null));

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(localName, attributePointer.getNodeName().getName());
        Assert.assertNull(attributePointer.getNodeName().getPrefix());
        Assert.assertEquals("value", attributePointer.getNodeValue().getValue());

        iterator.setPosition(2); // out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespace() {
        String localName = "test";

        NodePointer nodePointer = new JDOMPointer(new Element("root")
                .addAttribute(new Attribute(new QName(Namespace.NO_NAMESPACE, localName), localName, "value")));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(localName, ""));

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(localName, attributePointer.getNodeName().getName());
        Assert.assertEquals("", attributePointer.getNodeName().getPrefix());
        Assert.assertEquals("value", attributePointer.getNodeValue().getValue());

        iterator.setPosition(2); // out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testAttributeIteratorWithNamespaceAndPrefix() {
        String xmlns = "http://www.w3.org/2000/xmlns/";
        String prefix = "xml";
        String localName = "test";
        String attributeValue = "value";

        NodePointer nodePointer = new JDOMPointer(new Element("root")
                .addNamespaceDeclaration(xmlns))
                .addElement("child", new QName(xmlns + ":child"))
                .addAttribute(new Attribute(new QName(xmlns + ":test", prefix), localName, attributeValue));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(localName, prefix));

        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals(localName, attributePointer.getNodeName().getName());
        Assert.assertEquals(prefix, attributePointer.getNodeName().getPrefix());
        Assert.assertEquals(xmlns, attributePointer.getNodeName().getNamespaceURI());
        Assert.assertEquals(attributeValue, attributePointer.getNodeValue().getValue());

        iterator.setPosition(2); // out of bounds
        Assert.assertNull(iterator.getNodePointer());
    }
}