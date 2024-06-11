import org.junit.Test;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<test></test>");
        String baseUri = "http://example.com";
        Parser parser = new Parser();
        builder.initialiseParse(input, baseUri, parser);

        // assert statements
        assertNotNull(builder.getInput());
        assertEquals(baseUri, builder.getBaseUri());
        assertEquals(parser, builder.getParser());
    }

    @Test
    public void testParseWithStringReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<test></test>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(new StringReader(input), baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testParseWithReader() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Reader input = new StringReader("<test></test>");
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);
        assertNotNull(doc);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token startTagToken = new Token(Token.TokenType.StartTag, "<test>");
        assertTrue(builder.process(startTagToken));

        Token endTagToken = new Token(Token.TokenType.EndTag, "</test>");
        assertTrue(builder.process(endTagToken));

        Token commentToken = new Token(Token.TokenType.Comment, "<!-- comment -->");
        assertTrue(builder.process(commentToken));

        Token characterToken = new Token(Token.TokenType.Character, "text");
        assertTrue(builder.process(characterToken));

        Token doctypeToken = new Token(Token.TokenType.Doctype, "<!DOCTYPE html>");
        assertTrue(builder.process(doctypeToken));

        Token eofToken = new Token(Token.TokenType.EOF, "");
        assertTrue(builder.process(eofToken));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Node node = new Element(Tag.valueOf("test"), "http://example.com");
        builder.insertNode(node);

        assertEquals(node, builder.currentElement().childNodes().get(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("test", new Attributes());
        Element el = builder.insert(startTag);

        assertEquals(el, builder.currentElement());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("comment");
        builder.insert(commentToken);

        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("text");
        builder.insert(characterToken);

        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        builder.insert(doctypeToken);

        assertEquals(1, builder.currentElement().childNodes().size());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("test");

        builder.stack.add(new Element(Tag.valueOf("test"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("test"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("test"), "http://example.com"));

        builder.popStackToClose(endTag);

        assertEquals(1, builder.stack.size());
    }

    @Test
    public void testParseFragmentWithoutContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<test></test>", "http://example.com", new Parser());

        assertEquals(1, nodes.size());
    }

    @Test
    public void testParseFragmentWithContext() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<test></test>", new Element(Tag.valueOf("div"), "http://example.com"), "http://example.com", new Parser());

        assertEquals(1, nodes.size());
    }

}