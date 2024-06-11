package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    @Test
    public void testFlattenWithNoOptions()
    {
        String[] args = new String[]{"java", "MyProgram"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "MyProgram");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithSingleOption()
    {
        String[] args = new String[]{"java", "--opt", "value"};
        Options options = new Options();
        options.addOption("opt", true, "Option test");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--opt", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithSingleValueOption()
    {
        String[] args = new String[]{"java", "--opt", "value"};
        Options options = new Options();
        options.addOption("opt", true, "Option test");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--opt", "value");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMultipleOptions()
    {
        String[] args = new String[]{"java", "--opt1", "val1", "--opt2", "val2"};
        Options options = new Options();
        options.addOption("opt1", true, "Option 1 test");
        options.addOption("opt2", true, "Option 2 test");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--opt1", "val1", "--opt2", "val2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptions()
    {
        String[] args = new String[]{"java", "--long-option-with-value", "val"};
        Options options = new Options();
        options.addOption("longOptionWithValue", true, "Long option test");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--long-option-with-value", "val");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithMixedOptionsAndValues()
    {
        String[] args = new String[]{"java", "--opt1", "val1", "--opt2=val2", "--opt3"};
        Options options = new Options();
        options.addOption("opt1", true, "Option 1 test");
        options.addOption("opt2", true, "Option 2 test with value");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List<String> expected = Arrays.asList("java", "--opt1", "val1", "--opt2", "val2");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        String[] args = new String[]{"java", "--opt1", "val1", "nonOption"};
        Options options = new Options();
        options.addOption("opt1", true, "Option 1 test");
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List<String> expected = Arrays.asList("java", "--opt1", "val1", "nonOption");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithInvalidOption()
    {
        String[] args = new String[]{"java", "--invalidOption"};
        Options options = new Options();
        Parser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        // test should fail with an IllegalArgumentException
    }
}