package com.google.javascript.rhino.jstype;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.google.javascript.rhino.Node;

public class RecordTypeBuilderTest {

    private JSTypeRegistry registry;
    private RecordTypeBuilder builder;
    
    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        builder = new RecordTypeBuilder(registry);
    }
    
    @Test
    public void testEmptyBuilder() {
        JSType type = builder.build();
        assertTrue(type instanceof ObjectType);
        assertEquals(JSTypeNative.OBJECT_TYPE, type.getNativeType());
    }
    
    @Test
    public void testAddProperty() {
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node node = new Node();
        builder.addProperty("name", stringType, node);
        JSType type = builder.build();
        assertTrue(type instanceof RecordType);
        assertEquals(1, ((RecordType) type).getProperties().size());
        assertEquals(stringType, ((RecordType) type).getPropertyType("name"));
    }
    
    @Test
    public void testDuplicateProperty() {
        JSType stringType = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node node1 = new Node();
        Node node2 = new Node();
        builder.addProperty("name", stringType, node1);
        builder.addProperty("name", stringType, node2);
        assertNull(builder.build());
    }
    
    // add more test cases to cover all the methods in RecordTypeBuilder class
    
}