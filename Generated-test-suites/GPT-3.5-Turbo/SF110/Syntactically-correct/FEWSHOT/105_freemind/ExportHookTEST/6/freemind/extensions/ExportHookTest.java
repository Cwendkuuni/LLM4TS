package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class ExportHookTest {
    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        assertNull(exportHook.chooseFile("png", "PNG Image", ".png"));
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        assertNull(ExportHook.chooseImageFile("png", "PNG Image", ".png", null));
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        assertNull(exportHook.getTranslatableResourceString("resourceName"));
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        // No assertion as this method only executes file operations
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        // No assertion as this method only executes file operations
    }
}