package org.apache.commons.cli;
import org.junit.Test;

public class OptionBuilderTest {

    // Test case for withLongOpt method
    @Test
    public void testWithLongOpt() {
        OptionBuilder.withLongOpt("testOption");
        // assert statement
    }

    // Test case for hasArg method
    @Test
    public void testHasArg() {
        OptionBuilder.hasArg();
        // assert statement
    }

    // Test case for hasArg(boolean hasArg) method
    @Test
    public void testHasArgWithParameter() {
        OptionBuilder.hasArg(true);
        // assert statement
    }

    // Test case for withArgName method
    @Test
    public void testWithArgName() {
        OptionBuilder.withArgName("testArg");
        // assert statement
    }

    // Test case for isRequired method
    @Test
    public void testIsRequired() {
        OptionBuilder.isRequired();
        // assert statement
    }

    // Test case for withValueSeparator(char sep) method
    @Test
    public void testWithValueSeparator() {
        OptionBuilder.withValueSeparator(':');
        // assert statement
    }

    // Test case for withValueSeparator() method
    @Test
    public void testWithValueSeparatorDefault() {
        OptionBuilder.withValueSeparator();
        // assert statement
    }

    // Test case for isRequired(boolean newRequired) method
    @Test
    public void testIsRequiredWithParameter() {
        OptionBuilder.isRequired(true);
        // assert statement
    }

    // Test case for hasArgs method
    @Test
    public void testHasArgs() {
        OptionBuilder.hasArgs();
        // assert statement
    }

    // Test case for hasArgs(int num) method
    @Test
    public void testHasArgsWithParameter() {
        OptionBuilder.hasArgs(3);
        // assert statement
    }

    // Test case for hasOptionalArg method
    @Test
    public void testHasOptionalArg() {
        OptionBuilder.hasOptionalArg();
        // assert statement
    }

    // Test case for hasOptionalArgs method
    @Test
    public void testHasOptionalArgs() {
        OptionBuilder.hasOptionalArgs();
        // assert statement
    }

    // Test case for hasOptionalArgs(int numArgs) method
    @Test
    public void testHasOptionalArgsWithParameter() {
        OptionBuilder.hasOptionalArgs(2);
        // assert statement
    }

    // Test case for withType method
    @Test
    public void testWithType() {
        OptionBuilder.withType(String.class);
        // assert statement
    }

    // Test case for withDescription method
    @Test
    public void testWithDescription() {
        OptionBuilder.withDescription("Test description");
        // assert statement
    }

    // Test case for create(char opt) method
    @Test
    public void testCreateWithChar() {
        OptionBuilder.create('t');
        // assert statement
    }

    // Test case for create() method
    @Test
    public void testCreate() {
        OptionBuilder.create();
        // assert statement
    }

    // Test case for create(String opt) method
    @Test
    public void testCreateWithString() {
        OptionBuilder.create("testOption");
        // assert statement
    }
}