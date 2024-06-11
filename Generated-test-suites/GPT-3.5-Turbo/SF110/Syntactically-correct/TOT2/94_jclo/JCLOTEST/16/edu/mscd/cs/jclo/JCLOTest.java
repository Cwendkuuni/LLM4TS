package edu.mscd.cs.jclo;
import static org.junit.Assert.*;
import org.junit.Test;
import java.lang.reflect.Field;

public class JCLOTest {
    
    @Test
    public void testGetValueSuccess() {
        JCLO jclo = new JCLO(new Object());
        Object obj = new Object();
        Field field = obj.getClass().getDeclaredFields()[0];
        assertEquals(obj, jclo.getValue(field.getName()));
    }

    @Test
    public void testGetValueNotFound() {
        JCLO jclo = new JCLO(new Object());
        assertNull(jclo.getValue("invalidField"));
    }

    // More test cases for other methods...

}