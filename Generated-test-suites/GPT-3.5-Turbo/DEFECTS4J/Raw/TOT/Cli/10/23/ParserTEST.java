import static org.junit.Assert.*;
import org.junit.*;

public class ParserTest {

    @Test
    public void testFlatten_NullOptions_ReturnsEmptyArray() {
        Options options = null;
        String[] arguments = new String[0];
        Parser parser = new ParserMock();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(0, result.length);
    }
    
    @Test
    public void testFlatten_NullArguments_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = null;
        Parser parser = new ParserMock();
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testParse_NullOptions_ReturnsNull() {
        Options options = null;
        String[] arguments = new String[0];
        Parser parser = new ParserMock();
        
        CommandLine result = parser.parse(options, arguments);
        
        assertNull(result);
    }
    
    @Test
    public void testParse_NullArguments_ReturnsNull() {
        Options options = new Options();
        String[] arguments = null;
        Parser parser = new ParserMock();
        
        CommandLine result = parser.parse(options, arguments);
        
        assertNull(result);
    }
    
    @Test
    public void testProcessArgs_OptionWithRequiredArgument_ValidArguments_ProcessesArguments() {
        Option option = new Option("f", true, "Input file");
        ListIterator<String> iter = Arrays.asList("file.txt", "file2.txt").listIterator();
        Parser parser = new ParserMock();
        
        parser.processArgs(option, iter);
        
        assertTrue(option.getValues().length == 2);
        assertEquals("file.txt", option.getValue(0));
        assertEquals("file2.txt", option.getValue(1));
    }
    
    @Test(expected = ParseException.class)
    public void testProcessArgs_OptionWithRequiredArgument_MissingArgument_ThrowsParseException() {
        Option option = new Option("f", true, "Input file");
        ListIterator<String> iter = Collections.singletonList("file.txt").listIterator();
        Parser parser = new ParserMock();
        
        parser.processArgs(option, iter);
    }
    
    @Test
    public void testProcessOption_UnrecognizedOption_ThrowsUnrecognizedOptionException() {
        String arg = "-u";
        ListIterator<String> iter = Collections.singletonList(arg).listIterator();
        Parser parser = new ParserMock();
        
        try {
            parser.processOption(arg, iter);
            fail("Expected UnrecognizedOptionException to be thrown.");
        } catch (UnrecognizedOptionException e) {
            // Pass
        }
    }
    
    @Test
    public void testProcessOption_RequiredOption_RemovesFromRequiredOptions() {
        Option option = new Option("f", true, "Input file");
        option.setRequired(true);
        ListIterator<String> iter = Collections.singletonList("-f").listIterator();
        Parser parser = new ParserMock();
        
        parser.processOption("-f", iter);
        
        assertTrue(parser.getRequiredOptions().isEmpty());
    }
    
    @Test
    public void testProcessOption_OptionGroup_Selection() {
        Option option1 = new Option("a", false, "Option A");
        Option option2 = new Option("b", false, "Option B");
        
        OptionGroup group = new OptionGroup();
        group.addOption(option1);
        group.addOption(option2);
        
        Options options = new Options();
        options.addOptionGroup(group);
        
        ListIterator<String> iterA = Collections.singletonList("-a").listIterator();
        ListIterator<String> iterB = Collections.singletonList("-b").listIterator();
        
        Parser parser = new ParserMock();
        
        parser.processOption("-a", iterA);
        parser.processOption("-b", iterB);
        
        assertEquals(option1, group.getSelected());
        assertEquals(option2, group.getSelected());
    }
}

class ParserMock extends Parser {
    @Override
    protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return arguments;
    }
    
    @Override
    protected void processArgs(Option opt, ListIterator iter) throws ParseException {
        while (iter.hasNext()) {
            String str = (String) iter.next();
            opt.addValueForProcessing(str);
        }
    }
    
    @Override
    public void processOption(String arg, ListIterator iter) throws ParseException {
        Option option = new Option(arg, false, "");
        cmd.addOption(option);
    }
}