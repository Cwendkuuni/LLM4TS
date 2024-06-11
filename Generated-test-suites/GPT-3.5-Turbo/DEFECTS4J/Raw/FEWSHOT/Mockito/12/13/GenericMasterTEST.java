package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() {
        // Initialize field and generic type
        Field field = null;
        Type genericType = null;

        // Test case when field is null
        assertEquals(Object.class, genericMaster.getGenericType(field));

        // Test case when generic type is null
        field = getField("fieldName", null);
        assertEquals(Object.class, genericMaster.getGenericType(field));

        // Test case when generic type is not a ParameterizedType
        field = getField("fieldName", Integer.class);
        assertEquals(Integer.class, genericMaster.getGenericType(field));

        // Test case when generic type is a ParameterizedType
        field = getField("fieldName", new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{String.class};
            }

            // Define other methods
        });
        assertEquals(String.class, genericMaster.getGenericType(field));

        // Test case when generic type is a nested ParameterizedType
        field = getField("fieldName", new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[]{new ParameterizedType() {
                    @Override
                    public Type[] getActualTypeArguments() {
                        return new Type[]{Integer.class};
                    }

                    // Define other methods
                }};
            }

            // Define other methods
        });
        assertEquals(ParameterizedType.class, genericMaster.getGenericType(field));

    }

    private Field getField(String fieldName, Type genericType) {
        try {
            return GenericMasterTest.class.getDeclaredField(fieldName);
        } catch (NoSuchFieldException e) {
            Assert.fail("Field not found");
            return null;
        }
    }
}