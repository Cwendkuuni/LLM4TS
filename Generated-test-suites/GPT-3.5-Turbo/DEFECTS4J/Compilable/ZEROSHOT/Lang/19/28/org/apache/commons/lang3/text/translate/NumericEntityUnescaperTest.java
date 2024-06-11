package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.*;
import java.io.IOException;
import java.io.StringWriter;
import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper unescaper;
    
    @Before
    public void setUp() {
        unescaper = new NumericEntityUnescaper();
    }
    
    @Test
    public void testTranslate_ShouldReturnTranslatedEntity() throws IOException {
        // Given
        CharSequence input = "&#65;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        // When
        int result = unescaper.translate(input, index, out);
        
        // Then
        assertEquals("A", out.toString());
        assertEquals(4, result);
    }
    
    @Test
    public void testTranslate_ShouldReturnTranslatedHexEntity() throws IOException {
        // Given
        CharSequence input = "&#x41;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        // When
        int result = unescaper.translate(input, index, out);
        
        // Then
        assertEquals("A", out.toString());
        assertEquals(5, result);
    }
    
    @Test
    public void testTranslate_ShouldReturnTranslatedEntityWithoutSemicolon() throws IOException {
        // Given
        CharSequence input = "&#65";
        StringWriter out = new StringWriter();
        int index = 0;
        
        // When
        int result = unescaper.translate(input, index, out);
        
        // Then
        assertEquals("A", out.toString());
        assertEquals(3, result);
    }
    
    @Test
    public void testTranslate_ShouldReturnZeroForInvalidEntityValue() throws IOException {
        // Given
        CharSequence input = "&#INVALID;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        // When
        int result = unescaper.translate(input, index, out);
        
        // Then
        assertEquals("", out.toString());
        assertEquals(0, result);
    }
    
    @Test
    public void testTranslate_ShouldReturnZeroForInvalidHexEntityValue() throws IOException {
        // Given
        CharSequence input = "&#xINVALID;";
        StringWriter out = new StringWriter();
        int index = 0;
        
        // When
        int result = unescaper.translate(input, index, out);
        
        // Then
        assertEquals("", out.toString());
        assertEquals(0, result);
    }
}