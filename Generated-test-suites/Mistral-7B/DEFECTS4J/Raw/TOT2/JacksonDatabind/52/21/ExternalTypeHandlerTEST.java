import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.Builder;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private Builder builder;

    @Before
    public void setup() throws IOException {
        // Setup test data
        JsonFactory jsonFactory = new JsonFactory();
        ObjectMapper objectMapper = new ObjectMapper(jsonFactory);
        builder = new Builder();
        SettableBeanProperty prop1 = new SettableBeanProperty("prop1", String.class);
        SettableBeanProperty prop2 = new SettableBeanProperty("prop2", Integer.class);
        TypeDeserializer typeDeserializer1 = objectMapper.getDeserializerFactory().findValueDeserializer(String.class);
        TypeDeserializer typeDeserializer2 = objectMapper.getDeserializerFactory().findValueDeserializer(Integer.class);

        // Add properties to handler
        builder.addExternal(prop1, typeDeserializer1);
        builder.addExternal(prop2, typeDeserializer2);
        handler = builder.build();
    }

    @Test
    public void testHandleTypePropertyValueNormal() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\"}");
        Object bean = new Object();
        Assert.assertTrue("handleTypePropertyValue should return true", handler.handleTypePropertyValue(parser, null, "prop1", bean));
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));
    }

    @Test
    public void testHandleTypePropertyValueMissingProperty() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"missingProperty\":\"test\"}");
        Object bean = new Object();
        Assert.assertFalse("handleTypePropertyValue should return false when property is missing", handler.handleTypePropertyValue(parser, null, "missingProperty", bean));
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test
    public void testHandleTypePropertyValueMissingTypeId() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":1}");
        Object bean = new Object();
        Assert.assertTrue("handleTypePropertyValue should return true when type id is missing", handler.handleTypePropertyValue(parser, null, "prop1", bean));
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test
    public void testHandlePropertyValueNormal() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\", \"prop2\":42}");
        Object bean = new Object();
        Assert.assertTrue("handlePropertyValue should return true when property has a value", handler.handlePropertyValue(parser, null, "prop1", bean));
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));

        Assert.assertTrue("handlePropertyValue should return true when property has no type id", handler.handlePropertyValue(parser, null, "prop2", bean));
        Assert.assertEquals("prop2 should have been set to 42", 42, prop2.get(bean));
    }

    @Test
    public void testHandlePropertyValueMissingProperty() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"missingProperty\":\"test\"}");
        Object bean = new Object();
        Assert.assertFalse("handlePropertyValue should return false when property is missing", handler.handlePropertyValue(parser, null, "missingProperty", bean));
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test
    public void testHandlePropertyValueMissingValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\", \"prop2\":\"\"}");
        Object bean = new Object();
        Assert.assertTrue("handlePropertyValue should return true when property has no value", handler.handlePropertyValue(parser, null, "prop2", bean));
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test
    public void testCompleteNormal() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\", \"prop2\":42}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.handlePropertyValue(parser, null, "prop2", bean);
        handler.complete(parser, null, bean);
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));
        Assert.assertEquals("prop2 should have been set to 42", 42, prop2.get(bean));
    }

    @Test
    public void testCompleteMissingProperties() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\"}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.complete(parser, null, bean);
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));
        Assert.assertNull("bean should not have any other property set", prop2.get(bean));
    }

    @Test
    public void testCompleteMissingTypeId() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\", \"prop2\":\"test\"}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.handlePropertyValue(parser, null, "prop2", bean);
        handler.complete(parser, null, bean);
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));
        Assert.assertNull("bean should not have any other property set", prop2.get(bean));
    }

    @Test
    public void testCompleteNullValues() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":null, \"prop2\":null}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.handlePropertyValue(parser, null, "prop2", bean);
        handler.complete(parser, null, bean);
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test
    public void testCompleteMissingTypeIdAndValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":null, \"prop2\":\"test\"}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.handlePropertyValue(parser, null, "prop2", bean);
        handler.complete(parser, null, bean);
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertEquals("prop2 should have been set to \"test\"", "test", prop2.get(bean));
    }

    @Test
    public void testCompleteNullProperties() throws IOException {
        JsonParser parser = jsonFactory.createParser("{}");
        Object bean = new Object();
        handler.complete(parser, null, bean);
        Assert.assertNull("bean should not have any property set", prop1.get(bean));
        Assert.assertNull("bean should not have any property set", prop2.get(bean));
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingTypeIdAndNoValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\"}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.complete(parser, null, bean);
        Assert.assertEquals("prop1 should have been set to \"test\"", "test", prop1.get(bean));
        // Expected exception: Missing external type id property 'prop2'
    }

    @Test(expected = IOException.class)
    public void testCompleteNoTypeIdAndValue() throws IOException {
        JsonParser parser = jsonFactory.createParser("{\"prop1\":\"test\", \"prop2\":42}");
        Object bean = new Object();
        handler.handlePropertyValue(parser, null, "prop1", bean);
        handler.complete(parser, null, bean);
        // Expected exception: Missing property 'prop2' for external type id ''
    }
}