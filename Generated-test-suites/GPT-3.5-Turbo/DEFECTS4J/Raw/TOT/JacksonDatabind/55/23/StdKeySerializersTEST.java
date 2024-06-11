import java.io.IOException;
import java.util.*;

import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationConfig;
import com.fasterxml.jackson.databind.SerializerProvider;

import org.junit.*;
import static org.junit.Assert.*;

public class StdKeySerializersTest {

    private StdKeySerializers.Default defaultKeySerializer;

    @Before
    public void setUp() {
        defaultKeySerializer = new StdKeySerializers.Default(StdKeySerializers.Default.TYPE_TO_STRING, Object.class);
    }

    @Test
    public void testGetStdKeySerializer_withNullRawKeyType_returnDynamicSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, null, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetStdKeySerializer_withStringRawKeyType_returnDefaultStringSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = String.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetStdKeySerializer_withIntegerRawKeyType_returnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Integer.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_TO_STRING, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetStdKeySerializer_withClassRawKeyType_returnDefaultClassSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Class.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_CLASS, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetStdKeySerializer_withDateRawKeyType_returnDefaultDateSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Date.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetStdKeySerializer_withCalendarRawKeyType_returnDefaultCalendarSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Calendar.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, false);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_CALENDAR, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetStdKeySerializer_withUUIDRawKeyTypeAndUseDefault_returnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = UUID.class;
        boolean useDefault = true;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetStdKeySerializer_withUUIDRawKeyTypeAndNotUseDefault_returnNull() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = UUID.class;
        boolean useDefault = false;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getStdKeySerializer(config, rawKeyType, useDefault);

        // Assert
        assertNull(result);
    }

    @Test
    public void testGetFallbackKeySerializer_withNullRawKeyType_returnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = null;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    @Test
    public void testGetFallbackKeySerializer_withEnumRawKeyType_returnDynamicSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Enum.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Dynamic);
    }

    @Test
    public void testGetFallbackKeySerializer_withEnumClassRawKeyType_returnDefaultEnumSerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = SomeEnum.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertTrue(result instanceof StdKeySerializers.Default);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, ((StdKeySerializers.Default) result)._typeId);
    }

    @Test
    public void testGetFallbackKeySerializer_withNonEnumRawKeyType_returnDefaultKeySerializer() {
        // Arrange
        SerializationConfig config = new ObjectMapper().getSerializationConfig();
        Class<?> rawKeyType = Object.class;

        // Act
        JsonSerializer<Object> result = StdKeySerializers.getFallbackKeySerializer(config, rawKeyType);

        // Assert
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, result);
    }

    private enum SomeEnum {
        VALUE1, VALUE2
    }

    @Test
    public void testSerialize_withDateKeySerializerAndValidDateValue_serializeDateKey() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        Map<Date, Integer> map = new HashMap<>();
        map.put(new Date(0), 1);
        Map<Date, Integer> expected = new HashMap<>();
        expected.put(new Date(0), 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"1970-01-01\""));
    }

    @Test
    public void testSerialize_withCalendarKeySerializerAndValidCalendarValue_serializeCalendarKey() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        Map<Calendar, Integer> map = new HashMap<>();
        Calendar calendar = Calendar.getInstance();
        calendar.set(2000, Calendar.JANUARY, 1);
        map.put(calendar, 1);
        Map<Calendar, Integer> expected = new HashMap<>();
        expected.put(calendar, 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"2000-01-01\""));
    }

    @Test
    public void testSerialize_withClassKeySerializerAndValidClassValue_serializeClassName() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        Map<Class<?>, Integer> map = new HashMap<>();
        map.put(String.class, 1);
        Map<Class<?>, Integer> expected = new HashMap<>();
        expected.put(String.class, 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"java.lang.String\""));
    }

    @Test
    public void testSerialize_withEnumKeySerializerAndValidEnumValueAndWriteEnumsUsingToStringEnabled_serializeEnumName() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        Map<SomeEnum, Integer> map = new HashMap<>();
        map.put(SomeEnum.VALUE1, 1);
        Map<SomeEnum, Integer> expected = new HashMap<>();
        expected.put(SomeEnum.VALUE1, 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"VALUE1\""));
    }

    @Test
    public void testSerialize_withEnumKeySerializerAndValidEnumValueAndWriteEnumsUsingToStringDisabled_serializeEnumToStringValue() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        mapper.disable(SerializationFeature.WRITE_ENUMS_USING_TO_STRING);
        Map<SomeEnum, Integer> map = new HashMap<>();
        map.put(SomeEnum.VALUE1, 1);
        Map<SomeEnum, Integer> expected = new HashMap<>();
        expected.put(SomeEnum.VALUE1, 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"VALUE1\""));
    }

    @Test
    public void testSerialize_withToStringKeySerializerAndValidObjectValue_serializeToStringValue() throws IOException {
        // Arrange
        ObjectMapper mapper = new ObjectMapper();
        Map<Object, Integer> map = new HashMap<>();
        map.put(new Object(), 1);
        Map<Object, Integer> expected = new HashMap<>();
        expected.put(new Object(), 1);

        // Act
        String result = mapper.writeValueAsString(map);

        // Assert
        assertTrue(result.contains("\"java.lang.Object@"));
    }

}