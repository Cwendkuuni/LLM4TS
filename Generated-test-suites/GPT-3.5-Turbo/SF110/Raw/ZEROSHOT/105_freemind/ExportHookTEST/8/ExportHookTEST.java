import freemind.extensions.ExportHook;
import freemind.modes.ModeController;
import freemind.view.mindmapview.MapView;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import static org.junit.Assert.*;

public class ExportHookTest {
    
    private ExportHook exportHook;
    
    @Test
    public void testChooseFile() {
        exportHook = new ExportHook();
        assertNotNull(exportHook.chooseFile("type", "description", "extension"));
    }
    
    @Test
    public void testChooseImageFile() {
        ModeController controller = new ModeController();
        Container container = new Container();
        controller.getFrame().setContentPane(container);
        controller.setMap(new ExportHook.MapView());
        assertNotNull(ExportHook.chooseImageFile("type", "description", "extension", controller));
    }
    
    @Test
    public void testGetTranslatableResourceString() {
        exportHook = new ExportHook();
        assertNull(exportHook.getTranslatableResourceString("resourceName"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        exportHook = new ExportHook();
        assertNull(exportHook.createBufferedImage());
    }
    
    @Test
    public void testCopyFromResource() {
        exportHook = new ExportHook();
        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");
    }
    
    @Test
    public void testCopyFromFile() {
        exportHook = new ExportHook();
        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");
    }
    
    public static class MapView implements freemind.view.mindmapview.MapView {
        
        public BufferedImage createImage(int width, int height) {
            return new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
        
        public int getWidth() {
            return 800;
        }
        
        public int getHeight() {
            return 600;
        }
        
        public Rectangle getInnerBounds() {
            return new Rectangle(0, 0, getWidth(), getHeight());
        }
        
        public void preparePrinting() {}
        
        public void print(Graphics graphics) {}
        
        public void endPrinting() {}
    }
}