import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

public class MenuTest {
    private Menu menu;
    private GUI dvdGui;

    @Before
    public void setup() {
        dvdGui = new GUI();
        menu = new Menu("Title", "PicPath", "AudioPath", "TextFilePath", true, dvdGui);
    }

    @Test
    public void testMenuInitialization() {
        assertTrue(menu.init());
        assertFalse(menu.m_Error);
    }

    @Test
    public void testRun() {
        menu.run();
        // assert the correct method is called
    }

    @Test
    public void testDVDMainMenu() {
        // Prepare test directories and file
        File outputDir = new File(dvdGui.strOutputDir);
        outputDir.mkdirs();
        File vobFile = new File(dvdGui.strOutputDir + "/[testfile].vob");
        try {
            vobFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Test case
        menu.DVDMainMenu();

        // assert the correct actions are performed
    }

    @Test
    public void testCreateXML() {
        // Prepare test directories and file
        File outputDir = new File(dvdGui.strOutputDir);
        outputDir.mkdirs();
        File xmlFile = new File(dvdGui.strOutputDir + "/vmgm.xml");

        // Test case
        assertFalse(menu.createXML());

        // assert the correct actions are performed
        assertTrue(xmlFile.exists());
        assertTrue(xmlFile.isFile());
    }
}