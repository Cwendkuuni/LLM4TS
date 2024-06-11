import com.fasterxml.jackson.core.BaseTest;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ser.StandardSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.Converter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.EnumSet;
import java.util.List;
import java.util.Map;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertInstanceOf;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertThrows;

public class StdKeyDeserializerTest extends BaseTest {

    private ObjectMapper mapper;
    private JsonNodeFactory nodeFactory;

    @BeforeClass
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        nodeFactory = mapper.getNodeFactory();
    }

    @Test
    public void testBooleanType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("true", ctxt);
        assertEquals(result, Boolean.TRUE);

        result = serializer.deserializeKey("false", ctxt);
        assertEquals(result, Boolean.FALSE);

        assertThrows(DeserializationFeature.Property.FAIL_ON_EMPTY_BEANS.getId(), () -> {
            serializer.deserializeKey("empty", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testByteType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0", ctxt);
        assertEquals(result, Byte.valueOf((byte) 0));

        result = serializer.deserializeKey("127", ctxt);
        assertEquals(result, Byte.valueOf((byte) 127));

        result = serializer.deserializeKey("128", ctxt);
        assertThrows(DeserializationFeature.Property.FAIL_ON_NUMBER_OVERFLOW.getId(), () -> {
            serializer.deserializeKey("128", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testShortType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0", ctxt);
        assertEquals(result, Short.valueOf((short) 0));

        result = serializer.deserializeKey("32767", ctxt);
        assertEquals(result, Short.valueOf((short) 32767));

        result = serializer.deserializeKey("32768", ctxt);
        assertThrows(DeserializationFeature.Property.FAIL_ON_NUMBER_OVERFLOW.getId(), () -> {
            serializer.deserializeKey("32768", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testCharType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("'a'", ctxt);
        assertEquals(result, Character.valueOf('a'));

        result = serializer.deserializeKey("'z'", ctxt);
        assertEquals(result, Character.valueOf('z'));

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_UTF8_SEQUENCE.getId(), () -> {
            serializer.deserializeKey("invalid", ctxt);
        });
    }

    @Test
    public void testIntType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0", ctxt);
        assertEquals(result, Integer.valueOf(0));

        result = serializer.deserializeKey("2147483647", ctxt);
        assertEquals(result, Integer.valueOf(2147483647));

        result = serializer.deserializeKey("2147483648", ctxt);
        assertThrows(DeserializationFeature.Property.FAIL_ON_NUMBER_OVERFLOW.getId(), () -> {
            serializer.deserializeKey("2147483648", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testLongType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0", ctxt);
        assertEquals(result, Long.valueOf(0L));

        result = serializer.deserializeKey("9223372036854775807", ctxt);
        assertEquals(result, Long.valueOf(9223372036854775807L));

        result = serializer.deserializeKey("9223372036854775808", ctxt);
        assertThrows(DeserializationFeature.Property.FAIL_ON_NUMBER_OVERFLOW.getId(), () -> {
            serializer.deserializeKey("9223372036854775808", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testFloatType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0.0", ctxt);
        assertEquals(result, Float.valueOf(0.0f));

        result = serializer.deserializeKey("3.14", ctxt);
        assertEquals(result, Float.valueOf(3.14f));

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_FLOATING_POINT.getId(), () -> {
            serializer.deserializeKey("invalid", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testDoubleType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("0.0", ctxt);
        assertEquals(result, Double.valueOf(0.0));

        result = serializer.deserializeKey("3.14", ctxt);
        assertEquals(result, Double.valueOf(3.14));

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_FLOATING_POINT.getId(), () -> {
            serializer.deserializeKey("invalid", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testLocaleType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);

        ObjectNode localeNode = nodeFactory.objectNode();
        localeNode.put("language", "en");
        localeNode.put("country", "US");

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey(localeNode.toString(), ctxt);
        assertEquals(result, Locale.US);

        result = serializer.deserializeKey("en_US", ctxt);
        assertEquals(result, Locale.US);

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_FORMAT.getId(), () -> {
            serializer.deserializeKey("en_XX", ctxt);
        });
    }

    @Test
    public void testUUIDType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5", ctxt);
        assertEquals(result, UUID.fromString("a1b2c3d4-e5f6-g7h8-i9j0-k1l2m3n4o5"));

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_FORMAT.getId(), () -> {
            serializer.deserializeKey("invalid", ctxt);
        });
    }

    @Test
    public void testURIType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("http://www.example.com", ctxt);
        assertEquals(result, new URI("http://www.example.com"));

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_INVALID_FORMAT.getId(), () -> {
            serializer.deserializeKey("invalid", ctxt);
        });
    }

    @Test
    public void testURLType() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("http://www.example.com", ctxt);
        assertEquals(result, new URL("http://www.example.com"));

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(MalformedURLException.class, () -> {
            serializer.deserializeKey("invalid", ctxt);
        });
    }

    @Test
    public void testEnumType() throws IOException {
        EnumSet<Colors> colors = EnumSet.allOf(Colors.class);

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Colors.class);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey(Colors.RED.name(), ctxt);
        assertEquals(result, Colors.RED);

        result = serializer.deserializeKey("RED", ctxt);
        assertEquals(result, Colors.RED);

        assertThrows(DeserializationFeature.Property.FAIL_ON_ENUM_ABSENT.getId(), () -> {
            serializer.deserializeKey("INVALID", ctxt);
        });

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    @Test
    public void testClassType() throws IOException {
        Class<?> clazz = String[].class;

        StdKeyDeserializer serializer = StdKeyDeserializer.forType(clazz);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("[Ljava.lang.String;", ctxt);
        assertEquals(result, (Object) new String[0]);

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });

        assertThrows(ClassNotFoundException.class, () -> {
            serializer.deserializeKey("invalid", ctxt);
        });
    }

    @Test
    public void testCustomDeserializer() throws IOException {
        ExternalKeyDeserializer externalDeserializer = new ExternalKeyDeserializer();
        StdKeyDeserializer serializer = new StdKeyDeserializer(-1, ExternalKeyDeserializer.class, externalDeserializer);

        DeserializationContext ctxt = null;
        Object result = serializer.deserializeKey("external", ctxt);
        assertInstanceOf(String.class, result.getClass());
        assertEquals(result, "External");

        assertThrows(DeserializationFeature.Property.FAIL_ON_NULL_FOR_NON_NULLABLE_TYPES.getId(), () -> {
            serializer.deserializeKey(null, ctxt);
        });
    }

    private static class ExternalKeyDeserializer extends KeyDeserializer {
        @Override
        public Object deserializeKey(String key, DeserializationContext ctxt) throws IOException {
            return "External";
        }
    }

    @Test
    public void testNullKey() throws IOException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);

        DeserializationContext ctxt = null;
        assertNull(serializer.deserializeKey(null, ctxt));
    }
}