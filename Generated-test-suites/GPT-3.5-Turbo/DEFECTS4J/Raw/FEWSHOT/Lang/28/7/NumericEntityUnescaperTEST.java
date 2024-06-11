
package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

import java.io.IOException;
import java.io.Writer;

import org.junit.Before;
import org.junit.Test;

public class NumericEntityUnescaperTest {
    
    private NumericEntityUnescaper numericEntityUnescaper;

    @Before
    public void setUp() {
        numericEntityUnescaper = new NumericEntityUnescaper();
    }

    @Test
    public void testTranslate_WithValidNumericEntity_ReturnsCorrectCodepoint() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(6, numericEntityUnescaper.translate("&#x1F602;", 0, mockWriter));
        verify(mockWriter).write(128514);
    }
    
    @Test
    public void testTranslate_WithInvalidNumericEntity_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("&#xG;", 0, mockWriter));
        verifyZeroInteractions(mockWriter);
    }
    
    @Test
    public void testTranslate_WithLeadingCharNotAmp_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("a&#x1F602;", 1, mockWriter));
        verifyZeroInteractions(mockWriter);
    }
    
    @Test
    public void testTranslate_WithLeadingCharAmpAndInvalidSecondChar_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("&x1F602;", 0, mockWriter));
        verifyZeroInteractions(mockWriter);
    }
    
    @Test
    public void testTranslate_WithLeadingCharAmpAndSecondCharNotHash_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("&x1F602;", 1, mockWriter));
        verifyZeroInteractions(mockWriter);
    }

    @Test
    public void testTranslate_WithInvalidHexNumericValue_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("&#xG602;", 0, mockWriter));
        verifyZeroInteractions(mockWriter);
    }
    
    @Test
    public void testTranslate_WithValidDecNumericEntity_ReturnsCorrectCodepoint() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(6, numericEntityUnescaper.translate("&#128514;", 0, mockWriter));
        verify(mockWriter).write(128514);
    }
    
    @Test
    public void testTranslate_WithInvalidDecNumericValue_ReturnsZero() throws IOException {
        Writer mockWriter = mock(Writer.class);
        
        assertEquals(0, numericEntityUnescaper.translate("&#A;", 0, mockWriter));
        verifyZeroInteractions(mockWriter);
    }

}
