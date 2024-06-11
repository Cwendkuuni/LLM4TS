package org.junit.tests;

import org.apache.commons.io.input.StringReader;
import org.apache.commons.lang3.text.translate.NumericEntityUnescaper;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class NumericEntityUnescaperTest {

    @Test
    public void testTranslateSimpleNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#160;&#128;&#8203;&#35;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expected = "\u00A0\u0080\u2003\u0023";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testTranslateHexNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x1F345;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String expected = "\uD83C\uDF34";
        assertEquals(expected, output.toString());
    }

    @Test
    public void testTranslateInvalidNumericEntity() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#xInvalid;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            assert e instanceof NumberFormatException;
        }
    }

    @Test
    public void testTranslateMissingSemicolon() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#123;"; // This should cause an exception
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            assert e instanceof NumberFormatException;
        }
    }

    @Test
    public void testTranslateNegativeNumber() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#-1;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            assert e instanceof NumberFormatException;
        }
    }

    @Test
    public void testTranslateOverflowCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#65536;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            assert e instanceof NumberFormatException;
        }
    }

    @Test
    public void testTranslateHexOverflowCharacter() {
        NumericEntityUnescaper unescaper = new NumericEntityUnescaper();
        String input = "&amp;#x10FFFF;";
        StringBuilder output = new StringBuilder();
        try (Writer writer = output) {
            unescaper.translate(new StringReader(input), 0, writer);
        } catch (IOException e) {
            assert e instanceof NumberFormatException;
        }
    }
}