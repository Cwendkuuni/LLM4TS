import org.junit.Test;
import static org.junit.Assert.*;
import org.jsoup.parser.Tokeniser;
import org.jsoup.helper.CharacterInput;

public class TokeniserTest {

    @Test
    public void testReadCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("abc"));
        char c = tokeniser.read().getType();
        assertEquals(Token.TokenType.Character, c);
        assertEquals('a', tokeniser.read().getData());
    }

    @Test
    public void testReadStartTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p>"));
        Token startTag = tokeniser.read();
        assertTrue(startTag instanceof Token.StartTag);
        assertEquals("p", startTag.getData());
    }

    @Test
    public void testReadEndTag() {
        Tokeniser tokeniser = new CharacterInput("</p>");
        Token endTag = tokeniser.read();
        assertTrue(endTag instanceof Token.EndTag);
        assertEquals("p", endTag.getData());
    }

    @Test
    public void testUnexpectedCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("\u0001")); // Control character
        assertFalse(tokeniser.read() instanceof Token.Character);
        assertEquals(Token.TokenType.Error, tokeniser.read().getType());
    }

    @Test
    public void testInvalidCharacterReference() {
        Tokeniser tokeniser = new CharacterInput("#XYZ;"); // Invalid character reference
        assertFalse(tokeniser.read() instanceof Token.Character);
        assertEquals(Token.TokenType.Error, tokeniser.read().getType());
    }

    @Test
    public void testMismatchedTags() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p><script></p>")); // Mismatched script tag
        assertFalse(tokeniser.read() instanceof Token.EndTag);
        assertEquals(Token.TokenType.Error, tokeniser.read().getType());
    }

    @Test
    public void testEmptyTag() {
        Tokeniser tokeniser = new CharacterInput("<img />"); // Self-closing empty tag
        Token tag = tokeniser.read();
        assertTrue(tag instanceof Token.StartTag);
        assertEquals("img", tag.getData());
        assertTrue(tag.selfClosing);
    }

    @Test
    public void testEOF() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("")); // Empty input stream
        assertEquals(Token.TokenType.Eof, tokeniser.read().getType());
    }

    @Test
    public void testConsumeCharacterReference() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("&amp;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', result.charValue());
    }

    @Test
    public void testConsumeCharacterReferenceWithAdditionalAllowedCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("&amp;#x0A;"));
        Character result = tokeniser.consumeCharacterReference('#', true);
        assertEquals('\n', result.charValue());
    }

    @Test
    public void testConsumeCharacterReferenceWithInvalidCharacter() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("&xyz;"));
        Character result = tokeniser.consumeCharacterReference(null, false);
        assertEquals(replacementChar, result.charValue());
    }

    @Test
    public void testConsumeCharacterReferenceWithMismatchedEndTag() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p><script>&xyz;</script></p>"));
        Token tag = tokeniser.read();
        assertFalse(tag instanceof Token.EndTag);
        assertEquals(Token.TokenType.Error, tag.getType());
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p>"));
        Token.Tag tag = tokeniser.createTagPending(true);
        assertEquals("p", tag.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        Tokeniser tokeniser = new CharacterInput("<!-- My comment -->");
        Token.Comment comment = tokeniser.createCommentPending();
        assertEquals("My comment", comment.getData());
    }

    @Test
    public void testCreateDoctypePending() {
        Tokeniser tokeniser = new CharacterInput("<!DOCTYPE html>");
        Token.Doctype doctype = tokeniser.createDoctypePending();
        assertEquals("html", doctype.name);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p><p></p>"));
        assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagTokenWithMismatchedTags() {
        Tokeniser tokeniser = new Tokeniser(new CharacterInput("<p><script></p>"));
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}