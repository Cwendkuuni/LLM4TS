package com.fasterxml.jackson.databind.deser.std;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.core.JsonLocation;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JacksonStdImpl;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.jupiter.api.*;
import java.io.IOException;
import java.util.*;
import static org.junit.jupiter.api.Assertions.*;

@DisplayName("CollectionDeserializer Test")
public class CollectionDeserializerTest {

    private static final JavaType collectionType = TypeFactory.defaultInstance().constructCollectionType(List.class, String.class);
    private static final JsonDeserializer<String> stringDeserializer = new StringDeserializer();
    private static final TypeDeserializer stringTypeDeserializer = null;
    private static final ValueInstantiator valueInstantiator = new ValueInstantiator.Std() {};
    private static final JsonDeserializer<Object> delegateDeserializer = null;
    private static final Boolean unwrapSingle = null;

    private static CollectionDeserializer createDeserializerUnderTest(
            JsonDeserializer<Object> valueDeserializer,
            TypeDeserializer valueTypeDeserializer,
            ValueInstantiator valueInstantiator,
            JsonDeserializer<Object> delegateDeserializer,
            Boolean unwrapSingle) {
        return new CollectionDeserializer(collectionType, valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
    }

    @Nested
    @DisplayName("Test Create Contextual Instance")
    class TestCreateContextualInstance {

        @Test
        @DisplayName("Test Create Contextual Instance with Null ValueDeserializer, TypeDeserializer, ValueInstantiator, DelegateDeserializer, and UnwrapSingle")
        void testCreateContextualInstanceWithNullValues() {
            CollectionDeserializer deserializer = createDeserializerUnderTest(null, null, null, null, null);
            assertEquals(deserializer.getClass(), CollectionDeserializer.class);
            assertNull(deserializer.getValueDeserializer());
            assertNull(deserializer.getValueTypeDeserializer());
            assertNull(deserializer.getValueInstantiator());
            assertNull(deserializer.getDelegateDeserializer());
            assertNull(deserializer.unwrapSingle);
        }

        @Test
        @DisplayName("Test Create Contextual Instance with Assigned ValueDeserializer, TypeDeserializer, ValueInstantiator, DelegateDeserializer, and UnwrapSingle")
        void testCreateContextualInstanceWithAssignedValues() {
            JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
            TypeDeserializer valueTypeDeserializer = new StubTypeDeserializer<>();
            ValueInstantiator valueInstantiator = new StubValueInstantiator<>();
            JsonDeserializer<Object> delegateDeserializer = new StubJsonDeserializer<>();
            Boolean unwrapSingle = Boolean.TRUE;

            CollectionDeserializer deserializer = createDeserializerUnderTest(valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            assertEquals(deserializer.getClass(), CollectionDeserializer.class);
            assertEquals(valueDeserializer, deserializer.getValueDeserializer());
            assertEquals(valueTypeDeserializer, deserializer.getValueTypeDeserializer());
            assertEquals(valueInstantiator, deserializer.getValueInstantiator());
            assertEquals(delegateDeserializer, deserializer.getDelegateDeserializer());
            assertEquals(unwrapSingle, deserializer.unwrapSingle);
        }
    }

    @Nested
    @DisplayName("Test IsCachable")
    class TestIsCachable {

        @Test
        @DisplayName("Test IsCachable with Null ValueDeserializer, TypeDeserializer, DelegateDeserializer")
        void testIsCachableWithNullValues() {
            CollectionDeserializer deserializer = createDeserializerUnderTest(null, null, null, null, null);
            assertTrue(deserializer.isCachable());
        }

        @Test
        @DisplayName("Test IsCachable with Assigned ValueDeserializer, TypeDeserializer, DelegateDeserializer, and UnwrapSingle")
        void testIsCachableWithAssignedValues() {
            JsonDeserializer<Object> valueDeserializer = new StubJsonDeserializer<>();
            TypeDeserializer valueTypeDeserializer = new StubTypeDeserializer<>();
            ValueInstantiator valueInstantiator = new StubValueInstantiator<>();
            JsonDeserializer<Object> delegateDeserializer = new StubJsonDeserializer<>();
            Boolean unwrapSingle = Boolean.TRUE;

            CollectionDeserializer deserializer = createDeserializerUnderTest(valueDeserializer, valueTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            assertFalse(deserializer.isCachable());
        }
    }

    @Nested
    @DisplayName("Test Deserialize")
    class TestDeserialize {

        @Test
        @DisplayName("Test Deserialize with Empty String")
        void testDeserializeWithEmptyString() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(stringDeserializer, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[\"\"]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            assertEquals(Collections.singletonList(""), result);
        }

        @Test
        @DisplayName("Test Deserialize with Null Value")
        void testDeserializeWithNullValue() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(stringDeserializer, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("null");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            assertNull(result);
        }

        @Test
        @DisplayName("Test Deserialize with String Value")
        void testDeserializeWithStringValue() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(stringDeserializer, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[\"Test\"]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            List<Object> expected = Collections.singletonList("Test");
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Test Deserialize with Integer Value")
        void testDeserializeWithIntegerValue() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(stringDeserializer, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[42]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            List<Object> expected = Collections.singletonList(42);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Test Deserialize with Object Value")
        void testDeserializeWithObjectValue() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(stringDeserializer, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[{\"name\":\"Test\"}]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            List<Object> expected = Collections.singletonList(new TestObject("Test"));
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Test Deserialize with Referencing Object")
        void testDeserializeWithReferencingObject() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(new StubJsonDeserializer<Object>() {}, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[{\"$ref\":\"#/properties/test\"}]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            List<Object> expected = Collections.singletonList(TestJsonContext.TEST);
            assertEquals(expected, result);
        }

        @Test
        @DisplayName("Test Deserialize with Custom Class")
        void testDeserializeWithCustomClass() throws IOException {
            CollectionDeserializer deserializer = createDeserializerUnderTest(new StubJsonDeserializer<TestCustomClass>() {}, stringTypeDeserializer, valueInstantiator, delegateDeserializer, unwrapSingle);
            JsonParser jsonParser = createTestJsonParser("[{\"name\":\"Test\", \"numbers\":[1, 2, 3]}]");
            Collection<Object> result = deserializer.deserialize(jsonParser, new TestJsonContext());
            TestCustomClass testCustomClass = new TestCustomClass();
            testCustomClass.setNumbers(Arrays.asList(1, 2, 3));
            assertEquals(testCustomClass, result.get(0));
        }
    }

    private static JsonParser createTestJsonParser(String jsonString) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        return mapper.getFactory().createParser(new ByteArrayInputStream(jsonString.getBytes()));
    }

    private static class StubJsonDeserializer<T> extends JsonDeserializer<T> {
        @Override
        public T deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return null;
        }
    }

    private static class StubTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserializeTypedFromArray(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
            return null;
        }
    }

    private static class StubValueInstantiator implements ValueInstantiator {
        @Override
        public Object createValueInstance(DeserializationContext deserializationContext) throws IOException {
            return null;
        }

        @Override
        public Object createValueInstance(DeserializationContext deserializationContext, JsonDeserializer<?> deserializer) throws IOException {
            return null;
        }

        @Override
        public Object createValueInstance(DeserializationContext deserializationContext, BeanProperty property) throws IOException {
            return null;
        }
    }

    private static class TestJsonContext extends TestBaseContext {
        public static final Object TEST = new Object() {};

        @Override
        public JsonDeserializer<?> findContextualValueDeserializer(JavaType type, PropertyName propertyName) throws JsonMappingException {
            if (JsonToken.START_ARRAY.equals(currentToken())) {
                return stringDeserializer;
            }
            return null;
        }

        @Override
        public JsonDeserializer<?> handleSecondaryContextualization(JsonDeserializer<?> deserializer, BeanProperty property, JavaType propertyType) throws JsonMappingException {
            return deserializer;
        }
    }

    private static class TestObject {
        private String name;

        public TestObject(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }

    private static class TestCustomClass {
        private List<Integer> numbers;

        public List<Integer> getNumbers() {
            return numbers;
        }

        public void setNumbers(List<Integer> numbers) {
            this.numbers = numbers;
        }
    }
}