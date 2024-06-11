import org.junit.*;
import static org.junit.Assert.*;
import java.io.*;
import javax.swing.*;

public class ExportHookTest {
    private ExportHook exportHook;
    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final ByteArrayOutputStream errContent = new ByteArrayOutputStream();

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        System.setOut(new PrintStream(outContent));
        System.setErr(new PrintStream(errContent));
    }

    @After
    public void tearDown() {
        exportHook = null;
        System.setOut(null);
        System.setErr(null);
    }

    @Test
    public void testChooseFile() {
        File chosenFile = exportHook.chooseFile("jpg", "JPEG Image", ".jpg");
        assertNotNull(chosenFile);
    }

    @Test
    public void testChooseImageFile() {
        File chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", new ModeController());
        assertNotNull(chosenFile);
    }

    @Test
    public void testChooseImageFileWithNoSelection() {
        File chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", new ModeController());
        assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFileWithExistingFile() {
        File chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", new ModeController());
        assertNotNull(chosenFile);

        // Choose the same file again
        chosenFile = exportHook.chooseImageFile("png", "PNG Image", ".png", new ModeController());
        assertNull(chosenFile);
        assertEquals("File already exists: " + chosenFile.toString() + "\n", outContent.toString());
    }

    @Test
    public void testGetTranslatableResourceString() {
        String translatedString = exportHook.getTranslatableResourceString("%file_already_exists");
        assertNotNull(translatedString);
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("images/", "logo.png", "output");
        File copiedFile = new File("output/logo.png");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromNonexistentResource() {
        exportHook.copyFromResource("images/", "nonexistent.png", "output");
        assertEquals("Cannot find resource: images/nonexistent.png\n", outContent.toString());
    }

    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("resources/", "file.txt", "output");
        File copiedFile = new File("output/file.txt");
        assertTrue(copiedFile.exists());
    }

    @Test
    public void testCopyFromNonexistentFile() {
        exportHook.copyFromFile("resources/", "nonexistent.txt", "output");
        assertEquals("Cannot find resource: resources/nonexistent.txt\n", outContent.toString());
    }
}