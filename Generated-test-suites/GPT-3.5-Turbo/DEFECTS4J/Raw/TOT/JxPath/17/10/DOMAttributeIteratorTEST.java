
import static org.junit.Assert.*;

import org.apache.commons.jxpath.ri.QName;
import org.apache.commons.jxpath.ri.model.NodePointer;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.xml.sax.InputSource;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpression;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;
import java.io.StringReader;

public class DOMAttributeIteratorTest {
    private DOMAttributeIterator attributeIterator;

    @Before
    public void setUp() throws Exception {
        String xml = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" +
                "<root xmlns:foo=\"http://www.example.com/foo\">" +
                "   <element attr1=\"value1\" foo:attr2=\"value2\" attr3=\"\"/>" +
                "</root>";
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document document = builder.parse(new InputSource(new StringReader(xml)));
        NodePointer nodePointer = new DOMNodePointer(null, document);
        QName name = new QName("attr1");
        attributeIterator = new DOMAttributeIterator(nodePointer, name);
    }

    @Test
    public void getNodePointer_AttributeExists_ReturnsCorrectAttributeNodePointer() throws XPathExpressionException {
        NodePointer nodePointer = attributeIterator.getNodePointer();
        assertTrue(nodePointer instanceof DOMAttributePointer);
        assertEquals("value1", nodePointer.getStringValue());

        Attr attribute = ((DOMAttributePointer) nodePointer).getAttribute();
        assertNotNull(attribute);
        assertEquals("attr1", attribute.getName());
        assertEquals("value1", attribute.getValue());
    }

    @Test
    public void getNodePointer_AttributeDoesNotExist_ReturnsNull() throws XPathExpressionException {
        attributeIterator.setPosition(2);
        assertNull(attributeIterator.getNodePointer());
    }

    @Test
    public void getPosition_AttributeExists_ReturnsCurrentPosition() throws XPathExpressionException {
        assertEquals(0, attributeIterator.getPosition());

        attributeIterator.setPosition(3);
        assertEquals(3, attributeIterator.getPosition());
    }

    @Test
    public void setPosition_ValidPosition_SetsCurrentPosition() throws XPathExpressionException {
        assertTrue(attributeIterator.setPosition(1));
        assertEquals(1, attributeIterator.getPosition());

        assertTrue(attributeIterator.setPosition(2));
        assertEquals(2, attributeIterator.getPosition());

        assertFalse(attributeIterator.setPosition(3));
        assertEquals(0, attributeIterator.getPosition());
    }

    @Test
    public void setPosition_InvalidPosition_ReturnsFalse() throws XPathExpressionException {
        assertFalse(attributeIterator.setPosition(0));
        assertFalse(attributeIterator.setPosition(-1));
    }
}
