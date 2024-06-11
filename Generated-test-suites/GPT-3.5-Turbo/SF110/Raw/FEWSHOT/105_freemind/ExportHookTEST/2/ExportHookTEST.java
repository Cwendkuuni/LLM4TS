
package freemind.extensions;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import freemind.modes.ModeController;
import freemind.view.mindmapview.*;
import java.awt.Container;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import javax.swing.*;
import javax.swing.filechooser.*;

import org.junit.Before;
import org.junit.Test;

public class ExportHookTest {
    private ExportHook exportHook;
    private ModeController modeController;
    private MapView mapView;
    private Container frameContentPane;
    private JFileChooser fileChooser;
    private File mmFile;
    
    @Before
    public void setUp() {
        exportHook = new ExportHook();
        modeController = mock(ModeController.class);
        mapView = mock(MapView.class);
        frameContentPane = mock(Container.class);
        fileChooser = mock(JFileChooser.class);
        mmFile = mock(File.class);
        
        when(modeController.getFrame()).thenReturn(frameContentPane);
        when(modeController.getView()).thenReturn(mapView);
        when(modeController.getMap()).thenReturn(mapView);
        when(mapView.getWidth()).thenReturn(100);
        when(mapView.getHeight()).thenReturn(100);
        when(mapView.createImage(anyInt(), anyInt())).thenReturn(new BufferedImage(100, 100, BufferedImage.TYPE_INT_ARGB));
        when(mapView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 100));
        
        exportHook.setController(modeController);
    }
    
    @Test
    public void testChooseImageFile() {
        ImageFilter filter = new ImageFilter("png", "PNG Files");
        
        when(modeController.getFileChooser(eq(filter))).thenReturn(fileChooser);
        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(fileChooser.showSaveDialog(frameContentPane)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("/path/to/save/file.png"));
        
        File chosenFile = ExportHook.chooseImageFile("png", "PNG Files", null, modeController);
        
        assertEquals("/path/to/save/file.png", chosenFile.getPath());
    }
    
    @Test
    public void testChooseImageFile_withWrongExtension() {
        ImageFilter filter = new ImageFilter("png", "PNG Files");
        
        when(modeController.getFileChooser(eq(filter))).thenReturn(fileChooser);
        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(fileChooser.showSaveDialog(frameContentPane)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("/path/to/wrongextension/file.jpg"));
        
        File chosenFile = ExportHook.chooseImageFile("png", "PNG Files", null, modeController);
        
        assertEquals("/path/to/wrongextension/file.png", chosenFile.getPath());
    }
    
    @Test
    public void testChooseImageFile_withExistingFile() {
        ImageFilter filter = new ImageFilter("png", "PNG Files");
        
        when(modeController.getFileChooser(eq(filter))).thenReturn(fileChooser);
        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(fileChooser.showSaveDialog(frameContentPane)).thenReturn(JFileChooser.APPROVE_OPTION);
        when(fileChooser.getSelectedFile()).thenReturn(new File("/path/to/existing/file.png"));
        when(Resources.getInstance().logException(any())).thenReturn(null);
        when(JOptionPane.showConfirmDialog(frameContentPane, "File already exists: /path/to/existing/file.png", "File already exists: /path/to/existing/file.png", JOptionPane.YES_NO_OPTION)).thenReturn(JOptionPane.YES_OPTION);
        
        File chosenFile = ExportHook.chooseImageFile("png", "PNG Files", null, modeController);
        
        assertEquals("/path/to/existing/file.png", chosenFile.getPath());
    }
    
    @Test
    public void testChooseImageFile_cancelSelection() {
        ImageFilter filter = new ImageFilter("png", "PNG Files");
        
        when(modeController.getFileChooser(eq(filter))).thenReturn(fileChooser);
        when(modeController.getMap().getFile()).thenReturn(mmFile);
        when(mmFile.getAbsolutePath()).thenReturn("/path/to/file.mm");
        when(fileChooser.showSaveDialog(frameContentPane)).thenReturn(JFileChooser.CANCEL_OPTION);
        
        File chosenFile = ExportHook.chooseImageFile("png", "PNG Files", null, modeController);
        
        assertNull(chosenFile);
    }
    
    @Test
    public void testStripLeadingHyphens_nullString() {
        assertNull(Util.stripLeadingHyphens(null));
    }
    
    // Additional Test Cases for Util Class
    @Test
    public void testStripLeadingAndTrailingQuotes() {
        assertEquals("foo", Util.stripLeadingAndTrailingQuotes("\"foo\""));
        assertEquals("foo \"bar\"", Util.stripLeadingAndTrailingQuotes("foo \"bar\""));
        assertEquals("\"foo\" bar", Util.stripLeadingAndTrailingQuotes("\"foo\" bar"));
        assertEquals("\"foo\" and \"bar\"", Util.stripLeadingAndTrailingQuotes("\"foo\" and \"bar\""));
        assertEquals("", Util.stripLeadingAndTrailingQuotes("\""));
    }
    
    @Test
    public void testStripLeadingHyphens() {
        assertEquals("f", Util.stripLeadingHyphens("-f"));
        assertEquals("foo", Util.stripLeadingHyphens("--foo"));
        assertEquals("-foo", Util.stripLeadingHyphens("---foo"));
        assertNull(Util.stripLeadingHyphens(null));
    }

    @Test
    public void testGetTranslatableResourceString() {
        assertEquals("Resource Text", exportHook.getTranslatableResourceString("Resource Text"));
        assertEquals("Translated Text", exportHook.getTranslatableResourceString("%Translated Text"));
    }
    
    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        
        assertEquals(100, image.getWidth());
        assertEquals(100, image.getHeight());
    }
    
    @Test
    public void testCopyFromResource_ResourceFound() throws Exception {
        exportHook.copyFromResource("prefix/", "fileName", "destinationDirectory");
        
        verify(exportHook.logger).finest("searching for prefix/fileName");
        verify(exportHook).getResource("prefix/fileName");
        verify(Resources.getInstance()).logException(isA(Exception.class));
    }
    
    @Test
    public void testCopyFromResource_ResourceNotFound() {
        exportHook.copyFromResource("prefix/", "fileName", "destinationDirectory");
        
        verify(exportHook.logger).finest("searching for prefix/fileName");
        verify(exportHook).getResource("prefix/fileName");
        verify(exportHook.logger).severe("Cannot find resource: prefix/fileName");
    }
    
    @Test
    public void testCopyFromFile_FileNotFound() {
        exportHook.copyFromFile("dir/", "fileName", "destinationDirectory");
        
        verify(exportHook.logger).finest("searching for dir/fileName");
        verify(exportHook.logger).severe("Cannot find resource: dir/fileName");
        verify(Resources.getInstance()).logException(isA(Exception.class));
    }
}
