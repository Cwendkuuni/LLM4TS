
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdGenerator;
import com.fasterxml.jackson.databind.ser.impl.ObjectIdWriter;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

public class WritableObjectIdTest {
    private WritableObjectId testObject;
    
    @Before
    public void setUp() {
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        testObject = new WritableObjectId(generator);
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrue_shouldReturnTrue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        when(testObject.generateId(any())).thenReturn("id");
        testObject.idWritten = true;
        assertTrue(testObject.writeAsId(gen, provider, writer));
    }
    
    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalse_shouldReturnTrue() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        when(testObject.generateId(any())).thenReturn("id");
        assertFalse(testObject.writeAsId(gen, provider, writer));
    }
    
    @Test
    public void testWriteAsId_withNullIdAndIdWrittenTrue_shouldReturnFalse() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        testObject.id = null;
        testObject.idWritten = true;
        assertFalse(testObject.writeAsId(gen, provider, writer));
    }
    
    @Test
    public void testWriteAsId_withNullIdAndIdWrittenFalse_shouldReturnFalse() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        testObject.id = null;
        testObject.idWritten = false;
        assertFalse(testObject.writeAsId(gen, provider, writer));
    }
    
    @Test
    public void testGenerateId_withNonNullForPojo_shouldReturnId() {
        Object forPojo = new Object();
        ObjectIdGenerator<?> generator = mock(ObjectIdGenerator.class);
        when(generator.generateId(forPojo)).thenReturn("id");
        testObject.generator = generator;
        assertEquals("id", testObject.generateId(forPojo));
        assertEquals("id", testObject.id);
    }
    
    @Test
    public void testWriteAsField_withGenNotBeingAbleToWriteObjectId_shouldWriteFieldName() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        testObject.id = "id";
        SerializableString name = mock(SerializableString.class);
        when(writer.propertyName).thenReturn(name);
        testObject.writeAsField(gen, provider, writer);
        verify(gen).writeFieldName(name);
    }
    
    @Test
    public void testWriteAsField_withGenBeingAbleToWriteObjectId_shouldWriteObjectId() throws IOException {
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        ObjectIdWriter writer = mock(ObjectIdWriter.class);
        testObject.id = "id";
        when(gen.canWriteObjectId()).thenReturn(true);
        testObject.writeAsField(gen, provider, writer);
        verify(gen).writeObjectId("id");
    }
}
