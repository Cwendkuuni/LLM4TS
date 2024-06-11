
import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

public class MenuTest {
    
    private Menu menu;
    
    @Before
    public void setUp() {
        menu = new Menu();
    }
    
    @Test
    public void testInit() {
        Assert.assertTrue(menu.init());
    }
    
    @Test
    public void testDVDMainMenu() {
        menu.strPicPath = "picPath";
        menu.strAudioPath = "audioPath";
        menu.strTextFilePath = "textFilePath";
        menu.pal_menu = true;
        menu.m_GUI = new GUI();
        
        // mock runtime execution result
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
        
        menu.DVDMainMenu();
        
        String expectedOutput = "dvd-menu -c -D -n 'null' -o null -a audioPath -b picPath -p 2>&1\n";
        Assert.assertEquals(expectedOutput, outputStream.toString());
    }
    
    @Test
    public void testCreateXML() {
        menu.m_GUI = new GUI();
        menu.m_GUI.strOutputDir = "outputDir";
        
        Assert.assertTrue(menu.createXML());
        
        File xmlFile = new File("outputDir/vmgm.xml");
        Assert.assertTrue(xmlFile.exists());
        
        // cleanup
        xmlFile.delete();
    }
}
