import org.junit.Test;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void constructSimpleType() {
        // Arrange
        Class<?> cls = String.class;

        // Act
        SimpleType simpleType = new SimpleType(cls);

        // Assert
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test
    public void constructSimpleTypeWithBindings() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts);

        // Assert
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
    }

    @Test
    public void constructSimpleTypeUsingCopyConstructor() {
        // Arrange
        SimpleType base = new SimpleType(String.class);

        // Act
        SimpleType simpleType = new SimpleType(base);

        // Assert
        assertEquals(base.getRawClass(), simpleType.getRawClass());
    }

    @Test
    public void constructSimpleTypeWithHandlersAndFlags() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, valueHandler, typeHandler,
                asStatic);

        // Assert
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    @Test
    public void constructSimpleTypeWithExtraHash() {
        // Arrange
        Class<?> cls = String.class;
        TypeBindings bindings = TypeBindings.emptyBindings();
        JavaType superClass = null;
        JavaType[] superInts = null;
        int extraHash = 0;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;

        // Act
        SimpleType simpleType = new SimpleType(cls, bindings, superClass, superInts, extraHash, valueHandler,
                typeHandler, asStatic);

        // Assert
        assertEquals(cls, simpleType.getRawClass());
        assertEquals(bindings, simpleType.getBindings());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInts, simpleType.getInterfaces());
        assertEquals(extraHash, simpleType.getExtraHash());
        assertEquals(valueHandler, simpleType.getValueHandler());
        assertEquals(typeHandler, simpleType.getTypeHandler());
        assertEquals(asStatic, simpleType.isStatic());
    }

    @Test
    public void constructUnsafeSimpleType() {
        // Arrange
        Class<?> cls = String.class;

        // Act
        SimpleType simpleType = SimpleType.constructUnsafe(cls);

        // Assert
        assertEquals(cls, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSimpleTypeForMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSimpleTypeForCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void constructSimpleTypeForArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void narrowSimpleType() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        JavaType narrowedType = simpleType._narrow(Object.class);

        // Assert
        assertEquals(Object.class, narrowedType.getRawClass());
    }

    @Test
    public void isContainerTypeReturnsFalse() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        boolean isContainerType = simpleType.isContainerType();

        // Assert
        assertFalse(isContainerType);
    }

    @Test
    public void getErasedSignatureReturnsClassSignature() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());

        // Assert
        assertEquals("Ljava/lang/String;", signature.toString());
    }

    @Test
    public void getGenericSignatureReturnsClassAndBindingsSignature() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);

        // Act
        StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());

        // Assert
        assertEquals("Ljava/lang/String;", signature.toString());
    }

    @Test
    public void toStringReturnsCanonicalName() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        String stringRepresentation = simpleType.toString();

        // Assert
        assertEquals("[simple type, class java.lang.String]", stringRepresentation);
    }

    @Test
    public void equalsReturnsTrueForSameInstance() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        boolean isEquals = simpleType.equals(simpleType);

        // Assert
        assertTrue(isEquals);
    }

    @Test
    public void equalsReturnsFalseForDifferentClass() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);
        SimpleType differentSimpleType = new SimpleType(Integer.class);

        // Act
        boolean isEquals = simpleType.equals(differentSimpleType);

        // Assert
        assertFalse(isEquals);
    }

    @Test
    public void equalsReturnsFalseForNull() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);

        // Act
        boolean isEquals = simpleType.equals(null);

        // Assert
        assertFalse(isEquals);
    }

    @Test
    public void equalsReturnsFalseForDifferentType() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class);
        Object differentObject = new Object();

        // Act
        boolean isEquals = simpleType.equals(differentObject);

        // Assert
        assertFalse(isEquals);
    }

    @Test
    public void equalsReturnsFalseForDifferentBindings() {
        // Arrange
        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), null, null);
        SimpleType differentSimpleType = new SimpleType(String.class, TypeBindings.create(String.class), null, null);

        // Act
        boolean isEquals = simpleType.equals(differentSimpleType);

        // Assert
        assertFalse(isEquals);
    }
}