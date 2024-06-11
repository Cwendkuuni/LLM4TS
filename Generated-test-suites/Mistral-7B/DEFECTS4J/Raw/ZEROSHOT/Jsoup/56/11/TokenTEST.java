import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokenTest {

    @Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());
        assertFalse(doctypeToken.isDoctype());
        assertFalse(doctypeToken.isStartTag());
        assertFalse(doctypeToken.isEndTag());
        assertFalse(doctypeToken.isComment());
        assertFalse(doctypeToken.isCharacter());
        assertFalse(doctypeToken.isEOF());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals(doctypeToken, resetDoctypeToken);

        String name = "html";
        doctypeToken.name(name);
        Token resetNamedDoctypeToken = doctypeToken.reset();
        assertEquals(name, resetNamedDoctypeToken.asDoctype().getName());

        String publicIdentifier = "public id";
        doctypeToken.systemIdentifier("system id");
        doctypeToken.publicIdentifier(publicIdentifier);
        Token resetPublicIdentifierDoctypeToken = doctypeToken.reset();
        assertEquals(publicIdentifier, resetPublicIdentifierDoctypeToken.asDoctype().getPublicIdentifier());

        assertTrue(doctypeToken.isDoctype());
        assertSame(doctypeToken, doctypeToken.asDoctype());

        doctypeToken.isForceQuirks(true);
        assertSame(doctypeToken, doctypeToken.asDoctype());
        assertTrue(doctypeToken.asDoctype().isForceQuirks());

        doctypeToken.reset();
        assertFalse(doctypeToken.isForceQuirks());
    }

    @Test
    public void testTagToken() {
        Token tagToken = new Token.StartTag();
        assertEquals("StartTag", tagToken.tokenType());
        assertFalse(tagToken.isDoctype());
        assertFalse(tagToken.isStartTag());
        assertFalse(tagToken.isEndTag());
        assertFalse(tagToken.isComment());
        assertFalse(tagToken.isCharacter());
        assertFalse(tagToken.isEOF());

        Token resetTagToken = tagToken.reset();
        assertEquals(tagToken, resetTagToken);

        String tagName = "div";
        tagToken.name(tagName);
        Token resetNamedTagToken = tagToken.reset();
        assertEquals(tagName, resetNamedTagToken.asStartTag().getName());

        tagToken.name("div").attributes(new Attributes().put("class", "class1 class2"));
        Token resetNamedWithAttributesTagToken = tagToken.reset();
        assertEquals("class1 class2", resetNamedWithAttributesTagToken.asStartTag().getAttributes().get("class"));

        tagToken.finaliseTag();
        assertFalse(tagToken.isStartTag());
        assertTrue(tagToken.isEndTag());
        assertFalse(tagToken.hasPendingAttributeValue());

        tagToken.appendTagName("div").appendAttributeName("class").appendAttributeValue("class1").appendAttributeValue("class2");
        Token resetAppendedAttributesTagToken = tagToken.reset();
        assertEquals("div class1 class2", resetAppendedAttributesTagToken.asEndTag().getName());
        assertEquals("class1 class2", resetAppendedAttributesTagToken.asEndTag().getAttributes().toString());

        tagToken.setEmptyAttributeValue();
        assertFalse(tagToken.hasEmptyAttributeValue());

        tagToken.type = TokenType.EndTag;
        assertFalse(tagToken.isStartTag());
        assertTrue(tagToken.isEndTag());

        assertTrue(tagToken.isTag());
        assertSame(tagToken, tagToken.asTag());

        tagToken.reset();
        assertFalse(tagToken.isTag());
    }

    @Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        assertEquals("Comment", commentToken.tokenType());
        assertFalse(commentToken.isDoctype());
        assertFalse(commentToken.isStartTag());
        assertFalse(commentToken.isEndTag());
        assertFalse(commentToken.isComment());
        assertFalse(commentToken.isCharacter());
        assertFalse(commentToken.isEOF());

        Token resetCommentToken = commentToken.reset();
        assertEquals(commentToken, resetCommentToken);

        String data = "This is a comment.";
        commentToken.data(data);
        Token resetDataCommentToken = commentToken.reset();
        assertEquals(data, resetDataCommentToken.asComment().getData());

        assertTrue(commentToken.isComment());
        assertSame(commentToken, commentToken.asComment());

        commentToken.reset();
        assertFalse(commentToken.getData().isEmpty());
        assertFalse(commentToken.isComment());
    }

    @Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        assertEquals("Character", characterToken.tokenType());
        assertFalse(characterToken.isDoctype());
        assertFalse(characterToken.isStartTag());
        assertFalse(characterToken.isEndTag());
        assertFalse(characterToken.isComment());
        assertFalse(characterToken.isCharacter());
        assertFalse(characterToken.isEOF());

        Token resetCharacterToken = characterToken.reset();
        assertEquals(characterToken, resetCharacterToken);

        String data = "\n";
        characterToken.data(data);
        Token resetDataCharacterToken = characterToken.reset();
        assertEquals(data, resetDataCharacterToken.asCharacter().getData());

        assertTrue(characterToken.isCharacter());
        assertSame(characterToken, characterToken.asCharacter());

        characterToken.reset();
        assertFalse(characterToken.getData().isEmpty());
        assertFalse(characterToken.isCharacter());
    }

    @Test
    public void testEofToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());
        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertTrue(eofToken.isEOF());

        Token resetEOFToken = eofToken.reset();
        assertEquals(eofToken, resetEOFToken);

        assertFalse(eofToken.isDoctype());
        assertFalse(eofToken.isStartTag());
        assertFalse(eofToken.isEndTag());
        assertFalse(eofToken.isComment());
        assertFalse(eofToken.isCharacter());
        assertFalse(eofToken.isEOF());

        assertTrue(eofToken.isEOF());
        assertSame(eofToken, eofToken.asEOF());
    }
}