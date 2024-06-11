package org.jsoup.parser;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;
import static org.mockito.Mockito.when;
import static org.junit.Assert.*;

@RunWith(MockitoJUnitRunner.class)
public class TokenTest {

    // Test case for tokenType() method
    @Test
    public void testTokenType() {
        Token.Doctype doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token.StartTag startTag = new Token.StartTag();
        assertEquals("StartTag", startTag.tokenType());

        Token.EndTag endTag = new Token.EndTag();
        assertEquals("EndTag", endTag.tokenType());

        Token.Comment comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token.Character character = new Token.Character("data");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    // Test cases for Doctype class
    @Test
    public void testDoctypeClass() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.name.append("html");
        doctype.publicIdentifier.append("//DTD HTML//");
        doctype.systemIdentifier.append("http://www.example.com/");
        doctype.forceQuirks = false;

        assertEquals("html", doctype.getName());
        assertEquals("//DTD HTML//", doctype.getPublicIdentifier());
        assertEquals("http://www.example.com/", doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());
    }

    // Test cases for StartTag class
    @Test
    public void testStartTagClass() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        startTag.selfClosing = true;
        startTag.attributes = new Attributes();

        assertNotNull(startTag.getAttributes());
        assertEquals("div", startTag.name());
        assertTrue(startTag.isSelfClosing());

        startTag.attributes.put(new Attribute("id", "test"));
        assertEquals("<div id=\"test\">", startTag.toString());

        startTag.newAttribute();
        startTag.appendTagName("span");
        startTag.appendAttributeName("class");
        startTag.appendAttributeValue("test");
        startTag.finaliseTag();
        assertEquals("<div id=\"test\"><span class=\"test\">", startTag.toString());
    }

    // Test cases for EndTag class
    @Test
    public void testEndTagClass() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.tagName = "span";

        assertEquals("span", endTag.name());
        assertEquals("</span>", endTag.toString());
    }

    // Test cases for Comment class
    @Test
    public void testCommentClass() {
        Token.Comment comment = new Token.Comment();
        comment.data.append("Sample comment");

        assertEquals("Sample comment", comment.getData());
        assertEquals("<!--Sample comment-->", comment.toString());
    }

    // Test case for Character class
    @Test
    public void testCharacterClass() {
        Token.Character character = new Token.Character("data");
        assertEquals("data", character.getData());
        assertEquals("data", character.toString());
    }

    // Test case for EOF class
    @Test
    public void testEOFClass() {
        Token.EOF eof = new Token.EOF();
        assertNotNull(eof);
    }
}