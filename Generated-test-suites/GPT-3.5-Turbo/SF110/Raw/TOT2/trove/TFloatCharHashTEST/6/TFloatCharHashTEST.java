import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatCharHashTest {

    @Test
    public void testConstructor1() {
        TFloatCharHash hash = new TFloatCharHash();
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor2() {
        TFloatCharHash hash = new TFloatCharHash(10);
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testInsertKey() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        assertTrue(hash.contains(5.0f));
        assertFalse(hash.contains(10.0f));
    }

    @Test
    public void testRemoveAt() {
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);
        hash.removeAt(0);
        assertFalse(hash.contains(5.0f));
    }

    @Test
    public void testWriteExternal() {
        // Write and read back
        TFloatCharHash hash = new TFloatCharHash();
        hash.insertKey(5.0f);

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ObjectOutputStream objectOutputStream = new ObjectOutputStream(outputStream);
        hash.writeExternal(objectOutputStream);

        ByteArrayInputStream inputStream = new ByteArrayInputStream(outputStream.toByteArray());
        ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
        TFloatCharHash newHash = new TFloatCharHash();
        newHash.readExternal(objectInputStream);

        assertTrue(newHash.contains(5.0f));
    }
}