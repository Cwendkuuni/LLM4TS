import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.*;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import static org.junit.Assert.*;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.StringWriter;
import java.util.HashMap;

public class W3CDomTest {
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private DocumentBuilderFactory factory;
    private DocumentBuilder docBuilder;
    private Document w3cDoc;

    @Before
    public void setUp() throws Exception {
        w3cDom = new W3CDom();
        jsoupDoc = new org.jsoup.nodes.Document("");
        factory = DocumentBuilderFactory.newInstance();
        docBuilder = factory.newDocumentBuilder();
        w3cDoc = docBuilder.newDocument();
    }

    @Test
    public void testFromJsoup() {
        Document result = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(result);
    }

    @Test
    public void testConvert() {
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals("", w3cDoc.getDocumentURI());
        assertNotNull(w3cDoc.getChildNodes());
    }

    @Test
    public void testAsString() {
        String result = w3cDom.asString(w3cDoc);
        assertNotNull(result);
        assertEquals("", result);
    }
}