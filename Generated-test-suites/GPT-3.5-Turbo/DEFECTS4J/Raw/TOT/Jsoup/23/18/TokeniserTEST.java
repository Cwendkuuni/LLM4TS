import org.junit.Before;
import org.junit.Test;
import org.jsoup.helper.Validate;
import org.jsoup.nodes.Entities;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

public class TokeniserTest {

    private Tokeniser tokeniser;

    @Before
    public void setUp() {
        tokeniser = new Tokeniser(new CharacterReader(), new ParseErrorList());
    }

    @Test
    public void testRead_EmptyInput_ReturnsNull() {
        Token expected = null;
        Token actual = tokeniser.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmit_CharacterToken_ReturnsCorrectToken() {
        Token expected = new Token.Character("test");
        tokeniser.emit(expected);
        tokeniser.isEmitPending = true;
        Token actual = tokeniser.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmit_TagToken_ReturnsCorrectToken() {
        Token.StartTag expected = new Token.StartTag();
        tokeniser.tagPending = expected;
        tokeniser.emitTagPending();
        tokeniser.isEmitPending = true;
        Token actual = tokeniser.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testEmit_EndTagToken_ReturnsCorrectToken() {
        Token.EndTag expected = new Token.EndTag();
        expected.attributes.add(new Attribute("id", "test"));
        tokeniser.emit(expected);
        tokeniser.isEmitPending = true;
        Token actual = tokeniser.read();
        assertEquals(expected, actual);
    }

    @Test
    public void testTransition_SetState_ReturnsCorrectState() {
        TokeniserState expected = TokeniserState.Data;
        tokeniser.transition(expected);
        TokeniserState actual = tokeniser.getState();
        assertEquals(expected, actual);
    }

    @Test
    public void testAdvanceTransition_SetStateAndAdvanceReader_ReturnsCorrectStateAndAdvancesReader() {
        TokeniserState expected = TokeniserState.TagName;
        tokeniser.advanceTransition(expected);
        TokeniserState actual = tokeniser.getState();
        assertEquals(expected, actual);
        assertEquals(1, tokeniser.reader.pos());
    }

    @Test
    public void testAcknowledgeSelfClosingFlag_SetFlag_ReturnsFlag() {
        boolean expected = true;
        tokeniser.acknowledgeSelfClosingFlag();
        boolean actual = tokeniser.selfClosingFlagAcknowledged;
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_EmptyReader_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.isEmpty = true;
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_AdditionalCharacterMatches_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.currentChar = '!';
        Character actual = tokeniser.consumeCharacterReference('!', false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_CurrentCharacterIsConsumeCharacters_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.currentChar = '<';
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_NumericReference_ReturnsCorrectCharacter() {
        Character expected = 'A';
        tokeniser.reader.consumeSequence = "#65;";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_NumericReferenceWithHex_ReturnsCorrectCharacter() {
        Character expected = 'A';
        tokeniser.reader.consumeSequence = "#X41;";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_NumericReferenceWithNoNumerals_ReturnsReplacementChar() {
        Character expected = '\uFFFD';
        tokeniser.reader.consumeSequence = "#;";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_NumericReferenceOutsideValidRange_ReturnsReplacementChar() {
        Character expected = '\uFFFD';
        tokeniser.reader.consumeSequence = "#1000000;";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_NamedReference_ReturnsCorrectCharacter() {
        Character expected = '\u00A0';
        tokeniser.reader.consumeSequence = "&nbsp;";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_InAttributeAndInputMatch_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.currentChar = '=';
        Character actual = tokeniser.consumeCharacterReference('#', true);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_InAttributeAndInputNotMatch_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.currentChar = '=';
        Character actual = tokeniser.consumeCharacterReference(null, true);
        assertEquals(expected, actual);
    }

    @Test
    public void testConsumeCharacterReference_MissingSemicolon_ReturnsNull() {
        Character expected = null;
        tokeniser.reader.consumeSequence = "&#65";
        Character actual = tokeniser.consumeCharacterReference(null, false);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTagPending_StartTag_ReturnsStartTag() {
        Token.Tag expected = new Token.StartTag();
        Token.Tag actual = tokeniser.createTagPending(true);
        assertEquals(expected, actual);
    }

    @Test
    public void testCreateTagPending_EndTag_ReturnsEndTag() {
        Token.Tag expected = new Token.EndTag();
        Token.Tag actual = tokeniser.createTagPending(false);
        assertEquals(expected, actual);
    }

    @Test
    public void testEmitCommentPending_CreatesCommentToken() {
        tokeniser.createCommentPending();
        tokeniser.commentPending.comment = "test";
        tokeniser.emitCommentPending();
        tokeniser.isEmitPending = true;
        Token actual = tokeniser.read();
        assertTrue(actual instanceof Token.Comment);
        assertEquals("test", ((Token.Comment)actual).comment);
    }

    @Test
    public void testEmitDoctypePending_CreatesDoctypeToken() {
        tokeniser.createDoctypePending();
        tokeniser.doctypePending.name = "html";
        tokeniser.doctypePending.publicIdentifier = "-//W3C//DTD XHTML 1.0 Transitional//EN";
        tokeniser.doctypePending.systemIdentifier = "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd";
        tokeniser.emitDoctypePending();
        tokeniser.isEmitPending = true;
        Token actual = tokeniser.read();
        assertTrue(actual instanceof Token.Doctype);
        assertEquals("html", ((Token.Doctype)actual).name);
        assertEquals("-//W3C//DTD XHTML 1.0 Transitional//EN", ((Token.Doctype)actual).publicIdentifier);
        assertEquals("http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd", ((Token.Doctype)actual).systemIdentifier);
    }

    @Test
    public void testError_AddError_ReturnsCorrectError() {
        String expected = "Unexpected character 'a' in input state [Data]";
        tokeniser.error(TokeniserState.Data);
        ParseError actual = tokeniser.errors.get(0);
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testEofError_AddError_ReturnsCorrectError() {
        String expected = "Unexpectedly reached end of file (EOF) in input state [Data]";
        tokeniser.eofError(TokeniserState.Data);
        ParseError actual = tokeniser.errors.get(0);
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testCharacterReferenceError_AddError_ReturnsCorrectError() {
        String expected = "Invalid character reference: numeric reference with no numerals";
        tokeniser.characterReferenceError("numeric reference with no numerals");
        ParseError actual = tokeniser.errors.get(0);
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testErrorWithMessage_AddError_ReturnsCorrectError() {
        String expected = "Attributes incorrectly present on end tag";
        tokeniser.error("Attributes incorrectly present on end tag");
        ParseError actual = tokeniser.errors.get(0);
        assertEquals(expected, actual.toString());
    }

    @Test
    public void testCurrentNodeInHtmlNS_Always_ReturnsTrue() {
        assertTrue(tokeniser.currentNodeInHtmlNS());
    }
}