import static org.junit.Assert.*;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testSetFormat() {
        // Typical use case
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(Format.getPrettyFormat());
        assertEquals(Format.getPrettyFormat(), outputter.getFormat());

        // Edge case: null format
        outputter.setFormat(null);
        assertNotNull(outputter.getFormat());
    }

    @Test
    public void testOutputString() {
        // Typical use case
        XMLOutputter outputter = new XMLOutputter();
        Document doc = new Document();
        String output = outputter.outputString(doc);
        assertNotNull(output);

        // Error scenario: IOException while outputting
        // This scenario cannot be easily tested with JUnit
    }

    // More test cases for other methods...

}