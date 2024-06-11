package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.test.JavaType;

/**
 * This class represents test cases for {@link SetterlessProperty} class.
 */
public class SetterlessPropertyTest extends TestBase {

    @SuppressWarnings("unchecked")
    @Test
    public void testSetterlessProperty() throws IOException {
        Class<TestClass> clazz = TestClass.class;
        ObjectMapper mapper = new ObjectMapper();

        // Test with simple class having getterless property
        TestClass testClassWithGetterlessProperty = new TestClass();
        JavaType javaType = mapper.getTypeFactory().constructType(clazz);
        JsonParser jsonParser = mapper.getFactory().createParser(mapper.writeValueAsString(new TestClassValue()));

        SettableBeanProperty property = mapper.findProperty("list", javaType);
        assertNotNull(property);
        assertNull(property.getGetMethod());
        assertEquals(SettableBeanProperty.PropertyType.COLLECTION_OR_MAP, property.getBeanPropertyDefinition().getType());

        property.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClassWithGetterlessProperty);
        assertNotNull(testClassWithGetterlessProperty.getList());
        assertTrue(testClassWithGetterlessProperty.getList().isEmpty());

        // Test with class having getter and setter
        TestClassWithGetterAndSetter testClassWithGetterAndSetter = new TestClassWithGetterAndSetter();
        JavaType javaTypeWithGetterAndSetter = mapper.getTypeFactory().constructType(clazz);
        JsonParser jsonParserWithGetterAndSetter = mapper.getFactory().createParser(mapper.writeValueAsString(new TestClassValueWithGetterAndSetter()));

        SettableBeanProperty propertyWithGetterAndSetter = mapper.findProperty("map", javaTypeWithGetterAndSetter);
        assertNotNull(propertyWithGetterAndSetter);
        assertNotNull(propertyWithGetterAndSetter.getGetMethod());
        assertEquals(SettableBeanProperty.PropertyType.COLLECTION_OR_MAP, propertyWithGetterAndSetter.getBeanPropertyDefinition().getType());

        propertyWithGetterAndSetter.deserializeAndSet(jsonParserWithGetterAndSetter, mapper.getDeserializationContext(), testClassWithGetterAndSetter);
        assertNotNull(testClassWithGetterAndSetter.getMap());
        assertTrue(testClassWithGetterAndSetter.getMap().isEmpty());

        // Test with getterless property annotated with @JsonProperty
        TestClassWithJsonPropertyAnnotation testClassWithJsonPropertyAnnotation = new TestClassWithJsonPropertyAnnotation();
        SettableBeanProperty propertyWithJsonPropertyAnnotation = mapper.findProperty("list", javaType);
        assertNotNull(propertyWithJsonPropertyAnnotation);
        assertNull(propertyWithJsonPropertyAnnotation.getGetMethod());
        assertEquals(SettableBeanProperty.PropertyType.COLLECTION_OR_MAP, propertyWithJsonPropertyAnnotation.getBeanPropertyDefinition().getType());

        propertyWithJsonPropertyAnnotation.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClassWithJsonPropertyAnnotation);
        assertNotNull(testClassWithJsonPropertyAnnotation.getList());
        assertTrue(testClassWithJsonPropertyAnnotation.getList().isEmpty());

        // Test with getterless property with custom TypeDeserializer
        TestClassWithCustomTypeDeserializer testClassWithCustomTypeDeserializer = new TestClassWithCustomTypeDeserializer();
        SettableBeanProperty propertyWithCustomTypeDeserializer = mapper.findProperty("set", javaType);
        assertNotNull(propertyWithCustomTypeDeserializer);
        assertNull(propertyWithCustomTypeDeserializer.getGetMethod());
        assertEquals(SettableBeanProperty.PropertyType.COLLECTION_OR_MAP, propertyWithCustomTypeDeserializer.getBeanPropertyDefinition().getType());

        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.deserializeUsing(eq(JsonToken.START_ARRAY), any(), any(), any()))
                .thenReturn(new TestClassValueWithCustomTypeDeserializer());
        propertyWithCustomTypeDeserializer.setTypeDeserializer(typeDeserializer);

        propertyWithCustomTypeDeserializer.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClassWithCustomTypeDeserializer);
        assertNotNull(testClassWithCustomTypeDeserializer.getSet());
        assertTrue(testClassWithCustomTypeDeserializer.getSet().isEmpty());

        // Test with getterless property with custom NullValueProvider
        TestClassWithCustomNullValueProvider testClassWithCustomNullValueProvider = new TestClassWithCustomNullValueProvider();
        SettableBeanProperty propertyWithCustomNullValueProvider = mapper.findProperty("nullList", javaType);
        assertNotNull(propertyWithCustomNullValueProvider);
        assertNull(propertyWithCustomNullValueProvider.getGetMethod());
        assertEquals(SettableBeanProperty.PropertyType.COLLECTION_OR_MAP, propertyWithCustomNullValueProvider.getBeanPropertyDefinition().getType());

        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        when(nullValueProvider.nullValue(any()))
                .thenReturn(new TestClassValueWithCustomNullValueProvider());
        propertyWithCustomNullValueProvider.setNullProvider(nullValueProvider);

        propertyWithCustomNullValueProvider.deserializeAndSet(jsonParser, mapper.getDeserializationContext(), testClassWithCustomNullValueProvider);
        assertNotNull(testClassWithCustomNullValueProvider.getNullList());
        assertTrue(testClassWithCustomNullValueProvider.getNullList().isEmpty());
    }

    private static class TestClass {
        private transient List<Integer> list;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }

    @JsonProperty("list")
    private static class TestClassValue {
        private List<Integer> list;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }

    private static class TestClassValueWithGetterAndSetter {
        private Map<String, Integer> map;

        public Map<String, Integer> getMap() {
            return map;
        }

        public void setMap(Map<String, Integer> map) {
            this.map = map;
        }
    }

    @JsonProperty("list")
    private static class TestClassValueWithJsonPropertyAnnotation {
        @JsonProperty("list")
        private List<Integer> list;

        public List<Integer> getList() {
            return list;
        }

        public void setList(List<Integer> list) {
            this.list = list;
        }
    }

    private static class TestClassWithCustomTypeDeserializer {
        private transient Set<Integer> set;

        public Set<Integer> getSet() {
            return set;
        }

        // Empty constructor
        public TestClassWithCustomTypeDeserializer() {}

        // Constructor with custom TypeDeserializer
        @JsonDeserialize(using = TestClassCustomTypeDeserializer.class)
        public TestClassWithCustomTypeDeserializer(List<Integer> list) {
            this.set = ClassUtil.constructSet(Integer.class, list);
        }
    }

    private static class TestClassValueWithCustomTypeDeserializer {
        private int value;

        public int getValue() {
            return value;
        }

        public void setValue(int value) {
            this.value = value;
        }
    }

    private static class TestClassWithCustomNullValueProvider {
        private transient List<Integer> nullList;

        public List<Integer> getNullList() {
            return nullList;
        }

        // Empty constructor
        public TestClassWithCustomNullValueProvider() {}

        // Constructor with custom NullValueProvider
        @JsonDeserialize(using = TestClassCustomNullValueProvider.class)
        public TestClassWithCustomNullValueProvider(List<Integer> list) {
            this.nullList = list;
        }

        @JsonValue
        public List<Integer> toJson() {
            return nullList;
        }
    }

    private static class TestClassCustomTypeDeserializer extends JsonDeserializer<TestClassValueWithCustomTypeDeserializer> {
        @Override
        public TestClassValueWithCustomTypeDeserializer deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken == JsonToken.START_ARRAY) {
                TestClassValueWithCustomTypeDeserializer testClassValueWithCustomTypeDeserializer = new TestClassValueWithCustomTypeDeserializer();
                TestClassValueWithCustomTypeDeserializer.TestClassValueWithCustomTypeDeserializerBuilder builder = testClassValueWithCustomTypeDeserializer.newBuilder();
                while (currentToken != null) {
                    switch (currentToken) {
                        case VALUE_NUMBER_INT:
                            int value = p.getIntValue();
                            builder.addValue(value);
                            break;
                        case VALUE_STRING:
                            String valueAsString = p.getValueAsString();
                            builder.addValue(Integer.parseInt(valueAsString));
                            break;
                        case END_ARRAY:
                            TestClassValueWithCustomTypeDeserializer result = testClassValueWithCustomTypeDeserializer.build();
                            return result;
                        default:
                            throw ctxt.wrongTokenException(null, currentToken);
                    }
                    currentToken = p.nextToken();
                }
            }
            throw ctxt.mappingException("Can not deserialize TestClassValueWithCustomTypeDeserializer");
        }
    }

    private static class TestClassCustomNullValueProvider extends JsonDeserializer<List<Integer>> {
        @Override
        public List<Integer> deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            JsonToken currentToken = p.getCurrentToken();
            if (currentToken == JsonToken.VALUE_NULL) {
                return Collections.emptyList();
            }
            throw ctxt.wrongTokenException(null, currentToken);
        }
    }
}