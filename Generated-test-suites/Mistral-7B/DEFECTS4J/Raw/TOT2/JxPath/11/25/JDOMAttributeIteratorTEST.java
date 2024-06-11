import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.jdom.Element;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

public class JDOMAttributeIteratorTest_Constructor {
    @Test
    public void testConstructor() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        QName name = new QName("test");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
        assertNotNull(iterator);
    }
}