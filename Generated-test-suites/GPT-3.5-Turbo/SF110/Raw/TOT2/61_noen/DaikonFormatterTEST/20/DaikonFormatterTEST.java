package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;
import fi.vtt.noen.testgen.model.ProgramRun;
import fi.vtt.noen.testgen.parser.Event;
import fi.vtt.noen.testgen.observations.data.*;

public class DaikonFormatterTest {

    @Test
    public void testConstructor1() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testConstructor2() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile", new Collection<String>());
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassForProperty() {
        try {
            Class clazz = DaikonFormatter.classForProperty("testProperty");
            assertNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("testPrefix");
            assertNotNull(classes);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String header = formatter.header();
        assertNotNull(header);
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String footer = formatter.footer();
        assertNotNull(footer);
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        String fileNameExtension = formatter.fileNameExtension();
        assertNotNull(fileNameExtension);
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile", true);
        ProgramRun run = new ProgramRun();
        String observations = formatter.observations(run);
        assertNotNull(observations);
    }
    
}