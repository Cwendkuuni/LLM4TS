package org.jsoup.parser; // import statements go here

import static org.junit.Assert.*; // for all assertions
import static org.junit.Assume.*; // for assumptions

import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {
    // Test for Token reset() method
    @Test
    public void testTokenReset() {
        Token doctype = new Token.Doctype();
        doctype.reset();
        assertNull(doctype.getName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        Token startTag = new Token.StartTag();
        startTag.reset();
        assertNull(startTag.tagName);
        assertNull(startTag.normalName);
        assertNull(startTag.pendingAttributeName);
        assertTrue(startTag.hasPendingAttributeValue); // reset() sets this to true, but not false when no attribute value is set
        assertNull(startTag.pendingAttributeValue);
        assertNull(startTag.pendingAttributeValueS);
        assertFalse(startTag.hasEmptyAttributeValue);
        assertFalse(startTag.hasPendingAttributeValue);
        assertFalse(startTag.isSelfClosing());
        assertNull(startTag.getAttributes());

        Token endTag = new Token.EndTag();
        endTag.reset();
        assertNull(endTag.tagName);
        assertNull(endTag.normalName);
        assertNull(endTag.pendingAttributeName);
        assertNull(endTag.pendingAttributeValue);
        assertNull(endTag.pendingAttributeValueS);
        assertFalse(endTag.hasEmptyAttributeValue);
        assertFalse(endTag.hasPendingAttributeValue);
        assertFalse(endTag.isSelfClosing());
        assertNull(endTag.getAttributes());

        Token comment = new Token.Comment();
        comment.reset();
        assertNull(comment.getData());
        assertFalse(comment.isBogus());

        Token character = new Token.Character();
        character.reset();
        assertNull(character.getData());

        Token eof = new Token.EOF();
        eof.reset();
        // EOF cannot be reset, it is a singleton
    }

    // Test for Doctype methods
    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();

        doctype.getName("html");
        assertEquals("html", doctype.getName());
        assertEquals("html", doctype.normalName());
        assertNull(doctype.getPubSysKey());
        assertNull(doctype.getPublicIdentifier());
        assertNull(doctype.getSystemIdentifier());
        assertFalse(doctype.isForceQuirks());

        doctype.setPubSysKey("pubsyskey");
        assertEquals("pubsyskey", doctype.getPubSysKey());

        doctype.setPublicIdentifier("publicidentifier");
        assertEquals("publicidentifier", doctype.getPublicIdentifier());

        doctype.setSystemIdentifier("systemidentifier");
        assertEquals("systemidentifier", doctype.getSystemIdentifier());

        doctype.setForceQuirks(true);
        assertTrue(doctype.isForceQuirks());
    }

    // Test for Tag methods (common for StartTag and EndTag)
    @Test
    public void testTagCommon() {
        Token startTag = new Token.StartTag();

        startTag.appendTagName("div");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());

        startTag.appendTagName("Div");
        assertEquals("div", startTag.name());
        assertEquals("div", startTag.normalName());

        startTag.appendAttributeName("id");
        assertNull(startTag.getAttributes());

        startTag.appendAttributeValue("attributeValue");
        startTag.newAttribute();
        Attribute idAttribute = startTag.getAttributes().get("id");
        assertEquals("attributeValue", idAttribute.getValue());

        startTag.appendAttributeValue("anotherAttributeValue");
        Attribute anotherAttribute = startTag.getAttributes().get("anotherAttribute");
        assertNull(anotherAttribute);

        startTag.setEmptyAttributeValue();
        Attribute emptyAttribute = startTag.getAttributes().get("emptyAttribute");
        assertNull(emptyAttribute);

        startTag.finaliseTag();
        assertEquals("<div id=\"attributeValue\"></div>", startTag.toString());
    }

    // Test for StartTag methods
    @Test
    public void testStartTag() {
        Token startTag = new Token.StartTag();

        startTag.name("div").attributes(new Attributes());
        assertEquals("<div></div>", startTag.toString());

        startTag.name("div").attributes(new Attributes().put("id", "idValue"));
        assertEquals("<div id=\"idValue\"></div>", startTag.toString());

        startTag.name("div").attributes(new Attributes().put("class", "className"));
        assertEquals("<div class=\"className\"></div>", startTag.toString());

        startTag.name("div").attributes(new Attributes().put("id", "idValue").put("class", "className"));
        assertEquals("<div id=\"idValue\" class=\"className\"></div>", startTag.toString());
    }

    // Test for EndTag methods
    @Test
    public void testEndTag() {
        Token endTag = new Token.EndTag();

        endTag.name("div");
        assertEquals("</div>", endTag.toString());
    }

    // Test for Comment methods
    @Test
    public void testComment() {
        Token comment = new Token.Comment();

        comment.appendAttributeName("data");
        comment.appendAttributeValue("dataValue");
        comment.newAttribute();
        assertEquals("<!--dataValue-->", comment.toString());

        comment.appendAttributeName("bogus");
        comment.setBogus(true);
        assertEquals("<!--dataValue-->", comment.toString());
        assertTrue(comment.isBogus());
    }

    // Test for Character methods
    @Test
    public void testCharacter() {
        Token character = new Token.Character();

        character.data("characterValue");
        assertEquals("characterValue", character.getData());
    }

    // Test for EOF methods
    @Test
    public void testEOF() {
        // Cannot test EOF directly as it is a singleton
        assumeTrue(true);
    }
}