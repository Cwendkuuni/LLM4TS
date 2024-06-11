import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testFlattenBasic() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with two arguments");

        String[] arguments = {"--a", "--b", "1", "--c", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "-c", "arg1", "arg2"}, parsedArguments);
    }

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with two arguments");

        String[] arguments = {"--a", "--b", "1", "--c", "arg1", "arg2", "nonOption"};
        PosixParser parser = new PosixParser();
        String[] parsedArguments = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "1", "--c", "arg1", "arg2"}, parsedArguments);
        Assert.assertEquals(Arrays.asList("nonOption").toString(), Arrays.asList(parser.getTokens()).subList(parser.getTokens().size() - 1, parser.getTokens().size()).toString());
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");

        String[] arguments = {"--invalidOption", "--a"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, arguments, false);
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an argument");
        options.addOption("c", 3, "Option C with two arguments");

        PosixParser parser = new PosixParser();

        parser.processOptionToken("--a", false);
        Assert.assertEquals(Option.class, parser.getCurrentOption().getClass());
        Assert.assertEquals("a", parser.getCurrentOption().getLongOpt());
        Assert.assertNull(parser.getCurrentOption().getValue());
        Assert.assertNull(parser.getTokens().size() > 0 ? parser.getTokens().get(0) : null);

        parser.init();
        parser.processOptionToken("--a=value", false);
        Assert.assertEquals("a", parser.getCurrentOption().getLongOpt());
        Assert.assertEquals("value", parser.getCurrentOption().getValue());
        Assert.assertNull(parser.getTokens().size() > 0 ? parser.getTokens().get(0) : null);

        parser.init();
        parser.processOptionToken("--b", false);
        Assert.assertEquals(Option.class, parser.getCurrentOption().getClass());
        Assert.assertEquals("b", parser.getCurrentOption().getLongOpt());
        Assert.assertEquals(1, parser.getCurrentOption().getArgPosition());
        Assert.assertNull(parser.getCurrentOption().getValue());
        Assert.assertNull(parser.getTokens().size() > 0 ? parser.getTokens().get(0) : null);

        parser.init();
        parser.processOptionToken("--b=value", false);
        Assert.assertEquals("b", parser.getCurrentOption().getLongOpt());
        Assert.assertEquals("value", parser.getCurrentOption().getValue());
        Assert.assertEquals(1, parser.getTokens().size());
        Assert.assertEquals("value", parser.getTokens().get(0));

        parser.init();
        parser.processOptionToken("-b", false);
        Assert.assertNull(parser.getCurrentOption());
        Assert.assertEquals(1, parser.getTokens().size());
        Assert.assertEquals("-b", parser.getTokens().get(0));

        parser.init();
        parser.processOptionToken("-c", false);
        Assert.assertNull(parser.getCurrentOption());
        Assert.assertEquals(1, parser.getTokens().size());
        Assert.assertEquals("-c", parser.getTokens().get(0));

        parser.init();
        parser.processOptionToken("--", false);
        Assert.assertNull(parser.getCurrentOption());
        Assert.assertEquals(2, parser.getTokens().size());
        Assert.assertEquals("--", parser.getTokens().get(0));
        Assert.assertEquals(null, parser.getTokens().get(1));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("ab", true, "Option AB");
        options.addOption("cd", 2, "Option CD with two arguments");

        PosixParser parser = new PosixParser();

        parser.burstToken("--ab", false);
        Assert.assertEquals(1, parser.getTokens().size());
        Assert.assertEquals("--ab", parser.getTokens().get(0));

        parser.init();
        parser.burstToken("-ab", false);
        Assert.assertEquals(1, parser.getTokens().size());
        Assert.assertEquals("-ab", parser.getTokens().get(0));

        parser.init();
        parser.burstToken("-abcd", false);
        Assert.assertEquals(3, parser.getTokens().size());
        Assert.assertEquals("-ab", parser.getTokens().get(0));
        Assert.assertEquals("-cd", parser.getTokens().get(1));
        Assert.assertEquals(null, parser.getTokens().get(2));

        parser.init();
        parser.burstToken("--abcd", false);
        Assert.assertEquals(4, parser.getTokens().size());
        Assert.assertEquals("--ab", parser.getTokens().get(0));
        Assert.assertEquals("--cd", parser.getTokens().get(1));
        Assert.assertEquals(null, parser.getTokens().get(2));
        Assert.assertEquals(null, parser.getTokens().get(3));

        parser.init();
        parser.burstToken("--abcd=value", false);
        Assert.assertEquals(5, parser.getTokens().size());
        Assert.assertEquals("--ab", parser.getTokens().get(0));
        Assert.assertEquals("--cd", parser.getTokens().get(1));
        Assert.assertEquals("value", parser.getTokens().get(2));
        Assert.assertEquals(null, parser.getTokens().get(3));
        Assert.assertEquals(null, parser.getTokens().get(4));

        parser.init();
        parser.burstToken("--abcd=value1=value2", false);
        Assert.assertEquals(6, parser.getTokens().size());
        Assert.assertEquals("--ab", parser.getTokens().get(0));
        Assert.assertEquals("--cd", parser.getTokens().get(1));
        Assert.assertEquals("value1", parser.getTokens().get(2));
        Assert.assertEquals("value2", parser.getTokens().get(3));
        Assert.assertEquals(null, parser.getTokens().get(4));
        Assert.assertEquals(null, parser.getTokens().get(5));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();

        List<String> tokens = new ArrayList<>();
        parser.init();
        parser.gobble(Arrays.asList("--a", "--b", "1", "--c", "arg1", "arg2").iterator());
        Assert.assertEquals(Arrays.asList("--a", "--b", "1", "--c", "arg1", "arg2").toString(), tokens.toString());

        tokens.clear();
        parser.init();
        parser.gobble(Arrays.asList("--a", "--b", "1", "--c", "arg1", "arg2", "nonOption").iterator());
        Assert.assertEquals(Arrays.asList("--a", "--b", "1", "--c", "arg1", "arg2").toString(), tokens.subList(0, tokens.size() - 1).toString());
        Assert.assertEquals(Arrays.asList("nonOption").toString(), Arrays.asList(parser.getTokens().get(parser.getTokens().size() - 1)).toString());
    }
}