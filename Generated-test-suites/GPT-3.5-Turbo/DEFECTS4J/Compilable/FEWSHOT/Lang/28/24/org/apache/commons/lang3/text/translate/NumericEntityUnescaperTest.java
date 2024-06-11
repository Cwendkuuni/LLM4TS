package org.apache.commons.lang3.text.translate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

import org.junit.Test;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslate() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        StringWriter writer = new StringWriter();
        try {
            unescaper.translate("", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&amp;", 0, writer);
            assertEquals("&amp;", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x41;", 0, writer);
            assertEquals("A", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#65;", 0, writer);
            assertEquals("A", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x41;abc", 0, writer);
            assertEquals("A", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#65;abc", 0, writer);
            assertEquals("A", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x4A;", 0, writer);
            assertEquals("J", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#74;", 0, writer);
            assertEquals("J", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x499;", 0, writer);
            assertEquals("Й", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#1049;", 0, writer);
            assertEquals("Й", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x409A;", 0, writer);
            assertEquals("К", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#10426;", 0, writer);
            assertEquals("К", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#0099;", 0, writer);
            assertEquals("c", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#101;", 0, writer);
            assertEquals("e", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x99;", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#153;", 0, writer);
            assertEquals("", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#999999999999;", 0, writer);
            assertEquals("�", writer.toString());
            
            writer = new StringWriter();
            unescaper.translate("&#x99999999;", 0, writer);
            assertEquals("�", writer.toString());
        } catch (IOException e) {
            fail("Failed with IOException");
        }
    }
}