import org.junit.Test;
import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.option.PropertyOption;
import org.apache.commons.cli2.resource.ResourceConstants;
import org.apache.commons.cli2.resource.ResourceHelper;
import org.apache.commons.cli2.commandline.CommandLineImpl;
import org.apache.commons.cli2.commandline.WriteableCommandLineImpl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import static org.junit.Assert.*;

public class WriteableCommandLineImplTest {
   @Test
   public void testAddOption() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addOption(option);
      assertTrue(commandLine.hasOption(option));
   }
   
   @Test
   public void testAddValue() {
      Option option = new Argument();
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      String value = "test";
      commandLine.addValue(option, value);
      assertEquals(value, commandLine.getValues(option, null).get(0));
   }
   
   @Test
   public void testAddSwitch() {
      Option option = new Option("s");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addSwitch(option, true);
      assertTrue(commandLine.getSwitch(option, null));
   }
   
   @Test
   public void testHasOption() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      assertFalse(commandLine.hasOption(option));
   }
   
   @Test
   public void testGetOption() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addOption(option);
      assertEquals(option, commandLine.getOption("t"));
   }
   
   @Test
   public void testGetValues() {
      Option option = new Argument();
      List<String> defaults = new ArrayList<>();
      String defaultValue = "default";
      defaults.add(defaultValue);
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      String value = "test";
      commandLine.addValue(option, value);
      assertEquals(value, commandLine.getValues(option, defaults).get(0));
   }
   
   @Test
   public void testGetUndefaultedValues() {
      Option option = new Argument();
      String value = "test";
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addValue(option, value);
      assertEquals(value, commandLine.getUndefaultedValues(option).get(0));
   }
   
   @Test
   public void testGetSwitch() {
      Option option = new Option("s");
      Boolean defaultValue = true;
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addSwitch(option, true);
      assertEquals(true, commandLine.getSwitch(option, defaultValue));
   }
   
   @Test
   public void testGetProperty() {
      Option option = new PropertyOption();
      String property = "testProperty";
      String value = "testValue";
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addProperty(property, value);
      assertEquals(value, commandLine.getProperty(property));
   }
   
   @Test
   public void testAddProperty() {
      Option option = new PropertyOption();
      String property = "testProperty";
      String value = "testValue";
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addProperty(option, property, value);
      assertEquals(value, commandLine.getProperty(option, property, null));
   }
   
   @Test
   public void testGetProperties() {
      Option option = new PropertyOption();
      String property = "testProperty";
      String value = "testValue";
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addProperty(option, property, value);
      assertTrue(commandLine.getProperties(option).contains(property));
   }
   
   @Test
   public void testGetOptions() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addOption(option);
      assertTrue(commandLine.getOptions().contains(option));
   }
   
   @Test
   public void testGetOptionTriggers() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.addOption(option);
      assertTrue(commandLine.getOptionTriggers().contains(option.getPreferredName()));
   }
   
   @Test
   public void testSetDefaultValues() {
      Option option = new Option("t");
      List<String> defaults = new ArrayList<>();
      String defaultValue = "default";
      defaults.add(defaultValue);
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.setDefaultValues(option, defaults);
      assertEquals(defaults, commandLine.getValues(option, null));
   }
   
   @Test
   public void testSetDefaultSwitch() {
      Option option = new Option("s");
      Boolean defaultSwitch = true;
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      commandLine.setDefaultSwitch(option, defaultSwitch);
      assertEquals(defaultSwitch, commandLine.getSwitch(option, null));
   }
   
   @Test
   public void testGetNormalised() {
      Option option = new Option("t");
      WriteableCommandLineImpl commandLine = new WriteableCommandLineImpl(option, new ArrayList());
      List<String> arguments = new ArrayList<>();
      arguments.add("arg1");
      arguments.add("arg2");
      commandLine.addArgumentValues(arguments);
      assertEquals(arguments, commandLine.getNormalised());
   }
}