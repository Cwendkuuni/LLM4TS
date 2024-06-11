import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.Attr;

public class DOMAttributeIteratorTest {

    private Document document;
    private NodePointer nodePointer;

    @Before
    public void setUp() {
        document = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        Element rootElement = document.createElement("root");
        document.appendChild(rootElement);
        Element element = document.createElement("element");
        element.setAttribute("attr1", "value1");
        element.setAttribute("attr2", "value2");
        rootElement.appendChild(element);
        nodePointer = new DOMNodePointer(element);
    }

    @Test
    public void testConstructor() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertNotNull(iterator);
    }

    @Test
    public void testGetNodePointer() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertTrue(attributePointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPositionValid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(1);
        Assert.assertTrue(result);
        Assert.assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPositionInvalid() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        boolean result = iterator.setPosition(5);
        Assert.assertFalse(result);
        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testIterateAttributes() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("*"));
        Assert.assertTrue(iterator.setPosition(1));
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("attr1", ((Attr)attributePointer.getNode()).getName());
        Assert.assertTrue(iterator.setPosition(2));
        attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("attr2", ((Attr)attributePointer.getNode()).getName());
    }

    @Test
    public void testIterateWithSpecificAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("attr1"));
        Assert.assertTrue(iterator.setPosition(1));
        NodePointer attributePointer = iterator.getNodePointer();
        Assert.assertNotNull(attributePointer);
        Assert.assertEquals("attr1", ((Attr)attributePointer.getNode()).getName());
    }

    @Test
    public void testIterateWithNonExistingAttribute() {
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, new QName("attr3"));
        Assert.assertFalse(iterator.setPosition(1));
    }

}