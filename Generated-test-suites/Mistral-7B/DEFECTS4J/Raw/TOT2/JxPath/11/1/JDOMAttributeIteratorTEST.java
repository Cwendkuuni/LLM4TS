import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.JDOMAttributePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMElement;
import org.junit.Test;
import static org.junit.Assert.*;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstruction() {
        NodePointer nodePointer = new JDOMElement(new Element("root"));
        QName qName = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, qName);
        assertNotNull(iterator);
        assertEquals(JDOMAttributeIterator.class, iterator.getClass());
        assertEquals(nodePointer, iterator.parent);
        assertNotNull(iterator.attributes);
    }
}