import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.MethodProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Test;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    @Test
    public void testMethodProperty() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        JavaType javaType = mapper.getTypeFactory().constructType(TestClass.class);
        TestClass testClass = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{\"name\":\"John\", \"age\":40}"), javaType);

        MethodProperty methodProperty = mapper.findProperty("name", TestClass.class);
        assertEquals("John", methodProperty.getValue(testClass));

        methodProperty.setValue(testClass, "Jane");
        assertEquals("Jane", methodProperty.getValue(testClass));

        JsonParser jsonParser = mapper.getFactory().createParser("{\"name\":\"Jane\"}");
        methodProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClass);
        assertEquals("Jane", methodProperty.getValue(testClass));

        jsonParser = mapper.getFactory().createParser("null");
        methodProperty.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClass);
        assertNull(methodProperty.getValue(testClass));

        JsonParser jsonParserNullValue = mapper.getFactory().createParser("{\"name\":null}");
        methodProperty.deserializeAndSet(jsonParserNullValue, mapper.getDeserializationContext(), testClass);
        assertNull(methodProperty.getValue(testClass));

        JsonParser jsonParserEmptyString = mapper.getFactory().createParser("{\"name\":\"\"}");
        methodProperty.deserializeAndSet(jsonParserEmptyString, mapper.getDeserializationContext(), testClass);
        assertNull(methodProperty.getValue(testClass));

        TestClass testClassNull = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{\"name\":null}"), javaType);
        methodProperty.setValue(testClassNull, "Test");
        assertEquals("Test", methodProperty.getValue(testClassNull));

        JsonParser jsonParserNullValueDeserialize = mapper.getFactory().createParser("{\"name\":null}");
        Object value = methodProperty.deserializeSetAndReturn(jsonParserNullValueDeserializer, mapper.getDeserializationContext(), testClassNull);
        assertEquals(testClassNull, value);

        TestClass testClassEmptyString = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{\"name\":\"\"}"), javaType);
        value = methodProperty.deserializeSetAndReturn(jsonParserEmptyString, mapper.getDeserializationContext(), testClassEmptyString);
        assertEquals(testClassEmptyString, value);

        TestClass testClassNullValue = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{\"name\":null}"), javaType);
        value = methodProperty.deserializeSetAndReturn(jsonParserNullValueDeserializer, mapper.getDeserializationContext(), testClassNullValue);
        assertEquals(testClassNullValue, value);

        TestClass testClassNullDeserialize = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("null"), javaType);
        value = methodProperty.deserializeSetAndReturn(jsonParserNullValueDeserializer, mapper.getDeserializationContext(), testClassNullDeserialize);
        assertEquals(testClassNullDeserialize, value);

        TestClass testClassEmptyDeserialize = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{}"), javaType);
        value = methodProperty.deserializeSetAndReturn(jsonParserEmptyString, mapper.getDeserializationContext(), testClassEmptyDeserialize);
        assertEquals(testClassEmptyDeserialize, value);

        TestClass testClassWithType = mapper.readValue(new ObjectMapperTest().createObjectContext().readValueAsStream("{\"name\":\"Test\", \"type\":\"String\"}"), javaType);
        TypeDeserializer typeDeserializer = mapper.getTypeFactory().constructValueType(String.class, null);
        MethodProperty methodPropertyWithType = mapper.findProperty("name", TestClassWithType.class);
        value = methodPropertyWithType.deserializeSetAndReturn(jsonParser, mapper.getDeserializationContext(), testClassWithType);
        assertEquals("Test", value);
        assertEquals("Test", methodPropertyWithType.getValue(value));
    }

    static class TestClass {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    static class TestClassWithType {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getType() {
            return "String";
        }
    }
}

class ObjectMapperTest {
    ObjectMapper createObjectMapper() {
        return new ObjectMapper();
    }
}