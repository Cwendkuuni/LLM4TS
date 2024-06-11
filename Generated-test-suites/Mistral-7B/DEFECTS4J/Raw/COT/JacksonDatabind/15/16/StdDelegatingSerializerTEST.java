import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer<Object> serializer;
    private Converter<Object,?> converter;
    private JsonSerializer<Object> delegateSerializer;

    @Before
    public void setUp() {
        converter = Mockito.mock(Converter.class);
        delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer<>(converter,null, delegateSerializer);
    }

    @Test
    public void testStdDelegatingSerializerConstructorWithoutArguments() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer<>();
        assertNotNull(serializer);
    }

    @Test
    public void testStdDelegatingSerializerConstructorWithConverterAndType() {
        StdDelegatingSerializer<String> serializer = new StdDelegatingSerializer<>(String.class, converter);
        assertNotNull(serializer);
        assertEquals(String.class, serializer.getClass());
    }

    @Test
    public void testStdDelegatingSerializerConstructorWithConverterConverterTypeAndSerializer() {
        JavaType delegateType = Mockito.mock(JavaType.class);
        serializer = new StdDelegatingSerializer<>(converter, delegateType, delegateSerializer);
        assertEquals(delegateType, serializer._delegateType);
        assertEquals(delegateSerializer, serializer._delegateSerializer);
    }

    @Test
    public void testGetConverter() {
        assertEquals(converter, serializer.getConverter());
    }

    @Test
    public void testGetDelegatee() {
        assertEquals(delegateSerializer, serializer.getDelegatee());
    }

    @Test
    public void testSerializeNullValue() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializationConfig().getDefaultSerializerProvider();
        ObjectNode result = mapper.writeValueAsTree(serializer.serialize(null, Mockito.mock(JsonGenerator.class), serializerProvider));
        assertNotNull(result);
        assertEquals("null", result.asText());
    }

    @Test
    public void testSerializeValue() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializationConfig().getDefaultSerializerProvider();
        ObjectNode result = mapper.writeValueAsTree(serializer.serialize(new Object(), Mockito.mock(JsonGenerator.class), serializerProvider));
        assertNotNull(result);
        assertEquals("null", result.get("value").asText());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testSerializeValueWithSameType() throws JsonGenerationException, JsonMappingException, IOException {
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider serializerProvider = mapper.getSerializationConfig().getDefaultSerializerProvider();
        serializer = new StdDelegatingSerializer<>(String.class, converter);
        serializer.serialize(new String("test"), Mockito.mock(JsonGenerator.class), serializerProvider);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        Mockito.when(delegateSerializer.resolve(serializerProvider)).thenReturn(delegateSerializer);
        assertSame(delegateSerializer, serializer.resolve(serializerProvider));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        Mockito.when(delegateSerializer.createContextual(serializerProvider, Mockito.any(BeanProperty.class)))
                .thenReturn(delegateSerializer);
        assertEquals(delegateSerializer, serializer.createContextual(serializerProvider, Mockito.mock(BeanProperty.class)));
    }

}