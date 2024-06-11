package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class OptionBuilderTest {

    @Test
    public void testOptionBuilderReset() {
        OptionBuilder.reset();
        OptionBuilder builder = OptionBuilder.withLongOpt("longopt");
        assertEquals("longopt", builder.longopt);
        OptionBuilder.reset();
        assertNull(OptionBuilder.longopt);
    }

    @Test
    public void testOptionBuilderWithLongOpt() {
        OptionBuilder builder = OptionBuilder.instance();
        assertNull(builder.longopt);
        OptionBuilder withLongOpt = builder.withLongOpt("longopt");
        assertEquals("longopt", withLongOpt.longopt);
    }

    @Test
    public void testOptionBuilderHasArg() {
        OptionBuilder builder = OptionBuilder.instance();
        assertFalse(builder.required);
        OptionBuilder hasArg = builder.hasArg();
        assertEquals(1, builder.numberOfArgs);
        assertTrue(hasArg.required);
    }

    @Test
    public void testOptionBuilderHasOptionalArg() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasOptionalArg = builder.hasOptionalArg();
        assertEquals(1, builder.numberOfArgs);
        assertTrue(hasOptionalArg.optionalArg);
    }

    @Test
    public void testOptionBuilderHasArgs() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasArgs = builder.hasArgs();
        assertEquals(Option.UNLIMITED_VALUES, hasArgs.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasArgsInt() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasArgs = builder.hasArgs(2);
        assertEquals(2, hasArgs.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithArgName() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withArgName = builder.withArgName("argName");
        assertEquals("argName", withArgName.argName);
    }

    @Test
    public void testOptionBuilderIsRequired() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder isRequired = builder.isRequired();
        assertTrue(isRequired.required);
    }

    @Test
    public void testOptionBuilderWithValueSeparator() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withValueSeparator = builder.withValueSeparator(':');
        assertEquals(':', withValueSeparator.valuesep);
    }

    @Test
    public void testOptionBuilderWithValueSeparatorEmpty() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withValueSeparator = builder.withValueSeparator();
        assertEquals('=', withValueSeparator.valuesep);
    }

    @Test
    public void testOptionBuilderIsRequiredFalse() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder isRequiredFalse = builder.isRequired(false);
        assertFalse(isRequiredFalse.required);
    }

    @Test
    public void testOptionBuilderHasArgsUnlimitedOptional() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgs = builder.hasOptionalArgs();
        assertTrue(hasOptionalArgs.optionalArg);
        assertEquals(Option.UNLIMITED_VALUES, hasOptionalArgs.numberOfArgs);
    }

    @Test
    public void testOptionBuilderHasOptionalArgsInt() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasOptionalArgs = builder.hasOptionalArgs(2);
        assertTrue(hasOptionalArgs.optionalArg);
        assertEquals(2, hasOptionalArgs.numberOfArgs);
    }

    @Test
    public void testOptionBuilderWithType() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withType = builder.withType(Integer.class);
        assertEquals(Integer.class, withType.type);
    }

    @Test
    public void testOptionBuilderWithDescription() {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withDescription = builder.withDescription("description");
        assertEquals("description", withDescription.description);
    }

    @Test
    public void testOptionBuilderCreate() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        Option option = builder.create('option');
        assertNotNull(option);
        assertEquals("option", option.getLongOpt());
        assertNull(option.getArgName());
        assertFalse(option.isRequired());
        assertFalse(option.hasOptionalArg());
        assertEquals(Option.UNINITIALIZED, option.getArgs());
        assertNull(option.getType());
        assertEquals(',', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderCreateWithArgName() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withArgName = builder.withArgName("argName");
        Option option = builder.create('option');
        assertEquals("argName", option.getArgName());
    }

    @Test
    public void testOptionBuilderCreateRequired() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder isRequired = builder.isRequired();
        Option option = isRequired.create('option');
        assertTrue(option.isRequired());
    }

    @Test
    public void testOptionBuilderCreateOptionalArg() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder hasOptionalArg = builder.hasOptionalArg();
        Option option = hasOptionalArg.create('option');
        assertTrue(option.hasOptionalArg());
    }

    @Test
    public void testOptionBuilderCreateValueSeparator() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withValueSeparator = builder.withValueSeparator(':');
        Option option = withValueSeparator.create('option');
        assertEquals(':', option.getValueSeparator());
    }

    @Test
    public void testOptionBuilderCreateWithType() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withType = builder.withType(Integer.class);
        Option option = withType.create('option');
        assertEquals(Integer.class, option.getType());
    }

    @Test
    public void testOptionBuilderCreateWithDescription() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        OptionBuilder withDescription = builder.withDescription("description");
        Option option = withDescription.create('option');
        assertEquals("description", option.getDescription());
    }

    @Test(expected = IllegalArgumentException.class)
    public void testOptionBuilderCreateWithoutLongOpt() throws IllegalArgumentException {
        OptionBuilder builder = OptionBuilder.instance();
        Option option = builder.create('option');
        throw new AssertionError("IllegalArgumentException not thrown");
    }
}