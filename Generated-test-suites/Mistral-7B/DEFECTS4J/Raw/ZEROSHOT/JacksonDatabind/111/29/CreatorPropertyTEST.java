package com.fasterxml.jackson.databind.deser.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.lang.annotation.Annotation;

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

public class CreatorPropertyTest extends TestBase {

    public static class MyClass {
        @JsonCreator
        public MyClass(@JsonProperty("param1") int param1, @JsonProperty("param2") String param2) {
            this.param1 = param1;
            this.param2 = param2;
        }

        @JsonProperty
        private int param1;
        @JsonProperty
        private String param2;
    }

    @org.junit.Test
    public void testCreatorProperty() throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        JsonParser p = mapper.getFactory().createParser("{\"param1\":1, \"param2\":\"test\"}");

        MyClass obj = mapper.readValue(p, MyClass.class);
        CreatorProperty property = mapper.findDeserializedProperty("param1", MyClass.class);

        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(param.getName()).thenReturn("param1");
        CreatorProperty creatorProperty = new CreatorProperty(
                new PropertyName("param1"),
                JavaType.constructSimpleType(MyClass.class, int.class),
                null,
                null,
                Annotations.empty(),
                param,
                0,
                null,
                null
        );

        assertEquals(property.getName(), "param1");
        assertEquals(property.getType(), int.class);
        assertEquals(property.getAnnotations(), Annotations.empty());
        assertEquals(property._annotated, param);
        assertEquals(property._creatorIndex, 0);
        assertEquals(property._ignorable, false);

        // test methods that should not be called for CreatorProperty
        try {
            property.fixAccess(null);
            fail("should not reach this point");
        } catch (IllegalStateException e) {
            // expected
        }
        try {
            property.setFallbackSetter(null);
            fail("should not reach this point");
        } catch (IllegalStateException e) {
            // expected
        }
        try {
            property.markAsIgnorable();
            fail("should not reach this point");
        } catch (IllegalStateException e) {
            // expected
        }

        // test injection
        DeserializationContext context = mock(DeserializationContext.class);
        Object injectedValue = new Object();
        when(context.findInjectableValue(eq(property.getInjectableValueId()), any(Object.class), any(Object.class)))
                .thenReturn(injectedValue);
        property.inject(context, new Object());
        assertEquals(property.get(new Object()), injectedValue);
    }

}