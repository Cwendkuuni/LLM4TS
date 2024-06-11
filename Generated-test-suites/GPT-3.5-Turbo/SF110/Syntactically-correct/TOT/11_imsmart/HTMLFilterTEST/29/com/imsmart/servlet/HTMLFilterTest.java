
package com.imsmart.servlet;

import org.junit.Test;
import static org.junit.Assert.*;

public class HTMLFilterTest {

    @Test
    public void testFilter_NullMessage_ReturnsNull() {
        // Arrange
        String message = null;
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertNull(filteredMessage);
    }
    
    @Test
    public void testFilter_EmptyMessage_ReturnsEmptyString() {
        // Arrange
        String message = "";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("", filteredMessage);
    }
    
    @Test
    public void testFilter_NoSpecialCharacters_ReturnsOriginalString() {
        // Arrange
        String message = "Hello World!";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("Hello World!", filteredMessage);
    }
    
    @Test
    public void testFilter_ContainsLessThanSymbol_ReplacesWithEncodedValue() {
        // Arrange
        String message = "3 < 5";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("3 &lt; 5", filteredMessage);
    }
    
    @Test
    public void testFilter_ContainsGreaterThanSymbol_ReplacesWithEncodedValue() {
        // Arrange
        String message = "5 > 3";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("5 &gt; 3", filteredMessage);
    }
    
    @Test
    public void testFilter_ContainsAmpersandSymbol_ReplacesWithEncodedValue() {
        // Arrange
        String message = "AT&T";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("AT&amp;T", filteredMessage);
    }
    
    @Test
    public void testFilter_ContainsDoubleQuoteSymbol_ReplacesWithEncodedValue() {
        // Arrange
        String message = "She said, \"Hello!\"";
        
        // Act
        String filteredMessage = HTMLFilter.filter(message);
        
        // Assert
        assertEquals("She said, &quot;Hello!&quot;", filteredMessage);
    }

}

