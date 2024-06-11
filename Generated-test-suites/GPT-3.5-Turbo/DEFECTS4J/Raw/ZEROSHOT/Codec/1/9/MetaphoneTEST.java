import org.junit.Test;

import static org.junit.Assert.*;

public class MetaphoneTest {

    @Test
    public void testMetaphone() {
        Metaphone metaphone = new Metaphone();
        
        String code = metaphone.metaphone("hello");
        assertEquals("HL", code);
        
        code = metaphone.metaphone("world");
        assertEquals("WRLT", code);
        
        code = metaphone.metaphone("test");
        assertEquals("TST", code);
        
        code = metaphone.metaphone("java");
        assertEquals("JF", code);
        
        code = metaphone.metaphone("programming");
        assertEquals("PRKRMN", code);
    }

    @Test
    public void testEncode() {
        Metaphone metaphone = new Metaphone();
        
        String code = (String) metaphone.encode("hello");
        assertEquals("HL", code);
        
        code = (String) metaphone.encode("world");
        assertEquals("WRLT", code);
        
        code = (String) metaphone.encode("test");
        assertEquals("TST", code);
        
        code = (String) metaphone.encode("java");
        assertEquals("JF", code);
        
        code = (String) metaphone.encode("programming");
        assertEquals("PRKRMN", code);
    }

    @Test
    public void testIsMetaphoneEqual() {
        Metaphone metaphone = new Metaphone();
        
        boolean equal = metaphone.isMetaphoneEqual("hello", "hello");
        assertTrue(equal);
        
        equal = metaphone.isMetaphoneEqual("world", "world");
        assertTrue(equal);
        
        equal = metaphone.isMetaphoneEqual("test", "test");
        assertTrue(equal);
        
        equal = metaphone.isMetaphoneEqual("java", "java");
        assertTrue(equal);
        
        equal = metaphone.isMetaphoneEqual("programming", "programming");
        assertTrue(equal);
        
        equal = metaphone.isMetaphoneEqual("hello", "world");
        assertFalse(equal);
    }
}