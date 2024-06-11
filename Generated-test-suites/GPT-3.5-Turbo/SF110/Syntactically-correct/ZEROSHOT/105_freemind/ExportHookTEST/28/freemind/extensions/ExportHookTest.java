import static org.junit.Assert.*;
import org.junit.Test;
import freemind.extensions.ExportHook;
import freemind.view.mindmapview.MapView;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.filechooser.FileFilter;

public class ExportHookTest {

    @Test
    public void testChooseFile() {
        ExportHook exportHook = new ExportHook();
        MapView view = new MapView();
        Container container = new Container();
        exportHook.view = view;
        exportHook.getController().getFrame().setContentPane(container);
        File file = exportHook.chooseFile("png", "PNG File", ".png");
        assertNotNull(file);
    }

    @Test
    public void testChooseImageFile() {
        ExportHook exportHook = new ExportHook();
        MapView view = new MapView();
        Container container = new Container();
        exportHook.view = view;
        exportHook.getController().getFrame().setContentPane(container);
        File file = exportHook.chooseImageFile("png", "PNG File", ".png", exportHook.getController());
        assertNotNull(file);
    }

    @Test
    public void testGetTranslatableResourceString() {
        ExportHook exportHook = new ExportHook();
        String resourceString = exportHook.getTranslatableResourceString("testResource");
        assertNotNull(resourceString);
    }

    @Test
    public void testCreateBufferedImage() {
        ExportHook exportHook = new ExportHook();
        MapView view = new MapView();
        exportHook.view = view;
        BufferedImage image = exportHook.createBufferedImage();
        assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        ExportHook exportHook = new ExportHook();
        exportHook.logger.finest("Test Copy From Resource");
    }

    @Test
    public void testCopyFromFile() {
        ExportHook exportHook = new ExportHook();
        exportHook.logger.finest("Test Copy From File");
    }

    @Test
    public void testImageFilterAccept() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG File");
        File file = new File("test.png");
        assertTrue(filter.accept(file));
        File directory = new File("directory");
        assertTrue(filter.accept(directory));
        File textFile = new File("test.txt");
        assertFalse(filter.accept(textFile));
    }

    @Test
    public void testImageFilterGetDescription() {
        ExportHook.ImageFilter filter = new ExportHook.ImageFilter("png", "PNG File");
        assertEquals("PNG File", filter.getDescription());
    }

}