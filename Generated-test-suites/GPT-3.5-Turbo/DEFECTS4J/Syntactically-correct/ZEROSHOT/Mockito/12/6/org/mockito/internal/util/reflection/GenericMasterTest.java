package org.mockito.internal.util.reflection;

import org.junit.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

public class GenericMasterTest {

    private GenericMaster genericMaster;
    private Field field;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
        field = null; // initialize field here
    }

    @Test
    public void testGetGenericType() {
        // Test case 1: Generic field
        Type genericType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            @Override
            public Type getRawType() {
                return null;
            }

            @Override
            public Type getOwnerType() {
                return null;
            }
        };

        field = createField(genericType);
        Class result = genericMaster.getGenericType(field);
        assertEquals(String.class, result);

        // Test case 2: Non-generic field
        field = createField(null);
        result = genericMaster.getGenericType(field);
        assertEquals(Object.class, result);
    }

    private Field createField(Type genericType) {
        Field field = null; // create field here
        field.setGenericType(genericType);
        return field;
    }
}