package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class CreatorPropertyTest {

    private CreatorProperty property;
    private ObjectMapper mapper;
    private DeserializationContext context;
    private JsonDeserializer<String> stringDeserializer;

    @Before
    public void setup() throws Exception {
        mapper = new ObjectMapper();
        context = mock(DeserializationContext.class);
        stringDeserializer = mock(JsonDeserializer.class);
        property = new CreatorProperty(
                new PropertyName("testProperty"),
                mapper.getTypeFactory().constructType(String.class),
                null,
                null,
                null,
                Mockito.mock(AnnotatedParameter.class),
                0,
                null,
                null
        );
        when(context.findInjectableValue(any(), any(), any())).thenReturn((Object) "injectedValue");
        when(context.findValueDeserializer(mapper.getTypeFactory().constructType(String.class)))
                .thenReturn(stringDeserializer);
    }

    @Test
    public void testFindInjectableValue() throws IOException {
        Object injectedValue = property.findInjectableValue(context, null);
        assertNotNull(injectedValue);
        assertEquals("injectedValue", injectedValue);
    }

    @Test
    public void testInject() throws IOException {
        Object beanInstance = new Object();
        property.inject(context, beanInstance);
        Object value = property.get(beanInstance);
        assertEquals("injectedValue", value);
    }

    @Test
    public void testEmptyConstructor() throws IOException {
        property = new CreatorProperty(null, null, null, null, null, null, 0, null, null);
        assertNull(property.getInjectableValueId());
    }

    @Test(expected = InvalidDefinitionException.class)
    public void testMissingSetter() throws IOException {
        property.deserializeAndSet(Mockito.mock(JsonParser.class), context, null);
    }

    @Test
    public void testSetValueDeserializer() {
        JsonDeserializer<Integer> intDeserializer = mock(JsonDeserializer.class);
        property = new CreatorProperty(property, intDeserializer, null);
        assertEquals(intDeserializer, property.getValueDeserializer());
    }

    @Test
    public void testSetNullProvider() {
        NullValueProvider nullValueProvider = mock(NullValueProvider.class);
        property = new CreatorProperty(property, null, nullValueProvider);
        assertEquals(nullValueProvider, property.getNullProvider());
    }

    @JsonDeserialize(using = JsonBigDecimalDeserializer.class)
    static class TestBean {
        private String testProperty;

        public String getTestProperty() {
            return testProperty;
        }

        @JsonDeserialize(using = JsonStringDeserializer.class)
        public void setTestProperty(String testProperty) {
            this.testProperty = testProperty;
        }
    }

    @Test
    public void testDeserializeAndSet() throws JsonProcessingException {
        PropertyName wrapperName = new PropertyName("testBean");
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        property = new CreatorProperty(
                new PropertyName("testProperty"),
                mapper.getTypeFactory().constructType(TestBean.class),
                wrapperName,
                typeDeserializer,
                null,
                Mockito.mock(AnnotatedParameter.class),
                0,
                null,
                null
        );

        TestBean testBean = new TestBean();
        String json = "{\"testBean\":{\"testProperty\":\"testValue\"}}";
        ObjectMapper objectMapper = new ObjectMapper();
        JsonParser jsonParser = objectMapper.getFactory().createParser(json);

        property.deserializeAndSet(jsonParser, context, testBean);

        assertEquals("testValue", testBean.getTestProperty());
    }
}