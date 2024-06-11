import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testDefaultSettings() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        ParseSettings settings = builder.defaultSettings();

        assertEquals(ParseSettings.preserveCase, settings);
    }

    @Test
    public void testParseWithReaderAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("root", doc.child(0).nodeName());
    }

    @Test
    public void testParseWithStringAndBaseUri() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "http://example.com";
        Document doc = builder.parse(input, baseUri);

        assertNotNull(doc);
        assertEquals("http://example.com", doc.baseUri());
        assertEquals("root", doc.child(0).nodeName());
    }

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        StringReader input = new StringReader("<root></root>");
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        builder.initialiseParse(input, baseUri, errors, settings);

        assertNotNull(builder.stack);
        assertEquals(builder.doc, builder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, builder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcessStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("root", "attr1=value1", true);
        Element el = builder.insert(startTag);

        assertNotNull(el);
        assertEquals("root", el.nodeName());
        assertTrue(el.tagName().isKnownTag());
        assertTrue(el.tagName().isSelfClosing());
    }

    @Test
    public void testProcessEndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("root"));
        Token.EndTag endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);

        assertTrue(builder.stack.isEmpty());
    }

    @Test
    public void testProcessComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");
        builder.insert(commentToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("This is a comment", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("content");
        builder.insert(characterToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("content", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testProcessDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "");
        builder.insert(doctypeToken);

        assertEquals(1, builder.currentElement().childNodeSize());
        assertEquals("<!doctype html>", builder.currentElement().childNode(0).outerHtml());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element("root"));
        builder.stack.add(new Element("child"));
        Token.EndTag endTag = new Token.EndTag("root");
        builder.popStackToClose(endTag);

        assertEquals(1, builder.stack.size());
        assertEquals("child", builder.currentElement().nodeName());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<root></root>";
        String baseUri = "http://example.com";
        ParseErrorList errors = ParseErrorList.noTracking();
        ParseSettings settings = ParseSettings.preserveCase;

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors, settings);

        assertEquals(1, nodes.size());
        assertEquals("root", nodes.get(0).nodeName());
    }
}