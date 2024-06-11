package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.jsontype.*;
import com.fasterxml.jackson.databind.util.*;
import org.junit.Test;

public class ExternalTypeHandlerTest {

    @Test
    public void testHandleTypePropertyValue() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "typeProperty";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHandleTypePropertyValue_NullBean() throws IOException {
        // Arrange
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "typeProperty";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_InvalidPropertyName() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"invalidPropertyName\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "invalidPropertyName";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_InvalidTypeId() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"InvalidId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "typeProperty";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_NoTokenBuffer() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], null);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "typeProperty";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandleTypePropertyValue_NoTypeId() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), null, new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "typeProperty";

        // Act
        boolean result = handler.handleTypePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property\":\"Value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "property";

        // Act
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testHandlePropertyValue_NullBean() throws IOException {
        // Arrange
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property\":\"Value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "property";

        // Act
        boolean result = handler.handlePropertyValue(p, ctxt, propName, null);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_InvalidPropertyName() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"invalidPropertyName\":\"Value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "invalidPropertyName";

        // Act
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_NoTokenBuffer() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), null, new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property\":\"Value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "property";

        // Act
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testHandlePropertyValue_NoTypeId() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property\":\"Value\"}");
        DeserializationContext ctxt = new TestDeserializationContext();
        String propName = "property";

        // Act
        boolean result = handler.handlePropertyValue(p, ctxt, propName, bean);

        // Assert
        assertTrue(result);
    }

    @Test
    public void testComplete() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property1\":\"Value1\",\"property2\":\"Value2\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        // Act
        Object result = handler.complete(p, ctxt, bean);

        // Assert
        assertEquals(bean.getClass(), result.getClass());
    }

    @Test
    public void testComplete_NullBean() throws IOException {
        // Arrange
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property1\":\"Value1\",\"property2\":\"Value2\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        // Act
        Object result = handler.complete(p, ctxt, null);

        // Assert
        assertNull(result);
    }

    @Test
    public void testComplete_MissingTypeId() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdScalarValueProperty("property1"), null)}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"property2\":\"Value2\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        // Act and Assert
        try {
            handler.complete(p, ctxt, bean);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals("Missing property 'property1' for external type id 'null'", e.getMessage());
        }
    }

    @Test
    public void testComplete_MissingProperty() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdScalarValueProperty("property1"), new SimpleStringTypeDeserializer(null))}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        JsonParser p = new TestJsonFactory().createParser("{\"typeProperty\":\"TypeId\"}");
        DeserializationContext ctxt = new TestDeserializationContext();

        // Act and Assert
        try {
            handler.complete(p, ctxt, bean);
            fail("Expected MappingException");
        } catch (MappingException e) {
            assertEquals("Missing property 'property1' for external type id 'TypeId'", e.getMessage());
        }
    }

    @Test
    public void testCompleteWithCreator() throws IOException {
        // Arrange
        Object bean = new Object();
        ExternalTypeHandler handler = new ExternalTypeHandler(new ExtTypedProperty[]{new ExtTypedProperty(new StdScalarValueProperty("property1"), new SimpleStringTypeDeserializer(null))}, new HashMap<>(), new String[0], new TokenBuffer[0]);
        PropertyValueBuffer buffer = new PropertyValueBuffer();
        PropertyBasedCreator creator = new ObjectCreator();

        // Act
        Object result = handler.complete(p, ctxt, buffer, creator);

        // Assert
        assertEquals(creator.build(ctxt, bean), result);
    }
}

class TestJsonFactory extends JsonFactory {
    @Override
    public JsonParser createParser(String contentInUtf8) throws IOException {
        return new StringInput(new ByteArrayInputStream(contentInUtf8.getBytes()), 0, contentInUtf8.length());
    }
}

class TestDeserializationContext extends DeserializationContextBase {
    @Override
    public JsonParser _parser() {
        return new JsonToken() {};
    }
}

class ObjectCreator extends BaseJsonDeserializer<Object> {
    @Override
    public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new Object();
    }
}

class StdScalarValueProperty extends SettableBeanProperty.Std {
    public StdScalarValueProperty(String name) {
        super(null, null, null, null, null);
        this.name = name;
    }
}

class SimpleStringTypeDeserializer extends TypeDeserializer {
    public SimpleStringTypeDeserializer(String propertyName) {
        super(String.class, propertyName);
    }

    @Override
    public Object deserializeFromObject(JsonParser p, DeserializationContext ctxt) throws IOException {
        return p.getValueAsString();
    }
}