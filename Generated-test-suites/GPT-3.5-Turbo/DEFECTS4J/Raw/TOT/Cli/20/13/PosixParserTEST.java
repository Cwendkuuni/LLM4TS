import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.*;

import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser parser;
    private Options options;

    @Before
    public void setUp() {
        parser = new PosixParser();
        options = new Options();

        // add sample options for testing
        options.addOption(Option.builder("a").desc("Option 'a'").build());
        options.addOption(Option.builder("b").desc("Option 'b'").build());
        options.addOption(Option.builder("c").desc("Option 'c'").hasArg().argName("value").build());
    }

    @Test
    public void testFlatten_StopAtNonOption_True() {
        String[] arguments = {"-a", "value1", "nonOption"};
        String[] expected = {"-a", "value1", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, true);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_StopAtNonOption_False() {
        String[] arguments = {"-a", "value1", "nonOption"};
        String[] expected = {"-a", "value1", "nonOption"};
        
        String[] result = parser.flatten(options, arguments, false);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testFlatten_NoOption() {
        String[] arguments = {"arg1", "arg2", "arg3"};
        String[] expected = {"arg1", "arg2", "arg3"};

        String[] result = parser.flatten(options, arguments, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        String token = "-a";
        boolean stopAtNonOption = true;
        String[] expected = {"-a"};
        
        parser.processOptionToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);
        
        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken_InvalidOption_StopAtNonOption() {
        String token = "-z";
        boolean stopAtNonOption = true;
        String[] expected = {"-z"};

        parser.processOptionToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testProcessOptionToken_InvalidOption_NoStopAtNonOption() {
        String token = "-z";
        boolean stopAtNonOption = false;
        String[] expected = {"-z"};

        parser.processOptionToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidOption() {
        String token = "-abc";
        boolean stopAtNonOption = true;
        String[] expected = {"-a", "-b", "-c"};

        parser.burstToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_InvalidOption_StopAtNonOption() {
        String token = "-az";
        boolean stopAtNonOption = true;
        String[] expected = {"-a", "-z"};

        parser.burstToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_InvalidOption_NoStopAtNonOption() {
        String token = "-az";
        boolean stopAtNonOption = false;
        String[] expected = {"-az"};

        parser.burstToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

    @Test
    public void testBurstToken_ValidOptionWithValue() {
        String token = "-acvalue";
        boolean stopAtNonOption = true;
        String[] expected = {"-a", "-cvalue"};

        parser.burstToken(token, stopAtNonOption);

        String[] result = parser.flatten(options, new String[]{}, true);

        assertArrayEquals(expected, result);
    }

}