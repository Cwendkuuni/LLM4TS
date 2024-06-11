
import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {
    
    @Test
    public void testGetValue_existingField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "object";
        
        assertNotNull(jclo.getValue(key));
    }
    
    @Test
    public void testGetValue_nonExistingField() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "nonExistent";
        
        assertNull(jclo.getValue(key));
    }
    
    @Test
    public void testSetValue_success() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "object";
        
        jclo.getObject(jclo.getField(key));
        
        assertEquals(obj, jclo.getObject(jclo.getField(key)));
    }
    
    @Test
    public void testSetValue_exceptionHandling() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        String key = "object";
        
        try {
            jclo.setObject(jclo.getField(key), "invalidType");
            fail("Exception not thrown");
        } catch (IllegalArgumentException e) {
            // Exception properly thrown
        }
    }
}
