import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.TypeBindings;
import com.fasterxml.jackson.databind.type.SimpleType;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class SimpleTypeTest {

    @Test
    public void testSimpleTypeConstruction() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);
        JavaType javaType = simpleType;

        assertNotNull(javaType);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(TypeBindings.emptyBindings(), javaType.getTypeBindings());
        assertEquals(null, javaType.getSuperClass());
        assertEquals(new Class[0], javaType.getSuperTypes());
        assertEquals(0, javaType.getModifiers());
        assertEquals(null, javaType.getValueHandler());
        assertEquals(null, javaType.getTypeHandler());
        assertEquals(false, javaType.isStatic());
    }

    @Test
    public void testSimpleTypeConstructionWithTypeBindings() {
        Class<String> cls = String.class;
        TypeBindings bindings = new TypeBindings();
        bindings.putBinding(String.class, LongType.instance);

        SimpleType simpleType = new SimpleType(cls, bindings);
        JavaType javaType = simpleType;

        assertNotNull(javaType);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(bindings, javaType.getTypeBindings());
        assertEquals(null, javaType.getSuperClass());
        assertEquals(new Class[0], javaType.getSuperTypes());
        assertEquals(0, javaType.getModifiers());
        assertEquals(null, javaType.getValueHandler());
        assertEquals(null, javaType.getTypeHandler());
        assertEquals(false, javaType.isStatic());
        assertEquals(LongType.instance, javaType.containedType(0));
    }

    @Test
    public void testSimpleTypeConstructionWithSuperClassAndSuperInterfaces() {
        Class<String> cls = String.class;
        JavaType superClass = new SimpleType(Number.class);
        JavaType[] superInterfaces = new JavaType[]{new SimpleType(Comparable.class), new SimpleType(Serializable.class)};

        SimpleType simpleType = new SimpleType(cls, null, (JavaType)superClass, superInterfaces);
        JavaType javaType = simpleType;

        assertNotNull(javaType);
        assertEquals(cls, javaType.getRawClass());
        assertEquals(null, javaType.getTypeBindings());
        assertEquals(superClass, javaType.getSuperClass());
        assertEquals(superInterfaces, javaType.getSuperTypes());
        assertEquals(0, javaType.getModifiers());
        assertEquals(null, javaType.getValueHandler());
        assertEquals(null, javaType.getTypeHandler());
        assertEquals(false, javaType.isStatic());
    }

    @Test
    public void testSimpleTypeCopyConstructor() {
        Class<String> cls = String.class;
        SimpleType originalType = new SimpleType(cls);

        SimpleType copiedType = new SimpleType(originalType);
        JavaType javaOriginalType = originalType;
        JavaType javaCopiedType = copiedType;

        assertEquals(javaOriginalType, javaCopiedType);
        assertEquals(cls, javaCopiedType.getRawClass());
        assertEquals(originalType.getTypeBindings(), javaCopiedType.getTypeBindings());
        assertEquals(originalType.getSuperClass(), javaCopiedType.getSuperClass());
        assertArrayEquals(originalType.getSuperTypes(), javaCopiedType.getSuperTypes());
        assertEquals(originalType.getModifiers(), javaCopiedType.getModifiers());
        assertEquals(originalType.getValueHandler(), javaCopiedType.getValueHandler());
        assertEquals(originalType.getTypeHandler(), javaCopiedType.getTypeHandler());
        assertEquals(originalType.isStatic(), javaCopiedType.isStatic());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithMapClass() {
        SimpleType.constructUnsafe(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithCollectionClass() {
        SimpleType.constructUnsafe(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructUnsafeWithArrayClass() {
        SimpleType.constructUnsafe(int[].class);
    }

    @Test
    public void testNarrowMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        SimpleType narrowedType = simpleType._narrow(StringBuilder.class);
        JavaType javaSimpleType = simpleType;
        JavaType javaNarrowedType = narrowedType;

        assertNotNull(narrowedType);
        assertNotNull(javaNarrowedType);
        assertEquals(javaSimpleType, javaNarrowedType);
        assertEquals(StringBuilder.class, javaNarrowedType.getRawClass());
        assertEquals(simpleType.getTypeBindings(), javaNarrowedType.getTypeBindings());
        assertEquals(simpleType.getSuperClass(), javaNarrowedType.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), javaNarrowedType.getSuperTypes());
        assertEquals(simpleType.getModifiers(), javaNarrowedType.getModifiers());
        assertEquals(simpleType.getValueHandler(), javaNarrowedType.getValueHandler());
        assertEquals(simpleType.getTypeHandler(), javaNarrowedType.getTypeHandler());
        assertEquals(simpleType.isStatic(), javaNarrowedType.isStatic());
    }

    @Test
    public void testWithContentTypeMethod() {
        // Simple type doesn't have content type
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentType(new SimpleType(Integer.class));
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithTypeHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();

        SimpleType withTypeHandler = simpleType.withTypeHandler(handler);
        JavaType javaSimpleType = simpleType;
        JavaType javaWithTypeHandler = withTypeHandler;

        assertEquals(javaSimpleType, javaWithTypeHandler);
        assertEquals(simpleType.getRawClass(), javaWithTypeHandler.getRawClass());
        assertEquals(simpleType.getTypeBindings(), javaWithTypeHandler.getTypeBindings());
        assertEquals(simpleType.getSuperClass(), javaWithTypeHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), javaWithTypeHandler.getSuperTypes());
        assertEquals(simpleType.getModifiers(), javaWithTypeHandler.getModifiers());
        assertEquals(handler, javaWithTypeHandler.getTypeHandler());
        assertEquals(simpleType.getValueHandler(), javaWithTypeHandler.getValueHandler());
        assertEquals(simpleType.isStatic(), javaWithTypeHandler.isStatic());
    }

    @Test
    public void testWithContentTypeHandlerMethod() {
        // Simple type doesn't have content type handler
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentTypeHandler(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithValueHandlerMethod() {
        SimpleType simpleType = new SimpleType(String.class);
        Object handler = new Object();

        SimpleType withValueHandler = simpleType.withValueHandler(handler);
        JavaType javaSimpleType = simpleType;
        JavaType javaWithValueHandler = withValueHandler;

        assertEquals(javaSimpleType, javaWithValueHandler);
        assertEquals(simpleType.getRawClass(), javaWithValueHandler.getRawClass());
        assertEquals(simpleType.getTypeBindings(), javaWithValueHandler.getTypeBindings());
        assertEquals(simpleType.getSuperClass(), javaWithValueHandler.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), javaWithValueHandler.getSuperTypes());
        assertEquals(simpleType.getModifiers(), javaWithValueHandler.getModifiers());
        assertEquals(simpleType.getTypeHandler(), javaWithValueHandler.getTypeHandler());
        assertEquals(handler, javaWithValueHandler.getValueHandler());
        assertEquals(simpleType.isStatic(), javaWithValueHandler.isStatic());
    }

    @Test
    public void testWithContentValueHandlerMethod() {
        // Simple type doesn't have content value handler
        SimpleType simpleType = new SimpleType(String.class);
        try {
            simpleType.withContentValueHandler(new Object());
            fail();
        } catch (IllegalArgumentException e) {
            // Expected
        }
    }

    @Test
    public void testWithStaticTypingMethod() {
        SimpleType simpleType = new SimpleType(String.class);

        SimpleType withStaticTyping = simpleType.withStaticTyping();
        JavaType javaSimpleType = simpleType;
        JavaType javaWithStaticTyping = withStaticTyping;

        assertEquals(javaSimpleType, javaWithStaticTyping);
        assertEquals(simpleType.getRawClass(), javaWithStaticTyping.getRawClass());
        assertEquals(simpleType.getTypeBindings(), javaWithStaticTyping.getTypeBindings());
        assertEquals(simpleType.getSuperClass(), javaWithStaticTyping.getSuperClass());
        assertArrayEquals(simpleType.getSuperTypes(), javaWithStaticTyping.getSuperTypes());
        assertEquals(simpleType.getModifiers(), javaWithStaticTyping.getModifiers());
        assertEquals(simpleType.getTypeHandler(), javaWithStaticTyping.getTypeHandler());
        assertEquals(simpleType.getValueHandler(), javaWithStaticTyping.getValueHandler());
        assertTrue(javaWithStaticTyping.isStatic());
        assertFalse(simpleType.isStatic());
    }

    @Test
    public void testRefineMethod() {
        // Simple type doesn't have refinement
        SimpleType simpleType = new SimpleType(String.class);

        try {
            simpleType.refine(StringBuilder.class, null, null, null);
            fail();
        } catch (NullPointerException e) {
            // Expected
        }
    }

    @Test
    public void testBuildCanonicalNameMethod() {
        Class<String> cls = String.class;
        SimpleType simpleType = new SimpleType(cls);

        String canonicalName = simpleType.buildCanonicalName();
        assertEquals("java.lang.String", canonicalName);
    }
}