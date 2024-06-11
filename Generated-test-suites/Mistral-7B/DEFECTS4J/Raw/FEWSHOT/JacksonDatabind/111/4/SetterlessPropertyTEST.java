package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import org.junit.Test;

public class SetterlessPropertyTest {

    @Test
    public void testGetter() throws Exception {
        Object obj = new Object() {
            Object property;

            @SuppressWarnings("unchecked")
            public Object getProperty() {
                return property;
            }

            public void setProperty(Object value) {
                this.property = value;
            }
        };

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", Object.class, null);
        JavaType type = ObjectType.getDefaultInstance();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = new AnnotatedMethod(Getter.class, "getProperty", new Class[]{});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object value = new Object();

        Object instance = obj;
        JsonParser parser =创建JsonParser();
        parser.setValue("value");
        Object deserializedValue = property.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals(value, obj.getProperty());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        Object obj = new Object() {
            List<Integer> property = new ArrayList<>();

            public List<Integer> getProperty() {
                return property;
            }

            public void setProperty(List<Integer> value) {
                this.property = value;
            }
        };

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", new TypeFactory().constructCollectionType(List.class, Integer.class), null);
        JavaType type = new TypeFactory().constructCollectionType(List.class, Integer.class);
        TypeDeserializer typeDeser = new ArrayListDeserializer();
        Annotations contextAnnotations = null;
        AnnotatedMethod method = new AnnotatedMethod(Getter.class, "getProperty", new Class[]{});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        List<Integer> value = Arrays.asList(1, 2, 3);

        Object instance = obj;
        JsonParser parser =创建JsonParser();
        parser.setValue("1,2,3");
        property.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertEquals(value, obj.getProperty());
    }

    @Test
    public void testNullValue() throws Exception {
        Object obj = new Object() {
            List<Integer> property = null;

            @SuppressWarnings("unchecked")
            public List<Integer> getProperty() {
                return property;
            }

            public void setProperty(List<Integer> value) {
                this.property = value;
            }
        };

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", new TypeFactory().constructCollectionType(List.class, Integer.class), null);
        JavaType type = new TypeFactory().constructCollectionType(List.class, Integer.class);
        TypeDeserializer typeDeser = new ArrayListDeserializer();
        Annotations contextAnnotations = null;
        AnnotatedMethod method = new AnnotatedMethod(Getter.class, "getProperty", new Class[]{});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object instance = obj;
        JsonParser parser =创建JsonParser();
        parser.setValue(null);
        property.deserializeAndSet(parser, new DeserializationContext(), instance);

        assertNull(obj.getProperty());
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet() throws Exception {
        Object obj = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", String.class, null);
        JavaType type = StringType.getInstance();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = new AnnotatedMethod(Setter.class, "set", new Class[]{String.class});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object instance = obj;
        property.set(instance, "value");
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetAndReturn() throws Exception {
        Object obj = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", String.class, null);
        JavaType type = StringType.getInstance();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedMethod method = new AnnotatedMethod(Setter.class, "set", new Class[]{String.class});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        Object instance = obj;
        property.setAndReturn(instance, "value");
    }

    @Test
    public void testAnnotations() throws Exception {
        Object obj = new Object();

        BeanPropertyDefinition propDef = new BeanPropertyDefinition("property", String.class, null);
        JavaType type = StringType.getInstance();
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = Annotations.forSerialization(new JacksonAnnotation());
        AnnotatedMethod method = new AnnotatedMethod(Setter.class, "set", new Class[]{String.class});

        SetterlessProperty property = new SetterlessProperty(propDef, type, typeDeser, contextAnnotations, method);

        assertEquals(contextAnnotations, property.getMember().getAnnotations());
    }

    private JsonParser 创建JsonParser() throws IOException {
        String content = "{\"property\":[1,2,3]}";
        JsonFactory factory = new JsonFactory();
        return factory.createParser(new ByteArrayInputStream(content.getBytes()));
    }
}