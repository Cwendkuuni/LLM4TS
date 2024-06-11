import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.assertNotNull;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructor() {
        Element element = new Element("root");
        Namespace ns = Namespace.getNamespace("prefix", "http://example.com");
        element.addAttribute("attribute1", "value1", ns);

        NodePointer parent = new NodePointer(element);
        QName qname = new QName("prefix:attribute1");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, qname);
        JDOMAttributePointer attributePointer = (JDOMAttributePointer) iterator.getNodePointer();
        assertNotNull(attributePointer.getAttribute());
    }
}