package com.google.javascript.rhino.jstype;

import com.google.javascript.rhino.Node;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class RecordTypeBuilderTest {

    private RecordTypeBuilder recordTypeBuilder;

    @Before
    public void setUp() {
        JSTypeRegistry registry = new JSTypeRegistry();
        recordTypeBuilder = new RecordTypeBuilder(registry);
    }

    @Test
    public void testAddProperty() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        RecordTypeBuilder result = recordTypeBuilder.addProperty("testProperty", type, propertyNode);
        assertNotNull(result);
    }

    @Test
    public void testBuildEmptyRecord() {
        JSType result = recordTypeBuilder.build();
        assertEquals(JSTypeNative.OBJECT_TYPE, result);
    }

    @Test
    public void testBuildRecordWithProperties() {
        JSType type = new JSType();
        Node propertyNode = new Node();
        recordTypeBuilder.addProperty("testProperty", type, propertyNode);
        JSType result = recordTypeBuilder.build();
        assertNotNull(result);
        assertEquals(1, ((RecordType)result).getProperties().size());
    }

}