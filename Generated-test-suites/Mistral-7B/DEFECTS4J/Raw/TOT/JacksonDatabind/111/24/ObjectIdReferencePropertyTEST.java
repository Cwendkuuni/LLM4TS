package com.fasterxml.jackson.databind.deser.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationConfig;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectReader;
import com.fasterxml.jackson.databind.json.JsonMapperFactory;
import com.fasterxml.jackson.databind.ser.impl.SimpleBeanPropertyFilter;
import com.fasterxml.jackson.databind.util.ClassUtil;
import com.fasterxml.jackson.databind.util.TestBase;
import com.fasterxml.jackson.databind.util.TestUtil;

import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import static org.testng.Assert.*;

public class ObjectIdReferencePropertyTest extends TestBase
{
    private ObjectMapper mapper;
    private ObjectIdReferenceProperty property;
    private ObjectIdReferenceProperty forwardProperty;

    @BeforeClass
    public void setup() throws Exception
    {
        mapper = JsonMapperFactory.getMapper();
        ObjectReader reader = mapper.readerForClass(DummyClass.class);
        property = (ObjectIdReferenceProperty) reader.findProperty("property1");
        forwardProperty = (ObjectIdReferenceProperty) reader.findProperty("property2");
    }

    @Test
    public void testDeserializeAndSet() throws Exception
    {
        JsonParser p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": {\"$ref\": \"#/properties/property1\"}}");
        Object deserialized = property.deserializeAndSet(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertSame(deserialized, property.getBean().getProperty1());

        Object deserializedForward = forwardProperty.deserializeAndSet(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertSame(deserializedForward, property.getBean().getProperty1());
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception
    {
        JsonParser p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": {\"$ref\": \"#/properties/property1\"}}");
        Object deserialized = property.deserializeSetAndReturn(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertNull(deserialized);
        Assert.assertSame(property.getBean().getProperty1(), property.getBean().getProperty2());

        p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        deserialized = property.deserializeSetAndReturn(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertNull(deserialized);
        Assert.assertNull(property.getBean().getProperty2());
    }

    @Test
    public void testGetAnnotation() throws Exception
    {
        Assert.assertNull(property.getAnnotation(MyAnnotation.class));
        Assert.assertNull(forwardProperty.getAnnotation(MyAnnotation.class));

        property = new ObjectIdReferenceProperty(forwardProperty, new JsonDeserializer<String>() {}, null);
        Assert.assertNull(property.getAnnotation(MyAnnotation.class));

        property = new ObjectIdReferenceProperty(forwardProperty, PropertyName.valueOf("newName"));
        Assert.assertNull(property.getAnnotation(MyAnnotation.class));
    }

    @Test
    public void testFixAccess() throws Exception
    {
        property.fixAccess(mapper.getConfig());
        forwardProperty.fixAccess(mapper.getConfig());

        Assert.assertNotNull(property.getBean());
        Assert.assertNotNull(forwardProperty.getBean());
    }

    @Test
    public void testSet() throws Exception
    {
        Object original = property.getBean();
        Object newValue = new Object();

        property.set(original, newValue);
        Assert.assertSame(newValue, property.getBean().getProperty1());
        Assert.assertNotSame(original, property.getBean());

        property.set(original, null);
        Assert.assertNull(property.getBean().getProperty1());
        Assert.assertSame(original, property.getBean());
    }

    @Test
    public void testSetAndReturn() throws Exception
    {
        Object original = property.getBean();
        Object newValue = new Object();

        Object returned = property.setAndReturn(original, newValue);
        Assert.assertSame(newValue, property.getBean().getProperty1());
        Assert.assertNotNull(returned);

        Object returnedNull = property.setAndReturn(original, null);
        Assert.assertNull(property.getBean().getProperty1());
        Assert.assertNull(returnedNull);
    }

    @Test
    public void testErrorScenario() throws Exception
    {
        ObjectMapper mapperWithError = JsonMapperFactory.getMapperWithConfig(new DeserializationConfig().withErrorHandling(ErrorHandlingPolicy.FAIL_ON_EMPTY_BEAN_FIELD));
        ObjectReader readerWithError = mapperWithError.readerForClass(DummyClass.class);

        JsonParser p = mapperWithError.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        try {
            Object deserialized = readerWithError.readValue(p, DummyClass.class);
            fail("Expected JsonMappingException");
        } catch (JsonMappingException e) {
            // expected
        }
    }

    @Test
    public void testDeserializeWithNullProvider() throws Exception
    {
        ObjectMapper mapperWithNullProvider = JsonMapperFactory.builder()
                .addNullValueProvider(nullValueProvider())
                .build();
        ObjectReader readerWithNullProvider = mapperWithNullProvider.readerForClass(DummyClass.class);

        JsonParser p = mapperWithNullProvider.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        Object deserialized = property.deserializeAndSet(p, new DeserializationContext(mapperWithNullProvider.getConfig()), null);
        Assert.assertNull(deserialized);
        Assert.assertNull(property.getBean().getProperty2());
    }

    @Test
    public void testDeserializeWithValueDeserializer() throws Exception
    {
        JsonDeserializer<String> deserializer = new SimpleStringDeserializer();
        ObjectReader readerWithDeserializer = mapper.readerForClass(DummyClass.class)
                .withValueDeserializer(deserializer);

        JsonParser p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": \"value\"}");
        Object deserialized = property.deserializeAndSet(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertEquals("value", property.getBean().getProperty2());
        Assert.assertSame(property.getBean(), deserialized);

        JsonParser pNull = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        deserialized = property.deserializeAndSet(pNull, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertNull(deserialized);
        Assert.assertNull(property.getBean().getProperty2());
    }

    @Test
    public void testJsonDeserializer() throws Exception
    {
        JsonDeserializer<DummyClass> deserializer = new DummyDeserializer();
        ObjectReader readerWithDeserializer = mapper.readerForClass(DummyClass.class)
                .withValueDeserializer(deserializer);

        JsonParser p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        Object deserialized = property.deserializeAndSet(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertNull(deserialized);
        Assert.assertNull(property.getBean().getProperty2());

        p = mapper.getFactory().createParser("{\"property1\": 1, \"property2\": \"value\"}");
        deserialized = property.deserializeAndSet(p, new DeserializationContext(mapper.getConfig()), null);
        Assert.assertEquals(new DummyClass("value"), property.getBean());
        Assert.assertSame(property.getBean(), deserialized);
    }

    @Test
    public void testSettableBeanProperty() throws Exception
    {
        ObjectMapper mapperWithNullProvider = JsonMapperFactory.builder()
                .addNullValueProvider(nullValueProvider())
                .build();
        ObjectReader readerWithNullProvider = mapperWithNullProvider.readerForClass(DummyClass.class);

        JsonParser p = mapperWithNullProvider.getFactory().createParser("{\"property1\": 1, \"property2\": null}");
        Object deserialized = readerWithNullProvider.readValue(p, DummyClass.class);

        Object id = TestUtil.createSimpleValueId(DummyClass.class, "property2");
        DeserializationContext ctxt = new DeserializationContext(mapperWithNullProvider.getConfig());

        PropertyReferring referencingProperty = new PropertyReferring(property, null, DummyClass.class, deserialized);
        ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyType.PROPERTY, ClassUtil.makeReferenceType(DummyClass.class, "property2"), id);
        PropertyName propertyName = PropertyName.valueOf("property2");
        ObjectIdReferenceProperty propertyWithId = new ObjectIdReferenceProperty(property, null, objectIdInfo, propertyName);

        propertyWithId.deserializeAndSet(p, ctxt, deserialized);
        Object updatedDeserialized = readerWithNullProvider.readValue(p, DummyClass.class);
        Assert.assertSame(updatedDeserialized, deserialized);
        Assert.assertNotNull(updatedDeserialized.getProperty2());

        referencingProperty.handleResolvedForwardReference(id, new Object());
        Assert.assertSame(new Object(), updatedDeserialized.getProperty2());
    }

    private static JsonDeserializer<String> simpleStringDeserializer()
    {
        return new SimpleStringDeserializer();
    }

    private static JsonDeserializer<DummyClass> dummyDeserializer()
    {
        return new DummyDeserializer();
    }

    private static NullValueProvider nullValueProvider()
    {
        return new StandardNullValueProvider();
    }

    static class DummyClass
    {
        private Integer property1;
        private String property2;

        public DummyClass(String property2)
        {
            this.property1 = 1;
            this.property2 = property2;
        }

        public Integer getProperty1()
        {
            return property1;
        }

        public String getProperty2()
        {
            return property2;
        }
    }

    static class SimpleStringDeserializer extends JsonDeserializer<String>
    {
        @Override
        public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
        {
            return jsonParser.getText();
        }
    }

    static class DummyDeserializer extends JsonDeserializer<DummyClass>
    {
        @Override
        public DummyClass deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException
        {
            Object id = TestUtil.createSimpleValueId(DummyClass.class, "property2");
            DeserializationContext ctxt = new DeserializationContext(deserializationContext);

            PropertyReferring referencingProperty = new PropertyReferring(property, null, DummyClass.class, jsonParser.getCodec().readValue(jsonParser, DummyClass.class));
            ObjectIdInfo objectIdInfo = new ObjectIdInfo(PropertyType.PROPERTY, ClassUtil.makeReferenceType(DummyClass.class, "property2"), id);
            PropertyName propertyName = PropertyName.valueOf("property2");
            ObjectIdReferenceProperty propertyWithId = new ObjectIdReferenceProperty(property, null, objectIdInfo, propertyName);
            return propertyWithId.deserializeAndSet(jsonParser, ctxt, jsonParser.getCodec().readValue(jsonParser, DummyClass.class));
        }
    }
}