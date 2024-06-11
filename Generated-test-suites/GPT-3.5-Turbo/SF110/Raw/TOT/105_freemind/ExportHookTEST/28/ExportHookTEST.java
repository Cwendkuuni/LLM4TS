import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import javax.swing.filechooser.FileFilter;
import java.io.File;
import java.io.IOException;
import freemind.extensions.ExportHook;

public class ExportHookTest {

    @Test
    public void testChooseFile_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        String type = "jpg";
        String description = "JPEG Image";
        String nameExtension = "_exported";
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        
        Assert.assertNotNull(chosenFile);
        Assert.assertEquals(type, Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testChooseFile_nullReturnValue() {
        ExportHook exportHook = new ExportHook();
        String type = "png";
        String description = "PNG Image";
        String nameExtension = "";
        
        File chosenFile = exportHook.chooseFile(type, description, nameExtension);
        
        Assert.assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFile_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        String type = "bmp";
        String description = "Bitmap Image";
        String nameExtension = null;
        ModeController controller = new ModeController();

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        
        Assert.assertNotNull(chosenFile);
        Assert.assertEquals(type, Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testChooseImageFile_noSelection() {
        ExportHook exportHook = new ExportHook();
        String type = "gif";
        String description = "GIF Image";
        String nameExtension = "_exported";
        ModeController controller = new ModeController();

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        
        Assert.assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFile_fileExistsOverwrite() {
        ExportHook exportHook = new ExportHook();
        String type = "tif";
        String description = "TIFF Image";
        String nameExtension = "";
        ModeController controller = new ModeController();
        File mmFile = new File("test.mm");
        controller.getMap().setFile(mmFile);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("existing_file.tif"));
        controller.setFileChooser(fileChooser);

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        
        Assert.assertNotNull(chosenFile);
        Assert.assertEquals(type, Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testChooseImageFile_fileExistsDoNotOverwrite() {
        ExportHook exportHook = new ExportHook();
        String type = "tif";
        String description = "TIFF Image";
        String nameExtension = "";
        ModeController controller = new ModeController();
        File mmFile = new File("test.mm");
        controller.getMap().setFile(mmFile);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("existing_file.tif"));
        fileChooser.setReturnValut(JFileChooser.CANCEL_OPTION);
        controller.setFileChooser(fileChooser);

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        
        Assert.assertNull(chosenFile);
    }

    @Test
    public void testChooseImageFile_extensionNotMatching() {
        ExportHook exportHook = new ExportHook();
        String type = "jpeg";
        String description = "JPEG Image";
        String nameExtension = "";
        ModeController controller = new ModeController();
        File mmFile = new File("test.mm");
        controller.getMap().setFile(mmFile);
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setSelectedFile(new File("existing_file.tif"));
        controller.setFileChooser(fileChooser);

        File chosenFile = exportHook.chooseImageFile(type, description, nameExtension, controller);
        
        Assert.assertNotNull(chosenFile);
        Assert.assertEquals("jpeg", Tools.getExtension(chosenFile.getName()));
    }

    @Test
    public void testGetTranslatableResourceString_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resource.name";

        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
        
        Assert.assertNotNull(translatableResourceString);
        Assert.assertEquals("translated_resource", translatableResourceString);
    }

    @Test
    public void testGetTranslatableResourceString_noTranslation() {
        ExportHook exportHook = new ExportHook();
        String resourceName = "resource.name";

        String translatableResourceString = exportHook.getTranslatableResourceString(resourceName);
        
        Assert.assertNotNull(translatableResourceString);
        Assert.assertEquals(resourceName, translatableResourceString);
    }

    @Test
    public void testCreateBufferedImage_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();
        MapView mapView = new MapView();
        controller.setView(mapView);

        BufferedImage bufferedImage = exportHook.createBufferedImage();
        
        Assert.assertNotNull(bufferedImage);
        Assert.assertEquals(mapView.getWidth(), bufferedImage.getWidth());
        Assert.assertEquals(mapView.getHeight(), bufferedImage.getHeight());
    }

    @Test
    public void testCreateBufferedImage_nullView() {
        ExportHook exportHook = new ExportHook();
        ModeController controller = new ModeController();

        BufferedImage bufferedImage = exportHook.createBufferedImage();
        
        Assert.assertNull(bufferedImage);
    }

    @Test
    public void testCopyFromResource_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        String prefix = "resource.prefix";
        String fileName = "file.txt";
        String destinationDirectory = "destination/";
        exportHook.setLogger(new Logger());

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + fileName);
        Assert.assertTrue(copiedFile.exists());
    }

    @Test(expected = IOException.class)
    public void testCopyFromResource_fileNotFound() {
        ExportHook exportHook = new ExportHook();
        String prefix = "resource.prefix";
        String fileName = "non_existing_file.txt";
        String destinationDirectory = "destination/";
        exportHook.setLogger(new Logger());

        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
    }

    @Test
    public void testCopyFromFile_typicalUseCase() {
        ExportHook exportHook = new ExportHook();
        String dir = "resources/";
        String fileName = "file.txt";
        String destinationDirectory = "destination/";
        exportHook.setLogger(new Logger());

        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        
        File copiedFile = new File(destinationDirectory + fileName);
        Assert.assertTrue(copiedFile.exists());
    }

    @Test(expected = IOException.class)
    public void testCopyFromFile_fileNotFound() {
        ExportHook exportHook = new ExportHook();
        String dir = "resources/";
        String fileName = "non_existing_file.txt";
        String destinationDirectory = "destination/";
        exportHook.setLogger(new Logger());

        exportHook.copyFromFile(dir, fileName, destinationDirectory);
    }

    @Test
    public void testImageFilter_acceptDirectory() {
        String type = "jpg";
        String description = "JPEG Image";
        File directory = new File("images");
        FileFilter imageFilter = new ExportHook.ImageFilter(type, description);

        boolean accept = imageFilter.accept(directory);
        
        Assert.assertTrue(accept);
    }

    @Test
    public void testImageFilter_acceptFileWithMatchingExtension() {
        String type = "png";
        String description = "PNG Image";
        File imageFile = new File("image.png");
        FileFilter imageFilter = new ExportHook.ImageFilter(type, description);

        boolean accept = imageFilter.accept(imageFile);
        
        Assert.assertTrue(accept);
    }

    @Test
    public void testImageFilter_acceptFileWithNonMatchingExtension() {
        String type = "jpeg";
        String description = "JPEG Image";
        File imageFile = new File("image.png");
        FileFilter imageFilter = new ExportHook.ImageFilter(type, description);

        boolean accept = imageFilter.accept(imageFile);
        
        Assert.assertFalse(accept);
    }

    @Test
    public void testImageFilter_getDescription_withDescription() {
        String type = "bmp";
        String description = "Bitmap Image";
        FileFilter imageFilter = new ExportHook.ImageFilter(type, description);

        String filterDescription = imageFilter.getDescription();
        
        Assert.assertEquals(description, filterDescription);
    }

    @Test
    public void testImageFilter_getDescription_withNullDescription() {
        String type = "bmp";
        String description = null;
        FileFilter imageFilter = new ExportHook.ImageFilter(type, description);

        String filterDescription = imageFilter.getDescription();
        
        Assert.assertEquals(type, filterDescription);
    }

}