package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.BeanDescription;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.AnnotatedMember;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

import org.junit.Test;

public class CreatorPropertyTest extends TestBase {

    static class TestBean {

        @JsonCreator
        public TestBean(@JsonProperty("p1") int p1, @JsonProperty("p2") String p2) {}

        @JsonProperty
        int p1;

        @JsonProperty
        String p2;
    }

    static class TestBeanCreator {
        @JsonCreator
        public static TestBean create(@JsonProperty("p1") int p1, @JsonProperty("p2") String p2) {
            return new TestBean(p1, p2);
        }
    }

    @Test
    public void testBasic() throws Exception {
        BeanDescription description = mapper().introspect(TestBean.class);
        Object id = description.findInjectableValueId("p1");

        CreatorProperty property = (CreatorProperty) description.findProperty("p1");
        assertSame(id, property.getInjectableValueId());
        assertNull(property.getFallbackSetter());
    }

    @Test
    public void testWithSetter() throws Exception {
        BeanDescription description = mapper().introspect(TestBean.class);
        Object id = description.findInjectableValueId("p2");

        CreatorProperty property = (CreatorProperty) description.findProperty("p2");
        assertSame(id, property.getInjectableValueId());
        assertNull(property.getFallbackSetter());

        // test injection
        Object bean = mapper().readValue(mapper().createParser(getClass().getResource("TestBean.json")), TestBean.class);
        property.inject(mapper().getCurrentContext(), bean);
        assertEquals(1, property.get(bean).intValue());
        assertEquals("two", property.get(bean).toString());
    }

    @Test
    public void testWithCreator() throws Exception {
        BeanDescription description = mapper().introspect(TestBean.class);
        Object id = description.findInjectableValueId("p1");

        CreatorProperty property = (CreatorProperty) description.findProperty("p1");
        assertSame(id, property.getInjectableValueId());

        AnnotatedMember member = property.getMember();
        AnnotatedParameter param = (AnnotatedParameter) member;
        JsonDeserializer<TestBean> deser = (JsonDeserializer<TestBean>) param.getAnnotation(JsonDeserializer.class);
        TypeDeserializer tdeser = deser.getTypeDeserializer();
        assertSame(TestBeanCreator.class, tdeser.getAnnotation(JsonCreator.class).asType());

        // test injection
        Object bean = mapper().readValue(mapper().createParser(getClass().getResource("TestBean.json")), TestBean.class);
        property.inject(mapper().getCurrentContext(), bean);
        assertEquals(1, property.get(bean).intValue());
        assertEquals("two", property.get(bean).toString());
    }

    @Test
    public void testIgnore() throws Exception {
        BeanDescription description = mapper().introspect(TestBean.class);
        CreatorProperty property = (CreatorProperty) description.findProperty("p1");
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());

        // test injection
        Object bean = mapper().readValue(mapper().createParser(getClass().getResource("TestBean.json")), TestBean.class);
        property.inject(mapper().getCurrentContext(), bean);
        assertNull(property.get(bean));
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws Exception {
        BeanDescription description = mapper().introspect(TestBean.class);
        CreatorProperty property = (CreatorProperty) description.findProperty("p1");
        property.setFallbackSetter(null);
        property.inject(mapper().getCurrentContext(), null);
    }
}