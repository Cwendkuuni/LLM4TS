
import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.ArrayList;
import java.util.List;

public class DOMAttributeIteratorTest {

    private JXPathContext context;
    private NodePointer nodePointer;

    @Before
    public void setup() {
        Element root = DOMTestUtils.createRootElement();
        Node node = root.appendChild(root.getOwnerDocument().createElement("test"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr1"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr2"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr3"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr4"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr5"));
        node.getAttributes().setNamedItem(root.getOwnerDocument().createAttribute("attr6"));

        context = JXPathContext.newContext(root);
        nodePointer = (NodePointer) context.getPointer("/");
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertNotNull(iterator.getNodePointer());
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertFalse(iterator.setPosition(7));
    }

    @Test
    public void testHasNext() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertTrue(iterator.hasNext());
        iterator.setPosition(6);
        Assert.assertFalse(iterator.hasNext());
    }

    @Test
    public void testNext() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertNotNull(iterator.next());
    }

    @Test
    public void testAttributesCount() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        Assert.assertEquals(6, iterator.getAttributesCount());
    }

    @Test
    public void testAttributesList() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, null);
        List<Attr> attributes = new ArrayList<>();
        for (int i = 0; i < iterator.getAttributesCount(); i++) {
            attributes.add(iterator.next());
        }
        Assert.assertEquals(6, attributes.size());
    }
}
