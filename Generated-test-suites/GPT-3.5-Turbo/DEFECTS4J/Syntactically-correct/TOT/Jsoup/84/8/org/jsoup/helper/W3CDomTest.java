import org.junit.Before;
import org.junit.Test;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;

import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Text;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.Comment;
import org.w3c.dom.EntityReference;
import org.w3c.dom.CDATASection;
import org.w3c.dom.DocumentType;
import org.w3c.dom.DocumentFragment;
import org.w3c.dom.Attr;
import org.w3c.dom.DOMException;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class W3CDomTest {

    private W3CDom w3cDom;
    private org.jsoup.nodes.Document jsoupDoc;
    private Document w3cDoc;

    @Before
    public void setUp() {
        w3cDom = new W3CDom();
        jsoupDoc = Parser.parseBodyFragment("<html><head><title>Test</title></head><body><h1>Hello World!</h1></body></html>");
        w3cDoc = w3cDom.fromJsoup(jsoupDoc);
    }

    @Test
    public void convert_shouldReturnNonNullW3CDocument() {
        assertNotNull(w3cDoc);
    }

    @Test
    public void convert_shouldSetDocumentURI() {
        assertEquals(jsoupDoc.location(), w3cDoc.getDocumentURI());
    }

    @Test
    public void convert_shouldCreateRootElement() {
        assertNotNull(w3cDoc.getDocumentElement());
    }

    @Test
    public void convert_shouldCreateElementWithCorrectTagName() {
        Element jsoupElement = jsoupDoc.select("h1").first();
        Element w3cElement = (Element) w3cDoc.getDocumentElement().getChildNodes().item(1);
        assertEquals(jsoupElement.tagName(), w3cElement.getTagName());
    }

    @Test
    public void convert_shouldAddTextToElement() {
        Element jsoupElement = jsoupDoc.select("h1").first();
        Element w3cElement = (Element) w3cDoc.getDocumentElement().getChildNodes().item(1);
        Text jsoupText = (Text) jsoupElement.childNodes().get(0);
        Text w3cText = (Text) w3cElement.getChildNodes().item(0);
        assertEquals(jsoupText.getWholeText(), w3cText.getWholeText());
    }

    @Test
    public void convert_shouldCreateCommentNode() {
        Element jsoupElement = jsoupDoc.select("h1").first();
        Comment jsoupComment = jsoupElement.appendComment("This is a comment");
        Comment w3cComment = (Comment) w3cElement.getChildNodes().item(1);
        assertEquals(jsoupComment.getData(), w3cComment.getData());
    }

    @Test
    public void convert_shouldAddAttributeToElement() {
        Element jsoupElement = jsoupDoc.select("h1").first();
        jsoupElement.attr("id", "heading");
        Element w3cElement = (Element) w3cDoc.getDocumentElement().getChildNodes().item(1);
        Attr attribute = w3cElement.getAttributeNode("id");
        assertNotNull(attribute);
        assertEquals("heading", attribute.getValue());
    }

    @Test
    public void asString_shouldReturnNonNullString() {
        String w3cString = w3cDom.asString(w3cDoc);
        assertNotNull(w3cString);
    }

    @Test
    public void asString_shouldReturnOriginalHtmlString() {
        String w3cString = w3cDom.asString(w3cDoc);
        assertEquals(jsoupDoc.toString(), w3cString);
    }

    @Test
    public void fromJsoup_shouldThrowExceptionForNullInput() {
        try {
            w3cDom.fromJsoup(null);
        } catch (IllegalArgumentException e) {
            assertNull(w3cDoc);
        }
    }

    @Test
    public void fromJsoup_shouldReturnNonNullW3CDocument() {
        Document w3cDoc = w3cDom.fromJsoup(jsoupDoc);
        assertNotNull(w3cDoc);
    }
}