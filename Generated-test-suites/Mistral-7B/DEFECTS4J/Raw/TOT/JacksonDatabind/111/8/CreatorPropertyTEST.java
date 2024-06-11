package com.fasterxml.jackson.databind.deser;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.junit.jupiter.api.*;
import org.mockito.Mockito;
import org.testng.Assert;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;

import static org.mockito.Mockito.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CreatorPropertyTest {

    private ObjectMapper objectMapper;
    private DeserializationContext context;
    private CreatorProperty creatorProperty;

    @BeforeAll
    public void setup() throws Exception {
        objectMapper = new ObjectMapper();
        context = mock(DeserializationContext.class);
        creatorProperty = new CreatorProperty(
                PropertyName.construct("testName"),
                JavaType.constructRawList(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                Mockito.mock(AnnotatedParameter.class),
                0,
                null,
                PropertyMetadata.empty());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        String json = "['test']";
        ObjectNode node = ObjectMapper.getDefaultInstance().readValue(json, ObjectNode.class);

        when(context.findInjectableValue(any(), any(), any())).thenReturn(null);
        when(context.findValueDeserializer(creatorProperty.getType()))
                .thenReturn(Mockito.mock(JsonDeserializer.class));
        when(context.findValueDeserializer(JavaType.constructRawList(String.class)))
                .thenReturn(Mockito.mock(JsonDeserializer.class));
        when(context.handleNonWritableProperty(any(), any(), any(), any(), any()))
                .thenReturn(null);

        Object instance = new Object();

        creatorProperty.deserializeAndSet(Mockito.mock(JsonParser.class), context, instance);

        verify(context, times(1)).handleNonWritableProperty(eq(instance.getClass()), eq(creatorProperty), eq(context), any(), any());
        verify(context, times(1)).findValueDeserializer(JavaType.constructRawList(String.class));
        Assert.assertEquals(Arrays.asList("test"), creatorProperty.get(instance));
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        when(context.findInjectableValue(_eq(creatorProperty), any()))
                .thenReturn("testValue");

        Object instance = new Object();

        creatorProperty.inject(context, instance);

        verify(instance, times(1)).set(any(), eq("testValue"));
    }

    @Test
    public void testMissingSetter() throws IOException {
        creatorProperty = new CreatorProperty(
                PropertyName.construct("testName"),
                JavaType.constructRawList(String.class),
                PropertyName.construct("testWrapperName"),
                null,
                Annotations.empty(),
                Mockito.mock(AnnotatedParameter.class),
                0,
                null,
                PropertyMetadata.empty());

        try {
            creatorProperty.setFallbackSetter(null);
            creatorProperty.inject(context, new Object());
        } catch (NullPointerException e) {
            Assert.assertEquals("No fallback setter/field defined for creator property 'testName'", e.getMessage());
        }
    }
}