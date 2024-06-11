import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;

import static org.mockito.Mockito.*;

public class JDOMAttributeIteratorTest {
    private JDOMAttributeIterator iterator;
    private NodePointer parent;
    private QName name;

    @Before
    public void setUp() {
        parent = mock(NodePointer.class);
        Element element = mock(Element.class);
        Attribute attribute = mock(Attribute.class);
        when(parent.getNode()).thenReturn(element);
        when(attribute.getNamespace()).thenReturn(Namespace.NO_NAMESPACE);

        name = new QName("test", "http://www.example.com", "attr");

        when(element.getAttribute("attr", Namespace.NO_NAMESPACE)).thenReturn(attribute);
        when(element.getAttributes()).thenReturn(Collections.singletonList(attribute));
    }

    @Test
    public void testConstructor() {
        iterator = new JDOMAttributeIterator(parent, name);

        Assert.assertEquals(parent, iterator.parent);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(1, iterator.attributes.size());
        Assert.assertEquals(0, iterator.position);
    }

    @Test
    public void testGetNodePointer() {
        iterator = new JDOMAttributeIterator(parent, name);
        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(JDOMAttributePointer.class, nodePointer.getClass());
        Assert.assertEquals(parent, nodePointer.getParent());
        Assert.assertEquals(parent.getNode(), ((JDOMAttributePointer) nodePointer).getParentNodePointer().getNode());
        Assert.assertEquals(parent.getNode(), ((JDOMAttributePointer) nodePointer).getAttribute().getParent());
    }

    @Test
    public void testGetPosition() {
        iterator = new JDOMAttributeIterator(parent, name);
        int position = iterator.getPosition();

        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition_Valid() {
        iterator = new JDOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(1);

        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition_Invalid() {
        iterator = new JDOMAttributeIterator(parent, name);

        boolean result = iterator.setPosition(2);

        Assert.assertFalse(result);
        Assert.assertEquals(2, iterator.getPosition());
    }
}