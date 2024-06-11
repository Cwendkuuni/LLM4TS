package org.apache.commons.cli;
import static org.junit.Assert.*;

import java.util.Collection;

import org.apache.commons.cli.AlreadySelectedException;
import org.apache.commons.cli.Option;
import org.apache.commons.cli.OptionGroup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class OptionGroupTest {

	private OptionGroup optionGroup;

	@Before
	public void setUp() throws Exception {
		optionGroup = new OptionGroup();
	}

	@After
	public void tearDown() throws Exception {
		optionGroup = null;
	}

	@Test
	public void testAddOption() {
		Option option1 = new Option("a", "option 1");
		optionGroup.addOption(option1);
		Collection options = optionGroup.getOptions();
		assertTrue(options.contains(option1));
	}

	@Test
	public void testGetNames() {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		Collection names = optionGroup.getNames();
		assertTrue(names.contains("a"));
		assertTrue(names.contains("b"));
	}

	@Test
	public void testGetOptions() {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		Collection options = optionGroup.getOptions();
		assertTrue(options.contains(option1));
		assertTrue(options.contains(option2));
	}

	@Test
	public void testSetSelected() throws AlreadySelectedException {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		optionGroup.setSelected(option1);
		assertEquals(option1.getOpt(), optionGroup.getSelected());
	}

	@Test(expected = AlreadySelectedException.class)
	public void testAlreadySelectedException() throws AlreadySelectedException {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		optionGroup.setSelected(option1);
		optionGroup.setSelected(option2);
	}

	@Test
	public void testGetSelected() throws AlreadySelectedException {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		optionGroup.setSelected(option1);
		assertEquals(option1.getOpt(), optionGroup.getSelected());
	}

	@Test
	public void testSetRequired() {
		optionGroup.setRequired(true);
		assertTrue(optionGroup.isRequired());
	}

	@Test
	public void testToString() {
		Option option1 = new Option("a", "option 1");
		Option option2 = new Option("b", "option 2");
		optionGroup.addOption(option1);
		optionGroup.addOption(option2);
		String expectedToString = "[\"-a option 1\", \"-b option 2\"]";
		assertEquals(expectedToString, optionGroup.toString());
	}

}