import org.junit.Test;
import static org.junit.Assert.*;
import org.mockito.*;

import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.net.URL;
import java.text.MessageFormat;

public class ExportHookTest {

    private ExportHook exportHook;
    private ModeController mockedController;
    private MapView mockedView;
    private Frame mockedFrame;
    private ModeControllerHookAdapter.Resources mockedResources;
    private Container mockedContainer;
    private FreeMindFileDialog mockedFileChooser;
    private File mockedFile;
    private InputStream mockedInputStream;
    private OutputStream mockedOutputStream;

    @Before
    public void setUp() {
        exportHook = new ExportHook();
        mockedController = Mockito.mock(ModeController.class);
        mockedView = Mockito.mock(MapView.class);
        mockedFrame = Mockito.mock(Frame.class);
        mockedResources = Mockito.mock(ModeControllerHookAdapter.Resources.class);
        mockedContainer = Mockito.mock(Container.class);
        mockedFileChooser = Mockito.mock(FreeMindFileDialog.class);
        mockedFile = Mockito.mock(File.class);
        mockedInputStream = Mockito.mock(InputStream.class);
        mockedOutputStream = Mockito.mock(OutputStream.class);

        Mockito.when(mockedController.getFrame()).thenReturn(mockedFrame);
        Mockito.when(mockedController.getFileChooser(Mockito.any(FileFilter.class))).thenReturn(mockedFileChooser);
        Mockito.when(mockedController.getMap().getFile()).thenReturn(mockedFile);
        Mockito.when(mockedFile.getAbsolutePath()).thenReturn("mmFileAbsolutePath");
        Mockito.when(mockedFile.getName()).thenReturn("mmFileName");
        Mockito.when(mockedFile.exists()).thenReturn(true);
        Mockito.when(mockedFile.getParent()).thenReturn("fileParent");
        Mockito.when(Tools.getExtension(Mockito.any(String.class))).thenReturn("ext");
        Mockito.when(mockedFileChooser.showSaveDialog(mockedContainer)).thenReturn(0);
        Mockito.when(mockedFileChooser.getSelectedFile()).thenReturn(mockedFile);
        Mockito.when(mockedResources.getString(Mockito.any(String.class))).thenReturn("resourceString");

        exportHook.setController(mockedController);
    }

    @After
    public void tearDown() {
        exportHook = null;
        mockedController = null;
        mockedView = null;
        mockedFrame = null;
        mockedResources = null;
        mockedContainer = null;
        mockedFileChooser = null;
        mockedFile = null;
        mockedInputStream = null;
        mockedOutputStream = null;
    }

    @Test
    public void testChooseFile() {
        File result = exportHook.chooseFile("type", "description", "nameExtension");

        Mockito.verify(mockedController).getFileChooser(Mockito.any(FileFilter.class));
        Mockito.verify(mockedController.getMap()).getFile();
        Mockito.verify(mockedFileChooser).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(mockedFileChooser).showSaveDialog(mockedContainer);
        Mockito.verify(mockedFileChooser).getSelectedFile();

        assertNotNull(result);
        assertEquals(mockedFile, result);
    }

    @Test
    public void testChooseImageFile() {
        File result = exportHook.chooseImageFile("type", "description", "nameExtension", mockedController);

        Mockito.verify(mockedController.getFrame()).getContentPane();
        Mockito.verify(mockedController.getFileChooser(Mockito.any(FileFilter.class)));
        Mockito.verify(mockedController.getMap()).getFile();
        Mockito.verify(mockedFileChooser).setSelectedFile(Mockito.any(File.class));
        Mockito.verify(mockedFileChooser).showSaveDialog(mockedContainer);
        Mockito.verify(mockedFileChooser).getSelectedFile();
        Mockito.verify(mockedFile).getName();
        Mockito.verify(mockedFile.getParent()).getName();
        Mockito.verify(Tools.getExtension(Mockito.any(String.class))).getName();
        Mockito.verify(mockedFile.exists()).getName();
        Mockito.verify(mockedLogger).severe(Mockito.any(String.class));

        assertNotNull(result);
        assertEquals(mockedFile, result);
    }

    @Test
    public void testGetTranslatableResourceString() {
        String result = exportHook.getTranslatableResourceString("resourceName");

        Mockito.verify(mockedController).getText("resourceString");

        assertNotNull(result);
        assertEquals("resourceString", result);
    }

    @Test
    public void testCreateBufferedImage() {
        Mockito.when(mockedController.getView()).thenReturn(mockedView);
        Mockito.when(mockedView.getWidth()).thenReturn(100);
        Mockito.when(mockedView.getHeight()).thenReturn(200);
        Mockito.when(mockedView.getInnerBounds()).thenReturn(new Rectangle(0, 0, 100, 200));
        Mockito.when(mockedView.createImage(Mockito.anyInt(), Mockito.anyInt())).thenReturn(new BufferedImage(100, 200, BufferedImage.TYPE_INT_ARGB));
        Mockito.when(mockedView.print(Mockito.any(Graphics.class))).thenReturn(true);
        Mockito.when(mockedView.getGraphics()).thenReturn(Mockito.mock(Graphics.class));

        BufferedImage result = exportHook.createBufferedImage();

        Mockito.verify(mockedController).getView();
        Mockito.verify(mockedView).preparePrinting();
        Mockito.verify(mockedView).getInnerBounds();
        Mockito.verify(mockedView).createImage(Mockito.anyInt(), Mockito.anyInt());
        Mockito.verify(mockedView).print(Mockito.any(Graphics.class));
        Mockito.verify(mockedView).endPrinting();

        assertNotNull(result);
        assertEquals(100, result.getWidth());
        assertEquals(200, result.getHeight());
    }

    @Test
    public void testCopyFromResource() throws Exception {
        Mockito.when(exportHook.getResource(Mockito.anyString())).thenReturn(new URL("http://www.example.com/resource"));
        Mockito.when(mockedResources.getString(Mockito.anyString())).thenReturn("%text");

        exportHook.copyFromResource("prefix", "fileName", "destinationDirectory");

        Mockito.verify(exportHook).getResource("prefix" + "fileName");
        Mockito.verify(mockedLogger).severe(Mockito.anyString());
        Mockito.verify(mockedResources).logException(Mockito.any(Exception.class));
    }

    @Test
    public void testCopyFromFile() throws Exception {
        File dir = new File("dir");
        Mockito.when(mockedResources.getString(Mockito.anyString())).thenReturn("%text");
        Mockito.when(mockedFile.getParent()).thenReturn("dir");

        exportHook.copyFromFile("dir", "fileName", "destinationDirectory");

        Mockito.verify(mockedLogger).severe(Mockito.anyString());
    }

    @Test
    public void testImageFilterAccept() {
        File mockedFile = Mockito.mock(File.class);
        Mockito.when(mockedFile.isDirectory()).thenReturn(true);
        Mockito.when(mockedFile.getName()).thenReturn("file");
        Mockito.when(mockedFile.exists()).thenReturn(true);
        Mockito.when(Tools.getExtension(Mockito.anyString())).thenReturn("ext");

        FileFilter imageFilter = exportHook.new ImageFilter("ext", "description");
        boolean accepted = imageFilter.accept(mockedFile);
        assertTrue(accepted);

        accepted = imageFilter.accept(mockedFile);
        assertTrue(accepted);

        Mockito.when(mockedFile.isDirectory()).thenReturn(false);
        Mockito.when(Tools.getExtension(Mockito.anyString())).thenReturn("anotherExt");

        accepted = imageFilter.accept(mockedFile);
        assertFalse(accepted);
    }

    @Test
    public void testImageFilterGetDescription() {
        FileFilter imageFilter = exportHook.new ImageFilter("ext", "description");

        String description = imageFilter.getDescription();
        assertNotNull(description);
        assertEquals("description", description);

        FileFilter anotherImageFilter = exportHook.new ImageFilter("ext", null);

        description = anotherImageFilter.getDescription();
        assertNotNull(description);
        assertEquals("ext", description);
    }
}