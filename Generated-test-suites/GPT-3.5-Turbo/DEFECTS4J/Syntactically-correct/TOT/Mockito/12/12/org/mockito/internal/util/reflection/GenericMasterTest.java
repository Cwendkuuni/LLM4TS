import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

public class GenericMasterTest {

    @Test
    public void testGetGenericType_GenericField() {
        Field field = mock(Field.class);
        
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> expectedClass = String.class;
        
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { expectedClass });
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testGetGenericType_NonGenericField() {
        Field field = mock(Field.class);
        
        Mockito.when(field.getGenericType()).thenReturn(null);
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(Object.class, actualClass);
    }
    
    @Test
    public void testGetGenericType_EmptyTypeArguments() {
        Field field = mock(Field.class);
        
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> expectedClass = Object.class;
        
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] {});
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testGetGenericType_NullActualTypeArgument() {
        Field field = mock(Field.class);
        
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> expectedClass = Object.class;
        
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { null });
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testGetGenericType_MultipleActualTypeArguments() {
        Field field = mock(Field.class);
        
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> expectedClass = String.class;
        
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { expectedClass, Integer.class });
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(expectedClass, actualClass);
    }
    
    @Test
    public void testGetGenericType_NestedGenericTypes() {
        Field field = mock(Field.class);
        
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Class<?> expectedClass = Integer.class;
        
        ParameterizedType nestedParameterizedType = mock(ParameterizedType.class);
        Class<?> nestedExpectedClass = String.class;
        
        Mockito.when(field.getGenericType()).thenReturn(parameterizedType);
        Mockito.when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { nestedParameterizedType });
        Mockito.when(nestedParameterizedType.getActualTypeArguments()).thenReturn(new Type[] { expectedClass });
        
        GenericMaster genericMaster = new GenericMaster();
        Class<?> actualClass = genericMaster.getGenericType(field);
        
        assertEquals(nestedExpectedClass, actualClass);
    }
}