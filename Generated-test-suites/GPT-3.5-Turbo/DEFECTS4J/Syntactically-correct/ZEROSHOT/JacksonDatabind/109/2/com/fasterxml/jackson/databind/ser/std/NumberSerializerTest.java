package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class NumberSerializerTest {

    @Mock
    private JsonGenerator generator;

    @Mock
    private SerializerProvider provider;

    private NumberSerializer numberSerializer;

    @Before
    public void setup() {
        numberSerializer = new NumberSerializer(Number.class);
    }

    @Test
    public void testSerialize_BigDecimal() throws IOException {
        BigDecimal value = new BigDecimal("10.5");
        ArgumentCaptor<BigDecimal> captor = ArgumentCaptor.forClass(BigDecimal.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_BigInteger() throws IOException {
        BigInteger value = new BigInteger("100");
        ArgumentCaptor<BigInteger> captor = ArgumentCaptor.forClass(BigInteger.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Long() throws IOException {
        Long value = 100L;
        ArgumentCaptor<Long> captor = ArgumentCaptor.forClass(Long.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Double() throws IOException {
        Double value = 10.5;
        ArgumentCaptor<Double> captor = ArgumentCaptor.forClass(Double.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Float() throws IOException {
        Float value = 10.5f;
        ArgumentCaptor<Float> captor = ArgumentCaptor.forClass(Float.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Integer() throws IOException {
        Integer value = 100;
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value, captor.getValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Byte() throws IOException {
        Byte value = 100;
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value.intValue(), captor.getValue().intValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_Short() throws IOException {
        Short value = 100;
        ArgumentCaptor<Integer> captor = ArgumentCaptor.forClass(Integer.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value.intValue(), captor.getValue().intValue());
        verify(generator, times(1)).writeNumber(value);
    }

    @Test
    public void testSerialize_other() throws IOException {
        Number value = 100;
        ArgumentCaptor<String> captor = ArgumentCaptor.forClass(String.class);
        doNothing().when(generator).writeNumber(captor.capture());

        numberSerializer.serialize(value, generator, provider);

        assertEquals(value.toString(), captor.getValue());
        verify(generator, times(1)).writeNumber(value.toString());
    }
}