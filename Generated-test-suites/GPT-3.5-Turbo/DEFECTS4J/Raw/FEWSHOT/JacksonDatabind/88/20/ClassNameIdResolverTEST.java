package com.fasterxml.jackson.databind.jsontype.impl;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class ClassNameIdResolverTest {
    
    @Test
    public void testGetMechanism() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        assertEquals(JsonTypeInfo.Id.CLASS, resolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // Test case not applicable to class name based resolvers
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver.idFromValue("value");
        assertEquals("java.lang.String", result);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver.idFromValueAndType("value", String.class);
        assertEquals("java.lang.String", result);
    }
    
    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null; // provide proper context object
        try {
            JavaType result = resolver.typeFromId(context, "java.lang.String");
            // assert statement
        } catch (IOException e) {
            // handle exception
        }
    }
    
    @Test
    public void test_typeFromId_invalidId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = null; // provide proper context object
        try {
            JavaType result = resolver.typeFromId(context, "invalid_id");
            // assert statement
        } catch (IOException e) {
            // assert statement
        }
    }
    
    @Test
    public void test_typeFromId_unknownType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null); // provide proper context object
        try {
            JavaType result = resolver.typeFromId(context, "unknown.Type");
            // assert statement
        } catch (IOException e) {
            // assert statement
        }
    }
    
    @Test
    public void test_typeFromId_exception() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        DatabindContext context = new DeserializationContext(null, null, null); // provide proper context object
        try {
            JavaType result = resolver.typeFromId(context, "java.lang.Integer");
            // assert statement
        } catch (IOException e) {
            // handle exception
        }
    }
    
    /* Internal methods */
    
    @Test
    public void test_idFrom_withEnum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver._idFrom(TestEnum.ONE, TestEnum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TestEnum", result);
    }
    
    @Test
    public void test_idFrom_withEnumSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver._idFrom(SubTestEnum.ONE, SubTestEnum.class, null);
        assertEquals("com.fasterxml.jackson.databind.jsontype.impl.TestEnum", result);
    }
    
    @Test
    public void test_idFrom_withUtilClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver._idFrom(new EnumSet<TestEnum>(TestEnum.class), EnumSet.class, null);
        assertEquals("java.util.EnumSet", result);
    }
    
    @Test
    public void test_idFrom_withUtilClassInner() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver._idFrom(new EnumSet<SubTestEnum>(SubTestEnum.class), EnumSet.class, null);
        assertEquals("java.util.EnumSet", result);
    }
    
    @Test
    public void test_idFrom_withArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver._idFrom(new ArrayList<String>(), ArrayList.class, null);
        assertEquals("java.util.ArrayList", result);
    }
    
    
    @Test
    public void test_getDescForKnownTypeIds() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String result = resolver.getDescForKnownTypeIds();
        assertEquals("class name used as type id", result);
    }
    
    private enum TestEnum {
        ONE, TWO, THREE;
    }
    
    private enum SubTestEnum {
        ONE, TWO, THREE;
    }
}