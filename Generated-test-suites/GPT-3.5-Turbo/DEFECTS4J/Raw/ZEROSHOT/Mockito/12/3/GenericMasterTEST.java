
package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

@SuppressWarnings("unchecked")
public class GenericMasterTest {

    private GenericMaster genericMaster;
    
    @Mock
    private Field field;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        genericMaster = new GenericMaster();
    }
    
    @Test
    public void testGetGenericType_FieldIsNull_ReturnsObjectClass() {
        Class result = genericMaster.getGenericType(null);
        
        assertEquals(Object.class, result);
    }
    
    @Test
    public void testGetGenericType_FieldIsGeneric_ReturnsActualGenericType() {
        ParameterizedType parameterizedType = new ParameterizedType() {
            @Override
            public Type[] getActualTypeArguments() {
                return new Type[] { String.class };
            }
            
            @Override
            public Type getRawType() {
                return getClass();
            }
            
            @Override
            public Type getOwnerType() {
                return null;
            }
        };
        Type genericType = parameterizedType;
        field = new Field() {
            @Override
            public String getName() {
                return "dummyField";
            }

            @Override
            public Class<?> getType() {
                return null;
            }

            @Override
            public Type getGenericType() {
                return genericType;
            }

            // Other overridden methods...
        };
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(String.class, result);
    }
    
    @Test
    public void testGetGenericType_FieldIsNotGeneric_ReturnsObjectClass() {
        field = new Field() {
            @Override
            public String getName() {
                return "dummyField";
            }

            @Override
            public Class<?> getType() {
                return null;
            }

            @Override
            public Type getGenericType() {
                return null;
            }

            // Other overridden methods...
        };
        
        Class result = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, result);
    }
    
}
