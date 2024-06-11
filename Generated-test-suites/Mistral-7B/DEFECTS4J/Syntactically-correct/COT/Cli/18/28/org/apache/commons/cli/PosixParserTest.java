import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser.
 */
public class PosixParserTest {

    

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", 1, "An option with an integer value");
        options.addOption("c", 2, "An option with an integer value");
        options.addOption("d", "default", "An option with a string value");
        options.addOption("e", "eValue", "An option with a string value");
        options.addOption("f", "fValue", "An option with a string value");
        options.addOption("g", null, "An option with no value");
        options.addOption("h", "hValue", "An option with a value and arg");
        options.addOption("i", "iValue", "An option with a value and multiple args");

        String[] args = {"--a", "--b=1", "--c=2", "--d", "--e=eValue", "--e=eValue2", "--f=fValue", "--g", "--h=hValue", "--i=iValue"};
        PosixParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--a", "--b", "1", "--c", "2", "--d", "--e", "eValue", "eValue2", "--f", "--g", "--h", "hValue", "--i", "iValue");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Test with stopAtNonOption
        args = new String[]{"--a", "--b=1", "--c=2", "--d", "--e=eValue", "--e=eValue2", "--f=fValue", "--g", "--h=hValue", "--i=iValue", "newOption"};
        try {
            String[] parsedArgs = parser.flatten(options, args, true);
            List<String> expected = Arrays.asList("--a", "--b", "1", "--c", "2", "--d", "--e", "eValue", "eValue2", "--f", "--g", "--h", "hValue", "--i", "iValue", "--newOption");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlattenWithLongOptions() {
        Options options = new Options();
        options.addOption("longOption1", true, "A long option with a boolean value");
        options.addOption("longOption2", 1, "A long option with an integer value");
        options.addOption("longOption3", 2, "A long option with an integer value");
        options.addOption("longOption4", "default", "A long option with a string value");
        options.addOption("longOption5", "longValue", "A long option with a string value");
        options.addOption("longOption6", null, "A long option with no value");
        options.addOption("longOption7", "longValue7", "A long option with a value and arg");
        options.addOption("longOption8", "longValue8", "A long option with a value and multiple args");

        String[] args = {"--long-option1", "--long-option2=1", "--long-option3=2", "--long-option4", "--long-option5=longValue", "--long-option5=longValue2", "--long-option6", "--long-option7=longValue7", "--long-option8=longValue8"};
        PosixParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option1", "--long-option2", "1", "--long-option3", "2", "--long-option4", "--long-option5", "longValue", "longValue2", "--long-option6", "--long-option7", "longValue7", "--long-option8", "longValue8");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }

        // Test with stopAtNonOption
        String[] argsWithNonOption = {"--long-option1", "--long-option2=1", "--long-option3=2", "--long-option4", "--long-option5=longValue", "--long-option5=longValue2", "--long-option6", "--long-option7=longValue7", "--long-option8=longValue8", "newOption"};
        try {
            String[] parsedArgs = parser.flatten(options, argsWithNonOption, true);
            List<String> expected = Arrays.asList("--long-option1", "--long-option2", "1", "--long-option3", "2", "--long-option4", "--long-option5", "longValue", "longValue2", "--long-option6", "--long-option7", "longValue7", "--long-option8", "longValue8", "--newOption");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * Test method for {@link PosixParser#flatten(Options, String[], boolean)}.
     */
    @Test
    public void testFlattenWithInvalidOptions() {
        Options options = new Options();
        options.addOption("a", true, "An option with a boolean value");
        options.addOption("b", 1, "An option with an integer value");
        options.addOption("c", 2, "An option with an integer value");
        options.addOption("d", "default", "An option with a string value");
        options.addOption("e", "eValue", "An option with a string value");
        options.addOption("f", "fValue", "An option with a string value");
        options.addOption("g", null, "An option with no value");

        String[] args = {"--a", "--b=1", "--c=2", "--d", "--e=eValue", "--e=eValue2", "--f=fValue", "--g", "--h", "--i=iValue", "newOption1", "--i=iValue2"};
        PosixParser parser = new PosixParser();
        try {
            String[] parsedArgs = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--a", "--b", "1", "--c", "2", "--d", "--e", "eValue", "eValue2", "--f", "--g", "--h", "--i", "iValue", "iValue2", "--newOption1");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            // The last option "newOption1" is not valid, so an exception should be thrown
            Assert.assertEquals("Invalid option: newOption1", e.getMessage());
        }

        // Test with stopAtNonOption
        String[] argsWithStopAtNonOption = {"--a", "--b=1", "--c=2", "--d", "--e=eValue", "--e=eValue2", "--f=fValue", "--g", "--h", "--i=iValue", "newOption1", "--i=iValue2", "invalidOption"};
        try {
            String[] parsedArgs = parser.flatten(options, argsWithStopAtNonOption, true);
            // With stopAtNonOption, the test should stop at the first non-option and not reach the invalid option
            List<String> expected = Arrays.asList("--a", "--b", "1", "--c", "2", "--d", "--e", "eValue", "eValue2", "--f", "--g", "--h", "--i", "iValue");
            Assert.assertArrayEquals(expected.toArray(new String[0]), parsedArgs);
        } catch (ParseException e) {
            // The last option "invalidOption" is not valid, so an exception should be thrown, but it should not be reached with stopAtNonOption
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    
}