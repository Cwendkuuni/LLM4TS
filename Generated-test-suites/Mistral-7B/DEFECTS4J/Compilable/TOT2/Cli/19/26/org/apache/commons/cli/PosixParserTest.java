package org.apache.commons.cli;
import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlattenEmptyArgs() {
        Options options = new Options();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], flattenedArgs);
    }

    @Test
    public void testFlattenBasicUsage() {
        Options options = new Options();
        String[] args = {"--option1", "value1", "--option2=value2", "--option3"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(Arrays.asList("--option1", "--option2=value2", "--option3").toArray(new String[0]), flattenedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOption() {
        Options options = new Options();
        String[] args = {"--unknownOption"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test(expected = ParseException.class)
    public void testFlattenUnknownOptionWithValue() {
        Options options = new Options();
        String[] args = {"--unknownOption=value"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "WithArgument");
        options.addOption("b", false, "WithoutArgument");
        String[] args = {"--a", "--b", "arg1", "--c", "arg2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, true);
        Assert.assertArrayEquals(Arrays.asList("--a", "--c", "arg1", "arg2").toArray(new String[0]), flattenedArgs);
    }

    @Test
    public void testFlattenLongOption() {
        Options options = new Options();
        String[] args = {"--longOption"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertEquals("--longOption", flattenedArgs[0]);
    }

    @Test
    public void testFlattenLongOptionWithValue() {
        Options options = new Options();
        String[] args = {"--longOption=value"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertEquals("--longOption=value", flattenedArgs[0]);
    }

    @Test
    public void testFlattenLongOptionWithValueAndOtherArgs() {
        Options options = new Options();
        options.addOption("l", "longOption", true, "A long option.");
        String[] args = {"--longOption=value1", "arg1", "--longOption=value2"};
        PosixParser parser = new PosixParser();
        String[] flattenedArgs = parser.flatten(options, args, false);
        Assert.assertArrayEquals(Arrays.asList("--longOption=value1", "--longOption=value2", "arg1").toArray(new String[0]), flattenedArgs);
    }
}