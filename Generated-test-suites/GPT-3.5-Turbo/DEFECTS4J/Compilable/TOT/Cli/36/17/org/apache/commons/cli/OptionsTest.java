package org.apache.commons.cli;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.util.List;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Arrays;

public class OptionsTest {
  
  private Options options;
  private Option option1;
  private Option option2;
  private Option option3;
  private OptionGroup optionGroup;
  
  @Before
  public void setUp() {
    options = new Options();
    
    option1 = new Option("a", "option1", false, "Option 1");
    option2 = new Option("b", "option2", true, "Option 2");
    option3 = new Option("c", null, false, "Option 3");
    
    optionGroup = new OptionGroup();
    Option option4 = new Option("d", "option4", false, "Option 4");
    optionGroup.addOption(option4);
    
    options.addOption(option1)
           .addOption(option2)
           .addOption(option3)
           .addOptionGroup(optionGroup);
  }
  
  // Test for addOptionGroup(OptionGroup group) method
  @Test
  public void testAddOptionGroup() {
    Option option = new Option("e", "option5", false, "Option 5");
    OptionGroup newOptionGroup = new OptionGroup();
    newOptionGroup.addOption(option);
    
    options.addOptionGroup(newOptionGroup);
    
    Collection<OptionGroup> optionGroups = options.getOptionGroups();
    assertTrue(optionGroups.contains(optionGroup));
    assertTrue(optionGroups.contains(newOptionGroup));
  }
  
  // Test for addOption(String opt, String description) method
  @Test
  public void testAddOption1() {
    Option option = new Option("e", "option5", false, "Option 5");
    options.addOption("e", "Option 5");
    
    Option result = options.getOption("e");
    assertEquals(option, result);
  }
  
  // Test for addOption(String opt, boolean hasArg, String description) method
  @Test
  public void testAddOption2() {
    Option option = new Option("f", null, true, "Option 6");
    options.addOption("f", true, "Option 6");
    
    Option result = options.getOption("f");
    assertEquals(option, result);
  }
  
  // Test for addOption(String opt, String longOpt, boolean hasArg, String description) method
  @Test
  public void testAddOption3() {
    Option option = new Option("g", "option7", false, "Option 7");
    options.addOption("g", "option7", false, "Option 7");
    
    Option result = options.getOption("g");
    assertEquals(option, result);
  }
  
  // Test for addOption(Option opt) method
  @Test
  public void testAddOption4() {
    Option option = new Option("h", "option8", false, "Option 8");
    options.addOption(option);
    
    Option result = options.getOption("h");
    assertEquals(option, result);
  }
  
  // Test for getOptions() method
  @Test
  public void testGetOptions() {
    List<Option> expectedOptions = Arrays.asList(option1, option2, option3);
    Collection<Option> resultOptions = options.getOptions();
    
    assertEquals(expectedOptions, resultOptions);
  }
  
  // Test for getRequiredOptions() method
  @Test
  public void testGetRequiredOptions() {
    List<Object> expectedRequiredOptions = Arrays.asList(optionGroup);
    List<Object> resultRequiredOptions = options.getRequiredOptions();
    
    assertEquals(expectedRequiredOptions, resultRequiredOptions);
  }
  
  // Test for getOption(String opt) method
  @Test
  public void testGetOption1() {
    Option result = options.getOption("a");
    assertEquals(option1, result);
  }
  
  @Test
  public void testGetOption2() {
    Option result = options.getOption("option2");
    assertEquals(option2, result);
  }
  
  // Test for getMatchingOptions(String opt) method
  @Test
  public void testGetMatchingOptions() {
    List<String> expectedMatchingOptions = Arrays.asList("option1");
    List<String> resultMatchingOptions = options.getMatchingOptions("opt");
    
    assertEquals(expectedMatchingOptions, resultMatchingOptions);
  }
  
  // Test for hasOption(String opt) method
  @Test
  public void testHasOption1() {
    boolean result = options.hasOption("a");
    assertTrue(result);
  }
  
  @Test
  public void testHasOption2() {
    boolean result = options.hasOption("option2");
    assertTrue(result);
  }
  
  @Test
  public void testHasOption3() {
    boolean result = options.hasOption("option3");
    assertTrue(result);
  }
  
  @Test
  public void testHasOption4() {
    boolean result = options.hasOption("option7");
    assertFalse(result);
  }
  
  // Test for hasLongOption(String opt) method
  @Test
  public void testHasLongOption1() {
    boolean result = options.hasLongOption("option2");
    assertTrue(result);
  }
  
  @Test
  public void testHasLongOption2() {
    boolean result = options.hasLongOption("option7");
    assertFalse(result);
  }
  
  // Test for hasShortOption(String opt) method
  @Test
  public void testHasShortOption1() {
    boolean result = options.hasShortOption("a");
    assertTrue(result);
  }
  
  @Test
  public void testHasShortOption2() {
    boolean result = options.hasShortOption("b");
    assertFalse(result);
  }
  
  // Test for getOptionGroup(Option opt) method
  @Test
  public void testGetOptionGroup1() {
    Option resultOption = options.getOption("d");
    OptionGroup resultOptionGroup = options.getOptionGroup(resultOption);
    
    assertEquals(optionGroup, resultOptionGroup);
  }
  
  @Test
  public void testGetOptionGroup2() {
    Option resultOption = options.getOption("a");
    OptionGroup resultOptionGroup = options.getOptionGroup(resultOption);
    
    assertNull(resultOptionGroup);
  }
  
  // Test for toString() method
  @Test
  public void testToString() {
    String expectedString = "[ Options: [ short {a=Option 1, b=Option 2, c=Option 3} ] [ long {option1=Option 1, option2=Option 2, null=Option 3} ]";
    String resultString = options.toString();
    
    assertEquals(expectedString, resultString);
  }
}