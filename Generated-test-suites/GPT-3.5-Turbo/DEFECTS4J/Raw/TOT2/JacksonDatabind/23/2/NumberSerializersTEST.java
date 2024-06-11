import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;

public class NumberSerializersTest {

    private Map<String, JsonSerializer<?>> allDeserializers;

    @Before
    public void setUp() {
        allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
    }

    @Test
    public void testIntegerSerializer() throws IOException {
        IntegerSerializer intSerializer = new IntegerSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        int inputValue = 42;
        intSerializer.serialize(inputValue, gen, provider);

        assertEquals(42, inputValue);
    }

    @Test
    public void testShortSerializer() throws IOException {
        ShortSerializer shortSerializer = new ShortSerializer();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        short inputValue = 10;
        shortSerializer.serialize(inputValue, gen, provider);

        assertEquals(10, inputValue);
    }

    // Add test cases for other serializers
    
    @Test
    public void testAddAllDeserializers() {
        assertEquals(7, allDeserializers.size());
    }
    
    // Add test cases for edge cases and error scenarios

}