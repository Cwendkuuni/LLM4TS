import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Properties;

public class ParserTest {
    
    private Options options;
    private Parser parser;
    
    @Before
    public void setUp() {
        options = new Options();
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) throws ParseException {
                return new String[0];
            }
        };
    }

    @Test
    public void testParse_OptionsArguments_Success() throws ParseException {
        CommandLine result = parser.parse(options, new String[]{"-a", "value"});
        
        Assert.assertNotNull(result);
    }
    
    @Test(expected = MissingOptionException.class)
    public void testParse_RequiredOptionMissing_ExceptionThrown() throws ParseException {
        options.addOption(Option.builder("a").required().build());
        
        parser.parse(options, new String[]{});
    }
    
    @Test
    public void testParse_OptionsProperties_Success() throws ParseException {
        Properties properties = new Properties();
        properties.setProperty("a", "value");
        
        CommandLine result = parser.parse(options, new String[]{}, properties);
        
        Assert.assertNotNull(result);
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testParse_UnrecognizedOption_ExceptionThrown() throws ParseException {
        parser.parse(options, new String[]{"-x"});
    }
    
    @Test
    public void testParse_OptionsStopAtNonOption_Success() throws ParseException {
        CommandLine result = parser.parse(options, new String[]{"-a", "value", "--", "-b", "otherValue"});
        
        Assert.assertTrue(result.hasOption("a"));
        Assert.assertFalse(result.hasOption("b"));
        Assert.assertTrue(result.getArgList().contains("-b"));
        Assert.assertTrue(result.getArgList().contains("otherValue"));
    }
    
    @Test
    public void testProcessArgs_OptionWithArg_Success() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator<String> iter = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iter);
        
        Assert.assertEquals("value1", option.getValue());
        Assert.assertEquals("value2", option.getValue());
    }
    
    @Test(expected = MissingArgumentException.class)
    public void testProcessArgs_OptionWithArgMissing_ValueNotFound() throws ParseException {
        Option option = Option.builder("a").hasArg().build();
        ListIterator<String> iter = Arrays.asList().listIterator();
        
        parser.processArgs(option, iter);
    }
    
    @Test
    public void testProcessArgs_OptionWithOptionalArg_Success() throws ParseException {
        Option option = Option.builder("a").hasOptionalArg().build();
        ListIterator<String> iter = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iter);
        
        Assert.assertEquals("value1", option.getValue());
        Assert.assertEquals("value2", option.getValue());
    }
    
    @Test
    public void testProcessArgs_OptionWithoutArg_Success() throws ParseException {
        Option option = Option.builder("a").build();
        ListIterator<String> iter = Arrays.asList("value1", "value2").listIterator();
        
        parser.processArgs(option, iter);
        
        Assert.assertNull(option.getValue());
    }
    
    @Test
    public void testProcessOption_OptionFound_OptionAddedToCommandLine() throws ParseException {
        options.addOption(Option.builder("a").build());
        ListIterator<String> iter = Arrays.asList("-a").listIterator();
        CommandLine cmd = new CommandLine();
        
        parser.processOption("-a", iter);
        
        Assert.assertEquals(cmd, parser.getCmd());
    }
    
    @Test(expected = UnrecognizedOptionException.class)
    public void testProcessOption_OptionNotFound_ExceptionThrown() throws ParseException {
        ListIterator<String> iter = Arrays.asList("-x").listIterator();
        
        parser.processOption("-x", iter);
    }
    
    @Test
    public void testCheckRequiredOptions_NoMissingOptions_Success() throws MissingOptionException {
        options.addOption(Option.builder("a").required().build());
        
        parser.checkRequiredOptions();
    }
    
    @Test(expected = MissingOptionException.class)
    public void testCheckRequiredOptions_MissingOption_ExceptionThrown() throws MissingOptionException {
        options.addOption(Option.builder("a").required().build());
        parser.getRequiredOptions().add(option);
        
        parser.checkRequiredOptions();
    }
}