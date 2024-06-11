import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import org.apache.commons.cli.Argument;
import org.apache.commons.cli.OptionException;
import org.apache.commons.cli.WriteableCommandLine;
import org.junit.Before;
import org.junit.Test;

public class ArgumentImplTest {

   private ArgumentImpl argument;
   private WriteableCommandLine commandLine;
   private TestValidator validator;

   @Before
   public void setup() {
       argument = new ArgumentImpl("arg", "description", 0, 1, '\0', '\0', null, null, null, 0);
       commandLine = mock(WriteableCommandLine.class);
       validator = new TestValidator();
   }

   @Test
   public void testGetPreferredName() {
       assertEquals("arg", argument.getPreferredName());
   }

   @Test
   public void testProcessValues() throws OptionException {
       ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
       argument.processValues(commandLine, arguments, argument);
       verify(commandLine).addValue(argument, "value1");
       verify(commandLine).addValue(argument, "value2");
   }

   @Test
   public void testCanProcess() {
       assertTrue(argument.canProcess(commandLine, "value1"));
   }

   @Test
   public void testGetPrefixes() {
       assertEquals(Collections.EMPTY_SET, argument.getPrefixes());
   }

   @Test
   public void testProcess() throws OptionException {
       ListIterator<String> arguments = Arrays.asList("value1", "value2").listIterator();
       argument.process(commandLine, arguments);
       verify(commandLine).addValue(argument, "value1");
       verify(commandLine).addValue(argument, "value2");
   }

   @Test
   public void testGetInitialSeparator() {
       assertEquals('\0', argument.getInitialSeparator());
   }

   @Test
   public void testGetSubsequentSeparator() {
       assertEquals('\0', argument.getSubsequentSeparator());
   }

   @Test
   public void testGetTriggers() {
       assertEquals(Collections.EMPTY_SET, argument.getTriggers());
   }

   @Test
   public void testGetConsumeRemaining() {
       assertNull(argument.getConsumeRemaining());
   }

   @Test
   public void testGetDefaultValues() {
       assertNull(argument.getDefaultValues());
   }

   @Test
   public void testGetValidator() {
       assertNull(argument.getValidator());
   }

   @Test
   public void testValidate() throws OptionException {
       List<String> values = Arrays.asList("value1", "value2");
       when(commandLine.getValues(argument)).thenReturn(values);
       argument.validate(commandLine);
   }

   @Test(expected = OptionException.class)
   public void testValidateThrowsOptionException() throws OptionException {
       List<String> values = Arrays.asList("value1", "value2");
       when(commandLine.getValues(argument)).thenReturn(values);
       validator.throwException = true;
       argument.validate(commandLine);
   }

   @Test
   public void testAppendUsage() {
       StringBuffer buffer = new StringBuffer();
       Set helpSettings = new HashSet<>();
       helpSettings.add(DisplaySetting.DISPLAY_OPTIONAL);
       helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_NUMBERED);
       helpSettings.add(DisplaySetting.DISPLAY_ARGUMENT_BRACKETED);
       argument.appendUsage(buffer, helpSettings, null);
       assertEquals("arg1 arg2 ...", buffer.toString());
   }

   @Test
   public void testGetDescription() {
       assertEquals("description", argument.getDescription());
   }

   @Test
   public void testHelpLines() {
       List<HelpLine> helpLines = argument.helpLines(0, null, null);
       assertEquals(1, helpLines.size());
       HelpLine helpLine = helpLines.get(0);
       assertEquals(argument, helpLine.getOption());
       assertEquals(0, helpLine.getDepth());
       assertEquals("description", helpLine.getDescription());
   }

   @Test
   public void testGetMaximum() {
       assertEquals(1, argument.getMaximum());
   }

   @Test
   public void testGetMinimum() {
       assertEquals(0, argument.getMinimum());
   }

   @Test
   public void testIsRequired() {
       assertFalse(argument.isRequired());
   }

   @Test
   public void testDefaults() {
       argument.defaults(commandLine);
       verify(commandLine).setDefaultValues(argument, null);
   }

   class TestValidator implements Validator {

       public boolean throwException = false;

       @Override
       public void validate(List values) throws InvalidArgumentException {
           if (throwException) {
               throw new InvalidArgumentException("Invalid argument");
           }
       }
   }

}