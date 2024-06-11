package org.apache.commons.cli.test;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    private Parser parser;
    private Options options;

    @Before
    public void setup() {
        parser = new PosixParser();
        options = new Options();
    }

    

    @Test
    public void testFlatten_WithoutOptions() {
        String[] arguments = new String[]{"arg1", "arg2"};
        String[] expectedResult = new String[]{"arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testFlatten_WithOptions() {
        options.addOption("option1", true, "o");
        options.addOption("option2", 1, "i");
        String[] arguments = new String[]{"-o", "value1", "--option2", "value2", "arg1", "arg2"};
        String[] expectedResult = new String[]{"-o", "option1", "true", "--option2", "value2", "arg1", "arg2"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlatten_WithInvalidOption() {
        options.addOption("option1", true, "o");
        String[] arguments = new String[]{"--invalidOption", "arg1", "arg2"};

        parser.flatten(options, arguments, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        options.addOption("option1", true, "o");
        String[] arguments = new String[]{"--option1", "value1", "nonOption", "arg1", "arg2"};
        String[] expectedResult = new String[]{"--option1", "value1"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(expectedResult, result);
    }

    @Test
    public void testBurstToken_SimpleOption() {
        String token = "-o";
        String[] expectedResult = new String[]{"-o", "option1"};

        parser.burstToken(token, false);

        Assert.assertEquals(Arrays.toString(expectedResult), Arrays.toString((String[]) parser.tokens.toArray()));
        parser.init();
    }

    @Test
    public void testBurstToken_OptionWithArgument() {
        String token = "--option2=value2";
        String[] expectedResult = new String[]{"-option2", "value2"};

        parser.burstToken(token, false);

        Assert.assertEquals(Arrays.toString(expectedResult), Arrays.toString((String[]) parser.tokens.toArray()));
        parser.init();
    }

    @Test
    public void testBurstToken_StopAtNonOption() {
        String token = "-n";
        String[] expectedResult = new String[]{"--", "n"};

        parser.burstToken(token, true);

        Assert.assertEquals(Arrays.toString(expectedResult), Arrays.toString((String[]) parser.tokens.toArray()));
        parser.init();
    }

    
}