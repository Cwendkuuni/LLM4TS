
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.w3c.dom.*;

import java.util.HashMap;
import java.util.Map;

import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    private DOMAttributeIterator attributeIterator;

    private NodePointer parent;
    private QName name;
    private Node node;
    private NodeList nodeList;
    private NamedNodeMap namedNodeMap;
    private Attr attr1;
    private Attr attr2;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        name = Mockito.mock(QName.class);
        node = Mockito.mock(Node.class);
        nodeList = Mockito.mock(NodeList.class);
        namedNodeMap = Mockito.mock(NamedNodeMap.class);
        attr1 = Mockito.mock(Attr.class);
        attr2 = Mockito.mock(Attr.class);
        attributeIterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor_ElementNode_NameNotWildcard() {
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("attribute1");
        when(name.getPrefix()).thenReturn(null);

        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getLength()).thenReturn(2);

        when(namedNodeMap.item(0)).thenReturn(attr1);
        when(namedNodeMap.item(1)).thenReturn(attr2);

        when(attr1.getLocalName()).thenReturn("attr1");
        when(attr2.getLocalName()).thenReturn("attr2");

        when(attr1.getPrefix()).thenReturn(null);
        when(attr2.getPrefix()).thenReturn(null);

        when(parent.getNamespaceURI(null)).thenReturn(null);
        when(parent.getNamespaceURI(null)).thenReturn(null);

        when(attr1.getNamespaceURI()).thenReturn(null);
        when(attr2.getNamespaceURI()).thenReturn(null);

        when(attr1.getName()).thenReturn("attr1");
        when(attr2.getName()).thenReturn("attr2");
        when(attr1.getNodeType()).thenReturn(Node.ATTRIBUTE_NODE);
        when(attr2.getNodeType()).thenReturn(Node.ATTRIBUTE_NODE);

        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(1));

        Assert.assertEquals(DOMAttributePointer.class, attributeIterator.getNodePointer().getClass());
        Assert.assertEquals(attr1, ((DOMAttributePointer)attributeIterator.getNodePointer()).getAttr());

        Assert.assertEquals(2, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(2));

        Assert.assertEquals(DOMAttributePointer.class, attributeIterator.getNodePointer().getClass());
        Assert.assertEquals(attr2, ((DOMAttributePointer)attributeIterator.getNodePointer()).getAttr());

        Assert.assertFalse(attributeIterator.setPosition(3));
        Assert.assertEquals(null, attributeIterator.getNodePointer());
    }

    @Test
    public void testConstructor_ElementNode_NameWildcard() {
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(name.getName()).thenReturn("*");

        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getLength()).thenReturn(2);

        when(namedNodeMap.item(0)).thenReturn(attr1);
        when(namedNodeMap.item(1)).thenReturn(attr2);

        when(attr1.getLocalName()).thenReturn("attr1");
        when(attr2.getLocalName()).thenReturn("attr2");

        when(attr1.getPrefix()).thenReturn(null);
        when(attr2.getPrefix()).thenReturn(null);

        when(parent.getNamespaceURI(null)).thenReturn(null);
        when(parent.getNamespaceURI(null)).thenReturn(null);

        when(attr1.getNamespaceURI()).thenReturn(null);
        when(attr2.getNamespaceURI()).thenReturn(null);

        when(attr1.getName()).thenReturn("attr1");
        when(attr2.getName()).thenReturn("attr2");
        when(attr1.getNodeType()).thenReturn(Node.ATTRIBUTE_NODE);
        when(attr2.getNodeType()).thenReturn(Node.ATTRIBUTE_NODE);

        Assert.assertEquals(1, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(1));

        Assert.assertEquals(DOMAttributePointer.class, attributeIterator.getNodePointer().getClass());
        Assert.assertEquals(attr1, ((DOMAttributePointer)attributeIterator.getNodePointer()).getAttr());

        Assert.assertEquals(2, attributeIterator.getPosition());
        Assert.assertTrue(attributeIterator.setPosition(2));

        Assert.assertEquals(DOMAttributePointer.class, attributeIterator.getNodePointer().getClass());
        Assert.assertEquals(attr2, ((DOMAttributePointer)attributeIterator.getNodePointer()).getAttr());

        Assert.assertFalse(attributeIterator.setPosition(3));
        Assert.assertEquals(null, attributeIterator.getNodePointer());
    }

    @Test
    public void testConstructor_OtherNodeTypes() {
        when(parent.getNode()).thenReturn(node);
        when(node.getNodeType()).thenReturn(Node.COMMENT_NODE);
        when(name.getName()).thenReturn("*");

        Assert.assertEquals(null, attributeIterator.getNodePointer());
        Assert.assertEquals(0, attributeIterator.getPosition());
        Assert.assertFalse(attributeIterator.setPosition(1));
    }

    @Test
    public void testTestAttr_NodeWithNamespace() {
        when(attr1.getPrefix()).thenReturn("prefix1");
        when(name.getName()).thenReturn("attribute1");
        when(name.getPrefix()).thenReturn("prefix1");
        when(parent.getNamespaceURI("prefix1")).thenReturn("ns1");
        when(attr1.getNamespaceURI()).thenReturn("ns1");
        when(attr1.getLocalName()).thenReturn("attr1");

        Assert.assertTrue(attributeIterator.testAttr(attr1));
    }

    @Test
    public void testTestAttr_DefaultNamespace() {
        when(attr1.getLocalName()).thenReturn("attr1");
        when(name.getName()).thenReturn("attribute1");
        when(name.getPrefix()).thenReturn(null);
        when(parent.getNamespaceURI(null)).thenReturn("ns1");
        when(attr1.getPrefix()).thenReturn(null);
        when(attr1.getNamespaceURI()).thenReturn(null);

        Assert.assertTrue(attributeIterator.testAttr(attr1));
    }

    @Test
    public void testSetPosition_InvalidPosition() {
        Assert.assertFalse(attributeIterator.setPosition(0));
    }

    @Test
    public void testSetPosition_ValidPosition() {
        Assert.assertTrue(attributeIterator.setPosition(1));
        Assert.assertEquals(1, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_OutOfBounds() {
        when(node.getAttributes()).thenReturn(namedNodeMap);
        when(namedNodeMap.getLength()).thenReturn(2);
        Assert.assertTrue(attributeIterator.setPosition(10)); // out of bounds

        Assert.assertEquals(0, attributeIterator.getPosition());
        Assert.assertEquals(null, attributeIterator.getNodePointer());
    }
}
