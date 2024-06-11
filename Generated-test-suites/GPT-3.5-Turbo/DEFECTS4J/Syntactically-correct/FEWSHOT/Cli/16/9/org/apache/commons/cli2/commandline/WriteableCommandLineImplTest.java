package org.apache.commons.cli2.commandline;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.cli2.Argument;
import org.apache.commons.cli2.Option;
import org.junit.Before;
import org.junit.Test;

public class WriteableCommandLineImplTest {
	
	private WriteableCommandLineImpl commandLine;
	private Option option1;
	private Option option2;
	private Option option3;
	private Option option4;
	private Argument argument;
	private List<String> arguments;
	
	@Before
	public void setUp() {
		option1 = OptionBuilder.withLongName("option1").create();
		option2 = OptionBuilder.withLongName("option2").create();
		option3 = OptionBuilder.withLongName("option3").create();
		option4 = OptionBuilder.withLongName("option4").create();
		argument = ArgumentBuilder.withName("argument").create();
		arguments = new ArrayList<>();
		arguments.add("arg1");
		arguments.add("arg2");
		
		commandLine = new WriteableCommandLineImpl(option1, arguments);
	}
	
	@Test
	public void testAddOption() {
		commandLine.addOption(option1);
		assertTrue(commandLine.getOptions().contains(option1));
		assertTrue(commandLine.getOptionTriggers().contains(option1.getPreferredName()));
	}
	
	@Test
	public void testAddValue() {
		commandLine.addValue(option1, "value1");
		assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, null));
	}
	
	@Test
	public void testAddSwitch() {
		commandLine.addSwitch(option1, true);
		assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, null));
	}
	
	@Test
	public void testHasOption() {
		commandLine.addOption(option1);
		assertTrue(commandLine.hasOption(option1));
		assertFalse(commandLine.hasOption(option2));
	}
	
	@Test
	public void testGetOption() {
		commandLine.addOption(option1);
		assertEquals(option1, commandLine.getOption("option1"));
		assertNull(commandLine.getOption("option2"));
	}
	
	@Test
	public void testGetValues() {
		commandLine.addValue(option1, "value1");
		assertEquals(Arrays.asList("value1"), commandLine.getValues(option1, null));
		assertNull(commandLine.getValues(option2, null));
	}
	
	@Test
	public void testGetUndefaultedValues() {
		commandLine.addValue(option1, "value1");
		assertEquals(Arrays.asList("value1"), commandLine.getUndefaultedValues(option1));
		assertEquals(Arrays.asList(), commandLine.getUndefaultedValues(option2));
	}
	
	@Test
	public void testGetSwitch() {
		commandLine.addSwitch(option1, true);
		assertEquals(Boolean.TRUE, commandLine.getSwitch(option1, null));
		assertEquals(null, commandLine.getSwitch(option2, null));
	}
	
	@Test
	public void testGetProperty() {
		commandLine.addProperty(option1, "property1", "value1");
		assertEquals("value1", commandLine.getProperty(option1, "property1", null));
		assertEquals(null, commandLine.getProperty(option2, "property1", null));
	}
	
	@Test
	public void testAddProperty() {
		commandLine.addProperty(option1, "property1", "value1");
		assertEquals("value1", commandLine.getProperty(option1, "property1", null));
	}
	
	@Test
	public void testGetProperties() {
		commandLine.addProperty(option1, "property1", "value1");
		assertEquals(Arrays.asList("property1"), commandLine.getProperties(option1));
		assertEquals(Arrays.asList(), commandLine.getProperties(option2));
	}
	
	@Test
	public void testLooksLikeOption() {
		assertTrue(commandLine.looksLikeOption("-option1"));
		assertFalse(commandLine.looksLikeOption("arg1"));
	}
	
	@Test
	public void testToString() {
		assertEquals("arg1 arg2", commandLine.toString());
	}
	
	@Test
	public void testGetOptions() {
		commandLine.addOption(option1);
		commandLine.addOption(option2);
		assertEquals(Arrays.asList(option1, option2), commandLine.getOptions());
	}
	
	@Test
	public void testGetOptionTriggers() {
		commandLine.addOption(option1);
		commandLine.addOption(option2);
		assertEquals(Arrays.asList("option1", "option2"), commandLine.getOptionTriggers());
	}
	
	@Test
	public void testSetDefaultValues() {
		commandLine.setDefaultValues(option1, Arrays.asList("default"));
		assertEquals(Arrays.asList("default"), commandLine.getValues(option1, null));
	}
	
	@Test
	public void testSetDefaultSwitch() {
		commandLine.setDefaultSwitch(option1, false);
		assertEquals(Boolean.FALSE, commandLine.getSwitch(option1, null));
	}
	
	@Test
	public void testGetNormalised() {
		assertEquals(Arrays.asList("arg1", "arg2"), commandLine.getNormalised());
	}
}