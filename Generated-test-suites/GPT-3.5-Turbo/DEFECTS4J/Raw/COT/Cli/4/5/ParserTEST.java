import org.junit.Assert;
import org.junit.Test;

public class ParserTest {

   @Test
   public void testParser_parse_Options_StringArray() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"-a"};
        Parser parser = new ParserImpl();

        CommandLine result = parser.parse(options, args);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertEquals(1, result.getOptions().length);
        Assert.assertEquals(null, result.getOptionValue("b"));
    }

   @Test
   public void testParser_parse_Options_StringArray_Properties() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new ParserImpl();

        CommandLine result = parser.parse(options, args, properties);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertEquals(1, result.getOptions().length);
        Assert.assertEquals("value", result.getOptionValue("b"));
    }

   @Test
   public void testParser_parse_Options_StringArray_boolean() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"-a"};
        boolean stopAtNonOption = true;

        Parser parser = new ParserImpl();

        CommandLine result = parser.parse(options, args, stopAtNonOption);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertEquals(1, result.getOptions().length);
        Assert.assertEquals(null, result.getOptionValue("b"));
    }

   @Test
   public void testParser_parse_Options_StringArray_Properties_boolean() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        boolean stopAtNonOption = true;

        Parser parser = new ParserImpl();

        CommandLine result = parser.parse(options, args, properties, stopAtNonOption);

        Assert.assertTrue(result.hasOption("a"));
        Assert.assertEquals(1, result.getOptions().length);
        Assert.assertEquals("value", result.getOptionValue("b"));
    }

   @Test
   public void testParser_parse_Options_StringArray_throwsParseException() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-a", "-b"};

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, args);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected exception
        }
    }

   @Test
   public void testParser_parse_Options_StringArray_Properties_throwsParseException() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, args, properties);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected exception
        }
    }

   @Test
   public void testParser_parse_Options_StringArray_boolean_throwsParseException() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-a", "-b"};
        boolean stopAtNonOption = true;

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, args, stopAtNonOption);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected exception
        }
    }

   @Test
   public void testParser_parse_Options_StringArray_Properties_boolean_throwsParseException() {
        Options options = new Options();
        options.addOption("a", false, "option a");

        String[] args = {"-a"};
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        boolean stopAtNonOption = true;

        Parser parser = new ParserImpl();

        try {
            parser.parse(options, args, properties, stopAtNonOption);
            Assert.fail("Expected ParseException");
        } catch (ParseException e) {
            // expected exception
        }
    }
  
}