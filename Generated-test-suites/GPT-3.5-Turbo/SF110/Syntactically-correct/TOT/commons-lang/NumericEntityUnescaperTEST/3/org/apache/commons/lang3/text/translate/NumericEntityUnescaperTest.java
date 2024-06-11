package org.apache.commons.lang3.text.translate;

import org.junit.Test;
import org.junit.Assert;

public class NumericEntityUnescaperTest {
    
    @Test
    public void testTranslate_SingleDigitDecimalEntity_TranslateSuccessfully() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#9;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(2, translationCount);
            Assert.assertEquals('\t', output.charAt(0));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_MultiDigitDecimalEntity_TranslateSuccessfully() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(2, translationCount);
            Assert.assertEquals('A', output.charAt(0));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_HexadecimalEntityCaseInsensitive_TranslateSuccessfully() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#X41;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(3, translationCount);
            Assert.assertEquals('A', output.charAt(0));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_NoSemiColonRequiredOption_ReturnsZeroTranslation() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.semiColonRequired);
        StringBuilder input = new StringBuilder();
        input.append("&#65");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(0, translationCount);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testTranslate_ErrorIfNoSemiColonOption_ThrowsIllegalArgumentException() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper(NumericEntityUnescaper.OPTION.errorIfNoSemiColon);
        StringBuilder input = new StringBuilder();
        input.append("&#65");
        StringBuilder output = new StringBuilder();
        unescaper.translate(input, 0, new DummyWriter(output));
    }
    
    @Test
    public void testTranslate_NonNumericEntity_ReturnsZeroTranslation() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&amp;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(0, translationCount);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_EntityValueExceeds65535_TranslateSuccessfully() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#65536;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(5, translationCount);
            Assert.assertEquals('\uD800', output.charAt(0));
            Assert.assertEquals('\uDC00', output.charAt(1));
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    @Test
    public void testTranslate_InvalidHexadecimalEntity_ReturnsZeroTranslation() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringBuilder input = new StringBuilder();
        input.append("&#xYZ;");
        StringBuilder output = new StringBuilder();
        try {
            int translationCount = unescaper.translate(input, 0, new DummyWriter(output));
            Assert.assertEquals(0, translationCount);
        } catch (Exception e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
    
    private class DummyWriter extends Writer {
        private StringBuilder output;
        
        public DummyWriter(StringBuilder output) {
            this.output = output;
        }
        
        @Override
        public void write(char[] cbuf, int off, int len) throws IOException {
            output.append(cbuf, off, len);
        }
    
        @Override
        public void flush() throws IOException {
        }
    
        @Override
        public void close() throws IOException {
        }
    }
}