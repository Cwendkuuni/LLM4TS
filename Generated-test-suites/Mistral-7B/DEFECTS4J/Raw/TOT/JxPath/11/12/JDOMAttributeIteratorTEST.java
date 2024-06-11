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
    public void testAttributeIterator() {
        String xml = "<root xmlns:x='http://example.com/ns'><child attr1='value1' attr2='value2' attr3='value3'/></root>";
        Element root = JdomHelper.parseStringToElement(xml);

        NodePointer nodePointer = new NodePointer(root);
        NodeIterator iterator = new JDOMAttributeIterator(nodePointer, new QName("", "attr1"));

        List<String> expectedValues = new ArrayList<>();
        expectedValues.add("value1");

        for (int i = 0; i < 3; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            Attribute attribute = (Attribute) attributePointer.getNode();
            String value = attribute.getValue();
            Assert.assertEquals("Unexpected attribute value", expectedValues.get(i), value);
            iterator.setPosition(iterator.getPosition() + 1);
        }

        // Test end of iteration
        NodePointer lastAttributePointer = iterator.getNodePointer();
        Assert.assertNull("Last attribute should be null", lastAttributePointer.getNode());

        // Test iteration back to the beginning
        iterator.setPosition(0);
        NodePointer firstAttributePointer = iterator.getNodePointer();
        Attribute firstAttribute = (Attribute) firstAttributePointer.getNode();
        String firstAttributeValue = firstAttribute.getValue();
        Assert.assertEquals("Unexpected first attribute value", "value1", firstAttributeValue);

        // Test iteration to the non-existent attribute
        iterator.setPosition(4);
        NodePointer nonExistentAttributePointer = iterator.getNodePointer();
        Assert.assertNull("Non-existent attribute should be null", nonExistentAttributePointer.getNode());

        // Test iteration with empty namespace
        iterator = new JDOMAttributeIterator(nodePointer, new QName("", ""));
        for (int i = 0; i < 3; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            Attribute attribute = (Attribute) attributePointer.getNode();
            String value = attribute.getValue();
            if (i == 0) {
                Assert.assertEquals("Unexpected first attribute value (xml:namespace)", "", value);
            } else {
                Assert.assertEquals("Unexpected attribute value", "value" + (i + 1), value);
            }
            iterator.setPosition(iterator.getPosition() + 1);
        }

        // Test iteration with specific namespace
        iterator = new JDOMAttributeIterator(nodePointer, new QName("x", "attr2"));
        for (int i = 0; i < 1; i++) {
            NodePointer attributePointer = iterator.getNodePointer();
            Attribute attribute = (Attribute) attributePointer.getNode();
            String value = attribute.getValue();
            Assert.assertEquals("Unexpected attribute value", "value2", value);
            iterator.setPosition(iterator.getPosition() + 1);
        }
        NodePointer lastAttributePointerWithNamespace = iterator.getNodePointer();
        Assert.assertNull("Last attribute should be null", lastAttributePointerWithNamespace.getNode());
    }
}