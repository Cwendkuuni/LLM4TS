package com.fasterxml.jackson.databind.deser.impl;

import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.NONE;
import static com.fasterxml.jackson.annotation.JsonAutoDetect.Visibility.PUBLIC_ONLY;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.AnnotationIntrospector;
import com.fasterxml.jackson.databind.BeanProperty;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.ObjectIdReader;
import com.fasterxml.jackson.databind.PropertyMetadata;
import com.fasterxml.jackson.databind.deser.SettableBeanProperty;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.node.ObjectIdReader.IdType;

public class ObjectIdValuePropertyTest {

    @Test
    public void testDeserializeAndSet() throws IOException {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(IdType.INT_ID);
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return this;
            }
        };

        JsonDeserializer<Object> mockValueDeserializer = mock(JsonDeserializer.class);

        DeserializationContext mockCtxt = mock(DeserializationContext.class);
        when(mockCtxt.findObjectId(anyObject(), eq(mockObjectIdReader), any())).thenReturn(mock(ReadableObjectId.class));

        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getValueAsInt()).thenReturn(123);

        Object instance = new Object() {
            private int id;

            @JsonProperty("id")
            public int getId() {
                return id;
            }

            @JsonAutoDetect(getterVisibility = NONE, setterVisibility = PUBLIC_ONLY)
            public void setId(int id) {
                this.id = id;
            }
        };

        objectIdValueProperty.deserializeAndSet(mockJsonParser, mockCtxt, instance);

        assertEquals(123, instance.getId());
    }

    @Test
    public void testDeserializeSetAndReturn() throws IOException {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(IdType.INT_ID);
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return this;
            }
        };

        JsonDeserializer<Object> mockValueDeserializer = mock(JsonDeserializer.class);

        DeserializationContext mockCtxt = mock(DeserializationContext.class);
        when(mockCtxt.findObjectId(anyObject(), eq(mockObjectIdReader), any())).thenReturn(mock(ReadableObjectId.class));

        Object instance = new Object() {
            private int id;

            @JsonProperty("id")
            public int getId() {
                return id;
            }

            @JsonAutoDetect(getterVisibility = NONE, setterVisibility = PUBLIC_ONLY)
            public void setId(int id) {
                this.id = id;
            }
        };

        BeanPropertyDefinition mockPropertyDefinition = mock(BeanPropertyDefinition.class);
        AnnotationIntrospector mockAnnotationIntrospector = mock(AnnotationIntrospector.class);
        when(mockAnnotationIntrospector.findBeanPropertyDefinition(anyObject(), eq("id"))).thenReturn(mockPropertyDefinition);
        when(mockCtxt.findBeanProperty(anyObject(), anyString())).thenReturn(mockPropertyDefinition);

        JsonParser mockJsonParser = mock(JsonParser.class);
        when(mockJsonParser.getValueAsInt()).thenReturn(123);

        Object result = objectIdValueProperty.deserializeSetAndReturn(mockJsonParser, mockCtxt, instance);

        assertEquals(instance, result);
        assertEquals(123, instance.getId());
    }

    @Test
    public void testSetAndReturn() throws IOException {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(IdType.INT_ID);
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return this;
            }
        };

        Object instance = new Object() {
            private int id;

            @JsonProperty("id")
            public int getId() {
                return id;
            }

            @JsonAutoDetect(getterVisibility = NONE, setterVisibility = PUBLIC_ONLY)
            public void setId(int id) {
                this.id = id;
            }
        };

        SettableBeanProperty mockSettableBeanProperty = mock(SettableBeanProperty.class);
        when(mockSettableBeanProperty.setAndReturn(instance, anyObject())).thenReturn(instance);

        Object result = objectIdValueProperty.setAndReturn(instance, 123);

        assertEquals(instance, result);
        assertEquals(123, instance.getId());
    }

    @Test
    public void testSetNull() throws IOException {
        ObjectIdReader mockObjectIdReader = mock(ObjectIdReader.class);
        when(mockObjectIdReader.propertyName).thenReturn(new PropertyName("id"));
        when(mockObjectIdReader.getIdType()).thenReturn(IdType.INT_ID);
        when(mockObjectIdReader.generator).thenReturn(mock(ObjectIdGenerator.class));
        when(mockObjectIdReader.resolver).thenReturn(mock(ObjectIdResolver.class));
        when(mockObjectIdReader.idProperty).thenReturn(mock(SettableBeanProperty.class));

        ObjectIdValueProperty objectIdValueProperty = new ObjectIdValueProperty(mockObjectIdReader, new PropertyMetadata()) {
            @Override
            public SettableBeanProperty withName(PropertyName newName) {
                return this;
            }

            @Override
            public SettableBeanProperty withValueDeserializer(JsonDeserializer<?> deser) {
                return this;
            }
        };

        Object instance = new Object() {
            private Integer id;

            @JsonProperty("id")
            public Integer getId() {
                return id;
            }

            @JsonAutoDetect(getterVisibility = NONE, setterVisibility = PUBLIC_ONLY)
            public void setId(Integer id) {
                this.id = id;
            }
        };

        objectIdValueProperty.set(instance, null);

        assertNull(instance.getId());
    }
}