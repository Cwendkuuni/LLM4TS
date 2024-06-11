import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {
    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("Test input");
        ParseErrorList errors = new ParseErrorList();
        tokeniser = new Tokeniser(reader, errors);
    }

    @Test
    public void testRead() {
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
    }

    @Test
    public void testEmitCharacter() {
        tokeniser.emit('a');
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("a", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitString() {
        tokeniser.emit("Test");
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Character, token.type);
        Assert.assertEquals("Test", ((Token.Character) token).getData());
    }

    @Test
    public void testEmitToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.emit(startTag);
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("div", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateTagPending() {
        Token.Tag tag = tokeniser.createTagPending(true);
        Assert.assertNotNull(tag);
        Assert.assertTrue(tag instanceof Token.StartTag);
    }

    @Test
    public void testEmitTagPending() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.tagPending = startTag;
        tokeniser.emitTagPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.StartTag, token.type);
        Assert.assertEquals("div", ((Token.StartTag) token).tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Assert.assertNotNull(tokeniser.commentPending);
    }

    @Test
    public void testEmitCommentPending() {
        Token.Comment comment = new Token.Comment();
        comment.setData("Test Comment");
        tokeniser.commentPending = comment;
        tokeniser.emitCommentPending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Comment, token.type);
        Assert.assertEquals("Test Comment", ((Token.Comment) token).getData());
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Assert.assertNotNull(tokeniser.doctypePending);
    }

    @Test
    public void testEmitDoctypePending() {
        Token.Doctype doctype = new Token.Doctype();
        doctype.setName("html");
        tokeniser.doctypePending = doctype;
        tokeniser.emitDoctypePending();
        Token token = tokeniser.read();
        Assert.assertEquals(Token.TokenType.Doctype, token.type);
        Assert.assertEquals("html", ((Token.Doctype) token).getName());
    }

    @Test
    public void testIsAppropriateEndTagToken() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        tokeniser.tagPending = startTag;
        boolean result = tokeniser.isAppropriateEndTagToken();
        Assert.assertTrue(result);
    }

    @Test
    public void testAppropriateEndTagName() {
        Token.StartTag startTag = new Token.StartTag();
        startTag.tagName = "div";
        tokeniser.lastStartTag = startTag;
        String tagName = tokeniser.appropriateEndTagName();
        Assert.assertEquals("div", tagName);
    }

    @Test
    public void testErrorState() {
        tokeniser.error(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpected character 'T' in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testEofErrorState() {
        tokeniser.eofError(TokeniserState.Data);
        ParseErrorList errors = tokeniser.errors;
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", errors.get(0).getMessage());
    }

    @Test
    public void testCharacterReferenceError() {
        tokeniser.characterReferenceError("Invalid reference");
        ParseErrorList errors = tokeniser.errors;
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Invalid character reference: Invalid reference", errors.get(0).getMessage());
    }

    @Test
    public void testError() {
        tokeniser.error("Test error");
        ParseErrorList errors = tokeniser.errors;
        Assert.assertEquals(1, errors.size());
        Assert.assertEquals("Test error", errors.get(0).getMessage());
    }

    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean result = tokeniser.currentNodeInHtmlNS();
        Assert.assertTrue(result);
    }
}