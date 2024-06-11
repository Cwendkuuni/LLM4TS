
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Element;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public class W3CDomTest {
    private W3CDom w3cDom;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
    }

    @Test
    public void testFromJsoup() {
        // Create a jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><body><div id='test'>Sample</div></body></html>");

        // Convert jsoup Document to W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Check that W3C Document is not null
        assertNotNull(w3cDoc);
        
        // Check that converted W3C Document contains the root element
        Element rootElement = w3cDoc.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testConvert() {
        // Create a jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><body><div id='test'>Sample</div></body></html>");

        // Create W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        org.w3c.dom.Document out = w3cDom.factory.newDocumentBuilder().newDocument();

        // Convert jsoup Document to W3C Document
        w3cDom.convert(jsoupDoc, out);

        // Check that converted W3C Document is not null
        assertNotNull(out);
        
        // Check that converted W3C Document contains the root element
        Element rootElement = out.getDocumentElement();
        assertNotNull(rootElement);
        assertEquals("html", rootElement.getTagName());
    }

    @Test
    public void testAsString() {
        // Create a jsoup Document
        Document jsoupDoc = Jsoup.parse("<html><body><div id='test'>Sample</div></body></html>");

        // Convert jsoup Document to W3C Document
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        // Serialize W3C Document to String
        String str = w3cDom.asString(w3cDoc);

        // Check that serialized string is not null or empty
        assertNotNull(str);
        assertTrue(str.length() > 0);
    }
}
