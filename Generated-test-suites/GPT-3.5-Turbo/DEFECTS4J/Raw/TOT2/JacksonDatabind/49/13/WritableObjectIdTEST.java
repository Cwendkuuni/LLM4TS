import org.junit.Test;
import static org.junit.Assert.*;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() {
        WritableObjectId writableObjId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        // Edge case: id is null
        writableObjId.id = null;
        assertFalse(writableObjId.writeAsId(gen, provider, null));
        // Edge case: idWritten is false and alwaysAsId is false
        writableObjId.id = new Object();
        assertFalse(writableObjId.writeAsId(gen, provider, null));
        // Typical use case
        writableObjId.idWritten = true;
        assertTrue(writableObjId.writeAsId(gen, provider, null));
    }

    @Test
    public void testGenerateId() {
        WritableObjectId writableObjId = new WritableObjectId(null);
        Object forPojo = new Object();
        // Typical use case
        assertNotNull(writableObjId.generateId(forPojo));
    }

    @Test
    public void testWriteAsField() {
        WritableObjectId writableObjId = new WritableObjectId(null);
        JsonGenerator gen = null;
        SerializerProvider provider = null;
        // Edge case: name is null
        ObjectIdWriter w = new ObjectIdWriter(null, null, null);
        writableObjId.writeAsField(gen, provider, w);
        // Edge case: gen can write object id
        w.alwaysAsId = false;
        gen = new NativeJsonGenerator(null, null, null);
        writableObjId.writeAsField(gen, provider, w);
        // Typical use case
        w.alwaysAsId = true;
        gen = new NonNativeJsonGenerator(null, null, null);
        writableObjId.id = new Object();
        writableObjId.writeAsField(gen, provider, w);
    }
}