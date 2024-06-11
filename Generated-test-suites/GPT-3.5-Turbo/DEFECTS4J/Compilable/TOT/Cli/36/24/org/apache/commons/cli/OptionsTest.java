package org.apache.commons.cli;
import org.junit.Test;

import static org.junit.Assert.*;

public class OptionsTest {

	@Test
	public void testAddOptionGroup() {
		Options options = new Options();
		
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", false, "ball option");
		
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		
		options.addOptionGroup(optionGroup);
		
		assertTrue(options.hasOption("a"));
		assertTrue(options.hasOption("b"));
	}
	
	@Test
	public void testAddOption() {
		Options options = new Options();
		
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option - value required");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertTrue(options.hasOption("a"));
		assertTrue(options.hasOption("b"));
		assertTrue(options.getOption("a").getDescription().equals("apple option"));
		assertTrue(options.getOption("b").getDescription().equals("ball option - value required"));
		assertEquals(options.getMatchingOptions("a").get(0), "a");
		assertEquals(options.getMatchingOptions("a").get(1), "apple");
		assertEquals(options.getMatchingOptions("b").get(0), "b");
		assertEquals(options.getMatchingOptions("b").get(1), "ball");
	}
	
	@Test
	public void testGetRequiredOptions() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		option1.setRequired(true);
		option2.setRequired(true);
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertTrue(options.getRequiredOptions().size() == 2);
		assertTrue(options.getRequiredOptions().contains("a"));
		assertTrue(options.getRequiredOptions().contains("b"));
	}
	
	@Test
	public void testGetOption() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertNotNull(options.getOption("a"));
		assertNotNull(options.getOption("b"));
		assertNull(options.getOption("c"));
	}
	
	@Test
	public void testHasOption() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertTrue(options.hasOption("a"));
		assertTrue(options.hasOption("b"));
		assertFalse(options.hasOption("c"));
	}
	
	@Test
	public void testHasLongOption() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertTrue(options.hasLongOption("apple"));
		assertFalse(options.hasLongOption("a"));
		assertFalse(options.hasLongOption("c"));
	}
	
	@Test
	public void testHasShortOption() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertTrue(options.hasShortOption("a"));
		assertFalse(options.hasShortOption("apple"));
		assertFalse(options.hasShortOption("c"));
	}
	
	@Test
	public void testGetOptionGroup() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		OptionGroup optionGroup = new OptionGroup();
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		
		options.addOptionGroup(optionGroup);
		
		assertNotNull(options.getOptionGroup(option1));
		assertNotNull(options.getOptionGroup(option2));
	}
	
	@Test
	public void testToString() {
		Options options = new Options();
		Option option1 = new Option("a", "apple", false, "apple option");
		Option option2 = new Option("b", "ball", true, "ball option");
		
		options.addOption(option1);
		options.addOption(option2);
		
		assertEquals(options.toString(), "[ Options: [ short {a=apple, b=ball} ] [ long {apple=apple, ball=ball} ]");
	}
}