import org.apache.commons.cli.*;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

public class PosixParserTest {

    @Test
    public void testFlattenNormalCase() {
        String[] args = {"--foo", "-ab", "-c", "d", "--ef=g", "-h"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("ab", true, "Ab option");
        options.addOption("c", true, "C option");
        options.addOption("e", true, "E option");
        options.addOption("ef", true, "Ef option");
        options.addOption("h", true, "H option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--foo",
                "-a",
                "-b",
                "-c",
                "d",
                "--ef",
                "g",
                "-h"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        String[] args = {"--foo", "-ab", "-c", "d", "--ef=g", "-h", "--"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("ab", true, "Ab option");
        options.addOption("c", true, "C option");
        options.addOption("e", true, "E option");
        options.addOption("ef", true, "Ef option");
        options.addOption("h", true, "H option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, true);

        List<String> expected = Arrays.asList(
                "--foo",
                "--",
                "d",
                "--ef=g",
                "--",
                "-h"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        String[] args = {"--foo", "-ab", "-xyz", "d", "--ef=g", "-h"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");
        options.addOption("ab", true, "Ab option");
        options.addOption("e", true, "E option");
        options.addOption("ef", true, "Ef option");
        options.addOption("h", true, "H option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--foo",
                "-a",
                "-b",
                "--xyz", // Invalid option
                "d",
                "--ef",
                "g",
                "-h"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test
    public void testFlattenWithLongOptionValue() {
        String[] args = {"--foo=verylongoptionvalue", "-ab", "-c", "d"};
        Options options = new Options();
        options.addOption("foo", true, "Foo option");

        PosixParser parser = new PosixParser();
        String[] result = parser.flatten(options, args, false);

        List<String> expected = Arrays.asList(
                "--foo=verylongoptionvalue"
        );

        Assert.assertArrayEquals(expected.toArray(new String[expected.size()]), result);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFlattenWithNoOptions() {
        String[] args = {"--", "d", "--ef=g", "--"};
        Options options = new Options();

        PosixParser parser = new PosixParser();
        parser.flatten(options, args, false);

        // The test should fail with an IllegalArgumentException
    }
}