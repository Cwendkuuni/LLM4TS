import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.parser.ParseErrorList;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("<xml></xml>", "http://www.example.com", new ParseErrorList());

        assertEquals(builder.baseUri, "http://www.example.com");
        assertEquals(builder.errors.size(), 0);
        assertTrue(builder.stack.size() > 0);
        assertEquals(builder.doc.outputSettings().syntax(), Document.OutputSettings.Syntax.xml);
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        boolean result = builder.process(new Token.StartTag("<tag>"));
        assertTrue(result);
        assertEquals(builder.stack.size(), 1);
    }

    @Test
    public void testProcess_EndTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag"), "", null));
        boolean result = builder.process(new Token.EndTag("</tag>"));
        assertTrue(result);
        assertEquals(builder.stack.size(), 0);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element = new Element(Tag.valueOf("tag"), "", null);
        builder.stack.add(element);
        Node node = new TextNode("text", "");
        builder.insertNode(node);
        assertEquals(builder.currentElement().childNodes().size(), 1);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("<tag></tag>");
        Element element = builder.insert(startTag);

        assertEquals(element.tagName(), "tag");
        assertTrue(builder.tokeniser.selfClosingFlagAcknowledged);
        assertTrue(element.tag().isSelfClosing());
        assertEquals(builder.stack.size(), 1);
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);

        assertEquals(builder.currentElement().childNodes().size(), 1);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character character = new Token.Character("character");
        builder.insert(character);

        assertEquals(builder.currentElement().childNodes().size(), 1);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctype = new Token.Doctype("doctype");
        builder.insert(doctype);

        assertEquals(builder.currentElement().childNodes().size(), 1);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("tag"), "", null);
        Element element2 = new Element(Tag.valueOf("tag"), "", null);
        builder.stack.add(element1);
        builder.stack.add(element2);

        Token.EndTag endTag = new Token.EndTag("</tag>");
        builder.popStackToClose(endTag);

        assertEquals(builder.stack.size(), 0);
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        List<Node> nodes = builder.parseFragment("<tag>text</tag>", "http://www.example.com", new ParseErrorList());

        assertEquals(nodes.size(), 1);
        assertTrue(nodes.get(0) instanceof Element);
    }
}