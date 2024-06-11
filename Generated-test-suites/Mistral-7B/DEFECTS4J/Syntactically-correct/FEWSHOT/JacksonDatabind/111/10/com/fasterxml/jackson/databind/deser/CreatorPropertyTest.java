package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonInject;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;

public class CreatorPropertyTest extends TestBase {

    @Test
    public void testCreatorProperty() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        assertEquals("name", property.getName().getShortName());
        assertEquals(type, property.getType());
        assertNull(property.getFallbackSetter());
        assertEquals(0, property.getCreatorIndex());
        assertFalse(property.isIgnorable());
        assertSame(param, property._annotated);
        assertSame(injectableValueId, property._injectableValueId);
    }

    @Test
    public void testSetFallbackSetter() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        assertEquals(fallbackSetter, property._fallbackSetter);
    }

    @Test
    public void testIsIgnorable() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingFallbackSetter() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        JsonParser parser = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        property.deserializeAndSet(parser, ctxt, null);
    }

    @Test
    public void testInject() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        JsonDeserializer<String> deser = mock(JsonDeserializer.class);
        when(deser.deserialize(any(JsonParser.class), any(DeserializationContext.class))).thenReturn("value");
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        JsonParser parser = mock(JsonParser.class);
        Object beanInstance = new Object() {};
        when(parser.isExpectedStartArrayValue()).thenReturn(false);
        when(parser.getValueAsToken()).thenReturn(contextSource.createValueInstance("value"));
        DeserializationContext ctxt = mock(DeserializationContext.class);
        when(ctxt.findInjectableValue(anyObject(), eq(property), anyObject())).thenReturn("injectedValue");
        property.inject(ctxt, beanInstance);
        assertEquals("injectedValue", property.get(beanInstance));
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        JavaType type = mapType(String.class, new TypeReference<String>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        Object injectableValueId = new Object() {};
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, injectableValueId, null
        );
        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object beanInstance = new Object() {};
        when(ctxt.findInjectableValue(eq(injectableValueId), any(BeanProperty.class), anyObject())).thenReturn("value");
        assertEquals("value", property.findInjectableValue(ctxt, beanInstance));
    }
    
    @JsonInject
    public static class MyBean {
        private String name;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
    
    @Test
    public void testCreatePropertyWithInjectAnnotation() throws IOException {
        JavaType type = mapType(MyBean.class, new TypeReference<MyBean>() {});
        AnnotatedMember member = mock(AnnotatedMember.class);
        AnnotatedParameter param = mock(AnnotatedParameter.class);
        when(member.getAnnotations()).thenReturn(new Annotations());
        when(member.getMember()).thenReturn(member);
        when(param.getParameterName()).thenReturn("name");
        CreatorProperty property = new CreatorProperty(
                new PropertyName("name"), type, null, null, null, param, 0, null, null
        );
        assertSame(member, property.getMember());
        assertEquals("name", property.getName().getShortName());
        assertEquals(type, property.getType());
        assertNull(property.getFallbackSetter());
        assertSame(param, property._annotated);
        assertNull(property._injectableValueId);
        assertEquals(0, property.getCreatorIndex());
        assertFalse(property.isIgnorable());
    }
}