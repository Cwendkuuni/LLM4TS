import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.w3c.dom.Attr;
import org.w3c.dom.Comment;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class W3CDomTest {
    
    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        w3cDoc = w3CDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFromJsoupWithNullInput() {
        w3CDom.fromJsoup(null);
    }

    @Test
    public void testConvert() {
        Document out = createW3CDocument();
        w3CDom.convert(jsoupDoc, out);

        assertEquals(jsoupDoc.location(), out.getDocumentURI());

        Element rootEl = jsoupDoc.child(0);
        assertEquals(rootEl.tagName(), out.getDocumentElement().getTagName());

        Element outEl = (Element) out.getFirstChild();
        compareElements(rootEl, outEl);
    }

    @Test(expected = IllegalStateException.class)
    public void testConvertWithParserConfigurationException() throws ParserConfigurationException {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setFeature("http://apache.org/xml/features/disallow-doctype-decl", true);
        w3CDom.factory = factory;

        Document out = createW3CDocument();
        w3CDom.convert(jsoupDoc, out);
    }

    @Test
    public void testAsString() {
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html></html>";
        String actual = w3CDom.asString(w3cDoc);

        assertEquals(expected, actual);
    }

    @Test(expected = IllegalStateException.class)
    public void testAsStringWithTransformerException() throws TransformerException {
        TransformerFactory factory = TransformerFactory.newInstance();
        factory.setAttribute("http://javax.xml.XMLConstants/property/accessExternalDTD", "");
        w3CDom.factory = factory;

        w3CDom.asString(w3cDoc);
    }

    private Document createW3CDocument() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder;
        try {
            builder = factory.newDocumentBuilder();
            return builder.newDocument();
        } catch (ParserConfigurationException e) {
            throw new IllegalStateException(e);
        }
    }

    private void compareElements(Element jsoupElement, Element w3cElement) {
        assertEquals(jsoupElement.tagName(), w3cElement.getTagName());

        int jsoupChildCount = jsoupElement.children().size();
        int w3cChildCount = w3cElement.getChildNodes().getLength();
        assertEquals(jsoupChildCount, w3cChildCount);

        // Compare attributes
        for (org.jsoup.nodes.Attribute jsoupAttr : jsoupElement.attributes()) {
            String jsoupKey = jsoupAttr.getKey();
            String jsoupValue = jsoupAttr.getValue();

            Attr w3cAttr = (Attr) w3cElement.getAttributes().getNamedItem(jsoupKey);
            assertNotNull(w3cAttr);
            assertEquals(jsoupValue, w3cAttr.getValue());
        }

        // Compare child elements
        for (int i = 0; i < jsoupChildCount; i++) {
            org.jsoup.nodes.Element jsoupChild = jsoupElement.child(i);
            Element w3cChild = (Element) w3cElement.getChildNodes().item(i);
            compareElements(jsoupChild, w3cChild);
        }

        // Compare text nodes
        String jsoupText = jsoupElement.text();
        String w3cText = w3cElement.getTextContent().trim();
        assertEquals(jsoupText, w3cText);
    }
}