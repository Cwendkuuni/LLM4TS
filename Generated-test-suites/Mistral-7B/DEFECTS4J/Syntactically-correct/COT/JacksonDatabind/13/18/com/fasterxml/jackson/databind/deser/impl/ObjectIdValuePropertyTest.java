package com.example.objectidvalueproperty; // replace with your package name if necessary

import static org.junit.Assert.*;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.deser.ObjectIdReader;
import com.fasterxml.jackson.databind.deser.impl.ObjectIdReaderForString;
import com.fasterxml.jackson.databind.introspect.AnnotatedMember;
import com.fasterxml.jackson.databind.introspect.BeanPropertyDefinition;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;

import org.junit.Before;
import org.junit.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;

/**
 * Test class for {@link ObjectIdValueProperty}.
 */
public class ObjectIdValuePropertyTest {

    private ObjectIdReader _objectIdReader;
    private ObjectIdValueProperty _objectIdValueProperty;
    private JsonDeserializer<String> _stringDeserializer;
    private TypeDeserializer _typeDeserializer;
    private JsonParser _jsonParser;

    @Before
    public void setUp() throws Exception {
        _objectIdReader = Mockito.mock(ObjectIdReader.class);
        _stringDeserializer = Mockito.mock(JsonDeserializer.class);
        _typeDeserializer = Mockito.mock(TypeDeserializer.class);
        _jsonParser = Mockito.mock(JsonParser.class);

        ObjectIdValueProperty.ValueDeserializer valueDeserializer =
                new ObjectIdValueProperty.ValueDeserializer() {
                    @Override
                    public Object deserialize(JsonParser jp, DeserializationContext ctxt)
                            throws IOException {
                        return jp.getValueAsString();
                    }
                };

        _objectIdValueProperty = new ObjectIdValueProperty(
                _objectIdReader,
                new PropertyMetadata() {},
                new AnnotatedClass(String.class),
                _stringDeserializer,
                valueDeserializer,
                new ObjectIdReaderForString(new PropertyName("id")),
                new BeanPropertyDefinition(new PropertyName("id"), String.class),
                _typeDeserializer
        );
    }

    @Test
    public void testNewInstance() {
        assertEquals(_objectIdReader, _objectIdValueProperty.getObjectIdReader());
        assertEquals(_stringDeserializer, _objectIdValueProperty.getValueDeserializer());
    }

    @Test
    public void testDeserializeAndSet() throws Exception {
        String id = "some_id";
        when(_jsonParser.getValueAsString()).thenReturn(id);

        Object instance = new Object();

        _objectIdValueProperty.deserializeAndSet(_jsonParser, Mockito.mock(DeserializationContext.class), instance);

        assertEquals(id, _objectIdReader.getId(instance));
    }

    @Test
    public void testDeserializeSetAndReturn() throws Exception {
        String id = "some_id";
        when(_jsonParser.getValueAsString()).thenReturn(id);

        Object instance = new Object();

        Object result = _objectIdValueProperty.deserializeSetAndReturn(_jsonParser, Mockito.mock(DeserializationContext.class), instance);

        assertEquals(instance, result);
        assertEquals(id, _objectIdReader.getId(instance));
    }

    @Test(expected = UnsupportedOperationException.class)
    public void testSetWithoutIdProperty() throws Exception {
        Object instance = new Object();
        _objectIdValueProperty.set(instance, null);
        fail("Should throw UnsupportedOperationException when setting a value without an idProperty");
    }

    @Test
    public void testSetWithIdProperty() throws Exception {
        String id = "some_id";
        Object instance = new Object();
        Object idPropertyInstance = new Object();
        when(_objectIdReader.getIdProperty()).thenReturn(new ObjectIdValueProperty.SettableBeanProperty("id", String.class, null, null, null, null, null, null) {
            @Override
            public Object setAndReturn(Object instance, Object id) throws IOException {
                assertEquals(id, idPropertyInstance.getId());
                assertEquals(id, idPropertyInstance.getValue());
                return idPropertyInstance;
            }
        });

        _objectIdValueProperty.set(instance, id);
        assertEquals(id, _objectIdReader.getId(instance));
        assertEquals(id, _objectIdReader.getIdProperty().getId(instance));
    }
}