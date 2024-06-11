import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private Converter<Object, String> converter;
    private JavaType delegateType;
    private JsonSerializer<Object> delegateSerializer;
    private SerializerProvider provider;
    private StringWriter writer;
    private JsonGenerator generator;

    @Before
    public void setUp() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);
        provider = mock(SerializerProvider.class);
        writer = new StringWriter();
        generator = new JsonFactory().createGenerator(writer);

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_NullValue_DefaultSerializeNull() throws IOException {
        Object value = null;

        serializer.serialize(value, generator, provider);

        verify(provider).defaultSerializeNull(generator);
    }

    @Test
    public void testSerialize_NonNullValue_DelegateSerializerCalled() throws IOException {
        Object value = new Object();
        Object delegateValue = new Object();
        
        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serialize(value, generator, provider);

        verify(delegateSerializer).serialize(delegateValue, generator, provider);
    }

    @Test
    public void testSerializeWithType_NonNullValue_DelegateSerializerCalled() throws IOException {
        Object value = new Object();
        Object delegateValue = new Object();

        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, generator, provider, null);

        verify(delegateSerializer).serializeWithType(delegateValue, generator, provider, null);
    }

    @Test
    public void testIsEmpty_NonNullValue_DelegateSerializerCalled() {
        Object value = new Object();
        Object delegateValue = new Object();

        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(false);

        boolean isEmpty = serializer.isEmpty(provider, value);

        assertEquals(false, isEmpty);
    }

    @Test
    public void testGetSchema_DelegateSerializerImplementsSchemaAware_getSchemaCalled() {
        Object value = new Object();

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);

        serializer.getSchema(provider, value.getClass());

        verify(((SchemaAware) delegateSerializer)).getSchema(provider, value.getClass());
    }

    @Test
    public void testGetSchema_DelegateSerializerDoesNotImplementSchemaAware_DefaultImplementationCalled() {
        Object value = new Object();

        when(delegateSerializer instanceof SchemaAware).thenReturn(false);

        serializer.getSchema(provider, value.getClass());

        verify(provider).getSchema(serializer, value.getClass());
    }

    @Test
    public void testGetSchema_DelegateSerializerImplementsSchemaAwareAndIsOptional_getSchemaCalled() {
        Object value = new Object();

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);

        serializer.getSchema(provider, value.getClass(), true);

        verify(((SchemaAware) delegateSerializer)).getSchema(provider, value.getClass(), true);
    }
}