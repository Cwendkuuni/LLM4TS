import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import javax.swing.*;

import freemind.main.*;
import freemind.modes.*;
import freemind.view.mindmapview.MapView;
import javax.swing.filechooser.*;

public class ExportHookTest {

    private ExportHook exportHook;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile_typical() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";
        File file = exportHook.chooseFile(type, description, nameExtension);
        assertNotNull(file);
        assertTrue(file.getPath().endsWith(".png"));
    }

    @Test
    public void testChooseFile_nullIfNotSelected() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "_export";
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                File file = exportHook.chooseFile(type, description, nameExtension);
                assertNull(file);
            }
        });
    }

    @Test
    public void testChooseImageFile_existingFile_Overwrite() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        ModeController controller = new ModeController();
        controller.getFrame().getContentPane().add(new JPanel());
        File mmFile = new File("C:/path/to/existing_file.jpg");
        controller.getMap().setFile(mmFile);
        File file = exportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNotNull(file);
        assertEquals("C:/path/to/existing_file.jpg", file.getPath());
    }

    @Test
    public void testChooseImageFile_existingFile_CancelOverwrite() {
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_export";
        ModeController controller = new ModeController();
        controller.getFrame().getContentPane().add(new JPanel());
        File mmFile = new File("C:/path/to/existing_file.jpg");
        controller.getMap().setFile(mmFile);
        // Mock JOptionPane.showConfirmDialog() to return JOptionPane.CANCEL_OPTION
        JOptionPane.setRootFrame(controller.getFrame());
        final JOptionPane optionPane = new JOptionPane();
        optionPane.setMessageType(JOptionPane.QUESTION_MESSAGE);
        optionPane.setOptionType(JOptionPane.YES_NO_OPTION);
        optionPane.setInputValue(JOptionPane.NO_OPTION);
        final JDialog dialog = optionPane.createDialog(controller.getFrame(), "file_already_exists");
        javax.swing.SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                dialog.setVisible(true);
            }
        });
        File file = exportHook.chooseImageFile(type, description, nameExtension, controller);
        assertNull(file);
    }

    @Test
    public void testGetTranslatableResourceString_withValidResource() {
        String resourceName = "%file_already_exists";
        String expected = "File Already Exists!";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertEquals(expected, result);
    }

    @Test
    public void testGetTranslatableResourceString_withInvalidResource() {
        String resourceName = "invalid_resource";
        String result = exportHook.getTranslatableResourceString(resourceName);
        assertNull(result);
    }

    @Test
    public void testCreateBufferedImage_withNonNullView() {
        ModeController controller = new ModeController();
        MapView view = new MapView(controller);
        controller.setView(view);
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
        assertEquals(view.getInnerBounds().width, image.getWidth());
        assertEquals(view.getInnerBounds().height, image.getHeight());
    }

    @Test
    public void testCreateBufferedImage_withNullView() {
        ModeController controller = new ModeController();
        BufferedImage image = exportHook.createBufferedImage();
        assertNull(image);
    }

    @Test
    public void testCopyFromResource_existingResource() {
        String prefix = "/resources/";
        String fileName = "test.txt";
        String destinationDirectory = "/path/to/destination/";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory + fileName);
        assertTrue(destinationFile.exists());
    }

    @Test
    public void testCopyFromResource_nonExistingResource() {
        String prefix = "/resources/";
        String fileName = "non_existing_file.txt";
        String destinationDirectory = "/path/to/destination/";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory + fileName);
        assertFalse(destinationFile.exists());
    }

    @Test
    public void testCopyFromFile_existingFile() {
        String dir = "/path/to/source/";
        String fileName = "test.txt";
        String destinationDirectory = "/path/to/destination/";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory + fileName);
        assertTrue(destinationFile.exists());
    }

    @Test
    public void testCopyFromFile_nonExistingFile() {
        String dir = "/path/to/source/";
        String fileName = "non_existing_file.txt";
        String destinationDirectory = "/path/to/destination/";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File destinationFile = new File(destinationDirectory + fileName);
        assertFalse(destinationFile.exists());
    }

    @Test
    public void testImageFilter_accept_withDirectory() {
        File directory = new File("/path/to/directory");
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        assertTrue(filter.accept(directory));
    }

    @Test
    public void testImageFilter_accept_withMatchingExtension() {
        File file = new File("/path/to/image.png");
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        assertTrue(filter.accept(file));
    }

    @Test
    public void testImageFilter_accept_withNonMatchingExtension() {
        File file = new File("/path/to/file.txt");
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        assertFalse(filter.accept(file));
    }

    @Test
    public void testImageFilter_getDescription_withNonNullDescription() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG Image");
        String result = filter.getDescription();
        assertEquals("PNG Image", result);
    }

    @Test
    public void testImageFilter_getDescription_withNullDescription() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", null);
        String result = filter.getDescription();
        assertEquals("png", result);
    }

}