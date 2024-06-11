import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {
    @Test
    public void testGetTypeInfoForArray() {
        Type type = new Type() {};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);

        assertNotNull(result);
        assertEquals(type, result.getType());
    }

    @Test
    public void testGetTypeInfoForField() throws NoSuchFieldException {
        Class<?> classDefiningF = TypeInfoFactory.class;
        Field f = classDefiningF.getDeclaredField("testField");

        TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, f.getType());

        assertNotNull(result);
        assertEquals(f.getType(), result.getType());
    }

    @Test
    public void testGetTypeInfoForField_TypeVariable() throws NoSuchFieldException {
        class TestClass<A> {
            A testField;
        }

        Class<?> classDefiningF = TestClass.class;
        Field f = classDefiningF.getDeclaredField("testField");

        TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, f.getType());

        assertNotNull(result);
        assertEquals(f.getType(), result.getType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetTypeInfoForField_TypeVariable_WithoutParameterizedType() throws NoSuchFieldException {
        class TestClass<A> {
            A testField;
        }

        Class<?> classDefiningF = TestClass.class;
        Field f = classDefiningF.getDeclaredField("testField");

        TypeInfoFactory.getTypeInfoForField(f, f.getGenericType());
    }
}