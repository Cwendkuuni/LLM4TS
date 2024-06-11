import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // create ObjectIdGenerator
        ObjectIdGenerator generator = new ObjectIdGenerator();

        // create WritableObjectId
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        try {
            // test when id is not null and idWritten is true
            writableObjectId.id = "1";
            writableObjectId.idWritten = true;
            assertTrue(writableObjectId.writeAsId(null, null, null));

            // test when id is not null but idWritten is false and w.alwaysAsId is true
            writableObjectId.idWritten = false;
            ObjectIdWriter w = new ObjectIdWriter();
            w.alwaysAsId = true;
            assertTrue(writableObjectId.writeAsId(null, null, w));

            // test when id is null
            writableObjectId.id = null;
            assertFalse(writableObjectId.writeAsId(null, null, null));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }

    @Test
    public void testGenerateId() {
        // create ObjectIdGenerator
        ObjectIdGenerator generator = new ObjectIdGenerator();

        // create WritableObjectId
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        // test generateId method
        Object id = writableObjectId.generateId("test");
        assertNotNull(id);
    }

    @Test
    public void testWriteAsField() {
        // create ObjectIdGenerator
        ObjectIdGenerator generator = new ObjectIdGenerator();

        // create WritableObjectId
        WritableObjectId writableObjectId = new WritableObjectId(generator);

        // create ObjectIdWriter
        ObjectIdWriter w = new ObjectIdWriter();
        w.propertyName = new SerializableString();

        try {
            // test when id is not null
            writableObjectId.id = "1";
            writableObjectId.writeAsField(null, null, w);
            assertTrue(writableObjectId.idWritten);

            // test when id is null
            writableObjectId.id = null;
            writableObjectId.writeAsField(null, null, w);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}