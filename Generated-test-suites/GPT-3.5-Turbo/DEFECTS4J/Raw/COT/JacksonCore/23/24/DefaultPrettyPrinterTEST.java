import com.fasterxml.jackson.core.JsonGenerator;
import org.junit.Assert;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

    @Test
    public void testWriteRootValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeRootValueSeparator(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteStartObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeStartObject(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testBeforeObjectEntries() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.beforeObjectEntries(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteObjectFieldValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeObjectFieldValueSeparator(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteObjectEntrySeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeObjectEntrySeparator(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteEndObject() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        int nrOfEntries = 0; // initialise nrOfEntries
        prettyPrinter.writeEndObject(g, nrOfEntries); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
        Assert.assertEquals(0, nrOfEntries);
    }

    @Test
    public void testWriteStartArray() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeStartArray(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testBeforeArrayValues() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.beforeArrayValues(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteArrayValueSeparator() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        prettyPrinter.writeArrayValueSeparator(g); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
    }

    @Test
    public void testWriteEndArray() throws Exception {
        DefaultPrettyPrinter prettyPrinter = new DefaultPrettyPrinter();
        JsonGenerator g = null; // initialise the JsonGenerator object
        int nrOfValues = 0; // initialise nrOfValues
        prettyPrinter.writeEndArray(g, nrOfValues); // call the method to be tested
        // write assertions to verify the functionality of the method
        Assert.assertNotNull(g);
        Assert.assertEquals(0, nrOfValues);
    }
}