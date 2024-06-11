import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer parent = new JDOMNodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testAttributeIteratorWithOneAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("attr", "value");
        element.addAttribute(attribute);

        NodePointer parent = new JDOMNodePointer(new JDOMNodeWrapper(element));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr"));
        NodePointer attributePointer = iterator.getNodePointer();
        Assertions.assertNotNull(attributePointer);
        Assertions.assertEquals(1, iterator.getPosition());
        Assertions.assertEquals(attribute, attributePointer.getNode().getParentElement().getAttribute(attribute.getName()));
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3");
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        NodePointer parent = new JDOMNodePointer(new JDOMNodeWrapper(element));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

        List<NodePointer> attributePointers = new ArrayList<>();
        for (int i = 0; i < 3; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            Assertions.assertNotNull(attributePointer);
            attributePointers.add(attributePointer);
            iterator.next();
        }

        Assertions.assertEquals(3, iterator.getPosition());
        Assertions.assertEquals(3, attributePointers.size());

        for (int i = 0; i < 3; i++) {
            NodePointer attributePointer = attributePointers.get(i);
            Assertions.assertEquals(i + 1, attributePointer.getNode().getAttributeValue(attributePointer.getNode().getName()));
        }
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespace() {
        Element rootElement = new Element("root");
        Element childElement = new Element("child");
        rootElement.addContent(childElement);

        Attribute attr1 = new Attribute("xml:id", "id1");
        attr1.setNamespace(Namespace.XML_NAMESPACE);
        childElement.addAttribute(attr1);

        Attribute attr2 = new Attribute("attr", "value");
        childElement.addAttribute(attr2);

        NodePointer parent = new JDOMNodePointer(new JDOMNodeWrapper(rootElement));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "xml:id"));

        NodePointer idAttributePointer = iterator.getNodePointer();
        Assertions.assertNotNull(idAttributePointer);
        Assertions.assertEquals(1, iterator.getPosition());
        Assertions.assertEquals(attr1, idAttributePointer.getNode());
    }

    @Test
    public void testAttributeIteratorWithNameFilter() {
        Element element = new Element("root");
        Attribute attr1 = new Attribute("attr1", "value1");
        Attribute attr2 = new Attribute("attr2", "value2");
        Attribute attr3 = new Attribute("attr3", "value3");
        element.addAttribute(attr1);
        element.addAttribute(attr2);
        element.addAttribute(attr3);

        NodePointer parent = new JDOMNodePointer(new JDOMNodeWrapper(element));
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "attr"));

        NodePointer attributePointer = iterator.getNodePointer();
        Assertions.assertNotNull(attributePointer);
        Assertions.assertEquals("attr2", attributePointer.getNode().getName());
        Assertions.assertEquals("value2", attributePointer.getNode().getValue());
        iterator.next();
        Assertions.assertEquals("attr3", iterator.getNodePointer().getNode().getName());
        Assertions.assertEquals("value3", iterator.getNodePointer().getNode().getValue());
    }

    @Test
    public void testAttributeIteratorWithNullParent() {
        NodePointer parent = null;
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(-1, iterator.getPosition());
    }

    @Test
    public void testAttributeIteratorWithNullName() {
        NodePointer parent = new JDOMNodePointer(null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, null);
        Assertions.assertNull(iterator.getNodePointer());
        Assertions.assertEquals(-1, iterator.getPosition());
    }
}