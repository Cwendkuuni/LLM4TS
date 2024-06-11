package org.apache.commons.cli2;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

@RunWith(MockitoJUnitRunner.class)
public class WriteableCommandLineTest {

 WriteableCommandLine writeableCommandLine;
 Option option;
 Option optionWithDefaultValues;
 Option optionWithSwitch;
 Option optionWithProperty;

 @Mock
 CommandLine commandLine;

 @Before
 public void setUp() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 option = mock(Option.class);
 optionWithDefaultValues = mock(Option.class);
 optionWithSwitch = mock(Option.class);
 }

 @Test
 public void testAddOption() {
 when(commandLine.addOption(option)).thenReturn(writeableCommandLine);
 WriteableCommandLine result = writeableCommandLine.addOption(option);
 assertSame(writeableCommandLine, result);
 }

 @Test
 public void testAddValue() {
 when(commandLine.addOption(option)).thenReturn(writeableCommandLine);
 when(option.getValue()).thenReturn("value");
 WriteableCommandLine result = writeableCommandLine.addValue(option, "newValue");
 assertSame(writeableCommandLine, result);
 assertEquals(Arrays.asList("value", "newValue"), writeableCommandLine.getUndefaultedValues(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddValueIllegalStateException() {
 when(commandLine.addOption(option)).thenReturn(writeableCommandLine);
 when(option.getValue()).thenReturn("value");
 writeableCommandLine.addValue(option, "newValue");
 writeableCommandLine.addValue(option, "newValue2");
 }

 @Test
 public void testGetUndefaultedValues() {
 when(commandLine.getOptions()).thenReturn(Arrays.asList(optionWithDefaultValues));
 when(optionWithDefaultValues.getDefaultValues()).thenReturn(Arrays.asList("defaultValue1", "defaultValue2"));
 when(optionWithDefaultValues.getValue()).thenReturn("value");

 List result = writeableCommandLine.getUndefaultedValues(optionWithDefaultValues);
 assertEquals(Arrays.asList("value"), result);
 }

 @Test
 public void testSetDefaultValues() {
 List defaultValues = Arrays.asList("defaultValue1", "defaultValue2");
 when(commandLine.getOptionByName("optionName")).thenReturn(optionWithDefaultValues);
 WriteableCommandLine result = writeableCommandLine.setDefaultValues(optionWithDefaultValues, defaultValues);
 assertEquals(defaultValues, writeableCommandLine.getUndefaultedValues(optionWithDefaultValues));
 }

 @Test
 public void testAddSwitch() {
 when(commandLine.getOptionByName("optionName")).thenReturn(optionWithSwitch);
 when(optionWithSwitch.isSet()).thenReturn(false);
 writeableCommandLine.addSwitch(optionWithSwitch, true);
 assertTrue(writeableCommandLine.getOptionByName("optionName").isSet());
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchIllegalStateException() {
 when(commandLine.getOptionByName("optionName")).thenReturn(optionWithSwitch);
 when(optionWithSwitch.isSet()).thenReturn(true);
 writeableCommandLine.addSwitch(optionWithSwitch, true);
 }

 @Test
 public void testSetDefaultSwitch() {
 when(commandLine.getOptionByName("optionName")).thenReturn(optionWithSwitch);
 WriteableCommandLine result = writeableCommandLine.setDefaultSwitch(optionWithSwitch, true);
 assertTrue(writeableCommandLine.getOptionByName("optionName").isSet());
 }

 @Test
 public void testAddProperty() {
 when(commandLine.getOptionByName("optionName")).thenReturn(optionWithProperty);
 writeableCommandLine.addProperty(optionWithProperty, "propertyName", "propertyValue");
 assertEquals("propertyValue", writeableCommandLine.getOptionByName("optionName").getProperties().get("propertyName"));
 }

 @Test
 public void testAddPropertyToDefaultProperties() {
 writeableCommandLine.addProperty("propertyName", "propertyValue");
 assertEquals("propertyValue", writeableCommandLine.getProperties().get("propertyName"));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 assertFalse(writeableCommandLine.looksLikeOption("argument"));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 Option option = mock(Option.class);
 when(option.isSet()).thenReturn(true);
 when(writeableCommandLine.getOptionByName("optionName")).thenReturn(option);
 assertTrue(writeableCommandLine.looksLikeOption("optionName"));
 }

 @Test
 public void testGetCurrentOptionNull() {
 assertNull(writeableCommandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 Option option = mock(Option.class);
 WriteableCommandLine result = writeableCommandLine;
 writeableCommandLine.setCurrentOption(option);
 assertSame(option, writeableCommandLine.getCurrentOption());
 }

}