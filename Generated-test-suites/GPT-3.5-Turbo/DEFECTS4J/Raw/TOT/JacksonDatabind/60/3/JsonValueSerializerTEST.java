import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonValueSerializerTest {

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void testSerialize_withJsonValueAnnotation_shouldSerializeUsingAnnotatedMethod() throws Exception {
        // Arrange
        TestObject testObject = new TestObject("Test Value");

        // Act
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("{\"value\":\"Test Value\"}", json);
    }

    @Test
    public void testSerialize_withJsonValueAnnotationAndNullValue_shouldSerializeNullValue() throws Exception {
        // Arrange
        TestObject testObject = new TestObject(null);

        // Act
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("{\"value\":null}", json);
    }

    @Test
    public void testSerialize_withJsonValueAnnotationAndCustomSerializer_shouldSerializeUsingCustomSerializer() throws Exception {
        // Arrange
        TestObject testObject = new TestObject("Test Value");

        // Act
        objectMapper.addMixIn(TestObject.class, TestObjectMixin.class);
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("{\"value\":\"Modified Test Value\"}", json);
    }

    @Test
    public void testSerializeWithType_withJsonValueAnnotation_shouldSerializeUsingAnnotatedMethodWithTypeInfo() throws Exception {
        // Arrange
        TestObject testObject = new TestObject("Test Value");

        // Act
        objectMapper.enableDefaultTyping();
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("[\"com.fasterxml.jackson.databind.ser.std.TestObject\",{\"value\":\"Test Value\"}]", json);
    }

    @Test
    public void testSerializeWithType_withJsonValueAnnotationAndCustomSerializer_shouldSerializeUsingCustomSerializerWithTypeInfo() throws Exception {
        // Arrange
        TestObject testObject = new TestObject("Test Value");

        // Act
        objectMapper.enableDefaultTyping();
        objectMapper.addMixIn(TestObject.class, TestObjectMixin.class);
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("[\"com.fasterxml.jackson.databind.ser.std.TestObject\",{\"value\":\"Modified Test Value\"}]", json);
    }

    @Test
    public void testSerializeWithType_withJsonValueAnnotationAndNullValue_shouldSerializeNullValueWithTypeInfo() throws Exception {
        // Arrange
        TestObject testObject = new TestObject(null);

        // Act
        objectMapper.enableDefaultTyping();
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("[\"com.fasterxml.jackson.databind.ser.std.TestObject\",{\"value\":null}]", json);
    }

    @Test
    public void testSerializeWithType_withoutJsonValueAnnotation_shouldSerializeUsingSerializerForActualType() throws Exception {
        // Arrange
        TestObjectWithoutJsonValue testObject = new TestObjectWithoutJsonValue("Test Value");

        // Act
        objectMapper.enableDefaultTyping();
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("{\"value\":\"Test Value\"}", json);
    }

    @Test
    public void testSerializeWithType_withoutJsonValueAnnotationAndNullValue_shouldSerializeNullValueWithTypeInfo() throws Exception {
        // Arrange
        TestObjectWithoutJsonValue testObject = new TestObjectWithoutJsonValue(null);

        // Act
        objectMapper.enableDefaultTyping();
        String json = objectMapper.writeValueAsString(testObject);

        // Assert
        assertEquals("{\"value\":null}", json);
    }

    static class TestObject {
        private String value;

        public TestObject(String value) {
            this.value = value;
        }

        @JsonValue
        public String getValue() {
            return value;
        }
    }

    static class TestObjectMixin {
        @JsonValue
        public String getValue() {
            return "Modified Test Value";
        }
    }

    static class TestObjectWithoutJsonValue {
        private String value;

        public TestObjectWithoutJsonValue(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}