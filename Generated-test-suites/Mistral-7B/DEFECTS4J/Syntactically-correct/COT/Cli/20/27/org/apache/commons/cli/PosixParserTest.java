import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        String[] arguments = new String[]{"--foo", "--bar=1", "-a", "-b", "--baz=2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("baz", true, "z");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, false);
        List<String> expected = Arrays.asList(
                "--foo",
                "--bar",
                "1",
                "-a",
                "-b",
                "--baz",
                "2",
                "--",
                "arg1",
                "arg2"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() throws ParseException {
        String[] arguments = new String[]{"--foo", "--bar=1", "-a", "-b", "--baz=2", "-c", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("baz", true, "z");
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, arguments, true);
        List<String> expected = Arrays.asList(
                "--foo",
                "--bar=1",
                "-a",
                "-b",
                "--baz=2",
                "-c",
                "arg1",
                "arg2"
        );
        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        String[] arguments = new String[]{"--foo", "--bar=1", "-a", "-b", "--baz=2", "-c", "-- InvalidOption", "--", "arg1", "arg2"};
        Options options = new Options();
        options.addOption("foo", true, "f");
        options.addOption("bar", true, "b");
        options.addOption("baz", true, "z");
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testBurstTokenWithOption() {
        PosixParser parser = new PosixParser();
        String token = "-foobar";
        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-f", "-o", "-o", "-b", "-a", "-r");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    @Test
    public void testBurstTokenWithStopAtNonOption() {
        PosixParser parser = new PosixParser();
        String token = "-foobar";
        parser.burstToken(token, true);
        List<String> expected = Arrays.asList("--", "-foobar");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    @Test
    public void testBurstTokenWithNoOption() {
        PosixParser parser = new PosixParser();
        String token = "-abc";
        parser.burstToken(token, false);
        List<String> expected = Arrays.asList("-a", "-b", "-c");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    @Test
    public void testProcess() {
        PosixParser parser = new PosixParser();
        parser.init();
        parser.process("--foo");
        List<String> expected = Arrays.asList("--foo");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }

        parser.init();
        parser.process("--foo=bar");
        expected = Arrays.asList("--foo", "bar");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }

        parser.init();
        parser.process("--");
        expected = Arrays.asList("--", "--");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }

        parser.init();
        parser.process("arg1");
        expected = Arrays.asList("--", "arg1");
        Assert.assertEquals(expected.size(), parser.tokens.size());
        for (int i = 0; i < expected.size(); i++) {
            Assert.assertEquals(expected.get(i), parser.tokens.get(i));
        }
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String token = "-foo";
        parser.processOptionToken(token, false);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-foo", parser.tokens.get(0));
        Assert.assertNull(parser.currentOption);

        Options options = new Options();
        options.addOption("foo", true, "f");
        parser = new PosixParser();
        parser.init();
        parser.processOptionToken(token, false);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertEquals("-foo", parser.tokens.get(1));
        Assert.assertEquals(options.getOption("foo"), parser.currentOption);

        parser.init();
        parser.processOptionToken("--invalid", false);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertNull(parser.currentOption);

        parser.init();
        parser.processOptionToken("--foo=bar", false);
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertEquals("--", parser.tokens.get(1));
        Assert.assertEquals("bar", parser.tokens.get(2));
        Assert.assertEquals(options.getOption("foo"), parser.currentOption);
    }
}