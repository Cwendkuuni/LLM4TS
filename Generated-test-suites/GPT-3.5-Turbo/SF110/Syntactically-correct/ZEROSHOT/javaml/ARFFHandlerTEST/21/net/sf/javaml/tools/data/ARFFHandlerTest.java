import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import net.sf.javaml.core.*;
import java.io.*;

public class ARFFHandlerTest {
    
    private ARFFHandler arffHandler;
    
    @Before
    public void setup() {
        arffHandler = new ARFFHandler();
    }
    
    @Test
    public void testLoadARFF() {
        try {
            File file = new File("test.arff");
            Dataset dataset = arffHandler.loadARFF(file);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndex() {
        try {
            File file = new File("test.arff");
            int classIndex = 1;
            Dataset dataset = arffHandler.loadARFF(file, classIndex);
            assertNotNull(dataset);
        } catch (FileNotFoundException e) {
            fail("FileNotFoundException should not be thrown");
        }
    }
    
    @Test
    public void testLoadARFFThrowsFileNotFoundException() {
        try {
            File file = new File("nonexistent.arff");
            arffHandler.loadARFF(file);
            fail("FileNotFoundException should be thrown");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }
    
    @Test
    public void testLoadARFFWithClassIndexThrowsFileNotFoundException() {
        try {
            File file = new File("nonexistent.arff");
            int classIndex = 1;
            arffHandler.loadARFF(file, classIndex);
            fail("FileNotFoundException should be thrown");
        } catch (FileNotFoundException e) {
            assertTrue(true);
        }
    }
}