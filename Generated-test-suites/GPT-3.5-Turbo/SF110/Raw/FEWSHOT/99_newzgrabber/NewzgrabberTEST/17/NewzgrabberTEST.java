
package Newzgrabber;

import org.junit.Test;
import static org.junit.Assert.*;

public class NewzgrabberTest {

    @Test
    public void testFindDirectory() {
        String classfile = "Newzgrabber.jar";
        String expected = System.getProperty("user.dir");
        String actual = Newzgrabber.findDirectory(classfile);
        assertEquals(expected, actual);
    }

    @Test
    public void testSetupBasicSystem() {
        Newzgrabber.setupBasicSystem();
        assertNotNull(Newzgrabber.nf);
    }

    @Test
    public void testUpdateIni() {
        String inifile = "TestIni.ini";
        String prop = "test";
        String key = "key";
        String value = "value";
        
        Newzgrabber ng = new Newzgrabber();
        ng.updateIni(inifile, prop, key, value);
        
        Ini ini = new Ini(inifile, prop);
        String actual = ini.getProperty(key);
        assertEquals(value, actual);
    }
}
