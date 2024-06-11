package com.yourpackage; // replace with your test package

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.TypeFactory;
import com.fasterxml.jackson.databind.JavaType;

public class JavaTypeTest {

    private TypeFactory typeFactory;

    @Before
    public void setup() {
        typeFactory = new TypeFactory();
    }

    @Test
    public void testJavaTypeConstruction() {
        JavaType javaType = typeFactory.constructType(String.class);
        assertEquals("java.lang.String", javaType.getErasedSignature());
        assertEquals("Ljava/lang/String;", javaType.getGenericSignature());
        assertFalse(javaType.isContainerType());
        assertFalse(javaType.isCollectionLikeType());
        assertFalse(javaType.isMapLikeType());
        assertFalse(javaType.isThrowable());
        assertFalse(javaType.isArrayType());
        assertFalse(javaType.isEnumType());
        assertFalse(javaType.isInterface());
        assertFalse(javaType.isPrimitive());
        assertFalse(javaType.isFinal());
        assertFalse(javaType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeIllegalConstruction() {
        JavaType javaType = typeFactory.constructType((Class<?>) null);
    }

    @Test
    public void testJavaTypeNarrow() {
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType stringSubType = typeFactory.constructType(StringBuilder.class);
        JavaType narrowedType = stringType.narrowBy(StringBuilder.class);
        assertEquals(StringBuilder.class, narrowedType.getRawClass());
        assertEquals("Ljava/lang/StringBuilder;", narrowedType.getGenericSignature());
        assertSame(stringType.getValueHandler(), narrowedType.getValueHandler());
        assertSame(stringType.getTypeHandler(), narrowedType.getTypeHandler());
        assertFalse(narrowedType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeIllegalNarrow() {
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType intType = typeFactory.constructType(int.class);
        JavaType narrowedType = stringType.narrowBy(int.class);
    }

    @Test
    public void testJavaTypeWiden() {
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType objectType = typeFactory.constructType(Object.class);
        JavaType widenedType = stringType.widenBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
        assertEquals("Ljava/lang/Object;", widenedType.getGenericSignature());
        assertFalse(widenedType.useStaticType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeIllegalWiden() {
        JavaType intType = typeFactory.constructType(int.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType widenedType = intType.widenBy(String.class);
    }

    @Test
    public void testJavaTypeNarrowContents() {
        JavaType listType = typeFactory.constructType(java.util.List.class);
        JavaType stringListType = listType.narrowContentsBy(String.class);
        assertEquals(String.class, stringListType.getRawClass());
        assertEquals("Ljava/util/List<Ljava/lang/String;>;", stringListType.getGenericSignature());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeIllegalNarrowContents() {
        JavaType listType = typeFactory.constructType(java.util.List.class);
        JavaType intType = typeFactory.constructType(int.class);
        JavaType narrowedType = listType.narrowContentsBy(int.class);
    }

    @Test
    public void testJavaTypeWidenContents() {
        JavaType listType = typeFactory.constructType(java.util.List.class);
        JavaType objectType = typeFactory.constructType(Object.class);
        JavaType widenedType = listType.widenContentsBy(Object.class);
        assertEquals(Object.class, widenedType.getRawClass());
        assertEquals("Ljava/util/List<Ljava/lang/Object;>;", widenedType.getGenericSignature());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testJavaTypeIllegalWidenContents() {
        JavaType listType = typeFactory.constructType(java.util.List.class);
        JavaType stringType = typeFactory.constructType(String.class);
        JavaType widenedType = listType.widenContentsBy(String.class);
    }
}