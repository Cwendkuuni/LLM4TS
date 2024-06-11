
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StringIncrementorTest {
    
    @Test
    public void diffTest() {
        // Test 1: Positive scenario
        String a1 = "abcdefg";
        String b1 = "abcdxyz";
        RelativeString result1 = StringIncrementor.diff(a1, b1);
        assertTrue(result1.getLinks().isEmpty());
        assertEquals("efg", result1.getAbsolute());
        
        // Test 2: Positive scenario with verbose
        String a2 = "abcdefg";
        String b2 = "abcdxyz";
        RelativeString result2 = StringIncrementor.diff(a2, b2, true);
        assertTrue(result2.getLinks().isEmpty());
        assertEquals("efg", result2.getAbsolute());
    }
    
    @Test
    public void patchTest() {
        // Test 1: Positive scenario
        String a1 = "abcdefg";
        RelativeString r1 = new RelativeString();
        StringLink sl1 = new StringLink(1, 0, 2);
        r1.addLink(sl1);
        
        String result1 = StringIncrementor.patch(a1, r1);
        assertEquals("abbcdefg", result1);
        
        // Test 2: Positive scenario with verbose
        String a2 = "abcdefg";
        RelativeString r2 = new RelativeString();
        StringLink sl2 = new StringLink(1, 0, 2);
        r2.addLink(sl2);
        
        String result2 = StringIncrementor.patch(a2, r2, true);
        assertEquals("abbcdefg", result2);
    }
    
}
