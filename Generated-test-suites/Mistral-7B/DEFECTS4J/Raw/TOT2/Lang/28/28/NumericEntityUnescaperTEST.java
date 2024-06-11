package org.apache.commons.lang3.text.translate;

import org.apache.commons.io.StringReader;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;
import static org.junit.Assert.*;

public class NumericEntityUnescaperTest {
    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &test& with some &#65 &#x41; &#x1F60A; entities.";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("This is a test with some А entities.", out.getBuffer().toString());
        assertEquals(12, length);
    }
    
    @Test
    public void testTranslate_BadEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &%$# entity.";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("This is a &%$# entity.", out.getBuffer().toString());
        assertEquals(10, length);
    }
    
    @Test
    public void testTranslate_EmptyInput() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("", out.getBuffer().toString());
        assertEquals(0, length);
    }
    
    @Test
    public void testTranslate_EmptyEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &; entity.";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("This is a &; entity.", out.getBuffer().toString());
        assertEquals(5, length);
    }
    
    @Test
    public void testTranslate_LargeEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "This is a &#[1114111;] entity.";
        Writer out = new StringWriter();
        int length = unescaper.translate(new StringReader(input), 0, out);
        assertEquals("This is a ẠẠẠẠẠẠẠẠẠẠẠẠ entity.", out.getBuffer().toString());
        assertEquals(23, length);
    }
}