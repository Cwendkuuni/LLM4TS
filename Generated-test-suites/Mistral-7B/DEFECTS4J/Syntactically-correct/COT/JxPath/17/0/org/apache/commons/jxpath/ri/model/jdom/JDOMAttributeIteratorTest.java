import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Attribute;
import org.jdom.Element;
import org.jdom.Namespace;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIterator_whenParentIsNotNull_thenReturnsListOfAttributes() {
        // Given
        Element parentElement = mock(Element.class);
        Namespace namespace = mock(Namespace.class);
        String prefix = "prefix";
        String localName = "localName";
        Attribute attribute = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        when(parentElement.getNamespace()).thenReturn(namespace);
        when(parentElement.getAttribute(localName, namespace)).thenReturn(attribute);
        when(parentElement.getAttributes()).thenReturn(attributes);

        NodePointer parent = new JDOMAtributePointer(null, parentElement);
        QName name = new QName(prefix, localName);

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // Then
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(1, iterator.attributes.size());
    }

    @Test
    public void testJDOMAttributeIterator_whenParentIsNull_thenReturnsEmptyList() {
        // Given
        NodePointer parent = null;
        QName name = new QName("", "*");

        // When
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // Then
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.attributes.size());
    }

    @Test
    public void testJDOMAttributeIterator_whenPositionIsOutOfRange_thenReturnsNull() {
        // Given
        NodePointer parent = mock(NodePointer.class);
        Element parentElement = mock(Element.class);
        Namespace namespace = mock(Namespace.class);
        String prefix = "prefix";
        String localName = "localName";
        Attribute attribute = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute);

        when(parentElement.getNamespace()).thenReturn(namespace);
        when(parentElement.getAttribute(localName, namespace)).thenReturn(attribute);
        when(parentElement.getAttributes()).thenReturn(attributes);
        when(parent.getNode()).thenReturn(parentElement);

        QName name = new QName(prefix, localName);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // When & Then
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIterator_whenPositionIsValid_thenReturnsAttribute() {
        // Given
        NodePointer parent = mock(NodePointer.class);
        Element parentElement = mock(Element.class);
        Namespace namespace = mock(Namespace.class);
        String prefix = "prefix";
        String localName = "localName";
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);

        when(parentElement.getNamespace()).thenReturn(namespace);
        when(parentElement.getAttribute(localName, namespace)).thenReturn(attribute1);
        when(parentElement.getAttributes()).thenReturn(attributes);
        when(parent.getNode()).thenReturn(parentElement);

        QName name = new QName(prefix, localName);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // When
        NodePointer nodePointer = iterator.getNodePointer();

        // Then
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(attribute1, nodePointer.getNode());

        // When & Then
        when(parentElement.getAttribute(localName, namespace)).thenReturn(attribute2);
        NodePointer nodePointer2 = iterator.getNodePointer();
        Assert.assertEquals(attribute2, nodePointer2.getNode());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIterator_whenSettingPositionToInvalidValue_thenThrowsException() {
        // Given
        NodePointer parent = mock(NodePointer.class);
        Element parentElement = mock(Element.class);
        Namespace namespace = mock(Namespace.class);
        String prefix = "prefix";
        String localName = "localName";
        Attribute attribute1 = mock(Attribute.class);
        Attribute attribute2 = mock(Attribute.class);
        List<Attribute> attributes = new ArrayList<>();
        attributes.add(attribute1);
        attributes.add(attribute2);

        when(parentElement.getNamespace()).thenReturn(namespace);
        when(parentElement.getAttribute(localName, namespace)).thenReturn(attribute1);
        when(parentElement.getAttributes()).thenReturn(attributes);
        when(parent.getNode()).thenReturn(parentElement);

        QName name = new QName(prefix, localName);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        // When
        iterator.setPosition(-1);
    }
}