
package com.fasterxml.jackson.databind.type;

import org.junit.Assert;
import org.junit.Test;

import java.util.*;

import com.fasterxml.jackson.databind.JavaType;

public class SimpleTypeTest {

    @Test
    public void testConstructSimpleType() {
        SimpleType st = SimpleType.construct(String.class);
        Assert.assertEquals(String.class, st.getRawClass());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructMap() {
        SimpleType.construct(Map.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructCollection() {
        SimpleType.construct(Collection.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testInvalidConstructArray() {
        SimpleType.construct(Array.class);
    }

    @Test
    public void testConstructUnsafe() {
        SimpleType st = SimpleType.constructUnsafe(String.class);
        Assert.assertEquals(String.class, st.getRawClass());
    }

    @Test
    public void testNarrowSubclass() {
        SimpleType st = SimpleType.construct(String.class);
        JavaType narrowedType = st.narrow(CharSequence.class);
        Assert.assertEquals(CharSequence.class, narrowedType.getRawClass());
    }

    @Test
    public void testWithContentType() {
        SimpleType st = SimpleType.construct(String.class);
        Assert.assertThrows(IllegalArgumentException.class,
                () -> st.withContentType(String.class));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        st.withTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentTypeHandler() {
        SimpleType st = SimpleType.construct(String.class);
        st.withContentTypeHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithValueHandler() {
        SimpleType st = SimpleType.construct(String.class);
        st.withValueHandler(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testWithContentValueHandler() {
        SimpleType st = SimpleType.construct(String.class);
        st.withContentValueHandler(null);
    }

    @Test
    public void testWithStaticTyping() {
        SimpleType st = SimpleType.construct(String.class);
        SimpleType staticType = st.withStaticTyping();
        Assert.assertTrue(staticType.isStatic());
    }

    @Test
    public void testRefine() {
        SimpleType st = SimpleType.construct(String.class);
        JavaType refinedType = st.refine(StringBuffer.class, TypeBindings.emptyBindings(), null, null);
        Assert.assertNull(refinedType);
    }

    @Test
    public void testIsContainerType() {
        SimpleType st = SimpleType.construct(String.class);
        Assert.assertFalse(st.isContainerType());
    }

    @Test
    public void testGetErasedSignature() {
        SimpleType st = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder erasedSignature = st.getErasedSignature(sb);

        Assert.assertNotNull(erasedSignature);
        Assert.assertEquals(String.class.getName(), erasedSignature.toString());
    }

    @Test
    public void testGetGenericSignature() {
        SimpleType st = SimpleType.construct(String.class);
        StringBuilder sb = new StringBuilder();
        StringBuilder genericSignature = st.getGenericSignature(sb);

        Assert.assertNotNull(genericSignature);
        Assert.assertTrue(genericSignature.toString().contains(String.class.getName()));
    }

    @Test
    public void testToString() {
        SimpleType st = SimpleType.construct(String.class);
        Assert.assertTrue(st.toString().startsWith("[simple type, class java.lang.String"));
    }

    @Test
    public void testEquals() {
        SimpleType st1 = SimpleType.construct(String.class);
        SimpleType st2 = SimpleType.construct(String.class);
        Assert.assertEquals(st1, st2);
    }
}

