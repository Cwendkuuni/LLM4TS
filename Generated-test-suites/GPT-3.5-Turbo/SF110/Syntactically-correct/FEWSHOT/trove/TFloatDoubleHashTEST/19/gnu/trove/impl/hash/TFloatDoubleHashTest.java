
package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

public class TFloatDoubleHashTest {

    private static TFloatDoubleHash tFloatDoubleHash;

    @BeforeClass
    public static void setUp() {
        tFloatDoubleHash = new TFloatDoubleHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatDoubleHash.getNoEntryKey(), 0.0);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals(0.0, tFloatDoubleHash.getNoEntryValue(), 0.0);
    }

    @Test
    public void testSetUp() {
        int capacity = tFloatDoubleHash.setUp(10);
        assertEquals(10, capacity);
        float[] set = tFloatDoubleHash._set;
        assertEquals(10, set.length);
    }

    @Test
    public void testContains() {
        assertFalse(tFloatDoubleHash.contains(1.0f));
    }

    @Test
    public void testForEach() {
        TFloatProcedure procedure = new TFloatProcedure();
        assertTrue(tFloatDoubleHash.forEach(procedure));
    }

    @Test
    public void testRemoveAt() {
        tFloatDoubleHash.removeAt(0);
        float[] set = tFloatDoubleHash._set;
        assertEquals(0.0f, set[0], 0.0);
        byte[] states = tFloatDoubleHash._states;
        assertEquals(0, states[0]);
    }

    @Test
    public void testIndex() {
        assertEquals(-1, tFloatDoubleHash.index(1.0f));
    }

    @Test
    public void testIndexRehashed() {
        assertEquals(-1, tFloatDoubleHash.indexRehashed(1.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, tFloatDoubleHash.insertKey(1.0f));
    }

    @Test
    public void testInsertKeyRehash() {
        assertEquals(0, tFloatDoubleHash.insertKeyRehash(1.0f, 0, 0, (byte) 0));
    }

    @Test
    public void testInsertKeyAt() {
        tFloatDoubleHash.insertKeyAt(0, 1.0f);
        float[] set = tFloatDoubleHash._set;
        assertEquals(1.0f, set[0], 0.0);
        byte[] states = tFloatDoubleHash._states;
        assertEquals(1, states[0]);
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, tFloatDoubleHash.XinsertKey(1.0f));
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream byteOutput = new ByteArrayOutputStream();
        ObjectOutputStream objectOutput = new ObjectOutputStream(byteOutput);
        tFloatDoubleHash.writeExternal(objectOutput);
        objectOutput.flush();
        byte[] writtenBytes = byteOutput.toByteArray();
        assertNotNull(writtenBytes);
        assertTrue(writtenBytes.length > 0);
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayInputStream byteInput = new ByteArrayInputStream(new byte[0]);
        ObjectInputStream objectInput = new ObjectInputStream(byteInput);
        tFloatDoubleHash.readExternal(objectInput);
        assertEquals(0.0f, tFloatDoubleHash.no_entry_key, 0.0);
        assertEquals(0.0, tFloatDoubleHash.no_entry_value, 0.0);
    }

}
