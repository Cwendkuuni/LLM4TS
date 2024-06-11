import org.junit.Test;
import org.junit.Assert;

public class SoundexTest {

    @Test
    public void testEncode_Object_String() throws Exception {
        Soundex soundex = new Soundex();

        // Test basic functionality
        String encoded = soundex.encode("hello");
        Assert.assertEquals("H400", encoded);

        // Test empty string
        encoded = soundex.encode("");
        Assert.assertEquals("", encoded);
    }

    @Test
    public void testEncode_String_String() {
        // Test basic functionality
        String encoded = Soundex.soundex("hello");
        Assert.assertEquals("H400", encoded);

        // Test empty string
        encoded = Soundex.soundex("");
        Assert.assertEquals("", encoded);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testEncode_InvalidObject() throws Exception {
        Soundex soundex = new Soundex();
        soundex.encode(123);
    }

    @Test
    public void testDifference() throws Exception {
        Soundex soundex = new Soundex();

        // Test similarity between "hello" and "hella"
        int difference = soundex.difference("hello", "hella");
        Assert.assertEquals(3, difference);

        // Test similarity between "hello" and "world"
        difference = soundex.difference("hello", "world");
        Assert.assertEquals(0, difference);
    }

    @Test(expected = EncoderException.class)
    public void testDifference_InvalidString() throws Exception {
        Soundex soundex = new Soundex();
        soundex.difference("hello", 123);
    }

    @Test
    public void testConstructor_DefaultMapping() {
        Soundex soundex = new Soundex();
        char[] mapping = soundex.getSoundexMapping();

        // Test mapping of 'A'
        Assert.assertEquals('0', mapping['A' - 'A']);

        // Test mapping of 'B'
        Assert.assertEquals('1', mapping['B' - 'A']);

        // Test mapping of 'C'
        Assert.assertEquals('2', mapping['C' - 'A']);

        // Test mapping of 'D'
        Assert.assertEquals('3', mapping['D' - 'A']);

        // Test mapping of 'Z'
        Assert.assertEquals('2', mapping['Z' - 'A']);
    }

    @Test
    public void testConstructor_CustomMapping() {
        char[] customMapping = "123456".toCharArray();
        Soundex soundex = new Soundex(customMapping);
        char[] mapping = soundex.getSoundexMapping();

        // Test mapping of 'A'
        Assert.assertEquals('1', mapping['A' - 'A']);

        // Test mapping of 'B'
        Assert.assertEquals('2', mapping['B' - 'A']);

        // Test mapping of 'C'
        Assert.assertEquals('3', mapping['C' - 'A']);

        // Test mapping of 'D'
        Assert.assertEquals('4', mapping['D' - 'A']);

        // Test mapping of 'Z'
        Assert.assertEquals('6', mapping['Z' - 'A']);
    }

    @Test
    public void testConstructor_CustomMappingString() {
        String customMappingString = "123456";
        Soundex soundex = new Soundex(customMappingString);
        char[] mapping = soundex.getSoundexMapping();

        // Test mapping of 'A'
        Assert.assertEquals('1', mapping['A' - 'A']);

        // Test mapping of 'B'
        Assert.assertEquals('2', mapping['B' - 'A']);

        // Test mapping of 'C'
        Assert.assertEquals('3', mapping['C' - 'A']);

        // Test mapping of 'D'
        Assert.assertEquals('4', mapping['D' - 'A']);

        // Test mapping of 'Z'
        Assert.assertEquals('6', mapping['Z' - 'A']);
    }

    @Test
    public void testGetMaxLength() {
        Soundex soundex = new Soundex();
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(4, maxLength);
    }

    @Test
    public void testSetMaxLength() {
        Soundex soundex = new Soundex();
        soundex.setMaxLength(5);
        int maxLength = soundex.getMaxLength();
        Assert.assertEquals(5, maxLength);
    }

    @Test
    public void testSoundex() {
        Soundex soundex = new Soundex();

        // Test basic functionality
        String encoded = soundex.soundex("hello");
        Assert.assertEquals("H400", encoded);

        // Test empty string
        encoded = soundex.soundex("");
        Assert.assertEquals("", encoded);
    }
}

