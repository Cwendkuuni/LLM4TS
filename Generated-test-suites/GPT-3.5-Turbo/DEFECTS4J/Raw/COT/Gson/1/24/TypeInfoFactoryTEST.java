
import com.google.gson.*;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void getTypeInfoForArray_shouldReturnTypeInfoArray() {
        Type type = new Type() {};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);
        assertEquals(type, result.getType());
    }

    @Test
    public void getTypeInfoForField_shouldReturnTypeInfo() throws NoSuchFieldException {
        Field field = TypeInfoFactoryTest.class.getDeclaredField("type");
        Type type = new Type() {};
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(field, type);
        assertNotNull(result);

        // Additional assertion for TypeInfo
    }

    @Test(expected = IllegalArgumentException.class)
    public void getTypeInfoForArray_shouldThrowIllegalArgumentException() {
        Type type = new Type() {};
        TypeInfoFactory.getTypeInfoForArray(type);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void getTypeInfoForField_shouldThrowUnsupportedOperationException() throws NoSuchFieldException {
        Field field = TypeInfoFactoryTest.class.getDeclaredField("type");
        Type type = new Type() {};
        TypeInfoFactory.getTypeInfoForField(field, type);
    }
}
