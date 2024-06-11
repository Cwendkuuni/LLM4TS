package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import org.junit.Test;

public class jgaapGUITest {
    @Test
    public void testActionPerformed() {
        jgaapGUI gui = new jgaapGUI();
        
        /* Test Exit button */
        gui.actionPerformed(new ActionEvent(15, 0, "Exit"));
        
        /* Test knownauthor radio button */
        gui.actionPerformed(new ActionEvent(15, 0, "knownauthor"));
        
        /* Test unknownauthor radio button */
        gui.actionPerformed(new ActionEvent(15, 0, "unknownauthor"));
        
        /* Test Import button */
        final File file = new File("test.txt");
        gui.actionPerformed(new ActionEvent(15, 0, "Import"));
        
        /* Test Canonicize button */
        gui.actionPerformed(new ActionEvent(15, 0, "Canonicize"));
        
        /* Test CreateEventSet button */
        gui.actionPerformed(new ActionEvent(15, 0, "CreateEventSet"));
        
        /* Test Analyze button */
        gui.actionPerformed(new ActionEvent(15, 0, "Analyze"));
        
        /* Test demo button */
        gui.actionPerformed(new ActionEvent(15, 0, "demo"));
    }
}