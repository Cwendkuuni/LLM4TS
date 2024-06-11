import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.io.IOException;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.FixedSpaceIndenter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter.NopIndenter;

public class DefaultPrettyPrinterTest {

  private DefaultPrettyPrinter printer;
  
  @Before
  public void setUp() {
    printer = new DefaultPrettyPrinter();
  }
  
  @Test
  public void testWriteRootValueSeparator() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeRootValueSeparator(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteStartObject() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeStartObject(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testBeforeObjectEntries() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.beforeObjectEntries(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteObjectFieldValueSeparator() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeObjectFieldValueSeparator(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteObjectEntrySeparator() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeObjectEntrySeparator(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteEndObject() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    int nrOfEntries = 0; // Set number of entries
    printer.writeEndObject(g, nrOfEntries);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteStartArray() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeStartArray(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testBeforeArrayValues() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.beforeArrayValues(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteArrayValueSeparator() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    printer.writeArrayValueSeparator(g);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWriteEndArray() throws IOException {
    JsonGenerator g = null; // Create instance of JsonGenerator
    int nrOfValues = 0; // Set number of values
    printer.writeEndArray(g, nrOfValues);
    // TODO: Assert expected result
  }

  @Test
  public void testCreateInstance() {
    DefaultPrettyPrinter newPrinter = printer.createInstance();
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
  
  @Test
  public void testIndentArraysWith() {
    DefaultPrettyPrinter.Indenter i = null; // Create instance of Indenter
    printer.indentArraysWith(i);
    // TODO: Assert expected result
  }
  
  @Test
  public void testIndentObjectsWith() {
    DefaultPrettyPrinter.Indenter i = null; // Create instance of Indenter
    printer.indentObjectsWith(i);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWithArrayIndenter() {
    DefaultPrettyPrinter.Indenter i = null; // Create instance of Indenter
    DefaultPrettyPrinter newPrinter = printer.withArrayIndenter(i);
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWithObjectIndenter() {
    DefaultPrettyPrinter.Indenter i = null; // Create instance of Indenter
    DefaultPrettyPrinter newPrinter = printer.withObjectIndenter(i);
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWithSpacesInObjectEntries() {
    DefaultPrettyPrinter newPrinter = printer.withSpacesInObjectEntries();
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWithoutSpacesInObjectEntries() {
    DefaultPrettyPrinter newPrinter = printer.withoutSpacesInObjectEntries();
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
  
  @Test
  public void testWithSeparators() {
    DefaultPrettyPrinter.Separators separators = null; // Create instance of Separators
    DefaultPrettyPrinter newPrinter = printer.withSeparators(separators);
    assertNotNull(newPrinter);
    assertNotSame(printer, newPrinter);
    // TODO: Assert expected result
  }
}