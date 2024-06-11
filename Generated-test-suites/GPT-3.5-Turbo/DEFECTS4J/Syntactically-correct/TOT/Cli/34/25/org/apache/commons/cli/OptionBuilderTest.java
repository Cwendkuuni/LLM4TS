
import org.apache.commons.cli.Option;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class OptionBuilderTest {

    OptionBuilder optionBuilder;

    @Before
    public void setUp() {
        optionBuilder = new OptionBuilder();
    }

    @Test
    public void testWithLongOpt() {
        OptionBuilder option = optionBuilder.withLongOpt("testOption");
        Option expectedOption = new Option.Builder().longOpt("testOption").build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasArg() {
        OptionBuilder option = optionBuilder.hasArg();
        Option expectedOption = new Option.Builder().hasArg().build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasArgWithValue() {
        OptionBuilder option = optionBuilder.hasArg(true);
        Option expectedOption = new Option.Builder().hasArg(true).build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder option = optionBuilder.withArgName("testArg");
        Option expectedOption = new Option.Builder().argName("testArg").build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder option = optionBuilder.isRequired();
        Option expectedOption = new Option.Builder().required().build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder option = optionBuilder.withValueSeparator(':');
        Option expectedOption = new Option.Builder().valueSeparator(':').build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder option = optionBuilder.withValueSeparator();
        Option expectedOption = new Option.Builder().valueSeparator('=').build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testIsRequiredWithArg() {
        OptionBuilder option = optionBuilder.isRequired(true);
        Option expectedOption = new Option.Builder().required(true).build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder option = optionBuilder.hasArgs();
        Option expectedOption = new Option.Builder().hasArgs().build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasArgsWithValue() {
        OptionBuilder option = optionBuilder.hasArgs(2);
        Option expectedOption = new Option.Builder().hasArgs(2).build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder option = optionBuilder.hasOptionalArg();
        Option expectedOption = new Option.Builder().hasOptionalArg().build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder option = optionBuilder.hasOptionalArgs();
        Option expectedOption = new Option.Builder().hasOptionalArgs().build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testHasOptionalArgsWithValue() {
        OptionBuilder option = optionBuilder.hasOptionalArgs(3);
        Option expectedOption = new Option.Builder().hasOptionalArgs(3).build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithType() {
        OptionBuilder option = optionBuilder.withType(Integer.class);
        Option expectedOption = new Option.Builder().type(Integer.class).build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder option = optionBuilder.withDescription("testDescription");
        Option expectedOption = new Option.Builder().description("testDescription").build();
        assertEquals(expectedOption, option);
    }

    @Test
    public void testCreateWithChar() {
        Option option = optionBuilder.create('t');
        Option expectedOption = new Option.Builder().opt('t').build();
        assertEquals(expectedOption, option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        optionBuilder.create();
    }

    @Test
    public void testCreateWithString() {
        Option option = optionBuilder.create("testOption");
        Option expectedOption = new Option.Builder().opt("testOption").build();
        assertEquals(expectedOption, option);
    }
}
