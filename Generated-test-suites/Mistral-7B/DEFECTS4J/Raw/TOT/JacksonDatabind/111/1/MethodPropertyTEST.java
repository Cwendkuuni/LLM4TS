package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

import static com.fasterxml.jackson.databind.util.TestUtil.createMinimalObjectMapper;
import static org.junit.Assert.*;

public class MethodPropertyTest {

    private ObjectMapper objectMapper;
    private SettableBeanProperty methodProperty;

    @Before
    public void setup() throws Exception {
        objectMapper = createMinimalObjectMapper();
        AnnotatedMethod annotatedMethod = mock(AnnotatedMethod.class);
        Method setterMethod = mock(Method.class);
        Annotation ann = mock(Annotation.class);
        methodProperty = new MethodProperty(
                new BeanPropertyDefinition("testProperty", int.class),
                JavaType.of(int.class), null, null, annotatedMethod);
        methodProperty._setter = setterMethod;
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        when(annotatedMethod.getAnnotated()).thenReturn(ann);
        when(setterMethod.invoke(any(), any())).then(invocation -> invocation.getArguments()[0] = 42);

        JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\": 42}");
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());
        assertEquals(42, parser.getValueAsInt());

        verify(setterMethod).invoke(any(), eq(42));
        parser.close();
    }

    @Test
    public void testDeserializeSetAndReturn() throws JsonProcessingException {
        when(annotatedMethod.getAnnotated()).thenReturn(ann);
        when(setterMethod.invoke(any(), any())).then(invocation -> invocation.getArguments()[0] = 42);

        Object result = methodProperty.deserializeSetAndReturn(parser, objectMapper.getDeserializationContext(), new Object());
        assertEquals(42, result);

        verify(setterMethod).invoke(any(), eq(42));
        parser.close();
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeAndSetNull() throws JsonProcessingException {
        when(annotatedMethod.getAnnotated()).thenReturn(ann);

        JsonParser parser = objectMapper.getFactory().createParser("null");
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());
        parser.close();

        fail("Expected JsonProcessingException but got none");
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeAndSetInvalidType() throws JsonProcessingException {
        when(annotatedMethod.getAnnotated()).thenReturn(ann);
        when(setterMethod.invoke(any(), any())).then(invocation -> invocation.getArguments()[0] = "invalidType");

        JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\":\"invalidType\"}");
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());
        parser.close();

        fail("Expected JsonProcessingException but got none");
    }

    @Test(expected = JsonProcessingException.class)
    public void testDeserializeAndSetThrowingException() throws JsonProcessingException {
        when(annotatedMethod.getAnnotated()).thenReturn(ann);
        doThrow(new RuntimeException("test exception"))
                .when(setterMethod).invoke(any(), any());

        JsonParser parser = objectMapper.getFactory().createParser("{\"testProperty\": 42}");
        methodProperty.deserializeAndSet(parser, objectMapper.getDeserializationContext(), new Object());
        parser.close();

        fail("Expected JsonProcessingException but got none");
    }
}