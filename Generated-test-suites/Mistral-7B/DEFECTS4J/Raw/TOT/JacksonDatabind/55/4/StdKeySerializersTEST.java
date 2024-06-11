import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.json.JsonMapperFactoryBuilder;
import com.fasterxml.jackson.databind.ser.std.StdKeySerializers;
import com.fasterxml.jackson.databind.type.TypeFactory;
import org.junit.Test;

import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class StdKeySerializersTest {

    private ObjectMapper objectMapper;

    public StdKeySerializersTest() throws JsonMappingException {
        objectMapper = new ObjectMapper(new JsonMapperFactoryBuilder()
                .findAndRegisterModules());
    }

    @Test
    public void testGetStringSerializer() throws JsonGenerationException, IOException {
        StringKeySerializer serializer = StdKeySerializers.DEFAULT_STRING_SERIALIZER;
        SerializerProvider provider = new SerializerProvider();
        JsonGenerator generator = provider.getGenerator();

        generator.setCodec(objectMapper.getCodec());
        serializer.serialize("test", generator, provider);
        assertEquals("test", generator.getOutputText());
    }

    @Test
    public void testGetDateSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();

        Date date = new Date();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getConfiguration(),
                Date.class, false);

        serializer.serialize(date, jsonGenerator, serializerProvider);
        assertEquals(date.toString(), jsonGenerator.getOutputText());
    }

    @Test
    public void testGetCalendarSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();

        Calendar calendar = Calendar.getInstance();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getConfiguration(),
                Calendar.class, false);

        serializer.serialize(calendar, jsonGenerator, serializerProvider);
        assertEquals(calendar.getTimeInMillis() + "", jsonGenerator.getOutputText());
    }

    @Test
    public void testGetNullSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();

        JsonSerializer<Object> serializer = StdKeySerializers.DEFAULT_KEY_SERIALIZER;
        serializer.serialize(null, jsonGenerator, serializerProvider);
        assertNull(jsonGenerator.getOutputText());
    }

    @Test
    public void testGetUnknownSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();

        Class<CustomClass> customClass = new CustomClass();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(objectMapper.getConfiguration(),
                customClass, false);

        assertNull(serializer);
    }

    @Test
    public void testGetFallbackSerializer() throws JsonGenerationException, IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        SerializerProvider serializerProvider = new SerializerProvider();
        JsonGenerator jsonGenerator = serializerProvider.getGenerator();

        Class<CustomEnum> customEnum = CustomEnum.class;
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(objectMapper.getConfiguration(),
                customEnum);

        serializer.serialize(CustomEnum.AUTUMN, jsonGenerator, serializerProvider);
        assertEquals(CustomEnum.AUTUMN.name(), jsonGenerator.getOutputText());
    }
}

class CustomClass {
}

enum CustomEnum {
    AUTUMN, SPRING, SUMMER, WINTER
}