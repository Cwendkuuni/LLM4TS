import org.junit.Assert;
import org.junit.Test;
import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.helper.Validate;

public class TokeniserTest {

    @Test
    public void testTokeniserConstructor() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);
        Assert.assertNotNull(tokeniser);
    }

    @Test
    public void testReadCharacter() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        Token token = tokeniser.read();
        Assert.assertNotNull(token);

        char expectedChar = (char) System.in.read(); // replace with expected character
        Assert.assertEquals(expectedChar, token.getText().charAt(0));

        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadStartTag() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset(" <html>");

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.HTML, token.getTag());

        token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test(expected = ParseError.class)
    public void testReadSelfClosingTagWithoutAcknowledgement() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("<img/>");

        tokeniser.read(); // read initial <
        tokeniser.read(); // read img

        tokeniser.read(); // read /
        tokeniser.read(); // read >

        tokeniser.read(); // try to read next token, expecting self-closing tag acknowledgement error
    }

    @Test
    public void testReadEndTag() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("<html><body><p></p></body></html>");

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.HTML, token.getTag());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.HEAD, token.getTag());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.BODY, token.getTag());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.P, token.getTag());

        token = tokeniser.read(); // read </p>
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.EndTag, token.getTag());
        Assert.assertEquals("p", token.getTag().tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.END_TAG, token.getTag().tagName);

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.BODY, token.getTag());

        token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.Tag.HTML, token.getTag());

        Assert.assertNull(tokeniser.read());
    }

    @Test
    public void testReadString() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("Hello, world!");

        String expectedString = "Hello, world!";
        StringBuilder actualStringBuilder = new StringBuilder();

        tokeniser.emit("Hello, world!");
        tokeniser.read();

        while (tokeniser.getState() != TokeniserState.Data) {
            tokeniser.read();
            actualStringBuilder.append(tokeniser.read().getText());
        }

        Assert.assertEquals(expectedString, actualStringBuilder.toString());
    }

    @Test
    public void testReadCharacterReference() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("#x26;"); // Unicode for the character '&'

        CharacterReference characterReference = tokeniser.consumeCharacterReference(null, false);
        Assert.assertEquals('&', characterReference.getChar());

        characterReference = tokeniser.consumeCharacterReference(null, true);
        Assert.assertEquals(Token.Character.AMPERSAND, tokeniser.read().getType());
    }

    @Test(expected = ParseError.class)
    public void testReadInvalidCharacterReference() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("#abcd");

        tokeniser.consumeCharacterReference(null, false);

        // This should throw a ParseError
    }

    @Test
    public void testCreateTagPending() {
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(), new ParseErrorList());
        Token.Tag tagPending = tokeniser.createTagPending(true);

        Validate.isInstanceOf(Token.StartTag.class, tagPending);
    }

    @Test
    public void testEmitTagPending() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();

        Token expectedToken = new Token.Character("</s>"); // Replace with the expected tag name
        Token actualToken = tokeniser.read();

        Assert.assertEquals(expectedToken.getTag(), actualToken.getTag());
    }

    @Test
    public void testErrorHandling() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset(" <html><body><script>");

        tokeniser.read();
        tokeniser.read();

        tokeniser.error(TokeniserState.TagSoup);
        Assert.assertEquals(1, errors.size());

        tokeniser.read();

        characterReader.reset("<script>");
        tokeniser.transition(TokeniserState.Script);
        tokeniser.read();

        tokeniser.error(TokeniserState.Script);
        Assert.assertEquals(2, errors.size());

        tokeniser.acknowledgeSelfClosingFlag();
        tokeniser.transition(TokeniserState.Data);
        tokeniser.read();

        Assert.assertEquals(3, errors.size());
    }

    @Test
    public void testTransitionStates() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("<html><head><title>Test</title>");

        tokeniser.transition(TokeniserState.StartTag);
        tokeniser.read();

        Assert.assertEquals(TokeniserState.StartTag, tokeniser.getState());

        tokeniser.transition(TokeniserState.Attributes);
        tokeniser.read();

        Assert.assertEquals(TokeniserState.Attributes, tokeniser.getState());

        tokeniser.transition(TokeniserState.EndTag);
        tokeniser.read();

        Assert.assertEquals(TokeniserState.EndTag, tokeniser.getState());

        tokeniser.transition(TokeniserState.Data);
        tokeniser.read();

        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());

        tokeniser.acknowledgeSelfClosingFlag();

        Assert.assertEquals(TokeniserState.Data, tokeniser.getState());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        CharacterReader characterReader = new CharacterReader();
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(characterReader, errors);

        characterReader.reset("<html><body><p></p></body></html>");

        Assert.assertTrue(tokeniser.isAppropriateEndTagToken());

        characterReader.reset("<html><body><p></p><div></div></body></html>");

        Assert.assertFalse(tokeniser.isAppropriateEndTagToken());
    }
}