import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionBuilderTest {

   private OptionBuilder optionBuilder;

   @Before
   public void setup() {
       optionBuilder = new OptionBuilder();
   }

   @Test
   public void testWithLongOpt() {
       OptionBuilder optionBuilder1 = OptionBuilder.withLongOpt("testLongOpt");
       assertNotNull(optionBuilder1);
       assertEquals("testLongOpt", optionBuilder1.getLongopt());
   }

   @Test
   public void testHasArg() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasArg();
       assertNotNull(optionBuilder1);
       assertEquals(1, optionBuilder1.getNumberOfArgs());
   }

   @Test
   public void testHasArgWithTrue() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasArg(true);
       assertNotNull(optionBuilder1);
       assertEquals(1, optionBuilder1.getNumberOfArgs());
   }

   @Test
   public void testHasArgWithFalse() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasArg(false);
       assertNotNull(optionBuilder1);
       assertEquals(Option.UNINITIALIZED, optionBuilder1.getNumberOfArgs());
   }

   @Test
   public void testWithArgName() {
       OptionBuilder optionBuilder1 = OptionBuilder.withArgName("testArgName");
       assertNotNull(optionBuilder1);
       assertEquals("testArgName", optionBuilder1.getArgName());
   }

   @Test
   public void testIsRequired() {
       OptionBuilder optionBuilder1 = OptionBuilder.isRequired();
       assertNotNull(optionBuilder1);
       assertTrue(optionBuilder1.getRequired());
   }

   @Test
   public void testWithSeparator() {
       OptionBuilder optionBuilder1 = OptionBuilder.withValueSeparator(':');
       assertNotNull(optionBuilder1);
       assertEquals(':', optionBuilder1.getValuesep());
   }

   @Test
   public void testWithSeparatorDefault() {
       OptionBuilder optionBuilder1 = OptionBuilder.withValueSeparator();
       assertNotNull(optionBuilder1);
       assertEquals('=', optionBuilder1.getValuesep());
   }

   @Test
   public void testIsRequiredWithTrue() {
       OptionBuilder optionBuilder1 = OptionBuilder.isRequired(true);
       assertNotNull(optionBuilder1);
       assertTrue(optionBuilder1.getRequired());
   }

   @Test
   public void testHasArgs() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasArgs();
       assertNotNull(optionBuilder1);
       assertEquals(Option.UNLIMITED_VALUES, optionBuilder1.getNumberOfArgs());
   }

   @Test
   public void testHasArgsWithNum() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasArgs(2);
       assertNotNull(optionBuilder1);
       assertEquals(2, optionBuilder1.getNumberOfArgs());
   }

   @Test
   public void testHasOptionalArg() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasOptionalArg();
       assertNotNull(optionBuilder1);
       assertEquals(1, optionBuilder1.getNumberOfArgs());
       assertTrue(optionBuilder1.getOptionalArg());
   }

   @Test
   public void testHasOptionalArgs() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasOptionalArgs();
       assertNotNull(optionBuilder1);
       assertEquals(Option.UNLIMITED_VALUES, optionBuilder1.getNumberOfArgs());
       assertTrue(optionBuilder1.getOptionalArg());
   }

   @Test
   public void testHasOptionalArgsWithNumArgs() {
       OptionBuilder optionBuilder1 = OptionBuilder.hasOptionalArgs(3);
       assertNotNull(optionBuilder1);
       assertEquals(3, optionBuilder1.getNumberOfArgs());
       assertTrue(optionBuilder1.getOptionalArg());
   }

   @Test
   public void testWithType() {
       OptionBuilder optionBuilder1 = OptionBuilder.withType(Integer.class);
       assertNotNull(optionBuilder1);
       assertEquals(Integer.class, optionBuilder1.getType());
   }

   @Test
   public void testWithDescription() {
       OptionBuilder optionBuilder1 = OptionBuilder.withDescription("Test Description");
       assertNotNull(optionBuilder1);
       assertEquals("Test Description", optionBuilder1.getDescription());
   }

   @Test
   public void testCreateWithChar() {
       Option option = OptionBuilder.create('O');
       assertNotNull(option);
       assertEquals("O", option.getOpt());
   }

   @Test
   public void testCreate() {
       optionBuilder.withLongOpt("testLongOpt");
       Option option = OptionBuilder.create();
       assertNotNull(option);
       assertEquals("testLongOpt", option.getLongOpt());
   }

   @Test
   public void testCreateWithString() {
       Option option = OptionBuilder.create("O");
       assertNotNull(option);
       assertEquals("O", option.getOpt());
   }
}