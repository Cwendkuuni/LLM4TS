package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;
import static org.mockito.Matchers.anyString;
import static org.mockito.Mockito.*;

public class RecordTypeBuilderTest {
    private JSTypeRegistry registry;
    private JSType objectType;
    private RecordTypeBuilder builder;

    @Before
    public void setUp() {
        registry = mock(JSTypeRegistry.class);
        objectType = mock(JSType.class);
        builder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_notDuplicate() {
        Node propertyNode = mock(Node.class);
        JSType propertyType = mock(JSType.class);

        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectType);

        builder.addProperty("name", propertyType, propertyNode);

        JSType recordType = builder.build();

        verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertNotNull(recordType);
    }

    @Test
    public void testAddProperty_duplicate() {
        Node propertyNode = mock(Node.class);
        JSType propertyType = mock(JSType.class);

        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectType);

        builder.addProperty("name", propertyType, propertyNode);
        builder.addProperty("name", propertyType, propertyNode);

        JSType recordType = builder.build();

        verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertNull(recordType);
    }

    @Test
    public void testBuild_emptyRecord() {
        Node propertyNode = mock(Node.class);
        JSType recordType = builder.build();

        verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertNotNull(recordType);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        Node propertyNode = mock(Node.class);
        JSType propertyType = mock(JSType.class);

        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(objectType);

        builder.addProperty("name", propertyType, propertyNode);

        JSType recordType = builder.build();

        verify(registry).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertNotNull(recordType);
    }

    @Test
    public void testRecordProperty_getType() {
        Node propertyNode = mock(Node.class);
        JSType propertyType = mock(JSType.class);

        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(propertyType, propertyNode);

        assertEquals(propertyType, recordProperty.getType());
    }

    @Test
    public void testRecordProperty_getPropertyNode() {
        Node propertyNode = mock(Node.class);
        JSType propertyType = mock(JSType.class);

        RecordTypeBuilder.RecordProperty recordProperty = new RecordTypeBuilder.RecordProperty(propertyType, propertyNode);

        assertEquals(propertyNode, recordProperty.getPropertyNode());
    }
}