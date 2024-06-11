import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class DOMAttributeIteratorTest {

    private NodePointer parentMock;
    private Element elementMock;
    private NamedNodeMap attributeNodeMapMock;
    private Attr attrMock;
    private DOMAttributeIterator domAttributeIterator;

    @Before
    public void setUp() {
        parentMock = mock(NodePointer.class);
        elementMock = mock(Element.class);
        attributeNodeMapMock = mock(NamedNodeMap.class);
        attrMock = mock(Attr.class);
        domAttributeIterator = new DOMAttributeIterator(parentMock, new QName("test"));
    }

    @Test
    public void testConstructor_noMatchingAttributes() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(0);

        List<Attr> attributes = getPrivateAttributeList();

        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testConstructor_singleMatchingAttributes() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(1);
        when(attributeNodeMapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getLocalName()).thenReturn("test");

        List<Attr> attributes = getPrivateAttributeList();

        Assert.assertEquals(1, attributes.size());
        Assert.assertEquals(attrMock, attributes.get(0));
    }

    @Test
    public void testConstructor_multipleMatchingAttributes() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(3);
        when(attributeNodeMapMock.item(0)).thenReturn(attrMock);
        when(attributeNodeMapMock.item(1)).thenReturn(attrMock);
        when(attributeNodeMapMock.item(2)).thenReturn(attrMock);
        when(attrMock.getLocalName()).thenReturn("test");

        List<Attr> attributes = getPrivateAttributeList();

        Assert.assertEquals(3, attributes.size());
        Assert.assertEquals(attrMock, attributes.get(0));
        Assert.assertEquals(attrMock, attributes.get(1));
        Assert.assertEquals(attrMock, attributes.get(2));
    }

    @Test
    public void testConstructor_singlePartialMatchingAttributes() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(1);
        when(attributeNodeMapMock.item(0)).thenReturn(attrMock);
        when(attrMock.getLocalName()).thenReturn("different");

        List<Attr> attributes = getPrivateAttributeList();

        Assert.assertEquals(0, attributes.size());
    }

    @Test
    public void testTestAttr_matchingAttrWithPrefix() {
        when(attrMock.getPrefix()).thenReturn("prefix");
        when(attrMock.getLocalName()).thenReturn("localName");
        when(parentMock.getNamespaceURI("prefix")).thenReturn("nsURI");
        when(parentMock.getNamespaceURI(null)).thenReturn("");

        boolean result = getValueOfPrivateTestAttr(attrMock, new QName("localName"));

        Assert.assertTrue(result);
    }

    @Test
    public void testTestAttr_nonMatchingAttrWithPrefix() {
        when(attrMock.getPrefix()).thenReturn("prefix");
        when(attrMock.getLocalName()).thenReturn("localName");
        when(parentMock.getNamespaceURI("prefix")).thenReturn("nsURI");
        when(parentMock.getNamespaceURI(null)).thenReturn("");

        boolean result = getValueOfPrivateTestAttr(attrMock, new QName("different"));

        Assert.assertFalse(result);
    }

    @Test
    public void testTestAttr_nonMatchingAttrNoPrefix() {
        when(attrMock.getPrefix()).thenReturn(null);
        when(attrMock.getLocalName()).thenReturn("localName");
        when(parentMock.getNamespaceURI(null)).thenReturn("");

        boolean result = getValueOfPrivateTestAttr(attrMock, new QName("different"));

        Assert.assertFalse(result);
    }

    @Test
    public void testSetPosition_invalidPosition() {
        boolean result = domAttributeIterator.setPosition(0);

        Assert.assertFalse(result);
    }

    @Test
    public void testSetPosition_validPosition() {
        boolean result = domAttributeIterator.setPosition(2);

        Assert.assertTrue(result);
        Assert.assertEquals(2, domAttributeIterator.getPosition());
    }

    @Test
    public void testGetPosition() {
        int position = domAttributeIterator.getPosition();
        
        Assert.assertEquals(0, position);
    }

    @Test
    public void testGetNodePointer_nullPosition() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(2);
        when(attributeNodeMapMock.item(0)).thenReturn(attrMock);
        when(attributeNodeMapMock.item(1)).thenReturn(attrMock);
        when(attrMock.getLocalName()).thenReturn("test");

        NodePointer result = domAttributeIterator.getNodePointer();

        Assert.assertNotNull(result);
        Assert.assertEquals(new DOMAttributePointer(parentMock, attrMock), result);
    }

    @Test
    public void testGetNodePointer_validPosition() {
        when(parentMock.getNode()).thenReturn(elementMock);
        when(elementMock.getNodeType()).thenReturn(Node.ELEMENT_NODE);
        when(elementMock.getAttributes()).thenReturn(attributeNodeMapMock);
        when(attributeNodeMapMock.getLength()).thenReturn(2);
        when(attributeNodeMapMock.item(0)).thenReturn(attrMock);
        when(attributeNodeMapMock.item(1)).thenReturn(attrMock);
        when(attrMock.getLocalName()).thenReturn("test");

        domAttributeIterator.setPosition(2);

        NodePointer result = domAttributeIterator.getNodePointer();

        Assert.assertNotNull(result);
        Assert.assertEquals(new DOMAttributePointer(parentMock, attrMock), result);
    }

    private List<Attr> getPrivateAttributeList() {
        try {
            java.lang.reflect.Field attributesField = DOMAttributeIterator.class.getDeclaredField("attributes");
            attributesField.setAccessible(true);
            return (List<Attr>) attributesField.get(domAttributeIterator);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean getValueOfPrivateTestAttr(Attr attr, QName testName) {
        try {
            java.lang.reflect.Method testAttrMethod = DOMAttributeIterator.class.getDeclaredMethod("testAttr", Attr.class, QName.class);
            testAttrMethod.setAccessible(true);
            return (boolean) testAttrMethod.invoke(domAttributeIterator, attr, testName);
        } catch (NoSuchMethodException | IllegalAccessException | java.lang.reflect.InvocationTargetException e) {
            e.printStackTrace();
        }
        return false;
    }

}