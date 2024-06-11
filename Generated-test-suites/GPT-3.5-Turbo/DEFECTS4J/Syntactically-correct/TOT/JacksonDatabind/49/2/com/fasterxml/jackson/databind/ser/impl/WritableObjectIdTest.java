import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;

    @Before
    public void setUp() {
        ObjectMapper objectMapper = new ObjectMapper();
        writableObjectId = new WritableObjectId(new ObjectIdGenerator<>(WritableObjectId.class));
        jsonGenerator = objectMapper.getFactory().createGenerator(System.out);
        serializerProvider = objectMapper.getSerializerProvider();
        objectIdWriter = new ObjectIdWriter();
    }

    @Test
    public void testWriteAsId() throws IOException {
        // Test case for when id is not null and idWritten is false and alwaysAsId is true
        objectIdWriter.setAlwaysAsId(true);
        writableObjectId.id = "123";
        boolean result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        Assert.assertTrue(result);

        // Test case for when id is not null and idWritten is true and alwaysAsId is false
        objectIdWriter.setAlwaysAsId(false);
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        Assert.assertTrue(result);

        // Test case for when id is null and idWritten is false
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        result = writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter);

        Assert.assertFalse(result);
    }

    @Test
    public void testGenerateId() {
        // Test case for when id is null
        Object forPojo = new Object();
        Object result = writableObjectId.generateId(forPojo);

        Assert.assertNotNull(result);
        Assert.assertEquals(result, writableObjectId.id);

        // Test case for when id is not null
        forPojo = new Object();
        writableObjectId.id = "123";
        result = writableObjectId.generateId(forPojo);

        Assert.assertEquals(result, writableObjectId.id);
    }

    @Test
    public void testWriteAsField() throws IOException {
        // Test case for when idWritten is false and gen.canWriteObjectId() is true
        jsonGenerator.enable(JsonGenerator.Feature.WRITE_OBJID_FIELDS);
        writableObjectId.id = "123";
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);

        // Test case for when idWritten is false and gen.canWriteObjectId() is false
        jsonGenerator.disable(JsonGenerator.Feature.WRITE_OBJID_FIELDS);
        writableObjectId.id = "123";
        objectIdWriter.propertyName = serializerProvider.findNameForSerialization(null);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }

}