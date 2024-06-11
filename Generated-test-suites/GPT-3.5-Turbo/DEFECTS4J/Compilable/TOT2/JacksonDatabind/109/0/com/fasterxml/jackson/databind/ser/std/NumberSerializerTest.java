package com.fasterxml.jackson.databind.ser.std;

import static org.junit.Assert.*;
import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        // typical use case
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // test for BigDecimal
        // test for BigInteger
        // test for Long
        // test for Double
        // test for Float
        // test for Integer
        // test for Byte
        // test for Short
        // test for fallback "untyped" number write method
    }

    @Test
    public void testGetSchema() {
        // typical use case
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // test for integer type
        // test for number type
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        // typical use case
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // test for BigIntger with visitIntFormat
        // test for BigDecimal with visitFloatFormat
        // test for other numbers with expectNumberFormat
    }

    @Test
    public void testCreateContextual() {
        // typical use case
        NumberSerializer serializer = new NumberSerializer(Number.class);
        // test for STRING format
        // test for default format
    }

    @Test
    public void testSerialize_Exceptions() {
        // test for potential exceptions during serialization
    }

    @Test
    public void testGetSchema_Exceptions() {
        // test for potential exceptions during getting schema
    }

    @Test
    public void testAcceptJsonFormatVisitor_Exceptions() {
        // test for potential exceptions during accepting json format visitor
    }
}