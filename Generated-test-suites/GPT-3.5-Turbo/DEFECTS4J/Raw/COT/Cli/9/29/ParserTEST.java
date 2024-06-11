import org.junit.Test;
import static org.junit.Assert.*;

public class ParserTest {
  
  @Test
  public void testFlatten() {
    Options options = new Options();
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    String[] arguments = new String[0];
    String[] result = parser.flatten(options, arguments, false);
    assertArrayEquals(new String[0], result);
  }
  
  @Test
  public void testParse() {
    Options options = new Options();
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    String[] arguments = new String[0];
    try {
      CommandLine cmd = parser.parse(options, arguments);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
  
  @Test
  public void testParseWithProperties() {
    Options options = new Options();
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    String[] arguments = new String[0];
    Properties properties = new Properties();
    try {
      CommandLine cmd = parser.parse(options, arguments, properties);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
  
  @Test
  public void testParseWithStopAtNonOption() {
    Options options = new Options();
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    String[] arguments = new String[0];
    try {
      CommandLine cmd = parser.parse(options, arguments, true);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
  
  @Test
  public void testParseWithStopAtNonOptionAndProperties() {
    Options options = new Options();
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    String[] arguments = new String[0];
    Properties properties = new Properties();
    try {
      CommandLine cmd = parser.parse(options, arguments, properties, true);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
  
  @Test
  public void testProcessProperties() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    Properties properties = new Properties();
    parser.processProperties(properties);
  }
  
  @Test
  public void testCheckRequiredOptions() {
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    try {
      parser.checkRequiredOptions();
    } catch (MissingOptionException e) {
      fail("MissingOptionException not expected");
    }
  }
  
  @Test
  public void testProcessArgs() {
    Option opt = new Option("opt", null, false, null);
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    ListIterator iter = new ListIterator() {
      @Override
      public void add(Object e) {}
      @Override
      public boolean hasNext() { return false; }
      @Override
      public boolean hasPrevious() { return false; }
      @Override
      public Object next() { return null; }
      @Override
      public int nextIndex() { return 0; }
      @Override
      public Object previous() { return null; }
      @Override
      public int previousIndex() { return 0; }
      @Override
      public void remove() {}
      @Override
      public void set(Object e) {}
    };
    try {
      parser.processArgs(opt, iter);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
  
  @Test
  public void testProcessOption() {
    Option opt = new Option("opt", null, false, null);
    Parser parser = new Parser() {
      @Override
      protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
        return new String[0];
      }
    };
    ListIterator iter = new ListIterator() {
      @Override
      public void add(Object e) {}
      @Override
      public boolean hasNext() { return false; }
      @Override
      public boolean hasPrevious() { return false; }
      @Override
      public Object next() { return null; }
      @Override
      public int nextIndex() { return 0; }
      @Override
      public Object previous() { return null; }
      @Override
      public int previousIndex() { return 0; }
      @Override
      public void remove() {}
      @Override
      public void set(Object e) {}
    };
    try {
      parser.processOption("opt", iter);
    } catch (ParseException e) {
      fail("ParseException not expected");
    }
  }
}