import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.ser.std.JsonValueSerializer;
import com.fasterxml.jackson.databind.util.TestBase;

import org.testng.annotations.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class JsonValueSerializerTest extends TestBase {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    static {
        MAPPER.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
    }

    @Test
    public void testJsonValueSerializer_Simple() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(
                MAPPER.findMethodValueDeserializer(MyBean.class, "getValue"), null);

        String expectedJson = "{\"foo\":\"bar\"}";
        MyBean bean = new MyBean("bar");
        String json = serializer.serialize(bean, MAPPER.getFactory().createGenerator(null), null);
        assertEquals(expectedJson, json);
    }

    @Test
    public void testJsonValueSerializer_Null() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(
                MAPPER.findMethodValueDeserializer(MyBean.class, "getValue"), null);

        MyBean bean = new MyBean(null);
        assertNull(serializer.serialize(bean, MAPPER.getFactory().createGenerator(null), null));
    }

    @Test
    public void testJsonValueSerializer_Exception() throws JsonProcessingException {
        MyExceptionBean bean = new MyExceptionBean();
        JsonValueSerializer serializer = new JsonValueSerializer(
                MAPPER.findMethodValueDeserializer(MyExceptionBean.class, "getValue"), null);

        try {
            serializer.serialize(bean, MAPPER.getFactory().createGenerator(null), null);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            assertEquals("MyExceptionBean.getValue()", e.getPath());
        }
    }

    @Test
    public void testJsonValueSerializer_NaturalTypeWithStdHandling() throws JsonProcessingException {
        JsonValueSerializer serializer = new JsonValueSerializer(
                MAPPER.findMethodValueDeserializer(IntBean.class, "getValue"), null);

        int expectedInt = 42;
        IntBean bean = new IntBean(expectedInt);
        int jsonInt = MAPPER.readValue(serializer.serialize(bean, MAPPER.getFactory().createGenerator(null), null), int.class);
        assertEquals(expectedInt, jsonInt);
    }

    private static class MyBean {
        private final String value;

        public MyBean(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }

    private static class MyExceptionBean {
        // Empty constructor to simulate a bean with no value method
    }

    private static class IntBean {
        private final int value;

        public IntBean(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }
}