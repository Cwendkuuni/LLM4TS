import org.apache.commons.jxpath.ri.model.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstrucor() {
        Node node = new Element("root");
        NodePointer parent = new NodePointer(node);
        QName name = new QName("test");
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }

    @Test
    public void testNullParentNodePointer() {
        NodePointer parent = null;
        QName name = new QName("test");
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse("Expected NullPointerException", iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testNullQName() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = null;
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse("Expected NullPointerException", iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testNonElementParentNode() {
        NodePointer parent = new NodePointer(new Text("root"));
        QName name = new QName("test");
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse("Expected IllegalStateException", iterator instanceof DOMAttributeIterator);
    }

    @Test
    public void testUnknownAttributeName() {
        NodePointer parent = new NodePointer(new Element("root"));
        QName name = new QName("unknown");
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertFalse("Expected empty list", iterator.hasNext());
    }

    @Test
    public void testMultipleAttributesWithSameName() {
        Node node = new Element("root");
        NodePointer parent = new NodePointer(node);
        QName name = new QName("test");
        node.setAttributeNS("http://test.com", "test:attr1", "value1");
        node.setAttributeNS("http://test.com", "test:attr1", "value2");
        NodeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
        DOMAttributePointer attr1 = (DOMAttributePointer) iterator.getNodePointer();
        DOMAttributePointer attr2 = (DOMAttributePointer) iterator.getNodePointer();
        assertSame("Expected the same attribute", attr1.getNode(), attr2.getNode());
    }

    // ...
}