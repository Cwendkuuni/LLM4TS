import org.jsoup.parser.*;
import org.junit.Test;
import java.util.List;
import static org.junit.Assert.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.initialiseParse("<html></html>", "http://example.com", new ParseErrorList());
        assertEquals("http://example.com", treeBuilder.baseUri);
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(treeBuilder.doc, treeBuilder.stack.get(0));
        assertEquals(Document.OutputSettings.Syntax.xml, treeBuilder.doc.outputSettings().syntax());
    }

    @Test
    public void testProcess_StartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        treeBuilder.process(new Token.StartTag("child", new Attributes()));
        assertEquals(2, treeBuilder.stack.size());
        Element child = treeBuilder.stack.get(1);
        assertEquals("child", child.tagName());
        assertEquals("http://example.com", child.baseUri());
    }

    @Test
    public void testProcess_EndTag_TagFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        treeBuilder.stack.add(root);
        treeBuilder.stack.add(child);
        treeBuilder.process(new Token.EndTag("child"));
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(root, treeBuilder.stack.get(0));
    }

    @Test
    public void testProcess_EndTag_TagNotFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        treeBuilder.stack.add(root);
        treeBuilder.stack.add(child);
        treeBuilder.process(new Token.EndTag("missing"));
        assertEquals(2, treeBuilder.stack.size());
        assertEquals(child, treeBuilder.stack.get(1));
    }

    @Test
    public void testProcess_Comment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        treeBuilder.process(new Token.Comment("This is a comment"));
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        Comment comment = (Comment) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testProcess_Character() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        treeBuilder.process(new Token.Character("Text content"));
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        TextNode textNode = (TextNode) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("Text content", textNode.getWholeText());
    }

    @Test
    public void testProcess_Doctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        treeBuilder.process(new Token.Doctype("html"));
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        DocumentType doctype = (DocumentType) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("html", doctype.getName());
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        treeBuilder.insertNode(new Comment("This is a comment", "http://example.com"));
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        Comment comment = (Comment) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("This is a comment", comment.getData());
    }

    @Test
    public void testInsert_StartTag() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.StartTag startTag = new Token.StartTag("child", new Attributes());
        Element child = treeBuilder.insert(startTag);
        assertEquals(2, treeBuilder.stack.size());
        assertEquals("child", child.tagName());
        assertEquals("http://example.com", child.baseUri());
    }

    @Test
    public void testInsert_StartTag_SelfClosing() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.StartTag selfClosingTag = new Token.StartTag("self-closing", new Attributes());
        selfClosingTag.selfClosing = true;
        Element selfClosing = treeBuilder.insert(selfClosingTag);
        assertTrue(selfClosing.tag().isKnownTag());
        assertTrue(selfClosing.tag().isSelfClosing());
    }

    @Test
    public void testInsert_Comment_Bogus() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Comment bogusComment = new Token.Comment("<!DOCTYPE html>", true);
        treeBuilder.insert(bogusComment);
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        XmlDeclaration xmlDeclaration = (XmlDeclaration) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("DOCTYPE html", xmlDeclaration.getDeclaration());
    }

    @Test
    public void testInsert_Comment_NotBogus() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Comment comment = new Token.Comment("This is a comment");
        treeBuilder.insert(comment);
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        Comment insertedComment = (Comment) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("This is a comment", insertedComment.getData());
    }

    @Test
    public void testInsert_Character() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Character character = new Token.Character("Text content");
        treeBuilder.insert(character);
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        TextNode textNode = (TextNode) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("Text content", textNode.getWholeText());
    }

    @Test
    public void testInsert_Doctype() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        treeBuilder.stack.add(new Element(Tag.valueOf("root"), "http://example.com"));
        Token.Doctype doctype = new Token.Doctype("html");
        treeBuilder.insert(doctype);
        assertEquals(2, treeBuilder.stack.get(0).childNodes().size());
        DocumentType doctypeNode = (DocumentType) treeBuilder.stack.get(0).childNodes().get(1);
        assertEquals("html", doctypeNode.getName());
    }

    @Test
    public void testPopStackToClose_TagFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        Element sibling = new Element(Tag.valueOf("child"), "http://example.com");
        treeBuilder.stack.add(root);
        treeBuilder.stack.add(child);
        treeBuilder.stack.add(sibling);
        Token.EndTag endTag = new Token.EndTag("child");
        treeBuilder.popStackToClose(endTag);
        assertEquals(1, treeBuilder.stack.size());
        assertEquals(root, treeBuilder.stack.get(0));
    }

    @Test
    public void testPopStackToClose_TagNotFound() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        Element root = new Element(Tag.valueOf("root"), "http://example.com");
        Element child = new Element(Tag.valueOf("child"), "http://example.com");
        treeBuilder.stack.add(root);
        treeBuilder.stack.add(child);
        Token.EndTag endTag = new Token.EndTag("missing");
        treeBuilder.popStackToClose(endTag);
        assertEquals(2, treeBuilder.stack.size());
        assertEquals(child, treeBuilder.stack.get(1));
    }

    @Test
    public void testParseFragment() {
        XmlTreeBuilder treeBuilder = new XmlTreeBuilder();
        ParseErrorList errors = new ParseErrorList();
        List<Node> nodes = treeBuilder.parseFragment("<div></div><span></span>", "http://example.com", errors);
        assertEquals(2, nodes.size());
        assertTrue(nodes.get(0) instanceof Element);
        assertTrue(nodes.get(1) instanceof Element);
        Element div = (Element) nodes.get(0);
        Element span = (Element) nodes.get(1);
        assertEquals("div", div.tagName());
        assertEquals("http://example.com", div.baseUri());
        assertEquals("span", span.tagName());
        assertEquals("http://example.com", span.baseUri());
    }

}