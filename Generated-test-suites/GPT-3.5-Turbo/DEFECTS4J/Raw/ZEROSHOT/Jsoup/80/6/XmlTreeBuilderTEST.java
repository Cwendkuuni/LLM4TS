import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.StringReader;
import org.jsoup.Jsoup;
import org.junit.Before;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setup() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertEquals(settings.syntax, Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(new StringReader(input), baseUri);
        assertEquals(doc.baseUri(), baseUri);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        String input = "<xml><tag></tag></xml>";
        String baseUri = "http://example.com";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertEquals(doc.baseUri(), baseUri);
    }

    @Test
    public void testInitialiseParse() {
        StringReader input = new StringReader("<xml><tag></tag></xml>");
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        xmlTreeBuilder.initialiseParse(input, baseUri, errors, settings);
        assertEquals(xmlTreeBuilder.stack.size(), 1);
        assertEquals(xmlTreeBuilder.stack.peek(), xmlTreeBuilder.doc);
        assertEquals(xmlTreeBuilder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("tag", "tag", new Attributes());
        Element el = xmlTreeBuilder.insert(startTag);
        assertEquals(el.tagName(), "tag");
        assertEquals(el.parent(), xmlTreeBuilder.currentElement());
        assertEquals(xmlTreeBuilder.stack.size(), 2);
        assertEquals(xmlTreeBuilder.stack.get(xmlTreeBuilder.stack.size()-1), el);
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("tag");
        xmlTreeBuilder.stack.push(xmlTreeBuilder.doc.createElement("tag"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(xmlTreeBuilder.stack.size(), 0);
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new Comment("comment"));
    }

    @Test
    public void testProcessCharacter() {
        Token.Character characterToken = new Token.Character("data");
        xmlTreeBuilder.insert(characterToken);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new TextNode("data"));
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype", "public", "system");
        xmlTreeBuilder.insert(doctype);
        assertEquals(xmlTreeBuilder.currentElement().childNode(0), new DocumentType("doctype", "public", "system"));
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<tag></tag>";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, errors, settings);
        assertEquals(nodes.size(), 1);
        assertEquals(nodes.get(0), xmlTreeBuilder.doc.createElement("tag"));
    }
}