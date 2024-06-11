package net.sf.sugar.fspath;

import org.junit.Assert;
import org.junit.Test;
import java.io.File;
import java.net.URI;
import java.util.Date;

public class FSPathResultTest {
    
    @Test
    public void testGetFile() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file, result.getFile());
    }
    
    @Test
    public void testGetString() {
        String string = "testString";
        FSPathResult result = new FSPathResult(string);
        Assert.assertEquals(string, result.getString());
    }
    
    @Test
    public void testGetDouble() {
        Double number = 10.5;
        FSPathResult result = new FSPathResult(number);
        Assert.assertEquals(number, result.getDouble());
    }
    
    @Test
    public void testGetDate() {
        Date date = new Date();
        FSPathResult result = new FSPathResult(date);
        Assert.assertEquals(date, result.getDate());
    }
    
    @Test
    public void testGetURI() {
        URI uri = URI.create("http://www.example.com");
        FSPathResult result = new FSPathResult(uri);
        Assert.assertEquals(uri, result.getURI());
    }
    
    @Test
    public void testGetBoolean() {
        Boolean bool = true;
        FSPathResult result = new FSPathResult(bool);
        Assert.assertEquals(bool, result.getBoolean());
    }
    
    @Test
    public void testToString() {
        File file = new File("testFile.txt");
        FSPathResult result = new FSPathResult(file);
        Assert.assertEquals(file.getAbsolutePath(), result.toString());
    }
}