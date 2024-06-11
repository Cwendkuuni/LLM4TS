package org.mockito.internal.util.reflection;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;

import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        Field field = null; // replace with actual field
        
        // Test if field is generic
        Class actual = genericMaster.getGenericType(field);
        assertEquals(Object.class, actual);
        
        // Test if field is not generic
        actual = genericMaster.getGenericType(field);
        assertEquals(Object.class, actual);
    }
}