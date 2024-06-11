
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;

public class MenuTest {
    private Menu menu;
    private GUI gui;
    private String outputDir;

    @Before
    public void setUp() {
        menu = new Menu();
        gui = new GUI();
        outputDir = System.getProperty("user.dir") + File.separator + "output";
        gui.setOutputDir(outputDir);
        menu.m_GUI = gui;
    }

    @After
    public void tearDown() {
        menu = null;
        gui = null;
        outputDir = null;
    }

    @Test
    public void testInit() {
        boolean result = menu.init();
        Assert.assertFalse(result);
    }

    @Test
    public void testRun() {
        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        menu.run();
        String expectedOutput = "DVD MainMenu\n";

        Assert.assertEquals(expectedOutput, outContent.toString());
        System.setOut(System.out);
    }

    @Test
    public void testDVDMainMenu() {
        // Test case with valid inputs
        menu.strTitle = "Title";
        menu.strTextFilePath = "";
        menu.pal_menu = false;
        menu.m_GUI.lblAuthor = new JLabel();
        menu.m_GUI.lblAuthorProg = new JLabel();
        menu.m_GUI.prgAuthor = new JProgressBar();
        menu.m_GUI.prgAuthor.setIndeterminate(true);

        ByteArrayOutputStream outContent = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent));

        menu.DVDMainMenu();

        String expectedOutput = "DVD MainMenu -c -D -n 'Title' -o " + outputDir;

        Assert.assertTrue(outContent.toString().contains(expectedOutput));
        System.setOut(System.out);

        // Test case with text file present
        menu.strTextFilePath = "path/to/file.txt";
        menu.video_files = new String[]{"video1.vob", "video2.vob"};
        menu.titles = new String[]{"Title1", "Title2"};

        ByteArrayOutputStream outContent2 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent2));

        menu.DVDMainMenu();

        String expectedOutput2 = "DVD MainMenu -c -D -n 'Title' -o " + outputDir + " -t Title1 -f " + outputDir + "/video1.vob -t Title2 -f " + outputDir + "/video2.vob";

        Assert.assertTrue(outContent2.toString().contains(expectedOutput2));
        System.setOut(System.out);

        // Test case with text file not formatted correctly
        menu.titles = new String[]{"Title1", "BLANK", "Title3"};

        ByteArrayOutputStream outContent3 = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outContent3));

        menu.DVDMainMenu();

        String expectedOutput3 = "It appears that your text file is not\n" +
                "formatted correctly.  You currently have\n" +
                "1 vob files, \n" +
                "however your text file is showing\n" +
                "3 lines.";

        Assert.assertTrue(outContent3.toString().contains(expectedOutput3));
        System.setOut(System.out);
    }

    @Test
    public void testCreateXML() {
        // Test case with valid inputs
        menu.m_GUI.strOutputDir = outputDir;
        menu.video_files = new String[]{"video1.vob", "video2.vob"};
        menu.titles = new String[]{"Title1", "Title2"};

        boolean result = menu.createXML();

        Assert.assertFalse(result);

        File xmlFile = new File(outputDir + "/vmgm.xml");
        Assert.assertTrue(xmlFile.exists());

        xmlFile.delete();
    }
}
