import org.junit.*;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings defaultSettings = builder.defaultSettings();
        assertNotNull(defaultSettings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse(new StringReader("<root></root>"), "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = builder.parse("<root></root>", "http://example.com");
        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse(new StringReader("<root></root>"), "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertTrue(builder.stack.size() > 0);
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "attributes", false);
        builder.process(startTag);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.stack.add(builder.currentElement());
        builder.process(endTag);
        assertTrue(builder.stack.size() == 0);
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.process(comment);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        builder.process(character);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier", "pubSysKey");
        builder.process(doctype);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag", "baseUri", null);
        builder.insertNode(element);
        assertEquals(1, builder.currentElement().getChildNodes().size());
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag", "attributes", false);
        Element element = builder.insert(startTag);
        assertNotNull(element);
        assertNotNull(builder.stack);
        assertEquals(1, element.getChildNodes().size());
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        builder.insert(character);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("name", "publicIdentifier", "systemIdentifier", "pubSysKey");
        builder.insert(doctype);
        assertNotNull(builder.currentElement().getChildNodes());
    }

    @Test
    public void testPopStackToCloseNotFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("tag1", "baseUri", null));
        builder.stack.add(new Element("tag2", "baseUri", null));
        Token.EndTag endTag = new Token.EndTag("tag3");
        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
    }

    @Test
    public void testPopStackToCloseFound() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element("tag", "baseUri", null);
        builder.stack.add(new Element("tag1", "baseUri", null));
        builder.stack.add(element);
        builder.stack.add(new Element("tag2", "baseUri", null));
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.popStackToClose(endTag);
        assertEquals(2, builder.stack.size());
        assertFalse(builder.stack.contains(element));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<root></root>", "http://example.com", new ParseErrorList(), ParseSettings.preserveCase);
        assertNotNull(nodes);
        assertEquals(1, nodes.size());
    }
}