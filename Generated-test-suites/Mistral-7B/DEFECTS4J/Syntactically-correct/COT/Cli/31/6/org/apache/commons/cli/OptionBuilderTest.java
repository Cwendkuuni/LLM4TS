import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;
import org.junit.Test;

public class OptionBuilderTest {

    

    @Test
    public void testWithLongOpt() {
        OptionBuilder instance = OptionBuilder.withLongOpt("longOpt");
        String longOpt = instance.getLongOpt();
        assertEquals("longOpt", longOpt);
    }

    @Test(expected = IllegalStateException.class)
    public void testWithLongOpt_noReset() {
        OptionBuilder instance = OptionBuilder.withLongOpt("longOpt");
        OptionBuilder.reset(); // not called
        OptionBuilder.withLongOpt("newLongOpt");
    }

    @Test
    public void testHasArg() {
        OptionBuilder instance = OptionBuilder.hasArg();
        int numberOfArgs = instance.getNumberOfArgs();
        assertEquals(1, numberOfArgs);
    }

    @Test
    public void testHasArg_defaultArgName() {
        OptionBuilder instance = OptionBuilder.hasArg();
        String argName = instance.getArgName();
        assertEquals("arg", argName);
    }

    @Test
    public void testHasArg_customArgName() {
        OptionBuilder instance = OptionBuilder.hasArg().withArgName("newArgName");
        String argName = instance.getArgName();
        assertEquals("newArgName", argName);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder instance = OptionBuilder.isRequired();
        boolean required = instance.isRequired();
        assertTrue(required);
    }

    @Test
    public void testIsRequired_notRequired() {
        OptionBuilder instance = OptionBuilder.reset();
        boolean required = instance.isRequired();
        assertFalse(required);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder instance = OptionBuilder.withValueSeparator(':');
        char valueSeparator = instance.getValueSeparator();
        assertEquals(':', valueSeparator);
    }

    @Test
    public void testWithValueSeparator_noSeparator() {
        OptionBuilder instance = OptionBuilder.reset();
        char valueSeparator = instance.getValueSeparator();
        assertEquals(0, valueSeparator);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder instance = OptionBuilder.hasArgs();
        int numberOfArgs = instance.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasArgs_customNumberOfArgs() {
        OptionBuilder instance = OptionBuilder.hasArgs(2);
        int numberOfArgs = instance.getNumberOfArgs();
        assertEquals(2, numberOfArgs);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder instance = OptionBuilder.hasOptionalArg();
        boolean optionalArg = instance.isOptionalArg();
        assertTrue(optionalArg);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder instance = OptionBuilder.hasOptionalArgs();
        int numberOfArgs = instance.getNumberOfArgs();
        assertEquals(Option.UNLIMITED_VALUES, numberOfArgs);
    }

    @Test
    public void testHasOptionalArgs_customNumberOfArgs() {
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(3);
        int numberOfArgs = instance.getNumberOfArgs();
        assertEquals(3, numberOfArgs);
    }

    @Test
    public void testWithType() {
        OptionBuilder instance = OptionBuilder.withType(Integer.class);
        Class<?> type = instance.getType();
        assertEquals(Integer.class, type);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder instance = OptionBuilder.withDescription("option description");
        String description = instance.getDescription();
        assertEquals("option description", description);
    }

    @Test
    public void testCreate() throws IllegalArgumentException {
        OptionBuilder instance = OptionBuilder.withLongOpt("option")
                .withDescription("option description")
                .hasArg()
                .withValueSeparator(':')
                .isRequired()
                .withType(Integer.class);

        Option option = instance.create('o');
        assertEquals("option", option.getLongOpt());
        assertEquals("option description", option.getDescription());
        assertEquals(1, option.getArgPosition());
        assertEquals(':', option.getValueSeparator());
        assertTrue(option.isRequired());
        assertEquals(Integer.class, option.getType());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_longOptIsNull() {
        OptionBuilder instance = OptionBuilder.reset();
        instance.create(null);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_longOptIsEmpty() {
        OptionBuilder instance = OptionBuilder.reset();
        instance.create("");
    }

    @Test
    public void testCreate_noDescription() {
        OptionBuilder instance = OptionBuilder.reset();
        Option option = instance.create('o');
        assertNull(option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_hasOptionalArg_noArg() {
        OptionBuilder instance = OptionBuilder.hasOptionalArg();
        instance.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_hasOptionalArg_noValue() {
        OptionBuilder instance = OptionBuilder.hasOptionalArg().withArgName("arg");
        instance.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_hasOptionalArgs_noValues() {
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(2);
        instance.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_hasOptionalArgs_tooManyValues() {
        OptionBuilder instance = OptionBuilder.hasOptionalArgs(2);
        instance.create('o', "val1", "val2", "val3");
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_requiredArg_noArg() {
        OptionBuilder instance = OptionBuilder.isRequired().hasArg();
        instance.create('o');
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreate_invalidValueSeparator() {
        OptionBuilder instance = OptionBuilder.withValueSeparator('.');
        instance.create('o');
    }

    
}