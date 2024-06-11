
package org.mockito.internal.util.reflection;

import org.junit.Test;
import static org.junit.Assert.*;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Test case for when the field is generic
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = genericMaster.getGenericType(TestClass.class.getDeclaredField("testField"));
        assertEquals(String.class, clazz);
    }

    @Test
    public void testGetGenericTypeEdgeCases() {
        // Test case for when the field is not generic
        GenericMaster genericMaster = new GenericMaster();
        Class clazz = genericMaster.getGenericType(TestClass.class.getDeclaredField("nonGenericField"));
        assertEquals(Object.class, clazz);
    }

}
