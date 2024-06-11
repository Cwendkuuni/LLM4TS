import static org.junit.Assert.*;
import org.junit.Test;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.*;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFF_fileNotFound() {
        try {
            Dataset result = ARFFHandler.loadARFF(new File("nonexistent_file.arff"));
            fail("Expected FileNotFoundException to be thrown");
        } catch (FileNotFoundException e) {
            assertEquals("File not found: nonexistent_file.arff", e.getMessage());
        }
    }

    @Test
    public void testLoadARFF_defaultClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset result = ARFFHandler.loadARFF(file);

        // Validate dataset size
        assertEquals(5, result.size());

        // Validate first instance
        Instance firstInstance = result.get(0);
        assertEquals(4, firstInstance.noAttributes());
        assertEquals(4.2, firstInstance.value(0), 0.001);
        assertEquals(3.5, firstInstance.value(1), 0.001);
        assertEquals(2.1, firstInstance.value(2), 0.001);
        assertEquals(1.0, firstInstance.value(3), 0.001);
        assertNull(firstInstance.classValue());

        // Validate second instance
        Instance secondInstance = result.get(1);
        assertEquals(4, secondInstance.noAttributes());
        assertEquals(2.5, secondInstance.value(0), 0.001);
        assertEquals(6.4, secondInstance.value(1), 0.001);
        assertEquals(3.2, secondInstance.value(2), 0.001);
        assertEquals(0.5, secondInstance.value(3), 0.001);
        assertNull(secondInstance.classValue());

        // Validate third instance
        // ...

        // Validate fourth instance
        // ...

        // Validate fifth instance
        // ...
    }

    @Test
    public void testLoadARFF_withClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        int classIndex = 2;
        Dataset result = ARFFHandler.loadARFF(file, classIndex);

        // Validate dataset size
        assertEquals(5, result.size());

        // Validate first instance
        Instance firstInstance = result.get(0);
        assertEquals(3, firstInstance.noAttributes());
        assertEquals(4.2, firstInstance.value(0), 0.001);
        assertEquals(3.5, firstInstance.value(1), 0.001);
        assertNull(firstInstance.value(2));
        assertEquals("classA", firstInstance.classValue());

        // Validate second instance
        Instance secondInstance = result.get(1);
        // ...

        // Validate third instance
        // ...

        // Validate fourth instance
        // ...

        // Validate fifth instance
        // ...
    }

}