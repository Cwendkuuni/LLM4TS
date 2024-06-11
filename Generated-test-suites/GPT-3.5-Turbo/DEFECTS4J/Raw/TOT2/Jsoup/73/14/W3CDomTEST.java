import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import org.w3c.dom.Text;
import org.w3c.dom.Comment;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setup() {
        w3cDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>");
    }

    @Test
    public void testFromJsoup() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("Document conversion from Jsoup to W3C failed", Document.class, w3cDoc.getClass());
    }

    @Test
    public void testConvert() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            Element rootElement = w3cDoc.getDocumentElement();
            assertEquals("Conversion of Jsoup Document to W3C Document failed", Element.class, rootElement.getClass());
            // Add additional assertions for the converted document if needed
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testAsString() {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        DocumentBuilder builder = null;
        try {
            builder = factory.newDocumentBuilder();
            org.w3c.dom.Document w3cDoc = builder.newDocument();
            w3cDom.convert(jsoupDoc, w3cDoc);
            String xmlString = w3cDom.asString(w3cDoc);
            // Add assertions to validate the generated XML string
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}