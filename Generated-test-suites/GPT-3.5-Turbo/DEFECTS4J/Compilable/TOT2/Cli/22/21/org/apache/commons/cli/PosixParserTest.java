package org.apache.commons.cli;
import org.junit.Test;
import org.junit.Assert;

public class PosixParserTest {

    @Test
    public void testFlatten() {
        Options options = new Options();
        options.addOption("a", "add", false, "Add operation");
        options.addOption("f", "file", true, "File name");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-f","filename.txt"};
        String[] flattenedArgs = parser.flatten(options, args, false);
        
        Assert.assertEquals(3, flattenedArgs.length);
        Assert.assertEquals("-a", flattenedArgs[0]);
        Assert.assertEquals("-f", flattenedArgs[1]);
        Assert.assertEquals("filename.txt", flattenedArgs[2]);
    }

    @Test
    public void testFlattenWithStopAtNonOption() {
        Options options = new Options();
        options.addOption("a", "add", false, "Add operation");
        options.addOption("f", "file", true, "File name");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-f","filename.txt", "arg1", "arg2"};
        String[] flattenedArgs = parser.flatten(options, args, true);
        
        Assert.assertEquals(3, flattenedArgs.length);
        Assert.assertEquals("-a", flattenedArgs[0]);
        Assert.assertEquals("-f", flattenedArgs[1]);
        Assert.assertEquals("filename.txt", flattenedArgs[2]);
    }

    @Test
    public void testFlattenWithInvalidOption() {
        Options options = new Options();
        options.addOption("a", "add", false, "Add operation");
        options.addOption("f", "file", true, "File name");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "-f","filename.txt", "-x"};
        String[] flattenedArgs = parser.flatten(options, args, false);
        
        Assert.assertEquals(4, flattenedArgs.length);
        Assert.assertEquals("-a", flattenedArgs[0]);
        Assert.assertEquals("-f", flattenedArgs[1]);
        Assert.assertEquals("filename.txt", flattenedArgs[2]);
        Assert.assertEquals("-x", flattenedArgs[3]);
    }

    @Test
    public void testFlattenWithSpecialToken() {
        Options options = new Options();
        options.addOption("a", "add", false, "Add operation");
        options.addOption("f", "file", true, "File name");

        PosixParser parser = new PosixParser();
        String[] args = {"-a", "--", "-f", "filename.txt"};
        String[] flattenedArgs = parser.flatten(options, args, false);
        
        Assert.assertEquals(4, flattenedArgs.length);
        Assert.assertEquals("-a", flattenedArgs[0]);
        Assert.assertEquals("--", flattenedArgs[1]);
        Assert.assertEquals("-f", flattenedArgs[2]);
        Assert.assertEquals("filename.txt", flattenedArgs[3]);
    }
}