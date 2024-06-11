import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import org.apache.commons.codec.EncoderException;
import org.apache.commons.codec.StringEncoder;
import org.junit.Test;

public class SoundexUtilsTest {
  
    static class MockEncoder implements StringEncoder {
      
        public String encode(String str) throws EncoderException {
            // Mock implementation of the encode method
            return str;
        }
      
    }
  
    @Test
    public void testClean() {
        assertEquals("FOO", SoundexUtils.clean("foo"));
        assertEquals("BAR", SoundexUtils.clean("bar"));
        assertNull(SoundexUtils.clean(null));
    }
  
    @Test
    public void testDifference() throws EncoderException {
        MockEncoder encoder = new MockEncoder();
        assertEquals(0, SoundexUtils.difference(encoder, "foo", "bar"));
        assertEquals(4, SoundexUtils.difference(encoder, "foo", "foo"));
    }
  
    @Test
    public void testDifferenceEncoded() {
        assertEquals(0, SoundexUtils.differenceEncoded("FOO", "BAR"));
        assertEquals(4, SoundexUtils.differenceEncoded("FOO", "FOO"));
    }
}