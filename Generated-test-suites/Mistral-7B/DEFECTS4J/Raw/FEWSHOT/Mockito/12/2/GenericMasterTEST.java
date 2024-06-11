package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() {
        // Test with a simple field
        Field field = getClass().getDeclaredField("dummyField");
        assertEquals(Integer.class, new GenericMaster().getGenericType(field));

        // Test with a generic field of type List<Integer>
        Field listField = ArrayList.class.getDeclaredField("elementData");
        assertEquals(List.class);
        assertEquals(Integer.class, new GenericMaster().getGenericType(listField).getComponentType());

        // Test with a generic field of type Map<String, List<Integer>>
        Field mapField = HashMap.class.getDeclaredField("entrySet");
        assertEquals(Set.class);
        assertEquals(Map.Entry.class);
        assertEquals("key", new GenericMaster().getGenericType(Map.Entry.class.getDeclaredField("getKey")).getTypeName());
        assertEquals(List.class);
        assertEquals("value", new GenericMaster().getGenericType(Map.Entry.class.getDeclaredField("getValue")).getComponentType().getTypeName());

        // Test with null field
        assertNull(new GenericMaster().getGenericType(null));

        // Test with non-generic field
        Field nonGenericField = String.class.getDeclaredField("value");
        assertEquals(Object.class, new GenericMaster().getGenericType(nonGenericField));
    }

    private String typeName(Type type) {
        return type.getTypeName();
    }
}