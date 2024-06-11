package com.fasterxml.jackson.databind.jsontype.impl;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;

public class ClassNameIdResolverTest {

    private ClassNameIdResolver classNameIdResolver;
    
    @Before
    public void setUp() {
        classNameIdResolver = new ClassNameIdResolver(null, null);
    }
    
    @Test
    public void testGetMechanism() {
        assertEquals(JsonTypeInfo.Id.CLASS, classNameIdResolver.getMechanism());
    }
    
    @Test
    public void testRegisterSubtype() {
        // Test that the registerSubtype method does not throw any exception
        // since it is not used with class name-based resolvers
        classNameIdResolver.registerSubtype(String.class, "String");
        assertTrue(true);
    }
    
    @Test
    public void testIdFromValue() {
        String className = classNameIdResolver.idFromValue("Test");
        
        // Test that the idFromValue method returns the expected class name
        assertEquals("java.lang.String", className);
    }
    
    @Test
    public void testIdFromValueAndType() {
        String className = classNameIdResolver.idFromValueAndType("Test", String.class);
        
        // Test that the idFromValueAndType method returns the expected class name
        assertEquals("java.lang.String", className);
    }
    
    @Test
    public void testTypeFromId() {
        try {
            JavaType javaType = classNameIdResolver.typeFromId(null, "java.lang.String");
            
            // Test that the typeFromId method returns the expected JavaType
            assertEquals(String.class, javaType.getRawClass());
        } catch (IOException e) {
            fail("IOException should not occur");
        }
    }
    
}