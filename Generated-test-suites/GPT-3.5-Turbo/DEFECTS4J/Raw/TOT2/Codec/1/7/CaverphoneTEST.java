import static org.junit.Assert.*;
import org.junit.Test;

public class CaverphoneTest {

    Caverphone caverphone = new Caverphone();

    @Test
    public void testCaverphoneBasic() {
        assertEquals("1111111111", caverphone.caverphone(""));
        assertEquals("2222222222", caverphone.caverphone(" "));
        assertEquals("KKAAFMN111", caverphone.caverphone("kafman"));
        assertEquals("RFL3KA2222", caverphone.caverphone("refulka"));
    }

    @Test
    public void testCaverphoneEdgeCases() {
        assertEquals("3333333333", caverphone.caverphone("aeiou"));
        assertEquals("C3NS!!@#11", caverphone.caverphone("c3ns!!@#11"));
        assertEquals("AS3FN#MN3N", caverphone.caverphone("as3fn#mn3n"));
    }

    @Test
    public void testCaverphoneExceptions() {
        String str1 = "hello";
        String str2 = null;

        assertFalse(caverphone.isCaverphoneEqual(str1, str2));
    }
}