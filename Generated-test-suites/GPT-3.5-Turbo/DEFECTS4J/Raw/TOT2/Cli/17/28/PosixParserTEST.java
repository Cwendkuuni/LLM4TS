import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        // Check if tokens list is empty
        Assert.assertTrue(parser.tokens.isEmpty());
        // Check if eatTheRest is false
        Assert.assertFalse(parser.eatTheRest);
        // Check if currentOption is null
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "valueA", "-b=valueB"};
        String[] result = parser.flatten(options, args, true);

        // Check if the result contains the correct values
        Assert.assertArrayEquals(new String[]{"-a", "valueA", "-b", "valueB"}, result);
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        parser.processSingleHyphen("-");
        // Check if the hyphen is added to the tokens list
        Assert.assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        Options options = new Options();
        options.addOption("c", false, "Option C");

        PosixParser parser = new PosixParser();
        parser.processOptionToken("-c", false);
        // Check if the option is added to the tokens list
        Assert.assertEquals("-c", parser.tokens.get(0));

        parser.processOptionToken("-d", true);
        // Check if eatTheRest is true when option does not exist and stopAtNonOption is true
        Assert.assertTrue(parser.eatTheRest);
    }

    @Test
    public void testProcess() {
        Options options = new Options();
        options.addOption("e", false, "Option E");
        Option option = new Option("f", "Option F");
        option.setArg(true);
        options.addOption(option);

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.process("valueE");
        // Check if value is added to the tokens list
        Assert.assertEquals("valueE", parser.tokens.get(0));

        parser.currentOption = option;
        parser.process("valueF");
        // Check if value is added to the tokens list
        Assert.assertEquals("valueF", parser.tokens.get(1));
        // Check if currentOption is reset to null
        Assert.assertNull(parser.currentOption);
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        options.addOption("g", true, "Option G");

        PosixParser parser = new PosixParser();
        parser.options = options;

        parser.burstToken("-gh", false);
        // Check if both "-g" and "h" are added to the tokens list
        Assert.assertEquals("-g", parser.tokens.get(0));
        Assert.assertEquals("h", parser.tokens.get(1));
    }

    @Test
    public void testGobble() {
        PosixParser parser = new PosixParser();
        parser.eatTheRest = true;
        parser.tokens.add("remainingToken");

        Iterator<String> iter = Arrays.asList("additionalToken").iterator();
        parser.gobble(iter);

        // Check if remaining token is added to the tokens list
        Assert.assertEquals(2, parser.tokens.size());
        Assert.assertEquals("remainingToken", parser.tokens.get(0));
        Assert.assertEquals("additionalToken", parser.tokens.get(1));
    }
}