package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;

import org.junit.Test;

import fi.vtt.noen.testgen.model.ArrayAttribute;
import fi.vtt.noen.testgen.model.Const;
import fi.vtt.noen.testgen.model.Event;
import fi.vtt.noen.testgen.model.EventAttribute;
import fi.vtt.noen.testgen.model.ProgramRun;

public class DaikonFormatterTest {
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        String expectedOutput = "decl-version 2.0\n" + "var-comparability implicit\n";
        assertEquals(expectedOutput, formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
    
    @Test
    public void testObservations_SimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("Event1"));
        run.addEvent(new Event("Event2"));
        run.addEvent(new Event("Event3", true));
        
        String expectedOutput = "\n" +
                "Event1:::ENTER\n" +
                "\n" +
                "Event1\n" +
                "Event2:::ENTER\n" +
                "\n" +
                "Event2\n" +
                "Event3:::EXIT1\n";
        
        assertEquals(expectedOutput, formatter.observations(run));
    }
    
    @Test
    public void testObservations_ComplexPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("Enter1"));
        run.addEvent(new Event("Enter2", true));
        run.addEvent(new Event("MidEvent1"));
        run.addEvent(new Event("MidEvent2", true));
        run.addEvent(new Event("Exit1"));
        
        String expectedOutput = "\n" +
                "Enter1:::ENTER\n" +
                "\n" +
                "Enter1MidEvent1:::ENTER\n" +
                "\n" +
                "Enter1MidEvent1\n" +
                "MidEvent1:::EXIT1\n" +
                "\n" +
                "MidEvent1MidEvent2:::ENTER\n" +
                "\n" +
                "MidEvent1MidEvent2\n" +
                "MidEvent2:::EXIT1\n" +
                "\n" +
                "MidEvent2Exit1:::ENTER\n" +
                "\n" +
                "MidEvent2Exit1\n" +
                "Exit1:::EXIT1\n";
        
        assertEquals(expectedOutput, formatter.observations(run));
    }
    
    @Test
    public void testObservations_ExitPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        ProgramRun run = new ProgramRun();
        run.addEvent(new Event("Enter1"));
        run.addEvent(new Event("Enter2"));
        run.addEvent(new Event("Exit1", true));
        
        String expectedOutput = "\n" +
                "Enter1:::ENTER\n" +
                "\n" +
                "Enter1Enter2:::ENTER\n" +
                "\n" +
                "Enter1Enter2\n" +
                "Enter2:::EXIT1\n" +
                "\n" +
                "Enter2Exit1:::ENTER\n" +
                "\n" +
                "Enter2Exit1\n" +
                "Exit1:::EXIT1\n";
        
        assertEquals(expectedOutput, formatter.observations(run));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", true);
        Event event = new Event("Event1");
        OutputBuffer out = new OutputBuffer();
        String comparability = "10";
        
        String expectedOutput = "Event1:::ENTER\n" +
                "  variable name\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 10\n" +
                "\n" +
                "Event1\n";
        
        formatter.simplePoint(event, comparability, out);
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Event event = new Event("Event1");
        OutputBuffer out = new OutputBuffer();
        String name = "Event1Event2";
        String comparability = "11";
        
        String expectedOutput = "Event1Event2:::ENTER\n" +
                "  variable name\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n" +
                "\n" +
                "Event1Event2\n";
        
        formatter.point(name, event, comparability, out);
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Event event = new Event("Event1");
        String eventName = "Event1:::ENTER";
        OutputBuffer out = new OutputBuffer();
        String comparability = "11";
        
        String expectedOutput = "\n" +
                "ppt Event1:::ENTER\n" +
                "ppt-type enter\n" +
                "  variable name\n" +
                "    var-kind variable\n" +
                "    dec-type java.lang.String\n" +
                "    rep-type java.lang.String\n" +
                "    comparability 11\n";
        
        formatter.declarations(event, eventName, out, comparability);
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Event event = new Event("Event1");
        OutputBuffer out = new OutputBuffer();
        
        event.addAttribute(new EventAttribute("attr1", "1", "int"));
        event.addAttribute(new EventAttribute("attr2", "test", "java.lang.String"));
        
        String expectedOutput = "attr1\n" +
                "1\n" +
                "1\n" +
                "attr2\n" +
                "\"test\"\n" +
                "1\n";
        
        formatter.attributes(event, out);
        assertEquals(expectedOutput, out.toString());
    }
    
    @Test
    public void testClassesForMultipleProperties() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Collection<Class> classes = formatter.classesForMultipleProperties("InputInterface");
        assertEquals(0, classes.size());
    }
    
    @Test
    public void testInputs() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Collection<Class> classes = formatter.inputs();
        assertEquals(0, classes.size());
    }
    
    @Test
    public void testOutputs() throws Exception {
        DaikonFormatter formatter = new DaikonFormatter("testfile.txt", false);
        Collection<Class> classes = formatter.outputs();
        assertEquals(0, classes.size());
    }
}