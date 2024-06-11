import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.apache.commons.cli.CommonOptions.*;
import static org.apache.commons.cli.Options.builder;

public class PosixParserTest {

    @Test
    public void testFlatten_NoArguments() {
        Options options = builder().build();
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{}, result);
    }

    @Test
    public void testFlatten_NoOptions() {
        Options options = builder().build();
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOption() {
        Options options = builder().addOption(HELP_OPTION, true, "help", "Print this help message.").build();
        String[] args = {"--help"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--help"}, result);
    }

    @Test
    public void testFlatten_SingleCharacterOption() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        String[] args = {"-h"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-h"}, result);
    }

    @Test
    public void testFlatten_SingleCharacterOptionWithArgument() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        String[] args = {"-h", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"-h", "arg"}, result);
    }

    @Test
    public void testFlatten_MultipleArguments() {
        Options options = builder().build();
        String[] args = {"arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlatten_InvalidOption() {
        Options options = builder().build();
        String[] args = {"--invalid"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testFlatten_StopAtNonOption() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        String[] args = {"arg1", "-h", "arg3"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        List expected = Arrays.asList("arg1", "-h", "arg3");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithArgument() {
        Options options = builder().addOption(HELP_OPTION, true, "help", "Print this help message.").build();
        String[] args = {"--help", "arg"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--help", "arg"}, result);
    }

    @Test
    public void testFlatten_MultipleLongOptions() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").addOption(HEADER_OPTION, true, "H", "Print header.").build();
        String[] args = {"--help", "--header"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--help", "--header");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testFlatten_LongOptionWithArgumentAndOtherArguments() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        String[] args = {"--help", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        List expected = Arrays.asList("--help", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), result);
    }

    @Test
    public void testProcessOptionToken_ValidOption() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        PosixParser parser = new PosixParser();
        String[] args = {"-h"};
        parser.processOptionToken(args[0], false);
        Assert.assertNull(parser.currentOption);
        Assert.assertTrue(parser.tokens.contains("-h"));
    }

    @Test
    public void testProcessOptionToken_InvalidOption() {
        Options options = builder().build();
        PosixParser parser = new PosixParser();
        String[] args = {"--invalid"};
        parser.processOptionToken(args[0], false);
        Assert.assertNull(parser.currentOption);
        Assert.assertTrue(parser.tokens.contains("--invalid"));
    }

    @Test
    public void testProcessOptionToken_StopAtNonOption() {
        Options options = builder().addOption(HELP_OPTION, true, "h", "Print this help message.").build();
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "-h", "arg3"};
        parser.processOptionToken(args[1], true);
        Assert.assertNull(parser.currentOption);
        List expected = Arrays.asList("arg1", "-h", "arg3");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testProcess_SingleArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1"};
        parser.process(args[0]);
        List expected = Arrays.asList("--", "arg1");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testProcess_MultipleArguments() {
        PosixParser parser = new PosixParser();
        String[] args = {"arg1", "arg2"};
        parser.process(args[1]);
        List expected = Arrays.asList("--", "arg1", "arg2");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testProcess_LongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"--help", "arg1"};
        parser.process(args[0]);
        List expected = Arrays.asList("--help", "arg1");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testProcess_LongOptionWithoutArgument() {
        PosixParser parser = new PosixParser();
        String[] args = {"--help"};
        parser.process(args[0]);
        List expected = Arrays.asList("--help");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.toArray(new String[0]));
    }

    @Test
    public void testBurstToken_SingleCharacterOption() {
        PosixParser parser = new PosixParser();
        String token = "-h";
        parser.burstToken(token, false);
        List expected = Arrays.asList("-h");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.subList(0, parser.tokens.size() - 1).toArray(new String[0]));
        Assert.assertEquals("h", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstToken_LongOption() {
        PosixParser parser = new PosixParser();
        String token = "--help";
        parser.burstToken(token, false);
        List expected = Arrays.asList("--help");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.subList(0, parser.tokens.size() - 1).toArray(new String[0]));
        Assert.assertEquals("help", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstToken_LongOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String token = "--help=arg";
        parser.burstToken(token, false);
        List expected = Arrays.asList("--help", "arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.subList(0, parser.tokens.size() - 1).toArray(new String[0]));
        Assert.assertEquals("help", parser.tokens.get(parser.tokens.size() - 2));
        Assert.assertEquals("arg", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test
    public void testBurstToken_SingleCharacterOptionWithArgument() {
        PosixParser parser = new PosixParser();
        String token = "-h=arg";
        parser.burstToken(token, false);
        List expected = Arrays.asList("-h", "arg");
        Assert.assertArrayEquals(expected.toArray(new String[0]), parser.tokens.subList(0, parser.tokens.size() - 1).toArray(new String[0]));
        Assert.assertEquals("h", parser.tokens.get(parser.tokens.size() - 2));
        Assert.assertEquals("arg", parser.tokens.get(parser.tokens.size() - 1));
    }

    @Test(expected = ParseException.class)
    public void testBurstToken_InvalidToken() {
        PosixParser parser = new PosixParser();
        String token = "--invalid";
        parser.burstToken(token, false);
    }
}