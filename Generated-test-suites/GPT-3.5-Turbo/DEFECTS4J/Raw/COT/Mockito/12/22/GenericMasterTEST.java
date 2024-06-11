
import org.junit.Test;
import org.mockito.internal.util.reflection.GenericMaster;

import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

public class GenericMasterTest {

    @Test
    public void testGetGenericType() {
        // Test when the field is generic
        Field genericField = mockField(ParameterizedType.class);
        Class expectedGenericType = Object.class; // default value
        GenericMaster genericMaster = new GenericMaster();
        Class actualGenericType = genericMaster.getGenericType(genericField);
        assertEquals(expectedGenericType, actualGenericType);

        // Test when the field is not generic
        Field nonGenericField = mockField(Object.class);
        expectedGenericType = Object.class;
        actualGenericType = genericMaster.getGenericType(nonGenericField);
        assertEquals(expectedGenericType, actualGenericType);

        // Test nested generics
        Field nestedGenericField = mockField(mock(ParameterizedType.class));
        expectedGenericType = Object.class; // default value
        actualGenericType = genericMaster.getGenericType(nestedGenericField);
        assertEquals(expectedGenericType, actualGenericType);
    }

    @Test
    public void testGetGenericType_WhenGenericTypeIsNull() {
        Field field = mock(Field.class);
        GenericMaster genericMaster = new GenericMaster();
        Class expectedGenericType = Object.class; // default value
        Class actualGenericType = genericMaster.getGenericType(field);
        assertEquals(expectedGenericType, actualGenericType);
    }

    private Field mockField(Type genericType) {
        Field field = mock(Field.class);
        field.setGenericType(genericType);
        return field;
    }
}
