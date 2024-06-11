import org.junit.Test;
import static org.junit.Assert.*;

public class Utf8Test {

    @Test
    public void testEncodedLength() {
        assertEquals(3, Utf8.encodedLength("abc"));
        assertEquals(6, Utf8.encodedLength("한글"));
        assertEquals(1, Utf8.encodedLength("a"));
        assertEquals(0, Utf8.encodedLength(""));
    }

    @Test
    public void testIsWellFormed() {
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99}));
        assertTrue(Utf8.isWellFormed(new byte[]{56, 76, 23}));
        assertFalse(Utf8.isWellFormed(new byte[]{-1, 34, 67}));
    }

    @Test
    public void testIsWellFormedWithOffsetAndLength() {
        assertTrue(Utf8.isWellFormed(new byte[]{97, 98, 99}, 0, 3));
        assertFalse(Utf8.isWellFormed(new byte[]{97, -1, 99}, 0, 3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncodedLengthGeneralWithIllegalArgumentException() {
        Utf8.encodedLengthGeneral("abc", 0);
    }

}