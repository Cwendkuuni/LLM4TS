import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;
import org.apache.commons.cli.PosixParser;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;

public class PosixParserTest {

    @Test
    public void testFlatten() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", 4, "Option D with arguments");

        String[] args = {"--a", "--b", "1", "--c", "arg1", "--d", "arg1", "arg2"};
        PosixParser parser = new PosixParser();
        String[] parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b", "--c", "arg1", "--d", "arg1", "arg2"}, parsedArgs);

        args = new String[]{"--a", "--b=1", "--c", "arg1", "--d=arg1 arg2", "-d", "arg3", "arg4"};
        parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c", "--d", "arg1", "arg2", "--d", "arg3", "arg4"}, parsedArgs);

        args = new String[]{"--a", "--b=1", "--c", "--d=arg1 arg2", "-d", "arg3", "--d", "arg4"};
        parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c", "--d", "arg1", "arg2", "--d", "arg3", "arg4"}, parsedArgs);

        args = new String[]{"--a", "--b=1", "--c", "--d=arg1 arg2", "-d", "arg3", "--d", "arg4", "--"};
        parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c", "--d", "arg1", "arg2", "--d", "arg3", "arg4"}, parsedArgs);

        args = new String[]{"--a", "--b=1", "--c", "--d=arg1 arg2", "-d", "arg3", "--d", "arg4", "-", "arg5"};
        parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c", "--d", "arg1", "arg2", "--d", "arg3", "arg4", "--", "arg5"}, parsedArgs);

        args = new String[]{"--a", "--b=1", "--c", "--d=arg1 arg2", "-d", "arg3", "--d", "arg4", "--", "arg5", "--", "arg6"};
        parsedArgs = parser.parse(options, args, false);

        Assert.assertArrayEquals(new String[]{"--a", "--b=1", "--c", "--d", "arg1", "arg2", "--d", "arg3", "arg4", "--", "arg5", "--", "arg6"}, parsedArgs);

        args = new String[]{"--b=1", "--c", "--d=arg1 arg2"};
        parsedArgs = parser.parse(options, args, true);

        Assert.assertArrayEquals(new String[]{"--b=1", "--c", "--d=arg1 arg2"}, parsedArgs);
    }

    @Test(expected = ParseException.class)
    public void testFlattenWithInvalidOption() throws ParseException {
        Options options = new Options();
        options.addOption("a", true, "Option A");
        options.addOption("b", 2, "Option B");
        options.addOption("c", 3, "Option C with argument");
        options.addOption("d", 4, "Option D with arguments");

        String[] args = {"--a", "--x"};
        PosixParser parser = new PosixParser();
        parser.parse(options, args, false);
    }
}