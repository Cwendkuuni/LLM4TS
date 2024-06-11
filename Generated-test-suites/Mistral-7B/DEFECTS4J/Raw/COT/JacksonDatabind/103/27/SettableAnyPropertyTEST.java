package com.fasterxml.jackson.databind.deser.test;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableAnyProperty;
import com.fasterxml.jackson.databind.deser.impl.ReadableObjectId;
import com.fasterxml.jackson.databind.introspect.AnnotatedField;
import com.fasterxml.jackson.databind.introspect.AnnotatedMethod;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.ClassUtil;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class SettableAnyPropertyTest {

    private SettableAnyProperty _testProperty;

    @Before
    public void setup() throws Exception {
        BeanProperty property = new BeanProperty(new AnnotatedField(getClass(), "testProperty"), null);
        AnnotatedMember setter = new AnnotatedMethod(getClass(), "setTestProperty", new Class[0]);
        JavaType type = ObjectMapper.getDefaultInstance().getTypeFactory().constructMapType(Map.class, String.class, String.class);
        KeyDeserializer keyDeserializer = new SimpleKeyDeserializer();
        JsonDeserializer<Object> valueDeserializer = new StdDeserializer<>();
        TypeDeserializer valueTypeDeserializer = ObjectMapper.getDefaultInstance().getTypeFactory().findValueTypeDeserializer(String.class, null);
        _testProperty = new SettableAnyProperty(property, setter, type, keyDeserializer, valueDeserializer, valueTypeDeserializer);
    }

    @Test
    public void testConstructor() {
        Assert.assertNotNull(_testProperty);
    }

    @Test
    public void testSetterIsField() {
        Assert.assertEquals(false, _testProperty._setterIsField);
    }

    @Test
    public void testHasValueDeserializer() {
        Assert.assertFalse(_testProperty.hasValueDeserializer());
    }

    @Test
    public void testGetType() {
        Assert.assertNotNull(_testProperty.getType());
    }

    @Test
    public void testDeserializeAndSetBasic() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        String json = "{\"key\":\"value\"}";
        Map<String, String> testMap = new HashMap<>();

        _testProperty.deserializeAndSet(mapper.readTree(json), new TestContext(), new Object(), "testProperty");

        Assert.assertEquals("value", _testProperty.getProperty().get(new Object()));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testDeserializeAndSetOnNullInstance() throws IOException {
        _testProperty.deserializeAndSet(null, new TestContext(), null, "testProperty");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetNullKey() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        _testProperty.deserializeAndSet(mapper.readTree("{\"testProperty\":null}"), new TestContext(), new Object(), null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testDeserializeAndSetInvalidJson() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        _testProperty.deserializeAndSet(mapper.readTree("{\"testProperty\":"), new TestContext(), new Object(), "testProperty");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullValueDeserializer() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":\"value\"}");
        _testProperty = new SettableAnyProperty(_testProperty.getProperty(), _testProperty._setter,
                _testProperty.getType(), null, null, null);
        _testProperty.deserializeAndSet(parser, new TestContext(), new Object(), "testProperty");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNullTypeDeserializer() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonParser parser = mapper.getFactory().createParser("{\"key\":1}");
        _testProperty = new SettableAnyProperty(_testProperty.getProperty(), _testProperty._setter,
                _testProperty.getType(), null, null, null);
        _testProperty.deserializeAndSet(parser, new TestContext(), new Object(), "testProperty");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNonJsonInput() throws IOException {
        _testProperty = new SettableAnyProperty(_testProperty.getProperty(), _testProperty._setter,
                _testProperty.getType(), null, null, null);
        _testProperty.deserializeAndSet(new StringReader("test"), new TestContext(), new Object(), "testProperty");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithInvalidPropertyName() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonParser parser = mapper.getFactory().createParser("{\"testPropery\":\"value\"}");
        _testProperty = new SettableAnyProperty(_testProperty.getProperty(), _testProperty._setter,
                _testProperty.getType(), null, null, null);
        _testProperty.deserializeAndSet(parser, new TestContext(), new Object(), "testPropery");
    }

    @Test(expected = IOException.class)
    public void testDeserializeWithNonStringKey() throws IOException {
        ObjectMapper mapper = ObjectMapper.getDefaultInstance();
        JsonParser parser = mapper.getFactory().createParser("{\"1\":\"value\"}");
        _testProperty = new SettableAnyProperty(_testProperty.getProperty(), _testProperty._setter,
                _testProperty.getType(), null, null, null);
        _testProperty.deserializeAndSet(parser, new TestContext(), new Object(), "testProperty");
    }

    @Test
    public void testSetWithNullMap() {
        Map<String, String> testMap = null;
        try {
            _testProperty.set(new Object(), "testKey", "testValue");
            Assert.fail();
        } catch (NullPointerException e) {
            // expected
        }
    }

    @Test
    public void testSetWithNullValue() {
        Map<String, String> testMap = new HashMap<>();
        try {
            _testProperty.set(new Object(), "testKey", null);
            Assert.fail();
        } catch (IllegalArgumentException e) {
            // expected
        }
    }

    @Test
    public void testSetWithStringValue() throws IOException {
        Map<String, String> testMap = new HashMap<>();
        _testProperty.set(new Object(), "testKey", "testValue");
        Assert.assertEquals("testValue", _testProperty.getProperty().get("testKey"));
    }

    @Test
    public void testSetWithObjectValue() throws IOException {
        Map<String, Object> testMap = new HashMap<>();
        _testProperty.set(new Object(), "testKey", new Object());
        Assert.assertNotNull(_testProperty.getProperty().get("testKey"));
    }

    private static class TestContext extends DeserializationContext {
        @Override public DeserializationConfig getConfig() { return null; }

        @Override public JsonFactory getFactory() { return null; }

        @Override public ObjectReader getRootValueFactory() { return null; }

        @Override public ObjectReader getValueIntrinsics() { return null; }

        @Override public ObjectReader getValueHandler() { return null; }
    }
}