import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class PosixParserTest {

    private PosixParser posixParser;

    @Before
    public void setUp() {
        posixParser = new PosixParser();
    }

    @Test
    public void testInit() {
        posixParser.init();
        assertEquals(false, posixParser.eatTheRest);
        assertEquals(0, posixParser.tokens.size());
    }

    @Test
    public void testFlatten() {
        Options options = new Options();
        String[] arguments = new String[] {"--foo", "bar", "-abc", "arg1", "-def"};
        boolean stopAtNonOption = false;

        String[] flattenedArguments = posixParser.flatten(options, arguments, stopAtNonOption);

        assertEquals(8, flattenedArguments.length);
        assertEquals("--foo", flattenedArguments[0]);
        assertEquals("bar", flattenedArguments[1]);
        assertEquals("-a", flattenedArguments[2]);
        assertEquals("-b", flattenedArguments[3]);
        assertEquals("-c", flattenedArguments[4]);
        assertEquals("arg1", flattenedArguments[5]);
        assertEquals("-d", flattenedArguments[6]);
        assertEquals("-e", flattenedArguments[7]);
    }

    @Test
    public void testGobble() {
        Iterator iterator = Arrays.asList("--foo", "bar", "-a", "arg1").iterator();
        posixParser.eatTheRest = true;
  
        posixParser.gobble(iterator);
  
        assertEquals(4, posixParser.tokens.size());
        assertEquals("--foo", posixParser.tokens.get(0));
        assertEquals("bar", posixParser.tokens.get(1));
        assertEquals("-a", posixParser.tokens.get(2));
        assertEquals("arg1", posixParser.tokens.get(3));
    }

    @Test
    public void testProcessNonOptionToken() {
        posixParser.processNonOptionToken("value");

        assertEquals(true, posixParser.eatTheRest);
        assertEquals("--", posixParser.tokens.get(0));
        assertEquals("value", posixParser.tokens.get(1));
    }

    @Test
    public void testProcessOptionToken() {
        boolean stopAtNonOption = false;

        posixParser.processOptionToken("--foo", stopAtNonOption);

        assertEquals("--foo", posixParser.tokens.get(0));
    }

    @Test
    public void testBurstToken() {
        Options options = new Options();
        String[] arguments = new String[] {"-abc", "-def"};
        boolean stopAtNonOption = false;
        String ch1 = String.valueOf(arguments[0].charAt(1));
        String ch2 = String.valueOf(arguments[0].charAt(2));
        Option optionAB = new Option(ch1, "", false, "");
        Option optionC = new Option(ch2, "", false, "");
        options.addOption(optionAB);
        options.addOption(optionC);
        Option optionD = new Option("d", "", false, "");
        Option optionE = new Option("e", "", false, "");
        options.addOption(optionD);
        options.addOption(optionE);

        posixParser.burstToken(arguments[0], stopAtNonOption);

        assertEquals(5, posixParser.tokens.size());
        assertEquals("-" + ch1, posixParser.tokens.get(0));
        assertEquals("-" + ch2, posixParser.tokens.get(1));
        assertEquals("-d", posixParser.tokens.get(2));
        assertEquals("-e", posixParser.tokens.get(3));
        assertEquals("-f", posixParser.tokens.get(4));
    }

}