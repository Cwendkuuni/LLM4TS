package com.fasterxml.jackson.databind.deser;

import static com.fasterxml.jackson.core.util.TestUtil.constructParser;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

import java.io.IOException;

public class CreatorPropertyTest extends TestBase {
    static class TestBean {
        @JsonCreator
        public TestBean(@JsonProperty("a") int a, @JsonProperty("b") String b) {}

        @JsonProperty
        int a;
        @JsonProperty
        String b;
    }

    static class Creator {
        @JsonCreator
        public static TestBean create(@JsonProperty("a") int a, @JsonProperty("b") String b) {
            return new TestBean(a, b);
        }
    }

    static class TestCreatorProperty extends CreatorProperty {
        public TestCreatorProperty(PropertyName name, TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param, int index, Object injectableValueId, PropertyMetadata metadata) {
            super(name, null, null, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        }
    }

    @Test
    public void testBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = constructParser(mapper, "[1,\"foo\"]");

        TestCreatorProperty prop = mapper.findCreatorProperty(" creatorProperty", TestCreatorProperty.class, TestBean.class, null, null);
        assertSame(TestBean.class, prop.getType());

        TestBean bean = mapper.deserializeValue(p, TestBean.class);
        assertSame(1, bean.a);
        assertEquals("foo", bean.b);

        assertFalse(prop._fallbackSetter.isIgnorable());
        assertFalse(prop._ignorable);
    }

    @Test
    public void testNullValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = constructParser(mapper, "null");

        TestCreatorProperty prop = mapper.findCreatorProperty(" creatorProperty", TestCreatorProperty.class, TestBean.class, null, null);
        assertSame(TestBean.class, prop.getType());

        TestBean bean = mapper.deserializeValue(p, TestBean.class);
        assertNull(bean.a);
        assertNull(bean.b);

        assertFalse(prop._fallbackSetter.isIgnorable());
        assertFalse(prop._ignorable);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = constructParser(mapper, "[1,\"foo\"]");

        TestCreatorProperty prop = mapper.findCreatorProperty(" creatorProperty", TestCreatorProperty.class, TestBean.class, null, null);
        prop.markAsIgnorable();

        TestBean bean = mapper.deserializeValue(p, TestBean.class);
        // This should throw an exception
        prop.inject(mapper.getDeserializationContext(), bean);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter2() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = constructParser(mapper, "[1,\"foo\"]");

        TestCreatorProperty prop = mapper.findCreatorProperty(" creatorProperty", TestCreatorProperty.class, TestBean.class, null, null);
        prop._verifySetter();
    }

    @Test
    public void testInject() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = constructParser(mapper, "[null, \"foo\"]");

        TestCreatorProperty prop = mapper.findCreatorProperty(" creatorProperty", TestCreatorProperty.class, TestBean.class, null, null);
        prop._injectableValueId = "injectValue";

        TestBean bean = mapper.readValue(p, TestBean.class);
        assertEquals("foo", bean.b);

        TestBean injectedBean = new TestBean();
        Object injectedValue = new Object();
        AnnotatedMember injectMember = mapper.findInjectableValue("injectValue", TestBean.class);
        mapper.injectValues(mapper.getDeserializationContext(), injectedBean, injectMember);

        prop.inject(mapper.getDeserializationContext(), injectedBean);
        assertEquals(injectedValue, bean.a);
    }
}