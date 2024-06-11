import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.StdScalarSerializer;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;

@JacksonStdImpl
@SuppressWarnings("serial")
public class NumberSerializerTest {

    private NumberSerializer serializer;

    @Before
    public void setup() {
        serializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testCreateContextual() throws JsonMappingException {
        SerializerProvider provider = new SerializerProvider();
        BeanProperty property = new BeanProperty();
        JsonSerializer<?> result = serializer.createContextual(provider, property);
        Assert.assertEquals(serializer, result);
    }

    @Test
    public void testSerialize() throws IOException {
        JsonGenerator generator = new JsonGenerator();
        SerializerProvider provider = new SerializerProvider();
        BigDecimal bigDecimal = new BigDecimal("10.5");
        BigInteger bigInteger = new BigInteger("10");
        Long longValue = 10L;
        Double doubleValue = 10.5;
        Float floatValue = 10.5f;
        Integer intValue = 10;

        serializer.serialize(bigDecimal, generator, provider);
        Assert.assertEquals("10.5", generator.getResult());

        serializer.serialize(bigInteger, generator, provider);
        Assert.assertEquals("10", generator.getResult());

        serializer.serialize(longValue, generator, provider);
        Assert.assertEquals("10", generator.getResult());

        serializer.serialize(doubleValue, generator, provider);
        Assert.assertEquals("10.5", generator.getResult());

        serializer.serialize(floatValue, generator, provider);
        Assert.assertEquals("10.5", generator.getResult());

        serializer.serialize(intValue, generator, provider);
        Assert.assertEquals("10", generator.getResult());
    }

    @Test
    public void testGetSchema() {
        SerializerProvider provider = new SerializerProvider();
        Type typeHint = new Type();
        JsonNode result = serializer.getSchema(provider, typeHint);
        Assert.assertEquals("number", result.type());
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        JsonFormatVisitorWrapper visitor = new JsonFormatVisitorWrapper();
        JavaType typeHint = new JavaType();
        serializer.acceptJsonFormatVisitor(visitor, typeHint);
        // Assert something
    }
}