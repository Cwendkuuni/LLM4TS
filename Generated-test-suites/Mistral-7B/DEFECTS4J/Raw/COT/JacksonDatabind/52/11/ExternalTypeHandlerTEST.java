package com.example.externaltypehandler;

import static org.junit.Assert.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;

import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private ExternalTypeHandler handler;
    private Object bean;
    private JsonParser mockParser;
    private DeserializationContext mockContext;

    @Before
    public void setUp() throws Exception {
        TypeDeserializer typeDeserializerMock = Mockito.mock(TypeDeserializer.class);
        SettableBeanProperty propertyMock = Mockito.mock(SettableBeanProperty.class);

        List<ExtTypedProperty> properties = new ArrayList<>();
        ExtTypedProperty extProperty1 = new ExtTypedProperty(propertyMock, typeDeserializerMock);
        ExtTypedProperty extProperty2 = new ExtTypedProperty(propertyMock, typeDeserializerMock);
        properties.add(extProperty1);
        properties.add(extProperty2);

        Map<String, Integer> nameToPropertyIndex = new HashMap<>();
        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]), nameToPropertyIndex, null, null);
        bean = Mockito.mock(Object.class);

        mockParser = Mockito.mock(JsonParser.class);
        mockContext = Mockito.mock(DeserializationContext.class);
    }

    @Test
    public void testHandleTypePropertyValueTrue() throws IOException {
        String propName = "propName";
        String typeId = "typeId";
        Mockito.when(mockParser.getText()).thenReturn(typeId);
        Mockito.when(mockParser.hasNextElement()).thenReturn(true);
        Mockito.when(mockParser.nextElement()).thenReturn(Mockito.mock(JsonToken.class));
        Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);

        Mockito.verify(mockParser).nextToken();
        Mockito.verify(mockParser).nextToken();
        Mockito.verify(mockParser).nextToken();
        Mockito.verify(mockParser, times(2)).getText();
    }

    @Test
    public void testHandleTypePropertyValueFalse() throws IOException {
        String propName = "propName";
        Integer index = 0;
        Mockito.when(mockParser.getText()).thenReturn("propName");

        handler.handleTypePropertyValue(mockParser, mockContext, propName, bean);

        Mockito.verify(mockParser, never()).skipChildren();
    }

    @Test
    public void testHandlePropertyValueTrue() throws IOException {
        String propName = "propName";
        Integer index = 0;
        String typeId = "typeId";
        Mockito.when(mockParser.getText()).thenReturn(typeId);
        Mockito.when(mockParser.hasNextElement()).thenReturn(true);
        Mockito.when(mockParser.nextElement()).thenReturn(Mockito.mock(JsonToken.class));
        Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);

        handler.handlePropertyValue(mockParser, mockContext, propName, bean);

        Mockito.verify(mockParser, never()).getText();
        Mockito.verify(mockParser).skipChildren();
    }

    @Test
    public void testHandlePropertyValueFalse() throws IOException {
        String propName = "propName";
        Integer index = 0;

        handler.handlePropertyValue(mockParser, mockContext, propName, bean);

        Mockito.verify(mockParser, never()).getText();
        Mockito.verify(mockParser).skipChildren();
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingExternalTypeId() throws IOException {
        String propName = "propName";

        handler.complete(mockParser, mockContext, bean);

        Mockito.verify(mockContext).reportMappingException("Missing external type id property 'propName'");
    }

    @Test(expected = IOException.class)
    public void testCompleteMissingPropertyForExternalTypeId() throws IOException {
        String propName = "propName";
        String typeId = "typeId";

        Mockito.when(mockParser.getText()).thenReturn(typeId);
        Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.END_ARRAY);

        handler.complete(mockParser, mockContext, bean);

        Mockito.verify(mockContext).reportMappingException("Missing property 'propName' for external type id 'typeId'");
    }

    @Test
    public void testComplete() throws IOException {
        String propName = "propName";
        String typeId = "typeId";

        Mockito.when(mockParser.getText()).thenReturn(typeId);
        Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.START_OBJECT);
        Mockito.when(mockParser.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        Mockito.when(mockParser.getText()).thenReturn(propName);

        handler.complete(mockParser, mockContext, bean);

        Mockito.verify(mockParser, times(2)).getText();
        Mockito.verify(mockParser).nextToken();
        Mockito.verify(mockParser).nextToken();
        Mockito.verify(mockParser).nextToken();
        Mockito.verifyNoMoreInteractions(mockParser);
    }
}

class ExtTypedProperty {
    private static class TestTypeDeserializer extends TypeDeserializer {
        @Override
        public Object deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            return "defaultValue";
        }

        @Override
        public void reset(JsonDeserializer deserializer) {}

        @Override
        public JsonDeserializer createContextual(DeserializationContext ctxt) {
            return null;
        }

        @Override
        public String getPropertyName() {
            return "testPropertyName";
        }

        @Override
        public String getTypeId() {
            return "testTypeId";
        }
    }

    private SettableBeanProperty property;
    private TypeDeserializer typeDeserializer;
    private String typePropertyName;
    private SettableBeanProperty typeProperty;

    public ExtTypedProperty(SettableBeanProperty property, TypeDeserializer typeDeserializer) {
        this.property = property;
        this.typeDeserializer = typeDeserializer;
        this.typePropertyName = typeDeserializer.getPropertyName();
    }

    public boolean hasTypePropertyName(String n) {
        return n.equals(typePropertyName);
    }

    public boolean hasDefaultType() {
        return false;
    }

    public String getDefaultTypeId() {
        return null;
    }

    public String getTypePropertyName() {
        return typePropertyName;
    }

    public SettableBeanProperty getProperty() {
        return property;
    }
}