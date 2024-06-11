package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId.Referring;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.ObjectMapperUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    @Test
    public void testSettableAnyProperty() {
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };

        AnnotatedMember setter = new AnnotatedField() {
            @Override
            public JavaMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testSetter";
            }

            @Override
            public Object getValue(Object o) {
                return null;
            }
        };

        JavaType type = ObjectMapperUtil.constructType(TestPojo.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertNull(settableAnyProperty.getValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testSettableAnyPropertyWithValueDeserializer() {
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };

        AnnotatedMember setter = new AnnotatedField() {
            @Override
            public JavaMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testSetter";
            }

            @Override
            public Object getValue(Object o) {
                return null;
            }
        };

        JavaType type = ObjectMapperUtil.constructType(TestPojo.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        assertEquals("testProperty", settableAnyProperty.getProperty().getName());
        assertEquals(valueDeserializer, settableAnyProperty.getValueDeserializer());
        assertEquals(type, settableAnyProperty.getType());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };

        AnnotatedMember setter = new AnnotatedField() {
            @Override
            public JavaMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return ObjectMapperUtil.constructType(Map.class);
            }

            @Override
            public String getName() {
                return "testSetter";
            }

            @Override
            public Object getValue(Object o) {
                return null;
            }
        };

        JavaType type = ObjectMapperUtil.constructType(TestPojo.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);
        ObjectMapper objectMapper = new ObjectMapper();
        TestPojo testPojo = new TestPojo();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapperContext context = new ObjectMapperContext();
        context.setExecutor(new ThreadLocalExecutor());
        context.setContextualizer(new StandardDeserializationContext());

        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\":{\"testKey\":\"testValue\"}}");

        settableAnyProperty.deserializeAndSet(jsonParser, context, testPojo, "testProperty");

        Map<Object, Object> map = (Map<Object, Object>) settableAnyProperty.getProperty().getMember().getValue(testPojo);
        assertEquals("testValue", map.get("testKey"));
    }

    @Test
    public void testDeserialize() throws IOException {
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };

        AnnotatedMember setter = new AnnotatedField() {
            @Override
            public JavaMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return ObjectMapperUtil.constructType(Map.class);
            }

            @Override
            public String getName() {
                return "testSetter";
            }

            @Override
            public Object getValue(Object o) {
                return null;
            }
        };

        JavaType type = ObjectMapperUtil.constructType(TestPojo.class);
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        TypeDeserializer valueTypeDeserializer = mock(TypeDeserializer.class);
        KeyDeserializer keyDeserializer = mock(KeyDeserializer.class);

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);

        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser("{\"testProperty\":\"testValue\"}");

        assertEquals("testValue", settableAnyProperty.deserialize(jsonParser, new TestDeserializationContext()).getValue());
    }

    @Test
    public void testSet() throws IOException {
        BeanProperty property = new BeanProperty() {
            @Override
            public AnnotatedMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return null;
            }

            @Override
            public String getName() {
                return "testProperty";
            }
        };

        AnnotatedMember setter = new AnnotatedField() {
            @Override
            public JavaMember getMember() {
                return null;
            }

            @Override
            public JavaType getType() {
                return ObjectMapperUtil.constructType(Map.class);
            }

            @Override
            public String getName() {
                return "testSetter";
            }

            @Override
            public Object getValue(Object o) {
                return null;
            }
        };

        JavaType type = ObjectMapperUtil.constructType(TestPojo.class);
        TestPojo testPojo = new TestPojo();

        SettableAnyProperty settableAnyProperty = new SettableAnyProperty(property, setter, type, null, null, null);

        Map<Object, Object> map = new HashMap<>();
        map.put("testKey", "testValue");
        settableAnyProperty.set(testPojo, "testKey", map);

        Map<Object, Object> map2 = (Map<Object, Object>) settableAnyProperty.getProperty().getMember().getValue(testPojo);
        assertEquals("testValue", map2.get("testKey"));
    }
}

class TestPojo {
    Map<Object, Object> testProperty;
}

class TestDeserializationContext extends StandardDeserializationContext {
}