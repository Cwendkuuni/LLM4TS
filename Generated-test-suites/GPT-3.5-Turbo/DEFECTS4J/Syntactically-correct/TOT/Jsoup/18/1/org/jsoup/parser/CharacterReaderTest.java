import static org.junit.Assert.*;
import org.junit.Test;

public class CharacterReaderTest {

    @Test
    public void testPos_WhenInitialized_ReturnsZero() {
        CharacterReader reader = new CharacterReader("testString");
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testIsEmpty_WhenInitializedWithNonEmptyString_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.isEmpty());
    }

    @Test
    public void testCurrent_WhenInitialized_ReturnsFirstCharacter() {
        CharacterReader reader = new CharacterReader("testString");
        char expectedCurrent = 't';
        assertEquals(expectedCurrent, reader.current());
    }

    @Test
    public void testConsume_WhenInitialized_ReturnsFirstCharacterAndAdvancesPos() {
        CharacterReader reader = new CharacterReader("testString");
        char expectedVal = 't';
        assertEquals(expectedVal, reader.consume());
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testUnconsume_WhenCalled_AfterConsumePosMovesBackByOne() {
        CharacterReader reader = new CharacterReader("testString");
        reader.consume();
        reader.unconsume();
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testAdvance_WhenCalled_PosMovesForwardByOne() {
        CharacterReader reader = new CharacterReader("testString");
        int initialPos = reader.pos();
        reader.advance();
        assertEquals(initialPos + 1, reader.pos());
    }

    @Test
    public void testMark_WhenCalled_MarkVariableIsSetToCurrentPos() {
        CharacterReader reader = new CharacterReader("testString");
        reader.advance();
        reader.mark();
        int expectedMark = reader.pos();
        assertEquals(expectedMark, reader.pos());
    }

    @Test
    public void testRewindToMark_WhenCalled_PosIsSetToValueOfMark() {
        CharacterReader reader = new CharacterReader("testString");
        reader.advance();
        reader.mark();
        reader.advance();
        reader.rewindToMark();
        int expectedPos = reader.pos();
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeAsString_WhenCalled_ReturnsSubstringFromPosToNextPosition() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "t";
        assertEquals(expectedString, reader.consumeAsString());
        int expectedPos = 1;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeTo_WhenSequenceExists_ReturnsSubstringFromPosToNextOccurrence() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeTo("S"));
        int expectedPos = 6;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeTo_WhenSequenceDoesNotExist_ReturnsConsumeToEnd() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "testString";
        assertEquals(expectedString, reader.consumeTo("X"));
        int expectedPos = reader.length;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeToAny_WhenAnySequenceExists_ReturnsSubstringFromPosToNextOccurrenceOfAnyCharInSeq() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "testS";
        assertEquals(expectedString, reader.consumeToAny('S', 't'));
        int expectedPos = 6;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeToAny_WhenAnySequenceDoesNotExist_ReturnsEmptyString() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "";
        assertEquals(expectedString, reader.consumeToAny('X', 'Y'));
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeToEnd_WhenCalled_ReturnsSubstringFromPosToEnd() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "testString";
        assertEquals(expectedString, reader.consumeToEnd());
        int expectedPos = reader.length;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence_WhenLetterSequenceExists_ReturnsSequence() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "test";
        assertEquals(expectedString, reader.consumeLetterSequence());
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeLetterSequence_WhenLetterSequenceDoesNotExist_ReturnsEmptyString() {
        CharacterReader reader = new CharacterReader("1234");
        String expectedString = "";
        assertEquals(expectedString, reader.consumeLetterSequence());
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeHexSequence_WhenHexSequenceExists_ReturnsSequence() {
        CharacterReader reader = new CharacterReader("AB12");
        String expectedString = "AB12";
        assertEquals(expectedString, reader.consumeHexSequence());
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeHexSequence_WhenHexSequenceDoesNotExist_ReturnsEmptyString() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "";
        assertEquals(expectedString, reader.consumeHexSequence());
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence_WhenDigitSequenceExists_ReturnsSequence() {
        CharacterReader reader = new CharacterReader("123456");
        String expectedString = "123456";
        assertEquals(expectedString, reader.consumeDigitSequence());
        int expectedPos = 6;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testConsumeDigitSequence_WhenDigitSequenceDoesNotExist_ReturnsEmptyString() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "";
        assertEquals(expectedString, reader.consumeDigitSequence());
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatches_WhenCharacterMatches_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matches('t'));
    }

    @Test
    public void testMatches_WhenCharacterDoesNotMatch_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matches('X'));
    }

    @Test
    public void testMatches_WhenStringMatches_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matches("test"));
    }

    @Test
    public void testMatches_WhenStringDoesNotMatch_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matches("X"));
    }

    @Test
    public void testMatchesIgnoreCase_WhenStringMatchesIgnoresCase_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matchesIgnoreCase("TEST"));
    }

    @Test
    public void testMatchesIgnoreCase_WhenStringDoesNotMatchIgnoresCase_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matchesIgnoreCase("X"));
    }

    @Test
    public void testMatchesAny_WhenCharacterMatches_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matchesAny('t', 'X'));
    }

    @Test
    public void testMatchesAny_WhenCharacterDoesNotMatch_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matchesAny('X', 'Y'));
    }

    @Test
    public void testMatchesLetter_WhenCharacterIsLetter_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matchesLetter());
    }

    @Test
    public void testMatchesLetter_WhenCharacterIsNotLetter_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("1234");
        assertFalse(reader.matchesLetter());
    }

    @Test
    public void testMatchesDigit_WhenCharacterIsDigit_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("1234");
        assertTrue(reader.matchesDigit());
    }

    @Test
    public void testMatchesDigit_WhenCharacterIsNotDigit_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matchesDigit());
    }

    @Test
    public void testMatchConsume_WhenStringMatches_ReturnsTrueAndAdvancesPosByMatchLength() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matchConsume("test"));
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsume_WhenStringDoesNotMatch_ReturnsFalseAndDoesNotAdvancePos() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matchConsume("X"));
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_WhenStringMatchesIgnoresCase_ReturnsTrueAndAdvancesPosByMatchLength() {
        CharacterReader reader = new CharacterReader("testString");
        assertTrue(reader.matchConsumeIgnoreCase("TEST"));
        int expectedPos = 4;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testMatchConsumeIgnoreCase_WhenStringDoesNotMatchIgnoresCase_ReturnsFalseAndDoesNotAdvancePos() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.matchConsumeIgnoreCase("X"));
        int expectedPos = 0;
        assertEquals(expectedPos, reader.pos());
    }

    @Test
    public void testContainsIgnoreCase_WhenStringContainsIgnoreCase_ReturnsTrue() {
        CharacterReader reader = new CharacterReader("</Title>");
        assertTrue(reader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testContainsIgnoreCase_WhenStringDoesNotContainIgnoreCase_ReturnsFalse() {
        CharacterReader reader = new CharacterReader("testString");
        assertFalse(reader.containsIgnoreCase("</title>"));
    }

    @Test
    public void testToString_WhenCalled_ReturnsSubstringFromPosToEnd() {
        CharacterReader reader = new CharacterReader("testString");
        String expectedString = "testString";
        assertEquals(expectedString, reader.toString());
    }
}