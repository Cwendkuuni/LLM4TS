import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.Comment;
import org.jsoup.parser.Token.Doctype;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class TokeniserTest {
    private CharacterReader reader;
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        reader = mock(CharacterReader.class);
        tokeniser = new Tokeniser(reader);
    }

    @Test
    public void testConstructor() {
        assertNotNull(tokeniser);
        assertSame(reader, tokeniser.reader);
    }

    @Test
    public void testRead_InitialState() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('A');

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("A", token.getData());

        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testRead_EmitPending() {
        when(reader.isEmpty()).thenReturn(true);
        tokeniser.emit(new Token.Character("ABC"));

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("ABC", token.getData());

        assertTrue(tokeniser.isEmitPending());
    }

    @Test
    public void testRead_InvalidCharacterReference() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.current()).thenReturn('#');

        tokeniser.consumeCharacterReference((char) 0, false);
        tokeniser.error("State: Data");

        Token token = tokeniser.read();
        assertNull(token);

        verify(reader).rewindToMark();
    }

    @Test
    public void testRead_InvalidNamedCharacterReference() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.matchConsume(Character.isLetter())).thenReturn(true);
        when(reader.consumeLetter()).thenReturn('A');
        when(reader.consumeLetter()).thenReturn('B');
        when(reader.matchConsume(Character.isSemicolon())).thenReturn(false);

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.error("State: Data");

        Token token = tokeniser.read();
        assertNull(token);

        verify(reader).rewindToMark();
    }

    @Test
    public void testRead_ValidNamedCharacterReference() {
        when(reader.isEmpty()).thenReturn(false);
        when(reader.matchConsume(Character.isLetter())).thenReturn(true);
        when(reader.consumeLetter()).thenReturn('A');
        when(reader.consumeLetter()).thenReturn('M');
        when(reader.consumeLetter()).thenReturn('p');
        when(reader.consumeLetter()).thenReturn(';');

        Character character = tokeniser.consumeCharacterReference(null, false);
        assertEquals(Character.AMPERSAND, character);

        Token token = tokeniser.read();
        assertEquals(Token.Character.class, token.getClass());
        assertEquals("&", token.getData());
    }

    @Test
    public void testRead_EmptyInput() {
        when(reader.isEmpty()).thenReturn(true);

        Token token = tokeniser.read();
        assertNull(token);
    }

    @Test
    public void testEmit() {
        tokeniser.emit(new Token.Character("ABC"));
        Token expected = new Token.Character("ABC");

        Token token = tokeniser.read();
        assertEquals(expected.getClass(), token.getClass());
        assertEquals(expected.getData(), token.getData());

        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testEmit_SelfClosingTag() {
        tokeniser.transition(TokeniserState.TagStart);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.emit(new Token.Character(" "));
        tokeniser.emit(new Token.Character("/"));
        tokeniser.emit(new Token.Character("p"));

        Token token = tokeniser.read();
        assertEquals(Token.EndTag.class, token.getClass());
        assertEquals("p", token.getData().getName());
        assertEquals("", token.getData().getTagText());
        assertEquals(Token.TokenType.EndTag, token.getType());
        assertFalse(tokeniser.isEmitPending());
    }

    @Test
    public void testRead_InvalidEndTag() {
        tokeniser.transition(TokeniserState.TagStart);
        tokeniser.emit(new Token.StartTag("p"));
        tokeniser.emit(new Token.EndTag("div"));

        Token token = tokeniser.read();
        assertNull(token);
        tokeniser.error("State: TagStart");
    }

    @Test
    public void testRead_Comment() {
        when(reader.consumeLetter()).thenReturn('*');
        when(reader.consumeLetter()).thenReturn('!');
        when(reader.consumeLetter()).thenReturn('--');

        tokeniser.consumeCharacterReference(null, false);
        tokeniser.createCommentPending();

        tokeniser.read(); // consume '*'
        tokeniser.read(); // consume '!'
        tokeniser.read(); // consume '--'
        tokeniser.read(); // consume WhiteSpace

        Comment comment = tokeniser.read();
        assertEquals(Comment.class, comment.getClass());
        assertEquals("", comment.getData().getText());
        assertEquals(Token.TokenType.Comment, comment.getType());

        verify(reader).consumeLetter();
        verify(reader).consumeLetter();
        verify(reader).consumeLetter();
        verify(reader).consumeLetter();
    }

    @Test
    public void testRead_ScriptData() {
        tokeniser.createTempBuffer();
        tokeniser.emit(new Token.Character('<'));
        tokeniser.emit(new Token.Character('s'));
        tokeniser.emit(new Token.Character('c'+"ript"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('>'));

        assertEquals(TokeniserState.ScriptData, tokeniser.getState());

        tokeniser.emit(new Token.Character('A'));
        tokeniser.emit(new Token.Character('B'));
        tokeniser.emit(new Token.Character('C'));

        String scriptData = tokeniser.dataBuffer.toString();
        assertEquals("ABC", scriptData);

        tokeniser.emitTagPending();
        tokeniser.emit(new Token.Character('/'));
        tokeniser.emit(new Token.Character('s'));
        tokeniser.emit(new Token.Character('c'+"ript"));
        tokeniser.emit(new Token.Character(' '));
        tokeniser.emit(new Token.Character('>'));

        assertEquals(TokeniserState.Data, tokeniser.getState());
    }
}