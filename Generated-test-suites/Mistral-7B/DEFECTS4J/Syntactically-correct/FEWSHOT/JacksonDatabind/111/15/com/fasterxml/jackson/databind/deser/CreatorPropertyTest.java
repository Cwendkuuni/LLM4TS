package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.util.TestUtils.constructParser;
import static org.junit.Assert.*;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

import java.io.IOException;
import java.lang.annotation.Annotation;

public class CreatorPropertyTest extends TestBase {

    static class TestBean {
        @JsonCreator
        public TestBean(@JsonProperty("str") String str, @JsonProperty("num") int num) {
            this.str = str;
            this.num = num;
        }

        String str;
        int num;
    }

    @Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty property = mapper.findProperty("str", TestBean.class);
        assertNotNull(property);
        assertEquals("str", property.getName());
        assertNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(0, property._creatorIndex);
        assertFalse(property._ignorable);

        property = mapper.findProperty("num", TestBean.class);
        assertNotNull(property);
        assertEquals("num", property.getName());
        assertNotNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(1, property._creatorIndex);
        assertFalse(property._ignorable);

        Object id = new Object(){};
        property = mapper.findProperty("num", TestBean.class, id);
        assertNotNull(property);
        assertEquals("num", property.getName());
        assertNotNull(property._annotated);
        assertEquals(id, property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(1, property._creatorIndex);
        assertFalse(property._ignorable);

        property = mapper.findProperty("ignorable", TestBean.class);
        assertNotNull(property);
        assertEquals("ignorable", property.getName());
        assertNotNull(property._annotated);
        assertNull(property._injectableValueId);
        assertNull(property._fallbackSetter);
        assertEquals(-1, property._creatorIndex);
        assertTrue(property._ignorable);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();

        TestBean bean = mapper.readValue(constructParser("{\"str\":\"hello\", \"num\":42}").readValueAsTree(), TestBean.class);
        CreatorProperty property = mapper.findProperty("num", TestBean.class);
        assertNotNull(property);

        Object injectableValue = property.findInjectableValue(context, bean);
        assertNull(injectableValue);

        PropertyName idName = PropertyName.construct("injectId");
        Object injectId = new Object(){};
        property = mapper.findProperty("num", TestBean.class, idName, injectId);
        assertNotNull(property);
        assertNotNull(property._injectableValueId);
        assertEquals(injectId, property._injectableValueId);

        Object injectValue = mapper.findInjectableValue(context, bean);
        assertEquals(42, injectValue);
        property.inject(context, bean);
        assertEquals(42, bean.num);
    }
}