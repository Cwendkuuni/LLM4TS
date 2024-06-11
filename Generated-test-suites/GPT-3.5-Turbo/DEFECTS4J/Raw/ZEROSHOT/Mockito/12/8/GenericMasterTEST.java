import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import org.junit.Before;
import org.junit.Test;

public class GenericMasterTest {

    private GenericMaster genericMaster;

    @Before
    public void setup() {
        genericMaster = new GenericMaster();
    }

    @Test
    public void testGetGenericType() {
        Field field = mock(Field.class);
        ParameterizedType parameterizedType = mock(ParameterizedType.class);
        Type typeArgument = mock(Type.class);
        
        when(field.getGenericType()).thenReturn(parameterizedType);
        when(parameterizedType.getActualTypeArguments()).thenReturn(new Type[] { typeArgument });
        when(typeArgument instanceof Class).thenReturn(true);
        when((Class) typeArgument).thenReturn(Integer.class);

        Class result = genericMaster.getGenericType(field);

        assertEquals(Integer.class, result);
    }

    @Test
    public void testGetGenericType_nonGenericField() {
        Field field = mock(Field.class);

        when(field.getGenericType()).thenReturn(null);

        Class result = genericMaster.getGenericType(field);

        assertEquals(Object.class, result);
    }

    @Test
    public void testGetGenericType_nestedGenericField() {
        Field field = mock(Field.class);
        ParameterizedType parameterizedType1 = mock(ParameterizedType.class);
        ParameterizedType parameterizedType2 = mock(ParameterizedType.class);
        Type typeArgument = mock(Type.class);

        when(field.getGenericType()).thenReturn(parameterizedType1);
        when(parameterizedType1.getActualTypeArguments()).thenReturn(new Type[] { parameterizedType2 });
        when(parameterizedType2.getActualTypeArguments()).thenReturn(new Type[] { typeArgument });
        when(typeArgument instanceof Class).thenReturn(true);
        when((Class) typeArgument).thenReturn(Double.class);

        Class result = genericMaster.getGenericType(field);

        assertEquals(Double.class, result);
    }
}