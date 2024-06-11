import org.jsoup.parser.CharacterReader;
import org.jsoup.parser.CharacterReader.TokeniserState;
import org.junit.Assert;
import org.junit.Test;

import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader_Constructor_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("test"));
        Assert.assertNotNull(reader);
    }

    @Test
    public void testCharacterReader_Constructor_NullInput_ThrowsException() {
        try {
            new CharacterReader(null);
            Assert.fail("Expected UnsupportedOperationException");
        } catch (UnsupportedOperationException e) {
            // expected
        }
    }

    @Test
    public void testCharacterReader_Constructor_EmptyInput_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertTrue(reader.isEmpty());
        Assert.assertFalse(reader.matches('a'));
    }

    @Test
    public void testCharacterReader_Constructor_EmptyInput_Consumer_EmptyString() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testCharacterReader_Constructor_InputWithSingleCharacter_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("a"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertEquals("a", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_Constructor_InputWithMultipleCharacters_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertFalse(reader.isEmpty());
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertEquals("abcd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_consume_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        char c;
        c = reader.consume();
        Assert.assertEquals('a', c);
        c = reader.consume();
        Assert.assertEquals('b', c);
        c = reader.consume();
        Assert.assertEquals('c', c);
        c = reader.consume();
        Assert.assertEquals('d', c);
        c = reader.consume();
        Assert.assertEquals(CharacterReader.EOF, c);
    }

    @Test
    public void testCharacterReader_consume_EdgeCase_EndOfInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        char c = reader.consume();
        Assert.assertEquals(CharacterReader.EOF, c);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReader_bufferUp_IOException_ThrowsException() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.reader.mark(100);
        reader.reader.reset();
        reader.reader.skip(101);
        reader.bufferUp();
    }

    @Test
    public void testCharacterReader_mark_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        reader.mark();
        reader.rewindToMark();
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertEquals("ab", reader.consumeTo('c'));
        reader.mark();
        Assert.assertTrue(reader.matches('c'));
        reader.rewindToMark();
        Assert.assertTrue(reader.matches('b'));
    }

    @Test
    public void testCharacterReader_mark_EdgeCase_EndOfInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        reader.mark();
        reader.consumeToEnd();
        reader.rewindToMark();
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReader_nextIndexOf_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertEquals(1, reader.nextIndexOf('b'));
        Assert.assertEquals(4, reader.nextIndexOf('d'));
        Assert.assertEquals(-1, reader.nextIndexOf('e'));
        Assert.assertEquals(-1, reader.nextIndexOf(100));
    }

    @Test
    public void testCharacterReader_nextIndexOf_EdgeCase_EndOfInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        Assert.assertEquals(-1, reader.nextIndexOf('a'));
    }

    @Test
    public void testCharacterReader_consumeTo_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeTo('c');
        Assert.assertEquals("ab", consumed);
    }

    @Test
    public void testCharacterReader_consumeTo_EdgeCase_EndOfInput() {
        CharacterReader reader = new CharacterReader(new StringReader(""));
        String consumed = reader.consumeTo(TokeniserState.nullChar);
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testCharacterReader_consumeTo_EdgeCase_ScanTargetNotInInput() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeTo('e');
        Assert.assertEquals("", consumed);
    }

    @Test
    public void testCharacterReader_consumeToAny_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToAny(',', ';');
        Assert.assertEquals("", consumed);
        reader.advance();
        consumed = reader.consumeToAny(',');
        Assert.assertEquals("a", consumed);
        reader.advance();
        consumed = reader.consumeToAny(',');
        Assert.assertEquals("ab", consumed);
        reader.advance();
        consumed = reader.consumeToAny(',');
        Assert.assertEquals("abcd", consumed);
    }

    @Test
    public void testCharacterReader_consumeToAnySorted_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToAnySorted('a', 'b', 'c', 'd');
        Assert.assertEquals("", consumed);
        reader.advance();
        consumed = reader.consumeToAnySorted('a', 'b', 'c', 'd');
        Assert.assertEquals("a", consumed);
        reader.advance();
        consumed = reader.consumeToAnySorted('a', 'b', 'c', 'd');
        Assert.assertEquals("ab", consumed);
        reader.advance();
        consumed = reader.consumeToAnySorted('a', 'b', 'c', 'd');
        Assert.assertEquals("abcd", consumed);
    }

    @Test
    public void testCharacterReader_consumeData_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd&e>"));
        String consumed = reader.consumeData();
        Assert.assertEquals("abcd", consumed);
    }

    @Test
    public void testCharacterReader_consumeTagName_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("    <title>Test</title>"));
        String consumed = reader.consumeTagName();
        Assert.assertEquals("title", consumed);
    }

    @Test
    public void testCharacterReader_consumeToEnd_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeToEnd();
        Assert.assertEquals("abcd", consumed);
    }

    @Test
    public void testCharacterReader_consumeLetterSequence_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        String consumed = reader.consumeLetterSequence();
        Assert.assertEquals("abcd", consumed);
    }

    @Test
    public void testCharacterReader_consumeLetterThenDigitSequence_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ab12cd"));
        String consumed = reader.consumeLetterThenDigitSequence();
        Assert.assertEquals("ab12", consumed);
    }

    @Test
    public void testCharacterReader_matches_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReader_matchesIgnoreCase_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"));
        Assert.assertTrue(reader.matchesIgnoreCase("abcd"));
    }

    @Test
    public void testCharacterReader_matchesAny_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReader_matchesAnySorted_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'a', 'b', 'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e'}));
    }

    @Test
    public void testCharacterReader_matchesLetter_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertTrue(reader.matchesLetter());
        reader.advance();
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReader_matchesDigit_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("1234"));
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertTrue(reader.matchesDigit());
        reader.advance();
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReader_matchConsume_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("abcd"));
        Assert.assertTrue(reader.matchConsume("ab"));
        Assert.assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_matchConsumeIgnoreCase_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("AbCd"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ab"));
        Assert.assertEquals("cd", reader.consumeToEnd());
    }

    @Test
    public void testCharacterReader_containsIgnoreCase_NormalCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<title>Test</title>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
    }
}