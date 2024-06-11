package org.mockito.internal.util.reflection;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;

public class GenericMasterTest {
    @Test
    public void testGetGenericType() {
        GenericMaster genericMaster = new GenericMaster();
        
        // Test with generic type
        Field genericField = null; // Define generic field
        Type genericType = null; // Define generic type
        ParameterizedType parameterizedType = null; // Define parameterized type
        Class expectedGenericType = null; // Define expected generic type
        
        // Set up mocks and expectations
        genericField = new Field(); // Initialize generic field
        genericType = new ParameterizedType(); // Initialize generic type
        
        parameterizedType = new ParameterizedType();
        parameterizedType.setActualTypeArguments(new Type[] { Integer.class });
        
        genericType.setActualTypeArguments(new Type[] { parameterizedType });
        genericField.setGenericType(genericType);
        
        expectedGenericType = Integer.class;
        
        // Perform the test
        Class actualGenericType = genericMaster.getGenericType(genericField);
        
        // Verify the result
        assertEquals(expectedGenericType, actualGenericType);
        
        // Test without generic type
        Field notGenericField = null; // Define non-generic field
        Class expectedNotGenericType = Object.class; // Define expected non-generic type
        
        // Set up mocks and expectations
        notGenericField = new Field(); // Initialize non-generic field
        
        // Perform the test
        Class actualNotGenericType = genericMaster.getGenericType(notGenericField);
        
        // Verify the result
        assertEquals(expectedNotGenericType, actualNotGenericType);
    }
}