package fi.vtt.noen.testgen.observations.formatter;

import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import fi.vtt.noen.testgen.model.*;

public class DaikonFormatterTest {

    @Test
    public void testDaikonFormatterSimpleConstructor() {
        try {
            DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown for simple constructor: " + e.getMessage());
        }
    }
    
    @Test
    public void testDaikonFormatterInputsConstructor() {
        try {
            Collection<String> inputs = new ArrayList<>();
            inputs.add("Input1");
            inputs.add("Input2");
            DaikonFormatter formatter = new DaikonFormatter("testFile.txt", inputs);
            assertNotNull(formatter);
        } catch (IOException e) {
            fail("Exception thrown for inputs constructor: " + e.getMessage());
        }
    }

    @Test
    public void testClassForProperty() {
        try {
            Class clazz = DaikonFormatter.classForProperty("TestProperty");
            assertNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown for classForProperty: " + e.getMessage());
        }
    }

    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("TestPrefix");
            assertNotNull(classes);
        } catch (Exception e) {
            fail("Exception thrown for classesForMultipleProperties: " + e.getMessage());
        }
    }

    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
        } catch (Exception e) {
            fail("Exception thrown for inputs: " + e.getMessage());
        }
    }

    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
        } catch (Exception e) {
            fail("Exception thrown for outputs: " + e.getMessage());
        }
    }

    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        String expectedResult = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedResult, formatter.header());
    }

    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        assertEquals("", formatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }

    @Test
    public void testObservations() {
        DaikonFormatter formatter = new DaikonFormatter("testFile.txt", true);
        ProgramRun run = new ProgramRun();
        String result = formatter.observations(run);
        assertNotNull(result);
    }
}