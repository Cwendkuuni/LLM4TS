
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {
    
    @Test
    public void testFilter_NullInput_ReturnNull() {
        assertNull(HTMLFilter.filter(null));
    }
    
    @Test
    public void testFilter_EmptyString_ReturnEmptyString() {
        assertEquals("", HTMLFilter.filter(""));
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnSameString() {
        String input = "This is a sample message.";
        assertEquals(input, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_ContainsLessThan_ReturnValidOutput() {
        String input = "This is less than <.";
        String expectedOutput = "This is less than &lt;.";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_ContainsGreaterThan_ReturnValidOutput() {
        String input = "This is greater than >.";
        String expectedOutput = "This is greater than &gt;.";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_ContainsAmpersand_ReturnValidOutput() {
        String input = "This contains & symbol.";
        String expectedOutput = "This contains &amp; symbol.";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_ContainsDoubleQuote_ReturnValidOutput() {
        String input = "This contains \" symbol.";
        String expectedOutput = "This contains &quot; symbol.";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
    
    @Test
    public void testFilter_ContainsCombinationOfSpecialCharacters_ReturnValidOutput() {
        String input = "This <is> a &sample message\".";
        String expectedOutput = "This &lt;is&gt; a &amp;sample message&quot;.";
        assertEquals(expectedOutput, HTMLFilter.filter(input));
    }
}
