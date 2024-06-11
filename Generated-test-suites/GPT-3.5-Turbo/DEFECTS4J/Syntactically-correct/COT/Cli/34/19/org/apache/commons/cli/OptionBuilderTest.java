import org.junit.Test;

public class OptionBuilderTest {
  
  @Test
  public void testWithLongOpt() {
    OptionBuilder.withLongOpt("testLongOpt");
  }
  
  @Test
  public void testHasArg() {
    OptionBuilder.hasArg();
  }
  
  @Test
  public void testHasArgWithValue() {
    OptionBuilder.hasArg(true);
  }
  
  @Test
  public void testWithArgName() {
    OptionBuilder.withArgName("testArg");
  }
  
  @Test
  public void testIsRequired() {
    OptionBuilder.isRequired();
  }
  
  @Test
  public void testWithValueSeparator() {
    OptionBuilder.withValueSeparator(':');
  }
  
  @Test
  public void testWithDefaultSeparator() {
    OptionBuilder.withValueSeparator();
  }
  
  @Test
  public void testIsOptionalArg() {
    OptionBuilder.isOptionalArg();
  }
  
  @Test
  public void testHasArgs() {
    OptionBuilder.hasArgs();
  }
  
  @Test
  public void testHasArgsWithValue() {
    OptionBuilder.hasArgs(2);
  }
  
  @Test
  public void testHasOptionalArgs() {
    OptionBuilder.hasOptionalArgs();
  }
  
  @Test
  public void testHasOptionalArgsWithValue() {
    OptionBuilder.hasOptionalArgs(3);
  }
  
  @Test
  public void testWithType() {
    OptionBuilder.withType(String.class);
  }
  
  @Test
  public void testWithDescription() {
    OptionBuilder.withDescription("testDescription");
  }
  
  @Test
  public void testCreateWithChar() {
    OptionBuilder.create('c');
  }
  
  @Test
  public void testCreateWithoutOpt() {
    OptionBuilder.create();
  }
  
  @Test
  public void testCreateWithOpt() {
    OptionBuilder.create("testOpt");
  }
}