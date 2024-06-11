import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {
    
    @Test
    public void testFlatten_emptyOptions_emptyArguments_returnEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[0];
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_singleOption_singleArgument_returnFlattenedArray() {
        Options options = new Options();
        Option option = new Option("a", "option_a", false, "Option A");
        options.addOption(option);
        String[] arguments = {"-a"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }
    
    @Test
    public void testFlatten_multipleOptions_multipleArguments_returnFlattenedArray() {
        Options options = new Options();
        Option optionA = new Option("a", "option_a", false, "Option A");
        Option optionB = new Option("b", "option_b", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = {"-a", "-b"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("-b", result[1]);
    }
    
    @Test
    public void testFlatten_optionWithArgument_singleArgumentValue_returnFlattenedArray() {
        Options options = new Options();
        Option option = OptionBuilder.withArgName("arg")
                                    .hasArg()
                                    .withDescription("Option with argument")
                                    .create("o");
        options.addOption(option);
        String[] arguments = {"-o", "value"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(2, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value", result[1]);
    }
    
    @Test
    public void testFlatten_optionWithArgument_missingArgumentValue_throwParseException() {
        Options options = new Options();
        Option option = OptionBuilder.withArgName("arg")
                                    .hasArg()
                                    .withDescription("Option with argument")
                                    .create("o");
        options.addOption(option);
        String[] arguments = {"-o"};
        PosixParser parser = new PosixParser();
        
        try {
            parser.flatten(options, arguments, false);
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Missing argument for option: -o", e.getMessage());
        }
    }
    
    @Test
    public void testFlatten_optionWithMultipleArguments_multipleArgumentValues_returnFlattenedArray() {
        Options options = new Options();
        Option option = OptionBuilder.withArgName("arg")
                                    .hasArgs()
                                    .withDescription("Option with multiple arguments")
                                    .create("o");
        options.addOption(option);
        String[] arguments = {"-o", "value1", "value2"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(3, result.length);
        assertEquals("-o", result[0]);
        assertEquals("value1", result[1]);
        assertEquals("value2", result[2]);
    }
    
    @Test
    public void testFlatten_optionWithMultipleArguments_missingArgumentValue_throwParseException() {
        Options options = new Options();
        Option option = OptionBuilder.withArgName("arg")
                                    .hasArgs()
                                    .withDescription("Option with multiple arguments")
                                    .create("o");
        options.addOption(option);
        String[] arguments = {"-o", "value1", "-o"};
        PosixParser parser = new PosixParser();
        
        try {
            parser.flatten(options, arguments, false);
            fail("ParseException expected");
        } catch (ParseException e) {
            assertEquals("Missing argument for option: -o", e.getMessage());
        }
    }
    
    @Test
    public void testFlatten_stopAtNonOption_true_returnNonOptionArguments() {
        Options options = new Options();
        Option optionA = new Option("a", "option_a", false, "Option A");
        Option optionB = new Option("b", "option_b", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = {"-a", "nonOption1", "-b", "nonOption2"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertEquals(2, result.length);
        assertEquals("nonOption1", result[0]);
        assertEquals("nonOption2", result[1]);
    }
    
    @Test
    public void testFlatten_stopAtNonOption_false_returnAllArguments() {
        Options options = new Options();
        Option optionA = new Option("a", "option_a", false, "Option A");
        Option optionB = new Option("b", "option_b", false, "Option B");
        options.addOption(optionA);
        options.addOption(optionB);
        String[] arguments = {"-a", "nonOption1", "-b", "nonOption2"};
        PosixParser parser = new PosixParser();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(4, result.length);
        assertEquals("-a", result[0]);
        assertEquals("nonOption1", result[1]);
        assertEquals("-b", result[2]);
        assertEquals("nonOption2", result[3]);
    }

}