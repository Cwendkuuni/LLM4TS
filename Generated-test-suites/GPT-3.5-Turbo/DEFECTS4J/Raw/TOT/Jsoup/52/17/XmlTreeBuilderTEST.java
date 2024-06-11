import org.jsoup.nodes.*;
import org.jsoup.parser.Token.*;

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Test;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "<root></root>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);

        assertEquals(input, builder.currentInput);
        assertEquals(baseUri, builder.baseUri);
        assertEquals(errors, builder.errors);
        assertNotNull(builder.stack);
        assertEquals(1, builder.stack.size());
        assertEquals(builder.doc, builder.stack.get(0));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = new Document("");
        Element el = new Element(Tag.valueOf("element"), "");

        builder.stack.add(doc);
        builder.insertNode(el);

        assertEquals(el, doc.childNode(0));
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.attributes.put("attr1", "value1");
        startTag.attributes.put("attr2", "value2");

        Element el = builder.insert(startTag);

        assertEquals("tag", el.tagName());
        assertEquals("https://example.com", el.baseUri());
        assertEquals("value1", el.attr("attr1"));
        assertEquals("value2", el.attr("attr2"));
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("tag");
        startTag.selfClosing = true;

        Element el = builder.insert(startTag);

        assertTrue(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testInsert_StartTag_UnknownTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.StartTag startTag = new Token.StartTag("unknown");

        Element el = builder.insert(startTag);

        assertFalse(el.tag().isKnownTag());
        assertTrue(el.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("This is a comment");

        builder.insert(commentToken);

        Comment comment = (Comment) builder.currentElement().childNode(0);
        assertEquals("This is a comment", comment.getData());
        assertEquals("https://example.com", comment.baseUri());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Comment commentToken = new Token.Comment("<!DOCTYPE html>");

        builder.insert(commentToken);

        XmlDeclaration declaration = (XmlDeclaration) builder.currentElement().childNode(0);
        assertEquals("DOCTYPE html", declaration.getName());
        assertEquals("https://example.com", declaration.baseUri());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Character characterToken = new Token.Character("data");

        builder.insert(characterToken);

        TextNode textNode = (TextNode) builder.currentElement().childNode(0);
        assertEquals("data", textNode.getWholeText());
        assertEquals("https://example.com", textNode.baseUri());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Token.Doctype doctypeToken = new Token.Doctype("html", "", "", "");

        builder.insert(doctypeToken);

        DocumentType doctype = (DocumentType) builder.currentElement().childNode(0);
        assertEquals("html", doctype.getName());
        assertEquals("", doctype.getPublicIdentifier());
        assertEquals("", doctype.getSystemIdentifier());
        assertEquals("https://example.com", doctype.baseUri());
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Element element1 = new Element(Tag.valueOf("element"), "");
        Element element2 = new Element(Tag.valueOf("element"), "");

        builder.stack.add(element1);
        builder.stack.add(element2);

        builder.popStackToClose(new Token.EndTag("element"));

        assertEquals(0, builder.stack.size());
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String inputFragment = "<fragment></fragment>";
        String baseUri = "https://example.com";
        ParseErrorList errors = new ParseErrorList();

        List<Node> nodes = builder.parseFragment(inputFragment, baseUri, errors);

        assertEquals(1, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertEquals("fragment", ((Element) nodes.get(0)).tagName());
    }

}