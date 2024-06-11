import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenWithNoOptions() {
        String[] args = new String[]{"java", "MyClass"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("java", "MyClass");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithOneOption() {
        String[] args = new String[]{"java", "--option", "value"};
        Options options = new Options();
        Option option = new Option("option", true, "o", "opt");
        options.addOption(option);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("java", "--option", "value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithTwoOptions() {
        String[] args = new String[]{"java", "--option1", "value1", "--option2=value2"};
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1", "opt1");
        Option option2 = new Option("option2", true, "o2", "opt2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("java", "--option1", "value1", "--option2=value2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithLongOption() {
        String[] args = new String[]{"java", "--long-option", "value"};
        Options options = new Options();
        Option option = new Option("long-option", true, null, "longOpt");
        options.addOption(option);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("java", "--long-option=value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithMultipleLongOptions() {
        String[] args = new String[]{"java", "--long-option1=value1", "--long-option2=value2"};
        Options options = new Options();
        Option option1 = new Option("long-option1", true, null, "longOpt1");
        Option option2 = new Option("long-option2", true, null, "longOpt2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("java", "--long-option1=value1", "--long-option2=value2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption() {
        String[] args = new String[]{"java", "--invalid"};
        Options options = new Options();
        Parser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithEmptyArgument() {
        String[] args = new String[]{"java", "--option=", "--option2"};
        Options options = new Options();
        Option option1 = new Option("option", true, null, "opt");
        Option option2 = new Option("option2", true, null, "opt2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = new String[]{"java", "--option", "value", "nonOption"};
        Options options = new Options();
        Option option = new Option("option", true, "o", "opt");
        options.addOption(option);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("java", "--option=value");
        Assert.assertArrayEquals(expected.toArray(), result);
    }

    @Test
    public void testFlattenWithMultipleStopAtNonOption() {
        String[] args = new String[]{"java", "--option1", "value1", "option2", "value2", "nonOption"};
        Options options = new Options();
        Option option1 = new Option("option1", true, "o1", "opt1");
        Option option2 = new Option("option2", true, "o2", "opt2");
        options.addOption(option1);
        options.addOption(option2);
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("java", "--option1=value1", "option2", "value2");
        Assert.assertArrayEquals(expected.toArray(), result);
    }
}