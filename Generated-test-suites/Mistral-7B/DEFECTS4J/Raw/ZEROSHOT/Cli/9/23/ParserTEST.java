import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

 @Test
 public void testParse() throws ParseException {
 // given
 String[] args = new String[]{"--option1", "-o1", "value1", "--option2"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("o1", true, "Option 1 with short name");
 options.addOption("option2", null, "Option 2");

 Parser parser = new DefaultParser();

 // when
 CommandLine cmd = parser.parse(options, args);

 // then
 Assert.assertEquals(2, cmd.getNumArgs());
 Assert.assertTrue(cmd.hasOption("option1"));
 Assert.assertTrue(cmd.hasOption("o1"));
 Assert.assertFalse(cmd.hasOption("nonexistentoption"));
 Assert.assertEquals("value1", cmd.getOptionValue("option1"));
 Assert.assertEquals("value1", cmd.getOptionValue("o1"));
 Assert.assertNull(cmd.getOptionValue("option2"));
 }

 @Test(expected = MissingOptionException.class)
 public void testMissingRequiredOption() throws ParseException {
 // given
 String[] args = new String[]{"--option1"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", true, "Option 2");
 options.addRequiredOption("option2", true, "Option 2 is required");

 Parser parser = new DefaultParser();

 // when
 parser.parse(options, args);

 // then
 // an exception is thrown
 }

 @Test(expected = UnrecognizedOptionException.class)
 public void testUnrecognizedOption() throws ParseException {
 // given
 String[] args = new String[]{"--unknownoption", "--option1"};
 Options options = new Options();
 options.addOption("option1", true, "Option 1");

 Parser parser = new DefaultParser();

 // when
 parser.parse(options, args);

 // then
 // an exception is thrown
 }

 @Test
 public void testParseWithProperties() throws ParseException {
 // given
 String[] args = new String[]{"--option1", "--option2=value2"};
 Properties properties = new Properties();
 properties.setProperty("option1", "value1");
 properties.setProperty("option2", "value2");
 Options options = new Options();
 options.addOption("option1", true, "Option 1");
 options.addOption("option2", null, "Option 2");

 Parser parser = new DefaultParser();

 // when
 CommandLine cmd = parser.parse(options, args, properties);

 // then
 Assert.assertEquals(0, cmd.getNumArgs());
 Assert.assertTrue(cmd.hasOption("option1"));
 Assert.assertEquals("value1", cmd.getOptionValue("option1"));
 Assert.assertEquals("value2", cmd.getOptionValue("option2"));
 }

 @Test
 public void testParseWithEmptyArguments() throws ParseException {
 // given
 String[] args = new String[0];
 Options options = new Options();
 options.addOption("option1", true, "Option 1");

 Parser parser = new DefaultParser();

 // when
 CommandLine cmd = parser.parse(options, args);

 // then
 Assert.assertEquals(0, cmd.getNumArgs());
 Assert.assertFalse(cmd.hasOptions());
 }

}