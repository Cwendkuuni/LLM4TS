import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GenericMasterTest {
  
    private GenericMaster genericMaster;
    private Field field;

    @Before
    public void setup() {
        genericMaster = new GenericMaster();
        field = mock(Field.class);
    }

    @Test
    public void testGetGenericType_WithGenericField_ReturnsCorrectType() {
        ParameterizedType genericType = mock(ParameterizedType.class);
        Type actualType = mock(Type.class);
        Class expectedType = String.class;

        // Setup
        mockType(field, genericType);
        mockGenericType(genericType, actualType);
        mockActualType(actualType, expectedType);

        // Test
        Class result = genericMaster.getGenericType(field);

        // Verify
        assertEquals(expectedType, result);
    }

    @Test
    public void testGetGenericType_WithNonGenericField_ReturnsObjectClass() {
        // Setup
        mockType(field, null);

        // Test
        Class result = genericMaster.getGenericType(field);

        // Verify
        assertEquals(Object.class, result);
    }

    private void mockType(Field field, Type type) {
        when(field.getGenericType()).thenReturn(type);
    }

    private void mockGenericType(ParameterizedType genericType, Type actualType) {
        when(genericType.getActualTypeArguments()).thenReturn(new Type[] {actualType});
    }

    private void mockActualType(Type actualType, Class expectedType) {
        when(actualType instanceof Class).thenReturn(true);
        when((Class) actualType).thenReturn(expectedType);
    }
}