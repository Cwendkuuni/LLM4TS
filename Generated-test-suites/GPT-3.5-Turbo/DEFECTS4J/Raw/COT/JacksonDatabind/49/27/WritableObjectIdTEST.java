import org.junit.Test;
import static org.junit.Assert.*;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        // Test case 1: id and idWritten are both false, alwaysAsId is false
        WritableObjectId objectId1 = new WritableObjectId(null);
        assertFalse(objectId1.writeAsId(gen, provider, writer));

        // Test case 2: id is null, idWritten is true, alwaysAsId is false
        WritableObjectId objectId2 = new WritableObjectId(null);
        objectId2.idWritten = true;
        assertFalse(objectId2.writeAsId(gen, provider, writer));

        // Test case 3: id is not null, idWritten is true, alwaysAsId is false
        WritableObjectId objectId3 = new WritableObjectId(null);
        objectId3.id = "123";
        objectId3.idWritten = true;
        assertTrue(objectId3.writeAsId(gen, provider, writer));

        // Test case 4: id is not null, idWritten is false, alwaysAsId is false
        WritableObjectId objectId4 = new WritableObjectId(null);
        objectId4.id = "123";
        assertFalse(objectId4.writeAsId(gen, provider, writer));
    }

    @Test
    public void testGenerateId() {
        // Test case 1: forPojo is null
        WritableObjectId objectId1 = new WritableObjectId(null);
        assertNull(objectId1.generateId(null));

        // Test case 2: forPojo is not null
        WritableObjectId objectId2 = new WritableObjectId(null);
        assertNotNull(objectId2.generateId(new Object()));
    }

    @Test
    public void testWriteAsField() {
        // Test case 1: id is null
        WritableObjectId objectId1 = new WritableObjectId(null);
        objectId1.writeAsField(gen, provider, writer);
        assertFalse(objectId1.idWritten);

        // Test case 2: id is not null
        WritableObjectId objectId2 = new WritableObjectId(null);
        objectId2.id = "123";
        objectId2.writeAsField(gen, provider, writer);
        assertTrue(objectId2.idWritten);
    }
}