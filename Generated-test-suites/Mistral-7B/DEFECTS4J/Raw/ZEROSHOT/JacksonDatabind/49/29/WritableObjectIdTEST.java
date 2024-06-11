package com.fasterxml.jackson.databind.ser.impl;

import static org.junit.Assert.*;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.IBinder;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.NameTransformer;

/**
 * Test class for WritableObjectId class.
 */
public class WritableObjectIdTest {

    @Test
    public void testWritableObjectId() throws JsonProcessingException, IOException {
        ObjectIdGenerator<Object> idGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object forPojo) {
                return "id_" + forPojo.hashCode();
            }
        };

        WritableObjectId writableObjectId = new WritableObjectId(idGenerator);

        // Test generateId method
        Object id = writableObjectId.generateId(new Object() {});
        assertEquals("id_123456", id);

        // Test writeAsId method with writeObjectId support
        JsonGenerator jsonGenerator = createMockJsonGenerator();
        SerializerProvider serializerProvider = createMockSerializerProvider();
        ObjectIdWriter objectIdWriter = createMockObjectIdWriter();
        objectIdWriter.alwaysAsId = false;
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(result);
        assertEquals("id_123456", jsonGenerator.getOutputBuffer().toString());

        // Test writeAsId method without writeObjectId support
        jsonGenerator = createMockJsonGeneratorWithoutObjectIdSupport();
        result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        assertFalse(result);

        // Test writeAsField method
        jsonGenerator = createMockJsonGenerator();
        serializerProvider = createMockSerializerProvider();
        objectIdWriter = createMockObjectIdWriter();
        objectIdWriter.propertyName = new SerializableString("testField");
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertEquals("testField", jsonGenerator.getCurrentName());
        assertEquals("id_123456", jsonGenerator.getOutputBuffer().toString());

        // Test writeAsField method with writeObjectId support
        jsonGenerator = createMockJsonGeneratorWithObjectIdSupport();
        result = writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        assertTrue(result);
        assertEquals("testField", jsonGenerator.getCurrentName());
        assertEquals("id_123456", jsonGenerator.getOutputBuffer().toString());
    }

    private JsonGenerator createMockJsonGenerator() {
        return createMock(JsonGenerator.class);
    }

    private JsonGenerator createMockJsonGeneratorWithoutObjectIdSupport() {
        return createMock(JsonGenerator.class, "canWriteObjectId() returns false");
    }

    private JsonGenerator createMockJsonGeneratorWithObjectIdSupport() {
        return createMock(JsonGenerator.class, "canWriteObjectId() returns true");
    }

    private SerializerProvider createMockSerializerProvider() {
        return createMock(SerializerProvider.class);
    }

    private ObjectIdWriter createMockObjectIdWriter() {
        return createMock(ObjectIdWriter.class);
    }

    private ObjectMapper createObjectMapper() {
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.setDefaultPropertyInclusion(java.lang.annotation.ElementType.ANNOTATION_TYPE,
                java.lang.annotation.RetentionPolicy.RUNTIME);
        objectMapper.setDefaultTyping(TypeIdResolver.Std.DEFAULT_TYPING);
        return objectMapper;
    }

    private ObjectWriter createObjectWriter() throws IOException {
        ObjectMapper objectMapper = createObjectMapper();
        ObjectWriter objectWriter = objectMapper.writer();
        return objectWriter;
    }

    @SuppressWarnings("unchecked")
    private <T> T createMock(Class<T> clazz) {
        return (T) org.mockito.Mockito.mock(clazz);
    }

    @SuppressWarnings("unchecked")
    private <T> T createMock(Class<T> clazz, String expectation) {
        return (T) org.mockito.Mockito.mock(clazz, org.mockito.Mockito.withSettings().extraInterfaces(IBinder.class)
                .defaultAnswer(invocation -> {
                    throw new RuntimeException(expectation);
                }));
    }

    private StdSerializer<Object> createMockSerializer() {
        return new StdSerializer<Object>(Object.class) {
            @Override
            public void serialize(Object value, JsonGenerator gen, SerializerProvider provider) throws IOException {
                gen.writeString(value.toString());
            }
        };
    }

    private NameTransformer createNameTransformer() {
        return new NameTransformer() {
            @Override
            public SerializableString transform(String name, javax.xml.bind.annotation.XmlAccessType accessType,
                    javax.xml.bind.annotation.XmlType qname) {
                return new SerializableString("testField");
            }

            @Override
            public SerializableString transform(String name, javax.xml.bind.annotation.XmlElementDecl decl,
                    javax.xml.bind.annotation.XmlJavaTypeAdapter adapter) {
                return new SerializableString("testField");
            }
        };
    }

    private ObjectIdWriter createMockObjectIdWriterWithPropertyName() {
        ObjectIdWriter objectIdWriter = createMock(ObjectIdWriter.class);
        ObjectMapper objectMapper = createObjectMapper();
        objectIdWriter.propertyName = objectMapper.getFactory()
                .getSerializerProvider()
                .getAllSerializerProviders()
                .get(0)
                .getSerializer(Object.class);
        return objectIdWriter;
    }
}

class ObjectIdWriter {
    public boolean alwaysAsId;
    public Serializer<Object> serializer;
}