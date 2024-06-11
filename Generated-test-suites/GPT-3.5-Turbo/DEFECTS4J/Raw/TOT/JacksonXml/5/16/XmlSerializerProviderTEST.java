import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class XmlSerializerProviderTest {

    @Test
    public void testSerializeValue_NullInput_SerializeXmlNullCalled() {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = null;
        XmlSerializerProvider provider = new XmlSerializerProvider(Mockito.mock(XmlRootNameLookup.class));

        // Act
        provider.serializeValue(gen, value);

        // Assert
        Mockito.verify(provider, Mockito.times(1))._serializeXmlNull(gen);
    }

    @Test
    public void testSerializeValue_NonNullInput_SerializeCalled() {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        XmlSerializerProvider provider = new XmlSerializerProvider(Mockito.mock(XmlRootNameLookup.class));
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        provider.addTypedSerializer(Object.class, serializer);

        // Act
        provider.serializeValue(gen, value);

        // Assert
        Mockito.verify(serializer, Mockito.times(1)).serialize(value, gen, provider);
    }

    @Test
    public void testSerializeValue_ArrayWithRootName_RootArrayWritten() {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object[] value = new Object[] { new Object(), new Object() };
        XmlRootNameLookup rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
        Mockito.when(rootNameLookup.findRootName(Mockito.any(), Mockito.any())).thenReturn(new QName("root"));
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        // Act
        provider.serializeValue(gen, value);

        // Assert
        Mockito.verify(gen, Mockito.times(1)).writeStartObject();
        Mockito.verify(gen, Mockito.times(1)).writeFieldName("item");
        Mockito.verify(gen, Mockito.times(1)).writeEndObject();
    }

    @Test
    public void testSerializeValue_ArrayWithoutRootName_RootArrayNotWritten() {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object[] value = new Object[] { new Object(), new Object() };
        XmlRootNameLookup rootNameLookup = Mockito.mock(XmlRootNameLookup.class);
        XmlSerializerProvider provider = new XmlSerializerProvider(rootNameLookup);

        // Act
        provider.serializeValue(gen, value);

        // Assert
        Mockito.verify(gen, Mockito.never()).writeStartObject();
        Mockito.verify(gen, Mockito.never()).writeFieldName(Mockito.anyString());
        Mockito.verify(gen, Mockito.never()).writeEndObject();
    }

    @Test(expected = IOException.class)
    public void testSerializeValue_ExceptionInSerializer_WrappedAsIOException() throws IOException {
        // Arrange
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        Object value = new Object();
        XmlSerializerProvider provider = new XmlSerializerProvider(Mockito.mock(XmlRootNameLookup.class));
        JsonSerializer<Object> serializer = Mockito.mock(JsonSerializer.class);
        Mockito.doThrow(new RuntimeException("Serialize Error")).when(serializer).serialize(Mockito.any(), Mockito.any(), Mockito.any());
        provider.addTypedSerializer(Object.class, serializer);

        // Act
        provider.serializeValue(gen, value);
    }
}