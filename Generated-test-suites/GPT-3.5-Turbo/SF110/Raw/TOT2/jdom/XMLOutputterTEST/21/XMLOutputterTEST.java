import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;

public class XMLOutputterTest {

    private XMLOutputter xmlOutputter;

    @Before
    public void setUp() {
        xmlOutputter = new XMLOutputter();
    }

    @Test
    public void testSetFormat() {
        Format format = new Format(2);
        xmlOutputter.setFormat(format);
        assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputString() {
        Document doc = new Document();
        String output = xmlOutputter.outputString(doc);
        assertNotNull(output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        String str = "<hello>";
        String escapedStr = xmlOutputter.escapeAttributeEntities(str);
        assertEquals("&lt;hello>", escapedStr);
    }

    @Test
    public void testClone() {
        XMLOutputter clone = xmlOutputter.clone();
        assertNotNull(clone);
    }

    @Test
    public void testToString() {
        String output = xmlOutputter.toString();
        assertNotNull(output);
    }

    @Test(expected = IOException.class)
    public void testOutputIOException() throws IOException {
        Document doc = new Document();
        OutputStream out = new ByteArrayOutputStream();
        xmlOutputter.output(doc, out);
    }

    @Test
    public void testOutput() {
        Document doc = new Document();
        Writer writer = new StringWriter();
        try {
            xmlOutputter.output(doc, writer);
            assertNotNull(writer.toString());
        } catch (IOException e) {
            fail("IOException thrown");
        }
    }

}