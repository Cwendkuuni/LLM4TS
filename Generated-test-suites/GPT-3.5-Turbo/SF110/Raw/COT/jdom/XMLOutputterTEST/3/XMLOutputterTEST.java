import static org.junit.Assert.*;
import org.junit.Test;
import org.jdom2.*;
import java.util.*;
import java.io.*;

public class XMLOutputterTest {

  @Test
  public void testSetFormat() {
    XMLOutputter outputter = new XMLOutputter();
    Format format = Format.getDefaultFormat();
    outputter.setFormat(format);
    assertEquals(format, outputter.getFormat());
  }

  @Test
  public void testGetFormat() {
    XMLOutputter outputter = new XMLOutputter();
    assertNotNull(outputter.getFormat());
  }

  @Test
  public void testGetXMLOutputProcessor() {
    XMLOutputter outputter = new XMLOutputter();
    assertNotNull(outputter.getXMLOutputProcessor());
  }

  @Test
  public void testSetXMLOutputProcessor() {
    XMLOutputter outputter = new XMLOutputter();
    XMLOutputProcessor processor = new XMLOutputProcessor();
    outputter.setXMLOutputProcessor(processor);
    assertEquals(processor, outputter.getXMLOutputProcessor());
  }

  @Test
  public void testOutput() {
    XMLOutputter outputter = new XMLOutputter();
    Document doc = new Document();
    try {
      outputter.output(doc, System.out);
    } catch (IOException e) {
      fail("Exception thrown");
    }
  }

  @Test
  public void testOutputString() {
    XMLOutputter outputter = new XMLOutputter();
    Document doc = new Document();
    String output = outputter.outputString(doc);
    assertNotNull(output);
  }

  @Test
  public void testOutputElementContentString() {
    XMLOutputter outputter = new XMLOutputter();
    Element element = new Element("test");
    String output = outputter.outputElementContentString(element);
    assertNotNull(output);
  }

  @Test
  public void testEscapeAttributeEntities() {
    XMLOutputter outputter = new XMLOutputter();
    String str = "test str";
    String escapedStr = outputter.escapeAttributeEntities(str);
    assertEquals(str, escapedStr);
  }

  @Test
  public void testEscapeElementEntities() {
    XMLOutputter outputter = new XMLOutputter();
    String str = "test str";
    String escapedStr = outputter.escapeElementEntities(str);
    assertEquals(str, escapedStr);
  }

  @Test
  public void testClone() {
    XMLOutputter outputter = new XMLOutputter();
    XMLOutputter clone = outputter.clone();
    assertNotNull(clone);
    assertNotSame(outputter, clone);
  }

  @Test
  public void testToString() {
    XMLOutputter outputter = new XMLOutputter();
    String toString = outputter.toString();
    assertNotNull(toString);
  }

}