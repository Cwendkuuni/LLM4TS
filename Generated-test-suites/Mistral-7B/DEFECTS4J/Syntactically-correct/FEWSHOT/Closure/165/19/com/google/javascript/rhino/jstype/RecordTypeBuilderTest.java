package com.google.javascript.rhino.jstype;

import static com.google.common.truth.Truth.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import com.google.common.collect.Maps;
import com.google.javascript.rhino.JSTypeNative;
import com.google.javascript.rhino.JSTypeRegistry;
import com.google.javascript.rhino.Node;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class RecordTypeBuilderTest {
    @Test
    public void testEmptyRecord() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        JSType result = builder.build();
        JSType expected = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        assertThat(result).isSameInstanceAs(expected);
    }

    @Test
    public void testAddProperty() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        String propertyName = "foo";
        JSType propertyType = mock(JSType.class);
        Node propertyNode = mock(Node.class);
        when(registry.createType(any(JSType.class))).thenReturn(propertyType);
        builder.addProperty(propertyName, propertyType, propertyNode);
        RecordType result = (RecordType) builder.build();
        Map<String, RecordProperty> properties = result.getProperties();
        RecordProperty property = properties.get(propertyName);
        JSType propertyTypeResult = property.getType();
        Node propertyNodeResult = property.getPropertyNode();
        assertThat(propertyTypeResult).isSameInstanceAs(propertyType);
        assertThat(propertyNodeResult).isSameInstanceAs(propertyNode);
    }

    @Test
    public void testAddDuplicateProperty() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        String propertyName = "foo";
        JSType propertyType = mock(JSType.class);
        Node propertyNode = mock(Node.class);
        RecordTypeBuilder result = builder.addProperty(propertyName, propertyType, propertyNode);
        assertThat(result).isNull();
    }

    @Test
    public void testBuildWithProperties() {
        JSTypeRegistry registry = mock(JSTypeRegistry.class);
        RecordTypeBuilder builder = new RecordTypeBuilder(registry);
        String propertyName1 = "foo";
        JSType propertyType1 = mock(JSType.class);
        Node propertyNode1 = mock(Node.class);
        String propertyName2 = "bar";
        JSType propertyType2 = mock(JSType.class);
        Node propertyNode2 = mock(Node.class);
        when(registry.createType(any(JSType.class))).thenAnswer(invocation -> invocation.getArguments()[0]);
        builder.addProperty(propertyName1, propertyType1, propertyNode1);
        builder.addProperty(propertyName2, propertyType2, propertyNode2);
        RecordType result = (RecordType) builder.build();
        Map<String, RecordProperty> properties = result.getProperties();
        RecordProperty property1 = properties.get(propertyName1);
        RecordProperty property2 = properties.get(propertyName2);
        JSType propertyType1Result = property1.getType();
        JSType propertyType2Result = property2.getType();
        assertThat(propertyType1Result).isSameInstanceAs(propertyType1);
        assertThat(propertyType2Result).isSameInstanceAs(propertyType2);
    }
}