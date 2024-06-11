import static org.junit.Assert.*;
import org.junit.Test;
import java.io.IOException;
import java.io.StringWriter;
import java.io.OutputStream;
import org.jdom2.*;
import org.jdom2.output.*;
import org.jdom2.output.support.DefaultXMLProcessor;

public class XMLOutputterTest {

    @Test
    public void testSetFormat() {
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testOutputString_emptyDocument() {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        assertEquals("", outputter.outputString(doc));
    }

    @Test
    public void testOutputString_exceptionCaught() {
        Document doc = new Document();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(new Format());
        assertNotNull(outputter.outputString(doc));
    }

    // Add more test cases as needed for all methods
    
}