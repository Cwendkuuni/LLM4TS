package fi.vtt.noen.testgen.observations.formatter;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

import java.io.IOException;
import java.util.Collection;
import java.util.HashSet;

public class DaikonFormatterTest {

    private DaikonFormatter daikonFormatter;

    @Before
    public void setup() {
        try {
            daikonFormatter = new DaikonFormatter("testFile", false);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructorWithSimpleParameter() {
        try {
            daikonFormatter = new DaikonFormatter("testFile", true);
            assertNull(daikonFormatter.inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testConstructorWithInputsParameter() {
        Collection<String> inputs = new HashSet<>();
        inputs.add("input1");
        inputs.add("input2");
        try {
            daikonFormatter = new DaikonFormatter("testFile", inputs);
            assertEquals(inputs, daikonFormatter.inputs);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClassForProperty() {
        try {
            assertNull(DaikonFormatter.classForProperty("property1"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testClassesForMultipleProperties() {
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("prefix");
            assertNotNull(classes);
            assertEquals(0, classes.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
            assertEquals(0, inputs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
            assertEquals(0, outputs.size());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void testHeader() {
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit\n";
        assertEquals(expectedHeader, daikonFormatter.header());
    }

    @Test
    public void testFooter() {
        assertEquals("", daikonFormatter.footer());
    }

    @Test
    public void testFileNameExtension() {
        assertEquals("dtrace", daikonFormatter.fileNameExtension());
    }

    // TODO: Add more test methods to cover all methods in the DaikonFormatter class

}