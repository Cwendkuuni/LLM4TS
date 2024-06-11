import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Before;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Attr;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

public class DOMAttributeIteratorTest {

    private NodePointer parent;
    private QName name;
    private DOMAttributeIterator iterator;

    @Before
    public void setUp() {
        try {
            DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
            Document document = dBuilder.newDocument();

            Element rootElement = document.createElement("root");
            document.appendChild(rootElement);

            Attr attr1 = document.createAttribute("attr1");
            attr1.setValue("value1");
            rootElement.setAttributeNode(attr1);

            Attr attr2 = document.createAttribute("attr2");
            attr2.setValue("value2");
            rootElement.setAttributeNode(attr2);

            parent = new DOMNodePointer(document);
        } catch (Exception e) {
            e.printStackTrace();
        }

        name = new QName("attr1");
        iterator = new DOMAttributeIterator(parent, name);
    }

    @Test
    public void testGetNodePointer() {
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName().toString());
        assertEquals("value1", ((Attr) nodePointer.getNode()).getValue());
    }

    @Test
    public void testGetPosition() {
        assertEquals(0, iterator.getPosition());
        iterator.setPosition(1);
        assertEquals(1, iterator.getPosition());
    }

    @Test
    public void testSetPosition() {
        assertFalse(iterator.setPosition(0));
        assertTrue(iterator.setPosition(1));
        assertTrue(iterator.setPosition(2));
        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testCompleteIterator() {
        iterator.setPosition(1);
        NodePointer nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr1", nodePointer.getName().toString());
        assertEquals("value1", ((Attr) nodePointer.getNode()).getValue());

        iterator.setPosition(2);
        nodePointer = iterator.getNodePointer();
        assertNotNull(nodePointer);
        assertEquals("attr2", nodePointer.getName().toString());
        assertEquals("value2", ((Attr) nodePointer.getNode()).getValue());

        assertFalse(iterator.setPosition(3));
    }

    @Test
    public void testInvalidAttributeName() {
        name = new QName("attr3");
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

    @Test
    public void testEmptyParentNode() {
        parent = new DOMNodePointer(null);
        iterator = new DOMAttributeIterator(parent, name);
        assertNull(iterator.getNodePointer());
    }

}