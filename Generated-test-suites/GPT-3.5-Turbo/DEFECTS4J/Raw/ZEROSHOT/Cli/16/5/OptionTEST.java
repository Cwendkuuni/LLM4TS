import org.apache.commons.cli2.Option;
import org.apache.commons.cli2.OptionException;
import org.apache.commons.cli2.WriteableCommandLine;
import org.apache.commons.cli2.util.HelpFormatter;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.ListIterator;
import java.util.Set;

import static org.junit.Assert.*;

public class OptionTest {
  
   private Option option;
  
   @Before
   public void setUp() {
       option = new Option() {
           @Override
           public void process(WriteableCommandLine commandLine, ListIterator args) throws OptionException {
           }
  
           @Override
           public void defaults(WriteableCommandLine commandLine) {
           }
  
           @Override
           public boolean canProcess(WriteableCommandLine commandLine, String argument) {
               return false;
           }
  
           @Override
           public boolean canProcess(WriteableCommandLine commandLine, ListIterator arguments) {
               return false;
           }
  
           @Override
           public Set getTriggers() {
               return new HashSet<>();
           }
  
           @Override
           public Set getPrefixes() {
               return new HashSet<>();
           }
  
           @Override
           public void validate(WriteableCommandLine commandLine) throws OptionException {
           }
  
           @Override
           public List helpLines(int depth, Set helpSettings, Comparator comp) {
               return new ArrayList<>();
           }
  
           @Override
           public void appendUsage(StringBuffer buffer, Set helpSettings, Comparator comp) {
           }
  
           @Override
           public String getPreferredName() {
               return null;
           }
  
           @Override
           public String getDescription() {
               return null;
           }
  
           @Override
           public int getId() {
               return 0;
           }
  
           @Override
           public Option findOption(String trigger) {
               return null;
           }
  
           @Override
           public boolean isRequired() {
               return false;
           }
  
           @Override
           public Option getParent() {
               return null;
           }
  
           @Override
           public void setParent(Option parent) {
           }
       };
   }
  
   @Test
   public void testProcess() {
       WriteableCommandLine commandLine = null;
       ListIterator args = null;
  
       try {
           option.process(commandLine, args);
       } catch (OptionException e) {
           fail(e.getMessage());
       }
   }
  
   @Test
   public void testDefaults() {
       WriteableCommandLine commandLine = null;
       option.defaults(commandLine);
   }
  
   @Test
   public void testCanProcessWithArgument() {
       WriteableCommandLine commandLine = null;
       String argument = "";
  
       boolean result = option.canProcess(commandLine, argument);
       assertFalse(result);
   }
  
   @Test
   public void testCanProcessWithArguments() {
       WriteableCommandLine commandLine = null;
       ListIterator arguments = null;
  
       boolean result = option.canProcess(commandLine, arguments);
       assertFalse(result);
   }
  
   @Test
   public void testGetTriggers() {
       Set triggers = option.getTriggers();
       assertNotNull(triggers);
   }
  
   @Test
   public void testGetPrefixes() {
       Set prefixes = option.getPrefixes();
       assertNotNull(prefixes);
   }
  
   @Test
   public void testValidate() {
       WriteableCommandLine commandLine = null;
  
       try {
           option.validate(commandLine);
       } catch (OptionException e) {
           fail(e.getMessage());
       }
   }
  
   @Test
   public void testHelpLines() {
       int depth = 0;
       Set helpSettings = new HashSet<>();
       Comparator comp = null;
  
       List helpLines = option.helpLines(depth, helpSettings, comp);
       assertNotNull(helpLines);
   }
  
   @Test
   public void testAppendUsage() {
       StringBuffer buffer = new StringBuffer();
       Set helpSettings = new HashSet<>();
       Comparator comp = null;
  
       option.appendUsage(buffer, helpSettings, comp);
   }
  
   @Test
   public void testGetPreferredName() {
       String preferredName = option.getPreferredName();
       assertNull(preferredName);
   }
  
   @Test
   public void testGetDescription() {
       String description = option.getDescription();
       assertNull(description);
   }
  
   @Test
   public void testGetId() {
       int id = option.getId();
       assertEquals(0, id);
   }
  
   @Test
   public void testFindOption() {
       String trigger = "";
       Option result = option.findOption(trigger);
       assertNull(result);
   }
  
   @Test
   public void testIsRequired() {
       boolean required = option.isRequired();
       assertFalse(required);
   }
  
   @Test
   public void testGetParent() {
       Option parent = option.getParent();
       assertNull(parent);
   }
  
   @Test
   public void testSetParent() {
       Option parent = null;
       option.setParent(parent);
   }
}