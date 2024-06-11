import org.junit.Assert;
import org.junit.Test;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        Assert.assertFalse(parser.eatTheRest);
        Assert.assertTrue(parser.tokens.isEmpty());
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").build());
        options.addOption(Option.builder("r").build());

        PosixParser parser = new PosixParser();
        String[] arguments = {"-f", "-o", "output.txt"};
        String[] result = parser.flatten(options, arguments, true);

        Assert.assertEquals(4, result.length);
        Assert.assertEquals("-f", result[0]);
        Assert.assertEquals("-o", result[1]);
        Assert.assertEquals("output.txt", result[2]);
        Assert.assertEquals("--", result[3]);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").build());
        options.addOption(Option.builder("r").build());

        PosixParser parser = new PosixParser();
        boolean stopAtNonOption = true;
        parser.processOptionToken("-f", stopAtNonOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertNotNull(parser.currentOption);

        parser.init();
        stopAtNonOption = false;
        parser.processOptionToken("-f", stopAtNonOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertNotNull(parser.currentOption);

        parser.init();
        stopAtNonOption = true;
        parser.processOptionToken("-z", stopAtNonOption);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);

        parser.init();
        stopAtNonOption = false;
        parser.processOptionToken("-z", stopAtNonOption);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-", parser.tokens.get(0));
        Assert.assertEquals("z", parser.tokens.get(1));
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption(Option.builder("f").hasArg().build());
        options.addOption(Option.builder("o").hasArgs().build());
        options.addOption(Option.builder("r").build());

        PosixParser parser = new PosixParser();
        parser.currentOption = options.getOption("f");
        parser.process("file.txt");
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("file.txt", parser.tokens.get(0));
        Assert.assertNull(parser.currentOption);

        parser.init();
        parser.currentOption = options.getOption("o");
        parser.process("output1.txt");
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("output1.txt", parser.tokens.get(0));
        Assert.assertNull(parser.currentOption);

        parser.init();
        parser.currentOption = options.getOption("r");
        parser.process("value");
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);

        parser.init();
        parser.process("value");
        Assert.assertEquals(3, parser.tokens.size());
        Assert.assertEquals("--", parser.tokens.get(0));
        Assert.assertEquals("value", parser.tokens.get(1));
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption(Option.builder("f").build());
        options.addOption(Option.builder("o").build());
        options.addOption(Option.builder("r").build());

        PosixParser parser = new PosixParser();
        parser.options = options;

        boolean stopAtNonOption = true;
        parser.burstToken("-fo", stopAtNonOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertNotNull(parser.currentOption);

        parser.init();
        stopAtNonOption = false;
        parser.burstToken("-fo", stopAtNonOption);
        Assert.assertEquals(1, parser.tokens.size());
        Assert.assertEquals("-f", parser.tokens.get(0));
        Assert.assertNotNull(parser.currentOption);

        parser.init();
        stopAtNonOption = true;
        parser.burstToken("-zo", stopAtNonOption);
        Assert.assertEquals(0, parser.tokens.size());
        Assert.assertNull(parser.currentOption);

        parser.init();
        stopAtNonOption = false;
        parser.burstToken("-zo", stopAtNonOption);
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("-", parser.tokens.get(0));
        Assert.assertEquals("zo", parser.tokens.get(1));
        Assert.assertNull(parser.currentOption);
    }
}