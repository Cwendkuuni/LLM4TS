import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    @Test
    public void testFlatten_withValidOptionsAndArguments_shouldReturnFlattenedArray() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        
        String[] arguments = {"-a", "-b", "value", "-c=anotherValue"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-a", "-b", "value", "-c", "anotherValue"};
        assertArrayEquals(expected, result);
    }
    
    @Test
    public void testFlatten_withStopAtNonOption_shouldReturnFlattenedArrayUpToNonOption() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", true, "option b");
        options.addOption("c", true, "option c");
        
        String[] arguments = {"-a", "-b", "value", "nonOption", "-c", "anotherValue"};
        boolean stopAtNonOption = true;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-a", "-b", "value"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withOptionsWithoutArguments_shouldReturnFlattenedArray() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        options.addOption("b", false, "option b");
        options.addOption("c", false, "option c");
        
        String[] arguments = {"-a", "-b", "-c"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-a", "-b", "-c"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withInvalidOptions_shouldReturnFlattenedArrayWithSpecialToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        String[] arguments = {"-a", "-b", "nonOption"};
        boolean stopAtNonOption = false;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-a", "--", "-b", "nonOption"};
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_withRemainingTokens_shouldReturnFlattenedArrayWithSpecialToken() {
        Options options = new Options();
        options.addOption("a", false, "option a");
        
        String[] arguments = {"-a", "-b", "value"};
        boolean stopAtNonOption = true;
        
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, stopAtNonOption);
        
        String[] expected = {"-a", "--", "-b", "value"};
        assertArrayEquals(expected, result);
    }
}