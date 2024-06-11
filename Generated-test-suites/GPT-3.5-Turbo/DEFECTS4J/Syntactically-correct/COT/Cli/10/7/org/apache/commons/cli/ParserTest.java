import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class ParserTest {

    private Parser parser;
    private Options options;
    
    @Before
    public void setUp() {
        parser = new Parser() {
            @Override
            protected String[] flatten(Options opts, String[] arguments, boolean stopAtNonOption) {
                return null;
            }
        };
        
        options = new Options();
        options.addOption("a", false, "Option A");
        options.addOption("b", true, "Option B");
        options.addOption("c", false, "Option C");
    }
    
    @Test
    public void testParse_NoArguments() {
        String[] args = new String[] {};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertFalse(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_SingleShortOption() {
        String[] args = new String[] {"-a"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_SingleLongOption() {
        String[] args = new String[] {"--a"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_SingleShortOptionWithValue() {
        String[] args = new String[] {"-b", "value"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertFalse(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_SingleLongOptionWithValue() {
        String[] args = new String[] {"--b", "value"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertFalse(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_MultipleOptions() {
        String[] args = new String[] {"-a", "--b", "value", "-c"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertTrue(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_StopAtNonOption() {
        String[] args = new String[] {"-a", "nonOption", "-b", "value"};
        
        try {
            CommandLine cmd = parser.parse(options, args, true);
            
            assertTrue(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
            
            String[] remainingArgs = cmd.getArgs();
            assertEquals(2, remainingArgs.length);
            assertEquals("nonOption", remainingArgs[0]);
            assertEquals("-b", remainingArgs[1]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_RequiredOptions_NoMissing() {
        String[] args = new String[] {"-a", "--b", "value"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertFalse(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test(expected=MissingOptionException.class)
    public void testParse_RequiredOptions_Missing() throws ParseException {
        String[] args = new String[] {"-a"};
        
        CommandLine cmd = parser.parse(options, args);
    }
    
    @Test
    public void testParse_Properties() {
        String[] args = new String[] {"-a"};
        
        Properties properties = new Properties();
        properties.setProperty("b", "value");
        properties.setProperty("c", "true");
        
        try {
            CommandLine cmd = parser.parse(options, args, properties);
            
            assertTrue(cmd.hasOption("a"));
            assertTrue(cmd.hasOption("b"));
            assertEquals("value", cmd.getOptionValue("b"));
            assertTrue(cmd.hasOption("c"));
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_InvalidOption() {
        String[] args = new String[] {"-d"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
        } catch (UnrecognizedOptionException e) {
            assertEquals("Unrecognized option: -d", e.getMessage());
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
    @Test
    public void testParse_EndOfOptionsSeparator() {
        String[] args = new String[] {"--", "-a", "--b", "-c"};
        
        try {
            CommandLine cmd = parser.parse(options, args);
            
            assertFalse(cmd.hasOption("a"));
            assertFalse(cmd.hasOption("b"));
            assertFalse(cmd.hasOption("c"));
            
            String[] remainingArgs = cmd.getArgs();
            assertEquals(3, remainingArgs.length);
            assertEquals("-a", remainingArgs[0]);
            assertEquals("--b", remainingArgs[1]);
            assertEquals("-c", remainingArgs[2]);
        } catch (ParseException e) {
            fail(e.getMessage());
        }
    }
    
}

