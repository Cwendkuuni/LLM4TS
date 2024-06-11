import org.apache.commons.cli.Options;
import org.apache.commons.cli.Parser;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 1, "Option B with an integer argument");
        options.addOption("c", 2, "Option C with two integer arguments");
        options.addOption("d", 3, "Option D with three integer arguments");

        String[] args = {"--a", "--b", "3", "--c", "1", "2", "--d", "1", "2", "3"};
        Parser parser = new PosixParser();
        String[] tokens = parser.flatten(options, args, false);

        List expectedTokens = Arrays.asList(new String[]{"--a", "--b", "3", "--c", "1", "2", "--d", "1", "2", "3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "3", "--c=1 --c=2", "--d=1 --d=2 --d=3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "3", "--c", "1", "--c", "2", "--d", "1", "--d", "2", "--d", "3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "3", "--c=1 --c=2", "--d=1", "--e"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "3", "--c", "1", "--c", "2", "--d", "1", "--e"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "3", "--c=1 --c=2", "--d=1", "--e", "--"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "3", "--c", "1", "--c", "2", "--d", "1", "--e", "--"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "3", "--c=1=abc --c=2=def"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "3", "--c", "1=abc", "--c", "2=def"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "arg1", "arg2", "arg3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "arg1", "arg2", "arg3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "arg1", "arg2", "arg3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "arg1", "arg2", "arg3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "arg1", "--", "arg3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "arg1", "--", "arg3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "arg1", "--c", "arg2"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "arg1", "--c", "arg2"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c", "arg2"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "arg2"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d=1"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d", "1"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d=1", "--e=2"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d", "1", "--e", "2"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d=1", "--e=2", "--f"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "arg2", "--d", "1", "--e", "2", "--f"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c=1", "--d=1 --d=2 --d=3", "--e=1 --e=2 --e=3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "1", "--d", "1", "--d", "2", "--d", "3", "--e", "1", "--e", "2", "--e", "3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c=1 --c=2 --c=3", "--d=1 --d=2 --d=3", "--e=1 --e=2 --e=3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--", "--", "--c", "1", "--c", "2", "--c", "3", "--d", "1", "--d", "2", "--d", "3", "--e", "1", "--e", "2", "--e", "3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c=1 --c=2", "--d=1 --d=2 --d=3", "--e=1 --e=2 --e=3"};
        tokens = parser.flatten(options, args, true);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--c", "1", "--c", "2", "--d", "1", "--d", "2", "--d", "3", "--e", "1", "--e", "2", "--e", "3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);

        args = new String[]{"--a", "--b", "--", "--", "--c=1 --c=2", "--d=1 --d=2 --d=3", "--e=1 --e=2 --e=3"};
        tokens = parser.flatten(options, args, false);
        expectedTokens = Arrays.asList(new String[]{"--a", "--b", "--c=1--c=2", "--d=1--d=2--d=3", "--e=1--e=2--e=3"});
        Assert.assertArrayEquals(expectedTokens.toArray(new String[0]), tokens);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenThrowsExceptionWhenInvalidOptionSpecified() {
        Options options = new Options();
        options.addOption("a", true, "Option A");

        Parser parser = new PosixParser();
        String[] args = {"--x"};
        parser.flatten(options, args, false);
    }
}