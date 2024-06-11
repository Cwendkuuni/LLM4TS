import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.parser.Parser;
import org.w3c.dom.Element;

public class W3CDomTest {
    @Test
    public void testFromJsoup() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<body><h1>Hello World!</h1></body>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<body><h1>Hello World!</h1></body>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Document out = new Document();
        w3cDom.convert(jsoupDoc, out);
        assertNotNull(out);
    }

    @Test
    public void testAsString() {
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<body><h1>Hello World!</h1></body>");
        W3CDom w3cDom = new W3CDom();
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        String xmlStr = w3cDom.asString(w3cDoc);
        assertTrue(xmlStr.contains("<h1>Hello World!</h1>"));
    }
}