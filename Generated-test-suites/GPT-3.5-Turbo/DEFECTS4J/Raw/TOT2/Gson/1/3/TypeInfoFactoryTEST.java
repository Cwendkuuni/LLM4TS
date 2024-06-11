import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        TypeInfoArray arrayType = TypeInfoFactory.getTypeInfoForArray(String[].class);
        assertNotNull(arrayType);
        assertEquals(String[].class, arrayType.getType());
    }

    @Test
    public void testGetTypeInfoForField() {
        TypeInfo fieldType = TypeInfoFactory.getTypeInfoForField(TestClass.class.getDeclaredField("field"), TestClass.class);
        assertNotNull(fieldType);
        assertEquals(String.class, fieldType.getType());
    }

    @Test
    public void testGetActualType_Class() {
        Class<?> actualType = (Class<?>) TypeInfoFactory.getActualType(String.class, null, null);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_ParameterizedType() {
        ParameterizedType parameterizedType = new TestParameterizedType(List.class, new Type[] {String.class});
        ParameterizedType actualType = (ParameterizedType) TypeInfoFactory.getActualType(parameterizedType, null, null);
        assertNotNull(actualType);
        assertEquals(List.class, actualType.getRawType());
        assertArrayEquals(new Type[]{String.class}, actualType.getActualTypeArguments());
    }

    @Test
    public void testGetActualType_GenericArrayType() {
        GenericArrayType genericArrayType = new TestGenericArrayType(String.class);
        GenericArrayType actualType = (GenericArrayType) TypeInfoFactory.getActualType(genericArrayType, null, null);
        assertNotNull(actualType);
        assertEquals(String[].class, actualType.getGenericComponentType());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testGetActualType_TypeVariable_UnsupportedOperation() {
        TypeInfoFactory.getActualType(Integer.class, Integer.class, Integer.class);
    }

    @Test
    public void testGetActualType_TypeVariable_ParameterizedType() {
        TypeVariable<?> typeVariable = TestClass.class.getTypeParameters()[0];
        ParameterizedType parameterizedType = new TestParameterizedType(TestClass.class, new Type[] {String.class});
        Type actualType = TypeInfoFactory.getActualType(typeVariable, parameterizedType, TestClass.class);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test
    public void testGetActualType_WildcardType() {
        WildcardType wildcardType = new TestWildcardType(new Type[] {String.class});
        Type actualType = TypeInfoFactory.getActualType(wildcardType, null, null);
        assertNotNull(actualType);
        assertEquals(String.class, actualType);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetActualType_InvalidType() {
        TypeInfoFactory.getActualType(new InvalidType(), null, null);
    }

    @Test
    public void testExtractRealTypes() {
        Type[] actualTypeArguments = {String.class, Integer.class};
        Type[] realTypes = TypeInfoFactory.extractRealTypes(actualTypeArguments, null, null);
        assertNotNull(realTypes);
        assertArrayEquals(actualTypeArguments, realTypes);
    }

    @Test
    public void testGetIndex() {
        TypeVariable<?>[] typeVariables = TestClass.class.getTypeParameters();
        int index = TypeInfoFactory.getIndex(typeVariables, typeVariables[0]);
        assertEquals(0, index);
    }

    class TestClass {
        public String field;
    }

    class TestParameterizedType implements ParameterizedType {
        private Class<?> rawType;
        private Type[] actualTypeArguments;

        public TestParameterizedType(Class<?> rawType, Type[] actualTypeArguments) {
            this.rawType = rawType;
            this.actualTypeArguments = actualTypeArguments;
        }

        @Override
        public Type[] getActualTypeArguments() {
            return actualTypeArguments;
        }

        @Override
        public Type getRawType() {
            return rawType;
        }

        @Override
        public Type getOwnerType() {
            return null;
        }
    }

    class TestGenericArrayType implements GenericArrayType {
        private Type genericComponentType;

        public TestGenericArrayType(Type genericComponentType) {
            this.genericComponentType = genericComponentType;
        }

        @Override
        public Type getGenericComponentType() {
            return genericComponentType;
        }
    }

    class TestWildcardType implements WildcardType {
        private Type[] upperBounds;

        public TestWildcardType(Type[] upperBounds) {
            this.upperBounds = upperBounds;
        }

        @Override
        public Type[] getUpperBounds() {
            return upperBounds;
        }

        @Override
        public Type[] getLowerBounds() {
            return new Type[0];
        }
    }

    class InvalidType implements Type {
    }

}