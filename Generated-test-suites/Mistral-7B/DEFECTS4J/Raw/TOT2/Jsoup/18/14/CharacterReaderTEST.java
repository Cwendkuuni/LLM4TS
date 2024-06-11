import org.jsoup.parser.CharacterReader;
import org.junit.Assert.*;

public class CharacterReaderTest {

    @Test
    public void testCharacterReader() {
        // ...
    }

    @Test
    public void testConsumeToCharEdgeCases() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        // test empty input
        reader = new CharacterReader("");
        assertEquals("", reader.consumeTo((char) 0));
        // test pos at end of input
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consumeTo('z');
        assertEquals("", reader.consumeTo((char) 0));
        // test pos before expected char
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consumeTo('b');
        assertEquals("a", reader.consumeAsString());
        // test input contains expected char multiple times
        reader = new CharacterReader("aaabbbccc");
        reader.consumeTo('a');
        assertEquals("aa", reader.consumeTo('a'));
        // test input contains other characters before or after the expected char
        reader = new CharacterReader("abcdabcdabcd");
        reader.consumeTo('a');
        assertEquals("abcd", reader.consumeTo('d'));
    }

    @Test
    public void testConsumeToStringEdgeCases() {
        CharacterReader reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        // test empty input
        reader = new CharacterReader("");
        assertEquals("", reader.consumeTo(""));
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consumeTo('z');
        reader.consumeTo('z');
        assertEquals("", reader.consumeTo(""));
        // test pos at end of input
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consumeTo("xyz"); // should not consume anything
        assertEquals("", reader.consumeAsString());
        // test pos before expected string
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyz");
        reader.consumeTo("efg");
        assertEquals("abcdef", reader.consumeTo("efg").substring(0, 3));
        // test input contains the expected string multiple times
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzefg");
        reader.consumeTo("efg");
        assertEquals("efg", reader.consumeTo("efg"));
        // test input contains other strings before or after the expected string
        reader = new CharacterReader("abcdefghijklmnopqrstuvwxyzefgxyz");
        reader.consumeTo("xyz");
        assertEquals("efgxyz", reader.consumeTo("xyz"));
    }

    // Add test cases for other methodswith similar edge cases and exception handling scenarios

    @Test
    public void testToString() {
        CharacterReader reader = new CharacterReader("Hello World!");
        reader.consumeToEnd();
        assertEquals("Hello World!", reader.toString());
    }
}