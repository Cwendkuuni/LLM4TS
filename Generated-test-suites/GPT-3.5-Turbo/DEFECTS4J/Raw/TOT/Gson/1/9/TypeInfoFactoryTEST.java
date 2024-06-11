import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    // Test for getTypeInfoForArray method
    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray arrayTypeInfo = TypeInfoFactory.getTypeInfoForArray(new String[]{});
        assertNotNull(arrayTypeInfo);
        assertTrue(arrayTypeInfo.isArray());
    }

    // Test for getTypeInfoForField method
    @Test
    public void testGetTypeInfoForField() {
        class TestClass {
            private String field;
        }

        TestClass testClass = new TestClass();
        testClass.field = "test";

        TypeInfo fieldTypeInfo = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("field"), TestClass.class);
        assertNotNull(fieldTypeInfo);
        assertEquals(String.class, fieldTypeInfo.getType());
    }

    // Test for getActualType method with Class type
    @Test
    public void testGetActualType_Class() {
        Type classType = String.class;
        Type actualType = TypeInfoFactory.getActualType(classType, null, null);
        assertNotNull(actualType);
        assertEquals(classType, actualType);
    }

    // Test for getActualType method with ParameterizedType
    @Test
    public void testGetActualType_ParameterizedType() {
        class TestClass<T> {
            T field;
        }

        ParameterizedType parameterizedType = (ParameterizedType) TestClass.class.getDeclaredFields()[0].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(parameterizedType, null, null);
        assertNotNull(actualType);
        assertNotEquals(parameterizedType, actualType);
    }

    // Test for getActualType method with GenericArrayType
    @Test
    public void testGetActualType_GenericArrayType() {
        class TestClass<T> {
            T[] field;
        }

        GenericArrayType genericArrayType = (GenericArrayType) TestClass.class.getDeclaredFields()[0].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(genericArrayType, null, null);
        assertNotNull(actualType);
        assertNotEquals(genericArrayType, actualType);
    }

    // Test for getActualType method with TypeVariable
    @Test
    public void testGetActualType_TypeVariable() {
        class TestClass<T> {
            T field;
        }

        TypeVariable<?> typeVariable = (TypeVariable<?>) TestClass.class.getDeclaredFields()[0].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(typeVariable, TestClass.class, TestClass.class);
        assertNotNull(actualType);

        // Additional test for type variable with wildcard
        class AnotherTestClass<X extends Number> {
            X field;
        }

        TypeVariable<?> anotherTypeVariable = (TypeVariable<?>) AnotherTestClass.class.getDeclaredFields()[0].getGenericType();
        actualType = TypeInfoFactory.getActualType(anotherTypeVariable, AnotherTestClass.class, AnotherTestClass.class);
        assertNotNull(actualType);
    }

    // Test for getActualType method with WildcardType
    @Test
    public void testGetActualType_WildcardType() {
        class TestClass<T> {
            T field;
        }

        WildcardType wildcardType = (WildcardType) TestClass.class.getDeclaredFields()[0].getGenericType();
        Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
        assertNotNull(actualType);
        assertNotEquals(wildcardType, actualType);
    }

    // Test for getActualType method with invalid types
    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidTypes() {
        TypeInfoFactory.getActualType(new Object(), null, null);
    }

    // Test for getIndex method
    @Test
    public void testGetIndex() {
        class TestClass<T> {
            T field;
        }

        TypeVariable<?> typeVariable = (TypeVariable<?>) TestClass.class.getDeclaredFields()[0].getGenericType();

        TypeVariable<?>[] typeVariables = TestClass.class.getTypeParameters();
        int index = TypeInfoFactory.getIndex(typeVariables, typeVariable);
        assertEquals(0, index);
    }

    // Test for getIndex method with invalid type variable
    @Test(expected = IllegalStateException.class)
    public void testGetIndex_InvalidTypeVariable() {
        TypeInfoFactory.getIndex(new TypeVariable<?>[]{}, null);
    }
}