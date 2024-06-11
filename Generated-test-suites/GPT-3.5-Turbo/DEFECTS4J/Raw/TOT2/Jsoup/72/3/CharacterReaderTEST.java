import org.jsoup.UncheckedIOException;
import org.jsoup.helper.Validate;
import org.junit.Before;
import org.junit.Test;

import java.io.Reader;
import java.io.StringReader;

import static org.junit.Assert.*;



public class CharacterReaderTest {

    private CharacterReader characterReader;

    @Before
    public void setUp() {
        Reader reader = new StringReader("Test String");
        characterReader = new CharacterReader(reader);
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
        assertEquals('e', characterReader.current());
    }

    @Test
    public void testUnconsume() {
        characterReader.consume();
        characterReader.unconsume();
        assertEquals('T', characterReader.current());
    }

    // Add more test cases for other methods as needed...

    @Test
    public void testConsumeToEnd() {
        assertEquals("est String", characterReader.consumeToEnd());
        assertTrue(characterReader.isEmpty());
    }

    @Test(expected = UncheckedIOException.class)
    public void testBufferUpIOException() {
        Reader reader = new StringReader("Test String");
        CharacterReader readerWithException = new CharacterReader(reader, -1);
        readerWithException.bufferUp();
    }

}

