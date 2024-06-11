import org.apache.commons.jxpath.ri.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class JDOMAttributeIteratorTest {

    @Test
    public void testEmptyAttributeIterator() {
        NodePointer parent = new JDOMAttrributePointer(null, null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithSingleAttribute() {
        Element element = new Element("root");
        Attribute attribute = new Attribute("name", "value");
        element.setAttribute(attribute);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "name"));

        assertNotNull(iterator.getNodePointer());
        Assertions.assertEquals("value", iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithMultipleAttributes() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Attribute attribute3 = new Attribute("name3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

        assertNotNull(iterator.getNodePointer());
        Assertions.assertEquals("value1", iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals("value2", iterator.next().getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals("value3", iterator.next().getNodePointer().getValue().getValue());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithPrefixNamespace() {
        Element element = new Element("root");
        String prefix = "xml";
        Attribute attribute = new Attribute("name", "value");
        element.setAttribute(attribute);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName(prefix, "name"));

        assertNotNull(iterator.getNodePointer());
        Assertions.assertEquals("value", iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());

        iterator.next();
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithWildcardNamespace() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Attribute attribute3 = new Attribute("name3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", "*"));

        assertNotNull(iterator.getNodePointer());
        Assertions.assertEquals("value1", iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals("value2", iterator.next().getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals("value3", iterator.next().getNodePointer().getValue().getValue());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithEmptyNamespace() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Attribute attribute3 = new Attribute("name3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("", ""));

        assertNotNull(iterator.getNodePointer());
        List<Attribute> attributes = element.getAttributes();
        Attribute attribute = null;
        for (int i = 0; i < attributes.size(); i++) {
            if (iterator.getNodePointer().getValue().getValue().equals(attributes.get(i).getValue())) {
                attribute = attributes.get(i);
                break;
            }
        }
        Assertions.assertNotNull(attribute);
        Assertions.assertEquals(attribute.getName(), iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals(attributes.get(iterator.indexOfNext()).getName(), iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals(attributes.get(iterator.indexOfNext()).getName(), iterator.getNodePointer().getValue().getValue());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }

    @Test
    public void testAttributeIteratorWithMixedNamespaces() {
        Element element = new Element("root");
        Attribute attribute1 = new Attribute("xml:name1", "value1");
        Attribute attribute2 = new Attribute("name2", "value2");
        Attribute attribute3 = new Attribute("xml:name3", "value3");
        element.setAttribute(attribute1);
        element.setAttribute(attribute2);
        element.setAttribute(attribute3);

        NodePointer parent = new JDOMAttrributePointer(new JDOMNodePointer(element), null);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, new QName("xml", "*"));

        assertNotNull(iterator.getNodePointer());
        Assertions.assertEquals("value1", iterator.getNodePointer().getValue().getValue());
        assertTrue(iterator.hasNext());
        Assertions.assertEquals("value3", iterator.next().getNodePointer().getValue().getValue());
        assertFalse(iterator.hasNext());
        assertFalse(iterator.hasPrevious());
        assertFalse(iterator.hasNextOrSelf());
        assertFalse(iterator.hasPreviousOrSelf());
    }
}