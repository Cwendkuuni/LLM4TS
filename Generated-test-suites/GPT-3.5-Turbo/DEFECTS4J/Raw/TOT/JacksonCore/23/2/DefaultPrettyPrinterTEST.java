import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.PrettyPrinter;
import com.fasterxml.jackson.core.util.DefaultPrettyPrinter;

public class DefaultPrettyPrinterTest {
  
  DefaultPrettyPrinter prettyPrinter;

  @Before
  public void setup() {
    prettyPrinter = new DefaultPrettyPrinter();
  }

  @Test
  public void testWriteRootValueSeparator() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeRootValueSeparator(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteStartObject() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeStartObject(generator);
    // Assert expected behavior
  }

  @Test
  public void testBeforeObjectEntries() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.beforeObjectEntries(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteObjectFieldValueSeparator() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeObjectFieldValueSeparator(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteObjectEntrySeparator() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeObjectEntrySeparator(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteEndObject() throws Exception {
    JsonGenerator generator = null;
    int nrOfEntries = 0;
    prettyPrinter.writeEndObject(generator, nrOfEntries);
    // Assert expected behavior
  }

  @Test
  public void testWriteStartArray() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeStartArray(generator);
    // Assert expected behavior
  }

  @Test
  public void testBeforeArrayValues() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.beforeArrayValues(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteArrayValueSeparator() throws Exception {
    JsonGenerator generator = null;
    prettyPrinter.writeArrayValueSeparator(generator);
    // Assert expected behavior
  }

  @Test
  public void testWriteEndArray() throws Exception {
    JsonGenerator generator = null;
    int nrOfValues = 0;
    prettyPrinter.writeEndArray(generator, nrOfValues);
    // Assert expected behavior
  }
}