package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedClass;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException, InstantiationException, IllegalAccessException, InvocationTargetException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mapper.readerForMap().readValueAs(ObjectIdReader.class);

        PropertyName propertyName = PropertyName.construct("id");
        BeanPropertyDefinition def = BeanPropertyDefinition.stdDefine(propertyName, String.class, null);
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType("class Test");
        AnnotatedMember member = AnnotatedMember.Std.simpleValue(annotatedClass, "id");

        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, def);

        JsonParser p = constructParser("{\"id\": 1}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object instance = mapper.readValue(p, Object.class);
        assertEquals(1, property.deserializeAndSet(p, ctxt, instance).longValue());
    }

    @Test
    public void testDeserializeAndSetNull() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mapper.readerForMap().readValueAs(ObjectIdReader.class);

        PropertyName propertyName = PropertyName.construct("id");
        BeanPropertyDefinition def = BeanPropertyDefinition.stdDefine(propertyName, String.class, null);
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType("class Test");
        AnnotatedMember member = AnnotatedMember.Std.simpleValue(annotatedClass, "id");

        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, def);

        JsonParser p = constructParser("{}");
        DeserializationContext ctxt = mapper.getDeserializationContext();

        Object instance = mapper.readValue(p, Object.class);
        assertNull(property.deserializeAndSet(p, ctxt, instance));
    }

    @Test
    public void testSet() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        ObjectIdReader idReader = mapper.readerForMap().readValueAs(ObjectIdReader.class);

        PropertyName propertyName = PropertyName.construct("id");
        BeanPropertyDefinition def = BeanPropertyDefinition.stdDefine(propertyName, String.class, null);
        AnnotatedClass annotatedClass = mapper.getTypeFactory().constructType("class Test");
        AnnotatedMember member = AnnotatedMember.Std.simpleValue(annotatedClass, "id");

        ObjectIdValueProperty property = new ObjectIdValueProperty(idReader, def);

        Object instance = new Test();
        property.set(instance, 1);
        assertEquals(1, ((Test) instance).getId());
    }

    @Test
    public void testAnnotations() {
        JsonDeserialize jsonDeserialize = new JsonDeserialize();
        jsonDeserialize.using(ObjectIdValueProperty.class);

        JsonId idAnnotation = new JsonId();
        idAnnotation.value("id");

        assertEquals(jsonDeserialize, idAnnotation.getAnnotation(JsonDeserialize.class.getDeclaredField("value")));
        assertEquals("id", idAnnotation.value());
    }

    @JsonDeserialize(using = ObjectIdValueProperty.class)
    static class Test {
        @JsonId("id")
        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return ToStringSerializer.instance.serializeValueAsString(this);
        }
    }
}