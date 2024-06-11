import org.junit.Test;
import static org.junit.Assert.*;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Option;
import java.util.List;
import java.util.Collection;

public class OptionsTest {
   @Test
   public void testAddOption() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = new Option("d", "directory", true, "input directory");
      options.addOption(option2);
      
      Collection<Option> optionList = options.getOptions();
      assertTrue(optionList.contains(option1));
      assertTrue(optionList.contains(option2));
   }
   
   @Test
   public void testAddOptionGroup() {
      Options options = new Options();
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = new Option("d", "directory", false, "input directory");
      options.addOption(option2);
      
      Option option3 = new Option("r", "recursive", false, "recursive option");
      
      OptionGroup optionGroup = new OptionGroup();
      optionGroup.addOption(option2);
      optionGroup.addOption(option3);
      
      options.addOptionGroup(optionGroup);
      
      assertTrue(optionGroup.isRequired());
      
      Collection<OptionGroup> groupList = options.getOptionGroups();
      assertTrue(groupList.contains(optionGroup));
   }
   
   @Test
   public void testGetOption() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = options.getOption("f");
      assertEquals(option1, option2);
   }
   
   @Test
   public void testHasOption() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      assertTrue(options.hasOption("f"));
      assertFalse(options.hasOption("d"));
   }
   
   @Test
   public void testGetRequiredOptions() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = new Option("d", "directory", false, "input directory");
      options.addOption(option2);
      option2.setRequired(true);
      
      List requiredOptionsList = options.getRequiredOptions();
      assertTrue(requiredOptionsList.contains(option2));
   }
   
   @Test
   public void testGetMatchingOptions() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = new Option("d", "directory", false, "input directory");
      options.addOption(option2);
      
      List<String> matchingOptions = options.getMatchingOptions("d");
      assertTrue(matchingOptions.contains("directory"));
   }
   
   @Test
   public void testHasLongOption() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      assertTrue(options.hasLongOption("file"));
      assertFalse(options.hasLongOption("directory"));
   }
   
   @Test
   public void testHasShortOption() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      assertTrue(options.hasShortOption("f"));
      assertFalse(options.hasShortOption("d"));
   }
   
   @Test
   public void testGetOptionGroup() {
      Options options = new Options();
      
      Option option1 = new Option("f", "file", false, "input file");
      options.addOption(option1);
      
      Option option2 = new Option("d", "directory", false, "input directory");
      options.addOption(option2);
      
      Option option3 = new Option("r", "recursive", false, "recursive option");
      
      OptionGroup optionGroup = new OptionGroup();
      optionGroup.addOption(option2);
      optionGroup.addOption(option3);
      
      options.addOptionGroup(optionGroup);
      
      assertEquals(optionGroup, options.getOptionGroup(option2));
   }
}