import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.*;

import static org.junit.Assert.*;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    @Before
    public void setup() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testGetStdKeySerializer_String() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);

        String key = "test";
        ObjectNode node = objectMapper.createObjectNode();
        node.set("key", key);

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(key, generator, null);
        generator.flush();
    }

    @Test
    public void testGetStdKeySerializer_Class() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Integer.class, false);
        assertNull(serializer);

        JsonSerializer<Object> fallbackSerializer = StdKeySerializers.getFallbackKeySerializer(null, Integer.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, fallbackSerializer);
    }

    @Test
    public void testGetStdKeySerializer_Enum() throws IOException {
        enum TestEnum {
            TEST
        }

        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, TestEnum.class, false);
        assertEquals(StdKeySerializers.getDefault(), serializer);

        JsonSerializer<Object> fallbackSerializer = StdKeySerializers.getFallbackKeySerializer(null, TestEnum.class);
        assertEquals(StdKeySerializers.getDefault(), fallbackSerializer);

        ObjectMapper mapper = new ObjectMapper();
        ObjectNode node = mapper.createObjectNode();
        node.put("key", TestEnum.TEST.name());

        JsonGenerator generator = mapper.getFactory().createGenerator(System.out);
        serializer.serialize(TestEnum.TEST, generator, null);
        generator.flush();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testGetStdKeySerializer_Null() throws IOException {
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, null, false);
        fail("Expected an IllegalArgumentException");
    }

    @Test
    public void testGetStdKeySerializer_Date() throws IOException {
        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Date.class, false);
        assertEquals(StdKeySerializers.getDefault(StdKeySerializers.Default.TYPE_DATE, Date.class), serializer);

        ObjectNode node = objectMapper.createObjectNode();
        node.put("key", date.getTime());

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(date, generator, null);
        generator.flush();
    }

    @Test
    public void testGetStdKeySerializer_Calendar() throws IOException {
        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, Calendar.class, false);
        assertEquals(StdKeySerializers.getDefault(StdKeySerializers.Default.TYPE_CALENDAR, Calendar.class), serializer);

        ObjectNode node = objectMapper.createObjectNode();
        node.put("key", calendar.getTimeInMillis());

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(calendar, generator, null);
        generator.flush();
    }

    @Test
    public void testGetStdKeySerializer_UUID() throws IOException {
        UUID uuid = UUID.randomUUID();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(null, UUID.class, false);
        assertEquals(StdKeySerializers.getDefault(StdKeySerializers.Default.TYPE_TO_STRING, UUID.class), serializer);

        ObjectNode node = objectMapper.createObjectNode();
        node.put("key", uuid.toString());

        JsonGenerator generator = objectMapper.getFactory().createGenerator(System.out);
        serializer.serialize(uuid, generator, null);
        generator.flush();
    }
}