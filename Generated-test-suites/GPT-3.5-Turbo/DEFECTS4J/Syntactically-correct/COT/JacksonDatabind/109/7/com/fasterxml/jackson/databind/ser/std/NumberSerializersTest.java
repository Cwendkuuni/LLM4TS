

import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.beanProperty.BeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

import com.fasterxml.jackson.databind.ser.ContextualSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.*;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;

public class NumberSerializersTest {

    //Test all public methods in NumberSerializers class

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);
        //assert statements
    }

    @Test
    public void testShortSerializer() {
        ShortSerializer shortSerializer = new ShortSerializer();
        //assert statements
    }

    @Test
    public void testIntegerSerializer() {
        IntegerSerializer integerSerializer = new IntegerSerializer(Integer.class);
        //assert statements
    }

    @Test
    public void testIntLikeSerializer() {
        IntLikeSerializer intLikeSerializer = new IntLikeSerializer();
        //assert statements
    }

    @Test
    public void testLongSerializer() {
        LongSerializer longSerializer = new LongSerializer(Long.class);
        //assert statements
    }

    @Test
    public void testFloatSerializer() {
        FloatSerializer floatSerializer = new FloatSerializer();
        //assert statements
    }

    @Test
    public void testDoubleSerializer() {
        DoubleSerializer doubleSerializer = new DoubleSerializer(Double.class);
        //assert statements
    }
}
