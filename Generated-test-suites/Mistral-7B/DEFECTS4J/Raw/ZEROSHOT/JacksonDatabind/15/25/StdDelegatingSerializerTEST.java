import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsonschema.SchemaAware;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<Object> delegateSerializer;
    private StdDelegatingSerializer delegatingSerializer;

    @Before
    public void setUp() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);

        delegatingSerializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testEmptyConstructor() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(null);
        Assert.assertNotNull(serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
    }

    @Test
    public void testConstructorWithConverter() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter);
        Assert.assertNull(serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
    }

    @Test
    public void testConstructorWithClassAndConverter() {
        Class<String> cls = String.class;
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(cls, converter);
        Assert.assertEquals(cls, serializer.getClass());
        Assert.assertNull(serializer.getDelegateSerializer());
        Assert.assertNull(serializer.getDelegateType());
    }

    @Test
    public void testConstructorWithConverterDelegateTypeAndDelegateSerializer() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
        Assert.assertEquals(delegateType, serializer.getType());
        Assert.assertEquals(delegateSerializer, serializer.getDelegateSerializer());
    }

    @Test
    public void testResolve() throws JsonMappingException {
        when(delegateSerializer.resolve(any(SerializerProvider.class))).thenReturn(delegateSerializer);
        delegatingSerializer.resolve(mock(SerializerProvider.class));
        verify(delegateSerializer).resolve(mock(SerializerProvider.class));
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        when(delegateSerializer.createContextual(any(SerializerProvider.class), any(BeanProperty.class)))
                .thenReturn(delegateSerializer);
        JsonSerializer<?> returnedSerializer = delegatingSerializer.createContextual(mock(SerializerProvider.class), mock(BeanProperty.class));
        Assert.assertEquals(delegateSerializer, returnedSerializer);
        verify(delegateSerializer).createContextual(any(SerializerProvider.class), any(BeanProperty.class));
    }

    @Test
    public void testConvertValue() {
        Object input = new Object();
        when(converter.convert(input)).thenReturn(new Object());
        Object output = delegatingSerializer.convertValue(input);
        Assert.assertEquals(new Object(), output);
        verify(converter).convert(input);
    }

    @Test
    public void testSerialize() throws JsonGenerationException, IOException {
        Object input = new Object();
        Object delegateValue = new Object();
        when(converter.convert(input)).thenReturn(delegateValue);
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();

        delegatingSerializer.serialize(input, gen, provider);

        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws JsonGenerationException, IOException {
        Object input = new Object();
        Object delegateValue = new Object();
        when(converter.convert(input)).thenReturn(delegateValue);
        JsonGenerator gen = mock(JsonGenerator.class);
        ObjectMapper mapper = new ObjectMapper();
        SerializerProvider provider = mapper.getSerializerProvider();
        TypeSerializer typeSerializ = mock(TypeSerializer.class);

        delegatingSerializer.serializeWithType(input, gen, provider, typeSerializ);

        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSerializ);
    }

    @Test
    public void testIsEmpty() {
        Object input = new Object();
        boolean expected = false;
        when(delegateSerializer.isEmpty(any(Object.class))).thenReturn(expected);
        Assert.assertEquals(expected, delegatingSerializer.isEmpty(input));
        verify(delegateSerializer).isEmpty(input);
    }

    @Test
    public void testIsEmptyWithProvider() {
        Object input = new Object();
        boolean expected = false;
        when(delegateSerializer.isEmpty(any(SerializerProvider.class), any(Object.class))).thenReturn(expected);
        Assert.assertEquals(expected, delegatingSerializer.isEmpty(input, mock(SerializerProvider.class)));
        verify(delegateSerializer).isEmpty(input, mock(SerializerProvider.class));
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        when(delegateSerializer.getSchema(any(SerializerProvider.class), any(Type.class))).thenReturn(null);
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        ObjectMapper mapper = new ObjectMapper();
        Object schema = delegatingSerializer.getSchema(mapper.getSerializerProvider(), Object.class);
        Assert.assertNull(schema);
        verify(delegateSerializer).getSchema(any(SerializerProvider.class), eq(Object.class));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        delegatingSerializer.acceptJsonFormatVisitor(visitor, null);
        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, null);
    }
}