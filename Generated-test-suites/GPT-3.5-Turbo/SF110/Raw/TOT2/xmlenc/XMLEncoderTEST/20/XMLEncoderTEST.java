package org.znerd.xmlenc;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class XMLEncoderTest {

    private XMLEncoder encoder;

    @Test
    public void testGetEncoding() {
        this.encoder = new XMLEncoder("UTF-8");
        assertEquals("UTF-8", encoder.getEncoding());
    }

    @Test
    public void testDeclaration() {
        // Test declaration writer
        // Edge case
        try (Writer writer = new StringWriter()) {
            encoder.declaration(writer);
            String output = writer.toString();
            assertTrue(output.contains("<?xml version=\"1.0\" encoding=\"" + encoder.getEncoding() + "\"?>"));
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    public void testText() {
        // Test text writer, text, escapeAmpersands
        // Edge case
        try (Writer writer = new StringWriter()) {
            encoder.text(writer, "text", true);
            String output = writer.toString();
            assertEquals("text", output);
        } catch (IOException | InvalidXMLException e) {
            fail("IOException or InvalidXMLException not expected");
        }
    }

    @Test
    public void testAttribute() {
        // Test attribute writer, name, value, quotationMark, escapeAmpersands
        // Error scenario
        try (Writer writer = new StringWriter()) {
            encoder.attribute(writer, "name", "value", '"', false);
            fail("IllegalArgumentException expected for using invalid quotation mark");
        } catch (IOException e) {
            fail("IOException not expected");
        } catch (IllegalArgumentException e) {
            // pass
        }
    }

    @Rule
    public ExpectedException exceptionRule = ExpectedException.none();

    @Test
    public void testConstructorException() {
        exceptionRule.expect(IllegalArgumentException.class);
        exceptionRule.expectMessage("encoding == null");
        new XMLEncoder(null);
    }

    @Test
    public void testTextException() {
        exceptionRule.expect(IndexOutOfBoundsException.class);
        try (Writer writer = new StringWriter()) {
            encoder.text(writer, new char[3], 0, 4, true);
        } catch (IOException | InvalidXMLException e) {
            fail("IOException or InvalidXMLException not expected");
        }
    }

}