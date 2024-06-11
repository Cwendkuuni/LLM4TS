import org.junit.Assert;
import org.junit.Test;

public class SoundexTest {

    @Test
    public void testSoundex_DefaultMapping() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("A000", soundex.encode("Amy"));
        Assert.assertEquals("B625", soundex.encode("Brian"));
        Assert.assertEquals("C500", soundex.encode("Catherine"));
        Assert.assertEquals("D250", soundex.encode("Daniel"));
    }

    @Test
    public void testSoundex_CustomMapping() {
        char[] customMapping = {'0', '1', '2', '3', '4', '1', '6', '7', '8', '9'};
        Soundex soundex = new Soundex(customMapping);
        Assert.assertEquals("14", soundex.encode("Amy"));
        Assert.assertEquals("81", soundex.encode("Brian"));
        Assert.assertEquals("010", soundex.encode("Catherine"));
        Assert.assertEquals("07", soundex.encode("Daniel"));
    }

    @Test(expected = EncoderException.class)
    public void testSoundex_EncodeNonString() throws EncoderException {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testSoundex_EncodeEmptyString() {
        Soundex soundex = new Soundex();
        Assert.assertEquals("", soundex.encode(""));
    }

    @Test
    public void testSoundex_EncodeNullString() {
        Soundex soundex = new Soundex();
        Assert.assertNull(soundex.encode(null));
    }

    @Test
    public void testSoundex_Difference() throws EncoderException {
        Soundex soundex = new Soundex();
        Assert.assertEquals(4, soundex.difference("Amy", "Amy"));
        Assert.assertEquals(0, soundex.difference("Amy", "Brian"));
        Assert.assertEquals(2, soundex.difference("Catherine", "Katherine"));
        Assert.assertEquals(3, soundex.difference("Daniel", "Danielle"));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSoundex_GetMappingCode_InvalidCharacter() {
        Soundex soundex = new Soundex();
        soundex.privateGetMappingCode("Amy", 3);
    }

    @Test
    public void testSoundex_GetMappingCode_HWRule() {
        Soundex soundex = new Soundex();
        Assert.assertEquals('0', soundex.privateGetMappingCode("AWy", 2));
        Assert.assertEquals('0', soundex.privateGetMappingCode("AWyZ", 3));
        Assert.assertEquals('0', soundex.privateGetMappingCode("AHWyZ", 4));
        Assert.assertEquals('9', soundex.privateGetMappingCode("AHWZ", 3));
    }

    @Test
    public void testSoundex_GetMappingCode_ValidCharacter() {
        Soundex soundex = new Soundex();
        Assert.assertEquals('0', soundex.privateGetMappingCode("Amy", 0));
        Assert.assertEquals('1', soundex.privateGetMappingCode("Brian", 1));
        Assert.assertEquals('2', soundex.privateGetMappingCode("Catherine", 4));
        Assert.assertEquals('5', soundex.privateGetMappingCode("Daniel", 3));
    }

    @Test
    public void testSoundex_GetMaxLength() {
        Soundex soundex = new Soundex();
        Assert.assertEquals(4, soundex.getMaxLength());
    }

    @Test
    public void testSoundex_GetSoundexMapping() {
        Soundex soundex = new Soundex();
        Assert.assertArrayEquals(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '2', '2', '4', '5', '5', '5', '0', '1', '2', '2', '3', '0', '1', '0'}, soundex.getSoundexMapping());
    }

    @Test
    public void testSoundex_SetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        Assert.assertEquals(5, soundex.getMaxLength());
    }

    @Test
    public void testSoundex_Soundex() {
        Soundex soundex = new Soundex();
        Assert.assertArrayEquals(new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '0', '2', '2', '4', '5', '5', '5', '0', '1', '2', '2', '3', '0', '1', '0'}, soundex.getSoundexMapping());
    }
}