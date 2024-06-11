import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private JsonGenerator mockJsonGenerator;
    @Mock
    private ObjectNode mockObjectNode;

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        JsonSerializer<?> result = numberSerializer.createContextual(mockSerializerProvider, mockBeanProperty);

        assertNull(result);
    }

    @Test
    public void testSerialize() throws IOException {
        BigDecimal bigDecimal = new BigDecimal(1234);
        BigInteger bigInteger = new BigInteger("5678");
        Number number = 9876;
        Long l = 54321L;
        Double d = 12345.678;
        Float f = 9876.543F;
        Integer i = -1234;
        Byte b = 127;
        Short s = -32768;

        numberSerializer.serialize(bigDecimal, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(bigDecimal);

        numberSerializer.serialize(bigInteger, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(bigInteger);

        numberSerializer.serialize(l, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(l);

        numberSerializer.serialize(d, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(d);

        numberSerializer.serialize(f, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(f);

        numberSerializer.serialize(i, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(i);

        numberSerializer.serialize(b, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(b);

        numberSerializer.serialize(s, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(s);

        numberSerializer.serialize(number, mockJsonGenerator, null);
        Mockito.verify(mockJsonGenerator).writeNumber(number.toString());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        Object result = numberSerializer.getSchema(mockSerializerProvider, null);

        assertTrue(result instanceof JsonNode);
        assertEquals("number", ((JsonNode) result).asText());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(mockVisitorWrapper, mockJavaType);

        verify(mockVisitorWrapper).expectNumberFormat(mockJavaType);
    }
}