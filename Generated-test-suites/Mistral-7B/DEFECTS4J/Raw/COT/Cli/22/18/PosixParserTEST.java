import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest
{
    private Options options;

    @Before
    public void setUp()
    {
        options = new Options();
        options.addOption("a", true, "option A");
        options.addOption("b", 2, "option B");
        options.addOption("c", 3, "option C");
    }

    @Test
    public void testFlattenEmpty()
    {
        String[] args = {};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[0], result);
    }

    @Test
    public void testFlattenSimple()
    {
        String[] args = {"--a", "1", "--b=2", "c", "-d"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);
        Assert.assertArrayEquals(new String[]{"--a", "1", "--b", "2", "--", "c", "--", "d"}, result);
    }

    @Test
    public void testFlattenWithStopAtNonOption()
    {
        String[] args = {"--a", "1", "--b=2", "c", "-d", "e", "f"};
        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);
        Assert.assertArrayEquals(new String[]{"--a", "1", "--b=2", "--", "c", "--", "d", "e", "f"}, result);
    }

    @Test(expected = ParseException.class)
    public void testFlattenInvalidOption()
    {
        String[] args = {"--invalid-option"};
        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);
    }

    @Test
    public void testBurstTokenSingleCharacter()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-a", false);
        Assert.assertEquals(Arrays.asList("-a"), tokens);
    }

    @Test
    public void testBurstTokenMultiCharacterOption()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-ab", false);
        Assert.assertEquals(Arrays.asList("-a", "-b"), tokens);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOption()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-invalid-option", false);
    }

    @Test
    public void testBurstTokenOptionWithArgument()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-c", false);
        Assert.assertEquals(Arrays.asList("-c"), tokens);

        tokens = parser.burstToken("-c", true);
        Assert.assertEquals(Arrays.asList("-c", "3"), tokens);
    }

    @Test(expected = ParseException.class)
    public void testBurstTokenInvalidOptionWithArgument()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("-invalid-option", true);
    }

    @Test
    public void testBurstTokenNoOption()
    {
        PosixParser parser = new PosixParser();
        List<String> tokens = parser.burstToken("x", false);
        Assert.assertEquals(Arrays.asList("x"), tokens);

        tokens = parser.burstToken("x", true);
        Assert.assertEquals(Arrays.asList("--", "x"), tokens);
    }
}