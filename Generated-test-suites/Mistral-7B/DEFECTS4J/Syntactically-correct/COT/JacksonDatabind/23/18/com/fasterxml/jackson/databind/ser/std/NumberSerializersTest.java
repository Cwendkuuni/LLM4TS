import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonGeneratorFactory;
import com.fasterxml.jackson.databind.jsontype.TypeSerializer;
import com.fasterxml.jackson.test.BaseTest;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;

import static org.junit.Assert.assertEquals;

public class NumberSerializersTest extends BaseTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        objectMapper.registerModule(new SimpleModule() {{
            addDeserializers(NumberSerializers.class);
        }});
    }

    @Test
    public void testIntegerSerializer_intValue() throws IOException {
        Integer number = 123;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serialize(number, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("123", actual);
    }

    @Test
    public void testIntegerSerializer_serializeWithType() throws IOException {
        Integer number = 123;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serializeWithType(number, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }

            @Override
            public TypeSerializer getTypingSerializer() {
                return null;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("123", actual);
    }

    @Test
    public void testIntegerSerializer_negativeValue() throws IOException {
        Integer number = -123;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serialize(number, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("-123", actual);
    }

    @Test
    public void testIntegerSerializer_longValue() throws IOException {
        Long longValue = 1234567890L;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serialize(longValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("1234567890", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIntegerSerializer_nonIntegerValue() throws IOException {
        String nonIntegerValue = "abc";
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.IntegerSerializer integerSerializer = new NumberSerializers.IntegerSerializer();
        integerSerializer.serialize(nonIntegerValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
    }

    @Test
    public void testLongSerializer_longValue() throws IOException {
        Long longValue = 1234567890L;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        longSerializer.serialize(longValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("1234567890", actual);
    }

    @Test
    public void testLongSerializer_negativeValue() throws IOException {
        Long longValue = -1234567890L;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.LongSerializer longSerializer = new NumberSerializers.LongSerializer();
        longSerializer.serialize(longValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("-1234567890", actual);
    }

    @Test
    public void testFloatSerializer_floatValue() throws IOException {
        Float floatValue = 123.45f;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        floatSerializer.serialize(floatValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("123.45", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFloatSerializer_nonFloatValue() throws IOException {
        String nonFloatValue = "def";
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.FloatSerializer floatSerializer = new NumberSerializers.FloatSerializer();
        floatSerializer.serialize(nonFloatValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
    }

    @Test
    public void testDoubleSerializer_doubleValue() throws IOException {
        Double doubleValue = 123.456d;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        doubleSerializer.serialize(doubleValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("123.456", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDoubleSerializer_nonDoubleValue() throws IOException {
        String nonDoubleValue = "ghi";
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.DoubleSerializer doubleSerializer = new NumberSerializers.DoubleSerializer();
        doubleSerializer.serialize(nonDoubleValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
    }

    @Test
    public void testShortSerializer_shortValue() throws IOException {
        Short shortValue = Short.MIN_VALUE;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        shortSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("-128", actual);
    }

    @Test
    public void testShortSerializer_positiveValue() throws IOException {
        Short shortValue = 123;
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        shortSerializer.serialize(shortValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
        String actual = jsonGenerator.getOutputBuffer().toString();
        assertEquals("123", actual);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testShortSerializer_nonShortValue() throws IOException {
        String nonShortValue = "jkl";
        JsonGenerator jsonGenerator = getTestJsonGenerator();
        NumberSerializers.ShortSerializer shortSerializer = new NumberSerializers.ShortSerializer();
        shortSerializer.serialize(nonShortValue, jsonGenerator, new SerializerProvider() {
            @Override
            public JsonGenerator getGenerator() {
                return jsonGenerator;
            }

            @Override
            public SerializerProvider getProvider() {
                return this;
            }
        });
    }

    private JsonGenerator getTestJsonGenerator() throws IOException {
        JsonGeneratorFactory jsonGeneratorFactory = objectMapper.getFactory().createGenerator(new ByteArrayOutputStream(), objectMapper.getOutputFactory());
        jsonGeneratorFactory.disable(JsonGenerator.Feature.AUTO_CLOSE_TARGET);
        return jsonGeneratorFactory.getGenerator();
    }
}