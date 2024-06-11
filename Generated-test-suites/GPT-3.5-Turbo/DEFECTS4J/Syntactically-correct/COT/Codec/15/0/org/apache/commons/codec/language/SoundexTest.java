import org.junit.Test;

public class SoundexTest {

    @Test
    public void testDifference() throws Exception {
        Soundex soundex = new Soundex();
        String s1 = "hello";
        String s2 = "hella";
        int result = soundex.difference(s1, s2);
        assertEquals(2, result);
    }
    
    @Test
    public void testEncode() throws Exception {
        Soundex soundex = new Soundex();
        String str = "hello";
        String result = soundex.encode(str);
        assertEquals("H400", result);
    }
    
    @Test(expected = EncoderException.class)
    public void testEncodeNonString() throws Exception {
        Soundex soundex = new Soundex();
        Object obj = new Object();
        soundex.encode(obj);
    }
    
    @Test
    public void testSoundexMapping() throws Exception {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();
        assertEquals("01230120022455012623010202", new String(mapping));
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testMapInvalidCharacter() throws Exception {
        Soundex soundex = new Soundex();
        soundex.map('!');
    }
    
    @Test
    public void testSoundex() throws Exception {
        Soundex soundex = new Soundex();
        String str = "hello";
        String result = soundex.soundex(str);
        assertEquals("H400", result);
    }
    
    @Test
    public void testSoundexNull() throws Exception {
        Soundex soundex = new Soundex();
        String str = null;
        String result = soundex.soundex(str);
        assertNull(result);
    }
    
    @Test
    public void testSoundexEmptyString() throws Exception {
        Soundex soundex = new Soundex();
        String str = "";
        String result = soundex.soundex(str);
        assertEquals("", result);
    }
    
    @Test
    public void testSoundexSingleCharacter() throws Exception {
        Soundex soundex = new Soundex();
        String str = "a";
        String result = soundex.soundex(str);
        assertEquals("A000", result);
    }
    
    @Test
    public void testSoundexSameCharacter() throws Exception {
        Soundex soundex = new Soundex();
        String str = "aaaa";
        String result = soundex.soundex(str);
        assertEquals("A000", result);
    }
    
    @Test
    public void testSoundexUpperCase() throws Exception {
        Soundex soundex = new Soundex();
        String str = "HELLO";
        String result = soundex.soundex(str);
        assertEquals("H400", result);
    }
    
    @Test
    public void testSoundexMappedCharacter() throws Exception {
        Soundex soundex = new Soundex();
        String str = "AEIOU";
        String result = soundex.soundex(str);
        assertEquals("A000", result);
    }
    
    @Test
    public void testSoundexDifferentMapping() throws Exception {
        char[] mapping = new char[] {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(mapping);
        String str = "hello";
        String result = soundex.soundex(str);
        assertEquals("H355", result);
    }
    
    @Test
    public void testSoundexCustomMapping() throws Exception {
        Soundex soundex = new Soundex("01230000000000000000000000");
        String str = "hello";
        String result = soundex.soundex(str);
        assertEquals("H000", result);
    }

}