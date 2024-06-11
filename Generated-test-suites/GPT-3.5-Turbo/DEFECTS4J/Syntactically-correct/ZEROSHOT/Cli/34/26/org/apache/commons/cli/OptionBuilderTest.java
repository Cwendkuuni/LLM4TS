import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

  private OptionBuilder optionBuilder;

  @Before
  public void setUp() {
    optionBuilder = new OptionBuilder();
  }

  @Test
  public void testWithLongOpt() {
    String longOpt = "testOption";
    OptionBuilder result = optionBuilder.withLongOpt(longOpt);
    assertEquals(longOpt, OptionBuilder.longopt);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasArg() {
    OptionBuilder result = optionBuilder.hasArg();
    assertEquals(1, OptionBuilder.numberOfArgs);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasArgWithBoolean() {
    boolean hasArg = true;
    OptionBuilder result = optionBuilder.hasArg(hasArg);
    assertEquals(hasArg ? 1 : Option.UNINITIALIZED, OptionBuilder.numberOfArgs);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testWithArgName() {
    String argName = "testArg";
    OptionBuilder result = optionBuilder.withArgName(argName);
    assertEquals(argName, OptionBuilder.argName);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testIsRequired() {
    OptionBuilder result = optionBuilder.isRequired();
    assertTrue(OptionBuilder.required);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testWithValueSeparator() {
    char sep = ':';
    OptionBuilder result = optionBuilder.withValueSeparator(sep);
    assertEquals(sep, OptionBuilder.valuesep);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testWithValueSeparatorDefault() {
    OptionBuilder result = optionBuilder.withValueSeparator();
    assertEquals('=', OptionBuilder.valuesep);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testIsRequiredWithBoolean() {
    boolean newRequired = true;
    OptionBuilder result = optionBuilder.isRequired(newRequired);
    assertEquals(newRequired, OptionBuilder.required);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasArgs() {
    OptionBuilder result = optionBuilder.hasArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasArgsWithNumber() {
    int num = 3;
    OptionBuilder result = optionBuilder.hasArgs(num);
    assertEquals(num, OptionBuilder.numberOfArgs);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasOptionalArg() {
    OptionBuilder result = optionBuilder.hasOptionalArg();
    assertEquals(1, OptionBuilder.numberOfArgs);
    assertTrue(OptionBuilder.optionalArg);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasOptionalArgs() {
    OptionBuilder result = optionBuilder.hasOptionalArgs();
    assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.numberOfArgs);
    assertTrue(OptionBuilder.optionalArg);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testHasOptionalArgsWithNumber() {
    int numArgs = 2;
    OptionBuilder result = optionBuilder.hasOptionalArgs(numArgs);
    assertEquals(numArgs, OptionBuilder.numberOfArgs);
    assertTrue(OptionBuilder.optionalArg);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testWithType() {
    Class newType = Integer.class;
    OptionBuilder result = optionBuilder.withType(newType);
    assertEquals(newType, OptionBuilder.type);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testWithDescription() {
    String newDescription = "testDescription";
    OptionBuilder result = optionBuilder.withDescription(newDescription);
    assertEquals(newDescription, OptionBuilder.description);
    assertEquals(optionBuilder, result);
  }

  @Test
  public void testCreateWithChar() {
    char opt = 't';
    Option option = optionBuilder.create(opt);
    assertNotNull(option);
    assertEquals(String.valueOf(opt), option.getOpt());
    assertEquals(OptionBuilder.description, option.getDescription());
    assertEquals(OptionBuilder.longopt, option.getLongOpt());
    assertEquals(OptionBuilder.required, option.isRequired());
    assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
    assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
    assertEquals(OptionBuilder.type, option.getType());
    assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
    assertEquals(OptionBuilder.argName, option.getArgName());
  }

  @Test
  public void testCreate() {
    Option option = optionBuilder.create();
    assertNotNull(option);
    assertNull(option.getOpt());
    assertEquals(OptionBuilder.description, option.getDescription());
    assertEquals(OptionBuilder.longopt, option.getLongOpt());
    assertEquals(OptionBuilder.required, option.isRequired());
    assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
    assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
    assertEquals(OptionBuilder.type, option.getType());
    assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
    assertEquals(OptionBuilder.argName, option.getArgName());
  }

  @Test
  public void testCreateWithString() {
    String opt = "testOption";
    Option option = optionBuilder .create(opt);
    assertNotNull(option);
    assertEquals(opt, option.getOpt());
    assertEquals(OptionBuilder.description, option.getDescription());
    assertEquals(OptionBuilder.longopt, option.getLongOpt());
    assertEquals(OptionBuilder.required, option.isRequired());
    assertEquals(OptionBuilder.optionalArg, option.hasOptionalArg());
    assertEquals(OptionBuilder.numberOfArgs, option.getArgs());
    assertEquals(OptionBuilder.type, option.getType());
    assertEquals(OptionBuilder.valuesep, option.getValueSeparator());
    assertEquals(OptionBuilder.argName, option.getArgName());
  }
}