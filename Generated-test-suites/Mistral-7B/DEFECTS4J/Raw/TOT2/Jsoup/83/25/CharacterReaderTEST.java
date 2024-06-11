import org.jsoup.parser.CharacterReader;
import org.jsoup.UncheckedIOException;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class CharacterReaderTest {

    @Test
    public void testCharacterReaderConstructionWithReader() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        Assert.assertEquals(10, reader.pos());
        reader.consume();
        Assert.assertEquals(9, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(10, reader.pos());
    }

    @Test
    public void testCharacterReaderConstructionWithString() {
        CharacterReader reader = new CharacterReader("Hello World");
        Assert.assertEquals("Hello World", reader.toString());
        Assert.assertEquals(11, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        Assert.assertEquals(10, reader.pos());
        reader.consume();
        Assert.assertEquals(9, reader.pos());
        reader.rewindToMark();
        Assert.assertEquals(11, reader.pos());
    }

    @Test
    public void testCharacterReaderBufferUp() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        reader.bufferUp();
        Assert.assertEquals(11, reader.pos());
        Assert.assertEquals(11, reader.bufPos);
        Assert.assertFalse(reader.isEmpty());
        reader.consume();
        reader.bufferUp();
        Assert.assertEquals(10, reader.pos());
        Assert.assertEquals(11, reader.bufPos);
        reader.consume();
        reader.bufferUp();
        Assert.assertEquals(9, reader.pos());
        Assert.assertEquals(11, reader.bufPos);
    }

    @Test(expected = UncheckedIOException.class)
    public void testCharacterReaderIOException() throws IOException {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World")) {
            @Override
            public void bufferUp() {
                throw new IOException("Test IOException");
            }
        };
        reader.consume();
    }

    @Test
    public void testCharacterReaderConsumeToEnd() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeToEnd();
        Assert.assertEquals("Hello World", data);
        Assert.assertEquals(0, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTo() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeTo('W');
        Assert.assertEquals("Hello ", data);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToSequence() {
        CharacterReader reader = new CharacterReader(new StringReader("Hello World"));
        String data = reader.consumeTo("llo ");
        Assert.assertEquals("Hello ", data);
        Assert.assertEquals(6, reader.pos());
        Assert.assertFalse(reader.isEmpty());
        data = reader.consumeTo("ld");
        Assert.assertEquals("Hello World", data);
        Assert.assertEquals(11, reader.pos());
        Assert.assertTrue(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAny() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String data = reader.consumeToAny(new char[]{'1', '3', '5', '7'});
        Assert.assertEquals("123", data);
        Assert.assertEquals(3, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeToAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("1234567890"));
        String data = reader.consumeToAnySorted(new char[]{'5', '7'});
        Assert.assertEquals("56", data);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeData() {
        CharacterReader reader = new CharacterReader(new StringReader("&<null"));
        String data = reader.consumeData();
        Assert.assertEquals("&<", data);
        Assert.assertEquals(2, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderConsumeTagName() {
        CharacterReader reader = new CharacterReader(new StringReader("<h1 class=\"title\">Hello World</h1>"));
        String data = reader.consumeTagName();
        Assert.assertEquals("<h1 class=\"title\"", data);
        Assert.assertEquals(12, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatches() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matches('a'));
        Assert.assertTrue(reader.matches('b'));
        Assert.assertTrue(reader.matches('c'));
        Assert.assertTrue(reader.matches('d'));
        Assert.assertFalse(reader.matches('e'));
    }

    @Test
    public void testCharacterReaderMatchesIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDABCDABCD"));
        Assert.assertTrue(reader.matchesIgnoreCase("a"));
        Assert.assertTrue(reader.matchesIgnoreCase("b"));
        Assert.assertTrue(reader.matchesIgnoreCase("c"));
        Assert.assertTrue(reader.matchesIgnoreCase("d"));
        Assert.assertFalse(reader.matchesIgnoreCase("e"));
    }

    @Test
    public void testCharacterReaderMatchesAny() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAny('a', 'b', 'c', 'd'));
        Assert.assertFalse(reader.matchesAny('e'));
    }

    @Test
    public void testCharacterReaderMatchesAnySorted() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchesAnySorted(new char[]{'c', 'd'}));
        Assert.assertFalse(reader.matchesAnySorted(new char[]{'e'}));
    }

    @Test
    public void testCharacterReaderMatchesLetter() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdefghijklmnopqrstuvwxyz"));
        Assert.assertTrue(reader.matchesLetter());
        for (char c = 'a'; c <= 'z'; c++) {
            Assert.assertTrue(reader.matchesLetter());
        }
        Assert.assertFalse(reader.matchesLetter());
    }

    @Test
    public void testCharacterReaderMatchesDigit() {
        CharacterReader reader = new CharacterReader(new StringReader("0123456789"));
        Assert.assertTrue(reader.matchesDigit());
        for (char c = '0'; c <= '9'; c++) {
            Assert.assertTrue(reader.matchesDigit());
        }
        Assert.assertFalse(reader.matchesDigit());
    }

    @Test
    public void testCharacterReaderMatchConsume() {
        CharacterReader reader = new CharacterReader(new StringReader("abcdabcdabcd"));
        Assert.assertTrue(reader.matchConsume("abcd"));
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderMatchConsumeIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("ABCDABCDABCD"));
        Assert.assertTrue(reader.matchConsumeIgnoreCase("ABCD"));
        Assert.assertEquals(4, reader.pos());
        Assert.assertFalse(reader.isEmpty());
    }

    @Test
    public void testCharacterReaderContainsIgnoreCase() {
        CharacterReader reader = new CharacterReader(new StringReader("<h1 class=\"title\">Hello World</h1>"));
        Assert.assertTrue(reader.containsIgnoreCase("/title"));
        Assert.assertTrue(reader.containsIgnoreCase("title"));
        Assert.assertTrue(reader.containsIgnoreCase("class"));
        Assert.assertFalse(reader.containsIgnoreCase("/style"));
    }
}