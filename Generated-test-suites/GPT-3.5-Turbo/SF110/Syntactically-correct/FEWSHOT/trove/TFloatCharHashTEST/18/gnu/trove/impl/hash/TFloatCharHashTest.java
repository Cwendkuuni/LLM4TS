
package gnu.trove.impl.hash;

import gnu.trove.procedure.TFloatProcedure;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

public class TFloatCharHashTest {

    private TFloatCharHash hashMap;

    @Before
    public void setup() {
        hashMap = new TFloatCharHash();
    }

    @Test
    public void testContains() {
        assertFalse(hashMap.contains(1.0f));

        hashMap.insertKey(1.0f);
        assertTrue(hashMap.contains(1.0f));

        hashMap.remove(1.0f);
        assertFalse(hashMap.contains(1.0f));
    }

    @Test
    public void testForEach() {
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);

        final float[] expectedValues = {1.0f, 2.0f};
        final float[] actualValues = new float[2];
        final char[] actualChars = new char[2];

        assertTrue(hashMap.forEach(new TFloatProcedure() {
            int index = 0;

            public boolean execute(float value) {
                actualValues[index] = value;
                actualChars[index] = hashMap.get(value);
                index++;
                return true;
            }
        }));

        assertArrayEquals(expectedValues, actualValues, 0.0f);
        assertArrayEquals(new char[]{hashMap.getNoEntryValue(), hashMap.getNoEntryValue()}, actualChars);
    }

    @Test
    public void testRemoveAt() {
        hashMap.insertKey(1.0f);
        hashMap.insertKey(2.0f);

        assertEquals(2, hashMap.size());

        hashMap.removeAt(1);
        assertEquals(1, hashMap.size());
        assertEquals(1.0f, hashMap.get(0), 0.0f);

        hashMap.removeAt(0);
        assertEquals(0, hashMap.size());
    }

    @Test
    public void testInsertKey() {
        int index = hashMap.insertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1, hashMap.size());

        index = hashMap.insertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(1, hashMap.size());

        index = hashMap.insertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2, hashMap.size());
        assertEquals(2.0f, hashMap.get(index), 0.0f);
    }

    @Test
    public void testXInsertKey() {
        int index = hashMap.XinsertKey(1.0f);
        assertEquals(0, index);
        assertEquals(1, hashMap.size());

        index = hashMap.XinsertKey(1.0f);
        assertEquals(-1, index);
        assertEquals(1, hashMap.size());

        index = hashMap.XinsertKey(2.0f);
        assertEquals(1, index);
        assertEquals(2, hashMap.size());
        assertEquals(2.0f, hashMap.get(index), 0.0f);
    }
}

