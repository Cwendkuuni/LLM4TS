import static org.junit.Assert.*;
import org.junit.Test;
import org.mockito.Mockito;
import java.io.IOException;
import java.util.Map;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.BeanProperty;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;

public class NumberSerializersTest {

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        shortSerializer.serialize((short) 10, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer intSerializer = new IntegerSerializer(Integer.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        intSerializer.serialize(5, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(5);
    }

    @Test
    public void testIntLikeSerializer() throws IOException {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        intLikeSerializer.serialize(100L, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(100);
    }

    @Test
    public void testLongSerializer() throws IOException {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        longSerializer.serialize(500L, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(500L);
    }

    @Test
    public void testFloatSerializer() throws IOException {
        FloatSerializer floatSerializer = new FloatSerializer();
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        floatSerializer.serialize(10.5f, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(10.5f);
    }

    @Test
    public void testDoubleSerializer() throws IOException {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        doubleSerializer.serialize(15.75, gen, provider);
        Mockito.verify(gen, Mockito.times(1)).writeNumber(15.75);
    }

    // Additional test methods for edge cases and exception handling scenarios can be added here

}