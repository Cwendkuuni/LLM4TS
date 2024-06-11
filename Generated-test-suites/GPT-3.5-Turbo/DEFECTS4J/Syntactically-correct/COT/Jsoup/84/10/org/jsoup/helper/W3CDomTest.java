
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><head></head><body><div>Hello World</div></body></html>", "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
        assertEquals("html", w3cDoc.getDocumentElement().getTagName());
    }
    
    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><head></head><body><div>Hello World</div></body></html>", "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(out);
        assertEquals("html", out.getDocumentElement().getTagName());
    }
    
    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><head></head><body><div>Hello World</div></body></html>", "");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlString = w3cDom.asString(w3cDoc);
        assertNotNull(xmlString);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><head></head><body><div>Hello World</div></body></html>", xmlString);
    }
}
