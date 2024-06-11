import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

public class TokeniserTest {

    private CharacterReader reader;
    private Tokeniser tokeniser;
    private ParseErrorList errors;

    @Before
    public void setup() {
        reader = Mockito.mock(CharacterReader.class);
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyInput() {
        Mockito.when(reader.current()).thenReturn((char) 0);
        Mockito.when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertNull(token);
        Mockito.verify(reader, Mockito.times(1)).isEmpty();
    }

    @Test
    public void testReadCharacter() {
        String input = "abc";
        Mockito.when(reader.current()).thenReturn('a');
        Mockito.when(reader.consume()).thenReturn('b').thenReturn('c').thenReturn((char) 0);

        Token token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("a", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("b", token.data);

        token = tokeniser.read();
        assertEquals(Token.TokenType.Character, token.type);
        assertEquals("c", token.data);

        token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testReadSelfClosingTag() {
        String input = "<img src=\"image.jpg\" />";
        Mockito.when(reader.current()).thenReturn('<').thenReturn('i').thenReturn('m').thenReturn(' ').thenReturn('/').thenReturn('>');

        tokeniser.read(); // consume <
        tokeniser.read(); // consume img

        ArgumentCaptor<Token.StartTag> startTagCaptor = ArgumentCaptor.forClass(Token.StartTag.class);
        tokeniser.emit(Mockito.capture(startTagCaptor));
        Token.StartTag startTag = startTagCaptor.getValue();
        assertEquals("img", startTag.tagName);
        assertTrue(startTag.selfClosing);

        tokeniser.read(); // consume /
        tokeniser.read(); // consume >

        assertEquals(Token.TokenType.EndTag, tokeniser.read().type);
        assertEquals("img", tokeniser.read().data);
    }

    @Test
    public void testReadError() {
        String input = "<script type=\"text/javascript\">";
        Mockito.when(reader.current()).thenReturn('<').thenReturn('s').thenReturn('c').thenReturn('r').thenReturn('i').thenReturn('p').thenReturn('t')
                .thenReturn(' ').thenReturn('t').thenReturn('e').thenReturn('x').thenReturn('/').thenReturn('>');

        tokeniser.read(); // consume <
        tokeniser.read(); // consume script

        // should not read type or >
        assertFalse(tokeniser.getState().isScriptData());

        tokeniser.error(TokeniserState.Script);
        Mockito.verify(errors, Mockito.times(1)).add(Mockito.argThat(error -> error.getMessage().contains("Unexpected character 't' in input state [SCRIPT]")));

        tokeniser.read(); // consume >

        ArgumentCaptor<ParseError> errorCaptor = ArgumentCaptor.forClass(ParseError.class);
        tokeniser.read();
        Mockito.verify(errors, Mockito.times(2)).add(errorCaptor.capture());
        assertEquals("Unexpected character 't' in input state [SCRIPT]", errorCaptor.getValue().getMessage());
        assertEquals("Unexpected character '/' in input state [SCRIPT]", errorCaptor.getValue(1).getMessage());
    }

    @Test
    public void testReadEof() throws IOException {
        String input = "foo";
        InputStream stream = new ByteArrayInputStream(input.getBytes());
        Mockito.when(reader.inputStream()).thenReturn(stream);

        tokeniser.read(); // consume f
        tokeniser.read(); // consume o
        tokeniser.read(); // consume o
        tokeniser.read(); // consume e
        tokeniser.read(); // consume eof

        assertEquals(Token.TokenType.EndOfFile, tokeniser.read().type);
        assertEquals(Token.TokenType.EndOfFile, tokeniser.read().type);

        Mockito.verify(errors, Mockito.times(1)).eofError(TokeniserState.Data);
    }

    @Test
    public void testReadCharacterReference() {
        String input = "&aacute;";
        Mockito.when(reader.current()).thenReturn('&');

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('\u00E1', character);

        tokeniser.read(); // consume ;

        input = "&#x1F600;";
        Mockito.when(reader.current()).thenReturn('#').thenReturn('x').thenReturn(' ').thenReturn('1').thenReturn('F').thenReturn('6').thenReturn('0').thenReturn('.')
                .thenReturn(';');

        character = tokeniser.consumeCharacterReference('\uD835', false);
        assertEquals('\uD835\uDC00', character);

        input = "&amp; &lt; &gt;";
        Mockito.when(reader.current()).thenReturn('&').thenReturn('a').thenReturn('m').thenReturn('p').thenReturn(';').thenReturn(' ').thenReturn('&').thenReturn('<')
                .thenReturn(' ').thenReturn('>').thenReturn(';');

        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('&', character);
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('<', character);
        character = tokeniser.consumeCharacterReference(null, false);
        assertEquals('>', character);

        tokeniser.read(); // consume ;
        tokeniser.read(); // consume ;
        tokeniser.read(); // consume ;

        input = "&foo;";
        Mockito.when(reader.current()).thenReturn('&').thenReturn('f').thenReturn('o').thenReturn('o').thenReturn(';');

        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
        tokeniser.read(); // consume ;

        input = "&foo;bar";
        Mockito.when(reader.current()).thenReturn('&').thenReturn('f').thenReturn('o').thenReturn('o').thenReturn(';').thenReturn('b').thenReturn('a').thenReturn('r');

        character = tokeniser.consumeCharacterReference(null, false);
        assertNull(character);
        tokeniser.read(); // consume ;
        tokeniser.read(); // consume b
        tokeniser.read(); // consume a
        tokeniser.read(); // consume r
    }

    @Test
    public void testCreateAndEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token.StartTag startTag = Mockito.capture(tokeniser.tagPending);
        assertEquals("START_TAG", startTag.tagName);
        assertSame(tokeniser, startTag.parser);

        tokeniser.createTagPending(false);
        Token.EndTag endTag = Mockito.capture(tokeniser.tagPending);
        assertEquals("END_TAG", endTag.tagName);
        assertSame(tokeniser, endTag.parser);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertTrue(tokeniser.isAppropriateEndTagToken());
        tokeniser.createTagPending(false);
        assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testAppropriateEndTagName() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        assertEquals("img", tokeniser.appropriateEndTagName());
        tokeniser.createTagPending(false);
        assertNull(tokeniser.appropriateEndTagName());
    }
}