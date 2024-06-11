import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        NodePointer parent = new NodePointer(new Element("root"), null);
        QName name = new QName("test");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        assertEquals(parent, iterator.parent);
        assertNotNull(iterator.attributes);
        assertEquals(0, iterator.position);

        parent = null;
        iterator = new JDOMAttributeIterator(parent, name);
        assertNull(iterator.parent);
        assertNull(iterator.attributes);

        parent = new NodePointer(new Element("root"), null);
        name = new QName("*");
        iterator = new JDOMAttributeIterator(parent, name);
        assertEquals(parent, iterator.parent);
        assertNotNull(iterator.attributes);
        assertEquals(0, iterator.position);
    }
}