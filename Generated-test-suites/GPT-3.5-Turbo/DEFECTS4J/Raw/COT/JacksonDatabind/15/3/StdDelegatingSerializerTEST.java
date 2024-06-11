
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.ResolvableSerializer;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitable;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    private StdDelegatingSerializer serializer;
    private Converter<Object, ?> converter;
    private JavaType delegateType;
    private JsonSerializer<?> delegateSerializer;

    @SuppressWarnings("unchecked")
    @Before
    public void setUp() {
        converter = mock(Converter.class);
        delegateType = mock(JavaType.class);
        delegateSerializer = mock(JsonSerializer.class);

        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testResolve() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        ResolvableSerializer resolvableSerializer = mock(ResolvableSerializer.class);

        when(delegateSerializer instanceof ResolvableSerializer).thenReturn(true);
        when(delegateSerializer).thenReturn(resolvableSerializer);

        serializer.resolve(provider);

        verify(resolvableSerializer).resolve(provider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        BeanProperty property = mock(BeanProperty.class);
        ContextualSerializer contextualSerializer = mock(ContextualSerializer.class);

        when(delegateSerializer instanceof ContextualSerializer).thenReturn(true);
        when(provider.handleSecondaryContextualization(delegateSerializer, property)).thenReturn(contextualSerializer);

        serializer.createContextual(provider, property);

        verify(provider).handleSecondaryContextualization(delegateSerializer, property);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);

        when(serializer.convertValue(value)).thenReturn(value);

        serializer.serialize(value, gen, provider);

        verify(delegateSerializer).serialize(value, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();
        JsonGenerator gen = mock(JsonGenerator.class);
        SerializerProvider provider = mock(SerializerProvider.class);
        TypeSerializer typeSer = mock(TypeSerializer.class);

        when(serializer.convertValue(value)).thenReturn(value);

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(value, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();

        when(serializer.convertValue(value)).thenReturn(value);
        when(delegateSerializer.isEmpty(value)).thenReturn(true);

        serializer.isEmpty(value);

        verify(delegateSerializer).isEmpty(value);
    }

    @Test
    public void testIsEmptyWithProv() {
        Object value = new Object();
        SerializerProvider provider = mock(SerializerProvider.class);

        when(serializer.convertValue(value)).thenReturn(value);
        when(delegateSerializer.isEmpty(provider, value)).thenReturn(true);

        serializer.isEmpty(provider, value);

        verify(delegateSerializer).isEmpty(provider, value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(null);

        serializer.getSchema(provider, typeHint);

        verify((SchemaAware) delegateSerializer).getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        SerializerProvider provider = mock(SerializerProvider.class);
        Type typeHint = mock(Type.class);
        boolean isOptional = true;

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint, isOptional)).thenReturn(null);

        serializer.getSchema(provider, typeHint, isOptional);

        verify((SchemaAware) delegateSerializer).getSchema(provider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        serializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, typeHint);
    }

    @Test
    public void testConvertValue() {
        Object value = new Object();
        Object convertedValue = new Object();

        when(converter.convert(value)).thenReturn(convertedValue);

        Object result = serializer.convertValue(value);

        assertSame(convertedValue, result);
    }
}
