
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.ParseSettings;
import org.jsoup.parser.XmlTreeBuilder;
import org.jsoup.parser.Token;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        ParseSettings expectedSettings = ParseSettings.preserveCase;
        ParseSettings actualSettings = xmlTreeBuilder.defaultSettings();
        Assert.assertEquals(expectedSettings, actualSettings);
    }

    @Test
    public void testParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><element>text</element></xml>";
        String baseUri = "http://example.com";
        Document expectedDocument = new Document(baseUri);
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        Document actualDocument = xmlTreeBuilder.parse(input, baseUri);
        Assert.assertEquals(expectedDocument, actualDocument);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String input = "<xml><element>text</element></xml>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        // Test the state after initialiseParse is called
        // ...
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token token = new Token(Character.valueOf('c'), "character");
        boolean expectedValue = true;
        boolean actualValue = xmlTreeBuilder.process(token);
        Assert.assertEquals(expectedValue, actualValue);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Node expectedNode = new Node();
        xmlTreeBuilder.insertNode(expectedNode);
        Node actualNode = xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(expectedNode, actualNode);
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        Element expectedElement = new Element();
        Element actualElement = xmlTreeBuilder.insert(startTag);
        Assert.assertEquals(expectedElement, actualElement);
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        Comment expectedComment = new Comment();
        xmlTreeBuilder.insert(commentToken);
        Comment actualComment = (Comment) xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(expectedComment, actualComment);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("character");
        TextNode expectedTextNode = new TextNode();
        xmlTreeBuilder.insert(characterToken);
        TextNode actualTextNode = (TextNode) xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(expectedTextNode, actualTextNode);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("doctype");
        DocumentType expectedDoctype = new DocumentType();
        xmlTreeBuilder.insert(doctypeToken);
        DocumentType actualDoctype = (DocumentType) xmlTreeBuilder.currentElement().childNodes().get(0);
        Assert.assertEquals(expectedDoctype, actualDoctype);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        // Add elements to the stack
        xmlTreeBuilder.stack.add(new Element("tag1"));
        xmlTreeBuilder.stack.add(new Element("tag2"));
        xmlTreeBuilder.stack.add(new Element("tag3"));

        xmlTreeBuilder.popStackToClose(endTag);

        // Test the state of the stack after popStackToClose is called
        // ...
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder xmlTreeBuilder = new XmlTreeBuilder();
        String inputFragment = "<xml><element>text</element></xml>";
        String baseUri = "http://example.com";
        List<Node> expectedNodes = List.of(new Element("element"));
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;
        List<Node> actualNodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        Assert.assertEquals(expectedNodes, actualNodes);
    }
}

