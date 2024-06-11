import org.junit.Assert;
import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
    Assert.assertEquals("testLongOpt", OptionBuilder.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testHasArgWithFlag() {
    OptionBuilder.hasArg(true);
    Assert.assertEquals(1, OptionBuilder.getNumberOfArgs());
    
    OptionBuilder.hasArg(false);
    Assert.assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testArgName");
    Assert.assertEquals("testArgName", OptionBuilder.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Assert.assertTrue(OptionBuilder.isRequired());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
    Assert.assertEquals(':', OptionBuilder.getValueSeparator());
  }
  
  @Test
  public void testCreateChar() {
    Option option = OptionBuilder.create('t');
    Assert.assertEquals("t", option.getOpt());
  }
  
  @Test
  public void testCreate() {
    OptionBuilder.withLongOpt("testLongOpt");
    Option option = OptionBuilder.create();
    Assert.assertEquals("testLongOpt", option.getLongOpt());
  }
  
  @Test
  public void testCreateString() {
    Option option = OptionBuilder.create("testOpt");
    Assert.assertEquals("testOpt", option.getOpt());
  }
}