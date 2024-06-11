import org.junit.*;
import static org.junit.Assert.*;

public class PosixParserTest {

    private static PosixParser parser;

    @BeforeClass
    public static void setUp() {
        parser = new PosixParser();
    }

    @Test
    public void testFlatten_OptionsWithNoArguments_ReturnsEmptyArray() {
        Options options = new Options();
        String[] arguments = new String[0];
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(0, result.length);
    }

    @Test
    public void testFlatten_SingleNonOption_ReturnsSameNonOption() {
        Options options = new Options();
        String[] arguments = new String[]{"nonOption"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(1, result.length);
        assertEquals("nonOption", result[0]);
    }

    @Test
    public void testFlatten_SingleOptionWithoutArgument_ReturnsOptionToken() {
        Options options = new Options();
        options.addOption("a", false, "Option without argument");
        String[] arguments = new String[]{"-a"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(1, result.length);
        assertEquals("-a", result[0]);
    }

    @Test
    public void testFlatten_SingleOptionWithArgument_ReturnsOptionTokenAndArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option with argument");
        String[] arguments = new String[]{"-a", "argument"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals("argument", result[1]);
    }

    @Test
    public void testFlatten_SingleOptionMissingArgument_ReturnsErrorTokenAndMissingArgument() {
        Options options = new Options();
        options.addOption("a", true, "Option with argument");
        String[] arguments = new String[]{"-a"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, result.length);
        assertEquals("-a", result[0]);
        assertEquals(OptionBuilder.MISSING_ARG, result[1]);
    }

    @Test
    public void testFlatten_StopAtNonOptionEnabled_StopsFlatteningAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option without argument");
        String[] arguments = new String[]{"nonOption", "-a"};
        boolean stopAtNonOption = true;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(2, result.length);
        assertEquals("nonOption", result[0]);
        assertEquals("-a", result[1]);
    }

    @Test
    public void testFlatten_OptionsWithMixedArguments_ReturnsFlattenedTokens() {
        Options options = new Options();
        options.addOption("a", false, "Option without argument");
        options.addOption("b", true, "Option with argument");
        String[] arguments = new String[]{"nonOption", "-a", "nonOption2", "-b", "argument", "-a"};
        boolean stopAtNonOption = false;
        
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        assertEquals(8, result.length);
        assertEquals("nonOption", result[0]);
        assertEquals("-a", result[1]);
        assertEquals("nonOption2", result[2]);
        assertEquals("-b", result[3]);
        assertEquals("argument", result[4]);
        assertEquals("-a", result[5]);
        assertEquals("-a", result[6]);
        assertEquals(OptionBuilder.MISSING_ARG, result[7]);
    }

}