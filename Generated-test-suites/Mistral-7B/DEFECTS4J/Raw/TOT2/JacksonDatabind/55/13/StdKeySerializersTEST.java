import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import static org.junit.Assert.assertEquals;

public class StdKeySerializersTest {

    @Test
    public void testGetStdKeySerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), String.class, false);
        assertEquals(StdKeySerializers.DEFAULT_STRING_SERIALIZER, serializer);
    }

    @Test
    public void testGetFallbackKeySerializer() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getFallbackKeySerializer(mapper.getSerializationConfig(), int.class);
        assertEquals(StdKeySerializers.DEFAULT_KEY_SERIALIZER, serializer);
    }

    @Test
    public void testGetStdKeySerializer_ClassType() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Class.class, false);
        assertEquals(StdKeySerializers.DEFAULT, serializer);
    }

    @Test
    public void testGetStdKeySerializer_DateType() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), Date.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_DATE, serializer.getClass().getField("_typeId").getInt(serializer));
    }

    @Test
    public void testGetStdKeySerializer_EnumType() throws JsonMappingException, JsonGenerationException {
        ObjectMapper mapper = new ObjectMapper();
        JsonSerializer<Object> serializer = StdKeySerializers.getStdKeySerializer(mapper.getSerializationConfig(), MyEnum.class, false);
        assertEquals(StdKeySerializers.Default.TYPE_ENUM, serializer.getClass().getField("_typeId").getInt(serializer));
    }
}