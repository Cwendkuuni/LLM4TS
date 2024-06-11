import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.List;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private List<ParseError> errors;

    @BeforeEach
    public void setup() {
        reader = new CharacterReader(new ByteArrayInputStream("test data".getBytes()));
        tokeniser = new Tokeniser(reader);
        errors = tokeniser.getErrors();
    }

    @Test
    public void testReadNormalCase() {
        Token token;
        for (int i = 0; i < 10; i++) {
            token = tokeniser.read();
            Assertions.assertNotNull(token);
            Assertions.assertEquals(Token.TokenType.Character, token.type);
            Assertions.assertEquals("t", token.data);
        }
    }

    @Test
    public void testReadSelfClosingTag() {
        tokeniser.transition(TokeniserState.SelfClosingTag);
        tokeniser.emit(new Token.StartTag("img"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.EndTag("img"));

        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, token.type);
        Assertions.assertEquals("img", token.data);
    }

    @Test
    public void testReadEmptyElement() {
        tokeniser.transition(TokeniserState.EmptyTag);
        tokeniser.emit(new Token.StartTag("img"));
        tokeniser.emit(new Token.Character('>'));

        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, token.type);
        Assertions.assertEquals("img", token.data);
    }

    @Test
    public void testReadComment() {
        tokeniser.transition(TokeniserState.Comment);
        tokeniser.emit(new Token.Character('<'));
        tokeniser.emit(new Token.Character('!'));
        tokeniser.emit(new Token.Character('c'));
        tokeniser.emit(new Token.Character('o'));
        tokeniser.emit(new Token.Character('m'));
        tokeniser.emit(new Token.Character('m'));
        tokeniser.emit(new Token.Character('e'));
        tokeniser.emit(new Token.Character('n'));
        tokeniser.emit(new Token.Character('t'));
        tokeniser.emit(new Token.Character('>'));

        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.Comment, token.type);
        Assertions.assertEquals("<!--comment-->", token.data);
    }

    @Test
    public void testReadScriptTag() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.emit(new Token.StartTag("script"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('/'));
        tokeniser.emit(new Token.Character('s'));
        tokeniser.emit(new Token.Character('c'));
        tokeniser.emit(new Token.Character('r'));
        tokeniser.emit(new Token.Character('i'));
        tokeniser.emit(new Token.Character('p'));
        tokeniser.emit(new Token.Character('t'));
        tokeniser.emit(new Token.Character('>'));

        Token token = tokeniser.read();
        Assertions.assertEquals(Token.TokenType.EndTag, token.type);
        Assertions.assertEquals("script", token.data);
    }

    @Test
    public void testReadInvalidCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference(null, false);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Invalid character reference", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("x", false);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Invalid named character reference 'x'", errors.get(0).getMessage());
    }

    @Test
    public void testReadMissingSemicolon() {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("#123", false);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Missing semicolon", errors.get(0).getMessage());
    }

    @Test
    public void testReadInvalidHexCharacterReference() {
        tokeniser.transition(TokeniserState.CharacterReference);
        tokeniser.consumeCharacterReference("#\uFFF1", false);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Invalid Unicode point \uFFF1", errors.get(0).getMessage());
    }

    @Test
    public void testReadEOF() {
        tokeniser.transition(TokeniserState.Character);
        tokeniser.consumeCharacter();

        tokeniser.eofError(TokeniserState.Character);

        Assertions.assertEquals(1, errors.size());
        Assertions.assertEquals("Unexpectedly reached end of file (EOF)", errors.get(0).getMessage());
    }
}