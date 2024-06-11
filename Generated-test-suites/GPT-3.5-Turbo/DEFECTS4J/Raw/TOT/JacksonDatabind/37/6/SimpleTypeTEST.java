
import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

import static org.junit.Assert.*;

public class SimpleTypeTest {

    @Test
    public void testConstructor_withClass() {
        SimpleType simpleType = new SimpleType(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructor_withClassAndBindingsAndSuperClassAndSuperInterfaces() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{
                new SimpleType(Cloneable.class),
                new SimpleType(Comparable.class)
        };

        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, superInterfaces);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructor_withBase() {
        TypeBase base = new TypeBase(Object.class);

        SimpleType simpleType = new SimpleType(base);
        assertEquals("java.lang.Object", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructor_withClassAndBindingAndSuperClassAndSuperInterfacesAndValueHandlerAndTypeHandlerAndAsStatic() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{
                new SimpleType(Cloneable.class),
                new SimpleType(Comparable.class)
        };
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, superInterfaces, valueHandler, typeHandler, asStatic);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructor_withClassAndBindingAndSuperClassAndSuperInterfacesAndExtraHashAndValueHandlerAndTypeHandlerAndAsStatic() {
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[]{
                new SimpleType(Cloneable.class),
                new SimpleType(Comparable.class)
        };
        int extraHash = 12345;
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        boolean asStatic = true;

        SimpleType simpleType = new SimpleType(String.class, TypeBindings.emptyBindings(), superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(String.class);
        assertEquals("java.lang.String", simpleType.buildCanonicalName());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstruct_withClass() {
        try {
            SimpleType.construct(Map.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Map (class: java.util.Map)", e.getMessage());
        }

        try {
            SimpleType.construct(Collection.class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for a Collection (class: java.util.Collection)", e.getMessage());
        }

        try {
            SimpleType.construct(String[].class);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException e) {
            assertEquals("Can not construct SimpleType for an array (class: [Ljava.lang.String;)", e.getMessage());
        }
    }

    @Test
    public void testNarrow() {
        SimpleType simpleType = new SimpleType(Object.class);
        JavaType narrowedType = simpleType._narrow(String.class);
        assertEquals("java.lang.String", narrowedType.buildCanonicalName());
        assertFalse(narrowedType.isContainerType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentType(new SimpleType(String.class));
    }

    @Test
    public void testWithTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object typeHandler = new Object();
        SimpleType newSimpleType = simpleType.withTypeHandler(typeHandler);
        assertEquals(typeHandler, newSimpleType.getTypeHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentTypeHandler(new Object());
    }

    @Test
    public void testWithValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        Object valueHandler = new Object();
        SimpleType newSimpleType = simpleType.withValueHandler(valueHandler);
        assertEquals(valueHandler, newSimpleType.getValueHandler());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType simpleType = new SimpleType(Object.class);
        simpleType.withContentValueHandler(new Object());
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(Object.class);
        SimpleType newSimpleType = simpleType.withStaticTyping();
        assertTrue(newSimpleType.isStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertNull(simpleType.refine(String.class, TypeBindings.emptyBindings(), null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("java.lang.Object", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = simpleType.getErasedSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object;", signature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType simpleType = new SimpleType(Object.class);
        StringBuilder signature = simpleType.getGenericSignature(new StringBuilder());
        assertEquals("Ljava/lang/Object;", signature.toString());
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(Object.class);
        assertEquals("[simple type, class java.lang.Object]", simpleType.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType1 = new SimpleType(Object.class);
        SimpleType simpleType2 = new SimpleType(Object.class);
        assertTrue(simpleType1.equals(simpleType2));
    }
}

