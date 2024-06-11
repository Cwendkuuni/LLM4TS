import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;

import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class WritableObjectIdTest {

    @Mock
    private ObjectIdGenerator<?> generator;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_WithIdAndIdWrittenTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = true;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithIdAndIdWrittenFalse_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithIdAndAlwaysAsIdTrue_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        writer.alwaysAsId = true;
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithIdAndAlwaysAsIdFalse_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_WithNullId_ReturnsFalse() throws IOException {
        writableObjectId.id = null;
        writableObjectId.idWritten = true;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WithIdNotWrittenAndAlwaysAsIdFalse_ReturnsFalse() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertFalse(result);
    }
    
    @Test
    public void testWriteAsId_WithIdNotWrittenAndAlwaysAsIdTrueAndGenCanWriteObjectId_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        writer.alwaysAsId = true;
        when(gen.canWriteObjectId()).thenReturn(true);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
        verify(gen).writeObjectRef("1");
    }
    
    @Test
    public void testWriteAsId_WithIdNotWrittenAndAlwaysAsIdTrueAndGenCannotWriteObjectId_ReturnsTrue() throws IOException {
        writableObjectId.id = "1";
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, true);
        writer.alwaysAsId = true;
        when(gen.canWriteObjectId()).thenReturn(false);
        boolean result = writableObjectId.writeAsId(gen, provider, writer);
        assertTrue(result);
        verify(writer.serializer).serialize("1", gen, provider);
    }

    @Test
    public void testGenerateId_GeneratesIdForGivenPojo() {
        Object pojo = new Object();
        writableObjectId.generateId(pojo);
        verify(generator).generateId(pojo);
        assertNotNull(writableObjectId.id);
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalse_WritesObjectIdAsString() throws IOException {
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        SerializableString name = mock(SerializableString.class);
        writer.propertyName = name;
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        assertTrue(writableObjectId.idWritten);
        verify(gen).writeFieldName(name);
        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }

    @Test
    public void testWriteAsField_WithIdWrittenFalseAndNullPropertyName_WritesObjectIdAsString() throws IOException {
        writableObjectId.idWritten = false;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        writer.propertyName = null;
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        assertTrue(writableObjectId.idWritten);
        verify(gen, never()).writeFieldName(any(SerializableString.class));
        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }
    
    @Test
    public void testWriteAsField_WithIdWrittenTrue_WritesObjectIdAsString() throws IOException {
        writableObjectId.idWritten = true;
        ObjectIdWriter writer = new ObjectIdWriter(null, null, null, false);
        SerializableString name = mock(SerializableString.class);
        writer.propertyName = name;
        
        writableObjectId.writeAsField(gen, provider, writer);
        
        verify(gen, never()).writeFieldName(anyString());
        verify(writer.serializer).serialize(writableObjectId.id, gen, provider);
    }

}