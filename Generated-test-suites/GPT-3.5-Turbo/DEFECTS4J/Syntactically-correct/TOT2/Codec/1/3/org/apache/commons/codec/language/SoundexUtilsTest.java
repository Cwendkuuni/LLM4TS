import static org.junit.Assert.*;
import org.junit.Test;
import org.apache.commons.codec.EncoderException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class SoundexUtilsTest {

    @InjectMocks
    private SoundexUtils soundexUtils;

    @Test
    public void testClean() {
        assertEquals("HELLO", soundexUtils.clean("he123llo"));
        assertEquals("", soundexUtils.clean(""));
        assertNull(soundexUtils.clean(null));
    }

    @Test
    public void testDifference() throws EncoderException {
        assertEquals(4, soundexUtils.difference(new SoundexEncoder(), "hello", "hello"));
        assertEquals(0, soundexUtils.difference(new SoundexEncoder(), "world", "hello"));
        assertEquals(1, soundexUtils.difference(new SoundexEncoder(), "stack", "stuck"));
    }

    @Test
    public void testDifferenceEncoded() {
        assertEquals(4, soundexUtils.differenceEncoded("H452", "H452"));
        assertEquals(0, soundexUtils.differenceEncoded("W653", "H452"));
        assertEquals(1, soundexUtils.differenceEncoded("S530", "S520"));
    }
    
    @Test(expected = EncoderException.class)
    public void testDifferenceException() throws EncoderException {
        soundexUtils.difference(new SoundexEncoder(), null, "hello");
    }

}