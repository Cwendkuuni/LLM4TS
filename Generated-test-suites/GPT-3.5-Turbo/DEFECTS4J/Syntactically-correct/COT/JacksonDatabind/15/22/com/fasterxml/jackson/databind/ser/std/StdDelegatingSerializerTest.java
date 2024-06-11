
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import com.fasterxml.jackson.databind.util.Converter;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, ?> converter;

    @Mock
    private JavaType delegateType;

    @Mock
    private JsonSerializer<Object> delegateSerializer;

    @Mock
    private Object value;

    @Mock
    private JsonGenerator gen;

    @Mock
    private SerializerProvider provider;

    @Mock
    private TypeSerializer typeSer;

    @Mock
    private SerializerProvider prov;

    @Mock
    private Type typeHint;

    @Mock
    private JsonFormatVisitorWrapper visitor;

    @Test
    public void testSerialize() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serialize(value, gen, provider);

        verify(provider).defaultSerializeNull(gen);
        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);

        serializer.serializeWithType(value, gen, provider, typeSer);

        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void testIsEmpty() {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);

        assert serializer.isEmpty(prov, value);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint)).thenReturn(mock(JsonNode.class));

        serializer.getSchema(provider, typeHint);
    }

    @Test
    public void testGetSchemaWithOptional() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        when(((SchemaAware) delegateSerializer).getSchema(provider, typeHint, true)).thenReturn(mock(JsonNode.class));

        serializer.getSchema(provider, typeHint, true);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        StdDelegatingSerializer serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);

        serializer.acceptJsonFormatVisitor(visitor, delegateType);

        verify(delegateSerializer).acceptJsonFormatVisitor(visitor, delegateType);
    }
}
