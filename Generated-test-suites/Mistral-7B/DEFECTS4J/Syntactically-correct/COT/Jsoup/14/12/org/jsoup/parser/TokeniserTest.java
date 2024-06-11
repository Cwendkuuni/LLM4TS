import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;

public class TokeniserTest {

    @Test
    public void testTokeniserConstructor() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("Test input".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testEmptyInput() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new byte[0]);
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadCharacter() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("abc".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        Token token = tokeniser.read();
        Assert.assertEquals("a", token.getData());
        token = tokeniser.read();
        Assert.assertEquals("b", token.getData());
        token = tokeniser.read();
        Assert.assertEquals("c", token.getData());
        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadWillReturnPendingToken() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("ab<script>cd".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.emit(new Character("a"));
        tokeniser.emit(new Character("b"));
        tokeniser.createTempBuffer();
        tokeniser.emit(new Token.StartTag("script"));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('d'));
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.getType());
        Assert.assertEquals("script", token.getData());
        token = tokeniser.read();
        Assert.assertEquals("", token.getData()); // empty string token for <script> tag
    }

    @Test
    public void testReadWillReturnPendingEndTag() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("ab<script></script>cd".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.emit(new Character("a"));
        tokeniser.emit(new Character("b"));
        tokeniser.createTempBuffer();
        tokeniser.emit(new Token.StartTag("script"));
        tokeniser.createTempBuffer();
        tokeniser.emit(new Token.EndTag("script"));
        tokeniser.emit(new Character("c"));
        tokeniser.emit(new Character("d"));
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.EndTag, token.getType());
        Assert.assertEquals("script", token.getData());
        token = tokeniser.read();
        Assert.assertEquals("", token.getData()); // empty string token for </script> tag
    }

    @Test
    public void testReadWillReturnPendingComment() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("--comment".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createCommentPending();
        tokeniser.emit(new Character('--'));
        tokeniser.emit(new Character('c'));
        tokeniser.emit(new Character('o'));
        tokeniser.emit(new Character('m'));
        tokeniser.emit(new Character('m'));
        tokeniser.emit(new Character('e'));
        tokeniser.emit(new Character('n'));
        tokeniser.emit(new Character('t'));
        tokeniser.emit(new Character(' '));
        tokeniser.emit(new Character('>'));
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.getType());
        Assert.assertTrue(token.getComment().getText().contains("comment"));
    }

    @Test
    public void testReadWillReturnPendingDoctype() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<!doctype html>".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.createDoctypePending();
        tokeniser.emit(new Token.StartTag("!"));
        tokeniser.emit(new Character("d"));
        tokeniser.emit(new Character("o"));
        tokeniser.emit(new Character("c"));
        tokeniser.emit(new Character("t"));
        tokeniser.emit(new Character("y"));
        tokeniser.emit(new Character("p"));
        tokeniser.emit(new Character("e"));
        tokeniser.emit(new Character(" "));
        tokeniser.emit(new Character("h"));
        tokeniser.emit(new Character("t"));
        tokeniser.emit(new Character("m"));
        tokeniser.emit(new Character("l"));
        tokeniser.emit(new Character(">"));
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, token.getType());
        Assert.assertTrue(token.getDoctype().getText().contains("html"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testReadWithInvalidInput() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("ab>cd".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
    }

    @Test
    public void testConsumeCharacterReference() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("&#123;".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        Character character = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('\u007B', character.charValue());
        character = tokeniser.consumeCharacterReference('#', false);
        Assert.assertNull(character);

        character = tokeniser.consumeCharacterReference(null, true);
        Assert.assertEquals(Tokeniser.replacementChar, character.charValue());
        character = tokeniser.consumeCharacterReference("amp", true);
        Assert.assertEquals('&', character.charValue());
    }

    @Test
    public void testErrorHandling() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream("<html><body><script>eval(123)</script></body></html>".getBytes());
        CharacterReader reader = new InputStreamReader(inputStream);
        Tokeniser tokeniser = new Tokeniser(reader);
        List<ParseError> errors = new ArrayList<>();
        tokeniser.setTrackErrors(true);
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        tokeniser.read();
        Assert.assertEquals(6, errors.size());
        Assert.assertEquals("Unexpected character in input: (char) 123 at column 1", errors.get(0).getMessage());
        Assert.assertEquals("Unexpected character in input: (char) < at column 11", errors.get(1).getMessage());
        Assert.assertEquals("Unexpected character in input: (char) s at column 13", errors.get(2).getMessage());
        Assert.assertEquals("Unexpected character in input: (char) c at column 14", errors.get(3).getMessage());
        Assert.assertEquals("Unexpected character in input: (char) r at column 15", errors.get(4).getMessage());
        Assert.assertEquals("Unexpected character in input: (char) i at column 16", errors.get(5).getMessage());
    }
}