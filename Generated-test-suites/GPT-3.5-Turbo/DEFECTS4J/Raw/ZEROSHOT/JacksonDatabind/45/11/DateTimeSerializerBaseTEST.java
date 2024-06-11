
package com.fasterxml.jackson.databind.ser.std;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class DateTimeSerializerBaseTest {

    @Mock
    private SerializerProvider serializerProvider;

    @Mock
    private JsonGenerator jsonGenerator;

    @Mock
    private BeanProperty beanProperty;

    @Mock
    private JsonFormatVisitorWrapper jsonFormatVisitorWrapper;

    @Mock
    private JavaType javaType;

    @Test
    public void testCreateContextual() throws JsonMappingException {
        DateTimeSerializerBase dateTimeSerializerBase = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, true, null);

        dateTimeSerializerBase.createContextual(serializerProvider, beanProperty);
        verify(serializerProvider, times(1)).getAnnotationIntrospector();
        verify(beanProperty, times(1)).getMember();
    }

    @Test
    public void testIsEmpty() {
        DateTimeSerializerBase dateTimeSerializerBase = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, true, null);
        DateTimeSerializerBase dateTimeSerializerBase2 = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, false, null);

        dateTimeSerializerBase.isEmpty(null);
        dateTimeSerializerBase.isEmpty(serializerProvider, null);
        dateTimeSerializerBase2.isEmpty(serializerProvider, null);

        verifyZeroInteractions(serializerProvider);
    }

    @Test
    public void testGetSchema() {
        DateTimeSerializerBase dateTimeSerializerBase = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, true, null);

        dateTimeSerializerBase.getSchema(serializerProvider, null);
        verify(serializerProvider, times(1)).isEnabled(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    public void testAcceptJsonFormatVisitor() throws JsonMappingException {
        DateTimeSerializerBase dateTimeSerializerBase = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, true, null);

        dateTimeSerializerBase.acceptJsonFormatVisitor(jsonFormatVisitorWrapper, javaType);
        verify(jsonFormatVisitorWrapper, times(1)).getProvider();
    }

    @Test
    public void testSerialize() throws IOException {
        DateTimeSerializerBase dateTimeSerializerBase = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, true, null);
        DateTimeSerializerBase dateTimeSerializerBase2 = new DateTimeSerializerBaseImpl(DateTimeSerializerBaseTest.class, false, null);

        dateTimeSerializerBase.serialize(null, jsonGenerator, serializerProvider);
        dateTimeSerializerBase2.serialize(null, jsonGenerator, serializerProvider);

        verifyZeroInteractions(jsonGenerator);
    }

}
