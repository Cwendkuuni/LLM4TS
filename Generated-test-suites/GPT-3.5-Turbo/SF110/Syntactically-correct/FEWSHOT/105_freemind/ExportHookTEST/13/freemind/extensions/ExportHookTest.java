package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Before;
import org.junit.Test;

import java.io.File;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setup() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        assertNull(exportHook.chooseFile("type", "description", "nameExtension"));
    }

    @Test
    public void testChooseImageFile() {
        assertNull(ExportHook.chooseImageFile("type", "description", "nameExtension", null));
    }

    @Test
    public void testGetTranslatableResourceString() {
        assertNull(exportHook.getTranslatableResourceString("resourceName"));
    }

    @Test
    public void testCreateBufferedImage() {
        assertNull(exportHook.createBufferedImage());
    }

    @Test
    public void testCopyFromResource() {
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
        // Verify the copied file exists in the designated directory
        File copiedFile = new File("destinationDirectory/fileName");
        assertEquals(true, copiedFile.exists());
    }

    @Test
    public void testCopyFromFile() {
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
        // Verify the copied file exists in the designated directory
        File copiedFile = new File("destinationDirectory/fileName");
        assertEquals(true, copiedFile.exists());
    }
}