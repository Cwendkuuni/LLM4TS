import static org.junit.Assert.*;

import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.*;

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

public class W3CDomTest {
    private W3CDom w3CDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3CDom = new W3CDom();
        jsoupDoc = new Document();
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3CDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDoc = builder.newDocument();;
        w3CDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setNamespaceAware(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        Document w3cDoc = builder.newDocument();;
        String result = w3CDom.asString(w3cDoc);
        assertNotNull(result);
    }
}