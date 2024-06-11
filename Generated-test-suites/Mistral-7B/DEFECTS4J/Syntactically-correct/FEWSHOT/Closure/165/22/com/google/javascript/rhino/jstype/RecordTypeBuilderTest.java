package com.google.javascript.rhino.jstype;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.Node;
import com.google.javascript.rhino.JSType;
import com.google.javascript.rhino.testing.TypeMock;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
    @Test
    public void testAddProperty() {
        JSTypeRegistry registryMock = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registryMock);

        String propertyName = "name";
        JSType propertyType = TypeMock.createType("String");
        Node propertyNode = mock(Node.class);

        RecordTypeBuilder returnedBuilder = builder.addProperty(propertyName, propertyType, propertyNode);
        Map<String, RecordProperty> properties = builder.properties;

        verify(registryMock).registerType(propertyType);
        assertEquals(propertyType, properties.get(propertyName).getType());
        assertEquals(propertyNode, properties.get(propertyName).getPropertyNode());
        assertSame(builder, returnedBuilder);
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registryMock = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registryMock);

        String propertyName = "name";
        JSType propertyType = TypeMock.createType("String");
        Node propertyNode = mock(Node.class);

        builder.addProperty(propertyName, propertyType, propertyNode);

        assertNull(builder.addProperty(propertyName, propertyType, propertyNode));

        Map<String, RecordProperty> properties = builder.properties;
        assertEquals(1, properties.size());
    }

    @Test
    public void testBuildEmptyRecord() {
        JSTypeRegistry registryMock = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registryMock);

        JSType result = builder.build();

        verify(registryMock).getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertEquals(JSTypeNative.OBJECT_TYPE, result.getType());
    }

    @Test
    public void testBuildRecordWithProperties() {
        JSTypeRegistry registryMock = mock(JSTypeRegistry.class);

        RecordTypeBuilder builder = new RecordTypeBuilder(registryMock);

        String propertyName1 = "name1";
        JSType propertyType1 = TypeMock.createType("Number");
        Node propertyNode1 = mock(Node.class);

        String propertyName2 = "name2";
        JSType propertyType2 = TypeMock.createType("String");
        Node propertyNode2 = mock(Node.class);

        builder.addProperty(propertyName1, propertyType1, propertyNode1);
        builder.addProperty(propertyName2, propertyType2, propertyNode2);

        JSType result = builder.build();

        RecordType recordType = (RecordType) result;
        Map<String, RecordProperty> properties = recordType.getProperties();
        assertEquals(2, properties.size());

        assertEquals(propertyName1, properties.get(propertyName1).getName());
        assertEquals(propertyType1, properties.get(propertyName1).getType());
        assertEquals(propertyNode1, properties.get(propertyName1).getPropertyNode());

        assertEquals(propertyName2, properties.get(propertyName2).getName());
        assertEquals(propertyType2, properties.get(propertyName2).getType());
        assertEquals(propertyNode2, properties.get(propertyName2).getPropertyNode());
    }
}