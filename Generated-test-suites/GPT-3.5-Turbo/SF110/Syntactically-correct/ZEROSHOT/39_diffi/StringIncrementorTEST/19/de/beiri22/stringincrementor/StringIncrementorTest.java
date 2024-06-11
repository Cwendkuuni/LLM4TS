import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StringIncrementorTest {

    private static final String a = "abcdefghijklmnopqrstuvwxyz";
    private static final String b = "abcdeffghijklmnopqrstuvwxyz";
    private static final String c = "abcdeffghijklmnopqrstuvwxyzzz";
    private static final String d = "abcdezzzffghijklmnopqrstuvwxyz";
    
    @Before
    public void setUp() {
        System.out.println("Setting up test...");
    }
    
    @After
    public void tearDown() {
        System.out.println("Tearing down test...");
    }
    
    @Test
    public void testDiff() {
        RelativeString result1 = StringIncrementor.diff(a, b);
        assertNotNull(result1);
        assertEquals(result1.getLinksCount(), 1);
        
        RelativeString result2 = StringIncrementor.diff(a, c);
        assertNotNull(result2);
        assertEquals(result2.getLinksCount(), 1);
        
        RelativeString result3 = StringIncrementor.diff(a, d);
        assertNotNull(result3);
        assertEquals(result3.getLinksCount(), 2);
    }
    
    @Test
    public void testDiffWithVerbose() {
        RelativeString result1 = StringIncrementor.diff(a, b, true);
        assertNotNull(result1);
        assertEquals(result1.getLinksCount(), 1);
        
        RelativeString result2 = StringIncrementor.diff(a, c, true);
        assertNotNull(result2);
        assertEquals(result2.getLinksCount(), 1);
        
        RelativeString result3 = StringIncrementor.diff(a, d, true);
        assertNotNull(result3);
        assertEquals(result3.getLinksCount(), 2);
    }
    
    @Test
    public void testPatch() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(5, 2, 4));
        
        String result1 = StringIncrementor.patch(a, rs);
        assertNotNull(result1);
        assertEquals(result1, "abcdffghijklmnopqrstuvwxyz");
        
        String result2 = StringIncrementor.patch(b, rs);
        assertNotNull(result2);
        assertEquals(result2, "abcdeffghijklmnopqrstuvwxyz");
    }
    
    @Test
    public void testPatchWithVerbose() {
        RelativeString rs = new RelativeString();
        rs.addLink(new StringLink(5, 2, 4));
        
        String result1 = StringIncrementor.patch(a, rs, true);
        assertNotNull(result1);
        assertEquals(result1, "abcdffghijklmnopqrstuvwxyz");
        
        String result2 = StringIncrementor.patch(b, rs, true);
        assertNotNull(result2);
        assertEquals(result2, "abcdeffghijklmnopqrstuvwxyz");
    }

}