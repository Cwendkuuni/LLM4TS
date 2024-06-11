import org.apache.commons.jxpath.ri.model.NodeIterator;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.apache.commons.jxpath.ri.QName;
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
    public void testJDOMAttributeIteratorConstructionWithQNameAndElement() {
        QName name = new QName("test:name");
        Element element = mock(Element.class);
        when(element.getAttribute(name.getName(), name.getNamespace())).thenReturn(new Attribute("test", "testValue"));

        NodePointer parent = mock(NodePointer.class);
        when(parent.getNode()).thenReturn(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer nodePointer = iterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(QName.createQName("test", "name", null), nodePointer.getQName());
        Assert.assertEquals("test", nodePointer.getNode().getValue());
    }

    @Test
    public void testJDOMAttributeIteratorConstructionWithQNameAndEmptyElement() {
        QName name = new QName("test:name");
        Element element = mock(Element.class);
        when(element.getAttribute(name.getName(), name.getNamespace())).thenReturn(null);

        NodePointer parent = mock(NodePointer.class);
        when(parent.getNode()).thenReturn(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test
    public void testJDOMAttributeIteratorConstructionWithQNameAndElementWithMultipleAttributes() {
        QName name = new QName("test:name");
        Element element = mock(Element.class);
        Attribute attribute1 = new Attribute("test1", "testValue1");
        Attribute attribute2 = new Attribute("test2", "testValue2");
        when(element.getAttribute(name.getName(), name.getNamespace())).thenReturn(attribute1);
        when(element.getAttributes()).thenReturn(new ArrayList() {{
            add(attribute2);
        }});

        NodePointer parent = mock(NodePointer.class);
        when(parent.getNode()).thenReturn(element);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parent, name);

        NodePointer firstNodePointer = iterator.getNodePointer();
        Assert.assertNotNull(firstNodePointer);
        Assert.assertEquals(QName.createQName("test", "name", null), firstNodePointer.getQName());
        Assert.assertEquals("test1", firstNodePointer.getNode().getValue());

        NodePointer secondNodePointer = iterator.getNodePointer();
        Assert.assertNotNull(secondNodePointer);
        Assert.assertEquals(QName.createQName("test", "name", null), secondNodePointer.getQName());
        Assert.assertEquals("test2", secondNodePointer.getNode().getValue());
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIteratorGetNodePointerWithNegativePosition() {
        NodePointer parent = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(parent.getIterator()).thenReturn(iterator);

        JDOMAttributeIterator jDOMAttributeIterator = new JDOMAttributeIterator(parent, null);
        jDOMAttributeIterator.setPosition(-1);
        jDOMAttributeIterator.getNodePointer();
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithInvalidPosition() {
        NodePointer parent = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(parent.getIterator()).thenReturn(iterator);

        List attributes = new ArrayList() {{
            add(new Attribute("test1", "testValue1"));
            add(new Attribute("test2", "testValue2"));
        }};

        JDOMAttributeIterator jDOMAttributeIterator = new JDOMAttributeIterator(parent, null, attributes);
        jDOMAttributeIterator.setPosition(3);
        NodePointer nodePointer = jDOMAttributeIterator.getNodePointer();
        Assert.assertNull(nodePointer);
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithLastPosition() {
        NodePointer parent = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(parent.getIterator()).thenReturn(iterator);

        List attributes = new ArrayList() {{
            add(new Attribute("test1", "testValue1"));
            add(new Attribute("test2", "testValue2"));
        }};

        JDOMAttributeIterator jDOMAttributeIterator = new JDOMAttributeIterator(parent, null, attributes);
        jDOMAttributeIterator.setPosition(2);
        NodePointer nodePointer = jDOMAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(QName.createQName(null, null, null), nodePointer.getQName());
        Assert.assertNull(nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithFirstPosition() {
        NodePointer parent = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(parent.getIterator()).thenReturn(iterator);

        List attributes = new ArrayList() {{
            add(new Attribute("test1", "testValue1"));
            add(new Attribute("test2", "testValue2"));
        }};

        JDOMAttributeIterator jDOMAttributeIterator = new JDOMAttributeIterator(parent, null, attributes);
        jDOMAttributeIterator.setPosition(1);
        NodePointer nodePointer = jDOMAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(QName.createQName(null, null, null), nodePointer.getQName());
        Assert.assertNull(nodePointer.getNode());
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointerWithValidPosition() {
        NodePointer parent = mock(NodePointer.class);
        NodeIterator iterator = mock(NodeIterator.class);
        when(iterator.getNodePointer()).thenReturn(mock(NodePointer.class));
        when(parent.getIterator()).thenReturn(iterator);

        List attributes = new ArrayList() {{
            add(new Attribute("test1", "testValue1"));
            add(new Attribute("test2", "testValue2"));
        }};

        JDOMAttributeIterator jDOMAttributeIterator = new JDOMAttributeIterator(parent, null, attributes);
        jDOMAttributeIterator.setPosition(1);
        NodePointer nodePointer = jDOMAttributeIterator.getNodePointer();
        Assert.assertNotNull(nodePointer);
        Assert.assertEquals(QName.createQName("test1", null, null), nodePointer.getQName());
        Assert.assertEquals("testValue1", nodePointer.getNode().getValue());
    }
}