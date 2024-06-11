import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;
import java.io.*;

public class XMLOutputterTest {
    
    // Test for setFormat method
    @Test
    public void testSetFormat() {
        Format format = Format.getPrettyFormat();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }
    
    // Test for outputString method with typical scenario
    @Test
    public void testOutputString() {
        Document doc = new Document(new Element("root"));
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.outputString(doc);
        assertEquals("<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator() + "<root />" + System.lineSeparator(), result);
    }
    
    // Test for outputString method with IOException scenario
    @Test(expected = IOException.class)
    public void testOutputStringIOException() throws IOException {
        Document doc = new Document(new Element("root"));
        XMLOutputter outputter = new XMLOutputter();
        outputter.outputString(doc, new BufferedWriter(new FileWriter("nonexistent.txt")));
    }
    
    // Test for escapeAttributeEntities method
    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.escapeAttributeEntities("<html>");
        assertEquals("&lt;html&gt;", result);
    }
    
    // Test for escapeElementEntities method
    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String result = outputter.escapeElementEntities("<inner>text</inner>");
        assertEquals("<inner>text</inner>", result);
    }
    
    // Test for clone method
    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clonedOutputter = outputter.clone();
        assertNotSame(outputter, clonedOutputter);
    }
}