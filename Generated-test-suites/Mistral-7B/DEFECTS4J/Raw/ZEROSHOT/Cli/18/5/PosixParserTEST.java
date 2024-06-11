import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenSimple() throws ParseException {
        String[] args = {"--foo", "-o", "abc", "-a", "123", "--bar=value", "--baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "output", true, "o");
        options.addOption("a", "arg", true, "a");
        options.addOption("baz", true, "b");
        options.addOption("bar", true, "b").required(true);

        List<String> expected = Arrays.asList(
                "--foo",
                "-o",
                "abc",
                "-a",
                "123",
                "--bar",
                "value"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenNoOptions() throws ParseException {
        String[] args = {"--", "arg1", "arg2"};
        Options options = new Options();

        List<String> expected = Arrays.asList(
                "--",
                "arg1",
                "arg2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() throws ParseException {
        String[] args = {"--unknown"};
        Options options = new Options();

        new PosixParser().flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenEmptyOption() throws ParseException {
        String[] args = {"--"};
        Options options = new Options();

        new PosixParser().flatten(options, args, false);
    }

    @Test
    public void testFlattenLongOption() throws ParseException {
        String[] args = {"--long-option"};
        Options options = new Options();
        options.addOption("longOption", true, "l");

        List<String> expected = Arrays.asList(
                "--long-option"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArg() throws ParseException {
        String[] args = {"--option=value"};
        Options options = new Options();
        options.addOption("option", true, "o");

        List<String> expected = Arrays.asList(
                "--option",
                "value"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArgRequired() throws ParseException {
        String[] args = {"--option"};
        Options options = new Options();
        options.addOption("option", true, "o").required(true);

        try {
            new PosixParser().flatten(options, args, false);
            Assert.fail();
        } catch (ParseException e) {
            // expected
        }
    }

    @Test
    public void testFlattenOptionWithMultipleArgs() throws ParseException {
        String[] args = {"--option=value1 value2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        List<String> expected = Arrays.asList(
                "--option",
                "value1",
                "value2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenOptionWithArgAndOtherArgs() throws ParseException {
        String[] args = {"--option=value1", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("option", true, "o");

        List<String> expected = Arrays.asList(
                "--option",
                "value1",
                "arg1",
                "arg2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenMultipleOptions() throws ParseException {
        String[] args = {"-o", "abc", "--foo", "-a", "123", "--bar=value", "--baz"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("o", "output", true, "o");
        options.addOption("a", "arg", true, "a");
        options.addOption("baz", true, "b");
        options.addOption("bar", true, "b").required(true);

        List<String> expected = Arrays.asList(
                "-o",
                "abc",
                "--foo",
                "-a",
                "123",
                "--bar",
                "value"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptions() throws ParseException {
        String[] args = {"--long-option-1", "--long-option-2=value"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2");

        List<String> expected = Arrays.asList(
                "--long-option-1",
                "--long-option-2",
                "value"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithValues() throws ParseException {
        String[] args = {"--long-option-1=value1", "--long-option-2=value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2");

        List<String> expected = Arrays.asList(
                "--long-option-1",
                "value1",
                "--long-option-2",
                "value2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionsWithValuesAndOtherArgs() throws ParseException {
        String[] args = {"--long-option-1=value1", "--long-option-2=value2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");
        options.addOption("longOption2", true, "l2");

        List<String> expected = Arrays.asList(
                "--long-option-1",
                "value1",
                "--long-option-2",
                "value2",
                "arg1",
                "arg2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleValues() throws ParseException {
        String[] args = {"--long-option-1=value1 value2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");

        List<String> expected = Arrays.asList(
                "--long-option-1",
                "value1",
                "value2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }

    @Test
    public void testFlattenLongOptionWithMultipleValuesAndOtherArgs() throws ParseException {
        String[] args = {"--long-option-1=value1 value2", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("longOption1", true, "l1");

        List<String> expected = Arrays.asList(
                "--long-option-1",
                "value1",
                "value2",
                "arg1",
                "arg2"
        );

        List<String> result = new PosixParser().flatten(options, args, false);
        Assert.assertEquals(expected, result);
    }
}