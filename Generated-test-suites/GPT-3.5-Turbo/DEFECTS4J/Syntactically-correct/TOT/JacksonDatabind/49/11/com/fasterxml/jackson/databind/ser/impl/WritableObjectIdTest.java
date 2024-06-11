import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

public class WritableObjectIdTest {
    
    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> generator;
    private JsonGenerator gen;
    private SerializerProvider provider;
    private ObjectIdWriter w;
    
    @Before
    public void setup() {
        generator = mock(ObjectIdGenerator.class);
        writableObjectId = new WritableObjectId(generator);
        gen = mock(JsonGenerator.class);
        provider = mock(SerializerProvider.class);
        w = mock(ObjectIdWriter.class);
    }
    
    @Test
    public void testWriteAsId_WithIdNotNullAndIdWrittenTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        w.alwaysAsId = false;
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_WithIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        w.alwaysAsId = true;
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertTrue(result);
    }
    
    @Test
    public void testWriteAsId_WithIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalse_CallsSerializeMethod() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;
        
        writableObjectId.writeAsId(gen, provider, w);
        
        verify(w.serializer).serialize(writableObjectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsId_WithIdNull_ReturnsFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = false;
        w.alwaysAsId = false;
        
        boolean result = writableObjectId.writeAsId(gen, provider, w);
        
        assertFalse(result);
    }
    
    @Test
    public void testGenerateId_CallsGenerateIdMethodOfGenerator() {
        Object forPojo = new Object();
        
        writableObjectId.generateId(forPojo);
        
        verify(generator).generateId(forPojo);
    }
    
    @Test
    public void testGenerateId_SetsIdToGeneratedIdAndReturnsId() {
        Object forPojo = new Object();
        Object generatedId = new Object();
        
        when(generator.generateId(forPojo)).thenReturn(generatedId);
        
        Object result = writableObjectId.generateId(forPojo);
        
        assertEquals(generatedId, writableObjectId.id);
        assertEquals(generatedId, result);
    }
    
    @Test
    public void testWriteAsField_SetsIdWrittenToTrue() throws IOException {
        writableObjectId.idWritten = false;
        
        writableObjectId.writeAsField(gen, provider, w);
        
        assertTrue(writableObjectId.idWritten);
    }
    
    @Test
    public void testWriteAsField_WithCanWriteObjectIdTrue_CallsWriteObjectIdMethod() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        
        writableObjectId.writeAsField(gen, provider, w);
        
        verify(gen).writeObjectId(String.valueOf(writableObjectId.id));
    }
    
    @Test
    public void testWriteAsField_WithCanWriteObjectIdFalse_CallsWriteFieldNameAndSerializeMethods() throws IOException {
        Object id = "123";
        writableObjectId.id = id;
        writableObjectId.idWritten = true;
        when(gen.canWriteObjectId()).thenReturn(false);
        
        writableObjectId.writeAsField(gen, provider, w);
        
        verify(gen).writeFieldName(w.propertyName);
        verify(w.serializer).serialize(id, gen, provider);
    }

}