import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializerProvider;
import com.fasterxml.jackson.databind.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ObjectIdWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectMapper objectMapper;
    private ObjectIdGenerator<?> objectIdGenerator;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setup() throws Exception {
        jsonGenerator = new ObjectMapper().getFactory().createGenerator(new StringWriter());
        serializerProvider = new ObjectMapper().getSerializationConfig().without(SerializationFeature.WRITE_NULL_MAP_VALUES);
        objectMapper = new ObjectMapper();
        objectIdGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object forPojo) {
                return "testId";
            }
        };
        objectIdWriter = new ObjectIdWriter();
    }

    @Test
    public void testWritableObjectIdConstruction() {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        assertTrue(writableObjectId instanceof WritableObjectId);
    }

    @Test
    public void testWriteAsIdMultipleTimes() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        jsonGenerator.close();

        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        jsonGenerator.close();

        writableObjectId.idWritten = false;
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        jsonGenerator.close();

        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        jsonGenerator.close();
    }

    @Test
    public void testWriteAsIdWithIdWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = "testId";
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
        jsonGenerator.close();
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdWithCanWriteObjectIdFalseAndPropertyNameNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));

        jsonGenerator.canWriteObjectId(false);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test(expected = IOException.class)
    public void testWriteAsIdWithCanWriteObjectIdFalseAndPropertyNameNotNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        jsonGenerator.writeStartObject();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        objectIdWriter.propertyName = new SerializableString("test");
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        jsonGenerator.canWriteObjectId(false);
        jsonGenerator.writeObjectId("testId");
    }

    @Test
    public void testGenerateIdWithNullId() {
        ObjectIdGenerator<?> objectIdGenerator = new ObjectIdGenerator() {
            @Override
            public Object generateId(Object forPojo) {
                return null;
            }
        };
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        assertNull(writableObjectId.generateId(new Object()));
    }

    @Test
    public void testWriteAsFieldWithIdWrittenTrue() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        objectIdWriter.propertyName = new SerializableString("test");
        WritableObjectId writableObjectId2 = new WritableObjectId(objectIdGenerator);
        writableObjectId2.generateId(new Object());
        writableObjectId2.idWritten = true;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter, writableObjectId2);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String result = objectMapper.readValue(jsonGenerator.getOutputBuffer().toString(), String.class);
        assertTrue(result.contains("\"test\":\"" + "testId" + "\""));
    }

    @Test
    public void testWriteAsFieldWithCanWriteObjectIdFalseAndPropertyNameNotNull() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);

        jsonGenerator.writeStartObject();
        jsonGenerator.writeFieldName("test");
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        objectIdWriter.propertyName = new SerializableString("test");
        jsonGenerator.canWriteObjectId(false);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String result = objectMapper.readValue(jsonGenerator.getOutputBuffer().toString(), String.class);
        assertTrue(result.contains("\"test\":null"));
    }

    @Test
    public void testWriteAsFieldWithIdWrittenFalse() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(objectIdGenerator);
        jsonGenerator.writeStartObject();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        objectIdWriter.propertyName = new SerializableString("test");
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
        jsonGenerator.writeEndObject();
        jsonGenerator.close();

        String result = objectMapper.readValue(jsonGenerator.getOutputBuffer().toString(), String.class);
        assertTrue(result.contains("\"test\":null"));
    }
}