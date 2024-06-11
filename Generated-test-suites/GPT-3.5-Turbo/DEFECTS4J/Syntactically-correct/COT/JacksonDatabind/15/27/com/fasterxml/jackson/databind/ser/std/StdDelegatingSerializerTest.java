
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.mockito.Mockito.*;

public class StdDelegatingSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private SerializerProvider mockSerializerProvider;
    @Mock
    private TypeSerializer mockTypeSerializer;

    private StdDelegatingSerializer stdDelegatingSerializer;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
        stdDelegatingSerializer = new StdDelegatingSerializer(null);
    }

    @Test
    public void testSerialize() throws IOException {
        Object value = new Object();
        stdDelegatingSerializer.serialize(value, mockJsonGenerator, mockSerializerProvider);
        verify(mockSerializerProvider).defaultSerializeNull(mockJsonGenerator);
    }

    @Test
    public void testSerializeWithType() throws IOException {
        Object value = new Object();
        stdDelegatingSerializer.serializeWithType(value, mockJsonGenerator, mockSerializerProvider, mockTypeSerializer);
    }

    @Test
    public void testIsEmpty() {
        Object value = new Object();
        stdDelegatingSerializer.isEmpty(mockSerializerProvider, value);
        verify(stdDelegatingSerializer.getDelegatee()).isEmpty(value);
    }

    @Test
    public void testGetSchema() throws Exception {
        Type typeHint = Object.class;
        stdDelegatingSerializer.getSchema(mockSerializerProvider, typeHint);
        verify(stdDelegatingSerializer.getDelegatee()).getSchema(mockSerializerProvider, typeHint);
    }

    @Test
    public void testGetSchema_optional() throws Exception {
        Type typeHint = Object.class;
        boolean isOptional = false;
        stdDelegatingSerializer.getSchema(mockSerializerProvider, typeHint, isOptional);
        verify(stdDelegatingSerializer.getDelegatee()).getSchema(mockSerializerProvider, typeHint, isOptional);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JavaType typeHint = mock(JavaType.class);
        stdDelegatingSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, typeHint);
        verify(stdDelegatingSerializer.getDelegatee()).acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, typeHint);
    }

}
