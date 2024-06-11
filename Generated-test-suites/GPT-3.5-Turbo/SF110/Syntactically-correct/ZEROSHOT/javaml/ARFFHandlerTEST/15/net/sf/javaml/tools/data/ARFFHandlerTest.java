import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import net.sf.javaml.core.*;
import java.util.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File testFile = new File("test.arff");
        Dataset result = ARFFHandler.loadARFF(testFile);
        // Assert something about the result
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File testFile = new File("test.arff");
        int classIndex = 2;
        Dataset result = ARFFHandler.loadARFF(testFile, classIndex);
        // Assert something about the result
    }

}