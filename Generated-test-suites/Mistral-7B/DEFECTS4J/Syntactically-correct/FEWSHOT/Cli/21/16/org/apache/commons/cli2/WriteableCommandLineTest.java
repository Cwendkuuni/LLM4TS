package org.apache.commons.cli2;

import static org.junit.Assert.*;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;

@RunWith(PowerMockRunner.class)
public class WriteableCommandLineTest {

 private WriteableCommandLine writeableCommandLine;
 private Option option;

 @SuppressWarnings("unchecked")
 @Before
 public void setUp() {
 writeableCommandLine = Mockito.mock(WriteableCommandLine.class);
 option = Mockito.mock(Option.class);
 }

 @Test
 public void testAddOption() {
 writeableCommandLine.addOption(option);
 PowerMockito.verifyStatic();
 PowerMockito.verifyStatic(WriteableCommandLine.class).addOption((WriteableCommandLine) writeableCommandLine, option);
 }

 @Test
 public void testAddValue() {
 writeableCommandLine.addValue(option, "value");
 PowerMockito.verifyStatic();
 PowerMockito.verifyStatic(WriteableCommandLine.class).addValue((WriteableCommandLine) writeableCommandLine, option, "value");
 }

 @Test
 public void testGetUndefaultedValuesReturnsEmptyListIfNoValuesFound() {
 List<Object> values = writeableCommandLine.getUndefaultedValues(option);
 assertEquals(Collections.emptyList(), values);
 }

 @Test
 public void testGetUndefaultedValuesReturnsValuesForOption() {
 PowerMockito.when(option.getValue()).thenReturn(new String[]{"value1", "value2"});
 List<Object> values = writeableCommandLine.getUndefaultedValues(option);
 assertEquals(Arrays.asList("value1", "value2"), values);
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchThrowsIllegalStateExceptionIfSwitchAlreadyAdded() {
 PowerMockito.when(option.getValue()).thenReturn(new boolean[]{true});
 writeableCommandLine.addSwitch(option, false);
 writeableCommandLine.addSwitch(option, true);
 }

 @Test
 public void testSetDefaultSwitchSetsDefaultSwitchForOption() {
 writeableCommandLine.setDefaultSwitch(option, true);
 PowerMockito.verifyStatic();
 PowerMockito.verifyStatic(WriteableCommandLine.class).setDefaultSwitch((WriteableCommandLine) writeableCommandLine, option, true);
 }

 @Test
 public void testAddPropertyReplacesExistingPropertyValue() {
 writeableCommandLine.addProperty(option, "property", "value1");
 writeableCommandLine.addProperty(option, "property", "value2");
 PowerMockito.verifyStatic();
 PowerMockito.verifyStatic(WriteableCommandLine.class).addProperty((WriteableCommandLine) writeableCommandLine, option, "property", "value2");
 }

 @Test
 public void testLooksLikeOptionReturnsTrueIfArgumentLooksLikeOption() {
 assertTrue(writeableCommandLine.looksLikeOption("--option"));
 }

 @Test
 public void testLooksLikeOptionReturnsFalseIfArgumentDoesNotLookLikeOption() {
 assertFalse(writeableCommandLine.looksLikeOption("value"));
 }

}