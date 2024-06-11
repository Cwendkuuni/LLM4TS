package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class StdSerializerTest {

    private StdSerializer<TestObject> serializer;

    @Before
    public void setUp() {
        serializer = new StdSerializer<TestObject>(TestObject.class) {
            @Override
            public void serialize(TestObject value, JsonGenerator jgen, SerializerProvider provider)
                    throws IOException, JsonGenerationException {
                //TODO implement serialization
            }
        };
    }

    @Test
    public void testHandledType() {
        assertEquals(TestObject.class, serializer.handledType());
    }

    @Test
    public void testSerialize() {
        //TODO implement test
    }

    @Test
    public void testGetSchema() {
        //TODO implement test
    }

    @Test
    public void testGetSchemaWithOptional() {
        //TODO implement test
    }

    @Test
    public void testCreateObjectNode() {
        ObjectNode node = serializer.createObjectNode();
        assertNotNull(node);
        assertEquals(ObjectNode.class, node.getClass());
    }

    @Test
    public void testCreateSchemaNode() {
        String type = "string";
        ObjectNode node = serializer.createSchemaNode(type);
        assertNotNull(node);
        assertEquals(ObjectNode.class, node.getClass());
        assertEquals(type, node.get("type"));
    }

    @Test
    public void testCreateSchemaNodeWithOptional() {
        String type = "string";
        boolean isOptional = true;
        ObjectNode node = serializer.createSchemaNode(type, isOptional);
        assertNotNull(node);
        assertEquals(ObjectNode.class, node.getClass());
        assertEquals(type, node.get("type"));
        assertEquals(isOptional, node.get("required"));
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        //TODO implement test
    }

    @Test
    public void testIsDefaultSerializer() {
        //TODO implement test
    }

    @Test
    public void testFindConvertingContentSerializer() {
        //TODO implement test
    }

    @Test
    public void testFindPropertyFilter() {
        //TODO implement test
    }

    private class TestObject {

    }
}