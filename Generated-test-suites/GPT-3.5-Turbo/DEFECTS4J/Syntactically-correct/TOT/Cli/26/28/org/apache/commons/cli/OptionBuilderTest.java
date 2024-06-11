import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

   @Test
   public void testWithLongOpt_typical() {
      // typical use case
      Option option = OptionBuilder.withLongOpt("input").create();
      assertEquals("input", option.getLongOpt());
   }
   
   @Test
   public void testWithLongOpt_edge() {
      // edge case: longopt is null
      Option option = OptionBuilder.withLongOpt(null).create();
      assertNull(option.getLongOpt());
   }
   
   @Test
   public void testHasArg_typical() {
      // typical use case
      Option option = OptionBuilder.hasArg().create();
      assertTrue(option.hasArg());
      assertEquals(1, option.getArgs());
   }
   
   @Test
   public void testHasArg_edge() {
      // edge case: hasArg is false
      Option option = OptionBuilder.hasArg(false).create();
      assertFalse(option.hasArg());
      assertEquals(Option.UNINITIALIZED, option.getArgs());
   }
   
   @Test
   public void testWithArgName_typical() {
      // typical use case
      Option option = OptionBuilder.withArgName("fileName").create();
      assertEquals("fileName", option.getArgName());
   }
   
   @Test
   public void testWithArgName_edge() {
      // edge case: argName is null
      Option option = OptionBuilder.withArgName(null).create();
      assertNull(option.getArgName());
   }
   
   @Test
   public void testIsRequired_typical() {
      // typical use case
      Option option = OptionBuilder.isRequired().create();
      assertTrue(option.isRequired());
   }
   
   @Test
   public void testIsRequired_edge() {
      // edge case: required is false
      Option option = OptionBuilder.isRequired(false).create();
      assertFalse(option.isRequired());
   }
   
   @Test
   public void testWithValueSeparator_typical() {
      // typical use case
      Option option = OptionBuilder.withValueSeparator(':').create();
      assertEquals(':', option.getValueSeparator());
   }
   
   @Test
   public void testWithValueSeparator_edge() {
      // edge case: valuesep is 0
      Option option = OptionBuilder.withValueSeparator((char) 0).create();
      assertEquals((char) 0, option.getValueSeparator());
   }
   
   @Test
   public void testWithOptionalArg_typical() {
      // typical use case
      Option option = OptionBuilder.hasOptionalArg().create();
      assertTrue(option.hasOptionalArg());
      assertEquals(1, option.getArgs());
   }
   
   @Test
   public void testWithOptionalArg_edge() {
      // edge case: optionalArg is false
      Option option = OptionBuilder.hasOptionalArg(false).create();
      assertFalse(option.hasOptionalArg());
      assertEquals(Option.UNINITIALIZED, option.getArgs());
   }
   
   @Test
   public void testHasArgs_typical() {
      // typical use case
      Option option = OptionBuilder.hasArgs().create();
      assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
   }
   
   @Test
   public void testHasArgs_edge() {
      // edge case: num is 0
      Option option = OptionBuilder.hasArgs(0).create();
      assertEquals(0, option.getArgs());
   }
   
   @Test
   public void testWithOptionalArgs_typical() {
      // typical use case
      Option option = OptionBuilder.hasOptionalArgs().create();
      assertTrue(option.hasOptionalArg());
      assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
   }
   
   @Test
   public void testWithOptionalArgs_edge() {
      // edge case: numArgs is 0
      Option option = OptionBuilder.hasOptionalArgs(0).create();
      assertFalse(option.hasOptionalArg());
      assertEquals(0, option.getArgs());
   }
   
   @Test
   public void testWithType_typical() {
      // typical use case
      Option option = OptionBuilder.withType(Integer.class).create();
      assertEquals(Integer.class, option.getType());
   }
   
   @Test
   public void testWithType_edge() {
      // edge case: type is null
      Option option = OptionBuilder.withType(null).create();
      assertNull(option.getType());
   }
   
   @Test
   public void testWithDescription_typical() {
      // typical use case
      Option option = OptionBuilder.withDescription("This is a test option.").create();
      assertEquals("This is a test option.", option.getDescription());
   }
   
   @Test
   public void testWithDescription_edge() {
      // edge case: description is null
      Option option = OptionBuilder.withDescription(null).create();
      assertNull(option.getDescription());
   }
   
   @Test
   public void testCreate_typical() {
      // typical use case
      String longopt = "input";
      Option option = OptionBuilder.withLongOpt(longopt).hasArg().withArgName("fileName")
                                 .isRequired().withValueSeparator(':').create('i');
      assertEquals('i', option.getOpt());
      assertEquals(longopt, option.getLongOpt());
      assertTrue(option.hasArg());
      assertEquals(1, option.getArgs());
      assertEquals("fileName", option.getArgName());
      assertTrue(option.isRequired());
      assertEquals(':', option.getValueSeparator());
   }
   
   @Test(expected = IllegalArgumentException.class)
   public void testCreate_edge() {
      // edge case: longopt is null
      OptionBuilder.create();
   }
   
}