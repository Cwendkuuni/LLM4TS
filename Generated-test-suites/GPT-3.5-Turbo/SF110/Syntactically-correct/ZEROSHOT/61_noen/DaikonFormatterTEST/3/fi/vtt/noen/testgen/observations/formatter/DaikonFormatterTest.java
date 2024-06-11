import org.junit.*;
import java.io.*;
import java.util.*;
import fi.vtt.noen.testgen.observations.formatter.DaikonFormatter;
import fi.vtt.noen.testgen.parser.InterfaceParser;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.observations.data.Const;
import fi.vtt.noen.testgen.observations.data.EventAttribute;
import fi.vtt.noen.testgen.observations.data.ArrayAttribute;

public class DaikonFormatterTest {
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "decl-version 2.0\nvar-comparability implicit";
        String actual = formatter.header();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "";
        String actual = formatter.footer();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String expected = "dtrace";
        String actual = formatter.fileNameExtension();
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testObservationsSimple() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        Event event = new Event("testEvent", false);
        event.addAttribute(new EventAttribute("attribute1", "int", "1"));
        event.addAttribute(new EventAttribute("attribute2", "String", "\"test\""));
        ProgramRun run = new ProgramRun();
        run.addEvent(event);
        
        String expected = "testEvent:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 10\n  variable attribute2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 10";
        String actual = formatter.observations(run);
        Assert.assertEquals(expected, actual);
    }
    
    @Test
    public void testObservationsComplex() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", false);
        
        List<String> inputs = new ArrayList<>();
        inputs.add("input1");
        inputs.add("input2");
        
        DaikonFormatter formatter = new DaikonFormatter("testFile", inputs);
        Event event1 = new Event("input1", false);
        event1.addAttribute(new EventAttribute("attribute1", "int", "1"));
        event1.addAttribute(new EventAttribute("attribute2", "String", "\"test\""));
        
        Event event2 = new Event("input2", false);
        event2.addAttribute(new EventAttribute("attribute3", "int", "2"));
        event2.addAttribute(new EventAttribute("attribute4", "String", "\"test2\""));
        
        Event event3 = new Event("input1", true);
        event3.addAttribute(new EventAttribute("attribute5", "int", "3"));
        event3.addAttribute(new EventAttribute("attribute6", "String", "\"test3\""));
        
        ProgramRun run = new ProgramRun();
        run.addEvent(event1);
        run.addEvent(event2);
        run.addEvent(event3);
        
        String expected = "input1:::ENTER\n  variable attribute1\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable attribute2\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11\ninput2:::ENTER\n  variable attribute3\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 11\n  variable attribute4\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 11\ninput1,input2:::ENTER\n  variable attribute5\n    var-kind variable\n    dec-type int\n    rep-type int\n    comparability 22\n  variable attribute6\n    var-kind variable\n    dec-type String\n    rep-type String\n    comparability 22\ninput1:::EXIT1\n  variable attribute5[]\n    var-kind array\n    dec-type int\n    rep-type int\n    comparability 22\n  variable attribute6[]\n    var-kind array\n    dec-type String\n    rep-type String\n    comparability 22";
        String actual = formatter.observations(run);
        Assert.assertEquals(expected, actual);
    }
}