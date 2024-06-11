import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import gnu.trove.procedure.TFloatProcedure;
import java.io.ObjectInput;
import java.io.ObjectOutput;
import java.io.IOException;
import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashTest {
    private TFloatCharHash hash;

    @Before
    public void setUp() {
        hash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, hash.getNoEntryKey(), 0.0f);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', hash.getNoEntryValue());
    }

    @Test
    public void testContains() {
        assertFalse(hash.contains(0.0f));

        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testForEach() {
        final float[] sum = {0.0f};

        TFloatProcedure procedure = new TFloatProcedure() {
            public boolean execute(float value) {
                sum[0] += value;
                return true;
            }
        };

        assertTrue(hash.forEach(procedure));
        assertEquals(0.0f, sum[0], 0.0f);

        hash.insertKey(1.0f);
        assertTrue(hash.forEach(procedure));
        assertEquals(1.0f, sum[0], 0.0f);
    }

    @Test
    public void testRemoveAt() {
        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));

        hash.removeAt(0);
        assertFalse(hash.contains(0.0f));
    }

    @Test
    public void testIndex() {
        assertEquals(-1, hash.index(0.0f));

        hash.insertKey(0.0f);
        assertEquals(0, hash.index(0.0f));
    }

    @Test
    public void testInsertKey() {
        assertEquals(0, hash.insertKey(0.0f));
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testXinsertKey() {
        assertEquals(0, hash.XinsertKey(0.0f));
        assertTrue(hash.contains(0.0f));
    }

    @Test
    public void testWriteExternal() {
        try {
            ObjectOutput out = null; // Replace with your own implementation of ObjectOutput
            hash.writeExternal(out);
        } catch (IOException e) {
            // Handle exception
        }
    }

    @Test
    public void testReadExternal() {
        try {
            ObjectInput in = null; // Replace with your own implementation of ObjectInput
            hash.readExternal(in);
        } catch (IOException e) {
            // Handle exception
        } catch (ClassNotFoundException e) {
            // Handle exception
        }
    }
}