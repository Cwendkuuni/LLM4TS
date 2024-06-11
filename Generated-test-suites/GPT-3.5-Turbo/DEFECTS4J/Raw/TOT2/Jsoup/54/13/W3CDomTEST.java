import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.nodes.Document;
import org.w3c.dom.Element;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Element element = w3cDom.fromJsoup(jsoupDoc).getDocumentElement();
        assertNotNull(element);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertEquals(w3cDoc.getDocumentURI(), jsoupDoc.location());
    }

    @Test
    public void testW3CBuilder() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        assertNotNull(w3cDom.new W3CBuilder(w3cDoc));
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = new Document("");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, w3cDoc);
        String str = w3cDom.asString(w3cDoc);
        assertNotNull(str);
    }
}