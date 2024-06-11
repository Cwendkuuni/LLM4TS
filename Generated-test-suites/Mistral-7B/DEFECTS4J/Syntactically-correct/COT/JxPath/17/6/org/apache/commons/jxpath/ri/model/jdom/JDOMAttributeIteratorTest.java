import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_withEmptyList() {
        NodePointer parentMock = mock(NodePointer.class);
        QName name = new QName("", "");
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testJDOMAttributeIterator_withSingleAttribute() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        QName name = new QName("", "test");

        Attribute attributeMock = mock(Attribute.class);
        when(elementMock.getAttribute("test", null)).thenReturn(attributeMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(0, iterator.getPosition());
        Assert.assertEquals(attributeMock, nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIterator_withMultipleAttributes() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        QName name = new QName("", "*");

        List<Attribute> attributesMock = new ArrayList<>();
        attributesMock.add(mock(Attribute.class));
        attributesMock.add(mock(Attribute.class));

        when(elementMock.getAttributes()).thenReturn(attributesMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        for (int i = 0; i < 2; i++) {
            NodePointer nodePointer = iterator.getNodePointer();
            Assert.assertNotNull(nodePointer);
            Assert.assertEquals(i + 1, iterator.getPosition());
        }

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertEquals(3, iterator.getPosition());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_withInvalidPosition() {
        NodePointer parentMock = mock(NodePointer.class);
        Element elementMock = mock(Element.class);
        when(parentMock.getNode()).thenReturn(elementMock);
        QName name = new QName("", "*");

        List<Attribute> attributesMock = new ArrayList<>();
        attributesMock.add(mock(Attribute.class));

        when(elementMock.getAttributes()).thenReturn(attributesMock);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        iterator.setPosition(3);
        iterator.getNodePointer();
    }
}