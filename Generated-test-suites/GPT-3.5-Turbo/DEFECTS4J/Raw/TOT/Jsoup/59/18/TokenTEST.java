import org.junit.Test;
import static org.junit.Assert.*;

public class TokenTest {

    @Test
    public void testTokenType() {
        Token token = new Token() {
            @Override
            Token reset() {
                return null;
            }
        };
        TokenType expected = Token.TokenType.EOF;
        token.type = expected;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeStartTag() {
        Token token = new StartTag();
        TokenType expected = Token.TokenType.StartTag;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeEndTag() {
        Token token = new EndTag();
        TokenType expected = Token.TokenType.EndTag;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeComment() {
        Token token = new Comment();
        TokenType expected = Token.TokenType.Comment;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeCharacter() {
        Token token = new Character();
        TokenType expected = Token.TokenType.Character;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testTokenTypeEOF() {
        Token token = new EOF();
        TokenType expected = Token.TokenType.EOF;

        TokenType actual = token.type();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetName() {
        Token.Doctype doctype = new Token.Doctype();
        String expected = "";

        String actual = doctype.getName();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetPubSysKey() {
        Token.Doctype doctype = new Token.Doctype();
        String expected = null;

        String actual = doctype.getPubSysKey();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetPublicIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        String expected = "";

        String actual = doctype.getPublicIdentifier();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeGetSystemIdentifier() {
        Token.Doctype doctype = new Token.Doctype();
        String expected = "";

        String actual = doctype.getSystemIdentifier();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeIsForceQuirks() {
        Token.Doctype doctype = new Token.Doctype();
        boolean expected = false;

        boolean actual = doctype.isForceQuirks();

        assertEquals(expected, actual);
    }

    @Test
    public void testDoctypeReset() {
        Token.Doctype doctype = new Token.Doctype();

        Token.Doctype actual = doctype.reset();

        assertNotNull(actual);
    }

    @Test
    public void testStartTagReset() {
        Token.StartTag startTag = new Token.StartTag();

        Token.StartTag actual = startTag.reset();

        assertNotNull(actual);
    }

    @Test
    public void testStartTagToStringNoAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("div");
        String expected = "<div>";

        String actual = startTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testStartTagToStringWithAttributes() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.name("a");
        Attributes attributes = new Attributes();
        attributes.put("href", "https://example.com");
        startTag.attributes = attributes;
        String expected = "<a href=\"https://example.com\">";

        String actual = startTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testEndTagToString() {
        Token.EndTag endTag = new Token.EndTag();
        endTag.name("div");
        String expected = "</div>";

        String actual = endTag.toString();

        assertEquals(expected, actual);
    }

    @Test
    public void testCommentGetData() {
        Token.Comment comment = new Token.Comment();
        String expected = "";

        String actual = comment.getData();

        assertEquals(expected, actual);
    }

    @Test
    public void testCharacterGetData() {
        Token.Character character = new Token.Character();
        character.data("abc");
        String expected = "abc";

        String actual = character.getData();

        assertEquals(expected, actual);
    }

    @Test
    public void testEOFReset() {
        Token.EOF eof = new Token.EOF();

        Token.EOF actual = eof.reset();

        assertNotNull(actual);
    }
}