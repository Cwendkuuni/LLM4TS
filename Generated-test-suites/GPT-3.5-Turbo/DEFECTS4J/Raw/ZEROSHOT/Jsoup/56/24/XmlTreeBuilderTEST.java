import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tag;
import org.jsoup.helper.Validate;

import java.util.List;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings parseSettings = xmlTreeBuilder.defaultSettings();
        // Assert the returned ParseSettings is not null
        assertNotNull(parseSettings);
    }

    @Test
    public void testParse() {
        String input = "<xml><tag1/><tag2/></xml>";
        String baseUri = "http://example.com";
        Document document = xmlTreeBuilder.parse(input, baseUri);
        // Assert the returned document is not null
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml><tag1/><tag2/></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // Assert the stack contains the document
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.doc));
    }

    @Test
    public void testProcessWithStartTagToken() {
        Token startTagToken = new Token.StartTag("tag");
        xmlTreeBuilder.process(startTagToken);
        // Assert the stack contains the new element
        assertTrue(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
    }

    @Test
    public void testProcessWithEndTagToken() {
        Token endTagToken = new Token.EndTag("tag");
        xmlTreeBuilder.process(endTagToken);
        // Assert the stack does not contain the element
        assertFalse(xmlTreeBuilder.stack.contains(xmlTreeBuilder.currentElement()));
    }

    @Test
    public void testProcessWithCommentToken() {
        Token commentToken = new Token.Comment("comment");
        xmlTreeBuilder.process(commentToken);
        // Assert the current element contains the new comment
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.Comment);
    }

    @Test
    public void testProcessWithCharacterToken() {
        Token characterToken = new Token.Character("character");
        xmlTreeBuilder.process(characterToken);
        // Assert the current element contains the new text node
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.TextNode);
    }

    @Test
    public void testProcessWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.process(doctypeToken);
        // Assert the current element contains the new document type
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.DocumentType);
    }

    @Test
    public void testInsertNode() {
        Node node = Mockito.mock(Node.class);
        xmlTreeBuilder.insertNode(node);
        // Assert the current element contains the new node
        assertTrue(xmlTreeBuilder.currentElement().childNodes().contains(node));
    }

    @Test
    public void testInsertWithStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag");
        Element element = xmlTreeBuilder.insert(startTag);
        // Assert the element is not null
        assertNotNull(element);
    }

    @Test
    public void testInsertWithCommentToken() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        // Assert the current element contains the new comment
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.Comment);
    }

    @Test
    public void testInsertWithCharacterToken() {
        Token.Character characterToken = new Token.Character("character");
        xmlTreeBuilder.insert(characterToken);
        // Assert the current element contains the new text node
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.TextNode);
    }

    @Test
    public void testInsertWithDoctypeToken() {
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctypeToken);
        // Assert the current element contains the new document type
        assertTrue(xmlTreeBuilder.currentElement().childNodes().get(0) instanceof org.jsoup.nodes.DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Element firstElement = new Element("div");
        Element secondElement = new Element("div");
  
        xmlTreeBuilder.stack.add(firstElement);
        xmlTreeBuilder.stack.add(secondElement);

        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.popStackToClose(endTag);
        // Assert the stack does not contain the element with the given name
        assertFalse(xmlTreeBuilder.stack.contains(firstElement));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag1/><tag2/>";
        String baseUri = "http://example.com";
        ParseErrorList errors = Mockito.mock(ParseErrorList.class);
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        // Assert the returned node list is not null
        assertNotNull(nodes);
    }
}