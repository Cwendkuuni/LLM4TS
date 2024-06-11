import org.junit.*;
import java.io.*;

public class ARFFHandlerTest {
    private static final String TEST_FILE_PATH = "test.arff";

    @Test
    public void testLoadARFF() throws FileNotFoundException {
        File testFile = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(testFile);
        Assert.assertNotNull(dataset);
    }

    @Test
    public void testLoadARFFWithClassIndex() throws FileNotFoundException {
        File testFile = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(testFile, 2);
        Assert.assertNotNull(dataset);
    }

    @Test(expected = FileNotFoundException.class)
    public void testLoadARFFWithNonExistentFile() throws FileNotFoundException {
        File testFile = new File("nonexistent.arff");
        ARFFHandler.loadARFF(testFile);
    }

    @Test
    public void testLoadARFFWithDataMode() throws FileNotFoundException {
        File testFile = new File(TEST_FILE_PATH);
        Dataset dataset = ARFFHandler.loadARFF(testFile);
        Assert.assertEquals(5, dataset.size());
    }

    // Add more edge cases and exception scenarios as needed

    // ...

    @AfterClass
    public static void tearDown() {
        File testFile = new File(TEST_FILE_PATH);
        testFile.delete();
    }
}


@BeforeClass
public class ARFFHandlerTestSetup {
    @BeforeClass
    public static void setUp() throws IOException {
        String testData = "@relation test\n\n@attribute x numeric\n@attribute y numeric\n@attribute class {A,B,C}\n\n@data\n1,2,A\n3,4,B\n5,6,C";
        File testFile = new File(ARFFHandlerTest.TEST_FILE_PATH);
        FileWriter writer = new FileWriter(testFile);
        writer.write(testData);
        writer.close();
    }
}
