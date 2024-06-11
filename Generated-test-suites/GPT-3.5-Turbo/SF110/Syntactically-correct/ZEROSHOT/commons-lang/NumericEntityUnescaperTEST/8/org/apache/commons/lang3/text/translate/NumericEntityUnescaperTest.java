package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.io.StringWriter;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        
        // Test when input length is less than 3
        assertEquals(0, unescaper.translate("", 0, writer));
        assertEquals(0, unescaper.translate("a", 0, writer));
        assertEquals(0, unescaper.translate("&&", 0, writer));
        
        // Test when input does not start with "&#" sequence
        assertEquals(0, unescaper.translate("abc", 0, writer));
        assertEquals(0, unescaper.translate("&#abc;", 0, writer));
        
        // Test when input starts with "&#x" hex sequence
        assertEquals(4, unescaper.translate("&#x20;", 0, writer));
        assertEquals(4, unescaper.translate("&#xAB;", 0, writer));
        
        // Test when input starts with "&#X" hex sequence
        assertEquals(4, unescaper.translate("&#X20;", 0, writer));
        assertEquals(4, unescaper.translate("&#XAB;", 0, writer));
        
        // Test when input starts with "&#x" hex sequence and ends with ";"
        assertEquals(5, unescaper.translate("&#x20;", 0, writer));
        assertEquals(5, unescaper.translate("&#xAB;", 0, writer));
        assertEquals(5, unescaper.translate("&#xABC;", 0, writer));
        
        // Test when input starts with "&#X" hex sequence and ends with ";"
        assertEquals(5, unescaper.translate("&#X20;", 0, writer));
        assertEquals(5, unescaper.translate("&#XAB;", 0, writer));
        assertEquals(5, unescaper.translate("&#XABC;", 0, writer));
        
        // Test when input starts with "&#x" hex sequence but does not end with ";"
        assertEquals(0, unescaper.translate("&#x20", 0, writer));
        assertEquals(0, unescaper.translate("&#xAB", 0, writer));
        assertEquals(0, unescaper.translate("&#xABC", 0, writer));
        
        // Test when input starts with "&#X" hex sequence but does not end with ";"
        assertEquals(0, unescaper.translate("&#X20", 0, writer));
        assertEquals(0, unescaper.translate("&#XAB", 0, writer));
        assertEquals(0, unescaper.translate("&#XABC", 0, writer));
        
        // Test when input starts with numeric sequence and ends with ";"
        assertEquals(5, unescaper.translate("&#65;", 0, writer));
        assertEquals(5, unescaper.translate("&#97;", 0, writer));
        assertEquals(5, unescaper.translate("&#123;", 0, writer));
        
        // Test when input starts with numeric sequence but does not end with ";"
        assertEquals(0, unescaper.translate("&#65", 0, writer));
        assertEquals(0, unescaper.translate("&#97", 0, writer));
        assertEquals(0, unescaper.translate("&#123", 0, writer));
        
        // Test when input starts with numeric sequence, ends with ";", and contains non-numeric characters
        assertEquals(5, unescaper.translate("&#65x;", 0, writer));
        assertEquals(5, unescaper.translate("&#97x;", 0, writer));
        assertEquals(5, unescaper.translate("&#123x;", 0, writer));
    }
    
    @Test
    public void testIsSet() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
}