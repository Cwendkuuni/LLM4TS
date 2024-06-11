package org.mockito.internal.util.reflection;

import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.List;
import static org.junit.Assert.assertEquals;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field genericField = Example.class.getDeclaredField("genericList");
        Field nonGenericField = Example.class.getDeclaredField("nonGenericList");

        Class genericType = GenericMaster.getGenericType(genericField);
        assertEquals(List.class, genericType);

        Class nonGenericType = GenericMaster.getGenericType(nonGenericField);
        assertEquals(Object.class, nonGenericType);
    }

    private class Example {
        private List<String> genericList;
        private String nonGenericList;
    }
}