import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten_EmptyArgs() {
        String[] args = {};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        try {
            String[] result = parser.flatten(options, args, false);
            Assert.assertArrayEquals(new String[]{}, result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleOption() {
        String[] args = {"-a"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("-a");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_SingleArgumentOption() {
        String[] args = {"-a", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A with argument");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("-a", "arg");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleOptions() {
        String[] args = {"-a", "--be", "-c", "--de", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        options.addOption("d", false, "Option D");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("-a", "--be", "-c", "--de", "arg");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_MultipleArgumentsOptions() {
        String[] args = {"-a", "arg1", "arg2", "--be", "arg3", "arg4", "-c", "arg5"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", true, "Option A with arguments");
        options.addOption("b", true, "Option B with arguments");
        options.addOption("c", true, "Option C with arguments");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("-a", "arg1", "arg2", "--be", "arg3", "arg4", "-c", "arg5");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        String[] args = {"-a", "--", "other", "--be", "-c"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        options.addOption("c", false, "Option C");
        try {
            String[] result = parser.flatten(options, args, true);
            List<String> expected = Arrays.asList("-a", "--", "other", "--be", "-c");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        String[] args = {"-i", "-x"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", false, "Option B");
        try {
            parser.flatten(options, args, false);
        } catch (ParseException e) {
            // expected exception
        }
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--long-option", "arg"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option", "arg");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

    @Test
    public void testFlatten_EmptyLongOption() {
        String[] args = {"--", "--long-option"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "Long Option");
        try {
            String[] result = parser.flatten(options, args, false);
            List<String> expected = Arrays.asList("--long-option");
            Assert.assertArrayEquals(expected.toArray(new String[0]), result);
        } catch (ParseException e) {
            Assert.fail("Unexpected exception: " + e.getMessage());
        }
    }

}