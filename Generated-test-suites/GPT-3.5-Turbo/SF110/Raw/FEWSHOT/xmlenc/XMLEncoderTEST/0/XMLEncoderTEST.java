package org.znerd.xmlenc;
 
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.io.Writer;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;
 
public class XMLEncoderTest {
    
    private XMLEncoder xmlEncoder;
 
    @Before
    public void setUp() throws IllegalArgumentException, UnsupportedEncodingException {
        xmlEncoder = new XMLEncoder("UTF-8");
    }
 
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }
    
    @Test(expected = NullPointerException.class)
    public void testDeclarationWithNullWriter() throws NullPointerException, IOException{
        xmlEncoder.declaration(null);
    }
    
    @Test
    public void testDeclaration() throws NullPointerException, IOException{
        Writer writer = mock(Writer.class);
        xmlEncoder.declaration(writer);
        // Verify that the correct data was written to the writer.
    }
    
    @Test(expected = NullPointerException.class)
    public void testTextWithNullWriter() throws NullPointerException, InvalidXMLException, IOException{
        xmlEncoder.text(null, "text", true);
    }
    
    @Test(expected = NullPointerException.class)
    public void testTextCharArrayWithNullWriter() throws NullPointerException, InvalidXMLException, IOException{
        xmlEncoder.text(null, new char[]{'t', 'e', 'x', 't'}, 0, 4, true);
    }
 
    @Test
    public void testText() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = mock(Writer.class);
        xmlEncoder.text(writer, "text", true);
        // Verify that the correct data was written to the writer.
    }
    
    @Test
    public void testTextCharArray() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = mock(Writer.class);
        xmlEncoder.text(writer, new char[]{'t', 'e', 'x', 't'}, 0, 4, true);
        // Verify that the correct data was written to the writer.
    }
    
    @Test(expected = InvalidXMLException.class)
    public void testTextWithInvalidCharacter() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = mock(Writer.class);
        xmlEncoder.text(writer, '�');
    }
    
    @Test(expected = IOException.class)
    public void testTextWithIOException() throws NullPointerException, InvalidXMLException, IOException {
        Writer writer = mock(Writer.class);
        when(writer.write("text")).thenThrow(new IOException());
        xmlEncoder.text(writer, "text", true);
    }
    
    // More test cases for each method in XMLEncoder class
    
}