import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("kfatysia", caverphone.caverphone("catfish"));
        assertEquals("A2f3", caverphone.caverphone("apple"));
        assertEquals("krdytosia", caverphone.caverphone("credit"));

    }

    @Test
    public void testEncodeObject() {
        Caverphone caverphone = new Caverphone();
        try {
            caverphone.encode(new Integer(5));
            fail("Exception not thrown for non-String parameter");
        } catch (EncoderException e) {
            // Exception should be thrown as parameter is not of type String
        }
    }

    @Test
    public void testEncodeString() {
        Caverphone caverphone = new Caverphone();
        assertEquals("kfatysia", caverphone.encode("catfish"));
        assertEquals("A2f3", caverphone.encode("apple"));
        assertEquals("krdytosia", caverphone.encode("credit"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();
        assertTrue(caverphone.isCaverphoneEqual("cat", "kat"));
        assertFalse(caverphone.isCaverphoneEqual("dog", "doggy"));
    }

    @Test
    public void testEdgeCases() {
        Caverphone caverphone = new Caverphone();
        assertEquals("ktestosia", caverphone.caverphone("test"));
        assertEquals("kwto", caverphone.caverphone("two"));
        assertEquals("WO2nsi", caverphone.caverphone("wonder"));
    }

    @Test
    public void testExceptions() {
        Caverphone caverphone = new Caverphone();
        try {
            String output = caverphone.caverphone("input123");
            fail("No exception thrown for input containing numbers");
        } catch (Exception e) {
            // Exception should be thrown for input containing numbers
        }
    }

}