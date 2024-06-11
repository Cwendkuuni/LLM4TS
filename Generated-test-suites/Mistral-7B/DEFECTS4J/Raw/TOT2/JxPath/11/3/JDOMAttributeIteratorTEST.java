import org.apache.commons.jxpath.ri.NodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Test;

public class JDOMAttributeIteratorTest {
    @Test
    public void testConstructor() {
        NodePointer nodePointer = new NodePointer(new Element("root"));
        QName name = new QName("test:attribute");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(nodePointer, name);
        Assert.assertNotNull(iterator);
    }
}