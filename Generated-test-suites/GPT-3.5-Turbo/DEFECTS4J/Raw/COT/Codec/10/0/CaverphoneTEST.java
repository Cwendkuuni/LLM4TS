import org.junit.Test;

import static org.junit.Assert.*;

public class CaverphoneTest {

    @Test
    public void testCaverphone() {
        Caverphone caverphone = new Caverphone();

        // Test empty input
        String result1 = caverphone.caverphone("");
        assertEquals("1111111111", result1);

        // Test lowercase input
        String result2 = caverphone.caverphone("abcdefghijklmnopqrstuvwxyz");
        assertEquals("1111111111", result2);

        // Test removing final e
        String result3 = caverphone.caverphone("hello");
        assertEquals("hol", result3);

        // Test various start options
        String result4 = caverphone.caverphone("cough");
        assertEquals("cou2f", result4);

        String result5 = caverphone.caverphone("rough");
        assertEquals("rou2f", result5);

        String result6 = caverphone.caverphone("tough");
        assertEquals("tou2f", result6);

        String result7 = caverphone.caverphone("gnash");
        assertEquals("2nash", result7);

        // Test replacements
        String result8 = caverphone.caverphone("cq");
        assertEquals("2q", result8);

        String result9 = caverphone.caverphone("ci");
        assertEquals("si", result9);

        String result10 = caverphone.caverphone("ce");
        assertEquals("se", result10);

        String result11 = caverphone.caverphone("cy");
        assertEquals("sy", result11);

        // Test isCaverphoneEqual
        boolean result12 = caverphone.isCaverphoneEqual("hello", "hol");
        assertTrue(result12);

        boolean result13 = caverphone.isCaverphoneEqual("hello", "world");
        assertFalse(result13);
    }
}