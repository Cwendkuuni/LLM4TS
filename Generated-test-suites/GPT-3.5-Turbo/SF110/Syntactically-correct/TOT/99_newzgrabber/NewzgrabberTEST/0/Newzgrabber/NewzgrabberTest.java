import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;

public class NewzgrabberTest {

    private Newzgrabber newzgrabber;

    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        // Assert that the Newzdirectory has been set correctly
        assertEquals("Newzgrabber directory is not correct", System.getProperty("user.dir"), Newzgrabber.Newzdirectory);
    }

    @Test
    public void testFindDirectory() {
        String directory = Newzgrabber.findDirectory(Newzgrabber.JarFilename);
        // Assert that the directory is not null and it exists
        assertNotNull("Directory is null", directory);
        File file = new File(directory);
        assertTrue("Directory does not exist", file.exists());
    }

    @Test
    public void testUpdateIni() {
        String inifile = "test.ini";
        String prop = "TestProp";
        String key = "TestKey";
        String value = "TestValue";
        newzgrabber.updateIni(inifile, prop, key, value);
        // Assert that the ini file has been updated correctly
        try {
            BufferedReader reader = new BufferedReader(new FileReader(inifile));
            String line;
            boolean isUpdated = false;
            while ((line = reader.readLine()) != null) {
                if (line.contains(key) && line.contains(value)) {
                    isUpdated = true;
                    break;
                }
            }
            assertTrue("Ini file has not been updated correctly", isUpdated);
        } catch (IOException e) {
            fail("Error reading ini file");
        }
    }

    @After
    public void tearDown() {
        // Clean up any files created during the test
        File testIni = new File("test.ini");
        testIni.delete();
    }
}