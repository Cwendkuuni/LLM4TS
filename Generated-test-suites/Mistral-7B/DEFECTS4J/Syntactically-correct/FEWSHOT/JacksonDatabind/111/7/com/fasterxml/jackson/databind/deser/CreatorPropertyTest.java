package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.util.TestUtilities.constructParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest extends TestBase {

    // Test class for CreatorProperty
    static class TestBean {
        private int _value;

        @JsonCreator
        public TestBean(@JsonProperty("value") int value) {
            _value = value;
        }

        @JsonProperty
        public int getValue() {
            return _value;
        }

        public void setValue(int value) {
            _value = value;
        }
    }

    @SuppressWarnings("unchecked")
    static class TestCreator extends BeanDeserializerProvider.BeanDeserializerBase<TestBean> {
        public TestCreator() {
            super(TestBean.class);
        }

        @Override
        public TestBean deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return new TestBean(p.getValueAsInt());
        }
    }

    @Test
    public void testCreatorProperty() throws Exception {
        JavaType type = mapper().getTypeFactory().constructType(TestBean.class);
        ObjectMapper mapper = new ObjectMapper();

        Annotation[] contextAnnotations = new Annotation[0];
        PropertyName name = PropertyName.construct("value");
        JavaType wrapperName = mapper.getTypeFactory().constructType(TestBean.class);
        PropertyName simpleName = PropertyName.construct("value");
        TypeDeserializer typeDeser = null;
        AnnotatedParameter param = new AnnotatedParameter(TestBean.class.getConstructor(int.class), 0);
        int index = 0;
        Object injectableValueId = null;

        CreatorProperty property = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, null);

        assertNull(property.getFallbackSetter());
        assertEquals(name, property.getName());
        assertEquals(type, property.getType());
        assertEquals(wrapperName, property.getWrapperName());
        assertEquals(typeDeser, property.getTypeDeserializer());
        assertNull(property.getInjectableValueId());
        assertEquals(contextAnnotations, property.getContextAnnotations());
        assertEquals(param, property.getAnnotated());
        assertEquals(index, property.getCreatorIndex());
        assertEquals(false, property.isIgnorable());

        // Test with setFallbackSetter
        SettableBeanProperty fallbackSetter = new SimpleBeanProperty("fallbackSetter", IntType.getInstance());
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property.getFallbackSetter());

        // Test with findInjectableValue and inject
        DeserializationContext context = mapper.getDeserializationContext();
        TestBean instance = mapper.readValue(constructParser(mapper, "[1]"), TestBean.class);
        Object injectedValue = new Object() {
            @JsonCreator
            public Object(@JsonProperty("value") int value) {
                this.value = value;
            }

            int value;
        };
        Object injectedValueId = injectedValue;
        Object injectedValueResult = property.findInjectableValue(context, instance);
        property.inject(context, instance);
        assertEquals(injectedValueResult, instance.getValue());

        // Test with deserializeAndSet and setAndReturn
        JsonParser p = constructParser(mapper, "[2]");
        p.nextValue(); // consume int [1]
        int valueToSet = 2;
        Object result = property.deserializeAndSet(p, context, instance);
        assertEquals(valueToSet, instance.getValue());
        assertEquals(valueToSet, result);

        // Test with isIgnorable
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }
}