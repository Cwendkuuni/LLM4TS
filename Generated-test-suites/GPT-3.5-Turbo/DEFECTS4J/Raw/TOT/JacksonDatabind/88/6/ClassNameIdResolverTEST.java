
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.util.EnumMap;
import java.util.EnumSet;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;

    @Before
    public void setup() {
        JavaType baseType = TypeFactory.defaultInstance().constructSimpleType(ClassNameIdResolver.class, null);
        classNameIdResolver = new ClassNameIdResolver(baseType, TypeFactory.defaultInstance());
    }

    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }

    @Test
    public void testRegisterSubtype() {
        // Checking that the method does nothing
        classNameIdResolver.registerSubtype(Object.class, "");
    }

    @Test
    public void testIdFromValue() {
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        String actual = classNameIdResolver.idFromValue(classNameIdResolver);
        assertEquals(expected, actual);
    }

    @Test
    public void testIdFromValueAndType() {
        String expected = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        String actual = classNameIdResolver.idFromValueAndType(classNameIdResolver, ClassNameIdResolver.class);
        assertEquals(expected, actual);
    }

    @Test
    public void testTypeFromId() throws IOException {
        String id = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        JavaType expected = TypeFactory.defaultInstance().constructSimpleType(ClassNameIdResolver.class, null);
        JavaType actual = classNameIdResolver.typeFromId(null, id);
        assertEquals(expected, actual);
    }

    @Test
    public void testTypeFromId_UnknownId() throws IOException {
        String unknownId = "unknownId";
        JavaType actual = classNameIdResolver.typeFromId(null, unknownId);
        assertNull(actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testTypeFromId_InvalidTypeId() throws IOException {
        String invalidId = "java.lang.String";
        classNameIdResolver.typeFromId(null, invalidId);
    }

    // Internal methods

    @Test
    public void testIdFrom_ValueEnum() {
        String expected = "java.util.EnumSet<java.util.Enum>";
        String actual = classNameIdResolver._idFrom(EnumSet.allOf(Enum.class), EnumSet.class, TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }

    @Test
    public void testIdFrom_ValueEnumMap() {
        String expected = "java.util.EnumMap<java.util.Enum, java.lang.Object>";
        String actual = classNameIdResolver._idFrom(new EnumMap<>(Enum.class), EnumMap.class, TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }

    @Test
    public void testIdFrom_ValueJavaUtil() {
        String expected = "java.util.ArrayList";
        String actual = classNameIdResolver._idFrom(new ArrayList<>(), ArrayList.class, TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }

    @Test
    public void testIdFrom_ValueInnerClass() {
        // Not possible to test this scenario
    }

    @Test
    public void testIdFrom_ValueValidClass() {
        String expected = "java.lang.String";
        String actual = classNameIdResolver._idFrom("", String.class, TypeFactory.defaultInstance());
        assertEquals(expected, actual);
    }

    @Test
    public void testGetDescForKnownTypeIds() {
        // Not possible to test this method
    }

}

