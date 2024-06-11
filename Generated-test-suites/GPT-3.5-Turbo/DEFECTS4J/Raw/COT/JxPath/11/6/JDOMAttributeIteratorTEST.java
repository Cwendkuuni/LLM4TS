
import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class JDOMAttributeIteratorTest {

    private NodePointer parent;
    private JDOMAttributeIterator iterator;
    private static final QName NAME = new QName("test");

    @Before
    public void setUp() {
        Element element = new Element("testElement");

        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");

        parent = new NodePointer(element);
        iterator = new JDOMAttributeIterator(parent, NAME);
    }

    @Test
    public void testGetNodePointer() {
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(2);
        Assert.assertEquals(2, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(4));
    }

    @Test
    public void testEmptyAttributes() {
        Element element = new Element("testElement");
        NodePointer parent = new NodePointer(element);
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, NAME);

        Assert.assertNull(iterator.getNodePointer());
    }
}
