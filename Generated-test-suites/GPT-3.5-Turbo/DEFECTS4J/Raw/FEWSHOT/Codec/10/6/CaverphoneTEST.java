import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("a111111111", caverphone.caverphone("a"));
        assertEquals("a111111111", caverphone.caverphone("A"));
        assertEquals("abc1111111", caverphone.caverphone("abc"));
        assertEquals("bfm1111111", caverphone.caverphone("bfm"));
        assertEquals("1231111111", caverphone.caverphone("123"));
        assertEquals("1231111111", caverphone.caverphone("123abc"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));
        assertEquals("a111111111", caverphone.encode("a"));
        assertEquals("a111111111", caverphone.encode("A"));
        assertEquals("abc1111111", caverphone.encode("abc"));
        assertEquals("bfm1111111", caverphone.encode("bfm"));
        assertEquals("1231111111", caverphone.encode("123"));
        assertEquals("1231111111", caverphone.encode("123abc"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("abc", "abc"));
        assertTrue(caverphone.isCaverphoneEqual("a", "A"));
        assertFalse(caverphone.isCaverphoneEqual("abc", "def"));
        assertFalse(caverphone.isCaverphoneEqual("a", "b"));
    }

}