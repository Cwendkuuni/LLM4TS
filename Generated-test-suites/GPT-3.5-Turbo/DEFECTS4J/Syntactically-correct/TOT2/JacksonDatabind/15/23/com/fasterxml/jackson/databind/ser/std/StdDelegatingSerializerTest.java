import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    @Test
    public void testSerialize() throws IOException {
        Converter<Object, String> converter = value -> value.toString();
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        serializer.serialize("test", gen, provider);

        Mockito.verify(gen, Mockito.times(1)).writeString("test");
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Converter<Object, Integer> converter = value -> Integer.parseInt(value.toString());
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);

        JsonGenerator gen = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        serializer.serializeWithType("42", gen, provider, null);

        Mockito.verify(gen, Mockito.times(1)).writeNumber(42);
    }

    // Add more test cases for other methods as per the requirements

}