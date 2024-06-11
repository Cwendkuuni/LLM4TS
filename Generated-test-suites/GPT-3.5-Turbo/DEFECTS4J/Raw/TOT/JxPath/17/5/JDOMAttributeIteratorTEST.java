import org.apache.commons.jxpath.ri.model.jdom.JDOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.jdom.Element;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.List;

public class JDOMAttributeIteratorTest {

    @Mock
    private NodePointer mockParent;

    private Element element;
    private JDOMAttributeIterator attributeIterator;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        element = new Element("testElement");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        element.setAttribute("attr3", "value3");
        element.setAttribute("attr4", "value4");

        attributeIterator = new JDOMAttributeIterator(mockParent, new QName("attr"));
    }

    @Test
    public void testGetNodePointer_returnsFirstAttribute(){
        NodePointer nodePointer = attributeIterator.getNodePointer();

        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assert.assertEquals("attr1", ((JDOMAttributePointer) nodePointer).getAttr().getName());
    }

    @Test
    public void testGetPosition_returnsCurrentPosition(){
        int position = attributeIterator.getPosition();

        Assert.assertEquals(0, position);
    }

    @Test
    public void testSetPosition_setsCurrentPosition(){
        boolean result = attributeIterator.setPosition(2);

        Assert.assertTrue(result);
        Assert.assertEquals(2, attributeIterator.getPosition());
    }

    @Test
    public void testSetPosition_returnsFalseWhenPositionIsInvalid(){
        boolean result = attributeIterator.setPosition(5);

        Assert.assertFalse(result);
        Assert.assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void testGetNodePointer_returnsNextAttribute(){
        attributeIterator.setPosition(1);
        NodePointer nodePointer = attributeIterator.getNodePointer();

        Assert.assertTrue(nodePointer instanceof JDOMAttributePointer);
        Assert.assertEquals("attr2", ((JDOMAttributePointer) nodePointer).getAttr().getName());
    }

    @Test
    public void testGetNodePointer_returnsNullWhenNoMoreAttributes(){
        attributeIterator.setPosition(4);
        NodePointer nodePointer = attributeIterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_returnsNullWhenParentNodeIsNotElement(){
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("attr"));

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_returnsNullWhenAttributeNameDoesNotExist(){
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("nonExistentAttr"));

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_returnsNullWhenNamespaceResolverIsNull(){
        JDOMAttributeIterator iterator = new JDOMAttributeIterator(mockParent, new QName("attr"));

        // Set element without the necessary namespace
        iterator.element = new Element("testElement2");

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }

    @Test
    public void testGetNodePointer_returnsNullWhenAllAttributesHaveDifferentNamespace(){
        // Set element with different namespaces for each attribute
        element.setAttribute("attr5", "value5", org.jdom.Namespace.getNamespace("ns1", "uri1"));
        element.setAttribute("attr6", "value6", org.jdom.Namespace.getNamespace("ns2", "uri2"));

        NodePointer nodePointer = attributeIterator.getNodePointer();

        Assert.assertNull(nodePointer);
    }
}