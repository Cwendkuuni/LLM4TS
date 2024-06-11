package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testFlattenStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b", "value2", "value3"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "-b", "value2", "value3"}, result);
    }

    @Test
    public void testFlattenNotStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b", "value2", "value3"};

        String[] result = parser.flatten(options, arguments, false);

        Assert.assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "value3"}, result);
    }

    @Test
    public void testFlattenNoOptions() {
        Options options = new Options();

        PosixParser parser = new PosixParser();

        String[] arguments = {"-a", "value1", "-b", "value2", "value3"};

        String[] result = parser.flatten(options, arguments, true);

        Assert.assertArrayEquals(new String[]{"-a", "value1", "-b", "value2", "value3"}, result);
    }
}