import static org.junit.Assert.*;

import org.junit.Test;
import org.w3c.dom.Document;
import org.jsoup.nodes.DocumentBuilder;
import org.jsoup.nodes.Element;
import org.jsoup.helper.W3CDom;
import org.jsoup.helper.Validate;

public class W3CDomTest {

    W3CDom w3cDom = new W3CDom();
    org.jsoup.nodes.Document jsoupDoc = new org.jsoup.nodes.Document(new Element("html"));
    Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

    @Test
    public void testFromJsoup() {
        assertNotNull(w3cDoc);
        assertTrue(w3cDoc instanceof Document);
    }

    @Test
    public void testConvert() {
        Document out = w3cDom.fromJsoup(jsoupDoc);
        w3cDom.convert(jsoupDoc, out);
        assertNotNull(out.getDocumentURI());
    }

    @Test
    public void testW3CBuilderHead() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        Element source = new Element("test");
        builder.head(source, 0);
        assertNotNull(builder.dest);
    }

    @Test
    public void testW3CBuilderTail() {
        W3CDom.W3CBuilder builder = new W3CDom.W3CBuilder(w3cDoc);
        Element source = new Element("test");
        builder.head(source, 0);
        builder.tail(source, 0);
        assertNull(builder.dest);
    }

    @Test
    public void testAsString() {
        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
        assertFalse(w3cString.isEmpty());
    }
}