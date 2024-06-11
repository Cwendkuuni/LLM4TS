package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import java.util.Map;

import static org.junit.Assert.*;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;
    private JSTypeRegistry registry;

    @Before
    public void setUp() {
        registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty_emptyRecord() {
        String name = "property1";
        JSType type = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode = new Node();
        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type, propertyNode);

        assertNull(result);
        Map<String, RecordTypeBuilder.RecordProperty> properties = recordTypeBuilder.getProperties();
        assertTrue(properties.isEmpty());
    }

    @Test
    public void testAddProperty_duplicateProperty() {
        String name = "property1";
        JSType type1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        JSType type2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode = new Node();

        recordTypeBuilder.addProperty(name, type1, propertyNode);
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = recordTypeBuilder.getProperties();
        assertFalse(properties1.isEmpty());

        RecordTypeBuilder result = recordTypeBuilder.addProperty(name, type2, propertyNode);

        assertNull(result);
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = recordTypeBuilder.getProperties();
        assertEquals(properties1, properties2);
    }

    @Test
    public void testAddProperty_differentProperties() {
        String name1 = "property1";
        JSType type1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode1 = new Node();

        String name2 = "property2";
        JSType type2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode2 = new Node();

        recordTypeBuilder.addProperty(name1, type1, propertyNode1);
        Map<String, RecordTypeBuilder.RecordProperty> properties1 = recordTypeBuilder.getProperties();
        assertFalse(properties1.isEmpty());
        assertEquals(1, properties1.size());

        recordTypeBuilder.addProperty(name2, type2, propertyNode2);
        Map<String, RecordTypeBuilder.RecordProperty> properties2 = recordTypeBuilder.getProperties();
        assertFalse(properties2.isEmpty());
        assertEquals(2, properties2.size());
    }

    @Test
    public void testBuild_emptyRecord() {
        JSType expectedType = registry.getNativeObjectType(JSTypeNative.OBJECT_TYPE);
        JSType result = recordTypeBuilder.build();

        assertEquals(expectedType, result);
    }

    @Test
    public void testBuild_nonEmptyRecord() {
        String name1 = "property1";
        JSType type1 = registry.getNativeType(JSTypeNative.STRING_TYPE);
        Node propertyNode1 = new Node();

        String name2 = "property2";
        JSType type2 = registry.getNativeType(JSTypeNative.NUMBER_TYPE);
        Node propertyNode2 = new Node();

        recordTypeBuilder.addProperty(name1, type1, propertyNode1);
        recordTypeBuilder.addProperty(name2, type2, propertyNode2);

        Map<String, RecordTypeBuilder.RecordProperty> properties = recordTypeBuilder.getProperties();
        assertFalse(properties.isEmpty());

        JSType result = recordTypeBuilder.build();
        assertTrue(result instanceof RecordType);
        assertEquals(properties, ((RecordType) result).getProperties());
    }
}