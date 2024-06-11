
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest {
    private final JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
    private final SerializerProvider serializerProvider = Mockito.mock(SerializerProvider.class);

    @Test
    public void testShortSerializer() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        serializer.serialize((short) 42, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber((short) 42);
    }
    
    @Test
    public void testIntegerSerializer() throws IOException {
        NumberSerializers.IntegerSerializer serializer = new NumberSerializers.IntegerSerializer(Integer.class);
        serializer.serialize(42, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(42);
    }
    
    @Test
    public void testIntLikeSerializer() throws IOException {
        NumberSerializers.IntLikeSerializer serializer = new NumberSerializers.IntLikeSerializer();
        serializer.serialize(42, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(42);
    }
    
    @Test
    public void testLongSerializer() throws IOException {
        NumberSerializers.LongSerializer serializer = new NumberSerializers.LongSerializer(Long.class);
        serializer.serialize(42L, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(42L);
    }
    
    @Test
    public void testFloatSerializer() throws IOException {
        NumberSerializers.FloatSerializer serializer = new NumberSerializers.FloatSerializer();
        serializer.serialize(4.2f, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(4.2f);
    }
    
    @Test
    public void testDoubleSerializer() throws IOException {
        NumberSerializers.DoubleSerializer serializer = new NumberSerializers.DoubleSerializer(Double.class);
        serializer.serialize(4.2, jsonGenerator, serializerProvider);
        Mockito.verify(jsonGenerator).writeNumber(4.2);
    }
    
    @Test
    public void testGetSchema() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        assertEquals(serializer.createSchemaNode("number", true), serializer.getSchema(serializerProvider, Short.class));
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        serializer.acceptJsonFormatVisitor(Mockito.mock(JsonFormatVisitorWrapper.class), Mockito.mock(JavaType.class));
        // Just verify that the method is called without throwing any exception
    }
    
    @Test
    public void testCreateContextual() throws IOException {
        NumberSerializers.ShortSerializer serializer = new NumberSerializers.ShortSerializer();
        Mockito.when(serializer.findFormatOverrides(serializerProvider, Mockito.mock(BeanProperty.class), Short.class)).thenReturn(null);
        assertEquals(serializer, serializer.createContextual(serializerProvider, Mockito.mock(BeanProperty.class)));
    }
}
