package org.jsoup.parser;

import static org.junit.Assert.*;

import org.jsoup.helper.Validate;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.BooleanAttribute;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();
        String name = "html";
        doctype.name(name);
        String pubSysKey = "pubSysKey";
        doctype.pubSysKey(pubSysKey);
        String publicIdentifier = "publicIdentifier";
        doctype.publicIdentifier(publicIdentifier);
        String systemIdentifier = "systemIdentifier";
        doctype.systemIdentifier(systemIdentifier);
        doctype.forceQuirks(true);
        Token.Doctype resetDoctype = doctype.reset();
        assertEquals(name, resetDoctype.getName());
        assertNull(resetDoctype.getPubSysKey());
        assertNull(resetDoctype.getPublicIdentifier());
        assertNull(resetDoctype.getSystemIdentifier());
        assertFalse(resetDoctype.isForceQuirks());
    }

    @Test
    public void testTagReset() {
        Token.Tag tag = new Token.StartTag();
        String tagName = "div";
        tag.name(tagName);
        tag.name("p"); // setting normalName
        String attributeName = "class";
        tag.appendAttributeName("id"); // setting pendingAttributeName
        tag.appendAttributeName("class");
        tag.appendAttributeValue("red");
        tag.appendAttributeValue("blue");
        tag.setEmptyAttributeValue();
        tag.newAttribute(); // processing and clearing pendingAttributeName and pendingAttributeValue
        Token.Tag resetTag = tag.reset();
        assertEquals(tagName, resetTag.name());
        assertEquals("p", resetTag.normalName());
        assertNull(resetTag.getAttributes());
        assertFalse(resetTag.isSelfClosing());
    }

    @Test
    public void testTagFinalise() {
        Token.StartTag tag = new Token.StartTag();
        tag.nameAttr("div", new Attributes().put("class", "red"));
        tag.finaliseTag();
        Attributes attributes = tag.getAttributes();
        assertEquals("div", attributes.getClass().getSimpleName());
        assertEquals("class", attributes.keySet().iterator().next());
        assertEquals("red", attributes.getValue("class"));
        assertTrue(attributes.size() == 1);
    }

    @Test
    public void testTagToString() {
        Token.StartTag tag = new Token.StartTag().name("div").attributes(new Attributes().put("class", "red"));
        assertEquals("<div class=\"red\">", tag.toString());
    }

    @Test
    public void testCommentReset() {
        Token.Comment comment = new Token.Comment();
        String data = "This is a comment.";
        comment.getData(data);
        Token.Comment resetComment = comment.reset();
        assertEquals("", resetComment.getData());
        assertFalse(resetComment.isBogus());
    }

    @Test
    public void testCharacterReset() {
        Token.Character character = new Token.Character();
        String data = "A";
        character.data(data);
        Token.Character resetCharacter = character.reset();
        assertNull(resetCharacter.getData());
    }

    @Test
    public void testTokenTypes() {
        Token token = new Token();
        assertTrue(token.isDoctype() == false);
        assertTrue(token.isStartTag() == false);
        assertTrue(token.isEndTag() == false);
        assertTrue(token.isComment() == false);
        assertTrue(token.isCharacter() == false);
        assertTrue(token.isEOF() == false);
        
        Token doctype = new Token.Doctype();
        assertTrue(doctype.isDoctype() == true);
        assertTrue(doctype.isStartTag() == false);
        assertTrue(doctype.isEndTag() == false);
        assertTrue(doctype.isComment() == false);
        assertTrue(doctype.isCharacter() == false);
        assertTrue(doctype.isEOF() == false);

        Token startTag = new Token.StartTag();
        assertTrue(startTag.isDoctype() == false);
        assertTrue(startTag.isStartTag() == true);
        assertTrue(startTag.isEndTag() == false);
        assertTrue(startTag.isComment() == false);
        assertTrue(startTag.isCharacter() == false);
        assertTrue(startTag.isEOF() == false);

        Token endTag = new Token.EndTag();
        assertTrue(endTag.isDoctype() == false);
        assertTrue(endTag.isStartTag() == false);
        assertTrue(endTag.isEndTag() == true);
        assertTrue(endTag.isComment() == false);
        assertTrue(endTag.isCharacter() == false);
        assertTrue(endTag.isEOF() == false);

        Token comment = new Token.Comment();
        assertTrue(comment.isDoctype() == false);
        assertTrue(comment.isStartTag() == false);
        assertTrue(comment.isEndTag() == false);
        assertTrue(comment.isComment() == true);
        assertTrue(comment.isCharacter() == false);
        assertTrue(comment.isEOF() == false);

        Token character = new Token.Character();
        assertTrue(character.isDoctype() == false);
        assertTrue(character.isStartTag() == false);
        assertTrue(character.isEndTag() == false);
        assertTrue(character.isComment() == false);
        assertTrue(character.isCharacter() == true);
        assertTrue(character.isEOF() == false);

        Token eof = new Token.EOF();
        assertTrue(eof.isDoctype() == false);
        assertTrue(eof.isStartTag() == false);
        assertTrue(eof.isEndTag() == false);
        assertTrue(eof.isComment() == false);
        assertTrue(eof.isCharacter() == false);
        assertTrue(eof.isEOF() == true);
    }
}