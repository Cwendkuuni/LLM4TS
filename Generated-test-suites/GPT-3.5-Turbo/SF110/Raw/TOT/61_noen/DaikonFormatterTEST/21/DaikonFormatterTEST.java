import org.junit.*;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    
    @Test
    public void testDaikonFormatterConstructorWithSimpleParameter() {
        boolean simple = true;
        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", simple);
            assertNotNull(daikonFormatter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDaikonFormatterConstructorWithoutSimpleParameter() {
        boolean simple = false;
        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", simple);
            assertNotNull(daikonFormatter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testDaikonFormatterConstructorWithInvalidFile() {
        boolean simple = false;
        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("notfound.txt", simple);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue(e instanceof IOException);
            assertEquals("File not found", e.getMessage());
        }
    }
    
    @Test
    public void testDaikonFormatterConstructorWithInvalidInputs() {
        boolean simple = false;
        try {
            DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", null);
            fail("No exception thrown");
        } catch (Exception e) {
            assertTrue(e instanceof NullPointerException);
            assertEquals("Inputs cannot be null", e.getMessage());
        }
    }
    
    @Test
    public void testDaikonFormatterConstructorWithValidInputs() {
        boolean simple = false;
        try {
            Collection<String> inputs = new ArrayList<String>();
            inputs.add("InputInterface1");
            inputs.add("InputInterface2");
            DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", inputs);
            assertNotNull(daikonFormatter);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassForPropertyWithExistingClass() {
        String property = "InputInterface";
        try {
            Class clazz = DaikonFormatter.classForProperty(property);
            assertNotNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassForPropertyWithNonExistingClass() {
        String property = "InvalidProperty";
        try {
            Class clazz = DaikonFormatter.classForProperty(property);
            assertNull(clazz);
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testClassesForMultiplePropertiesWithPrefix() {
        String prefix = "InputInterface";
        try {
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties(prefix);
            assertNotNull(classes);
            assertFalse(classes.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testInputs() {
        try {
            Collection<Class> inputs = DaikonFormatter.inputs();
            assertNotNull(inputs);
            assertFalse(inputs.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testOutputs() {
        try {
            Collection<Class> outputs = DaikonFormatter.outputs();
            assertNotNull(outputs);
            assertFalse(outputs.isEmpty());
        } catch (Exception e) {
            fail("Exception thrown");
        }
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
        String expectedHeader = "decl-version 2.0\nvar-comparability implicit";
        assertEquals(expectedHeader, daikonFormatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
        assertEquals("", daikonFormatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
        assertEquals("dtrace", daikonFormatter.fileNameExtension());
    }
    
    @Test
    public void testObservations() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", false);
        ProgramRun programRun = new ProgramRun();
        programRun.addEvent(new Event("enter", new EventAttribute("attribute1", "value1")));
        programRun.addEvent(new Event("exit", new EventAttribute("attribute2", "value2")));
        String expectedObservations = "enter:::ENTER\n\nppt enter:::ENTER\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\nattribute1\nvalue11\n\nexit:::ENTER\n\nppt exit:::ENTER\nppt-type enter\n  variable attribute2\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11\nattribute2\nvalue21\n";
        assertEquals(expectedObservations, daikonFormatter.observations(programRun));
    }
    
    @Test
    public void testSimplePoint() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", true);
        Event event = new Event("enter", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "enter:::ENTER\n\nppt enter:::ENTER\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 10";
        daikonFormatter.simplePoint(event, "10", out);
        assertEquals(expectedPoint, out.toString());
    }
    
    @Test
    public void testPoint() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", false);
        Event event = new Event("enter", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        String expectedPoint = "enter,exit:::ENTER\n\nppt enter,exit:::ENTER\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11";
        daikonFormatter.point("enter,exit", event, "11", out);
        assertEquals(expectedPoint, out.toString());
    }
    
    @Test
    public void testDeclarations() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", false);
        Event event = new Event("enter", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        String expectedDeclarations = "\nppt enter:::ENTER\nppt-type enter\n  variable attribute1\n    var-kind variable\n    dec-type null\n    rep-type null\n    comparability 11";
        daikonFormatter.declarations(event, "enter", out, "11");
        assertEquals(expectedDeclarations, out.toString());
    }
    
    @Test
    public void testAttributes() {
        DaikonFormatter daikonFormatter = new DaikonFormatter("test.txt", false);
        Event event = new Event("enter", new EventAttribute("attribute1", "value1"));
        OutputBuffer out = new OutputBuffer();
        String expectedAttributes = "attribute1\nvalue11";
        daikonFormatter.attributes(event, out);
        assertEquals(expectedAttributes, out.toString());
    }
}