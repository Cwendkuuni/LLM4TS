import org.junit.Test;
import static org.junit.Assert.*;

public class TypeInfoFactoryTest {

    @Test
    public void testGetTypeInfoForArray() {
        // Typical use case
        Type type = new Type() {};
        TypeInfoArray result = TypeInfoFactory.getTypeInfoForArray(type);
        assertNotNull(result);

        // Edge case - null input
        try {
            TypeInfoFactory.getTypeInfoForArray(null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testGetTypeInfoForField() {
        // Typical use case
        Field f = new Field(null);
        Type typeDefiningF = new Type() {};
        TypeInfo result = TypeInfoFactory.getTypeInfoForField(f, typeDefiningF);
        assertNotNull(result);

        // Edge case - null field
        try {
            TypeInfoFactory.getTypeInfoForField(null, typeDefiningF);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Success
        }
    }

    @Test
    public void testGetActualType() {
        // Typical use case
        Type typeToEvaluate = new Type() {};
        Type parentType = new Type() {};
        Class<?> rawParentClass = Class.class;
        Type result = TypeInfoFactory.getActualType(typeToEvaluate, parentType, rawParentClass);
        assertNotNull(result);

        // Edge case - wrong type
        try {
            TypeInfoFactory.getActualType(null, null, null);
            fail("Expected IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Success
        }
    }

    @Test
    public void testExtractRealTypes() {
        // Typical use case
        Type[] actualTypeArguments = {new Type() {}};
        Type parentType = new Type() {};
        Class<?> rawParentClass = Class.class;
        Type[] result = TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass);
        assertNotNull(result);

        // Edge case - empty array
        actualTypeArguments = new Type[0];
        assertNull(TypeInfoFactory.extractRealTypes(actualTypeArguments, parentType, rawParentClass));
    }

    @Test
    public void testGetIndex() {
        // Typical use case
        TypeVariable<?>[] types = {new TypeVariable() {}};
        TypeVariable<?> type = new TypeVariable() {};
        int result = TypeInfoFactory.getIndex(types, type);
        assertEquals(0, result);

        // Edge case - type not present in array
        try {
            TypeInfoFactory.getIndex(types, new TypeVariable() {});
            fail("Expected IllegalStateException");
        } catch (IllegalStateException e) {
            // Success
        }
    }
}