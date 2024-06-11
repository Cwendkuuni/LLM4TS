package org.jsoup.parser; // make sure the test package is the same as the class package

import static org.junit.Assert.*;
import static org.jsoup.parser.TokeniserState.*;

import org.jsoup.helper.Validate;
import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.ParseErrorList;
import org.jsoup.parser.Token;
import org.jsoup.parser.Token.Character;
import org.jsoup.parser.Token.EndTag;
import org.jsoup.parser.Token.StartTag;
import org.jsoup.parser.Tokeniser;
import org.jsoup.parser.TokeniserState;

import java.util.Arrays;

public class TokeniserTest {

    @org.junit.Test
    public void testTokeniserConstructor() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        assertNotNull(tokeniser);
    }

    @org.junit.Test
    public void testReadNullCharacterReader() {
        ParseErrorList errors = new ParseErrorList();
        assertThrows(NullPointerException.class, () -> new Tokeniser(null, errors));
    }

    @org.junit.Test
    public void testReadNullParseErrorList() {
        CharacterReader reader = new CharacterReader("");
        assertThrows(NullPointerException.class, () -> new Tokeniser(reader, null));
    }

    @org.junit.Test
    public void testReadEmptyInput() {
        CharacterReader reader = new CharacterReader("");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token;
        while ((token = tokeniser.read()) != null) {
            token.print();
            System.out.println();
        }
        assertTrue(errors.isEmpty());
    }

    @org.junit.Test
    public void testReadCharacter() {
        CharacterReader reader = new CharacterReader("a");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.Character, token.type);
        Character charToken = (Character) token;
        assertEquals('a', charToken.data);
    }

    @org.junit.Test
    public void testReadString() {
        CharacterReader reader = new CharacterReader("abcd");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token;
        while ((token = tokeniser.read()) != null) {
            token.print();
            System.out.println();
        }
        assertTrue(errors.isEmpty());
    }

    @org.junit.Test
    public void testReadSelfClosingTag() {
        CharacterReader reader = new CharacterReader("<img alt='test'>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        Token token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.StartTag, token.type);
        StartTag startTag = (StartTag) token;
        assertTrue(startTag.isSelfClosing());

        token = tokeniser.read();
        assertNotNull(token);
        assertEquals(Token.TokenType.EndTag, token.type);
        EndTag endTag = (EndTag) token;
        assertEquals("img", endTag.tagName);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceSingleChar() {
        CharacterReader reader = new CharacterReader("#x41");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertEquals(1, codepoints.length);
        assertEquals('A', codepoints[0]);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceMultiChar() {
        CharacterReader reader = new CharacterReader("#x4142");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertEquals(2, codepoints.length);
        assertEquals('A', codepoints[0]);
        assertEquals('B', codepoints[1]);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceInvalid() {
        CharacterReader reader = new CharacterReader("#xABC");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceEmpty() {
        CharacterReader reader = new CharacterReader("#");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceNonHex() {
        CharacterReader reader = new CharacterReader("#123");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertNull(codepoints);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceInAttribute() {
        CharacterReader reader = new CharacterReader("<img src=\"#x4142\" alt=\"test\">");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        assertEquals(2, codepoints.length);
        assertEquals('A', codepoints[0]);
        assertEquals('B', codepoints[1]);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceNamed() {
        CharacterReader reader = new CharacterReader("<p>&nbsp;</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, false);
        assertArrayEquals(new int[]{0x00A0}, codepoints);
    }

    @org.junit.Test
    public void testConsumeCharacterReferenceNamedInAttribute() {
        CharacterReader reader = new CharacterReader("<p>&#x00A0;</p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        int[] codepoints = tokeniser.consumeCharacterReference(null, true);
        assertArrayEquals(new int[]{0x00A0}, codepoints);
    }

    @org.junit.Test
    public void testErrorUnknownState() {
        CharacterReader reader = new CharacterReader(" Una know state");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(DATA);
        tokeniser.error(null);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedCharacter() {
        CharacterReader reader = new CharacterReader("<!UNKNNOWN>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(SCRIPT_PI);
        tokeniser.error(SCRIPT_PI);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorExpectedEndTag() {
        CharacterReader reader = new CharacterReader("<p><script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error(START_TAG);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorExpectedEndTagWithName() {
        CharacterReader reader = new CharacterReader("<p><script></p></script>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("p");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTag() {
        CharacterReader reader = new CharacterReader("<p></p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error(END_TAG);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithName() {
        CharacterReader reader = new CharacterReader("<p><p></p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("p");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithoutName() {
        CharacterReader reader = new CharacterReader("<p><p></p></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error(END_TAG);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithInvalidName() {
        CharacterReader reader = new CharacterReader("<p><x></x></p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("x");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch() {
        CharacterReader reader = new CharacterReader("<p><div></p></li>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("li");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch2() {
        CharacterReader reader = new CharacterReader("<p><ul></p></ol>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("ol");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch3() {
        CharacterReader reader = new CharacterReader("<p><ol></p></ul>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("ul");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch4() {
        CharacterReader reader = new CharacterReader("<p><div><table></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("table");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch5() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("tr");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch6() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr><td></td></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("td");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch7() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr><td><table></table></td></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("table");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch8() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr><td><table><tr></tr></table></td></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("tr");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch9() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr><td><table><tr><td></td></tr></table></td></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("td");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorUnexpectedEndTagWithNameMismatch10() {
        CharacterReader reader = new CharacterReader("<p><div><table><tr><td><table><tr><td><table><tr><td></td></tr></table></td></tr></table></td></tr></table></div>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.error("table");
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorEOF() {
        CharacterReader reader = new CharacterReader("abcd<");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.eofError(START_TAG);
        assertTrue(errors.hasErrors());
    }

    @org.junit.Test
    public void testErrorEOFWithName() {
        CharacterReader reader = new CharacterReader("abcd<p>");
        ParseErrorList errors = new ParseErrorList();
        Tokeniser tokeniser = new Tokeniser(reader, errors);
        tokeniser.transition(START_TAG);
        tokeniser.eofError("p");
        assertTrue(errors.hasErrors());
    }

}