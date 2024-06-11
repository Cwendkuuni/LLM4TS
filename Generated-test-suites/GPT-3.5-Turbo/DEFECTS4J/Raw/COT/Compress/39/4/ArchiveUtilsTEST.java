import org.apache.commons.compress.archivers.ArchiveEntry;
import org.junit.Assert;
import org.junit.Test;

public class ArchiveUtilsTest {
    
    @Test
    public void testToString() {
        ArchiveEntry entry = new ArchiveEntryImpl("filename", false, 2000);
        String expected = "-    2000 filename";
        Assert.assertEquals(expected, ArchiveUtils.toString(entry));
        
        entry = new ArchiveEntryImpl("testfiles", true, 100);
        expected = "d     100 testfiles";
        Assert.assertEquals(expected, ArchiveUtils.toString(entry));
    }
    
    @Test
    public void testMatchAsciiBuffer() {
        byte[] buffer = { 97, 98, 99 };

        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer));
        Assert.assertTrue(ArchiveUtils.matchAsciiBuffer("abc", buffer, 0, buffer.length));
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer("def", buffer));
        Assert.assertFalse(ArchiveUtils.matchAsciiBuffer("def", buffer, 0, buffer.length));
    }
    
    @Test
    public void testToAsciiBytes() {
        byte[] expected = { 97, 98, 99 };
        Assert.assertArrayEquals(expected, ArchiveUtils.toAsciiBytes("abc"));
    }
    
    @Test
    public void testToAsciiString() {
        String expected = "abc";
        byte[] buffer = { 97, 98, 99 };
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(buffer));
        Assert.assertEquals(expected, ArchiveUtils.toAsciiString(buffer, 0, buffer.length));
    }
    
    @Test
    public void testIsEqual() {
        byte[] buffer1 = { 97, 98 };
        byte[] buffer2 = { 99, 100 };
        
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, buffer2));
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        Assert.assertFalse(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
        
        buffer1 = new byte[]{ 97, 98, 0 };
        buffer2 = new byte[]{ 99, 100, 0 };
        
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, buffer2));
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length));
        Assert.assertTrue(ArchiveUtils.isEqual(buffer1, 0, buffer1.length, buffer2, 0, buffer2.length, true));
    }
    
    @Test
    public void testIsArrayZero() {
        byte[] array = { 0, 0, 0 };
        Assert.assertTrue(ArchiveUtils.isArrayZero(array, 3));
        
        array = new byte[]{ 0, 0, 1 };
        Assert.assertFalse(ArchiveUtils.isArrayZero(array, 3));
    }
    
    @Test
    public void testSanitize() {
        String input = "abc";
        String expected = "abc";
        Assert.assertEquals(expected, ArchiveUtils.sanitize(input));
        
        input = "abc\n123";
        expected = "abc?123";
        Assert.assertEquals(expected, ArchiveUtils.sanitize(input));
    }
    
    // Additional test cases for edge cases and exception handling scenarios can be added here
    
}

class ArchiveEntryImpl implements ArchiveEntry {
    
    private String name;
    private boolean isDirectory;
    private long size;
    
    public ArchiveEntryImpl(String name, boolean isDirectory, long size) {
        this.name = name;
        this.isDirectory = isDirectory;
        this.size = size;
    }
    
    @Override
    public String getName() {
        return name;
    }

    @Override
    public long getSize() {
        return size;
    }

    @Override
    public boolean isDirectory() {
        return isDirectory;
    }
    
}