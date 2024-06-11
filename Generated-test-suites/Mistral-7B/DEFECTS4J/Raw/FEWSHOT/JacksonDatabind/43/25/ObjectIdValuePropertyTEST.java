package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.core.util.TestUtil.constructParser;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonId;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.JsonDeserializer.None;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyName;
import com.fasterxml.jackson.databind.PropertyResolver;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.BeanProperty;
import com.fasterxml.jackson.databind.deser.std.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotationIntrospector;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.type.TypeFactory;

public class ObjectIdValuePropertyTest {

    @Test
    public void testObjectIdValueProperty() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        PropertyName idName = PropertyName.construct("id");
        ObjectIdReader idReader = new ObjectIdReaderForString();
        ObjectIdValueProperty idProp = new ObjectIdValueProperty(idReader, PropertyMetadata.empty());

        assertEquals(idName, idProp.getName());
        assertEquals(idReader.getIdType(), idProp.getIdType());
        assertNull(idProp.getDeserializer());
        assertEquals(idReader, idProp._objectIdReader);

        BeanPropertyDefinition idPropDef = new AnnotationIntrospector().findPropertyDefinition(idProp, mapper());
        assertEquals(idName, idPropDef.getName());
        assertEquals(Object.class, idPropDef.getType());
        assertEquals(JsonId.class, idPropDef.getAnnotation(JsonId.class));
        assertEquals(BeanProperty.Standard.DESERIALIZER, idPropDef.getBeanPropertyType());

        assertSame(idProp, idProp.withName(idName));
        assertSame(idProp, idProp.withValueDeserializer(new None()));
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String id = "123";
        ObjectIdReader idReader = new ObjectIdReaderForString();
        ObjectIdValueProperty idProp = new ObjectIdValueProperty(idReader, PropertyMetadata.empty());

        JsonDeserializer<String> idDeserializer = mock(JsonDeserializer.class);
        when(idDeserializer.deserialize(constructParser(null), mock(DeserializationContext.class), null)).thenReturn(id);
        idProp = new ObjectIdValueProperty(idProp, idDeserializer);

        DeserializationContext ctxt = mock(DeserializationContext.class);
        Object instance = mock(Object.class);

        idProp.deserializeAndSet(constructParser(id), ctxt, instance);

        AnnotatedMember member = mock(AnnotatedMember.class);
        PropertyResolver resolver = mock(PropertyResolver.class);
        when(ctxt.findProperty(idName)).thenReturn(member);
        when(member.getAnnotation(JsonDeserialize.class)).thenReturn(new JsonDeserialize.With(idDeserializer));
        when(member.getChildPropertyNames()).thenReturn(new String[0]);
        when(member.hasAnnotation(JsonId.class)).thenReturn(true);
        when(resolver.getResolvedName(idName)).thenReturn(idName);

        PropertyName idPropertyName = PropertyName.construct("idProperty");
        ObjectIdReader idPropertyReader = new ObjectIdReaderForString();
        Object idProperty = new ObjectIdValueProperty(idPropertyReader, PropertyMetadata.empty());
        when(idProperty.getName()).thenReturn(idPropertyName);
        when(idProperty.getIdType()).thenReturn(String.class);
        when(idProperty.getDeserializer()).thenReturn(idDeserializer);

        when(ctxt.findObjectId(id, idReader, resolver)).thenReturn(new ReadableObjectId() {
            @Override
            public void bindItem(Object value) {
                // no-op
            }

            @Override
            public ObjectId getId() {
                return id;
            }
        });

        when(mapper.findPropertyValue("instance", idPropertyName)).thenReturn(id);

        Object result = idProp.deserializeAndSet(constructParser(id), ctxt, instance);
        assertSame(instance, result);
    }

    @Test
    public void testSetAndReturn() throws Exception {
        ObjectMapper mapper = new ObjectMapper();
        mapper.configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);

        String id = "123";
        ObjectIdReader idReader = new ObjectIdReaderForString();
        ObjectIdValueProperty idProp = new ObjectIdValueProperty(idReader, PropertyMetadata.empty());

        JsonDeserializer<String> idDeserializer = mock(JsonDeserializer.class);
        when(idDeserializer.deserialize(constructParser(null), mock(DeserializationContext.class), null)).thenReturn(id);
        idProp = new ObjectIdValueProperty(idProp, idDeserializer);

        Object instance = new Object() {
            private String id;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }
        };

        Object result = idProp.setAndReturn(instance, id);
        assertSame(instance, result);
        assertEquals(id, ((Object) result).getId());
    }
}