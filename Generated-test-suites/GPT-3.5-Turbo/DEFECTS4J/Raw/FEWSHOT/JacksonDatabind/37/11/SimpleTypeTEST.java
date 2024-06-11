
package com.fasterxml.jackson.databind.type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {
    @Test
    public void testConstructor() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertEquals(ArrayList.class, simpleType.getRawClass());

        simpleType = new SimpleType(ArrayList.class, new TypeBindings());
        assertEquals(ArrayList.class, simpleType.getRawClass());

        simpleType = new SimpleType(ArrayList.class, new TypeBindings(), null, null);
        assertEquals(ArrayList.class, simpleType.getRawClass());

        JavaType superClass = new SimpleType(Collection.class);
        JavaType[] superInterfaces = new JavaType[0];
        simpleType = new SimpleType(ArrayList.class, new TypeBindings(), superClass, superInterfaces);
        assertEquals(ArrayList.class, simpleType.getRawClass());
        assertEquals(superClass, simpleType.getSuperClass());
        assertEquals(superInterfaces.length, simpleType.getInterfaces().size());
    }

    @Test
    public void testCopyConstructor() {
        SimpleType baseType = new SimpleType(ArrayList.class);
        SimpleType simpleType = new SimpleType(baseType);
        assertEquals(ArrayList.class, simpleType.getRawClass());
        assertEquals(baseType, simpleType.getSuperClass());
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType simpleType = SimpleType.constructUnsafe(ArrayList.class);
        assertEquals(ArrayList.class, simpleType.getRawClass());
    }

    @Test
    public void testConstruct() {
        SimpleType simpleType = SimpleType.construct(ArrayList.class);
        assertEquals(ArrayList.class, simpleType.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testConstructWithArray() {
        SimpleType.construct(Object[].class);
    }

    @Test
    public void testNarrow() {
        SimpleType baseType = new SimpleType(ArrayList.class);
        SimpleType narrowedType = baseType.narrow(ArrayList.class);
        assertEquals(baseType, narrowedType);

        narrowedType = baseType.narrow(Collection.class);
        assertEquals(baseType, narrowedType);

        narrowedType = baseType.narrow(Map.class);
        assertEquals(baseType, narrowedType);

        narrowedType = baseType.narrow(Object.class);
        assertEquals(baseType, narrowedType);
    }

    @Test
    public void testWithContentType() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertNull(simpleType.withContentType(new SimpleType(Integer.class)));
    }

    @Test
    public void testWithValueType() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertNull(simpleType.withValueHandler("valueHandler"));
    }

    @Test
    public void testWithContentTypeHandler() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertNull(simpleType.withContentTypeHandler("contentTypeHandler"));
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertEquals(simpleType, simpleType.withStaticTyping());
    }

    @Test
    public void testRefine() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertNull(simpleType.refine(ArrayList.class, null, null, null));
    }

    @Test
    public void testBuildCanonicalName() {
        TypeBindings bindings = new TypeBindings();
        bindings.addBoundType(0, new SimpleType(Integer.class));
        SimpleType simpleType = new SimpleType(ArrayList.class, bindings, null, null);
        assertEquals("java.util.ArrayList<java.lang.Integer>", simpleType.buildCanonicalName());
    }

    @Test
    public void testIsContainerType() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertFalse(simpleType.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        StringBuilder builder = new StringBuilder();
        SimpleType simpleType = new SimpleType(ArrayList.class);
        simpleType.getErasedSignature(builder);
        assertEquals("Ljava/util/ArrayList;", builder.toString());
    }

    @Test
    public void testGetGenericSignature() {
        StringBuilder builder = new StringBuilder();
        SimpleType simpleType = new SimpleType(ArrayList.class);
        simpleType.getGenericSignature(builder);
        assertEquals("Ljava/util/ArrayList<*>;", builder.toString());
    }

    @Test
    public void testEquals() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertEquals(simpleType, simpleType);
        assertFalse(simpleType.equals(null));
        
        SimpleType otherType = new SimpleType(ArrayList.class);
        assertEquals(simpleType, otherType);
        
        otherType = new SimpleType(HashMap.class);
        assertFalse(simpleType.equals(otherType));
    }

    @Test
    public void testToString() {
        SimpleType simpleType = new SimpleType(ArrayList.class);
        assertEquals("[simple type, class java.util.ArrayList]", simpleType.toString());
    }
}
