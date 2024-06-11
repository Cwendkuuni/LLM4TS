
package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.StringWriter;
import java.io.Writer;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate_ReturnsZero_WhenGivenEmptyInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        int result = translator.translate("", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ReturnsZero_WhenGivenInvalidInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        int result = translator.translate("abc", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ReturnsZero_WhenInputStartsWithAmpersandButNoNumericEntity() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        int result = translator.translate("&abc;", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ReturnsZero_WhenSemiColonRequiredButNotPresent() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer out = new StringWriter();
        int result = translator.translate("&#123", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ThrowsIllegalArgumentException_WhenSemiColonNotPresentAndErrorIfNoSemiColonSet() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        Writer out = new StringWriter();
        
        try {
            translator.translate("&#123", 0, out);
            fail("Expected IllegalArgumentException to be thrown");
        } catch (IllegalArgumentException ex) {
            assertEquals("Semi-colon required at end of numeric entity", ex.getMessage());
        }
        
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ReturnsZero_WhenInvalidHexInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        int result = translator.translate("&#xg;", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test
    public void testTranslate_ReturnsNonZero_AndWritesCharacter_WhenValidHexInput() throws Exception {
        NumericEntityUnescaper translator = new NumericEntityUnescaper();
        Writer out = new StringWriter();
        int result = translator.translate("&#x61;", 0, out);
        
        assertEquals(5, result);
        assertEquals("a", out.toString());
    }
    
}
