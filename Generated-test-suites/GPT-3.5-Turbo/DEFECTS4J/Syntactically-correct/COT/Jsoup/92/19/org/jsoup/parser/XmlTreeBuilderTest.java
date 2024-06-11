// Generated JUnit 4 test cases for XmlTreeBuilder class
import org.jsoup.Jsoup;
import org.jsoup.nodes.CDataNode;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.DocumentType;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.nodes.XmlDeclaration;
import org.jsoup.parser.Parser;
import org.jsoup.parser.XmlTreeBuilder;
import org.junit.Before;
import org.junit.Test;
import java.io.Reader;
import java.io.StringReader;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {
    private XmlTreeBuilder xmlTreeBuilder;
    private Document document;
 
    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
        document = Jsoup.parse("<root></root>");
    }
 
    @Test
    public void testDefaultSettings() {
        assertNull(xmlTreeBuilder.defaultSettings());
    }
 
    @Test
    public void testInitialiseParse() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Parser parser = new Parser(this.xmlTreeBuilder);
 
        xmlTreeBuilder.initialiseParse(input, baseUri, parser);
 
        assertEquals(doc, xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size() - 1));
    }
 
    @Test
    public void testParseWithReaderAndBaseUri() {
        Reader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
 
        Document result = xmlTreeBuilder.parse(input, baseUri);
 
        assertNotNull(result);
    }
 
    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<root></root>";
        String baseUri = "http://example.com";
 
        Document result = xmlTreeBuilder.parse(input, baseUri);
 
        assertNotNull(result);
    }
 
    @Test
    public void testProcessStartTagToken() {
        Token token = new Token.StartTag("tag");
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testProcessEndTagToken() {
        Token token = new Token.EndTag("tag");
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testProcessCommentToken() {
        Token token = new Token.Comment("comment");
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testProcessCharacterToken() {
        Token token = new Token.Character("data");
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testProcessDoctypeToken() {
        Token token = new Token.Doctype("doctype");
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testProcessEOFToken() {
        Token token = new Token.EOF();
 
        boolean result = xmlTreeBuilder.process(token);
 
        assertTrue(result);
    }
 
    @Test
    public void testInsertNode() {
        Node node = new Element("tag");
 
        xmlTreeBuilder.insertNode(node);
 
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }
 
    @Test
    public void testInsertTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
 
        Element result = xmlTreeBuilder.insert(startTag);
 
        assertEquals("tag", result.tagName());
    }
 
    @Test
    public void testInsertCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
 
        xmlTreeBuilder.insert(commentToken);
 
        assertEquals(commentToken, xmlTreeBuilder.currentElement().childNode(0));
    }
 
    @Test
    public void testInsertCharacterToken() {
        Token.Character characterToken = new Token.Character("data");
 
        xmlTreeBuilder.insert(characterToken);
 
        assertEquals(characterToken, xmlTreeBuilder.currentElement().childNode(0));
    }
 
    @Test
    public void testInsertDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
 
        xmlTreeBuilder.insert(doctypeToken);
 
        assertEquals(doctypeToken, xmlTreeBuilder.currentElement().childNode(0));
    }
 
    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("tag");
 
        xmlTreeBuilder.popStackToClose(endTag);
 
        // Verify that the element with tag name "tag" has been removed from the stack
        assertNull(xmlTreeBuilder.stack.stream().filter(element -> element.nodeName().equals("tag")).findFirst().orElse(null));
    }
 
    @Test
    public void testParseFragmentWithStringAndBaseUri() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        Parser parser = new Parser(this.xmlTreeBuilder);
 
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, baseUri, parser);
 
        assertEquals(1, result.size());
    }
 
    @Test
    public void testParseFragmentWithStringAndContextAndBaseUri() {
        String inputFragment = "<tag></tag>";
        Element context = document.createElement("root");
        String baseUri = "http://example.com";
        Parser parser = new Parser(this.xmlTreeBuilder);
 
        List<Node> result = xmlTreeBuilder.parseFragment(inputFragment, context, baseUri, parser);
 
        assertEquals(1, result.size());
    }
}