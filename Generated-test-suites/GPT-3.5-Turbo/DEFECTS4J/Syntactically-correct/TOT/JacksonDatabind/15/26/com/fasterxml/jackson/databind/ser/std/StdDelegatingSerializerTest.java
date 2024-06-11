import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdDelegatingSerializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    private Converter<Object, Object> converter;

    @Mock
    private JavaType delegateType;

    @Mock
    private JsonSerializer<Object> delegateSerializer;

    @Mock
    private SerializerProvider provider;

    @Mock
    private Object value;

    @Mock
    private JsonGenerator gen;

    private StdDelegatingSerializer serializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new StdDelegatingSerializer(converter, delegateType, delegateSerializer);
    }

    @Test
    public void testSerialize_NullValue_DefaultSerializeNullCalled() throws IOException {
        when(converter.convert(null)).thenReturn(null);
        serializer.serialize(null, gen, provider);
        verify(provider).defaultSerializeNull(gen);
    }

    @Test
    public void testSerialize_NonNullValue_DelegateSerializeCalled() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.serialize(value, gen, provider);
        verify(delegateSerializer).serialize(delegateValue, gen, provider);
    }

    @Test
    public void testSerializeNullValue_IOExceptionThrown_ExceptionPropagated() throws IOException {
        when(converter.convert(null)).thenReturn(null);
        doThrow(IOException.class).when(provider).defaultSerializeNull(gen);
        try {
            serializer.serialize(null, gen, provider);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testSerialize_NonNullValue_IOExceptionThrown_ExceptionPropagated() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        doThrow(IOException.class).when(delegateSerializer).serialize(delegateValue, gen, provider);
        try {
            serializer.serialize(value, gen, provider);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testSerializeWithType_NullValue_DelegateSerializeWithTypeCalled() throws IOException {
        when(converter.convert(null)).thenReturn(null);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        serializer.serializeWithType(null, gen, provider, typeSer);
        verify(delegateSerializer).serializeWithType(null, gen, provider, typeSer);
    }

    @Test
    public void testSerializeWithType_NonNullValue_DelegateSerializeWithTypeCalled() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        serializer.serializeWithType(value, gen, provider, typeSer);
        verify(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
    }

    @Test
    public void testSerializeWithTypeNullValue_IOExceptionThrown_ExceptionPropagated() throws IOException {
        when(converter.convert(null)).thenReturn(null);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        doThrow(IOException.class).when(delegateSerializer).serializeWithType(null, gen, provider, typeSer);
        try {
            serializer.serializeWithType(null, gen, provider, typeSer);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testSerializeWithType_NonNullValue_IOExceptionThrown_ExceptionPropagated() throws IOException {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        TypeSerializer typeSer = mock(TypeSerializer.class);
        doThrow(IOException.class).when(delegateSerializer).serializeWithType(delegateValue, gen, provider, typeSer);
        try {
            serializer.serializeWithType(value, gen, provider, typeSer);
            Assert.fail("IOException expected but not thrown");
        } catch (IOException e) {
            // Expected IOException
        }
    }

    @Test
    public void testIsEmpty_NullValue_DelegateIsEmptyCalled() {
        when(converter.convert(null)).thenReturn(null);
        serializer.isEmpty(null);
        verify(delegateSerializer).isEmpty(null);
    }

    @Test
    public void testIsEmpty_NonNullValue_DelegateIsEmptyCalled() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        serializer.isEmpty(value);
        verify(delegateSerializer).isEmpty(delegateValue);
    }

    @Test
    public void testIsEmpty_NullValueAndEmptyDelegateValue_ReturnsTrue() {
        when(converter.convert(null)).thenReturn(null);
        when(delegateSerializer.isEmpty(null)).thenReturn(true);
        Assert.assertTrue(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_NonNullValueAndEmptyDelegateValue_ReturnsTrue() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(true);
        Assert.assertTrue(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_NullValueAndNonEmptyDelegateValue_ReturnsFalse() {
        when(converter.convert(null)).thenReturn(null);
        when(delegateSerializer.isEmpty(null)).thenReturn(false);
        Assert.assertFalse(serializer.isEmpty(null));
    }

    @Test
    public void testIsEmpty_NonNullValueAndNonEmptyDelegateValue_ReturnsFalse() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenReturn(false);
        Assert.assertFalse(serializer.isEmpty(value));
    }

    @Test
    public void testIsEmpty_NullValue_IOExceptionThrown_ExceptionPropagated() {
        when(converter.convert(null)).thenReturn(null);
        when(delegateSerializer.isEmpty(null)).thenThrow(RuntimeException.class);
        try {
            serializer.isEmpty(null);
            Assert.fail("RuntimeException expected but not thrown");
        } catch (RuntimeException e) {
            // Expected RuntimeException
        }
    }

    @Test
    public void testIsEmpty_NonNullValue_IOExceptionThrown_ExceptionPropagated() {
        Object delegateValue = new Object();
        when(converter.convert(value)).thenReturn(delegateValue);
        when(delegateSerializer.isEmpty(delegateValue)).thenThrow(RuntimeException.class);
        try {
            serializer.isEmpty(value);
            Assert.fail("RuntimeException expected but not thrown");
        } catch (RuntimeException e) {
            // Expected RuntimeException
        }
    }

    @Test
    public void testGetSchema_SchemaAwareDelegateSerializer_AttendGetSchemaCalled() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        serializer.getSchema(provider, String.class);
        verify((SchemaAware) delegateSerializer).getSchema(provider, String.class);
    }

    @Test
    public void testGetSchema_NotSchemaAwareDelegateSerializer_AttendGetSchemaCalled() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        serializer.getSchema(provider, String.class);
        verify(delegateSerializer).getSchema(provider, String.class);
    }

    @Test
    public void testGetSchema_SchemaAwareDelegateSerializer_AttendGetSchemaWithOptionalCalled() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(true);
        serializer.getSchema(provider, String.class, true);
        verify((SchemaAware) delegateSerializer).getSchema(provider, String.class, true);
    }

    @Test
    public void testGetSchema_NotSchemaAwareDelegateSerializer_AttendGetSchemaWithOptionalCalled() throws JsonMappingException {
        when(delegateSerializer instanceof SchemaAware).thenReturn(false);
        serializer.getSchema(provider, String.class, true);
        verify(delegateSerializer).getSchema(provider, String.class);
    }

    @Test
    public void testAcceptJsonFormatVisitor_DelegateSerializerCalled() throws JsonMappingException {
        JavaType typeHint = mock(JavaType.class);
        serializer.acceptJsonFormatVisitor(typeHint, null);
        verify(delegateSerializer).acceptJsonFormatVisitor(typeHint, null);
    }
}