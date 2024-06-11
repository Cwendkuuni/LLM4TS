
package freemind.extensions;

import static org.junit.Assert.*;
import org.junit.*;

import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

import javax.swing.JOptionPane;

import freemind.modes.ModeController;
import freemind.modes.ModeControllerHookAdapter;
import freemind.modes.Scroller;
import freemind.resources.Resources;
import freemind.tools.Tools;
import freemind.view.mindmapview.FreeMindFileDialog;
import freemind.view.mindmapview.MapView;

public class ExportHookTest {
  
    private ExportHook exportHook;
  
    @Before
    public void setup() {
        exportHook = new ExportHook();
    }

    @Test
    public void testChooseFile() {
        File expected = new File("test.png");
        File actual = exportHook.chooseFile("png", "PNG Image File", ".png");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        ModeControllerHookAdapter.ImageFilter filter = new ModeControllerHookAdapter.ImageFilter("png", "PNG Image File");
        Container container = new Container();
        
        FreeMindFileDialog chooser = controller.getFileChooser(filter);
        chooser.setApproveButtonText("Save");
        chooser.setMultiSelectionEnabled(false);
        chooser.setSelectedFile(new File("png_image.png"));
        
        File expected = new File("test.png");
        File actual = exportHook.chooseImageFile("png", "PNG Image File", ".png", controller);
        assertEquals(expected, actual);
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        String expected = "Title";
        String actual = exportHook.getTranslatableResourceString("Title");
        assertEquals(expected, actual);
    }
    
    @Test
    public void testCreateBufferedImage() {
        MapView view = new MapView();
        Rectangle innerBounds = new Rectangle(0, 0, 100, 100);
        view.setBounds(innerBounds);
        
        exportHook.setController(new ModeController(view, new Scroller(view)));
        BufferedImage expected = view.createImage(view.getWidth(), view.getHeight());
        exportHook.createBufferedImage();
        BufferedImage actual = exportHook.view.getImage();
        
        assertNotEquals(expected, actual);
    }
    
    @Test
    public void testCopyFromResource() {
        String prefix = "/";
        String fileName = "test.png";
        String destinationDirectory = "src/main/resources/";
        
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File expected = new File(destinationDirectory + fileName);
        assertTrue(expected.exists());
    }
    
    @Test
    public void testCopyFromFile() {
        String dir = "src/main/resources/";
        String fileName = "test.png";
        String destinationDirectory = "src/main/resources/";
        
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File expected = new File(destinationDirectory + fileName);
        assertTrue(expected.exists());
    }
    
}
