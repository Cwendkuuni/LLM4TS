
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class WritableObjectIdTest {

    private WritableObjectId writableObjectId;

    @Before
    public void setup() {
        writableObjectId = new WritableObjectId(new ObjectIdGenerator<Object>() {
            @Override
            public ObjectIdGenerator<Object> forScope(Class<?> scope) {
                return null;
            }

            @Override
            public ObjectIdGenerator<Object> newForSerialization(Object context) {
                return null;
            }

            @Override
            public ObjectIdResolver resolverForDeserialization(Object context) {
                return null;
            }

            @Override
            public Object generateId(Object forPojo) {
                return null;
            }

            @Override
            public IdKey key(Object key) {
                return null;
            }

            @Override
            public boolean canUseFor(ObjectIdGenerator<?> gen) {
                return false;
            }
        });
    }

    @Test
    public void testWriteAsId_WhenIdAndIdWrittenIsNotNullAndAlwaysAsIdIsTrue() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockWriter.alwaysAsId()).thenReturn(true);

        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;

        // Act
        boolean result = writableObjectId.writeAsId(mockGen, mockProvider, mockWriter);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(mockGen).writeObjectRef(Mockito.anyString());
    }

    @Test
    public void testWriteAsId_WhenIdIsNull() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);

        writableObjectId.id = null;

        // Act
        boolean result = writableObjectId.writeAsId(mockGen, mockProvider, mockWriter);

        // Assert
        Assert.assertFalse(result);
        Mockito.verifyZeroInteractions(mockGen);
        Mockito.verifyZeroInteractions(mockProvider);
        Mockito.verifyZeroInteractions(mockWriter);
    }

    @Test
    public void testWriteAsId_WhenIdWrittenIsFalseAndAlwaysAsIdIsFalse() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockWriter.alwaysAsId()).thenReturn(false);

        writableObjectId.id = new Object();
        writableObjectId.idWritten = false;

        // Act
        boolean result = writableObjectId.writeAsId(mockGen, mockProvider, mockWriter);

        // Assert
        Assert.assertFalse(result);
        Mockito.verifyZeroInteractions(mockGen);
        Mockito.verifyZeroInteractions(mockProvider);
        Mockito.verify(mockWriter).alwaysAsId();
    }

    @Test
    public void testWriteAsId_WhenGenCanWriteObjectId() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockWriter.alwaysAsId()).thenReturn(true);
        Mockito.when(mockGen.canWriteObjectId()).thenReturn(true);
        
        writableObjectId.id = new Object();
        writableObjectId.idWritten = true;

        // Act
        boolean result = writableObjectId.writeAsId(mockGen, mockProvider, mockWriter);

        // Assert
        Assert.assertTrue(result);
        Mockito.verify(mockGen).writeObjectRef(Mockito.anyString());
        Mockito.verifyZeroInteractions(mockWriter);
        Mockito.verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testWriteAsField_WhenIdIsNull() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);

        writableObjectId.id = null;

        // Act
        writableObjectId.writeAsField(mockGen, mockProvider, mockWriter);

        // Assert
        Mockito.verifyZeroInteractions(mockGen);
        Mockito.verifyZeroInteractions(mockProvider);
        Mockito.verifyZeroInteractions(mockWriter);
    }

    @Test
    public void testWriteAsField_WhenGenCanWriteObjectId() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockGen.canWriteObjectId()).thenReturn(true);

        writableObjectId.id = new Object();

        // Act
        writableObjectId.writeAsField(mockGen, mockProvider, mockWriter);

        // Assert
        Mockito.verify(mockGen).writeObjectId(Mockito.anyString());
        Mockito.verifyZeroInteractions(mockWriter);
        Mockito.verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testWriteAsField_WhenNameIsNull() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        Mockito.when(mockWriter.propertyName()).thenReturn(null);

        writableObjectId.id = new Object();

        // Act
        writableObjectId.writeAsField(mockGen, mockProvider, mockWriter);

        // Assert
        Mockito.verifyZeroInteractions(mockGen);
        Mockito.verifyZeroInteractions(mockProvider);
    }

    @Test
    public void testWriteAsField_WhenNameIsNotNull() throws IOException {
        // Arrange
        JsonGenerator mockGen = Mockito.mock(JsonGenerator.class);
        SerializerProvider mockProvider = Mockito.mock(SerializerProvider.class);
        ObjectIdWriter mockWriter = Mockito.mock(ObjectIdWriter.class);
        SerializableString mockName = Mockito.mock(SerializableString.class);
        Mockito.when(mockWriter.propertyName()).thenReturn(mockName);

        writableObjectId.id = new Object();

        // Act
        writableObjectId.writeAsField(mockGen, mockProvider, mockWriter);

        // Assert
        Mockito.verify(mockGen).writeFieldName(mockName);
        Mockito.verify(mockWriter).serializer().serialize(Mockito.any(), Mockito.eq(mockGen), Mockito.eq(mockProvider));
    }
}
