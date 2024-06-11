
package nu.staldal.lagoon;

import org.junit.Test;
import static org.junit.Assert.*;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringReader;

import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import nu.staldal.lagoon.core.LagoonException;
import nu.staldal.lagoon.core.LagoonProcessor;
import nu.staldal.lagoon.core.MessageException;
import nu.staldal.lagoon.core.MessageHandler;
import nu.staldal.xtree.Element;
import nu.staldal.xtree.TreeBuilder;

public class LagoonGUITest {

    class MockWindowEvent extends WindowEvent {
        private static final long serialVersionUID = 1L;
    
        MockWindowEvent(Frame source) {
            super(source, WindowEvent.WINDOW_CLOSING);
        }
    }
    
    File propertyFile = new File("test.properties");

    @Test
    public void testWindowClosing() throws IOException {
        LagoonGUI gui = new LagoonGUI();
        gui.windowClosing(new MockWindowEvent(gui));
        
        assertNull(gui.processor);
        assertNull(gui.sitemapLastModified);
    }

    @Test
    public void testLoadPropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        FileDialog fileDialog = new FileDialog(gui, "Load property file");
        fileDialog.setDirectory(".");
        fileDialog.setFile(propertyFile.getName());
        fileDialog.setVisible(true);
        
        gui.loadPropertyFile();
        
        assertEquals("sitemapFile", gui.sitemapFile.getValue());
        assertEquals("sourceDir", gui.sourceDir.getValue());
        assertEquals("targetURL", gui.targetURL.getValue());
        assertEquals("password", gui.password);
    }

    @Test
    public void testSavePropertyFile() {
        LagoonGUI gui = new LagoonGUI();
        FileDialog fileDialog = new FileDialog(gui, "Save property file");
        fileDialog.setDirectory(".");
        fileDialog.setFile(propertyFile.getName());
        fileDialog.setVisible(true);
        
        gui.savePropertyFile();
        
        assertEquals("Properties saved", gui.statusLabel.getText());
        
        // Use FileInputStream to verify property file contents
    }

    @Test
    public void testInit() throws SAXException, IOException, LagoonException {
        LagoonGUI gui = new LagoonGUI();
        Element sitemapTree = TreeBuilder.parseXML(new InputSource(new StringReader("<sitemap/>")), false);
        gui.sitemapFile.setValue("test.sitemap");
        gui.sourceDir.setValue("test/source");
        gui.targetURL.setValue("test.url");
        
        gui.processor = new LagoonProcessor(gui.targetURL.getValue(), sitemapTree, new File(gui.sourceDir.getValue()), gui.password,
                new MessageHandler() {
                    @Override
                    public void logMessage(String message) {
                        System.out.println(message);
                    }
                    
                    @Override
                    public void logError(String message) {
                        System.err.println(message);
                    }
                    
                    @Override
                    public void logFatal(String message, Exception e) {
                        e.printStackTrace();
                    }
                }, null);
        
        gui.init();
        
        assertEquals("Initializing...", gui.statusLabel.getText());
        
        assertEquals("Not initialized", gui.statusLabel.getText());
    }

    @Test
    public void testBuild() {
        LagoonGUI gui = new LagoonGUI();
        gui.loadButton.setEnabled(true);
        gui.saveButton.setEnabled(true);
        gui.buildButton.setEnabled(true);
        gui.forceButton.setEnabled(true);
        gui.sitemapFile.setEnabled(true);
        gui.sourceDir.setEnabled(true);
        gui.targetURL.setEnabled(true);

        gui.progressArea.setText("");
        gui.statusLabel.setText("Ready");
        
        gui.sitemapFile.setValue("test.sitemap");
        gui.processor = new LagoonProcessor("test.url", null, new File("test.source"), "password",
                new MessageHandler() {
                    @Override
                    public void logMessage(String message) {
                        System.out.println(message);
                    }
                    
                    @Override
                    public void logError(String message) {
                        System.err.println(message);
                    }
                    
                    @Override
                    public void logFatal(String message, Exception e) {
                        e.printStackTrace();
                    }
                }, null);
        
        gui.build(false);
        
        assertNull(gui.processor);
        
        assertEquals("Properties saved", gui.statusLabel.getText());
    }

}
