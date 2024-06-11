import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Test
    public void testWriteAsId_PositiveCase() throws IOException {
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = "1";
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        Assert.assertTrue(result);
    }

    @Test
    public void testWriteAsId_InvalidId() throws IOException {
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = null;
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);
        Assert.assertFalse(result);
    }

    @Test
    public void testGenerateId_PositiveCase() {
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        Object forPojo = new Object();
        Object result = writableObjectId.generateId(forPojo);
        Assert.assertNotNull(result);
    }

    @Test
    public void testWriteAsField_PositiveCase() throws IOException {
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = "1";
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }

    @Test
    public void testWriteAsField_NullPropertyName() throws IOException {
        ObjectIdGenerator objectIdGenerator = new ObjectIdGenerator();
        writableObjectId = new WritableObjectId(objectIdGenerator);
        writableObjectId.id = "1";
        JsonGenerator jsonGenerator = new JsonGenerator();
        SerializerProvider serializerProvider = new SerializerProvider();
        ObjectIdWriter objectIdWriter = new ObjectIdWriter();
        objectIdWriter.propertyName = null;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }
}