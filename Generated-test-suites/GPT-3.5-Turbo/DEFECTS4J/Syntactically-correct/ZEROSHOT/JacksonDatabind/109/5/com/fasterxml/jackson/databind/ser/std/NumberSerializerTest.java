package com.fasterxml.jackson.databind.ser.std;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumberSerializerTest {
    
    private NumberSerializer numberSerializer;
    
    @Before
    public void setUp() {
        numberSerializer = new NumberSerializer(Number.class);
    }
    
    @Test
    public void testCreateContextual() {
        assertNull(numberSerializer.createContextual(null, null));
    }
    
    @Test
    public void testSerialize() {
        // Test BigDecimal
        //TODO
        
        // Test BigInteger
        //TODO
        
        // Test Long
        //TODO
        
        // Test Double
        //TODO
        
        // Test Float
        //TODO
        
        // Test Integer
        //TODO
        
        // Test Byte
        //TODO
        
        // Test Short
        //TODO
        
        // Test fallback "untyped" number case
        //TODO
        
    }
    
    @Test
    public void testGetSchema() {
        // Test _isInt = true
        //TODO
        
        // Test _isInt = false
        //TODO
    }
    
    @Test
    public void testAcceptJsonFormatVisitor() {
        // Test _isInt = true
        //TODO
        
        // Test _isInt = false and handledType = BigDecimal.class
        //TODO
        
        // Test _isInt = false and handledType != BigDecimal.class
        //TODO
    }
}