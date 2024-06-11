
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

import static org.junit.Assert.*;

public class W3CDomTest {

    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>", "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        assertEquals("Hello World", rootElement.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>", "");
        Document w3cDoc = createW3CDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("html", rootElement.getTagName());
        assertEquals("Hello World", rootElement.getChildNodes().item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        Document w3cDoc = createW3CDocument();
        w3cDoc.appendChild(w3cDoc.createElement("testElement"));
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertTrue(result.contains("<testElement/>"));
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
}