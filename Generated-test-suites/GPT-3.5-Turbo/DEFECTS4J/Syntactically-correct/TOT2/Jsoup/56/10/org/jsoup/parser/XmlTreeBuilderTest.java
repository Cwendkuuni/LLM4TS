import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @Test
    public void testDefaultSettings() {
        assertEquals(ParseSettings.preserveCase, xmlTreeBuilder.defaultSettings());
    }

    @Test
    public void testParse() {
        String input = "<xml></xml>";
        String baseUri = "";
        Document doc = xmlTreeBuilder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testInitialiseParse() {
        String input = "<xml></xml>";
        String baseUri = "";
        xmlTreeBuilder.initialiseParse(input, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(xmlTreeBuilder.currentElement());
    }

    @Test
    public void testProcess() {
        Token token = new Token(Token.TokenType.StartTag, "<body>");
        assertTrue(xmlTreeBuilder.process(token));
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("div"), "", new ParseSettings(false));
        xmlTreeBuilder.insertNode(node);
        assertEquals(node, xmlTreeBuilder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        Token.StartTag startTag = new Token.StartTag("<div>");
        Element element = xmlTreeBuilder.insert(startTag);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsertComment() {
        Token.Comment comment = new Token.Comment("test comment", "");
        xmlTreeBuilder.insert(comment);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof Comment);
    }

    @Test
    public void testInsertCharacter() {
        Token.Character character = new Token.Character("test character");
        xmlTreeBuilder.insert(character);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof TextNode);
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("html", "", "", "");
        xmlTreeBuilder.insert(doctype);
        assertTrue(xmlTreeBuilder.currentElement().childNode(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("div");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("div"), "", new ParseSettings(false)));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        String inputFragment = "<xml></xml>";
        String baseUri = "";
        List<Node> nodes = xmlTreeBuilder.parseFragment(inputFragment, baseUri, ParseErrorList.noTracking(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}