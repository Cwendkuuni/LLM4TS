import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertNotNull;

import java.util.*;

import org.junit.Test;

public class SimpleTypeTest {

    @Test
    public void testConstructor() {
        SimpleType type = new SimpleType(String.class);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindings() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        SimpleType type = new SimpleType(String.class, bindings, null, null);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClass() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        SimpleType type = new SimpleType(String.class, bindings, superClass, null);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClassAndSuperInterfaces() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[2];
        superInterfaces[0] = new SimpleType(List.class);
        superInterfaces[1] = new SimpleType(Set.class);
        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(2, type.getInterfaces().length);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClassAndSuperInterfacesAndValueHandlerAndTypeHandler() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[2];
        superInterfaces[0] = new SimpleType(List.class);
        superInterfaces[1] = new SimpleType(Set.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces, valueHandler, typeHandler, false);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(2, type.getInterfaces().length);
        assertEquals(valueHandler, type.getValueHandler());
        assertEquals(typeHandler, type.getTypeHandler());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testConstructorWithTypeBindingsAndSuperClassAndSuperInterfacesAndExtraHashAndValueHandlerAndTypeHandlerAndAsStatic() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBinding(String.class, Integer.class);
        JavaType superClass = new SimpleType(Object.class);
        JavaType[] superInterfaces = new JavaType[2];
        superInterfaces[0] = new SimpleType(List.class);
        superInterfaces[1] = new SimpleType(Set.class);
        Object valueHandler = new Object();
        Object typeHandler = new Object();
        int extraHash = 123;
        boolean asStatic = true;
        SimpleType type = new SimpleType(String.class, bindings, superClass, superInterfaces, extraHash, valueHandler, typeHandler, asStatic);
        assertNotNull(type);
        assertEquals(String.class, type.getRawClass());
        assertEquals(bindings, type.getBindings());
        assertEquals(superClass, type.getSuperClass());
        assertEquals(2, type.getInterfaces().length);
        assertEquals(extraHash, type.getExtraHash());
        assertEquals(valueHandler, type.getValueHandler());
        assertEquals(typeHandler, type.getTypeHandler());
        assertTrue(type.isStatic());
        assertFalse(type.isContainerType());
    }

    @Test
    public void testWithContentType() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentType(new SimpleType(Integer.class));
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContentType()", e.getMessage());
        }
    }

    @Test
    public void testWithGenericTypeHandler() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withGenericTypeHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenTypeHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithValueHandler() {
        Object valueHandler = new Object();
        SimpleType type = new SimpleType(String.class);
        SimpleType newType = type.withValueHandler(valueHandler);
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertEquals(valueHandler, newType.getValueHandler());
        assertEquals(type.getTypeHandler(), newType.getTypeHandler());
        assertFalse(newType.isContainerType());
    }

    @Test
    public void testWithContentValueHandler() {
        SimpleType type = new SimpleType(String.class);
        try {
            type.withContentValueHandler(new Object());
        } catch (IllegalArgumentException e) {
            assertEquals("Simple types have no content types; can not call withContenValueHandler()", e.getMessage());
        }
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType type = new SimpleType(String.class);
        assertFalse(type.isStatic());
        SimpleType newType = type.withStaticTyping();
        assertNotNull(newType);
        assertEquals(String.class, newType.getRawClass());
        assertTrue(newType.isStatic());
        assertFalse(newType.isContainerType());
    }

    @Test
    public void testRefine() {
        SimpleType type = new SimpleType(String.class);
        assertNull(type.refine(String.class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        SimpleType type = new SimpleType(String.class);
        assertEquals("java.lang.String", type.getCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType type = new SimpleType(String.class);
        assertFalse(type.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getErasedSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType type = new SimpleType(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder result = type.getGenericSignature(sb);
        assertNotNull(result);
        assertEquals(sb, result);
    }

    @Test
    public void testToString() {
        SimpleType type = new SimpleType(String.class);
        assertEquals("[simple type, class java.lang.String]", type.toString());
    }

    @Test
    public void testEquals() {
        SimpleType type1 = new SimpleType(String.class);
        SimpleType type2 = new SimpleType(String.class);
        assertTrue(type1.equals(type2));
    }

}