import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

import org.junit.Before;
import org.junit.Test;

public class MetaphoneTest {
    
    private Metaphone metaphone;
    
    @Before
    public void setUp() {
        metaphone = new Metaphone();
    }
    
    // Test cases for metaphone() method
    
    @Test
    public void testMetaphoneWithEmptyString() {
        String result = metaphone.metaphone("");
        assertEquals("", result);
    }
    
    @Test
    public void testMetaphoneWithSingleCharacterString() {
        String result = metaphone.metaphone("a");
        assertEquals("A", result);
    }
    
    @Test
    public void testMetaphoneWithTwoCharacterString() {
        String result = metaphone.metaphone("ab");
        assertEquals("AB", result);
    }
    
    @Test
    public void testMetaphoneWithLeadingCharacterExceptions() {
        String result = metaphone.metaphone("kick");
        assertEquals("IK", result);
    }
    
    @Test
    public void testMetaphoneWithFirstCharacterAsA() {
        String result = metaphone.metaphone("agree");
        assertEquals("RES", result);
    }
    
    @Test
    public void testMetaphoneWithFirstCharacterAsWAndSecondCharacterAsR() {
        String result = metaphone.metaphone("wrong");
        assertEquals("RANK", result);
    }
    
    // Test cases for encode() method
    
    @Test
    public void testEncodeWithNonStringObject() {
        Object result = metaphone.encode(123);
        assertNotEquals("ABC", result);
    }
    
    @Test
    public void testEncodeWithStringObject() {
        Object result = metaphone.encode("abc");
        assertEquals("APK", result);
    }
    
    // Test cases for isMetaphoneEqual() method
    
    @Test
    public void testIsMetaphoneEqualWithEqualStrings() {
        boolean result = metaphone.isMetaphoneEqual("abc", "ab");
        assertEquals(true, result);
    }
    
    @Test
    public void testIsMetaphoneEqualWithUnequalStrings() {
        boolean result = metaphone.isMetaphoneEqual("abc", "def");
        assertEquals(false, result);
    }
    
    // Other test cases
    
    @Test
    public void testGetMaxCodeLen() {
        int result = metaphone.getMaxCodeLen();
        assertEquals(4, result);
    }
    
    @Test
    public void testSetMaxCodeLen() {
        metaphone.setMaxCodeLen(6);
        int result = metaphone.getMaxCodeLen();
        assertEquals(6, result);
    }
}