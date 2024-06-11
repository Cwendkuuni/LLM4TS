package org.apache.commons.cli;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.apache.commons.cli.Option;

public class OptionBuilderTest {

    private Option option;

    @Before
    public void setup() {
        option = null;
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("myLongOpt");
        option = OptionBuilder.create();

        Assert.assertEquals("myLongOpt", option.getLongOpt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        option = OptionBuilder.create();

        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(false, option.hasOptionalArg());
    }

    @Test
    public void testHasArgWithTrue() {
        OptionBuilder.hasArg(true);
        option = OptionBuilder.create();

        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(false, option.hasOptionalArg());
    }

    @Test
    public void testHasArgWithFalse() {
        OptionBuilder.hasArg(false);
        option = OptionBuilder.create();

        Assert.assertEquals(-1, option.getArgs());
        Assert.assertEquals(false, option.hasOptionalArg());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("myArgName");
        option = OptionBuilder.create();

        Assert.assertEquals("myArgName", option.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        option = OptionBuilder.create();

        Assert.assertEquals(true, option.isRequired());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("myDescription");
        option = OptionBuilder.create();

        Assert.assertEquals("myDescription", option.getDescription());
    }

    @Test
    public void testWithOptionalArgAndNoNumberOfArgs() {
        OptionBuilder.hasOptionalArg();
        option = OptionBuilder.create();

        Assert.assertEquals(1, option.getArgs());
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithOptionalArgAndNumberOfArgsSet() {
        OptionBuilder.hasOptionalArgs(3);
        option = OptionBuilder.create();

        Assert.assertEquals(3, option.getArgs());
        Assert.assertEquals(true, option.hasOptionalArg());
    }

    @Test
    public void testWithType() {
        OptionBuilder.withType(Integer.class);
        option = OptionBuilder.create();

        Assert.assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        option = OptionBuilder.create();

        Assert.assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        option = OptionBuilder.create();

        Assert.assertEquals('=', option.getValueSeparator());
    }

    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        option = OptionBuilder.create();

        Assert.assertEquals(Option.UNLIMITED_VALUES, option.getArgs());
    }

    @Test
    public void testHasArgsWithNumberOfArgsSet() {
        OptionBuilder.hasArgs(4);
        option = OptionBuilder.create();

        Assert.assertEquals(4, option.getArgs());
    }

    @Test
    public void testFullConstructor() {
        OptionBuilder.hasArgs(2);
        OptionBuilder.hasOptionalArg();
        OptionBuilder.withArgName("myArgName");
        OptionBuilder.withDescription("myDescription");
        OptionBuilder.withLongOpt("myLongOpt");
        OptionBuilder.isRequired();
        OptionBuilder.withType(String.class);
        OptionBuilder.withValueSeparator(':');
        option = OptionBuilder.create("a");

        Assert.assertEquals("a", option.getOpt());
        Assert.assertEquals("myLongOpt", option.getLongOpt());
        Assert.assertEquals("myDescription", option.getDescription());
        Assert.assertEquals("myArgName", option.getArgName());
        Assert.assertEquals(String.class, option.getType());
        Assert.assertEquals(true, option.hasOptionalArg());
        Assert.assertEquals(':', option.getValueSeparator());
        Assert.assertEquals(2, option.getArgs());
        Assert.assertEquals(true, option.isRequired());
    }
}