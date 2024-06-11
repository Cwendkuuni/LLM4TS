package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.JSTypeRegistry;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {

    @Test
    public void testEmptyRecordTypeBuilder() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        when(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE)).thenReturn(mock(JSType.class));

        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        JSType builtRecordType = recordTypeBuilder.build();

        assertEquals(registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE), builtRecordType);
    }

    @Test
    public void testRecordTypeBuilderWithProperty() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);

        Node propertyNode = mock(Node.class);
        JSType stringType = mock(JSType.class);

        when(registry.getTypeFromNode(propertyNode)).thenReturn(stringType);

        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String propertyName = "propertyName";
        JSType propertyType = stringType;

        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        RecordType builtRecordType = (RecordType) recordTypeBuilder.build();

        Map<String, RecordProperty> properties = builtRecordType.getProperties();
        assertTrue(properties.containsKey(propertyName));
        assertEquals(propertyType, properties.get(propertyName).getType());
        assertEquals(propertyNode, properties.get(propertyName).getPropertyNode());
    }

    @Test
    public void testRecordTypeBuilderWithDuplicateProperty() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);

        Node propertyNode = mock(Node.class);
        JSType stringType = mock(JSType.class);

        when(registry.getTypeFromNode(propertyNode)).thenReturn(stringType);

        RecordTypeBuilder recordTypeBuilder = new RecordTypeBuilder(registry);
        String propertyName = "propertyName";
        JSType propertyType = stringType;

        recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);
        RecordTypeBuilder duplicateRecordTypeBuilder = recordTypeBuilder.addProperty(propertyName, propertyType, propertyNode);

        assertNull(duplicateRecordTypeBuilder);
    }
}