package com.example.junit;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.BaseJsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.impl.SetterlessProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AnnotationIntrospector;

import java.io.IOException;
import java.lang.reflect.Method;

public class SetterlessPropertyTest {

    @JsonDeserialize(using = SetterlessProperty.class)
    static class TestClass {
        private Object property;

        @JsonValue
        public Object getProperty() {
            return property;
        }

        public void setProperty(Object value) {
            this.property = value;
        }

        @JsonProperty("testProperty")
        public Object getTestProperty() {
            return property;
        }
    }

    @ org.junit.Test
    public void testSetterlessProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        TestClass testClass = new TestClass();

        // Test deserialization with setterless property
        String json = "{\"testProperty\":[1,2,3]}";
        TestClass deserializedClass = mapper.readValue(new BaseJsonParser(new StringReader(json)), TestClass.class);
        assertEquals("[1, 2, 3]", deserializedClass.getProperty().toString());

        // Test setting value with setterless property
        TestClass testClassToSet = new TestClass();
        Object newValue = new Object() {};
        PropertyName propertyName = PropertyName.construct("testProperty");
        SetterlessProperty property = new SetterlessProperty(
                new BeanPropertyDefinition(propertyName, Object.class, null, null),
                ObjectType.constructRaw(Object.class),
                null,
                Annotations.EMPTY_ANNOTATIONS,
                AnnotatedMethod.construct(TestClass.class, "getTestProperty", new Class[]{})
        );
        TypeDeserializer typeDeserializer = null;
        JsonDeserializer<Object> valueDeserializer = null;
        SetterlessProperty setterlessProperty = new SetterlessProperty(property, valueDeserializer, null);

        Method getter = TestClass.class.getMethod("getProperty");
        setterlessProperty._getter = getter;
        setterlessProperty._annotated = AnnotatedMethod.construct(TestClass.class, "getTestProperty", new Class[]{});

        Object oldValue = testClassToSet.getProperty();
        setterlessProperty.deserializeAndSet(new BaseJsonParser(new StringReader("1")), new TestDeserializationContext(), testClassToSet);
        assertNotSame(oldValue, testClassToSet.getProperty());
        assertEquals(1, testClassToSet.getProperty().toString().length());

        // Test null value
        String jsonNull = "null";
        TestClass deserializedClassNull = mapper.readValue(new BaseJsonParser(new StringReader(jsonNull)), TestClass.class);
        assertNull(deserializedClassNull.getProperty());

        // Test deserialization with null value provider
        JsonNullProvider nullProvider = new JsonNullProvider.None();
        JsonDeserializer<Object> deserializerNull = null;
        SetterlessProperty setterlessPropertyNull = new SetterlessProperty(property, deserializerNull, nullProvider);
        deserializedClassNull = mapper.readValue(new BaseJsonParser(new StringReader(jsonNull)), TestClass.class, new ObjectMapper.DefaultTyping());
        assertNull(deserializedClassNull.getProperty());

        // Test fixAccess method
        DeserializationConfig config = new DeserializationConfig();
        setterlessProperty.fixAccess(config);
        AnnotationIntrospector introspector = new AnnotationIntrospector();
        assertEquals(Modifier.PUBLIC, introspector.findAnnotation(getter, Access.class).getMemberVisibility());
    }
}

class TestDeserializationContext extends DeserializationContext {
    public TestDeserializationContext() {
        super(new ObjectMapper(), new Provider());
    }
}