import org.junit.Test;
import org.junit.Before;
import org.junit.After;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    private XmlTreeBuilder xmlTreeBuilder;

    @Before
    public void setUp() {
        xmlTreeBuilder = new XmlTreeBuilder();
    }

    @After
    public void tearDown() {
        xmlTreeBuilder = null;
    }

    @Test
    public void testDefaultSettings() {
        ParseSettings settings = xmlTreeBuilder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testParseReaderString() {
        StringReader reader = new StringReader("<xml></xml>");
        Document document = xmlTreeBuilder.parse(reader, "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testParseStringString() {
        Document document = xmlTreeBuilder.parse("<xml></xml>", "http://example.com");
        assertNotNull(document);
    }

    @Test
    public void testInitialiseParse() {
        StringReader reader = new StringReader("<xml></xml>");
        xmlTreeBuilder.initialiseParse(reader, "http://example.com", new ParseErrorList(), new ParseSettings());
        assertTrue(xmlTreeBuilder.stack.size() > 0);
    }

    @Test
    public void testProcessStartTag() {
        Token.StartTag startTag = new Token.StartTag("xml");
        Element element = xmlTreeBuilder.insert(startTag);
        assertNotNull(element);
    }

    @Test
    public void testProcessEndTag() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testProcessComment() {
        Token.Comment commentToken = new Token.Comment("comment");
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testProcessCharacter() {
        Token.Character character = new Token.Character("text");
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testProcessDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertNode() {
        Node node = new Element(Tag.valueOf("element"), "http://example.com");
        xmlTreeBuilder.insertNode(node);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertSelfClosingTag() {
        Token.StartTag startTag = new Token.StartTag("self-closing");
        startTag.selfClosing = true;
        Element element = xmlTreeBuilder.insert(startTag);
        assertTrue(element.tag().isSelfClosing());
    }

    @Test
    public void testInsertCommentBogus() {
        Token.Comment commentToken = new Token.Comment("comment");
        commentToken.bogus = true;
        xmlTreeBuilder.insert(commentToken);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacterCDATA() {
        Token.Character character = new Token.Character("text");
        character.isCData = true;
        xmlTreeBuilder.insert(character);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        Token.Doctype doctype = new Token.Doctype("doctype");
        xmlTreeBuilder.insert(doctype);
        assertEquals(1, xmlTreeBuilder.currentElement().childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        Token.EndTag endTag = new Token.EndTag("xml");
        xmlTreeBuilder.stack.add(new Element(Tag.valueOf("xml"), "http://example.com"));
        xmlTreeBuilder.popStackToClose(endTag);
        assertEquals(0, xmlTreeBuilder.stack.size());
    }

    @Test
    public void testParseFragment() {
        List<Node> nodes = xmlTreeBuilder.parseFragment("<xml></xml>", "http://example.com", new ParseErrorList(), new ParseSettings());
        assertNotNull(nodes);
    }
}