import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringWriter;

import static org.junit.Assert.assertEquals;

public class XMLOutputterTest {

    private XMLOutputter xmlOutputter;

    @Before
    public void setup() {
        xmlOutputter = new XMLOutputter();
    }

    @Test
    public void testOutputStringWithDocument() {
        Document doc = new Document();
        Element root = new Element("root");
        doc.setRootElement(root);

        StringWriter expected = new StringWriter();
        try {
            xmlOutputter.output(doc, expected);
        } catch (IOException e) {
            // Ignore
        }

        String actual = xmlOutputter.outputString(doc);

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testOutputStringWithDocType() {
        DocType doctype = new DocType("root");
        StringWriter expected = new StringWriter();
        try {
            xmlOutputter.output(doctype, expected);
        } catch (IOException e) {
            // Ignore
        }

        String actual = xmlOutputter.outputString(doctype);

        assertEquals(expected.toString(), actual);
    }

    @Test
    public void testOutputStringWithElement() {
        Element element = new Element("element");
        StringWriter expected = new StringWriter();
        try {
            xmlOutputter.output(element, expected);
        } catch (IOException e) {
            // Ignore
        }

        String actual = xmlOutputter.outputString(element);

        assertEquals(expected.toString(), actual);
    }

    // Add more test methods for other public methods in XMLOutputter class

}