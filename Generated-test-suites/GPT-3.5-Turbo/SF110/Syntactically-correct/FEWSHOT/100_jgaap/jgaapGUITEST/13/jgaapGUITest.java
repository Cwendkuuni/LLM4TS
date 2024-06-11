package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class jgaapGUITest {
    @Test
    public void testActionPerformedExit() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Exit"));
    }

    @Test
    public void testActionPerformedKnownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "knownauthor"));
    }

    @Test
    public void testActionPerformedUnknownAuthor() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "unknownauthor"));
    }

    @Test
    public void testActionPerformedImport() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Import"));
    }

    @Test
    public void testActionPerformedCanonicize() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Canonicize"));
    }

    @Test
    public void testActionPerformedCreateEventSet() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "CreateEventSet"));
    }

    @Test
    public void testActionPerformedAnalyze() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "Analyze"));
    }

    @Test
    public void testActionPerformedDemo() {
        jgaapGUI gui = new jgaapGUI();
        gui.actionPerformed(new ActionEvent(new Object(), 0, "demo"));
    }
}