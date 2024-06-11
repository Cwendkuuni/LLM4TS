import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import com.fasterxml.jackson.databind.ser.BeanSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

public class JsonValueSerializerTest {

    private JsonSerializer<Object> mockSerializer;
    private AnnotatedMethod mockMethod;
    private BeanProperty mockProperty;
    private SerializerProvider mockProvider;
    private JsonGenerator mockGenerator;
    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() {
        mockSerializer = mock(JsonSerializer.class);
        mockMethod = mock(AnnotatedMethod.class);
        mockProperty = mock(BeanProperty.class);
        mockProvider = mock(SerializerProvider.class);
        mockGenerator = mock(JsonGenerator.class);
        jsonValueSerializer = new JsonValueSerializer(mockMethod, mockSerializer);
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        Object bean = new Object();
        when(mockMethod.getValue(bean)).thenReturn(null);

        jsonValueSerializer.serialize(bean, mockGenerator, mockProvider);

        verify(mockGenerator).writeNull();
    }

    @Test
    public void testSerializeWithNonNullValue() throws IOException {
        Object bean = new Object();
        Object value = "test";
        when(mockMethod.getValue(bean)).thenReturn(value);

        jsonValueSerializer.serialize(bean, mockGenerator, mockProvider);

        verify(mockSerializer).serialize(value, mockGenerator, mockProvider);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        ContextualSerializer contextualSerializer = jsonValueSerializer.createContextual(mockProvider, mockProperty);

        assertEquals(jsonValueSerializer, contextualSerializer);
    }

    @Test
    public void testGetSchema() throws JsonMappingException {
        assertNull(jsonValueSerializer.getSchema(mockProvider, null));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitor = mock(JsonFormatVisitorWrapper.class);
        JavaType typeHint = mock(JavaType.class);

        jsonValueSerializer.acceptJsonFormatVisitor(mockVisitor, typeHint);

        verify(mockSerializer).acceptJsonFormatVisitor(mockVisitor, null);
    }
}