import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        writableObjectId = new WritableObjectId(new ObjectIdGenerator<>());
    }

    @Test
    public void testWriteAsId_WhenIdIsNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse_ThenReturnFalse() {
        assertFalse(writableObjectId.writeAsId(null, null, null));
    }

    @Test
    public void testWriteAsId_WhenIdIsNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrue_ThenReturnFalse() {
        assertFalse(writableObjectId.writeAsId(null, null, new ObjectIdWriter(true, null, null, null, null)));
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsFalse_ThenReturnFalse() {
        writableObjectId.id = "id";
        assertFalse(writableObjectId.writeAsId(null, null, null));
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsTrueAndAlwaysAsIdIsFalse_ThenReturnTrue() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(null, null, null));
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrueAndCanWriteObjectIdIsTrue_ThenWriteObjectIdAndReturnTrue() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = false;
        JsonGeneratorJsonGenerator gen = new JsonGeneratorJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(true, null, null, null, new Serializer());
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testWriteAsId_WhenIdIsNotNullAndIdWrittenIsFalseAndAlwaysAsIdIsTrueAndCanWriteObjectIdIsFalse_ThenSerializeIdAndReturnTrue() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = false;
        JsonGeneratorJsonGenerator gen = new JsonGeneratorJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(true, null, null, null, new Serializer());
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        Object forPojo = new Object();
        assertNotNull(writableObjectId.generateId(forPojo));
        assertEquals(forPojo, writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WhenIdIsNotNullAndIdWrittenIsFalseAndCanWriteObjectIdIsTrue_ThenWriteObjectId() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = false;
        JsonGeneratorJsonGenerator gen = new JsonGeneratorJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(true, null, null, null, new Serializer());
        writableObjectId.writeAsField(gen, provider, writer);
    }

    @Test
    public void testWriteAsField_WhenIdIsNotNullAndIdWrittenIsTrueAndCanWriteObjectIdIsTrue_ThenWriteObjectId() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = true;
        JsonGeneratorJsonGenerator gen = new JsonGeneratorJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(true, null, null, null, new Serializer());
        writableObjectId.writeAsField(gen, provider, writer);
    }

    @Test
    public void testWriteAsField_WhenIdIsNotNullAndIdWrittenIsFalseAndCanWriteObjectIdIsFalse_ThenWriteFieldNameAndSerializeId() {
        writableObjectId.id = "id";
        writableObjectId.idWritten = false;
        JsonGeneratorJsonGenerator gen = new JsonGeneratorJsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter(false, new SerializableStringImpl(), null, null, new Serializer());
        writableObjectId.writeAsField(gen, provider, writer);
    }
}