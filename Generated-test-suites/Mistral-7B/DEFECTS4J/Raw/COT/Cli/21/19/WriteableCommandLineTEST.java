import org.apache.commons.cli2.Option;
import org.junit.Assert;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class WriteableCommandLineTest {

 private WriteableCommandLine writeableCommandLine;

 @Test
 public void testAddOption() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);

 when(writeableCommandLine.addOption(option)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.addOption(option));
 }

 @Test
 public void testAddValue() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 Object value = "testValue";

 when(writeableCommandLine.addValue(option, value)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.addValue(option, value));
 }

 @Test
 public void testGetUndefaultedValuesEmpty() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);

 when(writeableCommandLine.getUndefaultedValues(option)).thenReturn(java.util.Collections.emptyList());

 Assert.assertEquals(java.util.Collections.emptyList(), writeableCommandLine.getUndefaultedValues(option));
 }

 @Test
 public void testGetUndefaultedValuesSingleValue() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 Object value = "testValue";

 when(writeableCommandLine.getUndefaultedValues(option)).thenReturn(java.util.Arrays.asList(value));

 Assert.assertEquals(java.util.Arrays.asList(value), writeableCommandLine.getUndefaultedValues(option));
 }

 @Test(expected = IllegalStateException.class)
 public void testAddSwitchDuplicate() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 boolean firstSwitch = true;

 when(writeableCommandLine.addSwitch(option, firstSwitch)).thenReturn(writeableCommandLine);

 writeableCommandLine.addSwitch(option, firstSwitch);
 }

 @Test
 public void testAddSwitch() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 boolean switchValue = true;

 when(writeableCommandLine.addSwitch(option, switchValue)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.addSwitch(option, switchValue));
 }

 @Test
 public void testSetDefaultSwitchFalse() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 Boolean defaultSwitch = false;

 when(writeableCommandLine.setDefaultSwitch(option, defaultSwitch)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.setDefaultSwitch(option, defaultSwitch));
 }

 @Test
 public void testSetDefaultSwitchTrue() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 Boolean defaultSwitch = true;

 when(writeableCommandLine.setDefaultSwitch(option, defaultSwitch)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.setDefaultSwitch(option, defaultSwitch));
 }

 @Test
 public void testAddPropertyEmpty() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 String property = "testProperty";
 String value = "testValue";

 when(writeableCommandLine.addProperty(option, property, value)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.addProperty(option, property, value));
 }

 @Test
 public void testAddPropertySingleProperty() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option option = mock(Option.class);
 String property = "testProperty";
 String value = "testValue";

 when(writeableCommandLine.addProperty(option, property, value)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.addProperty(option, property, value));
 }

 @Test
 public void testLooksLikeOptionTrue() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 String argument = "--test";

 when(writeableCommandLine.looksLikeOption(argument)).thenReturn(true);

 Assert.assertTrue(writeableCommandLine.looksLikeOption(argument));
 }

 @Test
 public void testLooksLikeOptionFalse() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 String argument = "test";

 when(writeableCommandLine.looksLikeOption(argument)).thenReturn(false);

 Assert.assertFalse(writeableCommandLine.looksLikeOption(argument));
 }

 @Test
 public void testGetCurrentOptionNull() {
 writeableCommandLine = mock(WriteableCommandLine.class);

 when(writeableCommandLine.getCurrentOption()).thenReturn(null);

 Assert.assertNull(writeableCommandLine.getCurrentOption());
 }

 @Test
 public void testSetCurrentOption() {
 writeableCommandLine = mock(WriteableCommandLine.class);
 Option currentOption = mock(Option.class);

 when(writeableCommandLine.setCurrentOption(currentOption)).thenReturn(writeableCommandLine);

 Assert.assertEquals(writeableCommandLine, writeableCommandLine.setCurrentOption(currentOption));
 }
}