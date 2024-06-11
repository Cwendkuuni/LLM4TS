import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("test");
        // assert the value of longopt is "test"
        assertEquals("test", OptionBuilder.getLongopt());
    }

    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // assert the value of numberOfArgs is 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testHasArgWithBoolean() {
        OptionBuilder.hasArg(true);
        // assert the value of numberOfArgs is 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());

        OptionBuilder.hasArg(false);
        // assert the value of numberOfArgs is -1
        assertEquals(Option.UNINITIALIZED, OptionBuilder.getNumberOfArgs());
    }

    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("argName");
        // assert the value of argName is "argName"
        assertEquals("argName", OptionBuilder.getArgName());
    }

    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // assert the value of required is true
        assertEquals(true, OptionBuilder.isRequired());
    }

    @Test
    public void testWithOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // assert the value of numberOfArgs is 1
        assertEquals(1, OptionBuilder.getNumberOfArgs());
        // assert the value of optionalArg is true
        assertEquals(true, OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // assert the value of numberOfArgs is -2
        assertEquals(Option.UNLIMITED_VALUES, OptionBuilder.getNumberOfArgs());
        // assert the value of optionalArg is true
        assertEquals(true, OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithOptionalArgsWithNumArgs() {
        OptionBuilder.hasOptionalArgs(3);
        // assert the value of numberOfArgs is 3
        assertEquals(3, OptionBuilder.getNumberOfArgs());
        // assert the value of optionalArg is true
        assertEquals(true, OptionBuilder.isOptionalArg());
    }

    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("description");
        // assert the value of description is "description"
        assertEquals("description", OptionBuilder.getDescription());
    }

    @Test
    public void testCreateWithCharOpt() {
        Option option = OptionBuilder.create('c');
        // assert the option's opt is "c"
        assertEquals("c", option.getOpt());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithStringOpt() {
        Option option = OptionBuilder.create("opt");
        // assert the option's opt is "opt"
        assertEquals("opt", option.getOpt());
    }
}