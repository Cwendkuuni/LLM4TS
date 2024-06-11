import org.apache.commons.cli.*;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    

    @Test
    public void testInit() {
        PosixParser parser = new PosixParser();
        parser.init();
        assertFalse(parser.eatTheRest);
        assertTrue(parser.tokens.isEmpty());
        assertNull(parser.currentOption);
    }

    @Test
    public void testFlattenBasic() {
        String[] args = {"--option1", "--option2=value1", "-o", "option3", "value3", "--"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Description for option1");
        Option option2 = new Option("o", "option2", true, "Description for option2");
        Option option3 = new Option("o", "option3", false, "Description for option3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        parser.flatten(options, args, false);
        assertEquals(args.length, parser.tokens.size());
        assertArrayEquals(args, parser.tokens.toArray());
    }

    @Test
    public void testFlattenStopAtNonOption() {
        String[] args = {"--option1", "--option2=value1", "-o", "option3", "value3", "nonOption"};
        PosixParser parser = new PosixParser();
        Options options = new Options();
        Option option1 = new Option("o", "option1", false, "Description for option1");
        Option option2 = new Option("o", "option2", true, "Description for option2");
        Option option3 = new Option("o", "option3", false, "Description for option3");
        options.addOption(option1);
        options.addOption(option2);
        options.addOption(option3);
        parser.flatten(options, args, true);
        assertEquals(args.length - 1, parser.tokens.size());
        String[] expected = {"--option1", "--option2=value1", "-o", "option3", "value3"};
        assertArrayEquals(expected, parser.tokens.subList(0, parser.tokens.size() - 1).toArray());
    }

    @Test
    public void testProcessSingleHyphen() {
        PosixParser parser = new PosixParser();
        String[] args = {"-"};
        parser.flatten(new Options(), args, false);
        assertEquals(1, parser.tokens.size());
        assertEquals("-", parser.tokens.get(0));
    }

    @Test
    public void testProcessOptionToken() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "option1"};
        parser.options = new Options();
        Option option1 = new Option("o", "option1", false, "Description for option1");
        parser.options.addOption(option1);
        parser.flatten(parser.options, args, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("option1", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenBasic() {
        PosixParser parser = new PosixParser();
        String[] args = {"-foo"};
        parser.flatten(new Options(), args, false);
        assertEquals(2, parser.tokens.size());
        assertEquals("-foo", parser.tokens.get(0));
        assertEquals("foo", parser.tokens.get(1));
    }

    @Test
    public void testBurstTokenWithSeparator() {
        PosixParser parser = new PosixParser();
        String[] args = {"-foo--bar"};
        parser.flatten(new Options(), args, false);
        assertEquals(4, parser.tokens.size());
        assertEquals("-foo--", parser.tokens.get(0));
        assertEquals("foo", parser.tokens.get(1));
        assertEquals("--", parser.tokens.get(2));
        assertEquals("bar", parser.tokens.get(3));
    }

    @Test(expected = IllegalArgumentException.class)
    public void testBurstTokenNoOption() {
        PosixParser parser = new PosixParser();
        String[] args = {"-nonOption"};
        parser.flatten(new Options(), args, false);
    }

    @Test
    public void testBurstTokenWithArg() {
        PosixParser parser = new PosixParser();
        String[] args = {"-o", "option1=value1"};
        parser.options = new Options();
        Option option1 = new Option("o", "option1", true, "Description for option1");
        parser.options.addOption(option1);
        parser.flatten(parser.options, args, false);
        assertEquals(3, parser.tokens.size());
        assertEquals("-o", parser.tokens.get(0));
        assertEquals("option1", parser.tokens.get(1));
        assertEquals("value1", parser.tokens.get(2));
    }

    
}