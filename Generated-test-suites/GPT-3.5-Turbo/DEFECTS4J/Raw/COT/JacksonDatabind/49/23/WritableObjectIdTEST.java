
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;

import static org.junit.Assert.*;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setUp() {
        writableObjectId = new WritableObjectId(null);
    }

    @Test
    public void testWriteAsIdNullId() {
        assertFalse(writableObjectId.writeAsId(null, null, null));
    }

    @Test
    public void testWriteAsIdNoIdWritten() {
        writableObjectId.id = "12345";
        assertFalse(writableObjectId.writeAsId(null, null, null));
    }

    @Test
    public void testWriteAsIdAlwaysAsId() {
        writableObjectId.id = "12345";
        writableObjectId.idWritten = true;
        ObjectIdWriter objectIdWriter = new ObjectIdWriter(null, null, null, null);
        objectIdWriter.alwaysAsId = true;
        assertTrue(writableObjectId.writeAsId(null, null, objectIdWriter));
    }

    @Test
    public void testWriteAsIdCanWriteObjectId() {
        writableObjectId.id = "12345";
        writableObjectId.idWritten = true;
        JsonGeneratorMock jsonGeneratorMock = new JsonGeneratorMock();
        assertTrue(writableObjectId.writeAsId(jsonGeneratorMock, null, null));
    }

    @Test
    public void testWriteAsFieldNoIdWritten() {
        writableObjectId.id = "12345";
        writableObjectId.idWritten = false;
        writableObjectId.writeAsField(null, null, null);
        assertFalse(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsFieldCanWriteObjectId() {
        writableObjectId.id = "12345";
        writableObjectId.idWritten = true;
        JsonGeneratorMock jsonGeneratorMock = new JsonGeneratorMock();
        writableObjectId.writeAsField(jsonGeneratorMock, null, null);
        assertTrue(jsonGeneratorMock.isWriteObjectIdCalled());
    }

    private static class JsonGeneratorMock extends JsonGenerator {

        private boolean writeObjectIdCalled;

        @Override
        public void writeObjectId(String id) throws IOException {
            writeObjectIdCalled = true;
        }

        @Override
        public boolean canWriteObjectId() {
            return true;
        }

        public boolean isWriteObjectIdCalled() {
            return writeObjectIdCalled;
        }

        // Implement other abstract methods from JsonGenerator

    }

}
