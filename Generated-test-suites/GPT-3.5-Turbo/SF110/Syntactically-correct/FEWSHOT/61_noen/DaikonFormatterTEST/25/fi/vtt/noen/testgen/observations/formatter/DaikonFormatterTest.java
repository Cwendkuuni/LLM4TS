import org.junit.Test;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    @Test
    public void testConstructorWithSimpleTrue() {
        try {
            DaikonFormatter df = new DaikonFormatter("file.txt", true);
            assertNotNull(df);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testConstructorWithSimpleFalse() {
        try {
            DaikonFormatter df = new DaikonFormatter("file.txt", false);
            assertNotNull(df);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testConstructorWithInputs() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("input1");
            inputs.add("input2");
            
            DaikonFormatter df = new DaikonFormatter("file.txt", inputs);
            assertNotNull(df);
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter df = new DaikonFormatter("file.txt", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expected, df.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter df = new DaikonFormatter("file.txt", true);
        String expected = "";
        assertEquals(expected, df.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter df = new DaikonFormatter("file.txt", true);
        String expected = "dtrace";
        assertEquals(expected, df.fileNameExtension());
    }
    
    @Test
    public void testObservationsWithSimpleTrue() {
        try {
            DaikonFormatter df = new DaikonFormatter("file.txt", true);
            ProgramRun run = new ProgramRun();
            
            Event event1 = new Event("event1");
            Attribute attr1 = new Attribute("attr1", "String", "value1");
            event1.addAttribute(attr1);
            
            Event event2 = new Event("event2");
            Attribute attr2 = new Attribute("attr2", "String", "value2");
            event2.addAttribute(attr2);
            
            run.addEvent(event1);
            run.addEvent(event2);
            
            String expected = "event1:::ENTER\n  variable attr1\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10\n\nattr1\n\"value1\"\n1\nevent2:::ENTER\n  variable attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10\n\nattr2\n\"value2\"\n1";
            assertEquals(expected, df.observations(run));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
    
    @Test
    public void testObservationsWithSimpleFalse() {
        try {
            DaikonFormatter df = new DaikonFormatter("file.txt", false);
            ProgramRun run = new ProgramRun();
            
            Event event1 = new Event("event1");
            Attribute attr1 = new Attribute("attr1", "String", "value1");
            event1.addAttribute(attr1);
            
            Event event2 = new Event("event2");
            Attribute attr2 = new Attribute("attr2", "String", "value2");
            event2.addAttribute(attr2);
            
            run.addEvent(event1);
            run.addEvent(event2);
            
            String expected = "event1:::ENTER\n  variable attr1\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10\n\nattr1\n\"value1\"\n1\nevent2:::ENTER\n  variable attr2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10\n\nattr2\n\"value2\"\n1";
            assertEquals(expected, df.observations(run));
        } catch (IOException e) {
            fail("IOException should not be thrown");
        }
    }
}