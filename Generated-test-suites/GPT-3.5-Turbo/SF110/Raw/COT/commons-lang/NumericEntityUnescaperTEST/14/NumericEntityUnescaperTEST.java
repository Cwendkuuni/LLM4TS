
import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;

public class NumericEntityUnescaperTest {
    NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
    
    @Test
    public void testIsSet() {
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonRequired));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.semiColonOptional));
        assertFalse(unescaper.isSet(NumericEntityUnescaper.OPTION.errorIfNoSemiColon));
    }
    
    @Test
    public void testTranslateWithValidNumericEntity() throws IOException {
        Writer out = new StringWriter();
        int result = unescaper.translate("&#65;", 0, out);
        
        assertEquals(6, result);
        assertEquals("A", out.toString());
    }
    
    @Test
    public void testTranslateWithInvalidNumericEntity() throws IOException {
        Writer out = new StringWriter();
  int result = unescaper.translate("&#999999;", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslateWithNoSemiColonRequiredOption() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        Writer out = new StringWriter();

        unescaper.translate("&#65", 0, out);
    }
    
    @Test
    public void testTranslateWithNoSemiColonOptionalOption() throws IOException {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonOptional);
        Writer out = new StringWriter();

        int result = unescaper.translate("&#65", 0, out);
        
        assertEquals(0, result);
        assertEquals("", out.toString());
    }
}

