package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() throws NoSuchFieldException {
        Field field = mock(Field.class);
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> genericType = mock(Class.class);

        when(field.getGenericType()).thenReturn(parameterizedType);
        when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{genericType});
        when(genericType.getName()).thenReturn("java.lang.String");

        Class<?> result = genericMaster.getGenericType(field);

        assertNotNull(result);
        assertEquals(String.class, result);
    }

    @Test
    public void testGetGenericType_NoGeneric() throws NoSuchFieldException {
        Field field = mock(Field.class);

        when(field.getGenericType()).thenReturn(null);

        Class<?> result = genericMaster.getGenericType(field);

        assertNotNull(result);
        assertEquals(Object.class, result);
    }
}