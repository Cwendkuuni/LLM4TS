package com.fasterxml.jackson.databind.deser;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyNameFactory;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestNameGenerator;

public class CreatorPropertyTest extends TestBase {

    private ObjectMapper mapper;
    private CreatorProperty property;
    private JsonDeserializer<Object> deserializer;
    private TypeDeserializer typeDeserializer;
    private Annotations contextAnnotations;
    private AnnotatedParameter param;
    private JsonParser parser;
    private DeserializationContext context;
    private Object bean;

    @Before
    public void setUp() throws Exception {
        mapper = new ObjectMapper();
        deserializer = mock(JsonDeserializer.class);
        typeDeserializer = mock(TypeDeserializer.class);
        contextAnnotations = mock(Annotations.class);
        param = mock(AnnotatedParameter.class);
        parser = mock(JsonParser.class);
        context = mock(DeserializationContext.class);

        when(context.findInjectableValue(any(Object.class), any(BeanProperty.class), any(Object.class)))
                .thenReturn(new Object());

        property = new CreatorProperty(
                new PropertyNameFactory().forProperty("name"),
                mapper.getTypeFactory().constructValueType(Object.class),
                PropertyName.construct("wrapperName"),
                typeDeserializer,
                contextAnnotations,
                param,
                0,
                "injectableValueId",
                createPropertyMetadata());
    }

    @Test
    public void testCreate() throws IOException {
        BeanProperty result = property.getInstance(mapper.getConfiguration());
        assertSame(property, result);
    }

    @Test
    public void testName() throws IOException {
        PropertyName name = property.getName();
        assertEquals("name", name.asString());
    }

    @Test
    public void testType() throws IOException {
        JavaType type = property.getType();
        assertEquals(mapper.getTypeFactory().constructValueType(Object.class), type);
    }

    @Test
    public void testSetter() throws IOException {
        SettableBeanProperty fallbackSetter = mock(SettableBeanProperty.class);
        property.setFallbackSetter(fallbackSetter);
        SettableBeanProperty result = property.getInstance(mapper.getConfiguration());
        assertEquals(fallbackSetter, result._fallbackSetter);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetter() throws IOException {
        property.deserializeAndSet(parser, context, bean);
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingSetterWithContext() throws IOException {
        property.inject(context, bean);
    }

    @Test
    public void testAnnotations() throws IOException {
        JsonProperty jsonProperty = new JsonProperty("name");
        when(param.getAnnotation(JsonProperty.class)).thenReturn(jsonProperty);

        Annotation[] annotations = property.getAnnotations();
        assertNotNull(annotations);
        assertEquals(1, annotations.length);
        assertEquals(jsonProperty, annotations[0]);
    }

    @Test
    public void testCreatorIndex() throws IOException {
        assertEquals(0, property.getCreatorIndex());
    }

    @Test
    public void testGetter() throws IOException {
        Object getResult = property.get(bean);
        assertNull(getResult);
    }

    @Test
    public void testSetterAndGet() throws IOException {
        Object setValue = new Object();
        property.set(bean, setValue);
        Object getResult = property.get(bean);
        assertSame(setValue, getResult);
    }

    @Test
    public void testGetValueDeserializer() throws IOException {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        property = new CreatorProperty(property, valueDeserializer, null);
        JsonDeserializer<Object> result = property.getValueDeserializer();
        assertSame(valueDeserializer, result);
    }

    @Test
    public void testSetValueDeserializer() throws IOException {
        JsonDeserializer<Object> valueDeserializer = mock(JsonDeserializer.class);
        property.setValueDeserializer(valueDeserializer);
        JsonDeserializer<Object> result = property.getValueDeserializer();
        assertSame(valueDeserializer, result);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object result = property.findInjectableValue(context, bean);
        assertNotNull(result);
    }

    @Test
    public void testInject() throws IOException {
        property.inject(context, bean);
        Object getResult = property.get(bean);
        assertNotNull(getResult);
    }

    @Test
    public void testToString() throws IOException {
        String result = property.toString();
        assertTrue(result.contains("[creator property, name 'name'; inject id 'injectableValueId']"));
    }

    @Test(expected = JsonMappingException.class)
    public void testMissingInjectableValueId() throws IOException {
        property = new CreatorProperty(property, null, null);
        property.inject(context, bean);
    }

    @Test
    public void testIsIgnorable() throws IOException {
        property.markAsIgnorable();
        assertTrue(property.isIgnorable());
    }

    private PropertyMetadata createPropertyMetadata() {
        return new PropertyMetadata() {
            @Override
            public boolean isValid() {
                return true;
            }

            @Override
            public void reportBadDefinition(JsonParser p, String msg) throws IOException {
                throw new IOException(msg);
            }
        };
    }
}