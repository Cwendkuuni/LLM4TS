import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class TokeniserTest {

    private CharacterReader reader;
    private ParseErrorList errors;
    private Tokeniser tokeniser;

    @Before
    public void setup() {
        reader = new CharacterReader("");
        errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead_NullCharacter_ThrowsException() {
        reader.current(' '); // set current character to a valid one
        reader.next(); // consume the valid character
        reader.current((char) 0); // set current character to null

        try {
            tokeniser.read();
            Assert.fail("Expected an exception to be thrown");
        } catch (Exception e) {
            // expected
        }
    }

    @Test
    public void testRead_Character() {
        reader.current('a');
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Character.class, token.getClass());
        Assert.assertEquals("a", token.getText());
    }

    @Test
    public void testRead_StartTag() {
        reader.current('<');
        reader.consume('p');
        reader.consume('>');

        Token token = tokeniser.read();
        Assert.assertEquals(Token.StartTag.class, token.getClass());
        Assert.assertEquals("p", token.getTag().getName());
    }

    @Test
    public void testRead_EndTag() {
        reader.current('<');
        reader.consume('/');
        reader.consume('p');
        reader.consume('>');

        Token token = tokeniser.read();
        Assert.assertEquals(Token.EndTag.class, token.getClass());
        Assert.assertEquals("p", token.getTag().getName());
    }

    @Test
    public void testRead_String() {
        reader.current("hello world");
        tokeniser.emit("hello world");
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Text.class, token.getClass());
        Assert.assertEquals("hello world", token.getText());
    }

    @Test
    public void testRead_SelfClosingTag() {
        reader.current('<');
        reader.consume('i');
        reader.consume('m');
        reader.consume('g');
        reader.consume(' ');
        reader.consume('s');
        reader.consume('o');
        reader.consume('r');
        reader.consume('t');
        reader.consume('>');

        Token token = tokeniser.read();
        Assert.assertEquals(Token.StartTag.class, token.getClass());
        Assert.assertEquals("img", token.getTag().getName());
        Assert.assertTrue(token.getTag().isSelfClosing());
    }

    @Test
    public void testRead_Error() {
        reader.current('<');
        reader.consume('i');
        reader.consume('m');
        reader.consume('>'); // missing 'g'

        tokeniser.error(TokeniserState.Data);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Error.class, token.getClass());
        Assert.assertEquals("Unexpected character '<' in input state [Data]", token.getText());
    }

    @Test
    public void testRead_EOF() {
        tokeniser.error(TokeniserState.Data); // simulate an error to move to an error state
        reader.current(0); // simulate EOF

        tokeniser.read();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.EOF.class, token.getClass());
    }

    @Test
    public void testEmit_Character() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Character.class, token.getClass());
        Assert.assertEquals("a", token.getText());
    }

    @Test
    public void testEmit_StartTag() {
        tokeniser.emit(tokeniser.createTagPending(true));
        Token token = tokeniser.read();
        Assert.assertEquals(Token.StartTag.class, token.getClass());
        Assert.assertNull(token.getTag().getAttributes());
    }

    @Test
    public void testEmit_EndTag() {
        tokeniser.emitTagPending();
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        tokeniser.emit(tokeniser.createTagPending(false));
        tokeniser.emitDoctypePending();

        Token token = tokeniser.read();
        Assert.assertEquals(Token.EndTag.class, token.getClass());
        Assert.assertNull(token.getTag().getAttributes());
    }

    @Test
    public void testEmit_Text() {
        tokeniser.emit("hello world");
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Text.class, token.getClass());
        Assert.assertEquals("hello world", token.getText());
    }

    @Test
    public void testTransition_Data() {
        tokeniser.transition(TokeniserState.TagSoup);
        Assert.assertEquals(TokeniserState.TagSoup, tokeniser.getState());
    }

    @Test
    public void testTransition_TagSoup() {
        tokeniser.transition(TokeniserState.Script);
        Assert.assertEquals(TokeniserState.Script, tokeniser.getState());
    }

    @Test
    public void testTransition_Script() {
        tokeniser.transition(TokeniserState.ScriptData);
        Assert.assertEquals(TokeniserState.ScriptData, tokeniser.getState());
    }

    @Test
    public void testTransition_ScriptData() {
        tokeniser.transition(TokeniserState.TagSoup);
        Assert.assertEquals(TokeniserState.TagSoup, tokeniser.getState());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag() {
        tokeniser.acknowledgeSelfClosingFlag();
        Assert.assertTrue(tokeniser.selfClosingFlagAcknowledged);
    }

    @Test
    public void testConsumeCharacterReference_ValidReference() {
        reader.current('&amp;');
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('&', charRef.charValue());
    }

    @Test
    public void testConsumeCharacterReference_InvalidReference() {
        reader.current('&abcd;');
        Character charRef = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals(Tokeniser.replacementChar, charRef.charValue());
    }

    @Test
    public void testConsumeCharacterReference_InvalidReferenceWithAttribute() {
        reader.current('&abcd;');
        reader.consume(' '); // consume the space character
        reader.consume(' '); // consume the space character
        reader.consume('='); // consume the equals sign
        reader.consume('x'); // consume the attribute name
        reader.consume(' '); // consume the space character
        reader.consume('>'); // consume the end tag

        Character charRef = tokeniser.consumeCharacterReference(null, true);
        Assert.assertEquals(Tokeniser.replacementChar, charRef.charValue());
    }

    @Test
    public void testCreateTagPending_StartTag() {
        Token.StartTag tag = tokeniser.createTagPending(true);
        Assert.assertEquals("", tag.getTagName());
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testCreateTagPending_EndTag() {
        Token.EndTag tag = tokeniser.createTagPending(false);
        Assert.assertEquals("", tag.getTagName());
        Assert.assertNull(tag.getAttributes());
    }

    @Test
    public void testEmitTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.StartTag.class, token.getClass());
        Assert.assertNull(token.getTag().getAttributes());
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Comment.class, token.getClass());
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Comment.class, token.getClass());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Doctype.class, token.getClass());
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.Doctype.class, token.getClass());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        Assert.assertNotNull(tokeniser.dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_InvalidTagName() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        tokeniser.createTagPending(false);
        tokeniser.getTagPending().tagName = "invalidTagName";
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    @Test
    public void testIsAppropriateEndTagToken_NullLastStartTag() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }

    
}