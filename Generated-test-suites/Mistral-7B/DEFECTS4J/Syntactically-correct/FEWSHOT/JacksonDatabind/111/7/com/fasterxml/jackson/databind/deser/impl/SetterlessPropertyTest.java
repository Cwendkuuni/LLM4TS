package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import java.io.IOException;
import java.lang.reflect.Method;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.test.BaseMapTest;

public class SetterlessPropertyTest extends BaseMapTest {

    @Test
    public void testSetterlessProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass();
        JsonParser p = mapper.getFactory().createParser("[]");

        SettableBeanProperty property = mapper.findProperty("list", MyClass.class, obj);
        assertNotNull(property);

        property.deserializeAndSet(p, null, obj);
        assertNull(obj.list);

        p.nextValue(); // consume [
        p.nextValue(); // consume ]
        property.deserializeAndSet(p, null, obj);
        assertNotNull(obj.list);
        assertEquals(0, obj.list.size());

        Object newList = new ArrayList<>();
        property.deserializeAndSet(p, null, obj);
        assertNotNull(obj.list);
        assertEquals(newList, obj.list);

        property.setValueDeserializer(mapper.getFactory().createValueDeserializer(String.class));
        property.deserializeAndSet(p, null, obj);
        assertNotNull(obj.list);
        assertEquals(1, obj.list.size());
        assertEquals("test", obj.list.get(0));

        property.setValueDeserializer(null);
        property.deserializeAndSet(p, null, obj);
        assertNotNull(obj.list);
        assertEquals(1, obj.list.size());
        assertEquals(0, ((ArrayList) obj.list).size());
    }

    @Test
    public void testSetterlessPropertyWithValueSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        MyClass obj = new MyClass();
        JsonParser p = mapper.getFactory().createParser("[]");

        SettableBeanProperty property = mapper.findProperty("list", MyClass.class, obj);
        assertNotNull(property);

        property.setValueDeserializer(mapper.getFactory().createValueDeserializer(String.class));
        property.setAnnotation(new JsonSetter("test"));

        property.deserializeAndSet(p, null, obj);
        assertNotNull(obj.list);
        assertEquals(1, obj.list.size());
        assertEquals("test", obj.list.get(0));
    }

    static class MyClass {
        @JsonProperty("list")
        private List<String> list;

        public List<String> getList() {
            return list;
        }

        public void setList(List<String> list) {
            this.list = list;
        }
    }
}