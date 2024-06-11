import static org.junit.Assert.*;
import org.junit.Test;
import org.jsoup.nodes.*;
import org.jsoup.parser.*;

public class XmlTreeBuilderTest {

    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.initialiseParse("input", "base", new ParseErrorList());
        
        assertEquals(builder.stack.peek(), builder.doc);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        // Test StartTag
        Token.StartTag startTag = new Token.StartTag("tag", "attr");
        builder.process(startTag);
        assertTrue(builder.stack.peek() instanceof Element);

        // Test EndTag
        Token.EndTag endTag = new Token.EndTag("tag");
        builder.process(endTag);
        assertEquals(builder.stack.size(), 0);

        // Test Comment
        Token.Comment comment = new Token.Comment("comment");
        builder.process(comment);
        assertTrue(builder.doc.childNodes().get(0) instanceof Comment);

        // Test Character
        Token.Character character = new Token.Character("character");
        builder.process(character);
        assertTrue(builder.doc.childNodes().get(0) instanceof TextNode);

        // Test Doctype
        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        builder.process(doctype);
        assertTrue(builder.doc.childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Element element = new Element(Tag.valueOf("tag"), "base", new Attributes());
        builder.insertNode(element);
        assertEquals(builder.currentElement().childNodes().get(0), element);
    }

    @Test
    public void testInsert() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTag = new Token.StartTag("tag", "attr");
        builder.insert(startTag);
        assertTrue(builder.currentElement() instanceof Element);

        Token.Comment comment = new Token.Comment("comment");
        builder.insert(comment);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof Comment);

        Token.Character character = new Token.Character("character");
        builder.insert(character);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof TextNode);

        Token.Doctype doctype = new Token.Doctype("name", "public", "system");
        builder.insert(doctype);
        assertTrue(builder.currentElement().childNodes().get(0) instanceof DocumentType);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        builder.stack.add(new Element(Tag.valueOf("tag1"), "base", new Attributes()));
        builder.stack.add(new Element(Tag.valueOf("tag2"), "base", new Attributes()));
        
        Token.EndTag endTag = new Token.EndTag("tag1");
        builder.popStackToClose(endTag);
        
        assertEquals(builder.stack.size(), 1);
    }
}