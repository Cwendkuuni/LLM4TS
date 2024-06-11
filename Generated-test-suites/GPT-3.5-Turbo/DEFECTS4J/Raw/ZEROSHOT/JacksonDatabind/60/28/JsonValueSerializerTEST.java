

package com.fasterxml.jackson.databind.ser.std;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.io.IOException;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class JsonValueSerializerTest {

    private JsonValueSerializer serializer;

    @Mock
    private AnnotatedMethod annotatedMethod;

    @Mock
    private JsonSerializer<Object> valueSerializer;

    @Mock
    private BeanProperty property;

    @Mock
    private SerializerProvider provider;

    @Mock
    private JsonGenerator generator;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
        serializer = new JsonValueSerializer(annotatedMethod, valueSerializer);
    }

    @Test
    public void testSerialize() throws IOException {
        Object bean = new Object();
        Object value = new Object();
        when(annotatedMethod.getValue(bean)).thenReturn(value);

        serializer.serialize(bean, generator, provider);

        verify(generator).defaultSerializeNull();
        verify(provider).findTypedValueSerializer(value.getClass(), true, property);
        verify(valueSerializer).serialize(value, generator, provider);
    }
}

