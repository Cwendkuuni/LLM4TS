import org.jsoup.nodes.Element;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Comment;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.util.HashMap;
import java.util.Stack;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
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
        Document w3cDoc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            w3cDoc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assertNotNull(w3cDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
    }

    @Test
    public void testCopyAttributes() {
        org.jsoup.nodes.Element source = new org.jsoup.nodes.Element("");
        Element el = new Element("");
        w3cDom.copyAttributes(source, el);
        assertEquals(el.attributes().size(), source.attributes().size());
    }

    @Test
    public void testUpdateNamespaces() {
        org.jsoup.nodes.Element el = new org.jsoup.nodes.Element("");
        String prefix = w3cDom.updateNamespaces(el);
        assertNotNull(prefix);
    }

    @Test
    public void testAsString() {
        Document doc = null;
        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            doc = builder.newDocument();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        }
        assertNotNull(doc);
        String w3cDocAsString = w3cDom.asString(doc);
        assertNotNull(w3cDocAsString);
    }
}