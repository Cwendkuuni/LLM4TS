import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import org.junit.Before;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

public class NumberSerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerializeInteger() throws JsonProcessingException {
        Integer number = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Integer"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Integer.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.IntegerSerializer"));
    }

    @Test
    public void testSerializeShort() throws JsonProcessingException {
        Short number = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Short"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Short.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.ShortSerializer"));
    }

    @Test
    public void testSerializeLong() throws JsonProcessingException {
        Long number = 123L;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Long"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Long.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.LongSerializer"));
    }

    @Test
    public void testSerializeFloat() throws JsonProcessingException {
        Float number = 123.45f;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Float"));
        assertThat(jsonNode.asText(), is("123.45"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Float.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.FloatSerializer"));
    }

    @Test
    public void testSerializeDouble() throws JsonProcessingException {
        Double number = 123.45;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Double"));
        assertThat(jsonNode.asText(), is("123.45"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Double.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.DoubleSerializer"));
    }

    @Test
    public void testSerializeIntLike() throws JsonProcessingException {
        Byte byteNumber = 123;
        JsonNode jsonNode = objectMapper.writeValueAsTree(byteNumber);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, byteNumber);

        assertThat(jsonNode.get("@class"), is("java.lang.Byte"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Byte.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.IntLikeSerializer"));

        Short shortNumber = 123;
        jsonNode = objectMapper.writeValueAsTree(shortNumber);
        gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, shortNumber);

        assertThat(jsonNode.get("@class"), is("java.lang.Short"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Short.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.IntLikeSerializer"));

        Integer integerNumber = 123;
        jsonNode = objectMapper.writeValueAsTree(integerNumber);
        gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, integerNumber);

        assertThat(jsonNode.get("@class"), is("java.lang.Integer"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Integer.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.IntegerSerializer"));
    }

    @Test
    public void testSerializeNumberTypes() throws JsonProcessingException {
        Number number = 123.45;
        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number);

        assertThat(jsonNode.get("@class"), is("java.lang.Double"));
        assertThat(jsonNode.asText(), is("123.45"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Number.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.FloatSerializer"));
    }

    @Test
    public void testContextualSerializationWithIntegerFormat() throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        SimpleModule module = new SimpleModule();
        module.addSerializer(JsonFormat.Value.class, new IntegerSerializerWithCustomFormat());
        objectMapper.registerModule(module);

        JsonFormat integerFormat = new JsonFormat();
        integerFormat.shape(JsonFormat.Shape.NUMBER);
        integerFormat.numberFormat(IntegerFormatShape.INT);

        Integer number = 123;
        String json = objectMapper.writerWithView(objectMapper.getSerializationConfig().getFactory().createJsonFormatView(JsonFormat.Feature.WRITE_NUMBERS_AS_STRINGS, false, null))
                .writeValueAsString(number);

        assertThat(json, is("123"));

        JsonNode jsonNode = objectMapper.writeValueAsTree(number);
        JsonGenerator gen = objectMapper.creatingWriter(ObjectMapper.DefaultTyping.NON_FINAL, JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        objectMapper.writeValue(gen, number, integerFormat);

        assertThat(jsonNode.get("@class"), is("java.lang.Integer"));
        assertThat(jsonNode.asText(), is("123"));
        assertThat(gen.getOutputFactory().getSerializerProvider().getSerializerForClass(Integer.class).getClass().getName(), is("com.fasterxml.jackson.databind.ser.std.IntegerSerializerWithCustomFormat"));
    }
}

class IntegerSerializerWithCustomFormat extends IntegerSerializer {
    IntegerSerializerWithCustomFormat() {
        super(Integer.class);
    }

    @Override
    public void acceptJsonFormatVisitor(JsonFormatVisitorWrapper visitor, JavaType typeHint) throws JsonMappingException {
        if (_isInt) {
            visitor.visitIntFormat(typeHint, _numberType);
        } else {
            super.acceptJsonFormatVisitor(visitor, typeHint);
        }
    }
}