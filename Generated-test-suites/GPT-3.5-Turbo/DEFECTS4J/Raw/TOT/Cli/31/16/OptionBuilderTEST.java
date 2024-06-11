import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

   @Test
   public void testWithLongOpt() {
      OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longOpt");
      assertEquals("longOpt", optionBuilder.getOption().getLongOpt());
   }

   @Test
   public void testHasArg() {
      OptionBuilder optionBuilder = OptionBuilder.hasArg();
      assertEquals(1, optionBuilder.getOption().getArgs());
   }

   @Test
   public void testHasArgWithBooleanTrue() {
      OptionBuilder optionBuilder = OptionBuilder.hasArg(true);
      assertEquals(1, optionBuilder.getOption().getArgs());
   }

   @Test
   public void testHasArgWithBooleanFalse() {
      OptionBuilder optionBuilder = OptionBuilder.hasArg(false);
      assertEquals(Option.UNINITIALIZED, optionBuilder.getOption().getArgs());
   }

   @Test
   public void testWithArgName() {
      OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
      assertEquals("argName", optionBuilder.getOption().getArgName());
   }

   @Test
   public void testIsRequired() {
      OptionBuilder optionBuilder = OptionBuilder.isRequired();
      assertTrue(optionBuilder.getOption().isRequired());
   }

   @Test
   public void testWithValueSeparatorChar() {
      OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
      assertEquals(':', optionBuilder.getOption().getValueSeparator());
   }

   @Test
   public void testWithValueSeparator() {
      OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
      assertEquals('=', optionBuilder.getOption().getValueSeparator());
   }

   @Test
   public void testIsRequiredWithBooleanTrue() {
      OptionBuilder optionBuilder = OptionBuilder.isRequired(true);
      assertTrue(optionBuilder.getOption().isRequired());
   }

   @Test
   public void testIsRequiredWithBooleanFalse() {
      OptionBuilder optionBuilder = OptionBuilder.isRequired(false);
      assertFalse(optionBuilder.getOption().isRequired());
   }

   @Test
   public void testHasArgs() {
      OptionBuilder optionBuilder = OptionBuilder.hasArgs();
      assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getOption().getArgs());
   }

   @Test
   public void testHasArgsWithNum() {
      OptionBuilder optionBuilder = OptionBuilder.hasArgs(3);
      assertEquals(3, optionBuilder.getOption().getArgs());
   }

   @Test
   public void testHasOptionalArg() {
      OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
      assertEquals(1, optionBuilder.getOption().getArgs());
      assertTrue(optionBuilder.getOption().hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgs() {
      OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
      assertEquals(Option.UNLIMITED_VALUES, optionBuilder.getOption().getArgs());
      assertTrue(optionBuilder.getOption().hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgsWithNumArgs() {
      OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(4);
      assertEquals(4, optionBuilder.getOption().getArgs());
      assertTrue(optionBuilder.getOption().hasOptionalArg());
   }

   @Test
   public void testWithType() {
      OptionBuilder optionBuilder = OptionBuilder.withType(Integer.class);
      assertEquals(Integer.class, optionBuilder.getOption().getType());
   }

   @Test
   public void testWithDescription() {
      OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
      assertEquals("description", optionBuilder.getOption().getDescription());
   }

   @Test
   public void testCreateChar() {
      OptionBuilder optionBuilder = OptionBuilder.create('c');
      assertEquals("c", optionBuilder.getOption().getOpt());
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreateChar_InvalidChar() {
      OptionBuilder.create('1');
   }

   @Test(expected = IllegalArgumentException.class)
   public void testCreate_NoLongOpt() {
      OptionBuilder.create();
   }

   @Test
   public void testCreateString() {
      OptionBuilder optionBuilder = OptionBuilder.create("opt");
      assertEquals("opt", optionBuilder.getOption().getOpt());
   }

   private OptionBuilder getOptionBuilder() {
      // Helper method to build OptionBuilder instance with typical use cases
      return OptionBuilder.withLongOpt("longOpt")
            .hasArg()
            .withArgName("arg")
            .isRequired()
            .withValueSeparator(':')
            .withDescription("description")
            .create('c');
   }

   @Test
   public void testCompleteTest() {
      // Test typical use case scenarios
      OptionBuilder optionBuilder = getOptionBuilder();
      Option option = optionBuilder.getOption();
      assertEquals("longOpt", option.getLongOpt());
      assertEquals(1, option.getArgs());
      assertEquals("arg", option.getArgName());
      assertTrue(option.isRequired());
      assertEquals(':', option.getValueSeparator());
      assertEquals("description", option.getDescription());
      assertEquals("c", option.getOpt());

      // Test edge case scenarios
      optionBuilder = OptionBuilder.create();
      assertNull(optionBuilder.getOption().getOpt());

      optionBuilder = OptionBuilder.hasOptionalArg();
      assertTrue(optionBuilder.getOption().hasOptionalArg());

      optionBuilder = OptionBuilder.create("opt");
      assertEquals("opt", optionBuilder.getOption().getOpt());

      optionBuilder = OptionBuilder.create('x');
      assertEquals("x", optionBuilder.getOption().getOpt());
   }

}