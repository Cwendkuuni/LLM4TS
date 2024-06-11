import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
   
   private WriteableCommandLineImpl commandLine;
   
   @Before
   public void setUp() {
      Option option1 = new Option("option1");
      Option option2 = new Option("option2");
      Argument argument1 = new Argument("argument1");
      
      commandLine = new WriteableCommandLineImpl(option1, argument1);
      commandLine.addOption(option2);
   }
   
   @Test
   public void testAddOption() {
      Option option3 = new Option("option3");
      commandLine.addOption(option3);
      
      assertTrue(commandLine.hasOption(option3));
   }
   
   @Test
   public void testAddValue() {
      Option option1 = new Option("option1");
      commandLine.addValue(option1, "value1");
      
      assertEquals("value1", commandLine.getValues(option1, null).get(0));
   }
   
   @Test
   public void testAddSwitch() {
      Option option2 = new Option("option2");
      commandLine.addSwitch(option2, true);
      
      assertTrue(commandLine.getSwitch(option2, false));
   }
   
   @Test
   public void testHasOption() {
      Option option1 = new Option("option1");
      assertTrue(commandLine.hasOption(option1));
   }
   
   @Test
   public void testGetOption() {
      Option option2 = new Option("option2");
      assertEquals(option2, commandLine.getOption("option2"));
   }
   
   @Test
   public void testGetValues() {
      Option option1 = new Option("option1");
      commandLine.addValue(option1, "value1");
      
      assertEquals("value1", commandLine.getValues(option1, null).get(0));
   }
   
   @Test
   public void testGetSwitch() {
      Option option2 = new Option("option2");
      commandLine.addSwitch(option2, false);
      
      assertFalse(commandLine.getSwitch(option2, true));
   }
   
   @Test
   public void testAddProperty() {
      commandLine.addProperty("property1", "value1");
      
      assertEquals("value1", commandLine.getProperty("property1", null));
   }
   
   @Test
   public void testLooksLikeOption() {
      assertTrue(commandLine.looksLikeOption("-option1"));
   }
   
   @Test
   public void testToString() {
      commandLine.addValue(argument1, "value1");
      commandLine.addValue(option2, "value2");
      
      assertEquals("value1 value2", commandLine.toString());
   }
   
   @Test
   public void testGetOptions() {
      List options = commandLine.getOptions();
      
      assertEquals(2, options.size());
   }
   
   @Test
   public void testGetOptionTriggers() {
      Set triggers = commandLine.getOptionTriggers();
      
      assertTrue(triggers.contains("option1"));
      assertTrue(triggers.contains("option2"));
   }
   
   @Test
   public void testSetDefaultValues() {
      Option option3 = new Option("option3");
      List defaults = new ArrayList();
      defaults.add("default1");
      defaults.add("default2");
      
      commandLine.setDefaultValues(option3, defaults);
      
      assertEquals(defaults, commandLine.getValues(option3, null));
   }
   
   @Test
   public void testSetDefaultSwitch() {
      Option option2 = new Option("option2");
      commandLine.setDefaultSwitch(option2, true);
      
      assertTrue(commandLine.getSwitch(option2, false));
   }
   
   @Test
   public void testGetNormalised() {
      Argument argument1 = new Argument("argument1");
      commandLine.addValue(argument1, "value1");
      
      List normalised = commandLine.getNormalised();
      
      assertEquals(1, normalised.size());
      assertEquals("value1", normalised.get(0));
   }
}