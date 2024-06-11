import org.junit.Test;
import org.junit.Assert;
import org.apache.commons.cli.*;

public class GnuParserTest {

    @Test
    public void testFlatten() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property option");

        String[] arguments = {"--foo", "--", "-Dproperty=value", "arg1", "arg2"};

        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertEquals(7, flattened.length);
        Assert.assertEquals("--foo", flattened[0]);
        Assert.assertEquals("--", flattened[1]);
        Assert.assertEquals("-D", flattened[2]);
        Assert.assertEquals("property=value", flattened[3]);
        Assert.assertEquals("arg1", flattened[4]);
        Assert.assertEquals("arg2", flattened[5]);
    }
    
    @Test
    public void testFlattenWithNoStop() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property option");

        String[] arguments = {"--foo", "--", "-Dproperty=value", "arg1", "arg2"};

        String[] flattened = parser.flatten(options, arguments, false);

        Assert.assertEquals(7, flattened.length);
        Assert.assertEquals("--foo", flattened[0]);
        Assert.assertEquals("--", flattened[1]);
        Assert.assertEquals("-D", flattened[2]);
        Assert.assertEquals("property=value", flattened[3]);
        Assert.assertEquals("arg1", flattened[4]);
        Assert.assertEquals("arg2", flattened[5]);
    }

    @Test
    public void testFlattenWithNoOptions() {
        GnuParser parser = new GnuParser();
        Options options = new Options();

        String[] arguments = {"--foo", "--", "-Dproperty=value", "arg1", "arg2"};

        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertEquals(5, flattened.length);
        Assert.assertEquals("--foo", flattened[0]);
        Assert.assertEquals("--", flattened[1]);
        Assert.assertEquals("-Dproperty=value", flattened[2]);
        Assert.assertEquals("arg1", flattened[3]);
        Assert.assertEquals("arg2", flattened[4]);
    }

    @Test
    public void testFlattenWithNoArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property option");

        String[] arguments = {};

        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertEquals(0, flattened.length);
    }

    @Test
    public void testFlattenWithNullArguments() {
        GnuParser parser = new GnuParser();
        Options options = new Options();
        options.addOption("D", true, "Property option");

        String[] arguments = null;

        String[] flattened = parser.flatten(options, arguments, true);

        Assert.assertEquals(0, flattened.length);
    }

}