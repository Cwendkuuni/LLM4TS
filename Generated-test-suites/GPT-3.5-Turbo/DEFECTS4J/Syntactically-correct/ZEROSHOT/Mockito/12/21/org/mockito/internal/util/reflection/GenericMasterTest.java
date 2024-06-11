import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setUp() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType_NonGenericField_ReturnsObjectClass() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("nonGenericField");
        Class<?> expectedType = Object.class;
        
        Class<?> result = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetGenericType_GenericField_ReturnsActualGenericType() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("genericField");
        Class<?> expectedType = String.class;
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        when(field.getGenericType()).thenReturn(parameterizedType);
        when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[]{String.class});
        
        Class<?> result = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetGenericType_NestedGenericField_ReturnsActualGenericType() throws NoSuchFieldException {
        Field field = GenericMaster.class.getDeclaredField("nestedGenericField");
        Class<?> expectedType = Integer.class;
        ParameterizedType outerParameterizedType = mock(ParameterizedType.class);
        ParameterizedType innerParameterizedType = mock(ParameterizedType.class);
        when(field.getGenericType()).thenReturn(outerParameterizedType);
        when(outerParameterizedType.getActualTypeArguments()).thenReturn(new Type[]{innerParameterizedType});
        when(innerParameterizedType.getActualTypeArguments()).thenReturn(new Type[]{Integer.class});
        
        Class<?> result = genericMaster.getGenericType(field);
        
        assertEquals(expectedType, result);
    }

}