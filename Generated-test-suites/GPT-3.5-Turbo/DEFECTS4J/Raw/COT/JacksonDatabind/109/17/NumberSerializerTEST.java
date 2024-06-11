import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.*;

public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextualWithNullParameters() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        BeanProperty mockBeanProperty = mock(BeanProperty.class);

        numberSerializer.createContextual(null, mockBeanProperty);
        numberSerializer.createContextual(mockSerializerProvider, null);
    }

    @Test
    public void testCreateContextualWithDifferentJsonFormatValueShapes() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        BeanProperty mockBeanProperty = mock(BeanProperty.class);
        JsonFormat.Value mockJsonFormatValue = mock(JsonFormat.Value.class);
        when(mockJsonFormatValue.getShape()).thenReturn(JsonFormat.Shape.STRING);

        numberSerializer.createContextual(mockSerializerProvider, mockBeanProperty);
        numberSerializer.createContextual(mockSerializerProvider, mockBeanProperty);

        verify(mockBeanProperty, times(2)).getType();
        verify(mockSerializerProvider, times(2)).handlePrimaryContextualization(numberSerializer, mockBeanProperty);
    }

    @Test
    public void testSerializeWithDifferentNumberTypes() throws IOException {
        Number mockNumber = mock(Number.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(mockNumber, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(new BigDecimal(10), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(new BigInteger("10"), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10L, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10.0, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10.0f, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize((byte) 10, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize((short) 10, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(2)).writeNumber(any(BigDecimal.class));
        verify(mockJsonGenerator, times(2)).writeNumber(any(BigInteger.class));
        verify(mockJsonGenerator, times(1)).writeNumber(anyLong());
        verify(mockJsonGenerator, times(1)).writeNumber(anyDouble());
        verify(mockJsonGenerator, times(1)).writeNumber(anyFloat());
        verify(mockJsonGenerator, times(1)).writeNumber(anyInt());
        verify(mockJsonGenerator, times(1)).writeNumber(anyByte());
        verify(mockJsonGenerator, times(1)).writeNumber(anyShort());
    }

    @Test
    public void testSerializeWithNullValue() throws IOException {
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);

        numberSerializer.serialize(null, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(1)).writeNumber(anyString());
    }

    @Test
    public void testSerializeWithDifferentJsonGeneratorAndSerializerProvider() throws IOException {
        Number mockNumber = mock(Number.class);
        JsonGenerator mockJsonGenerator = mock(JsonGenerator.class);
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        IOException mockIOException = mock(IOException.class);

        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(any(BigDecimal.class));
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(any(BigInteger.class));
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyLong());
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyDouble());
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyFloat());
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyInt());
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyByte());
        doThrow(mockIOException).when(mockJsonGenerator).writeNumber(anyShort());

        numberSerializer.serialize(mockNumber, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(new BigDecimal(10), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(new BigInteger("10"), mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10L, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10.0, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10.0f, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize(10, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize((byte) 10, mockJsonGenerator, mockSerializerProvider);
        numberSerializer.serialize((short) 10, mockJsonGenerator, mockSerializerProvider);

        verify(mockJsonGenerator, times(2)).writeNumber(any(BigDecimal.class));
        verify(mockJsonGenerator, times(2)).writeNumber(any(BigInteger.class));
        verify(mockJsonGenerator, times(1)).writeNumber(anyLong());
        verify(mockJsonGenerator, times(1)).writeNumber(anyDouble());
        verify(mockJsonGenerator, times(1)).writeNumber(anyFloat());
        verify(mockJsonGenerator, times(1)).writeNumber(anyInt());
        verify(mockJsonGenerator, times(1)).writeNumber(anyByte());
        verify(mockJsonGenerator, times(1)).writeNumber(anyShort());
        verify(mockJsonGenerator, times(9)).writeNumber(anyString());

        verify(mockJsonGenerator, times(1)).writeNumber(any(BigDecimal.class));
        verify(mockJsonGenerator, times(1)).writeNumber(any(BigInteger.class));
        verify(mockJsonGenerator, times(1)).writeNumber(anyLong());
        verify(mockJsonGenerator, times(1)).writeNumber(anyDouble());
        verify(mockJsonGenerator, times(1)).writeNumber(anyFloat());
        verify(mockJsonGenerator, times(1)).writeNumber(anyInt());
        verify(mockJsonGenerator, times(1)).writeNumber(anyByte());
        verify(mockJsonGenerator, times(1)).writeNumber(anyShort());
    }

    @Test
    public void testGetSchemaWithDifferentSerializerProviderAndType() {
        SerializerProvider mockSerializerProvider = mock(SerializerProvider.class);
        Type mockType = mock(Type.class);

        numberSerializer.getSchema(mockSerializerProvider, mockType);

        verify(mockSerializerProvider, times(1)).getPath();
        verify(mockSerializerProvider, times(1)).getConfig();
    }

    @Test
    public void testAcceptJsonFormatVisitorWithDifferentJsonFormatVisitorWrapperAndJavaType() throws JsonMappingException {
        JsonFormatVisitorWrapper mockJsonFormatVisitorWrapper = mock(JsonFormatVisitorWrapper.class);
        JavaType mockJavaType = mock(JavaType.class);

        numberSerializer.acceptJsonFormatVisitor(mockJsonFormatVisitorWrapper, mockJavaType);

        verify(mockJsonFormatVisitorWrapper, times(1)).expectNumberFormat(any(JavaType.class));
        verify(mockJsonFormatVisitorWrapper, times(1)).getProvider();
        verify(mockJsonFormatVisitorWrapper, times(1)).setProvider(any(SerializerProvider.class));
    }

    // ...Other public helper methods
}