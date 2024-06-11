package com.fasterxml.jackson.databind.jsontype.impl;

import org.junit.*;
import static org.junit.Assert.*;

public class ClassNameIdResolverTest {
    
    @Test
    public void testRegisterSubtype() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        Class<?> type = String.class;
        String name = "String";
        
        // Test that registerSubtype method does not perform any actions
        resolver.registerSubtype(type, name);
        
        // Check that no changes were made
        assertNull(resolver.getIdFromValue(type));
    }
    
    @Test
    public void testIdFromValue() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValue method for a String object
        String value = "Hello World";
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValueAndType method for a String object
        String value = "Hello World";
        Class<?> type = String.class;
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testTypeFromId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String id = "java.lang.String";
        
        // Test the typeFromId method for a valid id
        JavaType expectedType = TypeFactory.defaultInstance().constructType(String.class);
        try {
            JavaType type = resolver.typeFromId(null, id);
            
            // Check that the correct type is returned
            assertEquals(expectedType, type);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown.");
        }
    }
    
    @Test
    public void testTypeFromId_InvalidId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String invalidId = "com.example.NonExistentClass";
        
        // Test the typeFromId method for an invalid id
        try {
            JavaType type = resolver.typeFromId(null, invalidId);
            
            // Check that null is returned for invalid id
            assertNull(type);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown.");
        }
    }
    
    @Test
    public void testTypeFromId_UnknownTypeId() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String unknownId = "com.fasterxml.jackson.core.UnknownType";
        DatabindContext context = null;
        
        // Test the typeFromId method for an unknown id
        try {
            JavaType type = resolver.typeFromId(context, unknownId);
            
            // Check that handleUnknownTypeId method is called
            // and the correct JavaType object is returned
            assertEquals(expectedType, type);
        } catch (IOException e) {
            e.printStackTrace();
            fail("IOException was thrown.");
        }
    }
    
    @Test
    public void testIdFromValue_Enum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValue method for an enum object
        Enum<?> value = TestEnum.FIRST;
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_Enum() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValueAndType method for an enum object
        Enum<?> value = TestEnum.FIRST;
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_EnumSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValue method for an enum subclass object
        Enum<?> value = TestEnumSubclass.SUBCLASS;
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_EnumSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.lang.String";
        
        // Test the idFromValueAndType method for an enum subclass object
        Enum<?> value = TestEnumSubclass.SUBCLASS;
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_EnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.EnumSet<java.lang.String>";
        
        // Test the idFromValue method for an EnumSet object
        EnumSet<?> value = EnumSet.of(TestEnum.FIRST, TestEnum.SECOND);
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_EnumSet() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.EnumSet<java.lang.String>";
        
        // Test the idFromValueAndType method for an EnumSet object
        EnumSet<?> value = EnumSet.of(TestEnum.FIRST, TestEnum.SECOND);
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_EnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.EnumMap<java.lang.String, java.lang.Object>";
        
        // Test the idFromValue method for an EnumMap object
        EnumMap<?, ?> value = new EnumMap<>(TestEnum.class);
        value.put(TestEnum.FIRST, "First");
        value.put(TestEnum.SECOND, "Second");
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_EnumMap() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.EnumMap<java.lang.String, java.lang.Object>";
        
        // Test the idFromValueAndType method for an EnumMap object
        EnumMap<?, ?> value = new EnumMap<>(TestEnum.class);
        value.put(TestEnum.FIRST, "First");
        value.put(TestEnum.SECOND, "Second");
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_ArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.ArrayList";
        
        // Test the idFromValue method for an ArrayList object
        List<?> value = new ArrayList<>();
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_ArrayList() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.ArrayList";
        
        // Test the idFromValueAndType method for an ArrayList object
        List<?> value = new ArrayList<>();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_CollectionSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.ArrayList";
        
        // Test the idFromValue method for a Collection subclass object
        List<?> value = new ArrayList<>();
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_CollectionSubclass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "java.util.ArrayList";
        
        // Test the idFromValueAndType method for a Collection subclass object
        List<?> value = new ArrayList<>();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValue_InnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        
        // Test the idFromValue method for an inner class object
        ClassNameIdResolver.InnerClass value = new ClassNameIdResolver.InnerClass();
        String id = resolver.idFromValue(value);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
    
    @Test
    public void testIdFromValueAndType_InnerClass() {
        ClassNameIdResolver resolver = new ClassNameIdResolver(null, null);
        String expectedId = "com.fasterxml.jackson.databind.jsontype.impl.ClassNameIdResolver";
        
        // Test the idFromValueAndType method for an inner class object
        ClassNameIdResolver.InnerClass value = new ClassNameIdResolver.InnerClass();
        Class<?> type = value.getClass();
        String id = resolver.idFromValueAndType(value, type);
        
        // Check that the correct id is returned
        assertEquals(expectedId, id);
    }
}