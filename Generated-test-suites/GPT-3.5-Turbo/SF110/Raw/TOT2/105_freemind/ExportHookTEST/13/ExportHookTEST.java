
import org.junit.Test;
import static org.junit.Assert.*;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile("txt", "Text File", ".txt");
        assertNotNull(file);
    }

    @Test
    public void testChooseFileNullValues() {
        ExportHook exportHook = new ExportHook();
        File file = exportHook.chooseFile(null, null, null);
        assertNull(file);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }

    @Test
    public void testCopyFromFileNotFound() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("dir", "file.txt", "destination");
        // Check log for error message
    }
}
