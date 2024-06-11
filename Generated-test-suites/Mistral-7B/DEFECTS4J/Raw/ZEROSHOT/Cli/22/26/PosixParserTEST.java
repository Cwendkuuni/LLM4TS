import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Options options;

    @Before
    public void setUp() {
        options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", false, "Option C without an argument");
        options.addOption("d", false, "Option D without an argument");
        options.addOption("e", "value", "Option E with an argument");
    }

    @Test
    public void testFlattenEmpty() {
        String[] args = {};
        String[] expected = {};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenSimple() {
        String[] args = {"--a", "valueA"};
        String[] expected = {"--a", "valueA"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenOptionWithArg() {
        String[] args = {"--b", "argB"};
        String[] expected = {"--b", "argB"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenOptionWithoutArg() {
        String[] args = {"-a", "-c"};
        String[] expected = {"-a", "-c"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenOptionWithLongArg() {
        String[] args = {"--optionE", "valueE"};
        String[] expected = {"--optionE", "valueE"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenMixed() {
        String[] args = {"--a", "--b=argB", "-c", "-d", "--e=valueE"};
        String[] expected = {"--a", "--b=argB", "-c", "-d", "--e=valueE"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithUnknownOption() {
        String[] args = {"--unknown"};
        String[] expected = {"--unknown"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            List<String> errorMessages = Arrays.asList(e.getMessage().split("\\|"));
            Assert.assertEquals(1, errorMessages.size());
            Assert.assertTrue(errorMessages.get(0).contains("Unknown option: unknown"));
        }
    }

    @Test
    public void testFlattenWithUnknownLongOption() {
        String[] args = {"--unknown-long-option"};
        String[] expected = {"--unknown-long-option"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            List<String> errorMessages = Arrays.asList(e.getMessage().split("\\|"));
            Assert.assertEquals(1, errorMessages.size());
            Assert.assertTrue(errorMessages.get(0).contains("Unknown option: --unknown-long-option"));
        }
    }

    @Test
    public void testFlattenWithNonOption() {
        String[] args = {"nonOption"};
        String[] expected = {"--", "nonOption"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, true);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithArgAfterNonOption() {
        String[] args = {"nonOption", "arg"};
        String[] expected = {"--", "nonOption", "arg"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, true);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlattenWithMixedArgs() {
        String[] args = {"nonOption", "--a", "-c", "--e=valueE"};
        String[] expected = {"--", "nonOption", "--a", "-c", "--e=valueE"};
        try {
            PosixParser parser = new PosixParser();
            String[] result = parser.flatten(options, args, true);
            Assert.assertArrayEquals(expected, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }
}