package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.jsoup.nodes.Attributes;
import org.junit.Before;
import org.junit.Test;

public class TokenTest {

    private Token.Doctype doctype;
    private Token.StartTag startTag;
    private Token.EndTag endTag;
    private Token.Comment comment;
    private Token.Character character;
    private Token.EOF eof;

    @Before
    public void setup() {
        doctype = new Token.Doctype();
        startTag = new Token.StartTag();
        endTag = new Token.EndTag();
        comment = new Token.Comment();
        character = new Token.Character();
        eof = new Token.EOF();
    }

    @Test
    public void testTokenReset() {
        doctype.forceQuirks = true;
        doctype.getName().append("html");
        doctype.getPublicIdentifier().append("//W3C//DTD HTML 4.0//EN");
        doctype.getSystemIdentifier().append("http://www.example.com");
        doctype.reset();
        assertFalse(doctype.isForceQuirks());
        assertTrue(doctype.getName().length() == 0);
        assertTrue(doctype.getPublicIdentifier().length() == 0);
        assertTrue(doctype.getSystemIdentifier().length() == 0);

        startTag.name("div");
        Attribute attribute = new Attribute("class", "red");
        startTag.attributes = new Attributes();
        startTag.attributes.put(attribute);
        startTag.reset();
        assertNull(startTag.getAttributes());
        assertTrue(startTag.tagName == null);
        assertTrue(startTag.normalName == null);

        endTag.name("p");
        endTag.reset();
        assertTrue(endTag.tagName == null);
        assertTrue(endTag.normalName == null);

        comment.data.append("This is a comment.");
        comment.reset();
        assertTrue(comment.getData().length() == 0);
        assertFalse(comment.bogus);

        character.data("This is a character.");
        character.reset();
        assertNull(character.getData());

        eof.reset();
        assertTrue(eof == eof.reset());
    }

    @Test
    public void testDoctypeGetters() {
        doctype.forceQuirks = true;
        doctype.name.append("html");
        doctype.publicIdentifier.append("//W3C//DTD HTML 4.0//EN");
        doctype.systemIdentifier.append("http://www.example.com");

        assertEquals("html", doctype.getName());
        assertEquals("//W3C//DTD HTML 4.0//EN", doctype.getPublicIdentifier());
        assertEquals("http://www.example.com", doctype.getSystemIdentifier());
        assertTrue(doctype.isForceQuirks());
    }

    @Test
    public void testStartTagAppendMethods() {
        startTag.appendTagName("di");
        startTag.appendTagName("v");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());

        startTag.appendAttributeName("class");
        assertEquals("class", startTag.pendingAttributeName);

        startTag.appendAttributeValue("\"red\"");
        assertEquals("red", startTag.pendingAttributeValue.toString());

        startTag.appendAttributeName('i');
        assertEquals("classi", startTag.pendingAttributeName);

        startTag.appendAttributeName("d");
        assertEquals("classid", startTag.pendingAttributeName);

        startTag.appendAttributeValue("<p>");
        assertEquals("\"red\"<p>", startTag.pendingAttributeValue.toString());

        startTag.appendAttributeValue(new char[]{'"', 'b', 'l', 'u', 'e', '"'});
        assertEquals("\"red\"<p>\"blue\"", startTag.pendingAttributeValue.toString());

        startTag.appendAttributeValue(new int[]{97, 98, 99});
        assertEquals("\"red\"<p>\"blue\"abc", startTag.pendingAttributeValue.toString());

        startTag.setEmptyAttributeValue();
        assertTrue(startTag.hasEmptyAttributeValue);
    }

    @Test
    public void testStartTagNewAttribute() {
        startTag.newAttribute();
        assertNull(startTag.pendingAttributeName);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertTrue(startTag.pendingAttributeValue.length() == 0);        
    }

    @Test
    public void testStartTagFinaliseTag() {
        startTag.pendingAttributeName = "class";
        startTag.pendingAttributeValue.append("red");
        startTag.finaliseTag();
        assertNull(startTag.pendingAttributeName);
        assertTrue(startTag.attributes.size() == 1);
    }

    @Test
    public void testEndTagToString() {
        endTag.name("p");
        assertEquals("</p>", endTag.toString());
    }

    @Test
    public void testCommentToString() {
        comment.data.append("This is a comment.");
        assertEquals("<!--This is a comment.-->", comment.toString());
    }

    @Test
    public void testCharacterToString() {
        character.data("This is a character.");
        assertEquals("This is a character.", character.toString());
    }

    @Test
    public void testDoctypeIsMethods() {
        assertFalse(doctype.isStartTag());
        assertFalse(doctype.isEndTag());
        assertFalse(doctype.isComment());
        assertFalse(doctype.isCharacter());
        assertFalse(doctype.isEOF());
        assertTrue(doctype.isDoctype());
    }

    @Test
    public void testStartTagIsMethods() {
        assertFalse(startTag.isDoctype());
        assertFalse(startTag.isEndTag());
        assertFalse(startTag.isComment());
        assertFalse(startTag.isCharacter());
        assertFalse(startTag.isEOF());
        assertTrue(startTag.isStartTag());
    }

    @Test
    public void testEndTagIsMethods() {
        assertFalse(endTag.isDoctype());
        assertFalse(endTag.isStartTag());
        assertFalse(endTag.isComment());
        assertFalse(endTag.isCharacter());
        assertFalse(endTag.isEOF());
        assertTrue(endTag.isEndTag());
    }

    @Test
    public void testCommentIsMethods() {
        assertFalse(comment.isDoctype());
        assertFalse(comment.isStartTag());
        assertFalse(comment.isEndTag());
        assertFalse(comment.isCharacter());
        assertFalse(comment.isEOF());
        assertTrue(comment.isComment());
    }

    @Test
    public void testCharacterIsMethods() {
        assertFalse(character.isDoctype());
        assertFalse(character.isStartTag());
        assertFalse(character.isEndTag());
        assertFalse(character.isComment());
        assertFalse(character.isEOF());
        assertTrue(character.isCharacter());
    }

    @Test
    public void testEOFIsMethods() {
        assertFalse(eof.isDoctype());
        assertFalse(eof.isStartTag());
        assertFalse(eof.isEndTag());
        assertFalse(eof.isComment());
        assertFalse(eof.isCharacter());
        assertTrue(eof.isEOF());
    }
}