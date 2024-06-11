import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("test");
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
  }
  
  @Test
  public void testHasArgWithBoolean() {
    OptionBuilder.hasArg(true);
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("arg");
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
  }
  
  @Test
  public void testWithValueSeparatorChar() {
    OptionBuilder.withValueSeparator(':');
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator();
  }
  
  @Test
  public void testIsRequiredWithBoolean() {
    OptionBuilder.isRequired(true);
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
  }
  
  @Test
  public void testHasArgsInt() {
    OptionBuilder.hasArgs(2);
  }
  
  @Test
  public void testHasOptionalArg() {
    OptionBuilder.hasOptionalArg();
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
  }
  
  @Test
  public void testHasOptionalArgsInt() {
    OptionBuilder.hasOptionalArgs(3);
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("test option");
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithInvalidOpt() {
    OptionBuilder.create('%');
  }
  
  @Test(expected = IllegalArgumentException.class)
  public void testCreateWithNullLongOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithValidOpt() {
    OptionBuilder.create("t");
  }
}