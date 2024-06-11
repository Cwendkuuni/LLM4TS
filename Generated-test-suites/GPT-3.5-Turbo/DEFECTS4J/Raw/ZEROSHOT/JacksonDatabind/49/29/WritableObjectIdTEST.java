import com.fasterxml.jackson.annotation.ObjectIdGenerator;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.SerializableString;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.*;

public class WritableObjectIdTest {
    
    private WritableObjectId writableObjectId;
    
    @Before
    public void setUp() {
        ObjectIdGenerator<?> generator = new ObjectIdGenerator<>(Object.class);
        writableObjectId = new WritableObjectId(generator);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenTrue_returnsTrue() throws IOException {
        JsonGenerator mockGenerator = createMockGenerator(true);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);
        
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        
        assertTrue(result);
    }

    @Test
    public void testWriteAsId_withNonNullIdAndIdWrittenFalse_returnsFalse() throws IOException {
        JsonGenerator mockGenerator = createMockGenerator(false);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);
        
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        
        assertFalse(result);
    }

    @Test
    public void testWriteAsId_withNullId_returnsFalse() throws IOException {
        JsonGenerator mockGenerator = createMockGenerator(true);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);
        
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        
        assertFalse(result);
    }

    @Test
    public void testGenerateId_withNonNullForPojo_generatesId() {
        Object forPojo = new Object();
        
        Object result = writableObjectId.generateId(forPojo);
        
        assertNotNull(result);
        assertEquals(result, writableObjectId.id);
    }

    @Test
    public void testWriteAsField_withIdWrittenFalse_writesAsField() throws IOException {
        JsonGenerator mockGenerator = createMockGenerator(false);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_withGenCanWriteObjectIdTrue_writesObjectId() throws IOException {
        JsonGenerator mockGenerator = createMockGeneratorWithObjectId(true);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);
        
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        
        assertTrue(writableObjectId.idWritten);
    }

    @Test
    public void testWriteAsField_withGenCanWriteObjectIdFalse_writesFieldNameAndId() throws IOException {
        JsonGenerator mockGenerator = createMockGeneratorWithObjectId(false);
        SerializerProvider mockProvider = createMockSerializerProvider();
        ObjectIdWriter mockWriter = createMockObjectWriter(true);

        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        
        assertTrue(writableObjectId.idWritten);
    }
    
    private JsonGenerator createMockGenerator(boolean canWriteObjectId) {
        JsonGenerator mockGenerator = Mockito.mock(JsonGenerator.class);
        Mockito.when(mockGenerator.canWriteObjectId()).thenReturn(canWriteObjectId);
        return mockGenerator;
    }

    private JsonGenerator createMockGeneratorWithObjectId(boolean canWriteObjectId) {
        JsonGenerator mockGenerator = createMockGenerator(canWriteObjectId);
        Mockito.when(mockGenerator.writeObjectId(Mockito.anyString())).thenReturn(mockGenerator);
        return mockGenerator;
    }
    
    private SerializerProvider createMockSerializerProvider() {
        return Mockito.mock(SerializerProvider.class);
    }

    private ObjectIdWriter createMockObjectWriter(boolean alwaysAsId) {
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockWriter.alwaysAsId).thenReturn(alwaysAsId);
        Mockito.when(mockWriter.propertyName).thenReturn(Mockito.mock(SerializableString.class));
        return mockWriter;
    }
}