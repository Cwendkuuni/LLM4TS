package Newzgrabber;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;

import static org.junit.Assert.*;

public class NewzgrabberTest {
    
    private Newzgrabber newzgrabber;
    
    @Before
    public void setUp() {
        newzgrabber = new Newzgrabber();
    }
    
    @After
    public void tearDown() {
        newzgrabber = null;
    }
    
    // Test setupBasicSystem method
    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        
        // Assert that Newzdirectory is not null
        assertNotNull(Newzgrabber.Newzdirectory);
        
        // Assert that nf is not null
        assertNotNull(Newzgrabber.nf);
    }
    
    // Test findDirectory method
    @Test
    public void testFindDirectory() {
        String classfile = "Newz.jar";
        String directory = Newzgrabber.findDirectory(classfile);
        
        // Assert that directory is not null
        assertNotNull(directory);
    }
    
    // Test updateIni method
    @Test
    public void testUpdateIni() {
        String inifile = "newzgrabber.ini";
        String prop = "Messageids";
        String key = "testKey";
        String value = "testValue";
        
        newzgrabber.updateIni(inifile, prop, key, value);
        
        // Assert that ini file is updated with the new key and value
        // Check that the key is present and has the correct value
        Ini ini = new Ini(inifile, prop);
        Vector<String[]> iniVector = ini.getIniVector();
        
        for (String[] entry : iniVector) {
            if (entry[0].equals(key)) {
                assertEquals(value, entry[1]);
                return;
            }
        }
        
        fail("Key not found in ini file");
    }
}