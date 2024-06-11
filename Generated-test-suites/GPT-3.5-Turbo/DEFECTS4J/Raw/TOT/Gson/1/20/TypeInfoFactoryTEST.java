import com.google.gson.TypeInfo;
import com.google.gson.TypeInfoArray;
import org.junit.Before;
import org.junit.Test;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    private TypeInfoFactory typeInfoFactory;
    private Field field;
    private Type type;

    @Before
    public void setUp() throws Exception {
        typeInfoFactory = new TypeInfoFactory();
        field = TestClass.class.getDeclaredField("testField");
        type = TestClass.class;
    }

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray typeInfoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(typeInfoArray);
        assertEquals(String[].class, typeInfoArray.getType());
    }

    @Test
    public void testGetTypeInformationForField() {
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type);
        assertNotNull(typeInfo);
        assertEquals(ParameterizedType.class, typeInfo.getType().getClass());
        ParameterizedType parameterizedType = (ParameterizedType) typeInfo.getType();
        assertEquals(TestField.class, parameterizedType.getRawType());
        assertEquals(String.class, parameterizedType.getActualTypeArguments()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInformationForField_InvalidType() {
        TypeInfoFactory.getTypeInfoForField(field, TestClass2.class);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInformationForField_InvalidField() throws NoSuchFieldException {
        TypeInfoFactory.getTypeInfoForField(TestClass2.class.getDeclaredField("testField"), type);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInformationForField_TypeVariableWithoutParameterizedType() {
        TypeInfoFactory.getTypeInfoForField(TestClass3.class.getDeclaredField("testField"), type);
    }

    @Test
    public void testGetTypeInformationForField_TypeVariableWithParameterizedType() {
        Type type = TestClass4.class;
        Field field = TestClass4.class.getDeclaredFields()[0];
        TypeInfo typeInfo = TypeInfoFactory.getTypeInfoForField(field, type);
        assertNotNull(typeInfo);
        assertEquals(ParameterizedType.class, typeInfo.getType().getClass());
        ParameterizedType parameterizedType = (ParameterizedType) typeInfo.getType();
        assertEquals(TestField.class, parameterizedType.getRawType());
        assertEquals(Integer.class, parameterizedType.getActualTypeArguments()[0]);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInformationForField_InvalidFieldType() {
        TypeInfoFactory.getTypeInfoForField(TestClass5.class.getDeclaredFields()[0], type);
    }

    private static class TestClass {
        private TestField<String> testField;
    }

    private static class TestClass2 {
        private int testField;
    }

    private static class TestClass3 {
        private TestField<?> testField;
    }

    private static class TestClass4 {
        private TestField<Integer> testField;
    }

    private static class TestClass5 {
        private TestField testField;
    }

    private static class TestField<T> {
        private T value;
    }
}