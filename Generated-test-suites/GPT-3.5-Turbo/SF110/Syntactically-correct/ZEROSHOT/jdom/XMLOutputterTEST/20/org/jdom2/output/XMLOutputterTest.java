package org.jdom2.output;

import org.jdom2.Document;
import org.jdom2.DocType;
import org.jdom2.Element;
import org.jdom2.Text;
import org.jdom2.CDATA;
import org.jdom2.Comment;
import org.jdom2.ProcessingInstruction;
import org.jdom2.EntityRef;
import org.jdom2.output.support.AbstractXMLOutputProcessor;
import org.jdom2.output.support.FormatStack;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.Assert.*;

public class XMLOutputterTest {

    private XMLOutputter xmlOutputter;

    @Before
    public void setUp() {
        xmlOutputter = new XMLOutputter();
    }

    @After
    public void tearDown() {
        xmlOutputter = null;
    }

    @Test
    public void testSetFormat() {
        Format format = new Format(2, true, StandardCharsets.UTF_8, true);
        xmlOutputter.setFormat(format);
        assertEquals(format, xmlOutputter.getFormat());
    }

    @Test
    public void testGetFormat() {
        Format format = xmlOutputter.getFormat();
        assertNotNull(format);
    }

    @Test
    public void testSetXMLOutputProcessor() {
        XMLOutputProcessor processor = new DefaultXMLOutputProcessor();
        xmlOutputter.setXMLOutputProcessor(processor);
        assertEquals(processor, xmlOutputter.getXMLOutputProcessor());
    }

    @Test
    public void testOutputDocument() {
        Document document = new Document();
        document.setRootElement(new Element("root"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(document, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "<?xml version=\"1.0\" encoding=\"UTF-8\"?>" + System.lineSeparator()
                + "<root />" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputDocType() {
        DocType docType = new DocType("root", "http://www.example.com");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(docType, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "<!DOCTYPE root SYSTEM \"http://www.example.com\">" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElement() {
        Element element = new Element("root");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(element, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "<root />" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputElementContent() {
        Element element = new Element("root");
        element.addContent(new Text("Hello World!"));
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.outputElementContent(element, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "Hello World!" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputList() {
        Element element1 = new Element("element1");
        Element element2 = new Element("element2");
        List<Element> elements = List.of(element1, element2);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(elements, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "<element1 />" + System.lineSeparator()
                + "<element2 />" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    @Test
    public void testOutputCDATA() {
        CDATA cdata = new CDATA("<![CDATA[Hello World!]]>");
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        try {
            xmlOutputter.output(cdata, out);
        } catch (IOException e) {
            fail("IOException: " + e.getMessage());
        }
        String expected = "<![CDATA[Hello World!]]>" + System.lineSeparator();
        assertEquals(expected, out.toString());
    }

    // TODO: Add more test cases to cover all methods in XMLOutputter class

    private static final class DefaultXMLOutputProcessor extends AbstractXMLOutputProcessor {

        public String escapeAttributeEntities(final String str, final Format format) {
            final StringWriter sw = new StringWriter();
            try {
                super.attributeEscapedEntitiesFilter(sw, new FormatStack(format), str);
            } catch (IOException ex) {
                fail("IOException: " + ex.getMessage());
            }
            return sw.toString();
        }

        public final String escapeElementEntities(final String str, final Format format) {
            return Format.escapeText(format.getEscapeStrategy(), format.getLineSeparator(), str);
        }
    }

}