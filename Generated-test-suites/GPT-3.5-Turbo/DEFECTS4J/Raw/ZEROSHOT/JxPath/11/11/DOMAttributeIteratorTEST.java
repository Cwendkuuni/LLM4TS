import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        parent = Mockito.mock(NodePointer.class);
        name = Mockito.mock(QName.class);
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testConstructor() {
        Assert.assertEquals(parent, iterator.getParent());
        Assert.assertEquals(name, iterator.getName());
        Assert.assertNotNull(iterator.getAttributes());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(2));
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = Mockito.mock(NodePointer.class);
        List<Attr> attributes = new ArrayList<>();
        Attr attr1 = Mockito.mock(Attr.class);
        Attr attr2 = Mockito.mock(Attr.class);
        attributes.add(attr1);
        attributes.add(attr2);
        iterator.setAttributes(attributes);
        
        Mockito.when(attr1.getLocalName()).thenReturn("testAttr1");
        Mockito.when(attr2.getLocalName()).thenReturn("testAttr2");
        
        Mockito.when(parent.getNamespaceURI(Mockito.anyString())).thenReturn(null);
        Mockito.when(iterator.getAttributes().get(Mockito.anyInt())).thenReturn(attr1);

        NodePointer result = iterator.getNodePointer();
        Assert.assertNotNull(result);
        Assert.assertEquals(DOMAttributePointer.class, result.getClass());
        Assert.assertEquals(attr1, ((DOMAttributePointer)result).getAttr());

        iterator.setPosition(2);
        Mockito.when(iterator.getAttributes().get(Mockito.anyInt())).thenReturn(attr2);

        result = iterator.getNodePointer();
        Assert.assertNotNull(result);
        Assert.assertEquals(DOMAttributePointer.class, result.getClass());
        Assert.assertEquals(attr2, ((DOMAttributePointer)result).getAttr());

        iterator.setPosition(0);
        result = iterator.getNodePointer();
        Assert.assertNull(result);
    }

    @Test
    public void testTestAttr() {
        Attr attr = Mockito.mock(Attr.class);
        Mockito.when(parent.getNamespaceURI(Mockito.anyString())).thenReturn(null);
        Mockito.when(name.getName()).thenReturn("testAttr");

        boolean result = iterator.testAttr(attr, name);
        Assert.assertFalse(result);

        Mockito.when(attr.getLocalName()).thenReturn("testAttr");

        result = iterator.testAttr(attr, name);
        Assert.assertTrue(result);
    }

    @Test
    public void testGetAttribute() {
        Element element = Mockito.mock(Element.class);
        QName name = Mockito.mock(QName.class);
        Attr attr = Mockito.mock(Attr.class);
        List<Attr> attributes = new ArrayList<>();
        attributes.add(attr);

        iterator.setAttributes(attributes);

        Mockito.when(parent.getNamespaceURI(Mockito.anyString())).thenReturn(null);
        Mockito.when(name.getPrefix()).thenReturn("testPrefix");
        Mockito.when(name.getName()).thenReturn("testName");
        Mockito.when(element.getAttributeNode(Mockito.anyString())).thenReturn(attr);

        Attr result = iterator.getAttribute(element, name);
        Assert.assertNotNull(result);
        Assert.assertEquals(attr, result);

        attributes.clear();
        Mockito.when(name.getPrefix()).thenReturn(null);
        Mockito.when(attr.getLocalName()).thenReturn("testName");

        result = iterator.getAttribute(element, name);
        Assert.assertNotNull(result);
        Assert.assertEquals(attr, result);

        attributes.clear();
        Mockito.when(name.getPrefix()).thenReturn(null);
        Mockito.when(name.getName()).thenReturn("testName");
        Mockito.when(parent.getNamespaceURI(Mockito.anyString())).thenReturn("testNS");

        Mockito.when(element.getAttributeNodeNS(Mockito.anyString(), Mockito.anyString())).thenReturn(attr);

        result = iterator.getAttribute(element, name);
        Assert.assertNotNull(result);
        Assert.assertEquals(attr, result);

        Mockito.when(element.getAttributes()).thenReturn(Mockito.mock(NamedNodeMap.class));
        Mockito.when(element.getAttributes().getLength()).thenReturn(1);
        Mockito.when(element.getAttributes().item(Mockito.anyInt())).thenReturn(attr);

        result = iterator.getAttribute(element, name);
        Assert.assertNotNull(result);
        Assert.assertEquals(attr, result);
    }
}