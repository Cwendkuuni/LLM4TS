package org.apache.commons.cli;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.Before;
import org.junit.Test;

public class OptionsTest {
	
	private Options options;
	
	@Before
	public void setUp() {
		options = new Options();
	}

	@Test
	public void testAddOptionGroup() {
		OptionGroup group = new OptionGroup();
		Option option1 = new Option("a", "first option");
		Option option2 = new Option("b", "second option");
		group.addOption(option1);
		group.addOption(option2);
		
		options.addOptionGroup(group);
		
		assertEquals(group, options.getOptionGroup(option1));
		assertEquals(group, options.getOptionGroup(option2));
		assertTrue(options.getRequiredOptions().contains(group));
	}
	
	@Test
	public void testAddOptionWithShortNameAndDescription() {
		String opt = "a";
		String description = "option with short name and no argument";
		
		options.addOption(opt, description);
		
		assertTrue(options.hasOption(opt));
		Option addedOption = options.getOption(opt);
		assertEquals(opt, addedOption.getOpt());
		assertNull(addedOption.getLongOpt());
		assertFalse(addedOption.hasArg());
		assertEquals(description, addedOption.getDescription());
	}
	
	@Test
	public void testAddOptionWithShortNameAndHasArgAndDescription() {
		String opt = "a";
		boolean hasArg = true;
		String description = "option with short name and has argument";
		
		options.addOption(opt, hasArg, description);
		
		assertTrue(options.hasOption(opt));
		Option addedOption = options.getOption(opt);
		assertEquals(opt, addedOption.getOpt());
		assertNull(addedOption.getLongOpt());
		assertTrue(addedOption.hasArg());
		assertEquals(description, addedOption.getDescription());
	}
	
	@Test
	public void testAddOptionWithShortNameAndLongNameAndHasArgAndDescription() {
		String opt = "a";
		String longOpt = "option";
		boolean hasArg = true;
		String description = "option with short and long names and has argument";
		
		options.addOption(opt, longOpt, hasArg, description);
		
		assertTrue(options.hasOption(opt));
		assertTrue(options.hasLongOption(longOpt));
		Option addedOption = options.getOption(opt);
		assertEquals(opt, addedOption.getOpt());
		assertEquals(longOpt, addedOption.getLongOpt());
		assertTrue(addedOption.hasArg());
		assertEquals(description, addedOption.getDescription());
	}
	
	@Test
	public void testAddOption() {
		Option option = new Option("a", "option");
		
		options.addOption(option);
		
		assertTrue(options.hasOption("a"));
		assertTrue(options.hasLongOption("option"));
		Option addedOption = options.getOption("a");
		assertEquals("a", addedOption.getOpt());
		assertEquals("option", addedOption.getLongOpt());
		assertFalse(addedOption.hasArg());
		assertNull(addedOption.getDescription());
	}
	
	@Test
	public void testGetOptions() {
		Option option1 = new Option("a", "first option");
		Option option2 = new Option("b", "second option");
		options.addOption(option1);
		options.addOption(option2);
		
		assertEquals(2, options.getOptions().size());
		assertTrue(options.getOptions().contains(option1));
		assertTrue(options.getOptions().contains(option2));
	}
	
	@Test
	public void testGetMatchingOptions() {
		Option option1 = new Option("f", "first");
		Option option2 = new Option("fo", "second");
		Option option3 = new Option("ff", "third");
		options.addOption(option1);
		options.addOption(option2);
		options.addOption(option3);
		
		assertEquals(2, options.getMatchingOptions("f").size());
		assertTrue(options.getMatchingOptions("f").contains("first"));
		assertTrue(options.getMatchingOptions("f").contains("third"));
	}
	
	@Test
	public void testHasOption() {
		String opt = "a";
		assertFalse(options.hasOption(opt));
		
		Option option = new Option(opt, "option");
		options.addOption(option);
		
		assertTrue(options.hasOption(opt));
	}
	
	@Test
	public void testHasLongOption() {
		String longOpt = "option";
		assertFalse(options.hasLongOption(longOpt));
		
		Option option = new Option("a", longOpt);
		options.addOption(option);
		
		assertTrue(options.hasLongOption(longOpt));
	}
	
	@Test
	public void testHasShortOption() {
		String opt = "a";
		assertFalse(options.hasShortOption(opt));
		
		Option option = new Option(opt, "option");
		options.addOption(option);
		
		assertTrue(options.hasShortOption(opt));
	}
	
	@Test
	public void testToString() {
		Option option1 = new Option("a", "first option");
		Option option2 = new Option("b", "second option");
		options.addOption(option1);
		options.addOption(option2);
		
		String expectedString = "[ Options: [ short {a=first option, b=second option} ] [ long {}]";
		assertEquals(expectedString, options.toString());
	}
}