import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setup() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testReadEmptyToken() {
        Token token = tokeniser.read();
        Assert.assertNull(token);
    }

    @Test
    public void testReadCharacterToken() {
        CharacterReader reader = new CharacterReader("Hello world");
        tokeniser = new Tokeniser(reader, new ParseErrorList());

        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("Hello world", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitToken() {
        Token token = new Token.StartTag();
        tokeniser.emit(token);

        Token emittedToken = tokeniser.read();
        Assert.assertEquals(token, emittedToken);
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('H');
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("H", ((Token.Character) token).getData());
    }

    @Test
    public void testTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.name = "div";
        Token.Tag tagPending = tokeniser.tagPending;

        Assert.assertNotNull(tagPending);
        Assert.assertEquals(tag, tagPending);
    }

    @Test
    public void testTagPendingFinaliseTag() {
        Token.Tag tag = tokeniser.createTagPending(true);
        tag.attributes.put("class", "example");
        tag.attributes.put("id", "element");
        tag.pendingAttributeValue = "Test attribute";

        tokeniser.emitTagPending();

        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, emittedToken.type);
        Assert.assertEquals(tag, emittedToken);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token.Comment commentPending = tokeniser.commentPending;

        Assert.assertNotNull(commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.data = "This is a comment";

        tokeniser.emitCommentPending();

        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, emittedToken.type);
        Assert.assertEquals("This is a comment", ((Token.Comment) emittedToken).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token.Doctype doctypePending = tokeniser.doctypePending;

        Assert.assertNotNull(doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.doctypePending.publicIdentifier = "-//W3C//DTD HTML 4.01 Transitional//EN";
        tokeniser.doctypePending.systemIdentifier = "http://www.w3.org/TR/html4/loose.dtd";

        tokeniser.emitDoctypePending();

        Token emittedToken = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, emittedToken.type);
        Assert.assertEquals("html", ((Token.Doctype) emittedToken).getName());
        Assert.assertEquals("-//W3C//DTD HTML 4.01 Transitional//EN", ((Token.Doctype) emittedToken).getPublicIdentifier());
        Assert.assertEquals("http://www.w3.org/TR/html4/loose.dtd", ((Token.Doctype) emittedToken).getSystemIdentifier());
    }

    @Test
    public void testCreateTempBuffer() {
        tokeniser.createTempBuffer();
        StringBuilder dataBuffer = tokeniser.dataBuffer;

        Assert.assertNotNull(dataBuffer);
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "div";

        boolean isAppropriateEndTagToken = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(isAppropriateEndTagToken);
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = new Token.EndTag();
        tokeniser.tagPending.tagName = "div";

        String appropriateEndTagName = tokeniser.appropriateEndTagName();
        Assert.assertEquals("div", appropriateEndTagName);
    }

    @Test
    public void testError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), errors);

        tokeniser.error(TokeniserState.TagName);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character '' in input state [TagName]", errors.get(0).toString());
    }

    @Test
    public void testEofError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), errors);

        tokeniser.eofError(TokeniserState.TagName);
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [TagName]", errors.get(0).toString());
    }

    @Test
    public void testCharacterReferenceError() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), errors);

        tokeniser.characterReferenceError("Invalid reference");
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference: Invalid reference", errors.get(0).toString());
    }

    @Test
    public void testErrorWithMessage() {
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(new CharacterReader(""), errors);

        tokeniser.error("Error message");
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Error at position 0: Error message", errors.get(0).toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean isCurrentNodeInHtmlNS = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(isCurrentNodeInHtmlNS); // Assuming HTML namespace is always true
    }
}