
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator iterator;
    private NodePointer parent;
    private List attributes;
    private int position;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        parent = (Element)Mockito.mock(Element.class);
        attributes = new ArrayList<>();
        position = 0;
        iterator = new DOMAttributeIterator(parent, new QName());
    }

    @Test
    public void testGetNodePointer() {
        iterator.attributes = attributes;
        iterator.position = position;

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNull(nodePointer);

        attributes.add(Mockito.mock(Attr.class));
        attributes.add(Mockito.mock(Attr.class));
        iterator.position = 1;
        nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
    }

    @Test
    public void testGetPosition() {
        int position = iterator.getPosition();
        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition() {
        iterator.attributes = attributes;
        iterator.setPosition(0);
        Assert.assertFalse(iterator.setPosition(0));

        attributes.add(Mockito.mock(Attr.class));
        iterator.attributes = attributes;
        iterator.setPosition(1);
        Assert.assertTrue(iterator.setPosition(1));
    }
}
