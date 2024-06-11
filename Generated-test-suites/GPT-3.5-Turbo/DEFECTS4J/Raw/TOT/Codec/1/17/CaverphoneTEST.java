import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals("1111111111", caverphone.caverphone(null));
        assertEquals("1111111111", caverphone.caverphone(""));

        // Edge cases
        assertEquals("1111111111", caverphone.caverphone("##$$%%^^"));
        assertEquals("cou2fgh153", caverphone.caverphone("Cough"));
        assertEquals("rou2f3153", caverphone.caverphone("Rough"));
        assertEquals("tou2fh153", caverphone.caverphone("Tough"));
        assertEquals("enou2fh153", caverphone.caverphone("Enough"));
        assertEquals("trou2fh153", caverphone.caverphone("Trough"));
        assertEquals("2n153gh153", caverphone.caverphone("Gn"));

        // Error scenarios
        // Test with non-alphabetic characters
        assertEquals("231531153", caverphone.caverphone("123abc"));
        // Test with mixed uppercase and lowercase letters
        assertEquals("153", caverphone.caverphone("CaveRPiphone"));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertEquals("1111111111", caverphone.encode(null));
        assertEquals("1111111111", caverphone.encode(""));

        // Edge cases
        assertEquals("1111111111", caverphone.encode("##$$%%^^"));
        assertEquals("cou2fgh153", caverphone.encode("Cough"));
        assertEquals("rou2f3153", caverphone.encode("Rough"));
        assertEquals("tou2fh153", caverphone.encode("Tough"));
        assertEquals("enou2fh153", caverphone.encode("Enough"));
        assertEquals("trou2fh153", caverphone.encode("Trough"));
        assertEquals("2n153gh153", caverphone.encode("Gn"));

        // Error scenarios
        // Test with non-alphabetic characters
        assertEquals("231531153", caverphone.encode("123abc"));
        // Test with mixed uppercase and lowercase letters
        assertEquals("153", caverphone.encode("CaveRPiphone"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Typical use cases
        assertTrue(caverphone.isCaverphoneEqual(null, null));
        assertTrue(caverphone.isCaverphoneEqual("", ""));
        assertTrue(caverphone.isCaverphoneEqual("Cough", "cou2fgh"));
        assertTrue(caverphone.isCaverphoneEqual("Rough", "rou2fh"));
        assertTrue(caverphone.isCaverphoneEqual("Tough", "tou2fh"));
        assertTrue(caverphone.isCaverphoneEqual("Enough", "enou2fh"));
        assertTrue(caverphone.isCaverphoneEqual("Trough", "trou2fh"));
        assertTrue(caverphone.isCaverphoneEqual("Gn", "2ngh"));

        // Edge cases
        assertFalse(caverphone.isCaverphoneEqual("Cough", "Rough"));
        assertFalse(caverphone.isCaverphoneEqual("Tough", "Gn"));

        // Error scenarios
        // Test with non-alphabetic characters
        assertFalse(caverphone.isCaverphoneEqual("123abc", "456def"));
        // Test with mixed uppercase and lowercase letters
        assertFalse(caverphone.isCaverphoneEqual("CaveRPiphone", "CaverrRphone"));
    }

}