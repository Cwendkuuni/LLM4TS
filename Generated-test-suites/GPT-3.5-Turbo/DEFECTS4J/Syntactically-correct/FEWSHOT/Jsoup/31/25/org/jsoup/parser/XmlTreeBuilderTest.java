package org.jsoup.parser;

import org.jsoup.nodes.*;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EOF;
import java.util.Iterator;
import org.jsoup.helper.Validate;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;


public class XmlTreeBuilderTest {
    @Test
    public void testInitialiseParse() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String input = "";
        String baseUri = "http://example.com";
        ParseErrorList errors = new ParseErrorList();

        builder.initialiseParse(input, baseUri, errors);
        assertEquals(builder.stack.size(), 1);
    }

    @Test
    public void testProcess() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Token.StartTag startTagToken = new Token.StartTag("tag", null);
        Token.EndTag endTagToken = new Token.EndTag("tag");
        Token.Comment commentToken = new Token.Comment("comment");
        Token.Character characterToken = new Token.Character("character");
        Token.Doctype doctypeToken = new Token.Doctype("doctype");

        assertTrue(builder.process(startTagToken));
        assertTrue(builder.process(endTagToken));
        assertTrue(builder.process(commentToken));
        assertTrue(builder.process(characterToken));
        assertTrue(builder.process(doctypeToken));
        assertTrue(builder.process(new Token.EOF()));

        assertFalse(builder.process(new Token("unknown")));
    }

    @Test
    public void testInsertNode() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        Document doc = new Document("");

        builder.init(doc, "");
        Element parent = builder.currentElement();
        Element child = new Element("child");

        builder.insertNode(child);
        assertTrue(parent.childNodes().contains(child));
    }

    @Test
    public void testInsertStartTag() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "tag";
        String baseUri = "http://example.com";

        Token.StartTag startTag = new Token.StartTag(name, null);
        Element element = builder.insert(startTag);

        assertEquals(element.tagName(), name);
        assertEquals(element.baseUri(), baseUri);
        assertTrue(builder.stack.contains(element));
    }

    @Test
    public void testInsertComment() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String data = "comment";
        String baseUri = "http://example.com";

        Token.Comment commentToken = new Token.Comment(data);
        builder.insert(commentToken);

        Comment comment = (Comment) builder.currentElement().childNodes().get(0);
        assertEquals(comment.getData(), data);
        assertEquals(comment.baseUri(), baseUri);
    }

    @Test
    public void testInsertCharacter() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String data = "character";
        String baseUri = "http://example.com";

        Token.Character characterToken = new Token.Character(data);
        builder.insert(characterToken);

        TextNode textNode = (TextNode) builder.currentElement().childNodes().get(0);
        assertEquals(textNode.getWholeText(), data);
        assertEquals(textNode.baseUri(), baseUri);
    }

    @Test
    public void testInsertDoctype() {
        XmlTreeBuilder builder = new XmlTreeBuilder();
        String name = "doctype";
        String publicIdentifier = "public";
        String systemIdentifier = "system";
        String baseUri = "http://example.com";

        Token.Doctype doctype = new Token.Doctype(name, publicIdentifier, systemIdentifier);
        builder.insert(doctype);

        DocumentType documentType = (DocumentType) builder.currentElement().childNodes().get(0);
        assertEquals(documentType.getName(), name);
        assertEquals(documentType.getPublicIdentifier(), publicIdentifier);
        assertEquals(documentType.getSystemIdentifier(), systemIdentifier);
        assertEquals(documentType.baseUri(), baseUri);
    }

    @Test
    public void testPopStackToClose() {
        XmlTreeBuilder builder = new XmlTreeBuilder();

        Tag tag1 = Tag.valueOf("tag1");
        Tag tag2 = Tag.valueOf("tag2");

        Element parent1 = new Element(tag1, "");
        Element parent2 = new Element(tag2, "");
        Element child = new Element(tag1, "");

        builder.stack.add(parent2);
        builder.stack.add(parent1);
        builder.stack.add(child);

        Token.EndTag endTagToken = new Token.EndTag("tag1");
        builder.popStackToClose(endTagToken);

        assertTrue(builder.stack.isEmpty());
    }
}