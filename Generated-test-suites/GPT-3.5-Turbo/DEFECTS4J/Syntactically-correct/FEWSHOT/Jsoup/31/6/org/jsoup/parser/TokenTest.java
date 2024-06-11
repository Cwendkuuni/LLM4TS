package org.jsoup.parser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.jsoup.nodes.Attribute;
import org.junit.Test;

public class TokenTest {
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

        Token.Character character = new Token.Character("");
        assertEquals("Character", character.tokenType());

        Token.EOF eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();
        assertFalse(doctype.isForceQuirks());
        doctype.forceQuirks = true;
        assertTrue(doctype.isForceQuirks());

        doctype.name.append("name");
        doctype.publicIdentifier.append("public");
        doctype.systemIdentifier.append("system");

        assertEquals("name", doctype.getName());
        assertEquals("public", doctype.getPublicIdentifier());
        assertEquals("system", doctype.getSystemIdentifier());
    }

    @Test
    public void testTag() {
        Token.Tag tag = new Token.Tag();

        tag.selfClosing = true;
        assertTrue(tag.isSelfClosing());

        tag.tagName = "tag";
        assertEquals("tag", tag.name());

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("name1", "value1"));
        attributes.put(new Attribute("name2", "value2"));
        tag.attributes = attributes;

        assertEquals(attributes, tag.getAttributes());

        tag.appendTagName("append1");
        tag.appendTagName("append2");
        assertEquals("tagappend1append2", tag.tagName);

        tag.appendAttributeName("attributeName");
        assertEquals("attributeName", tag.pendingAttributeName);

        tag.appendAttributeName('c');
        tag.appendAttributeName('h');
        tag.appendAttributeName('a');
        tag.appendAttributeName('r');
        assertEquals("attributeNamechar", tag.pendingAttributeName);

        tag.appendAttributeValue("value");
        assertEquals("value", tag.pendingAttributeValue.toString());

        tag.appendAttributeValue('a');
        tag.appendAttributeValue('p');
        tag.appendAttributeValue('p');
        tag.appendAttributeValue('e');
        tag.appendAttributeValue('n');
        tag.appendAttributeValue('d');
        assertEquals("valueappend", tag.pendingAttributeValue.toString());

        tag.newAttribute();
        assertNotNull(tag.attributes);
        assertEquals(1, tag.attributes.size());

        tag.finaliseTag();
        assertNull(tag.pendingAttributeName);
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        assertNotNull(startTag.attributes);

        startTag = new Token.StartTag("name");
        assertEquals("name", startTag.tagName);

        Attributes attributes = new Attributes();
        attributes.put(new Attribute("name1", "value1"));
        attributes.put(new Attribute("name2", "value2"));
        startTag = new Token.StartTag("name", attributes);
        assertEquals("name", startTag.tagName);
        assertEquals(attributes, startTag.attributes);
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();
        assertNull(endTag.tagName);

        endTag = new Token.EndTag("name");
        assertEquals("name", endTag.tagName);
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();
        assertEquals("", comment.getData());

        comment.data.append("data");
        assertEquals("data", comment.getData());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character("");
        assertEquals("", character.getData());

        character = new Token.Character("data");
        assertEquals("data", character.getData());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();
        assertTrue(eof.isEOF());
    }

    // Additional test cases can be added to cover more scenarios
}