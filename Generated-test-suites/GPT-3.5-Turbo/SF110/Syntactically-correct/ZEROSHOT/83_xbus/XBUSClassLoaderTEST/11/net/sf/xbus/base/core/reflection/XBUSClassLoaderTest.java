package net.sf.xbus.base.core.reflection;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class XBUSClassLoaderTest {

    private XBUSClassLoader classLoader;

    @Before
    public void setUp() {
        classLoader = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
    }

    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(this.getClass().getClassLoader());
        assertNotNull(instance);
        assertSame(instance, classLoader);
    }

    @Test
    public void testAddUrls() {
        Vector urls = new Vector();
        String dirName = Constants.XBUS_HOME + "/lib";
        XBUSClassLoader.addUrls(urls, dirName);
        assertFalse(urls.isEmpty());
    }

    // Additional test methods for each individual method in the XBUSClassLoader class

}