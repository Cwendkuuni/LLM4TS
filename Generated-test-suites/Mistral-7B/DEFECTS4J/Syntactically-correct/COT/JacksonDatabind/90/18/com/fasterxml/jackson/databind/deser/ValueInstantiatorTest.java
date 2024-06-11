package com.example.test;

import org.junit.Test;
import static org.junit.Assert.*;

import com.fasterxml.jackson.databind.deser.ValueInstantiator;

public class ValueInstantiatorTest {

    @Test
    public void testValueInstantiatorMethods() {
        ValueInstantiator valueInstantiator = new ValueInstantiator.Base(String.class);

        assertEquals("java.lang.String", valueInstantiator.getValueClass().getName());
        assertEquals("java.lang.String", valueInstantiator.getValueTypeDesc());

        valueInstantiator = new ValueInstantiator.Base(Integer.class);

        assertEquals("java.lang.Integer", valueInstantiator.getValueClass().getName());
        assertEquals("java.lang.Integer", valueInstantiator.getValueTypeDesc());
    }
}