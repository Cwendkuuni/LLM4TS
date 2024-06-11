import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import static org.junit.Assert.*;

public class jgaapGUITest {
    private jgaapGUI jgaapGUI;

    @Before
    public void setUp() {
        jgaapGUI = new jgaapGUI();
    }

    @Test
    public void testActionPerformed() {
        JButton buttonCanonicize = (JButton) jgaapGUI.getComponent(7).getComponent(0).getComponent(0);
        JButton buttonEventSet = (JButton) jgaapGUI.getComponent(8).getComponent(1).getComponent(0);
        JButton buttonAnalyze = (JButton) jgaapGUI.getComponent(9).getComponent(1).getComponent(0);
        JButton buttonLocal = jgaapGUI.buttonLocal;
        JTextField currentAuthor = jgaapGUI.currentAuthor;
        JRadioButton radioUnknown = jgaapGUI.radioUnknown;
        assertEquals("", currentAuthor.getText());
        buttonLocal.doClick();
        assertNotNull(jgaapGUI.chooseFile.getSelectedFile());
        currentAuthor.setText("Test Author");
        radioUnknown.doClick();
        assertEquals("", currentAuthor.getText());
        radioUnknown.doClick();
        assertEquals("Test Author", currentAuthor.getText());
        buttonCanonicize.doClick();
        buttonEventSet.doClick();
        buttonAnalyze.doClick();
        JMenuItem menuFileDemo = jgaapGUI.menuFileDemo;
        menuFileDemo.doClick();
        jgaapGUI.loadDemo();
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI.loadDemo();
    }

}