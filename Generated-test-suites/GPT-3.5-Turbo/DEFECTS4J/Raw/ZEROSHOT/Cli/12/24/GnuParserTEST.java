import org.junit.Test;
import org.junit.Assert;

public class GnuParserTest {
  
  @Test
  public void testFlattenWithNoArguments() {
    Options options = new Options();
    String[] arguments = {};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(0, result.length);
  }
  
  @Test
  public void testFlattenWithValidOption() {
    Options options = new Options();
    options.addOption("a");
    String[] arguments = {"-a"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(1, result.length);
    Assert.assertEquals("-a", result[0]);
  }
  
  @Test
  public void testFlattenWithInvalidOption() {
    Options options = new Options();
    options.addOption("a");
    String[] arguments = {"-b"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(1, result.length);
    Assert.assertEquals("-b", result[0]);
  }
  
  @Test
  public void testFlattenWithLongOption() {
    Options options = new Options();
    options.addOption("a");
    options.addOption("long");
    String[] arguments = {"--long"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(1, result.length);
    Assert.assertEquals("--long", result[0]);
  }
  
  @Test
  public void testFlattenWithLongOptionWithValue() {
    Options options = new Options();
    options.addOption("a");
    options.addOption("long", true, "Long option");
    String[] arguments = {"--long=value"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(2, result.length);
    Assert.assertEquals("--long", result[0]);
    Assert.assertEquals("value", result[1]);
  }
  
  @Test
  public void testFlattenWithStopAtNonOption() {
    Options options = new Options();
    options.addOption("a");
    String[] arguments = {"-a", "non-option", "value"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, true);
    
    Assert.assertEquals(3, result.length);
    Assert.assertEquals("-a", result[0]);
    Assert.assertEquals("non-option", result[1]);
    Assert.assertEquals("value", result[2]);
  }
  
  @Test
  public void testFlattenWithDoubleDash() {
    Options options = new Options();
    options.addOption("a");
    String[] arguments = {"--", "-a"};
    GnuParser parser = new GnuParser();
    
    String[] result = parser.flatten(options, arguments, false);
    
    Assert.assertEquals(2, result.length);
    Assert.assertEquals("--", result[0]);
    Assert.assertEquals("-a", result[1]);
  }
}