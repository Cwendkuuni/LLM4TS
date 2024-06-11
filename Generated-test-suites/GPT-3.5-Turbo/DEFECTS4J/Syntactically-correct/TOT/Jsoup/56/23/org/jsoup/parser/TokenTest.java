
package org.jsoup.parser;

import org.junit.Assert;
import org.junit.Test;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token.Doctype doctype = new Token.Doctype();

        // Test reset method
        doctype.name.append("HTML");
        doctype.reset();
        Assert.assertEquals("", doctype.getName());
        Assert.assertEquals("", doctype.getPublicIdentifier());
        Assert.assertEquals("", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());

        // Test accessor methods
        doctype.name.append("HTML");
        doctype.publicIdentifier.append("-//W3C//DTD HTML 4.01 Transitional//EN");
        doctype.systemIdentifier.append("http://www.w3.org/TR/html4/loose.dtd");
        Assert.assertEquals("HTML", doctype.getName());
        Assert.assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", doctype.getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/html4/loose.dtd", doctype.getSystemIdentifier());
        Assert.assertFalse(doctype.isForceQuirks());
    }

    @Test
    public void testStartTag() {
        Token.StartTag startTag = new Token.StartTag();
        Attributes attributes = new Attributes();
        startTag.attributes = attributes;

        // Test reset method
        startTag.tagName = "div";
        startTag.normalName = "div";
        startTag.pendingAttributeName = "id";
        startTag.reset();
        Assert.assertNull(startTag.tagName);
        Assert.assertNull(startTag.normalName);
        Assert.assertNull(startTag.pendingAttributeName);
        Assert.assertEquals("", startTag.pendingAttributeValue.toString());
        Assert.assertNull(startTag.pendingAttributeValueS);
        Assert.assertFalse(startTag.hasEmptyAttributeValue);
        Assert.assertFalse(startTag.hasPendingAttributeValue);
        Assert.assertFalse(startTag.selfClosing);
        Assert.assertNull(startTag.attributes);

        // Test accessor methods
        startTag.nameAttr("div", attributes);
        Assert.assertEquals("div", startTag.tagName);
        Assert.assertEquals("div", startTag.normalName());
        Assert.assertTrue(startTag.isSelfClosing());
        Assert.assertNotNull(startTag.getAttributes());
        Assert.assertEquals("<div>", startTag.toString());
    }

    @Test
    public void testEndTag() {
        Token.EndTag endTag = new Token.EndTag();

        // Test reset method
        endTag.tagName = "div";
        endTag.normalName = "div";
        endTag.reset();
        Assert.assertNull(endTag.tagName);
        Assert.assertNull(endTag.normalName);

        // Test toString method
        Assert.assertEquals("</div>", endTag.toString());
    }

    @Test
    public void testComment() {
        Token.Comment comment = new Token.Comment();

        // Test reset method
        comment.data.append("This is a comment");
        comment.reset();
        Assert.assertEquals("", comment.getData());
        Assert.assertFalse(comment.bogus);

        // Test accessor methods
        comment.data.append("This is a comment");
        Assert.assertEquals("This is a comment", comment.getData());
        Assert.assertFalse(comment.bogus);
        Assert.assertEquals("<!--This is a comment-->", comment.toString());
    }

    @Test
    public void testCharacter() {
        Token.Character character = new Token.Character();

        // Test reset method
        character.data("Some character data");
        character.reset();
        Assert.assertNull(character.getData());

        // Test accessor methods
        character.data("Some character data");
        Assert.assertEquals("Some character data", character.getData());
        Assert.assertEquals("Some character data", character.toString());
    }

    @Test
    public void testEOF() {
        Token.EOF eof = new Token.EOF();

        // Test reset method
        Token.EOF resetEOF = (Token.EOF) eof.reset();
        Assert.assertSame(eof, resetEOF);

        // Test accessor methods
        Assert.assertTrue(eof.isEOF());
    }
}
