
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.IntegerSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.LongSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.ShortSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.FloatSerializer;
import com.fasterxml.jackson.databind.ser.std.NumberSerializers.DoubleSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.beanProperty.BeanProperty;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonFormatVisitorWrapper;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonIntegerFormatVisitor;
import com.fasterxml.jackson.databind.jsonFormatVisitors.JsonNumberFormatVisitor;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.databind.ser.ContextualSerializer;

class NumberSerializersTest {

    @SuppressWarnings("unchecked")
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        NumberSerializers.addAll(allDeserializers);

        JsonSerializer<Integer> intS = (JsonSerializer<Integer>) allDeserializers.get(Integer.class.getName());
        assertNotNull(intS);
        assertThat(intS, instanceOf(IntegerSerializer.class));

        JsonSerializer<Integer> intTypeS = (JsonSerializer<Integer>) allDeserializers.get(Integer.TYPE.getName());
        assertNotNull(intTypeS);
        assertThat(intTypeS, instanceOf(IntegerSerializer.class));

        JsonSerializer<Long> longS = (JsonSerializer<Long>) allDeserializers.get(Long.class.getName());
        assertNotNull(longS);
        assertThat(longS, instanceOf(LongSerializer.class));

        JsonSerializer<Long> longTypeS = (JsonSerializer<Long>) allDeserializers.get(Long.TYPE.getName());
        assertNotNull(longTypeS);
        assertThat(longTypeS, instanceOf(LongSerializer.class));

        JsonSerializer<Byte> byteS = (JsonSerializer<Byte>) allDeserializers.get(Byte.class.getName());
        assertNotNull(byteS);
        assertThat(byteS, instanceOf(IntLikeSerializer.class));

        JsonSerializer<Byte> byteTypeS = (JsonSerializer<Byte>) allDeserializers.get(Byte.TYPE.getName());
        assertNotNull(byteTypeS);
        assertThat(byteTypeS, instanceOf(IntLikeSerializer.class));

        JsonSerializer<Short> shortS = (JsonSerializer<Short>) allDeserializers.get(Short.class.getName());
        assertNotNull(shortS);
        assertThat(shortS, instanceOf(ShortSerializer.class));

        JsonSerializer<Short> shortTypeS = (JsonSerializer<Short>) allDeserializers.get(Short.TYPE.getName());
        assertNotNull(shortTypeS);
        assertThat(shortTypeS, instanceOf(ShortSerializer.class));

        JsonSerializer<Float> floatS = (JsonSerializer<Float>) allDeserializers.get(Float.class.getName());
        assertNotNull(floatS);
        assertThat(floatS, instanceOf(FloatSerializer.class));

        JsonSerializer<Float> floatTypeS = (JsonSerializer<Float>) allDeserializers.get(Float.TYPE.getName());
        assertNotNull(floatTypeS);
        assertThat(floatTypeS, instanceOf(FloatSerializer.class));

        JsonSerializer<Double> doubleS = (JsonSerializer<Double>) allDeserializers.get(Double.class.getName());
        assertNotNull(doubleS);
        assertThat(doubleS, instanceOf(DoubleSerializer.class));

        JsonSerializer<Double> doubleTypeS = (JsonSerializer<Double>) allDeserializers.get(Double.TYPE.getName());
        assertNotNull(doubleTypeS);
        assertThat(doubleTypeS, instanceOf(DoubleSerializer.class));
    }

}
