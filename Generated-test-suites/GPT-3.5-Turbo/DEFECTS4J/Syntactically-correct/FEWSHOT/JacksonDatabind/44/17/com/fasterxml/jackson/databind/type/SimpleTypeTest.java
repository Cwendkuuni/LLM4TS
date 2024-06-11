
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class SimpleTypeTest {
    @Test
    public void testConstructorWithClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstructorWithClassAndTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        SimpleType simpleType = new SimpleType(String.class, bindings, null, null);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstructorWithClassTypeBindingsSuperClassAndSuperInterfaces() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstructorWithTypeBase() {
        TypeBase base = new TypeBase(String.class) {};
        SimpleType simpleType = new SimpleType(base);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstructorWithFullParameters() {
        TypeBindings bindings = new TypeBindings();
        JavaType superClass = null;
        JavaType[] superInterfaces = null;
        Object valueHandler = null;
        Object typeHandler = null;
        boolean asStatic = false;
        SimpleType simpleType = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals(String.class, simpleType.getRawClass());
        assertNull(simpleType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMapClass() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollectionClass() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArrayClass() {
        SimpleType.construct(Array.class);
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Number.class);
        JavaType narrowedType = simpleType._narrow(Integer.class);
        assertEquals(Integer.class, narrowedType.getRawClass());
        assertNull(narrowedType.getContentType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testNarrowWithNonSubtypeClass() {
        SimpleType simpleType = new SimpleType(Number.class);
        simpleType._narrow(String.class);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType withContentType = simpleType.withContentType(String.class);
        assertEquals(List.class, withContentType.getRawClass());
        assertEquals(String.class, withContentType.getContentType().getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentType(Integer.class);
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(List.class);
        JavaType withContentTypeHandler = simpleType.withContentTypeHandler("handler");
        assertEquals(List.class, withContentTypeHandler.getRawClass());
        assertNull(withContentTypeHandler.getContentTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandlerThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentTypeHandler("handler");
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType withValueHandler = simpleType.withValueHandler("handler");
        assertEquals(String.class, withValueHandler.getRawClass());
        assertEquals("handler", withValueHandler.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandlerThrowsException() {
        SimpleType simpleType = new SimpleType(List.class);
        simpleType.withValueHandler("handler");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandlerThrowsException() {
        SimpleType simpleType = new SimpleType(String.class);
        simpleType.withContentValueHandler("handler");
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType withStaticTyping = simpleType.withStaticTyping();
        assertEquals(String.class, withStaticTyping.getRawClass());
        assertNull(withStaticTyping.getContentType());
        assertTrue(withStaticTyping.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(String.class);
        JavaType refinedType = simpleType.refine(String.class, new TypeBindings(), null, null);
        assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(String.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = simpleType.getErasedSignature(sb);
        assertEquals("Ljava/lang/String;", erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = simpleType.getGenericSignature(sb);
        assertEquals("Ljava/lang/String;", genericSignature.toString());
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(String.class);
        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(String.class);
        String toString = simpleType.toString();
        assertEquals("[simple type, class java.lang.String]", toString);
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(String.class);
        SimpleType simpleType2 = new SimpleType(String.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}

