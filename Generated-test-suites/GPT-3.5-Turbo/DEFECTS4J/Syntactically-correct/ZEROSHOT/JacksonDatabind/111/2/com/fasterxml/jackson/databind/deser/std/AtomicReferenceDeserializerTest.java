package com.fasterxml.jackson.databind.deser.std;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import java.util.concurrent.atomic.AtomicReference;
import com.fasterxml.jackson.databind.*;
import com.fasterxml.jackson.databind.deser.ValueInstantiator;
import com.fasterxml.jackson.databind.jsontype.TypeDeserializer;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class AtomicReferenceDeserializerTest {

    @Mock
    private DeserializationContext deserializationContext;

    @Before
    public void setup() {

    }

    @Test
    public void testWithResolved() {
        // TODO: write test case
    }

    @Test
    public void testGetNullValue() throws JsonMappingException {
        // TODO: write test case
    }

    @Test
    public void testGetEmptyValue() {
        // TODO: write test case
    }

    @Test
    public void testReferenceValue() {
        // TODO: write test case
    }

    @Test
    public void testGetReferenced() {
        // TODO: write test case
    }

    @Test
    public void testUpdateReference() {
        // TODO: write test case
    }

    @Test
    public void testSupportsUpdate() {
        // TODO: write test case
    }
}