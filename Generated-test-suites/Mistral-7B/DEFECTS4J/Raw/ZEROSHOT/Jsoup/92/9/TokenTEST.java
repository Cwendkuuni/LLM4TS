import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token docTypeToken = new Token.Doctype();
        assertEquals("Doctype", docTypeToken.tokenType());

        Token resetDocTypeToken = docTypeToken.reset();
        assertNotSame(docTypeToken, resetDocTypeToken);
        assertEquals("Doctype", resetDocTypeToken.tokenType());
    }

    @org.junit.Test
    public void testDoctypeTokenAttributes() {
        Token docTypeToken = new Token.Doctype();
        docTypeToken.asDoctype().getName("html");
        docTypeToken.asDoctype().getPublicIdentifier().append("public");
        docTypeToken.asDoctype().getSystemIdentifier().append("system");
        docTypeToken.asDoctype().isForceQuirks(true);

        assertEquals("Doctype", docTypeToken.tokenType());
        assertEquals("html", docTypeToken.asDoctype().getName());
        assertNull(docTypeToken.asDoctype().getPubSysKey());
        assertEquals("public", docTypeToken.asDoctype().getPublicIdentifier().toString());
        assertEquals("system", docTypeToken.asDoctype().getSystemIdentifier().toString());
        assertTrue(docTypeToken.asDoctype().isForceQuirks());

        Token resetDocTypeToken = docTypeToken.reset();
        assertNotSame(docTypeToken, resetDocTypeToken);
        assertEquals("Doctype", resetDocTypeToken.tokenType());
        assertNull(resetDocTypeToken.asDoctype().getName());
        assertNull(resetDocTypeToken.asDoctype().getPubSysKey());
        assertNull(resetDocTypeToken.asDoctype().getPublicIdentifier());
        assertNull(resetDocTypeToken.asDoctype().getSystemIdentifier());
        assertFalse(resetDocTypeToken.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("tagName");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("tagName", startTagToken.asStartTag().name());

        Token resetStartTagToken = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTagToken);
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertNull(resetStartTagToken.asStartTag().name());
    }

    @org.junit.Test
    public void testStartTagTokenAttributes() {
        Token startTagToken = new Token.StartTag();
        startTagToken.asStartTag().name("tagName").attributes().put("attrName", "attrValue");

        assertEquals("StartTag", startTagToken.tokenType());
        assertEquals("tagName", startTagToken.asStartTag().name());
        assertEquals("attrValue", startTagToken.asStartTag().getAttributes().get("attrName"));

        Token resetStartTagToken = startTagToken.reset();
        assertNotSame(startTagToken, resetStartTagToken);
        assertEquals("StartTag", resetStartTagToken.tokenType());
        assertNull(resetStartTagToken.asStartTag().name());
        assertNull(resetStartTagToken.asStartTag().getAttributes());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag();
        endTagToken.asEndTag().name("tagName");

        assertEquals("EndTag", endTagToken.tokenType());
        assertEquals("tagName", endTagToken.asEndTag().name());

        Token resetEndTagToken = endTagToken.reset();
        assertNotSame(endTagToken, resetEndTagToken);
        assertEquals("EndTag", resetEndTagToken.tokenType());
        assertNull(resetEndTagToken.asEndTag().name());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment();
        commentToken.asComment().getData().append("commentData");

        assertEquals("Comment", commentToken.tokenType());
        assertEquals("commentData", commentToken.asComment().getData().toString());

        Token resetCommentToken = commentToken.reset();
        assertNotSame(commentToken, resetCommentToken);
        assertEquals("Comment", resetCommentToken.tokenType());
        assertNull(resetCommentToken.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character();
        characterToken.asCharacter().getData().append('c');

        assertEquals("Character", characterToken.tokenType());
        assertEquals('c', characterToken.asCharacter().getData().charAt(0));

        Token resetCharacterToken = characterToken.reset();
        assertNotSame(characterToken, resetCharacterToken);
        assertEquals("Character", resetCharacterToken.tokenType());
        assertNull(resetCharacterToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testCDataToken() {
        Token cDataToken = new Token.CData("cDataContent");

        assertEquals("Character", cDataToken.tokenType());
        assertEquals("cDataContent", cDataToken.asCharacter().getData().toString());

        Token resetCDataToken = cDataToken.reset();
        assertNotSame(cDataToken, resetCDataToken);
        assertEquals("Character", resetCDataToken.tokenType());
        assertNull(resetCDataToken.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEofToken = eofToken.reset();
        assertSame(eofToken, resetEofToken);
        assertEquals("EOF", resetEofToken.tokenType());
    }
}