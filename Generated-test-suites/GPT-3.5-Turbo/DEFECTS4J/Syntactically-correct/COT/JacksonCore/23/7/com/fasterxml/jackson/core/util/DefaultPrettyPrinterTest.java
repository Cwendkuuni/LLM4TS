import org.junit.Assert;
import org.junit.Test;

public class DefaultPrettyPrinterTest {

  @Test
  public void testDefaultPrettyPrinter() {
    DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter();
    Assert.assertNotNull(defaultPrettyPrinter);
  }

  @Test
  public void testDefaultPrettyPrinterWithRootSeparator() {
    DefaultPrettyPrinter defaultPrettyPrinter = new DefaultPrettyPrinter("rootSeparator");
    Assert.assertNotNull(defaultPrettyPrinter);
  }

  // Additional test cases for other methods

}