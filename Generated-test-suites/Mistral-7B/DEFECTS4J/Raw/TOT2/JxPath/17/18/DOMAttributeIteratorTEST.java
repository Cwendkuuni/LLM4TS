import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import static org.junit.Assert.*;

public class DOMAttributeIteratorTest {

    @Test
    public void testDOMAttributeIteratorConstructor() {
        Node node = new Element("root");
        NodePointer parent = new NodePointer(node);
        QName name = new QName("test");

        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        assertNotNull(iterator);
    }
}