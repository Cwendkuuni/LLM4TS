import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.config.DeserializationConfig;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.EnumSet;
import java.util.Locale;
import java.util.UUID;

/**
 * Unit tests for {@link StdKeyDeserializer}.
 */
public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        DeserializationConfig config = new DeserializationConfig();
        config.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
        objectMapper = new ObjectMapper(config);
    }

    /**
     * Test for boolean deserializer.
     */
    @Test
    public void testBooleanDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Boolean.class);
        Assert.assertEquals(Boolean.TRUE, serializer.deserializeKey("true", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Boolean.FALSE, serializer.deserializeKey("false", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for byte deserializer.
     */
    @Test
    public void testByteDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Byte.class);
        Assert.assertEquals((byte) 1, serializer.deserializeKey("1", new DeserializationContext(objectMapper)));
        Assert.assertEquals((byte) 127, serializer.deserializeKey("127", new DeserializationContext(objectMapper)));
        try {
            serializer.deserializeKey("256", new DeserializationContext(objectMapper));
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for short deserializer.
     */
    @Test
    public void testShortDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Short.class);
        Assert.assertEquals((short) 1, serializer.deserializeKey("1", new DeserializationContext(objectMapper)));
        Assert.assertEquals((short) 32767, serializer.deserializeKey("32767", new DeserializationContext(objectMapper)));
        try {
            serializer.deserializeKey("32768", new DeserializationContext(objectMapper));
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for char deserializer.
     */
    @Test
    public void testCharDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Character.class);
        Assert.assertEquals('a', serializer.deserializeKey("a", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for int deserializer.
     */
    @Test
    public void testIntDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Integer.class);
        Assert.assertEquals(1, serializer.deserializeKey("1", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Integer.MIN_VALUE, serializer.deserializeKey("-2147483648", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Integer.MAX_VALUE, serializer.deserializeKey("2147483647", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for long deserializer.
     */
    @Test
    public void testLongDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Long.class);
        Assert.assertEquals(1L, serializer.deserializeKey("1", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Long.MIN_VALUE, serializer.deserializeKey("-9223372036854775808", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Long.MAX_VALUE, serializer.deserializeKey("9223372036854775807", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for float deserializer.
     */
    @Test
    public void testFloatDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Float.class);
        Assert.assertEquals(1.0f, serializer.deserializeKey("1.0", new DeserializationContext(objectMapper)), 0.01f);
        Assert.assertEquals(Float.POSITIVE_INFINITY, serializer.deserializeKey("Infinity", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Float.NEGATIVE_INFINITY, serializer.deserializeKey("-Infinity", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Float.NaN, serializer.deserializeKey("NaN", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for double deserializer.
     */
    @Test
    public void testDoubleDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Double.class);
        Assert.assertEquals(1.0, serializer.deserializeKey("1.0", new DeserializationContext(objectMapper)), 0.01);
        Assert.assertEquals(Double.POSITIVE_INFINITY, serializer.deserializeKey("Infinity", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Double.NEGATIVE_INFINITY, serializer.deserializeKey("-Infinity", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Double.NaN, serializer.deserializeKey("NaN", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for locale deserializer.
     */
    @Test
    public void testLocaleDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Locale.class);
        Assert.assertEquals(Locale.ENGLISH, serializer.deserializeKey("en_US", new DeserializationContext(objectMapper)));
        Assert.assertEquals(Locale.JAPAN, serializer.deserializeKey("ja_JP", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for date deserializer.
     */
    @Test
    public void testDateDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Date.class);
        Assert.assertEquals(new Date(), serializer.deserializeKey("2023-03-20", new DeserializationContext(objectMapper)));
        Assert.assertEquals(new Date(1672492800000L), serializer.deserializeKey("2023-03-20 12:00:00Z", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for calendar deserializer.
     */
    @Test
    public void testCalendarDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(Calendar.class);
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.YEAR, 2023);
        calendar.set(Calendar.MONTH, Calendar.MARCH);
        calendar.set(Calendar.DAY_OF_MONTH, 20);

        Assert.assertEquals(calendar, serializer.deserializeKey("2023-03-20", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for UUID deserializer.
     */
    @Test
    public void testUuidDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(UUID.class);
        Assert.assertEquals(UUID.fromString("00000000-0000-0000-0000-000000000000"), serializer.deserializeKey("00000000-0000-0000-0000-000000000000", new DeserializationContext(objectMapper)));
        Assert.assertEquals(UUID.randomUUID(), serializer.deserializeKey("random", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for URI deserializer.
     */
    @Test
    public void testUriDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URI.class);
        Assert.assertEquals(new URI("http://example.com/"), serializer.deserializeKey("http://example.com", new DeserializationContext(objectMapper)));
        Assert.assertEquals(new URI("http://example.com/path?query=value#fragment"), serializer.deserializeKey("http://example.com/path?query=value#fragment", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for URL deserializer.
     */
    @Test
    public void testUrlDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(URL.class);
        Assert.assertEquals(new URL("http://example.com/"), serializer.deserializeKey("http://example.com", new DeserializationContext(objectMapper)));
    }

    /**
     * Test for enum deserializer.
     */
    @Test
    public void testEnumDeserializer() throws JsonMappingException, JsonProcessingException {
        EnumSet<TestEnum> enumSet = EnumSet.allOf(TestEnum.class);
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestEnum.class);

        for (TestEnum value : enumSet) {
            Assert.assertEquals(value, serializer.deserializeKey(value.getName(), new DeserializationContext(objectMapper)));
        }

        try {
            serializer.deserializeKey("unknown", new DeserializationContext(objectMapper));
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for enum deserializer with custom factory method.
     */
    @Test
    public void testEnumDeserializerWithFactoryMethod() throws JsonMappingException, JsonProcessingException {
        EnumSet<TestEnumWithFactory> enumSet = EnumSet.allOf(TestEnumWithFactory.class);
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(TestEnumWithFactory.class);

        for (TestEnumWithFactory value : enumSet) {
            Assert.assertEquals(value, serializer.deserializeKey(value.getName(), new DeserializationContext(objectMapper)));
        }

        try {
            serializer.deserializeKey("unknown", new DeserializationContext(objectMapper));
            Assert.fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // Expected
        }
    }

    /**
     * Test for custom deserializer.
     */
    @Test
    public void testCustomDeserializer() throws JsonMappingException, JsonProcessingException {
        StdKeyDeserializer serializer = StdKeyDeserializer.forType(CustomClass.class);
        Assert.assertEquals(new CustomClass("CustomClass"), serializer.deserializeKey("CustomClass", new DeserializationContext(objectMapper)));
    }
}

class TestEnum {
    public static final TestEnum ONE = new TestEnum("ONE");
    public static final TestEnum TWO = new TestEnum("TWO");
    public static final TestEnum THREE = new TestEnum("THREE");

    private final String name;

    TestEnum(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}

class TestEnumWithFactory {
    public static final TestEnumWithFactory ONE = new TestEnumWithFactory("ONE");
    public static final TestEnumWithFactory TWO = new TestEnumWithFactory("TWO");
    public static final TestEnumWithFactory THREE = new TestEnumWithFactory("THREE");

    private static final TestEnumWithFactory[] VALUES = new TestEnumWithFactory[]{ONE, TWO, THREE};

    private final String name;

    TestEnumWithFactory(String name) {
        this.name = name;
    }

    public static TestEnumWithFactory valueOf(String name) {
        for (TestEnumWithFactory value : VALUES) {
            if (value.name.equals(name)) {
                return value;
            }
        }
        throw new IllegalArgumentException();
    }

    public String getName() {
        return name;
    }
}

class CustomClass {
    private final String name;

    CustomClass(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}