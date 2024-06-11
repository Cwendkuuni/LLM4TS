import org.apache.commons.jxpath.JXPathContext;
import org.apache.commons.jxpath.Pointer;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.apache.commons.jxpath.ri.model.dom.DOMNodePointer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DOMAttributeIteratorTest {
    private Document document;
    private DOMNodePointer nodePointer;

    @Before
    public void setup() throws Exception {
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        document = dBuilder.newDocument();

        Element root = document.createElement("root");
        document.appendChild(root);

        Element element1 = document.createElement("element");
        element1.setAttribute("attr1", "value1");
        element1.setAttribute("attr2", "value2");
        root.appendChild(element1);

        Element element2 = document.createElement("element");
        element2.setAttribute("attr3", "value3");
        root.appendChild(element2);

        JXPathContext context = JXPathContext.newContext(document);
        Pointer pointer = context.getPointer("/root");
        nodePointer = new DOMNodePointer(pointer);
    }

    @Test
    public void testConstructor() {
        QName name = new QName(null, "*", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        Assert.assertEquals(nodePointer, iterator.parent);
        Assert.assertEquals(name, iterator.name);
        Assert.assertNotNull(iterator.attributes);
        Assert.assertEquals(0, iterator.position);
    }

    @Test
    public void testTestAttr() {
        QName name = new QName(null, "attr1", "attr1");

        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Node attribute = ((Attr) iterator.attributes.get(0)).getOwnerElement();

        Assert.assertEquals("value1", attribute.getAttribute("attr1"));
        Assert.assertTrue(iterator.testAttr((Attr) iterator.attributes.get(0)));
        Assert.assertFalse(iterator.testAttr((Attr) iterator.attributes.get(1)));
    }

    @Test
    public void testEqualStrings() {
        Assert.assertTrue(DOMAttributeIterator.equalStrings(null, null));
        Assert.assertFalse(DOMAttributeIterator.equalStrings(null, "test"));
        Assert.assertFalse(DOMAttributeIterator.equalStrings("test", null));
        Assert.assertTrue(DOMAttributeIterator.equalStrings("test", "test"));
        Assert.assertFalse(DOMAttributeIterator.equalStrings("test", "Test"));
    }

    @Test
    public void testGetAttribute() {
        QName name = new QName(null, "attr1", "attr1");
        Node node = document.getElementsByTagName("element").item(0);

        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);
        Attr attribute = iterator.getAttribute((Element) node, name);

        Assert.assertEquals("attr1", attribute.getName());
        Assert.assertEquals("value1", attribute.getValue());
    }

    @Test
    public void testGetNodePointer() {
        QName name = new QName(null, "*", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        NodePointer nodePointer = iterator.getNodePointer();

        Assert.assertNotNull(nodePointer);
        Assert.assertTrue(nodePointer instanceof DOMAttributePointer);
    }

    @Test
    public void testGetPosition() {
        QName name = new QName(null, "*", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        Assert.assertEquals(0, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        QName name = new QName(null, "*", "*");
        DOMAttributeIterator iterator = new DOMAttributeIterator(nodePointer, name);

        Assert.assertFalse(iterator.setPosition(0));
        Assert.assertTrue(iterator.setPosition(1));
        Assert.assertTrue(iterator.setPosition(2));
        Assert.assertFalse(iterator.setPosition(3));
    }
}