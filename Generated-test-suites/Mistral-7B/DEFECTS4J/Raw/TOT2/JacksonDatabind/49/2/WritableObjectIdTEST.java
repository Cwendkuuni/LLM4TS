package com.example;

import static org.junit.Assert.*;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.*;
import org.junit.*;

public class WritableObjectIdTest {

    private static final ObjectIdGenerator<Long> GENERATOR = new ObjectIdGenerator<Long>() {
        private long counter = 0;

        @Override
        public Long generateId(Object o) {
            return ++counter;
        }
    };

    private static final ObjectIdWriter OBJECT_ID_Writer = new ObjectIdWriter() {
        @Override
        public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeNumber(value);
        }

        @Override
        public String getPropertyName() {
            return "id";
        }
    };

    @Test
    public void testWritableObjectIdConstructor() {
        assertFalse(new WritableObjectId(null) instanceof WritableObjectId);
    }

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(GENERATOR);

        // Test for null id
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        assertFalse(writableObjectId.writeAsId(jsonGenerator, new SimpleSerializerProvider(), OBJECT_ID_Writer));

        // Test for JsonGenerator unable to write ObjectId
        jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        jsonGenerator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, new SimpleSerializerProvider(), OBJECT_ID_Writer));

        // Test for w.serializer being null
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(new StringWriter());
        ObjectIdWriter nullObjectIdWriter = null;
        assertFalse(writableObjectId.writeAsId(jsonGenerator2, new SimpleSerializerProvider(), nullObjectIdWriter));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(GENERATOR);

        // Test for null forPojo
        assertNull(writableObjectId.generateId(null));

        // Test for id already in use
        WritableObjectId writableObjectId1 = new WritableObjectId(GENERATOR);
        WritableObjectId writableObjectId2 = new WritableObjectId(GENERATOR);
        assertEquals(writableObjectId1.generateId(new Object()), writableObjectId2.generateId(new Object()));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(GENERATOR);

        // Test for JsonGenerator unable to write ObjectId
        JsonGenerator jsonGenerator = new JsonFactory().createGenerator(new StringWriter());
        jsonGenerator.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        try {
            writableObjectId.writeAsField(jsonGenerator, new SimpleSerializerProvider(), OBJECT_ID_Writer);
            fail();
        } catch (JsonGenerationException e) {
            // Expected
        }

        // Test for propertyName being null
        JsonGenerator jsonGenerator2 = new JsonFactory().createGenerator(new StringWriter());
        try {
            writableObjectId.writeAsField(jsonGenerator2, new SimpleSerializerProvider(), null);
            fail();
        } catch (JsonMappingException e) {
            // Expected
        }
    }
}