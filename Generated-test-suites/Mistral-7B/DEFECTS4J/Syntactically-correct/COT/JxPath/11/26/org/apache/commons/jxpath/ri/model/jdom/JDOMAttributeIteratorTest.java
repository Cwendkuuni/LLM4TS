import org.apache.commons.jxpath.ri.NodeIterator;
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

public class JDOMAttributeIteratorTest {

    @Test
    public void testJDOMAttributeIteratorConstructorWithQName() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "ns");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        Assert.assertNotNull(iterator.attributes);
        Assert.assertFalse(iterator.attributes.isEmpty());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithWildcardQName() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("*", "ns"));

        Assert.assertNotNull(iterator.attributes);
        Assert.assertFalse(iterator.attributes.isEmpty());
    }

    @Test
    public void testJDOMAttributeIteratorConstructorWithEmptyNamespace() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, new QName("name"));

        Assert.assertEquals(Collections.EMPTY_LIST, iterator.attributes);
    }

    @Test
    public void testJDOMAttributeIteratorGetNodePointer() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "ns");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        Attribute attribute1 = new Attribute("attr1", "val1", "ns");
        Attribute attribute2 = new Attribute("attr2", "val2", "ns");

        iterator.attributes.add(attribute1);
        iterator.attributes.add(attribute2);

        NodePointer expectedNodePointer1 = new JDOMAttributePointer(parentMock, attribute1);
        NodePointer expectedNodePointer2 = new JDOMAttributePointer(parentMock, attribute2);

        NodePointer nodePointer1 = iterator.getNodePointer();
        NodePointer nodePointer2 = iterator.getNodePointer();

        Assert.assertEquals(expectedNodePointer1, nodePointer1);
        Assert.assertEquals(expectedNodePointer2, nodePointer2);

        Assert.assertNull(iterator.getNodePointer());
        Assert.assertFalse(iterator.setPosition(2));
    }

    @Test(expected = IndexOutOfBoundsException.class)
    public void testJDOMAttributeIteratorSetPositionWithInvalidIndex() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "ns");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        Attribute attribute1 = new Attribute("attr1", "val1", "ns");
        Attribute attribute2 = new Attribute("attr2", "val2", "ns");

        iterator.attributes.add(attribute1);
        iterator.attributes.add(attribute2);

        iterator.setPosition(-1);
    }

    @Test
    public void testJDOMAttributeIteratorSetPositionWithValidIndex() {
        NodePointer parentMock = new NodePointer(new Element("root"), null);
        QName name = new QName("test", "ns");

        JDOMAttributeIterator iterator = new JDOMAttributeIterator(parentMock, name);

        Attribute attribute1 = new Attribute("attr1", "val1", "ns");
        Attribute attribute2 = new Attribute("attr2", "val2", "ns");

        iterator.attributes.add(attribute1);
        iterator.attributes.add(attribute2);

        iterator.setPosition(1);

        NodePointer expectedNodePointer = new JDOMAttributePointer(parentMock, attribute2);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertEquals(expectedNodePointer, nodePointer);
    }
}