
import org.jsoup.helper.W3CDom;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Attr;
import org.w3c.dom.Node;
import org.w3c.dom.Text;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class W3CDomTest {

    private W3CDom w3cDom;
    private Document jsoupDoc;

    @Before
    public void setUp() throws ParserConfigurationException {
        w3cDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<div id=\"testDiv\">Hello, World!</div>", "http://example.com");
    }

    @Test
    public void fromJsoup_ValidJsoupDocument_ReturnsW3CDocumentWithRootElement() {
        org.w3c.dom.Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        Element rootElement = jsoupDoc.body();
        assertNotNull(w3cDoc);
        assertEquals(rootElement.tagName(), w3cDoc.getDocumentElement().getTagName());
    }

    @Test
    public void convert_ValidJsoupDocument_ConvertsToW3CDocument() throws ParserConfigurationException {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        Node rootElement = w3cDoc.getDocumentElement();
        assertEquals("http://example.com", w3cDoc.getDocumentURI());
        assertNotNull(rootElement);
        assertEquals("div", rootElement.getNodeName());

        Node childNode = rootElement.getFirstChild();
        assertNotNull(childNode);
        assertEquals("Hello, World!", ((Text) childNode).getData());
    }

    @Test
    public void asString_ValidW3CDocument_ReturnsStringRepresentation() throws ParserConfigurationException {
        org.w3c.dom.Document w3cDoc = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument();
        w3cDom.convert(jsoupDoc, w3cDoc);

        String docString = w3cDom.asString(w3cDoc);
        assertNotNull(docString);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\" standalone=\"no\"?><div id=\"testDiv\">Hello, World!</div>", docString);
    }
}
