import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    private Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphone() {
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("KKAWWW" + "71111", caverphone.caverphone("kiwi"));
        assertEquals("A" + "55555" + "71111", caverphone.caverphone("apple"));
        assertEquals("M" + "71111" + "51111", caverphone.caverphone("mountain"));
    }

    @Test
    public void testEncodeString() {
        assertEquals("KKAWWW" + "71111", caverphone.encode("kiwi"));
        assertEquals("A" + "55555" + "71111", caverphone.encode("apple"));
        assertEquals("M" + "71111" + "51111", caverphone.encode("mountain"));
    }

    @Test
    public void testEncodeObject() {
        assertEquals("KKAWWW" + "71111", caverphone.encode((Object)"kiwi"));
        assertEquals("A" + "55555" + "71111", caverphone.encode((Object)"apple"));
        assertEquals("M" + "71111" + "51111", caverphone.encode((Object)"mountain"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        assertTrue(caverphone.isCaverphoneEqual("kiwi", "KIWI"));
        assertTrue(caverphone.isCaverphoneEqual("apple", "APPL"));
        assertFalse(caverphone.isCaverphoneEqual("kiwi", "apple"));
    }

}