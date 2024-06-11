package com.fasterxml.jackson.databind; // adjust the package if necessary

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import java.lang.reflect.Modifier;
import java.util.Arrays;

import org.junit.Test;

import com.fasterxml.jackson.core.type.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class JavaTypeTest {

    @Test
    public void testBasicFunctionality() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        assertEquals("Test for equals method", javaType1, javaType1);
        assertEquals("Test for equals method", javaType1, javaType1); // test reflexivity
        assertFalse("Test for not equals method", javaType1, javaType2);

        assertNotNull("Test for getRawClass method", javaType1.getRawClass());
        assertEquals("Test for getRawClass method", String.class, javaType1.getRawClass());

        assertTrue("Test for isAbstract method", javaType1.isAbstract());
        assertFalse("Test for isConcrete method", javaType1.isConcrete());
        assertFalse("Test for isEnumType method", javaType1.isEnumType());
        assertFalse("Test for isInterface method", javaType1.isInterface());
        assertFalse("Test for isPrimitive method", javaType1.isPrimitive());
        assertFalse("Test for isFinal method", javaType1.isFinal());

        JavaType javaType3 = TypeFactory.defaultInstance().constructType(String[].class);
        assertTrue("Test for isContainerType method", javaType3.isContainerType());
        assertFalse("Test for isCollectionLikeType method", javaType3.isCollectionLikeType());
        assertFalse("Test for isMapLikeType method", javaType3.isMapLikeType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentException() {
        JavaType javaType = TypeFactory.defaultInstance().constructType(String[].class);
        javaType.narrowBy(int[].class);
    }

    @Test
    public void testNarrowBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(StringBuffer.class);

        JavaType javaType3 = javaType1.narrowBy(StringBuffer.class);
        assertSame("Test for narrowBy method", javaType3, javaType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNarrowBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        javaType1.narrowBy(Integer.class);
    }

    @Test
    public void testWidenBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Object.class);

        JavaType javaType3 = javaType1.widenBy(Object.class);
        assertSame("Test for widenBy method", javaType3, javaType2);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForWidenBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        javaType1.widenBy(Integer.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalArgumentExceptionForNarrowContentsBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(int[].class);

        javaType1.narrowContentsBy(int[].class);
    }

    @Test
    public void testNarrowContentsBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(StringBuffer[].class);

        JavaType javaType3 = javaType1.narrowContentsBy(StringBuffer[].class);
        assertSame("Test for narrowContentsBy method", javaType3, javaType2);
    }

    @Test
    public void testWidenContentsBy() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Object[].class);

        JavaType javaType3 = javaType1.widenContentsBy(Object[].class);
        assertSame("Test for widenContentsBy method", javaType3, javaType2);
    }

    @Test
    public void testUseStaticType() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(Integer.class);

        assertFalse("Test for useStaticType method for Integer", javaType2.useStaticType());
        assertTrue("Test for useStaticType method for String", javaType1.useStaticType());
    }

    @Test
    public void testContainedTypeOrUnknown() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(StringBuffer[].class);
        JavaType javaType3 = TypeFactory.defaultInstance().constructType(Integer[].class);

        assertEquals("Test for containedTypeOrUnknown method for StringBuffer",
                     javaType2.containedTypeOrUnknown(0), StringBuffer.class);
        assertEquals("Test for containedTypeOrUnknown method for Integer",
                     javaType3.containedTypeOrUnknown(0), Integer.class);
        assertNull("Test for containedTypeOrUnknown method for null contained type",
                   javaType1.containedTypeOrUnknown(0));
    }

    @Test
    public void testToString() {
        JavaType javaType1 = TypeFactory.defaultInstance().constructType(String.class);
        JavaType javaType2 = TypeFactory.defaultInstance().constructType(String[].class);
        JavaType javaType3 = TypeFactory.defaultInstance().constructType(StringBuffer.class);

        assertEquals("Test for toString method for String", "class java.lang.String", javaType1.toString());
        assertEquals("Test for toString method for String[]",
                     "class [Ljava.lang.String;", javaType2.toString());
        assertEquals("Test for toString method for StringBuffer",
                     "class java.lang.StringBuffer", javaType3.toString());
    }
}