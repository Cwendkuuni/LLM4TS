import org.junit.*;
import java.io.*;
import static org.junit.Assert.*;

public class XMLEncoderTest {
    
    private XMLEncoder xmlEncoder;
    
    @Before
    public void setUp() throws Exception {
        xmlEncoder = new XMLEncoder("UTF-8");
    }
    
    @After
    public void tearDown() throws Exception {
        xmlEncoder = null;
    }
    
    @Test
    public void testGetEncoder() throws Exception {
        XMLEncoder encoder = XMLEncoder.getEncoder("UTF-8");
        assertNotNull(encoder);
        assertEquals("UTF-8", encoder.getEncoding());
    }
    
    @Test(expected = IllegalArgumentException.class)
    public void testGetEncoder_NullEncoding() throws Exception {
        XMLEncoder.getEncoder(null);
    }
    
    @Test(expected = UnsupportedEncodingException.class)
    public void testGetEncoder_InvalidEncoding() throws Exception {
        XMLEncoder.getEncoder("InvalidEncoding");
    }
    
    @Test
    public void testGetEncoding() {
        assertEquals("UTF-8", xmlEncoder.getEncoding());
    }
    
    @Test
    public void testDeclaration() throws Exception {
        StringWriter writer = new StringWriter();
        xmlEncoder.declaration(writer);
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_withEscape() throws Exception {
        StringWriter writer = new StringWriter();
        String text = "<xml>text&";
        xmlEncoder.text(writer, text, true);
        String expected = "&lt;xml&gt;text&amp;";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_withoutEscape() throws Exception {
        StringWriter writer = new StringWriter();
        String text = "<xml>text&";
        xmlEncoder.text(writer, text, false);
        String expected = "<xml>text&";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_CharArray_withEscape() throws Exception {
        StringWriter writer = new StringWriter();
        char[] ch = "<xml>text&".toCharArray();
        xmlEncoder.text(writer, ch, 0, ch.length, true);
        String expected = "&lt;xml&gt;text&amp;";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_CharArray_withoutEscape() throws Exception {
        StringWriter writer = new StringWriter();
        char[] ch = "<xml>text&".toCharArray();
        xmlEncoder.text(writer, ch, 0, ch.length, false);
        String expected = "<xml>text&";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testText_Char() throws Exception {
        StringWriter writer = new StringWriter();
        char c = '<';
        xmlEncoder.text(writer, c);
        String expected = "&lt;";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testWhitespace_String() throws Exception {
        StringWriter writer = new StringWriter();
        String whitespace = "  ";
        xmlEncoder.whitespace(writer, whitespace);
        String expected = "  ";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testWhitespace_CharArray() throws Exception {
        StringWriter writer = new StringWriter();
        char[] ch = "  ".toCharArray();
        xmlEncoder.whitespace(writer, ch, 0, ch.length);
        String expected = "  ";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testAttribute_withEscape() throws Exception {
        StringWriter writer = new StringWriter();
        String name = "attr";
        String value = "<value&";
        char quotationMark = '\"';
        xmlEncoder.attribute(writer, name, value, quotationMark, true);
        String expected = " attr=\"&lt;value&amp;\"";
        assertEquals(expected, writer.toString());
    }
    
    @Test
    public void testAttribute_withoutEscape() throws Exception {
        StringWriter writer = new StringWriter();
        String name = "attr";
        String value = "<value&";
        char quotationMark = '\"';
        xmlEncoder.attribute(writer, name, value, quotationMark, false);
        String expected = " attr=\"<value&\"";
        assertEquals(expected, writer.toString());
    }
}