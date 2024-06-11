
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;

@RunWith(MockitoJUnitRunner.class)
public class JsonValueSerializerTest {

    @Mock
    private AnnotatedMethod _accessorMethod;

    @Mock
    private JsonSerializer<Object> _valueSerializer;

    @Mock
    private BeanProperty _property;

    private JsonValueSerializer jsonValueSerializer;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        jsonValueSerializer = new JsonValueSerializer(_accessorMethod, _valueSerializer);
    }

    @Test
    public void testSerialize() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = null; // Replace with mock
        SerializerProvider prov = null; // Replace with mock

        jsonValueSerializer.serialize(bean, gen, prov);

        verify(gen).defaultSerializeNull();
        verify(prov).findTypedValueSerializer(Object.class, true, _property);
        verify(_valueSerializer).serialize(bean, gen, prov);
    }

    @Test
    public void testSerializeWithType() throws Exception {
        Object bean = new Object();
        JsonGenerator gen = null; // Replace with mock
        SerializerProvider provider = null; // Replace with mock
        TypeSerializer typeSer0 = null; // Replace with mock

        jsonValueSerializer.serializeWithType(bean, gen, provider, typeSer0);

        verify(gen).defaultSerializeNull();
        verify(provider).findValueSerializer(Object.class, _property);
        verify(_valueSerializer).serializeWithType(bean, gen, provider, typeSer0);
    }

    @Test
    public void testGetSchema() throws Exception {
        SerializerProvider provider = null; // Replace with mock
        Type typeHint = null; // Replace with mock

        jsonValueSerializer.getSchema(provider, typeHint);

        verify(_valueSerializer).getSchema(provider, null);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws Exception {
        JsonFormatVisitorWrapper visitor = null; // Replace with mock
        JavaType typeHint = null; // Replace with mock

        jsonValueSerializer.acceptJsonFormatVisitor(visitor, typeHint);

        verify(_valueSerializer).acceptJsonFormatVisitor(visitor, null);
    }
}
