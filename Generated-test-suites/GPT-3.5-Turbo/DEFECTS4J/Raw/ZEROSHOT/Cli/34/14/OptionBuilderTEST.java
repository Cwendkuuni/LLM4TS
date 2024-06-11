import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

   @Test
   public void testWithLongOpt() {
      OptionBuilder.withLongOpt("test");
      assertEquals("test", OptionBuilder.getLongOpt());
   }

   @Test
   public void testHasArg() {
      OptionBuilder.hasArg();
      assertEquals(1, OptionBuilder.getNumberOfArgs());
   }

   @Test
   public void testHasArgBoolean() {
      OptionBuilder.hasArg(true);
      assertEquals(1, OptionBuilder.getNumberOfArgs());
      OptionBuilder.hasArg(false);
      assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
   }

   @Test
   public void testWithArgName() {
      OptionBuilder.withArgName("test");
      assertEquals("test", OptionBuilder.getArgName());
   }

   @Test
   public void testIsRequired() {
      OptionBuilder.isRequired();
      assertTrue(OptionBuilder.isRequired());
   }

   @Test
   public void testWithValueSeparatorChar() {
      OptionBuilder.withValueSeparator(':');
      assertEquals(':', OptionBuilder.getValueSeparator());
   }

   @Test
   public void testWithValueSeparator() {
      OptionBuilder.withValueSeparator();
      assertEquals('=', OptionBuilder.getValueSeparator());
   }

   @Test
   public void testIsRequiredBoolean() {
      OptionBuilder.isRequired(false);
      assertFalse(OptionBuilder.isRequired());
   }

   @Test
   public void testHasArgs() {
      OptionBuilder.hasArgs();
      assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
   }

   @Test
   public void testHasArgsInt() {
      OptionBuilder.hasArgs(3);
      assertEquals(3, OptionBuilder.getNumberOfArgs());
   }

   @Test
   public void testHasOptionalArg() {
      OptionBuilder.hasOptionalArg();
      assertEquals(1, OptionBuilder.getNumberOfArgs());
      assertTrue(OptionBuilder.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgs() {
      OptionBuilder.hasOptionalArgs();
      assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
      assertTrue(OptionBuilder.hasOptionalArg());
   }

   @Test
   public void testHasOptionalArgsInt() {
      OptionBuilder.hasOptionalArgs(5);
      assertEquals(5, OptionBuilder.getNumberOfArgs());
      assertTrue(OptionBuilder.hasOptionalArg());
   }

   @Test
   public void testWithType() {
      OptionBuilder.withType(Integer.class);
      assertEquals(Integer.class, OptionBuilder.getType());
   }

   @Test
   public void testWithDescription() {
      OptionBuilder.withDescription("test");
      assertEquals("test", OptionBuilder.getDescription());
   }

   @Test
   public void testCreateChar() {
      Option option = OptionBuilder.create('t');
      assertEquals("t", option.getOpt());
      assertNull(option.getDescription());
      assertNull(option.getLongOpt());
      assertFalse(option.isRequired());
      assertEquals(Option.UNINITIALIZED, option.getArgs());
      assertEquals(null, option.getType());
      assertEquals((char) 0, option.getValueSeparator());
      assertNull(option.getArgName());
   }

   @Test
   public void testCreate() {
      try {
         OptionBuilder.create();
         fail("IllegalArgumentException should be thrown");
      } catch (IllegalArgumentException e) {
         assertEquals("must specify longopt", e.getMessage());
      }
   }

   @Test
   public void testCreateString() {
      Option option = OptionBuilder.create("test");
      assertEquals("test", option.getOpt());
      assertNull(option.getDescription());
      assertNull(option.getLongOpt());
      assertFalse(option.isRequired());
      assertEquals(Option.UNINITIALIZED, option.getArgs());
      assertEquals(null, option.getType());
      assertEquals((char) 0, option.getValueSeparator());
      assertNull(option.getArgName());
   }
}