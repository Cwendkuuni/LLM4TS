import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom2.Attribute;
import org.jdom2.Element;
import org.jdom2.Namespace;
import org.junit.Test;
import org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithQNameAndNamespace() {
        Element element = new Element("root");
        Namespace namespace = new Namespace("prefix", "http://example.com");
        element.addNamespaceDeclaration(namespace);
        element.addAttribute(new Attribute("name1", "value1", namespace));
        element.addAttribute(new Attribute("name2", "value2", namespace));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute expectedAttribute = element.getAttribute("name1", namespace);
        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, expectedAttribute);

        NodePointer actualNodePointer = iterator.getNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithQNameAndNoNamespace() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        Attribute expectedAttribute = element.getAttribute("name1");
        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, expectedAttribute);

        NodePointer actualNodePointer = iterator.getNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithNullQName() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        List attributes = element.getAttributes();
        Attribute expectedAttribute = attributes.get(0);

        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, expectedAttribute);

        NodePointer actualNodePointer = iterator.getNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJDOMAttributeIteratorConstructorWithInvalidParent() {
        Object invalidParent = new Object();
        QName name = new QName("prefix:name1");
        new JDOMAttributeIterator(new NodePointer(invalidParent), name);
    }

    @Test(expected = NullPointerException.class)
    public void testJDOMAttributeIteratorConstructorWithNullName() {
        Element parentElement = new Element("root");
        NodePointer parent = new NodePointer(parentElement);
        new JDOMAttributeIterator(parent, null);
    }

    @Test
    public void testGetPosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        int expectedPosition = 1;
        int actualPosition = iterator.getPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testSetPositionToValidPosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);
        int expectedPosition = 1;
        int actualPosition = iterator.getPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    public void testSetPositionToFirstPosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(0);
        int expectedPosition = 0;
        int actualPosition = iterator.getPosition();
        assertEquals(expectedPosition, actualPosition);
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testSetPositionToInvalidPosition() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(3);
    }

    @Test(expected = IllegalStateException.class)
    public void testSetPositionAfterLastAttribute() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(3);
    }

    @Test
    public void testGetNodePointer() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(1);

        Attribute expectedAttribute = element.getAttribute("name2");
        NodePointer expectedNodePointer = new JDOMAttributePointer(parent, expectedAttribute);

        NodePointer actualNodePointer = iterator.getNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }

    @Test
    public void testGetNodePointerAfterLastAttribute() {
        Element element = new Element("root");
        element.addAttribute(new Attribute("name1", "value1"));
        element.addAttribute(new Attribute("name2", "value2"));

        NodePointer parent = new NodePointer(element);
        QName name = new QName("prefix:name1");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        iterator.setPosition(2);

        Attribute expectedAttribute = null;
        NodePointer expectedNodePointer = null;

        NodePointer actualNodePointer = iterator.getNodePointer();
        assertEquals(expectedNodePointer, actualNodePointer);
    }
}