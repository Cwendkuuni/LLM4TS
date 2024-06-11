package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.CreatorProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedParameter;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;
import com.fasterxml.jackson.databind.util.ClassUtil;

public class CreatorPropertyTest {

    @Mock
    private JsonParser jsonParser;

    @Mock
    private DeserializationContext deserializationContext;

    private CreatorProperty creatorProperty;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

        PropertyName name = new PropertyName("testProperty");
        JavaType type = JavaType.forType(Object.class);
        PropertyName wrapperName = new PropertyName("wrapperName");
        TypeDeserializer typeDeser = null;
        Annotations contextAnnotations = null;
        AnnotatedParameter param = null;
        int index = 0;
        Object injectableValueId = null;
        PropertyMetadata metadata = null;

        creatorProperty = new CreatorProperty(name, type, wrapperName, typeDeser, contextAnnotations, param, index, injectableValueId, metadata);
    }

    @Test
    public void testFindInjectableValue_whenInjectableValueIsNull_shouldThrowException() {
        Object beanInstance = new Object();

        try {
            creatorProperty.findInjectableValue(deserializationContext, beanInstance);
            fail("Expected JsonMappingException was not thrown");
        } catch (JsonMappingException e) {
            assertEquals("Property 'testProperty' (type com.fasterxml.jackson.databind.deser.CreatorProperty) has no injectable value id configured", e.getMessage());
        }
    }

    @Test
    public void testInject() throws IOException {
        Object beanInstance = new Object();

        creatorProperty.inject(deserializationContext, beanInstance);

        // TODO: Assert on actual injection
    }

    @Test
    public void testGetAnnotation_whenAnnotatedIsNull_shouldReturnNull() {
        AnnotatedParameter param = null;
        
        CreatorProperty creatorProperty = new CreatorProperty(new PropertyName("test"), JavaType.forType(Object.class), new PropertyName("wrapperName"),
            null, null, param, 0, null, null);

        assertNull(creatorProperty.getAnnotation(Mock.class));
    }

    @Test
    public void testGetMember() {
        assertNotNull(creatorProperty.getMember());
    }

    @Test
    public void testGetCreatorIndex() {
        assertEquals(0, creatorProperty.getCreatorIndex());
    }

    @Test
    public void testDeserializeAndSet() throws IOException {
        Object instance = new Object();
        JsonDeserializer<Object> deser = new ObjectMapper().findDeserializer(creatorProperty.getType());

        creatorProperty.deserializeAndSet(jsonParser, deserializationContext, instance);

        // TODO: Assert on actual deserialization and set
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        Object instance = new Object();
        JsonDeserializer<Object> deser = new ObjectMapper().findDeserializer(creatorProperty.getType());

        Object result = creatorProperty.deserializeSetAndReturn(jsonParser, deserializationContext, instance);

        // TODO: Assert on actual deserialization, set, and return
    }

    @Test
    public void testSet() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        creatorProperty.set(instance, value);

        // TODO: Assert on actual set
    }

    @Test
    public void testSetAndReturn() throws IOException {
        Object instance = new Object();
        Object value = new Object();

        Object result = creatorProperty.setAndReturn(instance, value);

        // TODO: Assert on actual set and return
    }

    @Test
    public void testGetInjectableValueId() {
        assertNull(creatorProperty.getInjectableValueId());
    }

    @Test
    public void testToString() {
        assertEquals("[creator property, name 'testProperty'; inject id 'null']", creatorProperty.toString());
    }

    private void _verifySetter() throws IOException {
        if (creatorProperty._fallbackSetter == null) {
            _reportMissingSetter(null, null);
        }
    }

    private void _reportMissingSetter(JsonParser p, DeserializationContext ctxt) throws IOException {
        final String msg = "No fallback setter/field defined for creator property 'testProperty'";

        if (ctxt != null) {
            ctxt.reportBadDefinition(creatorProperty.getType(), msg);
        } else {
            throw InvalidDefinitionException.from(p, msg, creatorProperty.getType());
        }
    }
}