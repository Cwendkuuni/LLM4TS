
import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import java.io.IOException;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        assertFalse(writableObjectId.writeAsId(gen, provider, w));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        Object forPojo = new Object();
        assertNull(writableObjectId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() throws IOException {
        WritableObjectId writableObjectId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        ObjectIdWriter w = null;
        writableObjectId.writeAsField(gen, provider, w);
        assertTrue(writableObjectId.idWritten);
    }

}
