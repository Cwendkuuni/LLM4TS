import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

import org.junit.Test;

public class StringIncrementorTest {
    
    @Test
    public void testDiff() {
        RelativeString result = StringIncrementor.diff("abc", "def");
        assertNotNull(result);
        assertTrue(result.getAbsolute().isEmpty());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testDiffVerbose() {
        RelativeString result = StringIncrementor.diff("abc", "ab", true);
        assertNotNull(result);
        assertTrue(result.getAbsolute().isEmpty());
        assertEquals(0, result.linksCount());
    }
    
    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        String patched = StringIncrementor.patch("abc", rs);
        assertEquals("abc", patched);
    }
    
    @Test
    public void testPatchVerbose() {
        RelativeString rs = new RelativeString();
        String patched = StringIncrementor.patch("abc", rs, true);
        assertEquals("abc", patched);
    }
}