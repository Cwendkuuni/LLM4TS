import static org.junit.Assert.*;
import org.junit.Test;

public class SimpleCharStreamTest {

    @Test
    public void testBeginToken() {
        // testing BeginToken method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            char result = SimpleCharStream.BeginToken();
            assertEquals('T', result);
            assertEquals(1, SimpleCharStream.getBeginLine());
            assertEquals(1, SimpleCharStream.getBeginColumn());
            assertEquals(1, SimpleCharStream.getLine());
            assertEquals(2, SimpleCharStream.getColumn());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testReadChar() {
        // testing readChar method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            char result = SimpleCharStream.readChar();
            assertEquals('T', result);
            assertEquals(1, SimpleCharStream.getLine());
            assertEquals(2, SimpleCharStream.getColumn());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testGetImage() {
        // testing GetImage method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            SimpleCharStream.readChar();
            String result = SimpleCharStream.GetImage();
            assertEquals("T", result);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testFillBuff() {
        // testing FillBuff method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            SimpleCharStream.FillBuff();
            assertEquals('e', SimpleCharStream.buffer[SimpleCharStream.bufpos]);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testBackup() {
        // testing backup method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            SimpleCharStream.readChar();
            SimpleCharStream.backup(1);
            assertEquals('T', SimpleCharStream.buffer[SimpleCharStream.bufpos]);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

    @Test
    public void testAdjustBeginLineColumn() {
        // testing adjustBeginLineColumn method
        SimpleCharStream.ReInit(new StringReader("Test string"), 1, 1);
        try {
            SimpleCharStream.adjustBeginLineColumn(2, 5);
            assertEquals(2, SimpleCharStream.getBeginLine());
            assertEquals(5, SimpleCharStream.getBeginColumn());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }

}