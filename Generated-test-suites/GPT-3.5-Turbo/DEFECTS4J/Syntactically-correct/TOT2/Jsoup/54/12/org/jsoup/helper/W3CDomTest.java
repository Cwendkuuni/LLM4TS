import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.NodeList;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {
    
    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = new Document("");
    }

    @Test
    public void testFromJsoup() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertEquals("Document should not be null", w3cDoc, w3cDom.fromJsoup(jsoupDoc));
    }

    @Test
    public void testConvert() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        NodeList nodeList = w3cDoc.getChildNodes();
        assertEquals("Converted document should have child nodes", 1, nodeList.getLength());
    }

    @Test
    public void testAsString() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertEquals("String representation of document should not be empty", false, xmlString.isEmpty());
    }
}