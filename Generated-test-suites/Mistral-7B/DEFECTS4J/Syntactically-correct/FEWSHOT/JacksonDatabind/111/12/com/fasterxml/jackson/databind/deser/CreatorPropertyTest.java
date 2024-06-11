package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.exc.InvalidDefinitionException;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.JsonTestUtils;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    static class TestBean {
        private int value;
        @JsonCreator
        static TestBean create(@JsonProperty("v1") int a, @JsonProperty("v2") int b) {
            return new TestBean(a, b);
        }

        TestBean(int a, int b) {
            this.value = a + b;
        }
    }

    static class TestCreatorProperty extends CreatorProperty {
        public TestCreatorProperty(PropertyName name, JavaType type, PropertyName wrapperName,
                TypeDeserializer typeDeser, Annotations contextAnnotations, AnnotatedParameter param,
                int index, Object injectableValueId, PropertyMetadata metadata) {
            super(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
        }

        @Override
        public void setFallbackSetter(SettableBeanProperty fallbackSetter) {
            super.setFallbackSetter(fallbackSetter);
        }
    }

    @Test
    public void testBasic() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("[1,2]");
        TestBean bean = mapper.readValue(p, TestBean.class);
        assertEquals(3, bean.value);
    }

    @Test
    public void testConstructorAnnotation() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestCreatorProperty property = new TestCreatorProperty(
                PropertyName.construct("foo", TestBean.class),
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                null,
                new Annotations(),
                null,
                1,
                null,
                PropertyMetadata.STD_OBJ);

        JsonParser p = mapper.getFactory().createParser("[1,2]");
        TestBean bean = mapper.readValue(p, TestBean.class);
        assertEquals(3, bean.value);

        assertEquals(property, findProperty(TestBean.class, "foo"));
    }

    @Test
    public void testNoInjectValueId() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestCreatorProperty property = new TestCreatorProperty(
                PropertyName.construct("foo", TestBean.class),
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                null,
                new Annotations(),
                null,
                1,
                null,
                PropertyMetadata.STD_OBJ);

        TestBean bean = mapper.readValue(new ObjectMapper().createArrayContext().nextValue(), TestBean.class);
        assertNull(bean);
    }

    @Test
    public void testInjectValue() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        TestInjectValueBean injectValueBean = new TestInjectValueBean();
        ObjectIdReader idReader = mock(ObjectIdReader.class);
        when(idReader.readValue(mapper.getFactory(), JsonTestUtils.MINIMAL_MAPPER_PARSER)).thenReturn(injectValueBean);
        mapper.setAnnotationIntrospector(new JacksonAnnotationIntrospector(idReader));

        TestCreatorProperty property = new TestCreatorProperty(
                PropertyName.construct("foo", TestBean.class),
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                null,
                new Annotations(),
                null,
                1,
                "injectValueId",
                PropertyMetadata.STD_OBJ);

        TestBean bean = mapper.readValue(new ObjectMapper().createArrayContext().nextValue(), TestBean.class);
        assertEquals(4, bean.value);
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        TestCreatorProperty property = new TestCreatorProperty(
                PropertyName.construct("foo", TestBean.class),
                mapper.getTypeFactory().constructType(TestBean.class),
                null,
                null,
                new Annotations(),
                null,
                1,
                null,
                PropertyMetadata.STD_OBJ);

        JsonParser p = mapper.getFactory().createParser("[1,2]");
    }
}