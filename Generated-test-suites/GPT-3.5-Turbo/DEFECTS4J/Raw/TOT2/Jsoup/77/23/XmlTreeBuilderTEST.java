import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();
        assertNotNull(settings);
        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<html></html>"), "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<html></html>", "http://example.com");
        assertNotNull(doc);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("div"), "http://example.com");
        builder.insertNode(element);
        assertEquals(element, builder.currentElement().childNode(0));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("div", new Attributes());
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertEquals("div", element.tagName());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("Test comment");
        builder.insert(commentToken);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("Test character");
        builder.insert(characterToken);
        assertEquals(1, builder.currentElement().childNodeSize());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("html", "", "");
        builder.insert(doctype);
        assertEquals(DocumentType.class, builder.currentElement().childNode(0).getClass());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("div");
        builder.stack.add(new Element(Tag.valueOf("span"), "http://example.com"));
        builder.stack.add(new Element(Tag.valueOf("div"), "http://example.com"));
        builder.popStackToClose(endTag);
        assertEquals(1, builder.stack.size());
        assertEquals("span", builder.currentElement().tagName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<div></div>", "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}