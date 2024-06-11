package net.sf.javaml.tools.data;
import static org.junit.Assert.*;
import org.junit.Test;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.Instance;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
    }

    @Test
    public void testLoadARFFWithInvalidFile() {
        File file = new File("invalid-file.arff");
        assertThrows(FileNotFoundException.class, () -> ARFFHandler.loadARFF(file));
    }

    @Test
    public void testLoadARFFWithInvalidClassIndex() {
        File file = new File("test.arff");
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> ARFFHandler.loadARFF(file, 5));
    }

}