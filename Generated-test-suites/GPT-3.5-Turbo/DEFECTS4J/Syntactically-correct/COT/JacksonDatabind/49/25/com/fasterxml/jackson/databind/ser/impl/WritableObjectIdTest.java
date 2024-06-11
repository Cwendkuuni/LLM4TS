import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

import java.io.IOException;

import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {

    @Test
    public void testWriteAsId() throws IOException {
        // Setup
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        
        // Test
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        // Verify
        assertTrue(result);
        verify(gen).writeObjectRef("123");
    }
    
    @Test
    public void testGenerateId() {
        // Setup
        Object forPojo = new Object();
        
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        
        // Test
        Object result = writableObjectId.generateId(forPojo);
        
        // Verify
        assertEquals(result, writableObjectId.id);
    }
    
    @Test
    public void testWriteAsField() throws IOException {
        // Setup
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter w = mock(ObjectIdWriter.class);
        
        WritableObjectId writableObjectId = new WritableObjectId(mock(ObjectIdGenerator.class));
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        
        // Test
        writableObjectId.writeAsField(gen, provider, w);
        
        // Verify
        assertTrue(writableObjectId.idWritten);
        verify(gen).writeFieldName(Mockito.any(SerializableString.class));
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
    }
}