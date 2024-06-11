package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class jgaapGUITest {
    @Test
    public void testActionPerformExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "knownauthor"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "unknownauthor"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformImport() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Import"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Canonicize"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "CreateEventSet"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Analyze"));
        // add assertions here
    }
    
    @Test
    public void testActionPerformDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "demo"));
        // add assertions here
    }
    
    @Test
    public void testLoadDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.loadDemo();
        // add assertions here
    }
}