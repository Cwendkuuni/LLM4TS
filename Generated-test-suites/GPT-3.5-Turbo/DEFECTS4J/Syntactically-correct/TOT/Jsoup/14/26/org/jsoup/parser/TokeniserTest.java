import org.jsoup.parser.CharacterReader;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class TokeniserTest {

    private Tokeniser tokeniserUnderTest;

    @Before
    public void setUp() {
        CharacterReader reader = new CharacterReader("test string");
        tokeniserUnderTest = new Tokeniser(reader);
    }

    // Test case for read() method
    @Test
    public void testRead() {
        Token expectedToken = new Token.Character("test string");

        Token actualToken = tokeniserUnderTest.read();

        Assert.assertEquals(expectedToken, actualToken);
    }

    // Test case for emit() method with Token parameter
    @Test
    public void testEmitWithToken() {
        Token expectedToken = new Token.StartTag();

        tokeniserUnderTest.emit(expectedToken);
        Token actualToken = tokeniserUnderTest.read();

        Assert.assertEquals(expectedToken, actualToken);
    }

    // Test case for emit() method with string parameter
    @Test
    public void testEmitWithString() {
        String expectedString = "test string";

        tokeniserUnderTest.emit(expectedString);
        Token actualToken = tokeniserUnderTest.read();

        Assert.assertTrue(actualToken instanceof Token.Character);
        Assert.assertEquals(expectedString, ((Token.Character) actualToken).getData());
    }

    // Test case for emit() method with char parameter
    @Test
    public void testEmitWithChar() {
        char expectedChar = 't';

        tokeniserUnderTest.emit(expectedChar);
        Token actualToken = tokeniserUnderTest.read();

        Assert.assertTrue(actualToken instanceof Token.Character);
        Assert.assertEquals(String.valueOf(expectedChar), ((Token.Character) actualToken).getData());
    }

    // Test case for getState() method
    @Test
    public void testGetState() {
        TokeniserState expectedState = TokeniserState.Data;

        TokeniserState actualState = tokeniserUnderTest.getState();

        Assert.assertEquals(expectedState, actualState);
    }

    // Test case for transition() method
    @Test
    public void testTransition() {
        TokeniserState expectedState = TokeniserState.Comment;

        tokeniserUnderTest.transition(expectedState);
        TokeniserState actualState = tokeniserUnderTest.getState();

        Assert.assertEquals(expectedState, actualState);
    }

    // Test case for advanceTransition() method
    @Test
    public void testAdvanceTransition() {
        TokeniserState expectedState = TokeniserState.Comment;
        CharacterReader characterReader = new CharacterReader("test string");

        tokeniserUnderTest.advanceTransition(expectedState);
        TokeniserState actualState = tokeniserUnderTest.getState();

        Assert.assertEquals(expectedState, actualState);
        Assert.assertEquals(1, characterReader.pos());
    }

    // Test case for acknowledgeSelfClosingFlag() method
    @Test
    public void testAcknowledgeSelfClosingFlag() {
        boolean expectedSelfClosingFlagAcknowledged = true;

        tokeniserUnderTest.acknowledgeSelfClosingFlag();
        boolean actualSelfClosingFlagAcknowledged = tokeniserUnderTest.isSelfClosingFlagAcknowledged();

        Assert.assertEquals(expectedSelfClosingFlagAcknowledged, actualSelfClosingFlagAcknowledged);
    }

    // Test case for consumeCharacterReference() method
    @Test
    public void testConsumeCharacterReference() {
        Character additionalAllowedCharacter = 't';
        boolean inAttribute = false;
        Character expectedCharacterReference = null;

        Character actualCharacterReference = tokeniserUnderTest.consumeCharacterReference(additionalAllowedCharacter, inAttribute);

        Assert.assertEquals(expectedCharacterReference, actualCharacterReference);
    }

    // Test case for createTagPending() method
    @Test
    public void testCreateTagPending() {
        boolean start = true;
        Token.Tag expectedTagPending = new Token.StartTag();

        Token.Tag actualTagPending = tokeniserUnderTest.createTagPending(start);

        Assert.assertEquals(expectedTagPending, actualTagPending);
    }

    // Test case for emitTagPending() method
    @Test
    public void testEmitTagPending() {
        tokeniserUnderTest.createTagPending(true);
        Token.Tag expectedTagPending = tokeniserUnderTest.tagPending;

        tokeniserUnderTest.emitTagPending();
        Token.Tag actualTagPending = (Token.Tag) tokeniserUnderTest.read();

        Assert.assertEquals(expectedTagPending, actualTagPending);
    }

    // Test case for createCommentPending() method
    @Test
    public void testCreateCommentPending() {
        tokeniserUnderTest.createCommentPending();
        Token.Comment expectedCommentPending = tokeniserUnderTest.commentPending;

        Token.Comment actualCommentPending = (Token.Comment) tokeniserUnderTest.read();

        Assert.assertEquals(expectedCommentPending, actualCommentPending);
    }

    // Test case for emitCommentPending() method
    @Test
    public void testEmitCommentPending() {
        tokeniserUnderTest.createCommentPending();
        Token.Comment expectedCommentPending = tokeniserUnderTest.commentPending;

        tokeniserUnderTest.emitCommentPending();
        Token.Comment actualCommentPending = (Token.Comment) tokeniserUnderTest.read();

        Assert.assertEquals(expectedCommentPending, actualCommentPending);
    }

    // Test case for createDoctypePending() method
    @Test
    public void testCreateDoctypePending() {
        tokeniserUnderTest.createDoctypePending();
        Token.Doctype expectedDoctypePending = tokeniserUnderTest.doctypePending;

        Token.Doctype actualDoctypePending = (Token.Doctype) tokeniserUnderTest.read();

        Assert.assertEquals(expectedDoctypePending, actualDoctypePending);
    }

    // Test case for emitDoctypePending() method
    @Test
    public void testEmitDoctypePending() {
        tokeniserUnderTest.createDoctypePending();
        Token.Doctype expectedDoctypePending = tokeniserUnderTest.doctypePending;

        tokeniserUnderTest.emitDoctypePending();
        Token.Doctype actualDoctypePending = (Token.Doctype) tokeniserUnderTest.read();

        Assert.assertEquals(expectedDoctypePending, actualDoctypePending);
    }

    // Test case for createTempBuffer() method
    @Test
    public void testCreateTempBuffer() {
        tokeniserUnderTest.createTempBuffer();
        StringBuilder expectedDataBuffer = tokeniserUnderTest.dataBuffer;

        Assert.assertNotNull(expectedDataBuffer);
        Assert.assertEquals("", expectedDataBuffer.toString());
    }

    // Test case for isAppropriateEndTagToken() method
    @Test
    public void testIsAppropriateEndTagToken() {
        tokeniserUnderTest.createTagPending(true);
        tokeniserUnderTest.createTagPending(true);
        boolean expectedIsAppropriateEndTagToken = true;

        boolean actualIsAppropriateEndTagToken = tokeniserUnderTest.isAppropriateEndTagToken();

        Assert.assertEquals(expectedIsAppropriateEndTagToken, actualIsAppropriateEndTagToken);
    }

    // Test case for isTrackErrors() method
    @Test
    public void testIsTrackErrors() {
        boolean expectedTrackErrors = true;

        boolean actualTrackErrors = tokeniserUnderTest.isTrackErrors();

        Assert.assertEquals(expectedTrackErrors, actualTrackErrors);
    }

    // Test case for setTrackErrors() method
    @Test
    public void testSetTrackErrors() {
        boolean expectedTrackErrors = true;

        tokeniserUnderTest.setTrackErrors(expectedTrackErrors);
        boolean actualTrackErrors = tokeniserUnderTest.isTrackErrors();

        Assert.assertEquals(expectedTrackErrors, actualTrackErrors);
    }

    // Test case for error() method with TokeniserState parameter
    @Test
    public void testErrorWithTokeniserState() {
        TokeniserState state = TokeniserState.Data;
        List<ParseError> expectedErrors = tokeniserUnderTest.errors;

        tokeniserUnderTest.error(state);
        List<ParseError> actualErrors = tokeniserUnderTest.getErrors();

        Assert.assertEquals(expectedErrors, actualErrors);
    }

    // Test case for eofError() method with TokeniserState parameter
    @Test
    public void testEofErrorWithTokeniserState() {
        TokeniserState state = TokeniserState.Data;
        List<ParseError> expectedErrors = tokeniserUnderTest.errors;

        tokeniserUnderTest.eofError(state);
        List<ParseError> actualErrors = tokeniserUnderTest.getErrors();

        Assert.assertEquals(expectedErrors, actualErrors);
    }

    // Test case for characterReferenceError() method
    @Test
    public void testCharacterReferenceError() {
        List<ParseError> expectedErrors = tokeniserUnderTest.errors;

        tokeniserUnderTest.characterReferenceError();
        List<ParseError> actualErrors = tokeniserUnderTest.getErrors();

        Assert.assertEquals(expectedErrors, actualErrors);
    }

    // Test case for error() method with String parameter
    @Test
    public void testErrorWithString() {
        String errorMsg = "Test Error";
        List<ParseError> expectedErrors = tokeniserUnderTest.errors;

        tokeniserUnderTest.error(errorMsg);
        List<ParseError> actualErrors = tokeniserUnderTest.getErrors();

        Assert.assertEquals(expectedErrors, actualErrors);
    }

    // Test case for currentNodeInHtmlNS() method
    @Test
    public void testCurrentNodeInHtmlNS() {
        boolean expectedCurrentNodeInHtmlNS = true;

        boolean actualCurrentNodeInHtmlNS = tokeniserUnderTest.currentNodeInHtmlNS();

        Assert.assertEquals(expectedCurrentNodeInHtmlNS, actualCurrentNodeInHtmlNS);
    }
    
    // Add more test cases for other methods in Tokeniser class
    
}