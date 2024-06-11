import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

import static org.junit.Assert.assertEquals;

public class PosixParserTest {
  
  private PosixParser parser;
  
  @Before
  public void setUp() {
    parser = new PosixParser();
  }
  
  @Test
  public void testFlatten_noOptions() {
    Options options = new Options();
    String[] arguments = {"arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_singleOption() {
    Options options = new Options();
    Option option = new Option("a", "option A");
    options.addOption(option);
    String[] arguments = {"-a", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-a", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_multipleOptions() {
    Options options = new Options();
    Option option1 = new Option("a", "option A");
    Option option2 = new Option("b", "option B");
    options.addOption(option1);
    options.addOption(option2);
    String[] arguments = {"-a", "-b", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-a", "-b", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_stopAtNonOption() {
    Options options = new Options();
    Option option = new Option("a", "option A");
    options.addOption(option);
    String[] arguments = {"-a", "-b", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    String[] expected = {"-a", "-b", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_specialToken() {
    Options options = new Options();
    String[] arguments = {"--special", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"--special", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_singleHyphen() {
    Options options = new Options();
    String[] arguments = {"-"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_shortOptionNeedArgument() {
    Options options = new Options();
    Option option = Option.builder("a").hasArg().build();
    options.addOption(option);
    String[] arguments = {"-a", "value"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-a", "value"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_shortOptionNoArgument() {
    Options options = new Options();
    Option option = Option.builder("a").build();
    options.addOption(option);
    String[] arguments = {"-a"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-a"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_shortOptionNoArgument_withRemaining() {
    Options options = new Options();
    Option option = Option.builder("a").build();
    options.addOption(option);
    String[] arguments = {"-a", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-a", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidShortOption_stopAtNonOption() {
    Options options = new Options();
    String[] arguments = {"-x", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    String[] expected = {"-x", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidShortOption_noStopAtNonOption() {
    Options options = new Options();
    String[] arguments = {"-x", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"-x"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_longOptionNeedArgument() {
    Options options = new Options();
    Option option = Option.builder().longOpt("option").hasArg().build();
    options.addOption(option);
    String[] arguments = {"--option", "value"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"--option", "value"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_longOptionNoArgument() {
    Options options = new Options();
    Option option = Option.builder().longOpt("option").build();
    options.addOption(option);
    String[] arguments = {"--option"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"--option"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_longOptionNoArgument_withRemaining() {
    Options options = new Options();
    Option option = Option.builder().longOpt("option").build();
    options.addOption(option);
    String[] arguments = {"--option", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"--option", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidLongOption_stopAtNonOption() {
    Options options = new Options();
    String[] arguments = {"--invalid", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, true);
    String[] expected = {"--invalid", "arg1", "arg2"};
    
    assertArrayEquals(expected, result);
  }
  
  @Test
  public void testFlatten_invalidLongOption_noStopAtNonOption() {
    Options options = new Options();
    String[] arguments = {"--invalid", "arg1", "arg2"};
    String[] result = parser.flatten(options, arguments, false);
    String[] expected = {"--invalid"};
    
    assertArrayEquals(expected, result);
  }
  
}