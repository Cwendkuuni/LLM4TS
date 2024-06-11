import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import static org.mockito.Mockito.verify;

public class WritableObjectIdTest {
    private WritableObjectId writableObjectId;
    private ObjectIdGenerator<?> mockGenerator;
    private JsonGenerator mockGenerator;
    private SerializerProvider mockProvider;
    private ObjectIdWriter mockWriter;

    @Before
    public void setUp() {
        mockGenerator = Mockito.mock(JsonGenerator.class);
        mockProvider = Mockito.mock(SerializerProvider.class);
        mockWriter = Mockito.mock(ObjectIdWriter.class);
        mockGenerator = Mockito.mock(JsonGenerator.class);
        writableObjectId = new WritableObjectId(mockGenerator);
    }

    @Test
    public void testWriteAsId_withIdNotNullAndIdWrittenTrue_shouldWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockGenerator).writeObjectRef("123");
    }

    @Test
    public void testWriteAsId_withIdNotNullAndIdWrittenFalseAndAlwaysAsIdTrue_shouldWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        Mockito.when(mockWriter.alwaysAsId()).thenReturn(true);
        writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockGenerator).writeObjectRef("123");
    }

    @Test
    public void testWriteAsId_withIdNotNullAndIdWrittenFalseAndAlwaysAsIdFalse_shouldSerializeId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        Mockito.when(mockWriter.alwaysAsId()).thenReturn(false);
        writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockWriter.serializer).serialize("123", mockGenerator, mockProvider);
    }

    @Test
    public void testWriteAsId_withIdNull_shouldReturnFalse() throws IOException {
        writableObjectId.id = null;
        boolean result = writableObjectId.writeAsId(mockGenerator, mockProvider, mockWriter);
        Assert.assertFalse(result);
    }

    @Test
    public void testGenerateId_shouldGenerateIdUsingGenerator() {
        Object mockPojo = Mockito.mock(Object.class);
        writableObjectId.generateId(mockPojo);
        Mockito.verify(mockGenerator).generateId(mockPojo);
    }

    @Test
    public void testWriteAsField_withIdWrittenTrue_shouldWriteObjectId() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = true;
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockGenerator).writeObjectId("123");
    }

    @Test
    public void testWriteAsField_withIdWrittenFalseAndSerializedIdPropertyNameNotNull_shouldWriteIdField() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        Mockito.when(mockWriter.propertyName()).thenReturn(Mockito.mock(SerializableString.class));
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockGenerator).writeFieldName(Mockito.any(SerializableString.class));
        Mockito.verify(mockWriter.serializer).serialize("123", mockGenerator, mockProvider);
    }

    @Test
    public void testWriteAsField_withIdWrittenFalseAndSerializedIdPropertyNameNull_shouldNotWriteIdField() throws IOException {
        writableObjectId.id = "123";
        writableObjectId.idWritten = false;
        Mockito.when(mockWriter.propertyName()).thenReturn(null);
        writableObjectId.writeAsField(mockGenerator, mockProvider, mockWriter);
        Mockito.verify(mockGenerator, Mockito.never()).writeFieldName(Mockito.any(SerializableString.class));
        Mockito.verify(mockWriter.serializer, Mockito.never()).serialize(Mockito.any(), Mockito.any(), Mockito.any());
    }
}