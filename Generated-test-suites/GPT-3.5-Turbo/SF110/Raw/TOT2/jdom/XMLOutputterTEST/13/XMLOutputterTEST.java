import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import org.jdom2.*;
import org.jdom2.output.*;

public class XMLOutputterTest {

    @Test
    public void testSetAndGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = Format.getPrettyFormat();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testSetAndGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor() {
            @Override
            public void process(Writer writer, Format format, Object obj) throws IOException {}
        };
        outputter.setXMLOutputProcessor(processor);
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocumentToOutputStream() {
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();
        OutputStream out = new ByteArrayOutputStream();
        assertDoesNotThrow(() -> outputter.output(doc, out));
    }

    // Additional test cases for other public methods can be added here...

}