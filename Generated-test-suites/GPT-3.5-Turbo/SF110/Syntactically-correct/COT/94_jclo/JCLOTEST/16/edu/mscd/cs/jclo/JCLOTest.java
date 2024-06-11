import org.junit.Test;
import static org.junit.Assert.*;

public class JCLOTest {
    @Test
    public void testJCLOObject() {
        // Test with a normal object
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertEquals(obj, jclo.getValue("object"));
        
        // Test with a null object
        JCLO jcloNull = new JCLO(null);
        assertNull(jcloNull.getValue("object"));
    }
}