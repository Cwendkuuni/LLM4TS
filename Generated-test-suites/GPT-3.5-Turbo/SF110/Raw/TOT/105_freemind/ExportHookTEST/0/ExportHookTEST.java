
import org.junit.Before;
import org.junit.Test;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;

import static org.junit.Assert.*;


public class ExportHookTest {

private ExportHook exportHook;

@Before
public void setup() {
exportHook = new ExportHook();
}

@Test
public void testChooseFile_typical() {
String type = "csv";
String description = "Comma-separated values";
String nameExtension = "_new";
File file = exportHook.chooseFile(type, description, nameExtension);
// Check that the file is not null
assertNotNull(file);
// Check that the file has the correct type and extension
assertEquals(type, Tools.getExtension(file.getName()));
assertEquals(nameExtension + "." + type, file.getName());
}

@Test
public void testChooseFile_invalidExtension() {
String type = "jpg";
String description = "JPEG Image";
File file = exportHook.chooseFile(type, description, null);
// Check that the file is not null
assertNotNull(file);
// Check that the file has the correct type and extension
assertEquals(type, Tools.getExtension(file.getName()));
}

@Test
public void testChooseFile_fileExists_overwrite() {
String type = "png";
String description = "Portable Network Graphics";
String nameExtension = "_new";
// Create a temporary file to simulate an existing file
File existingFile = null;
try {
existingFile = File.createTempFile("existingFile", ".png");
} catch (Exception e) {
fail("Error creating temporary file");
}
File chosenFile = exportHook.chooseFile(type, description, nameExtension);
// Check that the chosen file is the existing file
assertEquals(existingFile, chosenFile);
}

@Test
public void testChooseFile_fileExists_noOverwrite() {
String type = "bmp";
String description = "Bitmap Image";
String nameExtension = "_new";
// Create a temporary file to simulate an existing file
File existingFile = null;
try {
existingFile = File.createTempFile("existingFile", ".bmp");
} catch (Exception e) {
fail("Error creating temporary file");
}
// Mock the JOptionPane.showConfirmDialog method to return "No"
JOptionPane.showConfirmDialog(null, "file_already_exists", "file_already_exists", JOptionPane.YES_NO_OPTION);
File chosenFile = exportHook.chooseFile(type, description, nameExtension);
// Check that the chosen file is null
assertNull(chosenFile);
}

@Test
public void testGetTranslatableResourceString_typical() {
String resourceName = "resource_name";
String expected = "Translated Resource Name";
// Mock the ModeController's getText() method to return the expected value
exportHook.getController().setText("%" + resourceName, expected);
// Call the getTranslatableResourceString() method
String actual = exportHook.getTranslatableResourceString(resourceName);
// Check that the actual value is the expected value
assertEquals(expected, actual);
}

@Test
public void testGetTranslatableResourceString_noTranslation() {
String resourceName = "resource_name";
String expected = "resource_name";
// Call the getTranslatableResourceString() method
String actual = exportHook.getTranslatableResourceString(resourceName);
// Check that the actual value is the expected value
assertEquals(expected, actual);
}

@Test
public void testCreateBufferedImage_typical() {
exportHook.getController().setView(new MapView());
BufferedImage image = exportHook.createBufferedImage();
// Check that the image is not null
assertNotNull(image);
// Check that the image dimensions are correct
assertEquals(exportHook.getController().getView().getWidth(), image.getWidth());
assertEquals(exportHook.getController().getView().getHeight(), image.getHeight());
}

@Test
public void testCopyFromResource_typical() {
String prefix = "prefix/";
String fileName = "file.txt";
String destinationDirectory = "destination";
// Create a temporary directory to serve as the destination directory
File destDir = new File(destinationDirectory);
destDir.mkdirs();
// Call the copyFromResource() method
exportHook.copyFromResource(prefix, fileName, destinationDirectory);
// Check that the file was copied to the destination directory
File copiedFile = new File(destinationDirectory, fileName);
assertTrue(copiedFile.exists());
}

@Test
public void testCopyFromFile_typical() {
String dir = "/dir";
String fileName = "file.txt";
String destinationDirectory = "destination";
// Create a temporary directory to serve as the destination directory
File destDir = new File(destinationDirectory);
destDir.mkdirs();
// Create a temporary file to serve as the source file
File sourceFile = null;
try {
sourceFile = File.createTempFile("sourceFile", ".txt", new File(dir));
} catch (Exception e) {
fail("Error creating temporary file");
}
// Call the copyFromFile() method
exportHook.copyFromFile(dir, fileName, destinationDirectory);
// Check that the file was copied to the destination directory
File copiedFile = new File(destinationDirectory, fileName);
assertTrue(copiedFile.exists());
}

@Test
public void testImageFilter_accept_valid() {
exportHook = new ExportHook.ImageFilter("png", "Portable Network Graphics");
File file = new File("image.png");
boolean accepted = exportHook.accept(file);
// Check that the file was accepted
assertTrue(accepted);
}

@Test
public void testImageFilter_accept_invalid() {
exportHook = new ExportHook.ImageFilter("gif", "Graphics Interchange Format");
File file = new File("image.png");
boolean accepted = exportHook.accept(file);
// Check that the file was not accepted
assertFalse(accepted);
}

@Test
public void testImageFilter_getDescription_typical() {
String type = "jpeg";
String description = "JPEG Image";
exportHook = new ExportHook.ImageFilter(type, description);
String expected = description;
String actual = exportHook.getDescription();
// Check that the actual value is the expected value
assertEquals(expected, actual);
}

@Test
public void testImageFilter_getDescription_noDescription() {
String type = "bmp";
String description = null;
exportHook = new ExportHook.ImageFilter(type, description);
String expected = type;
String actual = exportHook.getDescription();
// Check that the actual value is the expected value
assertEquals(expected, actual);
}

}

