import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributePointer;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;

public class JDOMAttributeIteratorTest {
    @Test
    public void testJDOMAttributeIteratorConstructionWithSpecificQName() {
        Element element = new Element("root", "http://test.com");
        Namespace ns = Namespace.getNamespace("test", "http://test.com");
        element.addAttribute("test-attr", "test-value", ns);

        NodePointer parent = new NodePointer(element);
        QName name = new QName("test:test-attr");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer);
        assertEquals("test-value", attributePointer.getAttribute().getValue());
    }
}