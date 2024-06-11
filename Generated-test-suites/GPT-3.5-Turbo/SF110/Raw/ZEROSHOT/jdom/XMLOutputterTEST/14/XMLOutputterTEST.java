import org.jdom2.*;
import org.jdom2.output.XMLOutputter;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

public class XMLOutputterTest {

    @Test
    public void testConstructor1() {
        XMLOutputter outputter = new XMLOutputter();
        assertNotNull(outputter);
        assertNull(outputter.getFormat());
        assertNotNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructor2() {
        Format format = Format.getRawFormat();
        XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(format, processor);
        assertNotNull(outputter);
        assertEquals(format, outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructor3() {
        Format format = Format.getRawFormat();
        XMLOutputter outputter = new XMLOutputter(format);
        assertNotNull(outputter);
        assertEquals(format, outputter.getFormat());
        assertNotNull(outputter.getXMLOutputProcessor());
    }

    @Test
    public void testConstructor4() {
        XMLOutputProcessor processor = new XMLOutputter.DefaultXMLProcessor();
        XMLOutputter outputter = new XMLOutputter(processor);
        assertNotNull(outputter);
        assertNull(outputter.getFormat());
        assertEquals(processor, outputter.getXMLOutputProcessor());
    }

    @Test
    public void testSetFormat() {
        Format format = Format.getRawFormat();
        XMLOutputter outputter = new XMLOutputter();
        outputter.setFormat(format);
        assertEquals(format, outputter.getFormat());
    }

    @Test
    public void testOutputString() {
        Element element = new Element("root");
        Document doc = new Document(element);

        XMLOutputter outputter = new XMLOutputter();
        String expectedOutput = "<root></root>";
        String output = outputter.outputString(doc);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOutput() throws IOException {
        Element element = new Element("root");
        Document doc = new Document(element);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.output(doc, byteArrayOutputStream);

        String expectedOutput = "<root></root>";
        String output = byteArrayOutputStream.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOutputElementContent() throws IOException {
        Element element = new Element("root");
        element.addContent(new Element("child1").setText("text1"));
        element.addContent(new Element("child2").setText("text2"));

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        XMLOutputter outputter = new XMLOutputter();
        outputter.outputElementContent(element, byteArrayOutputStream);

        String expectedOutput = "<child1>text1</child1><child2>text2</child2>";
        String output = byteArrayOutputStream.toString();
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testOutputStringWithIOException() {
        Element element = new Element("root");
        Document doc = new Document(element);

        XMLOutputter outputter = new XMLOutputter();

        // Custom XMLOutputProcessor that throws an IOException
        XMLOutputProcessor processor = new XMLOutputProcessor() {
            @Override
            public void process(Writer out, Format format, Object obj) throws IOException {
                throw new IOException();
            }
        };

        outputter.setXMLOutputProcessor(processor);

        String output = outputter.outputString(doc);
        assertEquals("", output);
    }

    @Test
    public void testOutputWithIOException() {
        Element element = new Element("root");
        Document doc = new Document(element);

        XMLOutputter outputter = new XMLOutputter();

        // Custom XMLOutputProcessor that throws an IOException
        XMLOutputProcessor processor = new XMLOutputProcessor() {
            @Override
            public void process(Writer out, Format format, Object obj) throws IOException {
                throw new IOException();
            }
        };

        outputter.setXMLOutputProcessor(processor);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        outputter.output(doc, byteArrayOutputStream);

        String output = byteArrayOutputStream.toString();
        assertEquals("", output);
    }

    @Test
    public void testEscapeAttributeEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "Hello World";
        String expectedOutput = "Hello World";
        String output = outputter.escapeAttributeEntities(str);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testEscapeElementEntities() {
        XMLOutputter outputter = new XMLOutputter();
        String str = "Hello <b>World</b>";
        String expectedOutput = "Hello &lt;b&gt;World&lt;/b&gt;";
        String output = outputter.escapeElementEntities(str);
        assertEquals(expectedOutput, output);
    }

    @Test
    public void testClone() {
        XMLOutputter outputter = new XMLOutputter();
        XMLOutputter clonedOutputter = outputter.clone();
        assertEquals(outputter.getFormat(), clonedOutputter.getFormat());
        assertEquals(outputter.getXMLOutputProcessor(), clonedOutputter.getXMLOutputProcessor());
    }
}