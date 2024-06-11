
package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ClassNameIdResolverTest {
    
    private ClassNameIdResolver resolver;
    private JavaType baseType;
    private TypeFactory typeFactory;
    
    @Before
    public void setUp() {
        baseType = TypeFactory.defaultInstance().constructType(ClassNameIdResolver.class);
        typeFactory = TypeFactory.defaultInstance();
        resolver = new ClassNameIdResolver(baseType, typeFactory);
    }
    
    @Test
    public void testGetMechanism() {
        JsonTypeInfo.Id mechanism = resolver.getMechanism();
        assertNotNull(mechanism);
        assertEquals(JsonTypeInfo.Id.CLASS, mechanism);
    }
    
    @Test
    public void testRegisterSubtype() {
        // Registering subtype should have no effect in ClassNameIdResolver
        resolver.registerSubtype(ClassNameIdResolver.class, "Resolver");
    }
    
    @Test
    public void testIdFromValue() {
        // Positive test case - value is not null
        String id = resolver.idFromValue(new ClassNameIdResolver(baseType, typeFactory));
        assertNotNull(id);
        assertFalse(id.isEmpty());
        
        // Negative test case - value is null
        id = resolver.idFromValue(null);
        assertNull(id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        // Positive test case - value and type are not null
        String id = resolver.idFromValueAndType(new ClassNameIdResolver(baseType, typeFactory), ClassNameIdResolver.class);
        assertNotNull(id);
        assertFalse(id.isEmpty());
        
        // Negative test case - value is null
        id = resolver.idFromValueAndType(null, ClassNameIdResolver.class);
        assertNull(id);
        
        // Negative test case - type is null
        id = resolver.idFromValueAndType(new ClassNameIdResolver(baseType, typeFactory), null);
        assertNull(id);
    }
    
    @Test
    public void testTypeFromId() {
        try {
            // Positive test case - id is valid
            JavaType type = resolver.typeFromId(null, ClassNameIdResolver.class.getName());
            assertNotNull(type);
            assertEquals(ClassNameIdResolver.class, type.getRawClass());
            
            // Negative test case - id is null
            type = resolver.typeFromId(null, null);
            assertNull(type);
            
            // Negative test case - id is empty
            type = resolver.typeFromId(null, "");
            assertNull(type);
            
            // Negative test case - id is invalid (non-existing class)
            type = resolver.typeFromId(null, "com.fasterxml.jackson.NonExistingClass");
            assertNull(type);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void test_typeFromId_withDeserializationContext() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null, null);
            
            // Positive test case - id is valid
            JavaType type = resolver.typeFromId(ctxt, ClassNameIdResolver.class.getName());
            assertNotNull(type);
            assertEquals(ClassNameIdResolver.class, type.getRawClass());
            
            // Negative test case - id is null
            type = resolver.typeFromId(ctxt, null);
            assertNull(type);
            
            // Negative test case - id is empty
            type = resolver.typeFromId(ctxt, "");
            assertNull(type);
            
            // Negative test case - id is invalid (non-existing class)
            type = resolver.typeFromId(ctxt, "com.fasterxml.jackson.NonExistingClass");
            assertNull(type);
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void test_typeFromId_withDeserializationContext_handleUnknownTypeId() {
        try {
            DeserializationContext ctxt = new DeserializationContext(null, null, null);
            
            // Positive test case - id is valid
            JavaType type = resolver.typeFromId(ctxt, ClassNameIdResolver.class.getName());
            assertNotNull(type);
            assertEquals(ClassNameIdResolver.class, type.getRawClass());
            
            // Negative test case - id is invalid (non-existing class)
            type = resolver.typeFromId(ctxt, "com.fasterxml.jackson.NonExistingClass");
            assertNull(type);
            
            // Negative test case - id is invalid (non-existing class) with custom error message
            ClassNameIdResolver customResolver = new ClassNameIdResolver(baseType, typeFactory) {
                @Override
                public JavaType handleUnknownTypeId(JavaType baseType, String id, TypeIdResolver res, String failureMsg) throws IOException {
                    throw new IOException("Unknown class: " + id);
                }
            };
            try {
                type = customResolver.typeFromId(ctxt, "com.fasterxml.jackson.NonExistingClass");
                fail("IOException not thrown");
            } catch (IOException e) {
                assertEquals("Unknown class: com.fasterxml.jackson.NonExistingClass", e.getMessage());
            }
        } catch (Exception e) {
            fail("Exception occurred: " + e.getMessage());
        }
    }
    
    @Test
    public void test_idFrom() {
        // Positive test case - value is not null
        String id = resolver._idFrom(new ClassNameIdResolver(baseType, typeFactory), ClassNameIdResolver.class, typeFactory);
        assertNotNull(id);
        assertFalse(id.isEmpty());
        
        // Negative test case - value is null
        id = resolver._idFrom(null, ClassNameIdResolver.class, typeFactory);
        assertNull(id);
        
        // Testing with enum subtype
        id = resolver._idFrom(TestEnum.A, TestEnum.class, typeFactory);
        assertNotNull(id);
        assertFalse(id.isEmpty());
    }
    
    @Test
    public void testGetDescForKnownTypeIds() {
        String desc = resolver.getDescForKnownTypeIds();
        assertNotNull(desc);
        assertFalse(desc.isEmpty());
        assertEquals("class name used as type id", desc);
    }
    
    private enum TestEnum {
        A, B, C
    }
    
}
