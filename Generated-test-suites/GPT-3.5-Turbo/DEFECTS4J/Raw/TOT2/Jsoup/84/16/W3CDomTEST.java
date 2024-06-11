import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Test;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.ls.DOMImplementationLS;
import org.w3c.dom.ls.LSSerializer;

import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;

public class W3CDomTest {

    @Test
    public void testFromJsoup() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><body><div>Hello, World!</div></body></html>");

        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        NodeList nodeList = w3cDoc.getElementsByTagName("div");
        assertEquals("Hello, World!", nodeList.item(0).getTextContent());
    }

    @Test
    public void testConvert() {
    	W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><body><h1>Title</h1></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        w3cDom.convert(jsoupDoc, w3cDoc);
        NodeList nodeList = w3cDoc.getElementsByTagName("h1");
        assertEquals("Title", nodeList.item(0).getTextContent());
    }

    @Test
    public void testAsString() {
        W3CDom w3cDom = new W3CDom();
        org.jsoup.nodes.Document jsoupDoc = Parser.parseBodyFragment("<html><body><p>Paragraph</p></body></html>");
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);

        String result = w3cDom.asString(w3cDoc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><html><body><p>Paragraph</p></body></html>", result.trim());
    }
}