package org.jsoup.parser;

import static org.junit.Assert.*;
import org.jsoup.nodes.Attributes;
import org.jsoup.nodes.Element;

public class TokenTest {

    @Test
    public void testDoctype() {
        Token doctype = new Token.Doctype();
        assertEquals("Doctype", doctype.tokenType());

        Token doctypeWithName = new Token.Doctype();
        doctypeWithName.asDoctype().name.append("html");
        assertEquals("Doctype[html]", doctypeWithName.tokenType());

        Token doctypeWithPublicIdentifier = new Token.Doctype();
        doctypeWithPublicIdentifier.asDoctype().publicIdentifier.append("_public");
        assertEquals("Doctype[html,_public]", doctypeWithPublicIdentifier.tokenType());

        Token doctypeWithSystemIdentifier = new Token.Doctype();
        doctypeWithSystemIdentifier.asDoctype().systemIdentifier.append("/system.html");
        assertEquals("Doctype[html,_public]/system.html", doctypeWithSystemIdentifier.tokenType());

        Token doctypeWithForceQuirks = new Token.Doctype();
        doctypeWithForceQuirks.asDoctype().forceQuirks = true;
        assertEquals("Doctype[html,_public]/system.html[forceQuirks]", doctypeWithForceQuirks.tokenType());
    }

    @Test
    public void testTag() {
        Token tag = new Token.Tag();
        assertEquals("Tag", tag.tokenType());

        Token startTag = new Token.StartTag();
        startTag.asStartTag().name.append("div");
        assertEquals("StartTag[div]", startTag.tokenType());

        Token startTagWithAttributes = new Token.StartTag();
        startTagWithAttributes.asStartTag().name.append("div");
        startTagWithAttributes.asStartTag().newAttribute();
        startTagWithAttributes.asStartTag().appendAttributeName("class").appendAttributeValue("container");
        assertEquals("StartTag[div][class=container]", startTagWithAttributes.tokenType());

        Token endTag = new Token.EndTag();
        endTag.asEndTag().name.append("div");
        assertEquals("EndTag[div]", endTag.tokenType());

        Token characterData = new Token.Character('a');
        assertEquals("Character[a]", characterData.tokenType());
    }

    @Test
    public void testComment() {
        Token comment = new Token.Comment();
        assertEquals("Comment", comment.tokenType());

        Token commentWithData = new Token.Comment();
        commentWithData.asComment().getData().append("This is a comment");
        assertEquals("Comment[This is a comment]", commentWithData.tokenType());
    }

    @Test
    public void testEOF() {
        Token eof = new Token.EOF();
        assertEquals("EOF", eof.tokenType());
    }

    @Test
    public void testTokenTypes() {
        TokenType[] tokenTypes = Token.TokenType.values();
        for (TokenType tokenType : tokenTypes) {
            Token token = new Token();
            token.type = tokenType;
            assertEquals(tokenType.toString(), token.tokenType());
        }
    }

    @Test
    public void testTokenCreation() {
        // Test creating various tokens
        assertEquals(TokenType.Doctype, new Token.Doctype().type);
        assertEquals(TokenType.StartTag, new Token.StartTag().type);
        assertEquals(TokenType.EndTag, new Token.EndTag().type);
        assertEquals(TokenType.Comment, new Token.Comment().type);
        assertEquals(TokenType.Character, new Token.Character("a").type);
        assertEquals(TokenType.EOF, new Token.EOF().type);

        // Test creating subclassed tokens
        assertEquals(TokenType.StartTag, new Token.StartTag("div").type);
        assertEquals(TokenType.EndTag, new Token.EndTag("div").type);
    }

    @Test
    public void testGetAttributes() {
        Token startTag = new Token.StartTag();
        startTag.asStartTag().newAttribute();
        startTag.asStartTag().appendAttributeName("class").appendAttributeValue("container");
        Attributes attributes = startTag.asStartTag().getAttributes();
        assertEquals(1, attributes.size());
        assertEquals("container", attributes.get("class").getValue());
    }
}