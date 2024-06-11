package gnu.trove.impl.hash;

import gnu.trove.procedure.*;
import gnu.trove.impl.*;
import java.io.*;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {

    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testConstructor() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor_withInitialCapacity() {
        hash = new TFloatCharHash(10);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor_withInitialCapacityAndLoadFactor() {
        hash = new TFloatCharHash(10, 0.5f);
        assertEquals(10, hash.capacity());
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testConstructor_withInitialCapacityAndLoadFactorAndNoEntryKeyAndNoEntryValue() {
        hash = new TFloatCharHash(10, 0.5f, 1.0f, 'a');
        assertEquals(10, hash.capacity());
        assertEquals(1.0f, hash.getNoEntryKey(), 0.0);
        assertEquals('a', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        hash.insertKey(1.0f);
        assertEquals(true, hash.contains(1.0f));
        assertEquals(false, hash.contains(2.0f));
    }

    @Test
    public void testForEach() {
        hash.insertKey(1.0f);
        hash.insertKey(2.0f);
        hash.insertKey(3.0f);

        TFloatProcedure procedure = new TFloatProcedure() {
            float sum = 0.0f;

            public boolean execute(float value) {
                sum += value;
                return true;
            }

            public float getSum() {
                return sum;
            }
        };

        hash.forEach(procedure);
        assertEquals(6.0f, procedure.getSum(), 0.0);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(1.0f);
        hash.removeAt(0);
        assertEquals(false, hash.contains(1.0f));
    }

    @Test
    public void testIndex() {
        hash.insertKey(1.0f);
        int index = hash.index(1.0f);
        assertEquals(0, index);

        index = hash.index(2.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testIndexRehashed() {
        hash.insertKey(1.0f);
        int index = hash.indexRehashed(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);

        index = hash.indexRehashed(2.0f, 0, 0, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKey() {
        hash.insertKey(1.0f);
        assertEquals(true, hash.contains(1.0f));

        int index = hash.insertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyRehash() {
        hash.insertKey(1.0f);
        int index = hash.insertKeyRehash(1.0f, 0, 0, (byte) 1);
        assertEquals(0, index);

        index = hash.insertKeyRehash(2.0f, 0, 0, (byte) 1);
        assertEquals(-1, index);
    }

    @Test
    public void testInsertKeyAt() {
        hash.insertKeyAt(0, 1.0f);
        assertEquals(true, hash.contains(1.0f));
    }

    @Test
    public void testXinsertKey() {
        hash.XinsertKey(1.0f);
        assertEquals(true, hash.contains(1.0f));

        int index = hash.XinsertKey(1.0f);
        assertEquals(-1, index);
    }

    @Test
    public void testWriteExternal() throws IOException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);

        hash.writeExternal(out);
        out.flush();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        try {
            in.readByte();
            //add assertions to verify the read data if needed
        } finally {
            in.close();
        }
    }

    @Test
    public void testReadExternal() throws IOException, ClassNotFoundException {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        ObjectOutputStream out = new ObjectOutputStream(bytes);
        out.writeByte(0);
        out.flush();

        ObjectInputStream in = new ObjectInputStream(new ByteArrayInputStream(bytes.toByteArray()));
        try {
            hash.readExternal(in);
            //add assertions to verify the read data if needed
        } finally {
            in.close();
        }
    }
}