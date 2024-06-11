
import org.junit.Test;
import static org.junit.Assert.*;

public class ProfileTest {
  
    @Test
    public void testInitProfiler() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setErr(new PrintStream(outContent));

        Profile.initProfiler();

        assertEquals("Java Interactive Profiler: starting\n", outContent.toString());
    }

    @Test
    public void testInit() {
        Profile.init();

        assertNotNull(Profile._threadActiveFrame);
        assertNotNull(Profile._threadDictionary);
        assertNotNull(Profile._methodDictionary);
        assertNotNull(Profile._frameList);
        assertNotNull(Profile._lock);
        assertNotNull(Profile._allocList);
    }
}
