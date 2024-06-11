
import org.junit.Assert;
import org.junit.Test;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExportHookTest {

    ExportHook exportHook = new ExportHook();

    @Test
    public void testChooseFile() {
        String type = "png";
        String description = "PNG Image";
        String nameExtension = ".png";
        File file = exportHook.chooseFile(type, description, nameExtension);
        Assert.assertNotNull(file);
        Assert.assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testChooseImageFile() {
        String type = "jpg";
        String description = "JPG Image";
        String nameExtension = ".jpg";
        ModeController controller = new ModeController();
        File file = ExportHook.chooseImageFile(type, description, nameExtension, controller);
        Assert.assertNotNull(file);
        Assert.assertEquals(type, Tools.getExtension(file.getName()));
    }

    @Test
    public void testGetTranslatableResourceString() {
        String resourceName = "resource_name";
        String result = exportHook.getTranslatableResourceString(resourceName);
        Assert.assertNotNull(result);
        Assert.assertNotEquals("%", result.substring(0, 1));
    }

    @Test
    public void testCreateBufferedImage() {
        BufferedImage image = exportHook.createBufferedImage();
        Assert.assertNotNull(image);
    }

    @Test
    public void testCopyFromResource() {
        String prefix = "prefix/";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromResource(prefix, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        Assert.assertTrue(file.exists());
        file.delete();
    }

    @Test
    public void testCopyFromFile() {
        String dir = "dir";
        String fileName = "test.txt";
        String destinationDirectory = "destination";
        exportHook.copyFromFile(dir, fileName, destinationDirectory);
        File file = new File(destinationDirectory + "/" + fileName);
        Assert.assertTrue(file.exists());
        file.delete();
    }
}
