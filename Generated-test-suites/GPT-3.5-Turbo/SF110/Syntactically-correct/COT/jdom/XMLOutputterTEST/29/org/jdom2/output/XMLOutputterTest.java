import org.jdom2.*;
import org.jdom2.output.support.*;
import org.junit.Assert;
import org.junit.Test;

public class XMLOutputterTest {

    @Test
    public void testSetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);

        Assert.assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        XMLOutputter outputter = new XMLOutputter();
        Format format = new Format();
        outputter.setFormat(format);

        Assert.assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testGetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);

        Assert.assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputProcessor processor = new XMLOutputProcessor();
        outputter.setXMLOutputProcessor(processor);

        Assert.assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    // Additional test cases for edge cases and exception handling scenarios

    @Test
    public void testOutput_NullDocument_ThrowsIOException() {
        XMLOutputter outputter = new XMLOutputter();

        try {
            outputter.output(null, new ByteArrayOutputStream());
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // exception is expected
        }
    }

    @Test
    public void testOutput_NullDocType_ThrowsIOException() {
        XMLOutputter outputter = new XMLOutputter();

        try {
            outputter.output((DocType) null, new ByteArrayOutputStream());
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // exception is expected
        }
    }

    @Test
    public void testOutput_NullElement_ThrowsIOException() {
        XMLOutputter outputter = new XMLOutputter();

        try {
            outputter.output((Element) null, new ByteArrayOutputStream());
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // exception is expected
        }
    }

    @Test
    public void testOutput_NullList_ThrowsIOException() {
        XMLOutputter outputter = new XMLOutputter();

        try {
            outputter.output((List<? extends Content>) null, new ByteArrayOutputStream());
            Assert.fail("IOException expected");
        } catch (IOException e) {
            // exception is expected
        }
    }

    // ... additional tests for other public methods

}