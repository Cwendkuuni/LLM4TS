import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.util.HashMap;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private W3CDom w3cDom;
    private DocumentBuilder docBuilder;

    @Before
    public void setup() throws Exception {
        w3cDom = new W3CDom();
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        docBuilder = factory.newDocumentBuilder();
    }

    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document("");
        Document w3cDoc = docBuilder.newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(docBuilder.newDocument());
        HashMap<String, String> namespaces = new HashMap<>();
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("");
        
        w3cBuilder.head(sourceEl, 0);
        Element createdElement = w3cBuilder.dest;
        assertNotNull(createdElement);
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder w3cBuilder = new W3CDom.W3CBuilder(docBuilder.newDocument());
        org.jsoup.nodes.Element sourceEl = new org.jsoup.nodes.Element("");
        w3cBuilder.head(sourceEl, 0);
        
        w3cBuilder.tail(sourceEl, 0);
        Element parentElement = w3cBuilder.dest;
        assertNotNull(parentElement);
    }

    @Test
    public void testAsString() {
        Document w3cDoc = docBuilder.newDocument();
        String serializedDoc = w3cDom.asString(w3cDoc);
        assertNotNull(serializedDoc);
    }
}