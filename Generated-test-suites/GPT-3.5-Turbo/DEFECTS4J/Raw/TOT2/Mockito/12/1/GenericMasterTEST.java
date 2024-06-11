
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.lang.reflect.Field;

import org.junit.Test;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        Field field = mock(Field.class);
        Type type = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }

            @Override
            public Type getOwnerType() {
                return null;
            }

            @Override
            public Type getRawType() {
                return null;
            }
        };
        when(field.getGenericType()).thenReturn(type);

        GenericMaster genericMaster = new GenericMaster();
        Class actualType = genericMaster.getGenericType(field);

        assertEquals(String.class, actualType);
    }
}
