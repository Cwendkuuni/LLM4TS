import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("216", caverphone.caverphone("Hello World"));
        assertEquals("216", caverphone.caverphone("hello world"));
        assertEquals("216", caverphone.caverphone("HELLO WORLD"));
        assertEquals("216", caverphone.caverphone("123hello!!!!"));
        assertEquals("166222", caverphone.caverphone("Cough"));
        assertEquals("246222", caverphone.caverphone("Rough"));
        assertEquals("256222", caverphone.caverphone("Tough"));
        assertEquals("206222", caverphone.caverphone("Enough"));
        assertEquals("286222", caverphone.caverphone("TrOugh"));
        assertEquals("2622n", caverphone.caverphone("Gn"));
        assertEquals("292m2", caverphone.caverphone("Mb"));
        assertEquals("1ks3f1222sio3n", caverphone.caverphone("Checkpointing"));
    }

    @Test
    public void testEncode() throws EncoderException {
        Caverphone caverphone = new Caverphone();
        assertEquals("216", caverphone.encode("Hello World"));
        assertEquals("216", caverphone.encode("hello world"));
        assertEquals("216", caverphone.encode("HELLO WORLD"));
        assertEquals("216", caverphone.encode("123hello!!!!"));
        assertEquals("166222", caverphone.encode("Cough"));
        assertEquals("246222", caverphone.encode("Rough"));
        assertEquals("256222", caverphone.encode("Tough"));
        assertEquals("206222", caverphone.encode("Enough"));
        assertEquals("286222", caverphone.encode("TrOugh"));
        assertEquals("2622n", caverphone.encode("Gn"));
        assertEquals("292m2", caverphone.encode("Mb"));
        assertEquals("1ks3f1222sio3n", caverphone.encode("Checkpointing"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("hello", "world"));
        assertFalse(caverphone.isCaverphoneEqual("Hello", "hello"));
        assertFalse(caverphone.isCaverphoneEqual("HELLO", "hello"));
    }
}