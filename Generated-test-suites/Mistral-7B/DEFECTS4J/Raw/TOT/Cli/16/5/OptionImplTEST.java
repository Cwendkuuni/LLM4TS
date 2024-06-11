import org.apache.commons.cli2.CommandLineParser;
import org.apache.commons.cli2.HelpFormatter;
import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionBuilder;
import org.apache.commons.cli2.Options;
import org.apache.commons.cli2.ParseException;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class OptionImplTest {

 private Options options;

 @Before
 public void setUp() {
 options = new Options();
 }

 /**
 * Test for OptionImpl constructor
 */
 @Test
 public void testOptionImplConstruction() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 assertEquals("optionName", option.getLongOpt());
 assertFalse(option.isRequired());
 }

 /**
 * Test for canProcess method
 */
 @Test
 public void testCanProcess() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 WriteableCommandLine commandLine = new WriteableCommandLine();
 ListIterator<String> arguments = commandLine.getParsedArgs().listIterator();
 assertTrue(option.canProcess(commandLine, arguments));
 arguments.next();
 assertFalse(option.canProcess(commandLine, arguments));
 }

 /**
 * Test for toString method
 */
 @Test
 public void testToString() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 String expectedToString = "optionName";
 String actualToString = option.toString();
 assertEquals(expectedToString, actualToString);
 }

 /**
 * Test for equals method
 */
 @Test
 public void testEquals() {
 Option option1 = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Option option2 = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 assertTrue(option1.equals(option2));
 }

 /**
 * Test for hashCode method
 */
 @Test
 public void testHashCode() {
 Option option1 = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Option option2 = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 int expectedHashCode = option1.hashCode();
 int actualHashCode = option2.hashCode();
 assertEquals(expectedHashCode, actualHashCode);
 }

 /**
 * Test for findOption method
 */
 @Test
 public void testFindOption() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Option expectedOption = option;
 Option actualOption = option.findOption("o");
 assertEquals(expectedOption, actualOption);
 }

 /**
 * Test for isRequired method
 */
 @Test
 public void testIsRequired() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 assertFalse(option.isRequired());
 }

 /**
 * Test for defaults method
 */
 @Test
 public void testDefaults() throws ParseException {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 options.addOption(option);
 CommandLineParser parser = new DefaultParser();
 WriteableCommandLine commandLine = parser.parse(options, new String[]{"--optionName"});
 assertTrue(commandLine.hasOption("optionName"));
 }

 /**
 * Test for checkPrefixes method with empty prefix list
 */
 @Test
 public void testCheckPrefixesEmptyPrefixList() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Set<String> prefixes = new java.util.HashSet<String>();
 option.checkPrefixes(prefixes);
 }

 /**
 * Test for checkPrefixes method with prefix match
 */
 @Test
 public void testCheckPrefixesPrefixMatch() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Set<String> prefixes = new java.util.HashSet<String>();
 prefixes.add("op");
 option.checkPrefixes(prefixes);
 }

 /**
 * Test for checkPrefixes method with no prefix match
 */
 @Test(expected = IllegalArgumentException.class)
 public void testCheckPrefixesNoPrefixMatch() {
 Option option = new OptionImpl(-1, "optionName", false, "o")
 .required(false)
 .build();
 Set<String> prefixes = new java.util.HashSet<String>();
 prefixes.add("ab");
 option.checkPrefixes(prefixes);
 }
}