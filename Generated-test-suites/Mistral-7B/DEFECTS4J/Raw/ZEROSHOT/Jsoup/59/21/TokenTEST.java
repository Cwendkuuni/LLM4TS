import static org.junit.Assert.*;
import org.jsoup.parser.Token;
import org.jsoup.parser.TokenType;

public class TokenTest {

    @org.junit.Test
    public void testDoctypeToken() {
        Token doctypeToken = new Token.Doctype();
        assertEquals("Doctype", doctypeToken.tokenType());

        Token resetDoctypeToken = doctypeToken.reset();
        assertEquals("Doctype", resetDoctypeToken.tokenType());

        TokenType doctypeType = doctypeToken.isDoctype();
        assertTrue(doctypeType == TokenType.Doctype);

        // Test getters for Doctype token
        Token doctypeTokenWithName = new Token.Doctype().name("html5");
        assertEquals("Doctype", doctypeTokenWithName.tokenType());
        assertEquals("html5", doctypeTokenWithName.asDoctype().getName());
        assertNull(doctypeTokenWithName.asDoctype().getPubSysKey());
        assertNull(doctypeTokenWithName.asDoctype().getPublicIdentifier());
        assertNull(doctypeTokenWithName.asDoctype().getSystemIdentifier());
        assertFalse(doctypeTokenWithName.asDoctype().isForceQuirks());
    }

    @org.junit.Test
    public void testStartTagToken() {
        Token startTagToken = new Token.StartTag().name("div");
        assertEquals("StartTag", startTagToken.tokenType());

        Token resetStartTagToken = startTagToken.reset();
        assertEquals("StartTag", resetStartTagToken.tokenType());

        TokenType startTagType = startTagToken.isStartTag();
        assertTrue(startTagType == TokenType.StartTag);

        // Test getters and setters for StartTag token
        Token startTagTokenWithAttributes = new Token.StartTag().name("p").attribute("class", "testClass");
        assertEquals("StartTag", startTagTokenWithAttributes.tokenType());
        assertEquals("p", startTagTokenWithAttributes.asStartTag().name());
        assertEquals("class", startTagTokenWithAttributes.asStartTag().getAttributes().getClass("testClass").getName());

        Token startTagTokenWithEmptyAttribute = new Token.StartTag().name("img").attribute("src", "").attribute("alt", "");
        assertEquals("StartTag", startTagTokenWithEmptyAttribute.tokenType());
        assertEquals("img", startTagTokenWithEmptyAttribute.asStartTag().name());
        assertEquals(2, startTagTokenWithEmptyAttribute.asStartTag().getAttributes().size());
        assertTrue(startTagTokenWithEmptyAttribute.asStartTag().getAttributes().hasKey("src"));
        assertTrue(startTagTokenWithEmptyAttribute.asStartTag().getAttributes().hasKey("alt"));
        assertTrue(startTagTokenWithEmptyAttribute.asStartTag().getAttributes().get("src").getValue().isEmpty());
        assertTrue(startTagTokenWithEmptyAttribute.asStartTag().getAttributes().get("alt").getValue().isEmpty());
    }

    @org.junit.Test
    public void testEndTagToken() {
        Token endTagToken = new Token.EndTag().name("p");
        assertEquals("EndTag", endTagToken.tokenType());

        Token resetEndTagToken = endTagToken.reset();
        assertEquals("EndTag", resetEndTagToken.tokenType());

        TokenType endTagType = endTagToken.isEndTag();
        assertTrue(endTagType == TokenType.EndTag);

        // Test getters for EndTag token
        Token endTagTokenWithName = new Token.EndTag().name("div");
        assertEquals("EndTag", endTagTokenWithName.tokenType());
        assertEquals("div", endTagTokenWithName.asEndTag().name());
    }

    @org.junit.Test
    public void testCommentToken() {
        Token commentToken = new Token.Comment().data("This is a test comment.");
        assertEquals("Comment", commentToken.tokenType());

        Token resetCommentToken = commentToken.reset();
        assertEquals("Comment", resetCommentToken.tokenType());

        TokenType commentType = commentToken.isComment();
        assertTrue(commentType == TokenType.Comment);

        // Test getters for Comment token
        Token commentTokenWithData = new Token.Comment().data("<!-- Test comment -->");
        assertEquals("Comment", commentTokenWithData.tokenType());
        assertEquals("Test comment", commentTokenWithData.asComment().getData());
    }

    @org.junit.Test
    public void testCharacterToken() {
        Token characterToken = new Token.Character().data("&quot;");
        assertEquals("Character", characterToken.tokenType());

        Token resetCharacterToken = characterToken.reset();
        assertEquals("Character", resetCharacterToken.tokenType());

        TokenType characterType = characterToken.isCharacter();
        assertTrue(characterType == TokenType.Character);

        // Test getters for Character token
        Token characterTokenWithData = new Token.Character().data("&auml;");
        assertEquals("Character", characterTokenWithData.tokenType());
        assertEquals("Ä", characterTokenWithData.asCharacter().getData());
    }

    @org.junit.Test
    public void testEOFToken() {
        Token eofToken = new Token.EOF();
        assertEquals("EOF", eofToken.tokenType());

        Token resetEOFToken = eofToken.reset();
        assertEquals("EOF", resetEOFToken.tokenType());

        TokenType eofType = eofToken.isEOF();
        assertTrue(eofType == TokenType.EOF);
    }
}