package org.apache.commons.jxpath.ri.model.jdom;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_EmptyElement() {
        Element emptyElement = new Element("emptyElement");
        NodePointer nodePointer = new NodePointer(emptyElement.getDocument(), emptyElement);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_SingleAttribute() {
        String TEST_NAMESPACE = "test";
        String TEST_PREFIX = "tp";
        String TEST_LOCAL_NAME = "testAttribute";

        Element element = new Element("testElement");
        Namespace testNamespace = new Namespace(TEST_PREFIX, TEST_NAMESPACE);
        Attribute attribute = new Attribute("testAttribute", "testValue", testNamespace);
        element.addContent(attribute);

        NodePointer nodePointer = new NodePointer(element.getDocument(), element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName(TEST_NAMESPACE, TEST_LOCAL_NAME));

        NodePointer attributePointer = iterator.getNodePointer();
        JDOMAttributePointer actualAttributePointer = (JDOMAttributePointer) attributePointer;
        Attribute expectedAttribute = attribute;

        assertEquals(attributePointer.getNode().getClass(), Attribute.class);
        assertSame(attribute, expectedAttribute);
        assertEquals(actualAttributePointer.getAttributeName().getLocalPart(), TEST_LOCAL_NAME);
        assertSame(actualAttributePointer.getNodePointer().getNode(), element);
    }

    @Test
    public void testJDOMAttributeIterator_MultipleAttributes() {
        String TEST_NAMESPACE = "test";
        String TEST_PREFIX = "tp";

        String TEST_LOCAL_NAME_1 = "testAttribute1";
        String TEST_VALUE_1 = "testValue1";

        String TEST_LOCAL_NAME_2 = "testAttribute2";
        String TEST_VALUE_2 = "testValue2";

        Element element = new Element("testElement");
        Namespace testNamespace = new Namespace(TEST_PREFIX, TEST_NAMESPACE);

        Attribute attribute1 = new Attribute(TEST_LOCAL_NAME_1, TEST_VALUE_1, testNamespace);
        Attribute attribute2 = new Attribute(TEST_LOCAL_NAME_2, TEST_VALUE_2, testNamespace);
        element.addContent(attribute1);
        element.addContent(attribute2);

        NodePointer nodePointer = new NodePointer(element.getDocument(), element);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, null);

        List<NodePointer> attributePointers = new ArrayList<>();

        for (int i = 0; i < iterator.getSize(); i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            JDOMAttributePointer actualAttributePointer = (JDOMAttributePointer) attributePointer;
            Attribute expectedAttribute = null;

            if (i == 0) {
                expectedAttribute = attribute1;
            } else {
                expectedAttribute = attribute2;
            }

            assertEquals(attributePointer.getNode().getClass(), Attribute.class);
            assertSame(expectedAttribute, expectedAttribute);
            assertSame(actualAttributePointer.getAttributeName().getLocalPart(), actualAttributePointer.getNodePointer().getName().getLocalPart());
            assertSame(actualAttributePointer.getNodePointer().getNode(), element);

            attributePointers.add(attributePointer);
            iterator.next();
        }

        assertEquals(iterator.getSize(), 2);
        for (NodePointer nodePointer : attributePointers) {
            iterator.remove();
        }
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_NullElement() {
        NodePointer nodePointer = null;
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, null);
        assertNull(iterator.getNodePointer());
    }
}