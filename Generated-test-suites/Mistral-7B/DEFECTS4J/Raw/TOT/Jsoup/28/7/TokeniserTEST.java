import org.jsoup.parser.CharacterReference;
import org.jsoup.parser.ParseError;
import org.jsoup.parser.Token;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;
import org.jsoup.parser.TokenType;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniser;
    private ParseErrorList errors;
    private List<ParseError> expectedErrors;

    @Before
    public void setup() {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(
                "<html><head><script>function test() {}</script></head><body><h1>Test</h1></body></html>".getBytes(StandardCharsets.UTF_8));
        tokeniser = new Tokeniser(new CharacterReader(inputStream), new ParseErrorList());
        errors = tokeniser.getErrors();
        expectedErrors = new ArrayList<>();
    }

    @Test
    public void testReadNormalTokenisation() {
        Token token;
        while ((token = tokeniser.read()) != null) {
            System.out.println(token);
            Assert.assertNotNull(token);
            Assert.assertTrue(token.isValid());
        }
        Assert.assertTrue(errors.isEmpty());
    }

    @Test
    public void testReadSelfClosingTag() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.acknowledgeSelfClosingFlag();
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(TokenType.EndTag, token.type);
        Assert.assertEquals("script", token.tagName);
    }

    @Test
    public void testReadInvalidCharacterReference() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.consumeCharacterReference((char) 0xFFFE, false);
        tokeniser.error(TokeniserState.Data);
        ParseError error = errors.get(0);
        Assert.assertEquals("Invalid character reference: unexpected character '�' in input state [Data]", error.getMessage());
        Assert.assertEquals("Invalid character reference: unexpected character '�'", error.getErrorMessage());
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testReadNamedCharacterReference() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consume('&');
        tokeniser.consume('a');
        tokeniser.consume('m');
        tokeniser.consume('p');
        tokeniser.consume(';');
        Token token = tokeniser.read();
        Assert.assertNotNull(token);
        Assert.assertEquals(TokenType.Character, token.type);
        Assert.assertEquals("\u0061", token.data);
    }

    @Test
    public void testReadInvalidNamedCharacterReference() {
        tokeniser.transition(TokeniserState.Data);
        tokeniser.consumeCharacterReference(null, false);
        tokeniser.consume('&');
        tokeniser.consume('x');
        tokeniser.consume('o');
        tokeniser.consume('x');
        tokeniser.consume(';');
        tokeniser.error(TokeniserState.Data);
        ParseError error = errors.get(0);
        Assert.assertEquals("Invalid named reference 'ox': no matching named entity found", error.getMessage());
        Assert.assertEquals("Invalid named reference 'ox'", error.getErrorMessage());
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testReadEndOfFile() {
        tokeniser = new Tokeniser(new CharacterReader(new byte[0]), new ParseErrorList());
        errors = tokeniser.getErrors();
        tokeniser.read();
        tokeniser.eofError(TokeniserState.Data);
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpectedly reached end of file (EOF) in input state [Data]", error.getMessage());
        Assert.assertEquals("Unexpectedly reached end of file (EOF)", error.getErrorMessage());
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testReadCharacterReferenceInAttribute() {
        tokeniser.transition(TokeniserState.StartTag);
        Token startTag = tokeniser.read();
        Assert.assertEquals("html", startTag.tagName);
        tokeniser.consumeCharacterReference(null, true);
        tokeniser.error(TokeniserState.StartTag);
        ParseError error = errors.get(0);
        Assert.assertEquals("Attributes incorrectly present on end tag", error.getMessage());
        Assert.assertEquals("Attributes incorrectly present on end tag", error.getErrorMessage());
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testReadInvalidStateTransition() {
        tokeniser.transition(TokeniserState.Script);
        tokeniser.error(TokeniserState.StartTag);
        ParseError error = errors.get(0);
        Assert.assertEquals("Unexpected character 'script' in input state [StartTag]", error.getMessage());
        Assert.assertEquals("Unexpected character 'script'", error.getErrorMessage());
        Assert.assertEquals(1, errors.size());
    }

    @Test
    public void testCreateTagPending() {
        tokeniser.createTagPending(true);
        Token tag = tokeniser.tagPending;
        Assert.assertNotNull(tag);
        Assert.assertEquals(TokenType.StartTag, tag.type);
        Assert.assertNull(tag.tagName);
    }

    @Test
    public void testReadTagPending() {
        tokeniser.createTagPending(true);
        tokeniser.emitTagPending();
        Token tag = tokeniser.read();
        Assert.assertNotNull(tag);
        Assert.assertEquals(TokenType.StartTag, tag.type);
        Assert.assertNotNull(tag.tagName);
    }

    @Test
    public void testCreateCommentPending() {
        tokeniser.createCommentPending();
        Token comment = tokeniser.commentPending;
        Assert.assertNotNull(comment);
        Assert.assertEquals(TokenType.Comment, comment.type);
        Assert.assertEquals("", comment.data);
    }

    @Test
    public void testReadCommentPending() {
        tokeniser.createCommentPending();
        tokeniser.emitCommentPending();
        Token comment = tokeniser.read();
        Assert.assertNotNull(comment);
        Assert.assertEquals(TokenType.Comment, comment.type);
        Assert.assertNotNull(comment.data);
    }

    @Test
    public void testCreateDoctypePending() {
        tokeniser.createDoctypePending();
        Token doctype = tokeniser.doctypePending;
        Assert.assertNotNull(doctype);
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        Assert.assertNull(doctype.data);
    }

    @Test
    public void testReadDoctypePending() {
        tokeniser.createDoctypePending();
        tokeniser.emitDoctypePending();
        Token doctype = tokeniser.read();
        Assert.assertNotNull(doctype);
        Assert.assertEquals(TokenType.Doctype, doctype.type);
        Assert.assertNotNull(doctype.data);
    }
}