import com.fasterxml.jackson.core.Base64Variants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.version.Version;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.KeyDeserializer;
import com.fasterxml.jackson.databind.deser.std.StdKeyDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.introspect.AnnotatedConstructor;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.testng.annotations.*;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.*;

public class StdKeyDeserializerTest {

    private ObjectMapper objectMapper;

    @BeforeClass
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testBasicTypeDeserialization() throws IOException {
        String json = "{\"key\":\"true\"}";
        Boolean expected = true;

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Boolean.class);
        Object deserialized = keyDeserializer.deserializeKey("true", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);

        json = "{\"key\":\"false\"}";
        expected = false;
        deserialized = keyDeserializer.deserializeKey("false", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);

        json = "{\"key\":1}";
        Integer expectedInt = 1;
        deserialized = keyDeserializer.deserializeKey("1", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expectedInt);

        json = "{\"key\":\"255\"}";
        Byte key = (Byte) keyDeserializer.deserializeKey("255", objectMapper.getDeserializationContext());

        assertEquals(key, (byte) 255);
    }

    @Test
    public void testCustomDeserializerUsage() throws IOException {
        String json = "{\"key\":\"Europe\"}";
        Locale expected = Locale.Europe;

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        Object deserialized = keyDeserializer.deserializeKey("Europe", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testEnumDeserialization() throws IOException {
        String json = "{\"key\":\"MONDAY\"}";
        DayOfWeek expected = DayOfWeek.MONDAY;

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        reader.findAndRegisterValues(DayOfWeek.class);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);
        Object deserialized = keyDeserializer.deserializeKey("MONDAY", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testCustomEnumDeserialization() throws IOException {
        String json = "{\"key\":\"RED\"}";
        Color expected = Color.RED;

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        reader.findAndRegisterValues(Color.class);

        KeyDeserializer keyDeserializer = new EnumKD(objectMapper.getAnnotationIntrospector().findValue(Color.class), null);
        Object deserialized = keyDeserializer.deserializeKey("RED", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testEmptyStringDeserialization() throws IOException {
        String json = "{\"key\":\"\"}";
        String expected = "";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object deserialized = keyDeserializer.deserializeKey("", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testNullDeserialization() throws IOException {
        String json = "{\"key\":null}";
        String expected = null;

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        Object deserialized = keyDeserializer.deserializeKey(null, objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testCustomClassDeserializationViaConstructor() throws IOException {
        String json = "{\"key\":\"MADAGASCAR\"}";
        Country expected = new Country("MADAGASCAR");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();

        Constructor<Country> constructor = Country.class.getConstructor(String.class);
        KeyDeserializer keyDeserializer = new StringCtorKeyDeserializer(constructor);
        Object deserialized = keyDeserializer.deserializeKey("MADAGASCAR", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testCustomClassDeserializationViaStaticFactoryMethod() throws IOException {
        String json = "{\"key\":\"MADAGASCAR\"}";
        Country expected = new Country("MADAGASCAR");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();

        AnnotatedMethod factoryMethod = objectMapper.getSerializationConfig().findValueMethod(Country.class);
        KeyDeserializer keyDeserializer = new StringFactoryKeyDeserializer(factoryMethod);
        Object deserialized = keyDeserializer.deserializeKey("MADAGASCAR", objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);
    }

    @Test
    public void testCustomClassDeserializationViaJsonDeserializer() throws IOException {
        String json = "{\"key\":{\"code\":\"MA\",\"name\":\"Madagascar\"}}";
        Country expected = new Country("MA", "Madagascar");

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        ObjectMapper countryMapper = new ObjectMapper();
        countryMapper.registerSubtypes(Country.class);
        JacksonAnnotationIntrospector introspector = new JacksonAnnotationIntrospector();
        AnnotatedClass countryClass = introspector.findClassAnnotations(Country.class, null);
        JsonDeserializer<Country> countryDeserializer = countryClass.findValueDeserializer(Country.class);
        reader.setValueInstantiator(Country.class, countryDeserializer);

        Object deserialized = reader.readValue(json, Map.class).get("key");

        assertEquals(deserialized, expected);
    }

    @Test
    public void testInvalidDeserialization() throws IOException {
        String json = "{\"key\":\"invalid\"}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);

        try {
            keyDeserializer.deserializeKey("invalid", objectMapper.getDeserializationContext());
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "not a valid representation");
        }
    }

    @Test
    public void testInvalidLongDeserialization() throws IOException {
        String json = "{\"key\":\"-1234567890123456789012345678901234567890\"}";
        Long expected = -1234567890123456789L;

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Long.class);
        Object deserialized = keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());

        assertEquals(deserialized, expected);

        try {
            keyDeserializer.deserializeKey("1234567890123456789012345678901234567891", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "overflow, value can not be represented as 64-bit value");
        }
    }

    @Test
    public void testInvalidURIDeserialization() throws IOException {
        String json = "{\"key\":\"invalidURI:/\"}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URI.class);
        try {
            keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "not a valid representation");
        }
    }

    @Test
    public void testInvalidURLDeserialization() throws IOException {
        String json = "{\"key\":\"invalidURL:malformedURL\":1}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(URL.class);
        try {
            keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "not a valid representation");
        }
    }

    @Test
    public void testInvalidClassDeserialization() throws IOException {
        String json = "{\"key\":\"invalidClass\"}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(String.class);
        try {
            keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());
            fail();
        } catch (JsonMappingException e) {
            assertEquals(e.getMessage(), "unable to parse key as Class");
        }
    }

    @Test
    public void testInvalidEnumDeserialization() throws IOException {
        String json = "{\"key\":\"INVALID_ENUM\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        reader.findAndRegisterValues(DayOfWeek.class);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);
        try {
            keyDeserializer.deserializeKey("INVALID_ENUM", objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "not one of values excepted for Enum class: [DAY_OF_WEEK]");
        }
    }

    @Test
    public void testInvalidEnumDeserializationWithUnknownValuesAsNull() throws IOException {
        String json = "{\"key\":\"INVALID_ENUM\"}";

        ObjectMapper objectMapper = new ObjectMapper();
        ObjectReader reader = objectMapper.readerForMap();
        objectMapper.configure(DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL, true);

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(DayOfWeek.class);
        Object deserialized = keyDeserializer.deserializeKey("INVALID_ENUM", objectMapper.getDeserializationContext());

        assertNull(deserialized);
    }

    @Test
    public void testInvalidLocaleDeserialization() throws IOException {
        String json = "{\"key\":\"invalidLocale:zZ\"}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Locale.class);
        try {
            keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "unable to parse key as locale");
        }
    }

    @Test
    public void testInvalidCurrencyDeserialization() throws IOException {
        String json = "{\"key\":\"invalidCurrency:XZD\"}";

        KeyDeserializer keyDeserializer = StdKeyDeserializer.forType(Currency.class);
        try {
            keyDeserializer.deserializeKey(json, objectMapper.getDeserializationContext());
            fail();
        } catch (JsonParseException e) {
            assertEquals(e.getMessage(), "unable to parse key as currency");
        }
    }
}