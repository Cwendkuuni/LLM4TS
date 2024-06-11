package com.fasterxml.jackson.databind.deser.impl;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonTypeInfo.As;
import com.fasterxml.jackson.core.*;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.*;
import com.fasterxml.jackson.databind.deser.impl.ExternalTypeHandler.ExtTypedProperty;
import com.fasterxml.jackson.databind.jsontype.TypeIdResolver;
import com.fasterxml.jackson.databind.util.TokenBuffer;

public class ExternalTypeHandlerTest {

    private Object createTestBean() {
        return new Object() {
            // Empty for test
        };
    }

    @SuppressWarnings("unchecked")
    private TypeDeserializer createTypeDeserializerMock(String typeId) {
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);
        when(typeDeserializer.getTypeIdResolver()).thenReturn(mock(TypeIdResolver.class));
        when(typeDeserializer.getTypeId()).thenReturn(typeId);
        when(typeDeserializer.getDefaultImpl()).thenReturn(null);
        return typeDeserializer;
    }

    @SuppressWarnings("resource")
    @Test
    public void testHandleTypePropertyValue() throws IOException {
        Object bean = createTestBean();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "typeProperty";
        String typeId = "testTypeId";

        List<ExtTypedProperty> properties = new ArrayList<>();
        ExtTypedProperty prop1 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(typeId));
        properties.add(prop1);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), new String[0], null).start();

        when(jp.getText()).thenReturn(typeId);

        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        // Test when type id is not present
        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), new String[0], null).start();
        when(jp.getText()).thenReturn("");
        handler.handleTypePropertyValue(jp, ctxt, propName, bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);
    }

    @SuppressWarnings("resource")
    @Test
    public void testHandlePropertyValue() throws IOException {
        Object bean = createTestBean();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        String propName = "property";

        List<ExtTypedProperty> properties = new ArrayList<>();
        ExtTypedProperty prop1 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(""));
        properties.add(prop1);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), new String[0], null).start();

        when(jp.getText()).thenReturn("testValue");

        handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._typeIds[0]);
        assertNull(handler._tokens[0]);

        // Test when type id is present
        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), new String[0], null).start();
        String typeId = "testTypeId";
        when(jp.getText()).thenReturn(typeId);
        handler.handlePropertyValue(jp, ctxt, propName, bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens[0]);
        assertSame(typeId, handler._typeIds[0]);

        // Test when property is missing
        handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), new String[0], null).start();
        String missingPropName = "missingProperty";
        handler.handlePropertyValue(jp, ctxt, missingPropName, bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);
    }

    @SuppressWarnings("resource")
    @Test
    public void testComplete() throws IOException {
        Object bean = createTestBean();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        List<ExtTypedProperty> properties = new ArrayList<>();
        ExtTypedProperty prop1 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(""));
        ExtTypedProperty prop2 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(""));
        properties.add(prop1);
        properties.add(prop2);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null).start();

        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.getText()).thenReturn("prop1");
        handler.complete(jp, ctxt, bean);
        SettableBeanProperty prop1 = properties.get(0).getProperty();
        assertSame(bean, prop1.getBase());
        assertNull(prop1.getValue());

        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("testValue1");
        handler.handlePropertyValue(jp, ctxt, prop1.getName(), bean);
        assertSame(bean, handler.getHandler());
        assertSame("testValue1", prop1.getValue());
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("prop2");
        handler.complete(jp, ctxt, bean);
        SettableBeanProperty prop2 = properties.get(1).getProperty();
        assertSame(bean, prop2.getBase());
        assertNull(prop2.getValue());

        when(jp.nextToken()).thenReturn(JsonToken.START_ARRAY);
        handler.complete(jp, ctxt, bean);
        assertSame(bean, handler.getHandler());

        when(jp.nextToken()).thenReturn(JsonToken.VALUE_STRING);
        when(jp.getText()).thenReturn("testValue2");
        handler.handlePropertyValue(jp, ctxt, prop2.getName(), bean);
        assertSame(bean, handler.getHandler());
        assertSame("testValue2", prop2.getValue());
        assertNull(handler._tokens[1]);
        assertNull(handler._typeIds[1]);

        when(jp.nextToken()).thenReturn(JsonToken.END_ARRAY);
        handler.complete(jp, ctxt, bean);
        assertSame(bean, handler.getHandler());

        when(creator.build(ctxt, buffer)).thenReturn(new Object());
        handler.complete(jp, ctxt, buffer, creator);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens);
        assertNull(handler._typeIds);

        Object creatorBean = handler.getHandler();
        assertSame(bean, creatorBean);
    }

    @SuppressWarnings("resource")
    @Test
    public void testCompleteWithNullValues() throws IOException {
        Object bean = createTestBean();
        JsonParser jp = mock(JsonParser.class);
        DeserializationContext ctxt = mock(DeserializationContext.class);
        PropertyBasedCreator creator = mock(PropertyBasedCreator.class);
        PropertyValueBuffer buffer = mock(PropertyValueBuffer.class);

        List<ExtTypedProperty> properties = new ArrayList<>();
        ExtTypedProperty prop1 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(""));
        ExtTypedProperty prop2 = new ExtTypedProperty(mock(SettableBeanProperty.class), createTypeDeserializerMock(""));
        properties.add(prop1);
        properties.add(prop2);

        ExternalTypeHandler handler = new ExternalTypeHandler(properties.toArray(new ExtTypedProperty[0]),
                new HashMap<>(), null, null).start();

        when(jp.nextToken()).thenReturn(JsonToken.START_OBJECT);
        when(jp.getText()).thenReturn("prop1");
        handler.complete(jp, ctxt, bean);
        SettableBeanProperty prop1 = properties.get(0).getProperty();
        assertSame(bean, prop1.getBase());
        assertNull(prop1.getValue());

        when(jp.nextToken()).thenReturn(JsonToken.NULL);
        handler.handlePropertyValue(jp, ctxt, prop1.getName(), bean);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens[0]);
        assertNull(handler._typeIds[0]);

        when(jp.nextToken()).thenReturn(JsonToken.FIELD_NAME);
        when(jp.getText()).thenReturn("prop2");
        handler.complete(jp, ctxt, bean);
        SettableBeanProperty prop2 = properties.get(1).getProperty();
        assertSame(bean, prop2.getBase());
        assertNull(prop2.getValue());

        when(jp.nextToken()).thenReturn(JsonToken.NULL);
        handler.complete(jp, ctxt, bean);
        assertSame(bean, handler.getHandler());

        when(creator.build(ctxt, buffer)).thenReturn(new Object());
        handler.complete(jp, ctxt, buffer, creator);
        assertSame(bean, handler.getHandler());
        assertNull(handler._tokens);
        assertNull(handler._typeIds);

        Object creatorBean = handler.getHandler();
        assertSame(bean, creatorBean);
    }

    @Test
    public void testBuilder() {
        ObjectBeanFactory beanFactory = mock(ObjectBeanFactory.class);
        JsonDeserializer<Object> deserializer = mock(JsonDeserializer.class);
        ObjectMapper mapper = mock(ObjectMapper.class);
        TypeDeserializer typeDeserializer = mock(TypeDeserializer.class);

        when(beanFactory.createAnnotationIntrospector()).thenReturn(mock(AnnotationIntrospector.class));
        when(beanFactory.findValueDeserializer(eq("java.lang.String"), any()))
                .thenReturn(deserializer);
        when(mapper.deserializationConfig()).thenReturn(mock(DeserializationConfig.class));
        when(mapper.getTypeFactory()).thenReturn(mock(TypeFactory.class));
        when(mapper.findValueDeserializer(eq(String.class), any())).thenReturn(deserializer);
        when(mapper.findTypeDeserializer(eq(TypeDeserializer.class), any()))
                .thenReturn(typeDeserializer);

        ExternalTypeHandler.Builder builder = new ExternalTypeHandler.Builder();
        SettableBeanProperty property1 = mock(SettableBeanProperty.class);
        SettableBeanProperty property2 = mock(SettableBeanProperty.class);
        builder.addExternal(property1, typeDeserializer);
        builder.addExternal(property2, typeDeserializer);

        ExternalTypeHandler handler = builder.build();
        assertSame(property1.getType(), handler._properties[0].getProperty().getType());
        assertSame(property2.getType(), handler._properties[1].getProperty().getType());
    }
}