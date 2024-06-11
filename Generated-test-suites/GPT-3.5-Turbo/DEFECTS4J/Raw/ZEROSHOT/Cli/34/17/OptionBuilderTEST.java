import static org.junit.Assert.*;
import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testWithLongOpt() {
        OptionBuilder optionBuilder = OptionBuilder.withLongOpt("longopt");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgBoolean() {
        OptionBuilder optionBuilderTrue = OptionBuilder.hasArg(true);
        assertNotNull(optionBuilderTrue);

        OptionBuilder optionBuilderFalse = OptionBuilder.hasArg(false);
        assertNotNull(optionBuilderFalse);
    }

    @Test
    public void testWithArgName() {
        OptionBuilder optionBuilder = OptionBuilder.withArgName("argName");
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequired() {
        OptionBuilder optionBuilder = OptionBuilder.isRequired();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparatorChar() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithValueSeparator() {
        OptionBuilder optionBuilder = OptionBuilder.withValueSeparator();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testIsRequiredBoolean() {
        OptionBuilder optionBuilderTrue = OptionBuilder.isRequired(true);
        assertNotNull(optionBuilderTrue);

        OptionBuilder optionBuilderFalse = OptionBuilder.isRequired(false);
        assertNotNull(optionBuilderFalse);
    }

    @Test
    public void testHasArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasArgs(2);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArg() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgs() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
        assertNotNull(optionBuilder);
    }

    @Test
    public void testHasOptionalArgsInt() {
        OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(2);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithType() {
        OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
        assertNotNull(optionBuilder);
    }

    @Test
    public void testWithDescription() {
        OptionBuilder optionBuilder = OptionBuilder.withDescription("description");
        assertNotNull(optionBuilder);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateCharInvalid() {
        OptionBuilder.create(' ');
    }

    @Test
    public void testCreateCharValid() {
        Option option = OptionBuilder.create('c');
        assertNotNull(option);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testCreateWithoutLongOpt() {
        OptionBuilder.create();
    }

    @Test
    public void testCreateWithLongOpt() {
        OptionBuilder.withLongOpt("longopt").create();
    }

    @Test
    public void testCreateString() {
        Option option = OptionBuilder.create("opt");
        assertNotNull(option);
    }
}