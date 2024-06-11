import org.junit.Test;
import static org.junit.Assert.*;
import java.io.File;
import java.io.FileNotFoundException;
import net.sf.javaml.core.Dataset;
import net.sf.javaml.core.DefaultDataset;

public class ARFFHandlerTest {

    @Test
    public void testLoadARFFWithoutClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        
        // Edge Case: Empty file
        File emptyFile = new File("empty.arff");
        Dataset emptyDataset = ARFFHandler.loadARFF(emptyFile);
        
        assertNotNull(emptyDataset);
        assertEquals(0, emptyDataset.size());
        
        // Edge Case: File with only attribute header and data section
        File attributeOnlyFile = new File("attribute_only.arff");
        Dataset attributeOnlyDataset = ARFFHandler.loadARFF(attributeOnlyFile);
        
        assertNotNull(attributeOnlyDataset);
        assertEquals(0, attributeOnlyDataset.size());
        
        // Edge Case: File with incomplete attribute values
        File incompleteAttributeFile = new File("incomplete_attribute.arff");
        Dataset incompleteAttributeDataset = ARFFHandler.loadARFF(incompleteAttributeFile);
        
        assertNotNull(incompleteAttributeDataset);
        assertEquals(0, incompleteAttributeDataset.size());
        
        // Typical Use Case: File with multiple instances and attributes, no class index specified
        File multipleInstancesFile = new File("multiple_instances.arff");
        Dataset multipleInstancesDataset = ARFFHandler.loadARFF(multipleInstancesFile);
        
        assertNotNull(multipleInstancesDataset);
        assertEquals(3, multipleInstancesDataset.size());
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File file = new File("test.arff");
        Dataset dataset = ARFFHandler.loadARFF(file, 2);
        
        assertNotNull(dataset);
        assertEquals(0, dataset.size());
        
        // Edge Case: Empty file
        File emptyFile = new File("empty.arff");
        Dataset emptyDataset = ARFFHandler.loadARFF(emptyFile, 1);
        
        assertNotNull(emptyDataset);
        assertEquals(0, emptyDataset.size());
        
        // Edge Case: File with only attribute header and data section
        File attributeOnlyFile = new File("attribute_only.arff");
        Dataset attributeOnlyDataset = ARFFHandler.loadARFF(attributeOnlyFile, 0);
        
        assertNotNull(attributeOnlyDataset);
        assertEquals(0, attributeOnlyDataset.size());
        
        // Edge Case: File with incomplete attribute values
        File incompleteAttributeFile = new File("incomplete_attribute.arff");
        Dataset incompleteAttributeDataset = ARFFHandler.loadARFF(incompleteAttributeFile, 3);
        
        assertNotNull(incompleteAttributeDataset);
        assertEquals(0, incompleteAttributeDataset.size());
        
        // Typical Use Case: File with multiple instances and attributes, with class index specified
        File multipleInstancesFile = new File("multiple_instances.arff");
        Dataset multipleInstancesDataset = ARFFHandler.loadARFF(multipleInstancesFile, 1);
        
        assertNotNull(multipleInstancesDataset);
        assertEquals(3, multipleInstancesDataset.size());
    }
    
}