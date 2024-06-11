import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.apache.commons.jxpath.ri.QName;
import org.junit.Assert;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    @Test
    public void testGetNodePointer() {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertFalse(iterator.setPosition(2));
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testEmptyIterator() {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        Assert.assertFalse(iterator.setPosition(1));
        Assert.assertNull(iterator.getNodePointer());
    }

    @Test(expected = NullPointerException.class)
    public void testNullParentNode() {
        DOMNodePointer parent = null;
        QName name = new QName("attribute");
        new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testNullAttribute() {
        DOMNodePointer parent = new DOMNodePointer(null, null);
        QName name = new QName("attribute");
        DOMAttributeIterator iterator = new DOMAttributeIterator(parent, name);
        NodeList nodeList = parent.getDOMNode().getChildNodes();
        List<Node> attributeList = new ArrayList<>();
        for (int i = 0; i < nodeList.getLength(); i++) {
            Node node = nodeList.item(i);
            if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
                attributeList.add(node);
            }
        }
        Assert.assertEquals(attributeList.size(), iterator.setPosition(1));
        Assert.assertEquals(attributeList.get(0), iterator.getNodePointer().getDOMNode());
    }
}



// Include the test cases here

