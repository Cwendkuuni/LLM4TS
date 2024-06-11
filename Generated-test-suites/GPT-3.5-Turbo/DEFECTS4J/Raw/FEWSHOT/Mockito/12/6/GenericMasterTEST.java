package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        //Test case 1
        Field field1 = GenericMaster.class.getDeclaredField("field1");
        Class expected1 = Object.class;
        Class actual1 = GenericMaster.getGenericType(field1);
        assertEquals(expected1, actual1);
        
        //Test case 2
        Field field2 = GenericMaster.class.getDeclaredField("field2");
        Class expected2 = Integer.class;
        Class actual2 = GenericMaster.getGenericType(field2);
        assertEquals(expected2, actual2);
        
        //Test case 3
        Field field3 = GenericMaster.class.getDeclaredField("field3");
        Class expected3 = String.class;
        Class actual3 = GenericMaster.getGenericType(field3);
        assertEquals(expected3, actual3);
    }
}