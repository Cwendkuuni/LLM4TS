package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field field = MyClass.class.getField("intList");

        Class actualGenericType = GenericMaster.getGenericType(field);

        assertEquals(Integer.class, actualGenericType);
    }

    @Test
    public void testGetGenericType_NotGeneric() throws NoSuchFieldException {
        Field field = MyClass.class.getField("name");

        Class actualGenericType = GenericMaster.getGenericType(field);

        assertEquals(Object.class, actualGenericType);
    }

    @Test
    public void testGetGenericType_NestedGenerics() throws NoSuchFieldException {
        Field field = MyClass.class.getField("map");

        Class actualGenericType = GenericMaster.getGenericType(field);

        assertEquals(Map.class, actualGenericType);
    }

    @Test
    public void testGetGenericType_NullGeneric() throws NoSuchFieldException {
        Field field = MyClass.class.getField("country");

        Class actualGenericType = GenericMaster.getGenericType(field);

        assertEquals(Object.class, actualGenericType);
    }

    // Support class for tests
    class MyClass {
        public List<Integer> intList;
        public String name;
        public Map<String, Integer> map;
        public Country country;
    }
}