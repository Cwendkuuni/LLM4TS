import org.junit.Test;

public class SoundexUtilsTest {

    @Test
    public void testClean() {
        // Test with null string
        String result = SoundexUtils.clean(null);
        org.junit.Assert.assertNull(result);

        // Test with empty string
        result = SoundexUtils.clean("");
        org.junit.Assert.assertEquals("", result);

        // Test with string containing non-letter characters
        result = SoundexUtils.clean("123");
        org.junit.Assert.assertEquals("", result);

        // Test with string containing lower case letters
        result = SoundexUtils.clean("abc");
        org.junit.Assert.assertEquals("ABC", result);

        // Test with string containing upper case letters
        result = SoundexUtils.clean("ABC");
        org.junit.Assert.assertEquals("ABC", result);

        // Test with string containing both upper case and lower case letters
        result = SoundexUtils.clean("aBc");
        org.junit.Assert.assertEquals("ABC", result);
    }

    @Test
    public void testDifference() {
        // Test with encoder, s1, and s2 all set to null
        try {
            int result = SoundexUtils.difference(null, null, null);
            org.junit.Assert.assertEquals(0, result);
        } catch (EncoderException e) {
            org.junit.Assert.fail("Exception should not be thrown");
        }

        // Test with encoder set to null
        try {
            int result = SoundexUtils.difference(null, "abc", "def");
            org.junit.Assert.assertEquals(0, result);
        } catch (EncoderException e) {
            org.junit.Assert.fail("Exception should not be thrown");
        }

        // Test with s1 set to null
        try {
            int result = SoundexUtils.difference(new MyEncoder(), null, "def");
            org.junit.Assert.assertEquals(0, result);
        } catch (EncoderException e) {
            org.junit.Assert.fail("Exception should not be thrown");
        }

        // Test with s2 set to null
        try {
            int result = SoundexUtils.difference(new MyEncoder(), "abc", null);
            org.junit.Assert.assertEquals(0, result);
        } catch (EncoderException e) {
            org.junit.Assert.fail("Exception should not be thrown");
        }

        // Test with valid encoder, s1, and s2
        try {
            int result = SoundexUtils.difference(new MyEncoder(), "abc", "def");
            org.junit.Assert.assertEquals(1, result);
        } catch (EncoderException e) {
            org.junit.Assert.fail("Exception should not be thrown");
        }
    }

    @Test
    public void testDifferenceEncoded() {
        // Test with null encoded strings
        int result = SoundexUtils.differenceEncoded(null, null);
        org.junit.Assert.assertEquals(0, result);

        // Test with one null encoded string
        result = SoundexUtils.differenceEncoded("ABC", null);
        org.junit.Assert.assertEquals(0, result);

        // Test with empty encoded strings
        result = SoundexUtils.differenceEncoded("", "");
        org.junit.Assert.assertEquals(0, result);

        // Test with different lengths of encoded strings
        result = SoundexUtils.differenceEncoded("ABC", "ABCD");
        org.junit.Assert.assertEquals(3, result);

        // Test with same encoded strings
        result = SoundexUtils.differenceEncoded("ABC", "ABC");
        org.junit.Assert.assertEquals(3, result);
    }

    private class MyEncoder implements StringEncoder {
        @Override
        public String encode(String source) throws EncoderException {
            return source.toUpperCase();
        }
    }

}