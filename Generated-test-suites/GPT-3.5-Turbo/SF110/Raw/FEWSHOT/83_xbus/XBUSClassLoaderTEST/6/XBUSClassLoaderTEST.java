package net.sf.xbus.base.core.reflection;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Vector;

import org.junit.Before;
import org.junit.Test;


public class XBUSClassLoaderTest {
    
    private XBUSClassLoader classLoader;
    private ClassLoader parent;
    private Vector urls;
    
    @Before
    public void setUp() {
        parent = mock(ClassLoader.class);
        classLoader = XBUSClassLoader.getInstance(parent);
        urls = new Vector();
    }
    
    @Test
    public void testGetInstance() {
        XBUSClassLoader instance = XBUSClassLoader.getInstance(parent);
        assertEquals(classLoader, instance);
    }
    
    @Test
    public void testCreateClassLoader() {
        XBUSClassLoader instance = XBUSClassLoader.createClassLoader(parent);
        assertEquals(classLoader, instance);
    }
    
    @Test
    public void testAddUrls() throws MalformedURLException {
        File jarFile1 = mock(File.class);
        when(jarFile1.isFile()).thenReturn(true);
        when(jarFile1.getName()).thenReturn("test.jar");
        
        File jarFile2 = mock(File.class);
        when(jarFile2.isFile()).thenReturn(true);
        when(jarFile2.getName()).thenReturn("test.zip");
        
        File jarFile3 = mock(File.class);
        when(jarFile3.isFile()).thenReturn(false);
        
        File[] jars = {jarFile1, jarFile2, jarFile3};
        
        doReturn(jars).when(mock(File.class)).listFiles();
        
        XBUSClassLoader.addUrls(urls, "testDir");
        
        URL[] expectedUrls = {new URL("file://testDir/test.jar"), new URL("file://testDir/test.zip")};
        assertArrayEquals(expectedUrls, urls.toArray());
    }
}