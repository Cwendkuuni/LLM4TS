
import org.junit.Test;
import org.junit.Assert;
import org.junit.Before;
import com.fasterxml.jackson.databind.ser.impl.WritableObjectId;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import java.io.IOException;

public class WritableObjectIdTest {
    private WritableObjectId writableObjectId;
    private JsonGenerator jsonGenerator;
    private SerializerProvider serializerProvider;
    private ObjectIdWriter objectIdWriter;
    
    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(null);
    }

    @Test
    public void testWriteAsIdWithNullId() throws IOException {
        Assert.assertFalse(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }
    
    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenFlagTrue() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;
        
        Assert.assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }
    
    @Test
    public void testWriteAsIdWithNonNullIdAndIdWrittenFlagFalse() throws IOException {
        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;
        objectIdWriter = new ObjectIdWriter(null, null, null, true, null);
        
        Assert.assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }
    
    @Test
    public void testWriteAsIdWithNonNullIdAndAlwaysAsIdTrueAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.id = new Object();
        objectIdWriter = new ObjectIdWriter(null, null, null, true, null);
        jsonGenerator = new JsonGenerator(null, 0, null, null);
        jsonGenerator.configure(JsonGenerator.Feature.WRITE_OBJECT_IDs, true);
        
        Assert.assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }
    
    @Test
    public void testWriteAsIdWithNonNullIdAndAlwaysAsIdTrueAndCanWriteObjectIdFalse() throws IOException {
        writableObjectId.id = new Object();
        objectIdWriter = new ObjectIdWriter(null, null, null, true, null);
        jsonGenerator = new JsonGenerator(null, 0, null, null);
        jsonGenerator.configure(JsonGenerator.Feature.WRITE_OBJECT_IDs, false);
        
        Assert.assertTrue(writableObjectId.writeAsId(jsonGenerator, serializerProvider, objectIdWriter));
    }
    
    @Test
    public void testWriteAsFieldWithIdWrittenTrueAndCanWriteObjectIdTrue() throws IOException {
        writableObjectId.idWritten = true;
        jsonGenerator = new JsonGenerator(null, 0, null, null);
        jsonGenerator.configure(JsonGenerator.Feature.WRITE_OBJECT_IDs, true);
        
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }
    
    @Test
    public void testWriteAsFieldWithIdWrittenTrueAndCanWriteObjectIdFalseAndNameNotNull() throws IOException {
        writableObjectId.idWritten = true;
        objectIdWriter = new ObjectIdWriter(null, null, null, false, null);
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }
    
    @Test
    public void testWriteAsFieldWithIdWrittenTrueAndCanWriteObjectIdFalseAndNameNull() throws IOException {
        writableObjectId.idWritten = true;
        objectIdWriter = new ObjectIdWriter(null, null, null, false, null);
        objectIdWriter.propertyName = null;
        writableObjectId.writeAsField(jsonGenerator, serializerProvider, objectIdWriter);
    }
}