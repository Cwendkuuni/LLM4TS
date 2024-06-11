import org.junit.Test;
import org.junit.Assert;
import org.junit.BeforeClass;
import java.io.File;
import java.io.FileNotFoundException;

public class ARFFHandlerTest {
    private static final String ARFF_FILE_PATH = "test.arff";

    @BeforeClass
    public static void setup() {
        // Create a test ARFF file to be used for testing
        try {
            File testFile = new File(ARFF_FILE_PATH);
            testFile.createNewFile();
        } catch (Exception ex) {
            Assert.fail("Failed to create test ARFF file.");
        }
    }

    @Test
    public void testLoadARFF_File_Success() {
        // Test loading a valid ARFF file
        try {
            File testFile = new File(ARFF_FILE_PATH);
            Dataset dataset = ARFFHandler.loadARFF(testFile);
            Assert.assertNotNull("Dataset should not be null", dataset);
            Assert.assertEquals("Dataset size should be zero", 0, dataset.size());
        } catch (Exception ex) {
            Assert.fail("Failed to load ARFF file.");
        }
    }

    @Test
    public void testLoadARFF_File_ClassIndex_Success() {
        // Test loading a valid ARFF file with class index specified
        try {
            File testFile = new File(ARFF_FILE_PATH);
            Dataset dataset = ARFFHandler.loadARFF(testFile, 2);
            Assert.assertNotNull("Dataset should not be null", dataset);
            Assert.assertEquals("Dataset size should be zero", 0, dataset.size());
        } catch (Exception ex) {
            Assert.fail("Failed to load ARFF file with class index.");
        }
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFF_File_NotFoundException() throws FileNotFoundException {
        // Test loading a non-existing ARFF file
        File testFile = new File("nonexistent.arff");
        ARFFHandler.loadARFF(testFile);
    }

    @Test
    public void testLoadARFF_File_ClassIndex_NotFoundException() {
        // Test loading a non-existing ARFF file with class index specified
        try {
            File testFile = new File("nonexistent.arff");
            ARFFHandler.loadARFF(testFile, 1);
            Assert.fail("Expected FileNotFoundException not thrown.");
        } catch (FileNotFoundException ex) {
            // FileNotFoundException correctly thrown
        } catch (Exception ex) {
            Assert.fail("Incorrect exception thrown when loading non-existing ARFF file with class index.");
        }
    }

    @Test
    public void testLoadARFF_File_EmptyFile() throws FileNotFoundException {
        // Test loading an empty ARFF file
        File testFile = new File(ARFF_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(testFile);
        Assert.assertNotNull("Dataset should not be null", dataset);
        Assert.assertEquals("Dataset size should be zero", 0, dataset.size());
    }

    @Test
    public void testLoadARFF_File_ClassIndex_EmptyFile() throws FileNotFoundException {
        // Test loading an empty ARFF file with class index specified
        File testFile = new File(ARFF_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(testFile, 1);
        Assert.assertNotNull("Dataset should not be null", dataset);
        Assert.assertEquals("Dataset size should be zero", 0, dataset.size());
    }

    @Test
    public void testLoadARFF_File_InvalidData() throws FileNotFoundException {
        // Test loading an ARFF file with invalid data
        File testFile = new File(ARFF_FILE_PATH);
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("@relation test\n");
            writer.write("@attribute attr1 numeric\n");
            writer.write("@attribute attr2 numeric\n");
            writer.write("@data\n");
            writer.write("1,2\n");
            writer.write("a,4\n");
            writer.close();
        } catch (Exception ex) {
            Assert.fail("Failed to write to test ARFF file.");
        }
        Dataset dataset = ARFFHandler.loadARFF(testFile);
        Assert.assertNotNull("Dataset should not be null", dataset);
        Assert.assertEquals("Dataset size should be one", 1, dataset.size());
        Instance instance = dataset.get(0);
        Assert.assertEquals("First attribute value should be NaN", Double.NaN, instance.value(0), 0.00001);
        Assert.assertEquals("Second attribute value should be 4.0", 4.0, instance.value(1), 0.00001);
    }

    @Test
    public void testLoadARFF_File_ClassIndex_InvalidData() throws FileNotFoundException {
        // Test loading an ARFF file with class index specified and invalid data
        File testFile = new File(ARFF_FILE_PATH);
        try {
            FileWriter writer = new FileWriter(testFile);
            writer.write("@relation test\n");
            writer.write("@attribute attr1 numeric\n");
            writer.write("@attribute class {yes,no}\n");
            writer.write("@data\n");
            writer.write("1,yes\n");
            writer.write("2,no\n");
            writer.write("3,invalid\n");
            writer.close();
        } catch (Exception ex) {
            Assert.fail("Failed to write to test ARFF file.");
        }
        Dataset dataset = ARFFHandler.loadARFF(testFile, 1);
        Assert.assertNotNull("Dataset should not be null", dataset);
        Assert.assertEquals("Dataset size should be two", 2, dataset.size());
        Instance instance1 = dataset.get(0);
        Assert.assertEquals("First attribute value should be 1.0", 1.0, instance1.value(0), 0.00001);
        Assert.assertEquals("Class value should be 'yes'", "yes", instance1.classValue());
        Instance instance2 = dataset.get(1);
        Assert.assertEquals("First attribute value should be 2.0", 2.0, instance2.value(0), 0.00001);
        Assert.assertEquals("Class value should be 'no'", "no", instance2.classValue());
    }
}