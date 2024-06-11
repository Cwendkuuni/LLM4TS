
package com.fasterxml.jackson.databind.deser.impl;
import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.deser.NullValueProvider;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.Annotations;

public class SetterlessPropertyTest {

    @Test
    public void testDeserializeAndSet_WhenTokenIsValueNull_DoNotSetAnything() throws IOException {
        SetterlessProperty property = getProperty();
        Object instance = new Object();
        JsonParser parser = new ObjectMapper().getFactory().createParser("null");

        property.deserializeAndSet(parser, DeserializationContext.builder().build(), instance);

        assertNull(instance);
    }

    @Test
    public void testDeserializeAndSet_WhenValueTypeDeserializerIsNotNull_ThrowException() throws IOException {
        SetterlessProperty property = getPropertyWithValueTypeDeserializer();
        Object instance = new Object();
        JsonParser parser = new ObjectMapper().getFactory().createParser("value");

        try {
            property.deserializeAndSet(parser, DeserializationContext.builder().build(), instance);
        } catch (Exception e) {
            assertEquals("Problem deserializing 'setterless' property (\"propertyName\"): no way to handle typed deser with setterless yet", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_WhenValueIsNull_ThrowException() throws IOException {
        SetterlessProperty property = getProperty();
        Object instance = new Object();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{}");

        try {
            property.deserializeAndSet(parser, DeserializationContext.builder().build(), instance);
        } catch (Exception e) {
            assertEquals("Problem deserializing 'setterless' property 'propertyName': get method returned null", e.getMessage());
        }
    }

    @Test
    public void testDeserializeAndSet_WhenValueIsNotNull_DeserializeAndSet() throws IOException {
        SetterlessProperty property = getProperty();
        Object instance = new Object();
        JsonParser parser = new ObjectMapper().getFactory().createParser("{\"key\":\"value\"}");

        property.deserializeAndSet(parser, DeserializationContext.builder().build(), instance);

        assertNotNull(instance);
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSet_ThrowUnsupportedOperationException() throws IOException {
        SetterlessProperty property = getProperty();
        Object instance = new Object();

        property.set(instance, new Object());
    }

    private SetterlessProperty getProperty() {
        return new SetterlessProperty(null, null, null, null, null);
    }

    private SetterlessProperty getPropertyWithValueTypeDeserializer() {
        TypeDeserializer typeDeserializer = new TypeDeserializer() {
            @Override
            public TypeDeserializer forProperty(BeanProperty prop) {
                return null;
            }

            @Override
            public Object deserializeTypedFromAny(JsonParser p, DeserializationContext ctxt) {
                return null;
            }
        };
        return new SetterlessProperty(null, null, typeDeserializer, null, null);
    }

}
