package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.TestBase;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatorPropertyTest extends TestBase {

    @Test
    public void testCreatorPropertyWithNoInjectId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        CreatorProperty cp = new CreatorProperty(
                new PropertyName("test"),
                JavaType.constructUntypedClass("java.lang.String"),
                null,
                null,
                null,
                null,
                0,
                null,
                null);
        cp.markAsIgnorable();

        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(ctxt.findInjectableValue(null, cp, instance)).thenReturn(null);

        cp.inject(ctxt, instance);

        assertNull(cp.getName(), instance.getClass().getField("test"));
    }

    @Test
    public void testCreatorPropertyWithInjectId() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object injectableValue = new Object();

        Class<?> cls = ClassUtil.classOf(Object.class);
        JsonDeserialize injectAnnotation = new JsonDeserialize();
        injectAnnotation.setContentAsStream(false);
        injectAnnotation.setId("testId");

        CreatorProperty cp = new CreatorProperty(
                new PropertyName("test"),
                JavaType.constructUntypedClass("java.lang.String"),
                null,
                null,
                new Annotations(new Annotation[]{injectAnnotation}),
                null,
                0,
                "testId",
                null);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        when(ctxt.findInjectableValue("testId", cp, instance)).thenReturn(injectableValue);

        cp.inject(ctxt, instance);

        assertEquals(injectableValue, cp.getName(), instance.getClass().getField("test").get(instance));
    }

    @Test
    public void testCreatorPropertyWithFallbackSetter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Object injectableValue = new Object();

        CreatorProperty cp = new CreatorProperty(
                new PropertyName("test"),
                JavaType.constructUntypedClass("java.lang.String"),
                null,
                null,
                null,
                null,
                0,
                null,
                new SettableBeanProperty() {
                    @Override
                    public void set(Object instance, Object value) throws IOException {
                        assertEquals("expected value", value, cp.getName(), instance);
                    }

                    @Override
                    public Object setAndReturn(Object instance, Object value) throws IOException {
                        assertEquals("expected value", value, cp.getName(), instance);
                        return value;
                    }
                });

        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();

        cp.setFallbackSetter(cp);

        when(ctxt.findInjectableValue(null, cp, instance)).thenReturn(null);

        JsonParser p = mock(JsonParser.class);
        when(p.getValueAsToken()).thenReturn(createValueToken("expected value"));

        cp.deserializeAndSet(p, ctxt, instance);

        assertEquals("expected value", cp.getName(), instance.getClass().getField("test").get(instance));
    }

    @Test
    public void testCreatorPropertyMissingSetter() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();

        CreatorProperty cp = new CreatorProperty(
                new PropertyName("test"),
                JavaType.constructUntypedClass("java.lang.String"),
                null,
                null,
                null,
                null,
                0,
                null,
                null);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = new Object();
        ObjectParser parser = createObjectMapper().treeAsTokens(createValue("{\"test\": \"value\"}"));

        cp.deserializeAndSet(parser.getParser(), ctxt, instance);

        cp._reportMissingSetter(null, ctxt);
    }
}