import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray infoArray = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(infoArray);
        assertEquals("java.lang.String[]", infoArray.toString());
    }

    @Test
    public void testGetTypeInfoForField() {
        TypeInfo fieldInfo = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("exampleField"), TestClass.class);
        assertNotNull(fieldInfo);
        assertEquals("java.lang.String", fieldInfo.toString());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetTypeInfoForArrayWithNonArray() {
        TypeInfoFactory.getTypeInfoForArray(String.class);
    }

    @Test(expected = NoSuchFieldException.class)
    public void testGetTypeInfoForInvalidField() throws NoSuchFieldException {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("invalidField"), TestClass.class);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testTypeVariableWithoutParameterizedType() {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("exampleField"), String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidType() {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("exampleField"), TestClass.class);
    }

    @Test(expected = IllegalStateException.class)
    public void testIndexNotFound() {
        TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("exampleField"), TestClass.class);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] types = {String.class, Integer.class};
        Type[] realTypes = TypeInfoFactory.extractRealTypes(types, TestClass.class, TestClass.class);
        assertNotNull(realTypes);
        assertEquals(2, realTypes.length);
        assertEquals("java.lang.String", realTypes[0].toString());
        assertEquals("java.lang.Integer", realTypes[1].toString());
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] types = TestClass.class.getTypeParameters();
        TypeVariable<?> type = TestClass.class.getTypeParameters()[0];
        int index = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, index);
    }

    private static class TestClass {
        private String exampleField;
    }
}