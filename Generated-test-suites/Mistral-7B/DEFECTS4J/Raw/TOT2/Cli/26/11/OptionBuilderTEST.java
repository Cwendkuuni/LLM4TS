package org.apache.commons.cli.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionBuilder;

import org.junit.Test;

/**
 * Test cases for OptionBuilder.
 */
public class OptionBuilderTest
{
	/**
	 * Test that OptionBuilder.withLongOpt() sets the long option value.
	 */
	@Test
	public void testWithLongOpt()
	{
		OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption");
		assertSame("myOption", optionBuilder.longopt);
	}

	/**
	 * Test that OptionBuilder.hasArg() sets the number of arguments to 1.
	 */
	@Test
	public void testHasArg()
	{
		OptionBuilder optionBuilder = OptionBuilder.hasArg();
		assertEquals(Option.UNINITIALIZED + 1, optionBuilder.numberOfArgs);
	}

	/**
	 * Test that OptionBuilder.isRequired() sets required to true.
	 */
	@Test
	public void testIsRequired()
	{
		OptionBuilder optionBuilder = OptionBuilder.isRequired();
		assertTrue(optionBuilder.required);
	}

	/**
	 * Test that OptionBuilder.withValueSeparator() sets the value separator.
	 */
	@Test
	public void testWithValueSeparator()
	{
		OptionBuilder optionBuilder = OptionBuilder.withValueSeparator(':');
		assertEquals(':', optionBuilder.valuesep);
	}

	/**
	 * Test that OptionBuilder.create() creates an Option with the correct
	 * properties.
	 */
	@Test
	public void testCreate()
	{
		OptionBuilder optionBuilder = OptionBuilder.withLongOpt("myOption")
				.hasArg()
				.isRequired().withValueSeparator(':');
		Option option = optionBuilder.create('M');
		assertEquals("myOption", option.getLongOpt());
		assertEquals("myOption", option.getOpt());
		assertEquals(Option.UNINITIALIZED + 1, option.getArgs().length);
		assertTrue(option.isRequired());
		assertEquals(':', option.getValueSeparator());
	}

	/**
	 * Test that OptionBuilder.create() with no arguments set returns an
	 * Option with default values.
	 */
	@Test
	public void testCreateDefault()
	{
		OptionBuilder optionBuilder = new OptionBuilder();
		Option option = optionBuilder.create('N');
		assertNull(option.getLongOpt());
		assertEquals("arg", option.getArgName());
		assertEquals(Option.UNINITIALIZED, option.getArgs().length);
		assertFalse(option.isRequired());
		assertNull(option.getValueSeparator());
	}

	/**
	 * Test that OptionBuilder.hasArgs() sets the number of arguments to
	 * unlimited.
	 */
	@Test
	public void testHasArgs()
	{
		OptionBuilder optionBuilder = OptionBuilder.hasArgs();
		assertEquals(Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
	}

	/**
	 * Test that OptionBuilder.hasOptionalArg() sets the number of arguments to 1 and optionalArg to true.
	 */
	@Test
	public void testHasOptionalArg()
	{
		OptionBuilder optionBuilder = OptionBuilder.hasOptionalArg();
		assertEquals(Option.UNINITIALIZED + 1, optionBuilder.numberOfArgs);
		assertTrue(optionBuilder.optionalArg);
	}

	/**
	 * Test that OptionBuilder.hasOptionalArgs() sets the number of arguments to unlimited and optionalArg to true.
	 */
	@Test
	public void testHasOptionalArgs()
	{
		OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs();
		assertEquals(Option.UNINITIALIZED + Option.UNLIMITED_VALUES, optionBuilder.numberOfArgs);
		assertTrue(optionBuilder.optionalArg);
	}

	/**
	 * Test that OptionBuilder.hasOptionalArgs(numArgs) sets the number of arguments to the specified number and optionalArg to true.
	 */
	@Test
	public void testHasOptionalArgsNumArgs()
	{
		OptionBuilder optionBuilder = OptionBuilder.hasOptionalArgs(3);
		assertEquals(Option.UNINITIALIZED + 3, optionBuilder.numberOfArgs);
		assertTrue(optionBuilder.optionalArg);
	}

	/**
	 * Test that OptionBuilder.withType() sets the type of the argument.
	 */
	@Test
	public void testWithType()
	{
		OptionBuilder optionBuilder = OptionBuilder.withType(String.class);
		assertEquals(String.class, optionBuilder.type);
	}

	/**
	 * Test that OptionBuilder.withDescription() sets the description of the option.
	 */
	@Test
	public void testWithDescription()
	{
		OptionBuilder optionBuilder = OptionBuilder.withDescription("My option description");
		assertEquals("My option description", optionBuilder.description);
	}
}