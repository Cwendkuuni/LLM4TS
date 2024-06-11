package com.fasterxml.jackson.databind.ser.std;
import static org.junit.Assert.*;

import org.junit.Test;

public class NumberSerializerTest {

    @Test
    public void testSerialize() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add test cases for serialize method
    }

    @Test
    public void testCreateContextual() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add test cases for createContextual method
    }

    @Test
    public void testGetSchema() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add test cases for getSchema method
    }

    @Test
    public void testAcceptJsonFormatVisitor() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add test cases for acceptJsonFormatVisitor method
    }

    @Test
    public void testIsInt() {
        NumberSerializer numberSerializer = new NumberSerializer(Number.class);
        // Add test cases for isInt field
    }

}