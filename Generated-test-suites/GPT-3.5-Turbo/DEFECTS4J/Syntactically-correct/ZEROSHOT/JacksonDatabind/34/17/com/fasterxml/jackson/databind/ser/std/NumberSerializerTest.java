import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
public class NumberSerializerTest {

    private NumberSerializer numberSerializer;

    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerializeBigDecimal() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        BigDecimal bigDecimal = new BigDecimal("123.45");
        numberSerializer.serialize(bigDecimal, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeBigInteger() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        BigInteger bigInteger = new BigInteger("1234567890");
        numberSerializer.serialize(bigInteger, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeInteger() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Integer integer = 123;
        numberSerializer.serialize(integer, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeLong() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Long l = 123L;
        numberSerializer.serialize(l, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeDouble() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Double d = 123.45;
        numberSerializer.serialize(d, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeFloat() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Float f = 123.45f;
        numberSerializer.serialize(f, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeByte() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Byte b = 12;
        numberSerializer.serialize(b, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeShort() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Short s = 123;
        numberSerializer.serialize(s, mockGenerator, mockProvider);
    }

    @Test
    public void testSerializeOtherNumber() throws IOException {
        JsonGenerator mockGenerator = new ObjectMapper().getFactory().createGenerator(System.out);
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Number n = 123.45;
        numberSerializer.serialize(n, mockGenerator, mockProvider);
    }

    @Test
    public void testGetSchema() {
        SerializerProvider mockProvider = new ObjectMapper().getSerializerProviderInstance();
        Type typeHint = Number.class;
        assertNotNull(numberSerializer.getSchema(mockProvider, typeHint));
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper mockVisitor = new ObjectMapper().getSerializerProviderInstance().getVisitorWrapper();
        JavaType typeHint = new ObjectMapper().constructType(Number.class);
        numberSerializer.acceptJsonFormatVisitor(mockVisitor, typeHint);
    }
}