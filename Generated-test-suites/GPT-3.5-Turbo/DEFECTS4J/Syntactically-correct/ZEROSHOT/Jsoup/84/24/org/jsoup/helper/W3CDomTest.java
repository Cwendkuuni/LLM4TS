import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.Jsoup;
import org.w3c.dom.Element;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;

public class W3CDomTest {

    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = Jsoup.parse("<html><head></head><body><div id=\"div1\">This is a div</div></body></html>");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        org.w3c.dom.Document w3cDoc;
        try {
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
            w3CDom.convert(jsoupDoc, w3cDoc);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }

        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());

        Element bodyElement = (Element) rootElement.getChildNodes().item(1);
        assertNotNull(bodyElement);
        assertEquals("body", bodyElement.getTagName());

        Element divElement = (Element) bodyElement.getChildNodes().item(1);
        assertNotNull(divElement);
        assertEquals("div", divElement.getTagName());
        assertEquals("div1", divElement.getAttribute("id"));

        Text textNode = (Text) divElement.getChildNodes().item(0);
        assertNotNull(textNode);
        assertEquals("This is a div", textNode.getWholeText());
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        String xmlString = w3CDom.asString(w3cDoc);
        assertNotNull(xmlString);
        assertTrue(xmlString.startsWith("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?>"));
        assertTrue(xmlString.contains("<html xmlns=\"http://www.w3.org/1999/xhtml\"><head></head><body><div id=\"div1\">This is a div</div></body></html>"));
    }
}