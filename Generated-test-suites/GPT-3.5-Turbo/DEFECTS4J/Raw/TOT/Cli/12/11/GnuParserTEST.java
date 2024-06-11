import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class GnuParserTest {

    private Options options;
    private GnuParser parser;

    @Before
    public void setUp() {
        options = new Options();
        parser = new GnuParser();
    }

    /*
    Test case for the flatten method with typical use case
     */
    @Test
    public void testFlatten_typicalUseCase() {
        String[] args = {"--option1", "value1", "-option2", "value2"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1", "value1", "-option2", "value2"};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with edge case of empty arguments
     */
    @Test
    public void testFlatten_emptyArguments() {
        String[] args = {};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with edge case of the stopAtNonOption flag set to true
     */
    @Test
    public void testFlatten_stopAtNonOptionTrue() {
        options.addOption("option1", false, "Description");

        String[] args = {"--option1", "value1", "nonOption"};

        String[] result = parser.flatten(options, args, true);

        String[] expected = {"--option1", "value1", "nonOption"};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with edge case of long option with value
     */
    @Test
    public void testFlatten_longOptionWithValue() {
        options.addOption("option1", true, "Description");

        String[] args = {"--option1=value1"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1", "value1"};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with edge case of short option with value
     */
    @Test
    public void testFlatten_shortOptionWithValue() {
        options.addOption("option1", true, "Description");

        String[] args = {"-option1=value1"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"-option1", "value1"};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with error scenario of invalid option
     */
    @Test
    public void testFlatten_invalidOption() {
        options.addOption("option1", false, "Description");

        String[] args = {"--option2"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option2"};
        assertArrayEquals(expected, result);
    }

    /*
    Test case for the flatten method with error scenario of multiple non-option arguments
     */
    @Test
    public void testFlatten_multipleNonOptionArguments() {
        options.addOption("option1", false, "Description");

        String[] args = {"--option1", "value1", "arg1", "arg2"};

        String[] result = parser.flatten(options, args, false);

        String[] expected = {"--option1", "value1", "arg1", "arg2"};
        assertArrayEquals(expected, result);
    }

}