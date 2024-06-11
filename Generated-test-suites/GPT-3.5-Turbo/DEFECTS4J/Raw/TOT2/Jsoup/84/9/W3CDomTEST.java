import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.NodeList;

import javax.xml.transform.TransformerException;
import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        assertNotNull(w3cDoc);
    }

    @Test
    public void testConvert() {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);

        NodeList elements = w3cDoc.getElementsByTagName("h1");

        assertEquals(1, elements.getLength());
    }

    @Test
    public void testAsString() throws TransformerException {
        W3CDom w3cDom = new W3CDom();
        Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Hello World</h1></body></html>");
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String result = w3cDom.asString(w3cDoc);

        assertNotNull(result);
    }
}