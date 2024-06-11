package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

import java.io.IOException;
import java.util.*;

public class ExternalTypeHandlerTest {

    @SuppressWarnings("unchecked")
    private static <T> T createTestInstance(Class<T> clazz) throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        TypeDeserializer typeDeserializer = mapper.getTypeDeserializers().findValueDeserializer(clazz);
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExternalTypeHandler.Builder()
                .addExternal(new TestBeanProperty(), typeDeserializer)
                .build());
        return (T) handler;
    }

    @Test
    public void testStart() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        ExternalTypeHandler started = handler.start();
        assertEquals(handler, started);
    }

    @Test
    public void testHandleTypePropertyValue() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"typeProperty\":\"testTypeId\"}");
        DeserializationContext ctxt = createMockContext();
        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "typeProperty", testBean));
        assertEquals("testTypeId", handler._typeIds[0]);
    }

    @Test
    public void testHandleTypePropertyValue_nullbean() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        JsonParser jp = createMockJsonParserFor("{\"typeProperty\":\"testTypeId\"}");
        DeserializationContext ctxt = createMockContext();
        assertTrue(handler.handleTypePropertyValue(jp, ctxt, "typeProperty", null));
        assertNull(handler._typeIds);
    }

    @Test
    public void testHandleTypePropertyValue_noProperty() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"nonExistentProperty\":\"testTypeId\"}");
        DeserializationContext ctxt = createMockContext();
        assertFalse(handler.handleTypePropertyValue(jp, ctxt, "nonExistentProperty", testBean));
        assertNull(handler._typeIds);
    }

    @Test
    public void testHandlePropertyValue() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"property\":\"value\"}");
        DeserializationContext ctxt = createMockContext();
        assertTrue(handler.handlePropertyValue(jp, ctxt, "property", testBean));
        assertNotNull(handler._tokens[0]);
    }

    @Test
    public void testHandlePropertyValue_noProperty() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"nonExistentProperty\":\"value\"}");
        DeserializationContext ctxt = createMockContext();
        assertFalse(handler.handlePropertyValue(jp, ctxt, "nonExistentProperty", testBean));
        assertNull(handler._tokens);
    }

    @Test
    public void testComplete() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"property\":\"value\", \"typeProperty\":\"testTypeId\"}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        Object result = handler.complete(jp, ctxt, testBean);
        assertEquals(testBean, result);
        assertEquals("testTypeId", handler._typeIds[0]);
    }

    @Test
    public void testComplete_nullBean() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        JsonParser jp = createMockJsonParserFor("{\"property\":\"value\", \"typeProperty\":\"testTypeId\"}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        assertNull(handler.complete(jp, ctxt, null));
        assertNull(handler._typeIds);
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_missingTypeIdProperty() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"property\":\"value\"}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        handler.complete(jp, ctxt, testBean);
    }

    @Test(expected = JsonMappingException.class)
    public void testComplete_missingProperty() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"typeProperty\":\"testTypeId\"}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        handler.complete(jp, ctxt, testBean);
    }

    @Test
    public void testComplete_missingBoth() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        handler.complete(jp, ctxt, testBean);
    }

    @Test
    public void testComplete_nullTypeId() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        handler.complete(jp, ctxt, testBean);
        assertNull(handler._typeIds);
    }

    @Test
    public void testComplete_builder() throws Exception {
        Object testBean = new Object();
        Class<TestBuilder> testBuilderClass = TestBuilder.class;
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        JsonParser jp = createMockJsonParserFor("{\"property1\":\"value1\", \"property2\":\"value2\"}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        PropertyBasedCreator creator = new TestBuilder();
        Object result = handler.complete(jp, ctxt, creator, testBean);
        assertEquals(testBean, result);
        assertEquals("value1", handler._properties[0].getProperty().getName());
        assertEquals("value2", handler._properties[1].getProperty().getName());
    }

    @Test
    public void testDeserialize() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"typeProperty\":\"testTypeId\", \"property\":1}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        assertEquals(1, handler._deserialize(jp, ctxt, 0, "testTypeId").getClass().getSimpleName());
    }

    @Test
    public void testDeserialize_nullTypeId() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        assertNull(handler._deserialize(jp, ctxt, 0, null));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{\"typeProperty\":\"testTypeId\", \"property\":1}")
                .getMockParser();
        DeserializationContext ctxt = createMockContext();
        handler._deserializeAndSet(jp, ctxt, testBean, 0, "testTypeId");
        assertEquals(1, handler._properties[0].getProperty().get(testBean).getClass().getSimpleName());
    }

    @Test
    public void testDeserializeAndSet_nullTypeId() throws Exception {
        ExternalTypeHandler handler = createTestInstance(ExternalTypeHandler.class);
        Object testBean = new Object();
        JsonParser jp = createMockJsonParserFor("{}")
                .getMockParser();
        handler._deserializeAndSet(jp, createMockContext(), testBean, 0, null);
        assertNull(handler._properties[0].getProperty().get(testBean));
    }

    private JsonParser createMockJsonParserFor(String jsonString) {
        return Mockito.mock(JsonParser.class, new MockitoMockitoOptions().lenient());
    }

    private DeserializationContext createMockContext() {
        return Mockito.mock(DeserializationContext.class, new MockitoMockitoOptions().lenient());
    }

    private static class TestBeanProperty extends SettableBeanProperty {
        TestBeanProperty(String name) { super(TestBean.class, name); }
    }

    private static class TestBean {
        @JsonProperty
        private int property;

        public int getProperty() {
            return property;
        }

        public void setProperty(int property) {
            this.property = property;
        }
    }

    private static class TestBuilder {
        public TestBuilder() {
            // no-op
        }

        public TestBean build(DeserializationContext ctxt, PropertyValueBuffer buffer)
                throws IOException {
            TestBean testBean = new TestBean();
            SettableBeanProperty property1 = new TestBeanProperty("property1");
            SettableBeanProperty property2 = new TestBeanProperty("property2");
            property1.set(testBean, "value1");
            property2.set(testBean, "value2");
            return testBean;
        }
    }
}

class TestBuilderTest {
    @Test
    public void testBuild() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        ObjectWriter writer = mapper.writer();
        String jsonString = writer.writeValueAsString(new TestBuilder().build(null, null));
        JsonParser jp = mapper.readerForValueType(TestBean.class).readValue(JsonFactory.class, jsonString);
        TestBean testBean = jp.nextValue().asObject();
        assertEquals("value1", testBean.getProperty1());
        assertEquals("value2", testBean.getProperty2());
    }
}