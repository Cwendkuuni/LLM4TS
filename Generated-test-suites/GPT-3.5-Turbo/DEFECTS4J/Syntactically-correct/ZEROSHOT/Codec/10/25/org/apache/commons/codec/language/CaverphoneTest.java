import org.junit.Test;
import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test with empty input
        assertEquals("1111111111", caverphone.caverphone(""));

        // Test with valid input
        assertEquals("kfms111111", caverphone.caverphone("Apache Software Foundation"));

        // Test with special characters
        assertEquals("kfms111111", caverphone.caverphone("@@@Apache@@@ Software@ Foundation!"));

        // Test with lowercase input
        assertEquals("kfms111111", caverphone.caverphone("apache software foundation"));

        // Test with input containing digits
        assertEquals("kfms111111", caverphone.caverphone("Apache Software 123 Foundation"));

        // Test with input containing non-alphabetic characters
        assertEquals("kfms111111", caverphone.caverphone("Apache Software $% Foundation"));

        // Test with input containing start options
        assertEquals("kfm2f11111", caverphone.caverphone("Coughing implementation"));
        assertEquals("kfms111111", caverphone.caverphone("Coughy implementation"));
        assertEquals("rof1111111", caverphone.caverphone("Rough implementation"));
        assertEquals("rou2f11111", caverphone.caverphone("Rougy implementation"));
        assertEquals("tou2f11111", caverphone.caverphone("Tougy implementation"));
        assertEquals("enou2f1111", caverphone.caverphone("Enough implementation"));
        assertEquals("trou2f1111", caverphone.caverphone("Trough implementation"));
        assertEquals("ktrn111111", caverphone.caverphone("Gni implementation"));

        // Test with input containing replacements
        assertEquals("sfk1111111", caverphone.caverphone("Sfk implementation"));
        assertEquals("kooo111111", caverphone.caverphone("Coooo implementation"));
        assertEquals("si2ky11111", caverphone.caverphone("Ci2ktio implementation"));
        assertEquals("kf2g111111", caverphone.caverphone("Dg implementation"));
        assertEquals("sio2A11111", caverphone.caverphone("Sia implementation"));
        assertEquals("sip2A11111", caverphone.caverphone("Sip implementation"));
        assertEquals("Se11111111", caverphone.caverphone("Spit implementation"));
        assertEquals("fht1111111", caverphone.caverphone("Fight implementation"));
        assertEquals("p11e111111", caverphone.caverphone("Brett implementation"));
        assertEquals("si2e111111", caverphone.caverphone("Site implementation"));
        assertEquals("sh11111111", caverphone.caverphone("Sh implementation"));
        assertEquals("11111M1111", caverphone.caverphone("Z implementation"));
        assertEquals("kfA1111111", caverphone.caverphone("Apple implementation"));
        assertEquals("3111111111", caverphone.caverphone("Elephant implementation"));
        assertEquals("yk11111111", caverphone.caverphone("Y implementation"));
        assertEquals("A311111111", caverphone.caverphone("Why implementation"));
        assertEquals("3kh3111111", caverphone.caverphone("Sigh implementation"));
        assertEquals("22h1111111", caverphone.caverphone("Gh implementation"));
        assertEquals("kk11111111", caverphone.caverphone("K implementation"));
        assertEquals("SS11111111", caverphone.caverphone("SS implementation"));
        assertEquals("TT11111111", caverphone.caverphone("T implementation"));
        assertEquals("P111111111", caverphone.caverphone("P implementation"));
        assertEquals("KK11111111", caverphone.caverphone("Kk implementation"));
        assertEquals("F111111111", caverphone.caverphone("F implementation"));
        assertEquals("1111111111", caverphone.caverphone("M implementation"));
        assertEquals("NA11111111", caverphone.caverphone("N implementation"));
        assertEquals("W311111111", caverphone.caverphone("W implementation"));
        assertEquals("Wh311111111", caverphone.caverphone("Wh implementation"));
        assertEquals("23d1111111", caverphone.caverphone("Wood implementation"));
        assertEquals("AR3B111111", caverphone.caverphone("Har implementation"));
        assertEquals("2r3y111111", caverphone.caverphone("Ry implementation"));
        assertEquals("K23d111111", caverphone.caverphone("Could implementation"));
        assertEquals("AL3y111111", caverphone.caverphone("Ly implementation"));

        // Test with input containing removals
        assertEquals("kfms", caverphone.caverphone("Kfms2"));
        assertEquals("kfms", caverphone.caverphone("kfms2"));
        assertEquals("kfms2A", caverphone.caverphone("kfms3A"));

        // Test with input exceeding maximum length
        String longInput = "kd8jXE8hWXbCrzYAqh8jXE8hWXbCrzYAqh8jXE8hWXbCrzYAqh8jXE8hWXbCrzYAqh8jXE8hWXbCrzYAqh8jXE8hWXbCrzYAqh8jXE8hWXbCrzY";
        assertEquals("kfms11111", caverphone.caverphone(longInput));
    }

    @Test
    public void testEncode() {
        Caverphone caverphone = new Caverphone();

        // Test with null input
        assertEquals(null, caverphone.encode(null));

        // Test with valid input
        assertEquals("kfms111111", caverphone.encode("Apache Software Foundation"));
    }

    @Test
    public void testIsCaverphoneEqual() {
        Caverphone caverphone = new Caverphone();

        // Test with equal caverphone codes
        assertTrue(caverphone.isCaverphoneEqual("Apache Software Foundation", "Apache Software 123 Foundation"));

        // Test with different caverphone codes
        assertFalse(caverphone.isCaverphoneEqual("Apache Software Foundation", "Not Apache Software Foundation"));
    }
    
}