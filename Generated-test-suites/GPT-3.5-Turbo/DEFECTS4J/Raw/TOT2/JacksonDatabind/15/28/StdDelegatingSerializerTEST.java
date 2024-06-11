import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;

    @Mock
    private Converter<Object, ?> converter;

    @Before
    public void setup() {
        serializer = new StdDelegatingSerializer(converter);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        Object value = "test";
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        JsonGenerator jsonGenerator = Mockito.mock(JsonGenerator.class);
        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        Mockito.when(converter.convert(Mockito.any())).thenReturn(value);

        serializer.serialize(value, jsonGenerator, provider);

        Mockito.verify(delegateSerializer, Mockito.times(1)).serialize(value, jsonGenerator, provider);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        Type typeHint = Object.class;

        Mockito.when(delegateSerializer instanceof SchemaAware).thenReturn(true);

        JsonNode schema = serializer.getSchema(provider, typeHint);
        assertTrue(schema instanceof JsonNode);
    }

    @Test
    public void testConvertValue() {
        Object value = "test";

        Mockito.when(converter.convert(value)).thenReturn(value);

        Object result = serializer.convertValue(value);

        assertEquals(value, result);
    }

    @Test
    public void testContextual() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        SerializerProvider provider = Mockito.mock(SerializerProvider.class);
        BeanProperty property = Mockito.mock(BeanProperty.class);

        Mockito.when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);

        JsonSerializer<?> result = serializer.createContextual(provider, property);

        assertEquals(delegateSerializer, result);
    }

    @Test
    public void testEmpty() {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        Object value = null;

        Mockito.when(delegateSerializer.isEmpty(value)).thenReturn(true);

        assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        JsonFormatVisitorWrapper visitor = Mockito.mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = JavaType.construct(Object.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        Mockito.verify(delegateSerializer, Mockito.times(1)).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithDelegateIllegalState() {
        serializer.withDelegate(converter, JavaType.construct(Object.class), Mockito.mock(JsonSerializer.class));
    }

    @Test
    public void testResolve() throws JsonMappingException {
        JsonSerializer<Object> delegateSerializer = Mockito.mock(JsonSerializer.class);
        serializer = new StdDelegatingSerializer(converter, JavaType.construct(Object.class), delegateSerializer);

        SerializerProvider provider = Mockito.mock(SerializerProvider.class);

        ((ResolvableSerializer) delegateSerializer).resolve(provider);

        serializer.resolve(provider);

        Mockito.verify(delegateSerializer, Mockito.times(1)).resolve(provider);
    }

}