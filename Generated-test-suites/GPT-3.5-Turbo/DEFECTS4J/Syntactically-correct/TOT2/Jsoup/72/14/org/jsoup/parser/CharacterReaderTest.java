import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        String input = "This is a test string";
        characterReader = new CharacterReader(new StringReader(input));
    }

    @Test
    public void testPos() {
        assertEquals(0, characterReader.pos());
    }

    @Test
    public void testIsEmpty() {
        assertFalse(characterReader.isEmpty());
    }

    @Test
    public void testCurrent() {
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testConsume() {
        assertEquals('T', characterReader.consume());
        assertEquals('h', characterReader.consume());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    @Test
    public void testAdvance() {
        characterReader.advance();
        assertEquals('h', characterReader.current());
    }

    // More test cases for other methods...

    @Test
    public void testRangeEquals() {
        assertTrue(CharacterReader.rangeEquals(new char[] {'a', 'b', 'c'}, 0, 3, "abc"));
    }

}