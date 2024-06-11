package org.apache.commons.cli.tests;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * Test class for PosixParser
 */
public class PosixParserTest {

    

    @Test
    public void testFlatten_SimpleOptions() {
        String[] args = {"--option1", "--option2=value1", "--option3=value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3");
        options.addOption("option4", true, "o4");
         String[] result = parser.flatten(options, args, false);
         List<String> expected = Arrays.asList("--option1", "--option2=value1", "--option3=value2");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_SimpleOptionsWithStopAtNonOption() {
        String[] args = {"--option1", "--option2=value1", "--option3=value2", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
        options.addOption("option3", true, "o3");
        options.addOption("option4", true, "o4");
         String[] result = parser.flatten(options, args, true);
         List<String> expected = Arrays.asList("--option1", "--option2=value1", "--option3=value2", "nonOption");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_LongOption() {
        String[] args = {"--longOption=value1", "--longOption=value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
         String[] result = parser.flatten(options, args, false);
         List<String> expected = Arrays.asList("--longOption=value1", "--longOption=value2");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_LongOptionWithValue() {
        String[] args = {"--longOption=value1=subvalue1", "--longOption=value1=subvalue2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        options.addOption("longOption", true, "lo").setValueSeparator('=');
        options.addOption("longOption", true, "lo").setLongOpt("longOptionWithSubValue");
         String[] result = parser.flatten(options, args, false);
         List<String> expected = Arrays.asList("--longOption=value1=subvalue1", "--longOption=value1=subvalue2");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_InvalidOption() {
        String[] args = {"--invalidOption", "--option1", "--option2=value1"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
         String[] result = null;
         try {
             result = parser.flatten(options, args, false);
         } catch (ParseException e) {
             // expected
         }
         Assert.assertNull(result);
    }

    @Test
    public void testFlatten_DoubleDash() {
        String[] args = {"--", "--option1=value1", "--option2=value2"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
        options.addOption("option2", true, "o2");
         String[] result = parser.flatten(options, args, false);
         List<String> expected = Arrays.asList("--", "--option1=value1", "--option2=value2");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlatten_DoubleDashBeforeOption() {
        String[] args = {"--", "--option1=value1"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("option1", true, "o1");
         String[] result = parser.flatten(options, args, false);
         List<String> expected = Arrays.asList("--", "--option1=value1");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = RuntimeException.class)
    public void testFlatten_Null() {
        String[] args = null;
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(null, args, false);
    }

    @Test
    public void testBurstToken_ValidOption() {
        String token = "-o1";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("o1", true, "o1");
        String[] result = parser.burstToken(token, false);
         List<String> expected = Arrays.asList("-o1");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken_ValidOptionWithValue() {
        String token = "--longOption=value";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        options.addOption("longOption", true, "lo");
        String[] result = parser.burstToken(token, false);
         List<String> expected = Arrays.asList("--longOption=value");
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken_InvalidOption() {
        String token = "-i";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.burstToken(token, false);
         List<String> expected = Arrays.asList();
         Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testBurstToken_InvalidOptionWithStopAtNonOption() {
        String token = "-i";
        PosixParser parser = new PosixParser();
        Options options = new Options();
        String[] result = parser.burstToken(token, true);
        List<String> expected = Arrays.asList("-i");
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testProcess_NothingToProcess() {
        PosixParser parser = new PosixParser();
        String token = "nonOption";
        parser.process(token);
        List<String> expected = Arrays.asList("--", "nonOption");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcess_ValidOption() {
        PosixParser parser = new PosixParser();
        String token = "-o1";
        parser.process(token);
        List<String> expected = Arrays.asList("-o1");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcess_LongOption() {
        PosixParser parser = new PosixParser();
        String token = "--longOption";
        parser.process(token);
        List<String> expected = Arrays.asList("--longOption");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcess_LongOptionWithValue() {
        PosixParser parser = new PosixParser();
        String token = "--longOption=value";
        parser.process(token);
        List<String> expected = Arrays.asList("--longOption=value");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcess_InvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "-i";
        parser.process(token);
        List<String> expected = Arrays.asList("--", "--invalidOption=-i");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcess_InvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-i";
        parser.process(token);
        List<String> expected = Arrays.asList("--", "-i");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        PosixParser parser = new PosixParser();
        String token = "-o1";
        parser.processOptionToken(token, false);
        List<String> expected = Arrays.asList("-o1");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcessOptionToken_ValidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-o1";
        parser.processOptionToken(token, true);
        List<String> expected = Arrays.asList("-o1");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcessOptionToken_InvalidOption() {
        PosixParser parser = new PosixParser();
        String token = "-i";
        parser.processOptionToken(token, false);
        List<String> expected = Arrays.asList("--", "--invalidOption=-i");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testProcessOptionToken_InvalidOptionWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-i";
        parser.processOptionToken(token, true);
        List<String> expected = Arrays.asList("-i");
        Assert.assertEquals(expected.toString(), parser.tokens.toString());
        parser.tokens.clear();
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);
    }

    
}