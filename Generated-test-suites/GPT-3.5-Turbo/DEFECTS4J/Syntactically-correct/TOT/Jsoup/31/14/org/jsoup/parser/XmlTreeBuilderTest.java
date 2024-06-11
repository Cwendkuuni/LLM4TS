import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.StringUtil;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.DataNode;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Nodes;

import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private ParseErrorList errors;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        errors = new ParseErrorList();
    }

    @Test
    public void initialiseParse_ValidInput_Success() {
        String input = "<root></root>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
    }

    @Test
    public void process_StartTag_Success() {
        String input = "<root><child1/><child2></child2></root>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        StartTag startTag = new StartTag("child1", StringUtil.EMPTY, StringUtil.EMPTY);
        assertTrue(xmlTreeBuilder.process(startTag));

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());

        startTag = new StartTag("child2", StringUtil.EMPTY, StringUtil.EMPTY);
        assertTrue(xmlTreeBuilder.process(startTag));

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
        assertEquals("child2", document.childNode(0).childNode(1).nodeName());
    }

    @Test
    public void process_EndTag_Success() {
        String input = "<root><child2></child2><child1/></root>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        StartTag startTag = new StartTag("child1", StringUtil.EMPTY, StringUtil.EMPTY);
        assertTrue(xmlTreeBuilder.process(startTag));

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());

        StartTag startTag2 = new StartTag("child2", StringUtil.EMPTY, StringUtil.EMPTY);
        assertTrue(xmlTreeBuilder.process(startTag2));

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
        assertEquals("child2", document.childNode(0).childNode(1).nodeName());

        org.jsoup.parser.Token.EndTag endTag = new org.jsoup.parser.Token.EndTag("child2");
        assertTrue(xmlTreeBuilder.process(endTag));

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
        assertEquals(2, document.childNode(0).childNodes().size());
        assertFalse(document.childNode(0).childNode(1).nodeName().equals("child2"));
    }

    @Test
    public void process_Comment_Success() {
        String input = "<root><!-- Comment1 --></root>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        Comment commentToken = new Comment(" Comment1 ");
        assertTrue(xmlTreeBuilder.process(commentToken));

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("<!-- Comment1 -->", document.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void process_Character_Success() {
        String input = "<root>Text</root>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        org.jsoup.parser.Token.Character characterToken = new org.jsoup.parser.Token.Character("Text");
        assertTrue(xmlTreeBuilder.process(characterToken));

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("Text", document.childNode(0).childNode(0).toString());
    }

    @Test
    public void process_Doctype_Success() {
        String input = "<!DOCTYPE html>";
        String baseUri = "http://www.example.com/";
        xmlTreeBuilder.initialiseParse(input, baseUri, errors);

        org.jsoup.parser.Token.Doctype doctypeToken = new org.jsoup.parser.Token.Doctype("html");
        assertTrue(xmlTreeBuilder.process(doctypeToken));

        Document document = xmlTreeBuilder.getDocument();
        assertNotNull(document);
        assertEquals("html", document.childNode(0).nodeName());
        assertEquals("<!DOCTYPE html>", document.childNode(0).toString());
    }

    @Test
    public void insertNode_ValidNode_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        xmlTreeBuilder.insertNode(child1);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
    }

    @Test
    public void insert_StartTag_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        StartTag startTag = new StartTag("child1", StringUtil.EMPTY, StringUtil.EMPTY);
        xmlTreeBuilder.insert(startTag);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
    }

    @Test
    public void insert_SelfClosingStartTag_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        StartTag startTag = new StartTag("selfClosing", StringUtil.EMPTY, StringUtil.EMPTY);
        startTag.setSelfClosing();
        xmlTreeBuilder.insert(startTag);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("selfClosing", document.childNode(0).childNode(0).nodeName());
        assertTrue(document.childNode(0).childNode(0) instanceof DataNode);
        assertEquals("<selfClosing />", document.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void insert_Comment_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        Comment commentToken = new Comment(" Comment1 ");
        xmlTreeBuilder.insert(commentToken);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("<!-- Comment1 -->", document.childNode(0).childNode(0).outerHtml());
    }

    @Test
    public void insert_Character_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        org.jsoup.parser.Token.Character characterToken = new org.jsoup.parser.Token.Character("Text");
        xmlTreeBuilder.insert(characterToken);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("Text", document.childNode(0).childNode(0).toString());
    }

    @Test
    public void insert_Doctype_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        org.jsoup.parser.Token.Doctype doctypeToken = new org.jsoup.parser.Token.Doctype("html");
        xmlTreeBuilder.insert(doctypeToken);

        assertNotNull(document);
        assertEquals("html", document.childNode(0).nodeName());
        assertEquals("<!DOCTYPE html>", document.childNode(0).toString());
    }

    @Test
    public void popStackToClose_ValidClosingTag_Success() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        org.jsoup.parser.Token.EndTag endTag = new org.jsoup.parser.Token.EndTag("child2");
        xmlTreeBuilder.popStackToClose(endTag);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
        assertEquals(1, document.childNode(0).childNodes().size());
    }

    @Test
    public void popStackToClose_InvalidClosingTag_Skip() {
        Document document = new Document("http://www.example.com/");
        Element root = document.createElement("root");
        document.appendChild(root);

        Element child1 = document.createElement("child1");
        Element child2 = document.createElement("child2");
        root.appendChild(child1);
        root.appendChild(child2);

        org.jsoup.parser.Token.EndTag endTag = new org.jsoup.parser.Token.EndTag("child3");
        xmlTreeBuilder.popStackToClose(endTag);

        assertNotNull(document);
        assertEquals("root", document.childNode(0).nodeName());
        assertEquals("child1", document.childNode(0).childNode(0).nodeName());
        assertEquals("child2", document.childNode(0).childNode(1).nodeName());
        assertEquals(2, document.childNode(0).childNodes().size());
    }

    @Test
    public void testCompleteUsage_Success() {
        // Add your complete test case that covers typical use cases, edge cases, and error scenarios
        // Test all methods and potential combinations
        // Make assertions to verify the correctness of XmlTreeBuilder functionalities
        // Use setup and teardown methods if necessary
    }
}