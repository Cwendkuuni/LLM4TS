import static org.junit.Assert.*;
import org.junit.Test;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(5, Utf8.encodedLength("hello"));
    }

    @Test
    public void testEncodedLengthGeneral() {
        assertEquals(2, Utf8.encodedLengthGeneral("hi", 0));
    }

    @Test
    public void testIsWellFormed() {
        byte[] bytes = {-50, 40, 70};
        assertTrue(Utf8.isWellFormed(bytes));
    }

    @Test
    public void testIsWellFormedSlowPath() {
        byte[] bytes = {-50, -80, 70};
        assertFalse(Utf8.isWellFormed(bytes, 0, 3));
    }

}