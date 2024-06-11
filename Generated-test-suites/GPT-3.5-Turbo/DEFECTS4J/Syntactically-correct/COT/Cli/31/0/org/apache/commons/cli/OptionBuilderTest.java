import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(1, option.getArgs());
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(1, option.getArgs());
    
    OptionBuilder.hasArg(false);
    option = OptionBuilder.create();
    org.junit.Assert.assertEquals(Option.UNINITIALIZED, option.getArgs());
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("test");
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals("test", option.getArgName());
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertTrue(option.isRequired());
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(':', option.getValueSeparator());
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals('=', option.getValueSeparator());
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder.isRequired(true);
    Option option = OptionBuilder.create();
    org.junit.Assert.assertTrue(option.isRequired());
    
    OptionBuilder.isRequired(false);
    option = OptionBuilder.create();
    org.junit.Assert.assertFalse(option.isRequired());
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
  }
  
  @Test
  public void testHasArgsWithNum() {
    OptionBuilder.hasArgs(2);
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(2, option.getArgs());
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(1, option.getArgs());
    org.junit.Assert.assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    org.junit.Assert.assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testHasOptionalArgsWithNum() {
    OptionBuilder.hasOptionalArgs(3);
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(3, option.getArgs());
    org.junit.Assert.assertTrue(option.hasOptionalArg());
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals(String.class, option.getType());
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test");
    Option option = OptionBuilder.create();
    org.junit.Assert.assertEquals("test", option.getDescription());
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testCreateWithCharInvalid() {
    OptionBuilder.create('*');
  }
  
  @Test(expected=IllegalArgumentException.class)
  public void testCreateWithoutLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithChar() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create('t');
    
    org.junit.Assert.assertEquals("t", option.getOpt());
    org.junit.Assert.assertEquals("test", option.getLongOpt());
  }
  
  @Test
  public void testCreateWithString() {
    OptionBuilder.withLongOpt("test");
    Option option = OptionBuilder.create("t");
    
    org.junit.Assert.assertEquals("t", option.getOpt());
    org.junit.Assert.assertEquals("test", option.getLongOpt());
  }
}