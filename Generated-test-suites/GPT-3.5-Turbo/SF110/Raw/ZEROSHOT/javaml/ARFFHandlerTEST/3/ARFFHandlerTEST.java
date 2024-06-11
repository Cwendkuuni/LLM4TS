import static org.junit.Assert.*;
import org.junit.Test;
import java.io.*;
import java.util.*;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset output = ARFFHandler.loadARFF(file);
        assertNotNull(output);
        assertEquals(0, output.size());
    }
    
    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 1;
        Dataset output = ARFFHandler.loadARFF(file, classIndex);
        assertNotNull(output);
        assertEquals(0, output.size());
    }

    @Test
    public void testLoadARFFWithData() throws FileNotFoundException {
        File file = new File("test.arff");
        List<String> dataLines = Arrays.asList("@relation test", 
                                                "@attribute att1 numeric", 
                                                "@attribute att2 numeric", 
                                                "@data", 
                                                "1,2", 
                                                "3,4");
        writeLinesToFile(file, dataLines);
        Dataset output = ARFFHandler.loadARFF(file);
        assertNotNull(output);
        assertEquals(2, output.size());
    }
    
    private void writeLinesToFile(File file, List<String> lines) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        for (String line : lines) {
            writer.println(line);
        }
        writer.close();
    }
}