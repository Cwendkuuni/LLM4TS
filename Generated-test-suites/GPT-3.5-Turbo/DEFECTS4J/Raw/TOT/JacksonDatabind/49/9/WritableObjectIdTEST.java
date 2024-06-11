import org.junit.Assert;
import org.junit.Test;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Test
    public void testWriteAsId_WhenIdIsNullAndAlwaysAsIdIsFalse_ReturnsFalse() throws IOException {
        writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNullAndAlwaysAsIdIsTrue_ReturnsFalse() throws IOException {
        writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.alwaysAsId = true;
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        Assert.assertFalse(result);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsFalse_ReturnsTrue_And_CallsGenWriteObjectRef() throws IOException {
        writableObjectId = new WritableObjectId(ObjectIdGenerator);
        writableObjectId.id = 123;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        Assert.assertTrue(result);
        Assert.assertTrue(gen.writeObjectRefCalled);
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrue_ReturnsTrue_And_CallsSerializerSerialize() throws IOException {
        writableObjectId = new WritableObjectId(ObjectIdGenerator);
        writableObjectId.id = 456;
        writableObjectId.idWritten = true;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        Assert.assertTrue(result);
        Assert.assertTrue(w.serializer.serializeCalled);
    }

    @Test
    public void testGenerateId_ReturnsGeneratedId() {
        writableObjectId = new WritableObjectId(ObjectIdGenerator);
        Object forPojo = new Object();
        Object generatedId = writableObjectId.generateId(forPojo);
        Assert.assertEquals(ObjectIdGenerator.generateIdResult, generatedId);
    }

    @Test
    public void testWriteAsField_WhenIdWrittenIsFalse_CallsGenWriteObjectId() throws IOException {
        writableObjectId = new WritableObjectId(ObjectIdGenerator);
        writableObjectId.idWritten = false;
        writableObjectId.id = 789;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        writableObjectId.writeAsField(gen, provider, w);
        Assert.assertTrue(gen.writeObjectIdCalled);
    }

    @Test
    public void testWriteAsField_WhenGenCanWriteObjectIdIsFalse_CallsGenWriteFieldName_And_CallsSerializerSerialize() throws IOException {
        writableObjectId = new WritableObjectId(ObjectIdGenerator);
        writableObjectId.idWritten = true;
        writableObjectId.id = 789;
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter w = new ObjectIdWriter();
        w.propertyName = new SerializableString();
        writableObjectId.writeAsField(gen, provider, w);
        Assert.assertTrue(gen.writeFieldNameCalled);
        Assert.assertTrue(w.serializer.serializeCalled);
    }

}