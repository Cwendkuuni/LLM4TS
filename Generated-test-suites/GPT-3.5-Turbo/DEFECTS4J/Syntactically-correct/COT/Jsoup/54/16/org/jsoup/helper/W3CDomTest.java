
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<body>Hello, World!</body>");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertEquals("body", rootElement.getTagName());
        assertEquals("Hello, World!", rootElement.getTextContent());
    }

    @Test
    public void testConvert() {
        DocumentBuilder builder;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setNamespaceAware(true);
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            assertEquals("body", rootElement.getTagName());
            assertEquals("Hello, World!", rootElement.getTextContent());
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertEquals("<body>Hello, World!</body>", xmlString);
    }
}
