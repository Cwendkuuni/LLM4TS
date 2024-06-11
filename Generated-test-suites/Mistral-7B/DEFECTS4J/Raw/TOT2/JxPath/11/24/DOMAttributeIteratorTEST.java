import org.junit.Test;
import org.junit.Assert.*;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributeIterator;
import org.apache.commons.jxpath.ri.model.dom.DOMAttributePointer;
import org.w3c.dom.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

public class DOMAttributeIteratorTest {

    @Test
    public void testConstructor() {
        Document document = DocumentBuilderFactory.newDocumentBuilder().newDocument();
        Element root = document.createElement("root");
        root.setAttribute("test", "testValue");
        Attr attr = root.getAttributeNode("test");
        DOMAttributeIterator iterator = new DOMAttributeIterator(new DOMAttributePointer(document, root), new QName("test", null, ""));
        List<Attr> attributes = iterator.nextN(Integer.MAX_VALUE);
        assertTrue(attributes.size() > 0);
        assertTrue(attributes.get(0) == attr);
    }
}