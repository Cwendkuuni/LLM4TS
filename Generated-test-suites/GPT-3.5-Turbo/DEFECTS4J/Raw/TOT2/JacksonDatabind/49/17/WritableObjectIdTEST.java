import static org.junit.Assert.*;
import org.junit.Test;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(new ObjectIdGenerator());
    }

    @Test
    public void testWriteAsId() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter();

        // Test for id being null
        assertFalse(writableObjectId.writeAsId(gen, provider, writer));

        // Test for id not written and not always as id
        writableObjectId.generateId(new Object());
        assertFalse(writableObjectId.writeAsId(gen, provider, writer));

        // Test for id being written
        writableObjectId.idWritten = true;
        assertTrue(writableObjectId.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        Object object = new Object();

        // Test for generating id
        assertNotNull(writableObjectId.generateId(object));
    }

    @Test
    public void testWriteAsField() {
        JsonGenerator gen = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        ObjectIdWriter writer = new ObjectIdWriter();
        writableObjectId.generateId(new Object());

        // Test for writing field with id written
        writableObjectId.writeAsField(gen, provider, writer);

        // Test for writing field with null property name
        writer.propertyName = null;
        writableObjectId.writeAsField(gen, provider, writer);
    }
}