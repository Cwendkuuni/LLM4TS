
import org.junit.Test;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;

public class NumberSerializersTest {

    private NumberSerializers numberSerializers = new NumberSerializers();

    @Test
    public void testAddAll() {
        Map<String, JsonSerializer<?>> allDeserializers = new HashMap<>();
        numberSerializers.addAll(allDeserializers);
        // Check if all number serializers are added
        assert (allDeserializers.containsKey(Integer.class.getName()));
        assert (allDeserializers.containsKey(Integer.TYPE.getName()));
        assert (allDeserializers.containsKey(Long.class.getName()));
        assert (allDeserializers.containsKey(Long.TYPE.getName()));
        assert (allDeserializers.containsKey(Byte.class.getName()));
        assert (allDeserializers.containsKey(Byte.TYPE.getName()));
        assert (allDeserializers.containsKey(Short.class.getName()));
        assert (allDeserializers.containsKey(Short.TYPE.getName()));
        assert (allDeserializers.containsKey(Double.class.getName()));
        assert (allDeserializers.containsKey(Double.TYPE.getName()));
        assert (allDeserializers.containsKey(Float.class.getName()));
        assert (allDeserializers.containsKey(Float.TYPE.getName()));
    }

    @Test
    public void testSerializeShort() throws IOException {
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        Short value = 5;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        shortSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }

    @Test
    public void testSerializeInteger() throws IOException {
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer(Integer.class);
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        integerSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }

    @Test
    public void testSerializeIntLike() throws IOException {
        NumberSerializers.IntLikeSerializer intLikeSerializer = new NumberSerializers.IntLikeSerializer();
        Integer value = 123;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        intLikeSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }

    @Test
    public void testSerializeLong() throws IOException {
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer(Long.class);
        Long value = 12345L;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        longSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }

    @Test
    public void testSerializeFloat() throws IOException {
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        Float value = 1.23f;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        floatSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }

    @Test
    public void testSerializeDouble() throws IOException {
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer(Double.class);
        Double value = 1.2345678;
        JsonGenerator gen = new JsonFactory().createGenerator(System.out);
        gen.writeStartObject();
        doubleSerializer.serialize(value, gen, null);
        gen.writeEndObject();
        gen.close();
    }
}
