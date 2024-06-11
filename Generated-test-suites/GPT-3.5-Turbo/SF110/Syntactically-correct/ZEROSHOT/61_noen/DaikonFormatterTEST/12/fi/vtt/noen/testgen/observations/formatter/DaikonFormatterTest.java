package fi.vtt.noen.testgen.observations.formatter;

import org.junit.*;
import static org.junit.Assert.*;

public class DaikonFormatterTest {
    @Test
    public void testConstructor() {
        // Test case 1
		try{
            DaikonFormatter formatter = new DaikonFormatter("testFileName", true);
            assertEquals(true, formatter.simple);
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
        
        // Test case 2
		try{
            DaikonFormatter formatter = new DaikonFormatter("testFileName", false);
            assertEquals(false, formatter.simple);
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
    }
    
    @Test
    public void testClassForProperty() {
        // Test case 1
		try{
            Class clazz = DaikonFormatter.classForProperty("testProperty1");
            assertEquals(null, clazz);
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
        
        // Test case 2
		try{
            Class clazz = DaikonFormatter.classForProperty("testProperty2");
            assertEquals(null, clazz);
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
    }
    
    @Test
    public void testClassesForMultipleProperties() {
        // Test case 1
		try{
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("testPrefix1");
            assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
        
        // Test case 2
		try{
            Collection<Class> classes = DaikonFormatter.classesForMultipleProperties("testPrefix2");
            assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
    }
    
    @Test
    public void testInputs() {
        // Test case 1
		try{
            Collection<Class> classes = DaikonFormatter.inputs();
			assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
        
        // Test case 2
		try{
            Collection<Class> classes = DaikonFormatter.inputs();
			assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
    }
    
    @Test
    public void testOutputs() {
        // Test case 1
		try{
            Collection<Class> classes = DaikonFormatter.outputs();
			assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
        
        // Test case 2
		try{
            Collection<Class> classes = DaikonFormatter.outputs();
			assertEquals(0, classes.size());
        } catch (Exception e) {
			fail("Exception thrown: " + e.getMessage());
		}
    }
    
    @Test
    public void testHeader() {
        DaikonFormatter formatter = new DaikonFormatter("testFileName", true);
        assertEquals("decl-version 2.0\nvar-comparability implicit", formatter.header());
    }
    
    @Test
    public void testFooter() {
        DaikonFormatter formatter = new DaikonFormatter("testFileName", true);
        assertEquals("", formatter.footer());
    }
    
    @Test
    public void testFileNameExtension() {
        DaikonFormatter formatter = new DaikonFormatter("testFileName", true);
        assertEquals("dtrace", formatter.fileNameExtension());
    }
}