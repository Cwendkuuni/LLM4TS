import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import gnu.trove.impl.hash.TFloatCharHash;

public class TFloatCharHashTest {

    private TFloatCharHash tFloatCharHash;

    @Before
    public void setUp() {
        tFloatCharHash = new TFloatCharHash();
    }

    @Test
    public void testGetNoEntryKey() {
        assertEquals(0.0f, tFloatCharHash.getNoEntryKey(), 0.0001);
    }

    @Test
    public void testGetNoEntryValue() {
        assertEquals('\0', tFloatCharHash.getNoEntryValue());
    }

    @Test
    public void testContains_ExistingKey_ReturnsTrue() {
        tFloatCharHash.insertKey(5.5f);
        assertTrue(tFloatCharHash.contains(5.5f));
    }

    @Test
    public void testContains_NonExistingKey_ReturnsFalse() {
        assertFalse(tFloatCharHash.contains(3.3f));
    }

    @Test
    public void testForEach_NoElements_ReturnsTrue() {
        assertTrue(tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return true;
            }
        }));
    }

    @Test
    public void testForEach_ElementsExists_ReturnsTrue() {
        tFloatCharHash.insertKey(1.1f);
        tFloatCharHash.insertKey(2.2f);
        tFloatCharHash.insertKey(3.3f);

        assertTrue(tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 0;
            }
        }));
    }

    @Test
    public void testForEach_ElementsWithFalseCondition_ReturnsFalse() {
        tFloatCharHash.insertKey(1.1f);
        tFloatCharHash.insertKey(2.2f);
        tFloatCharHash.insertKey(3.3f);

        assertFalse(tFloatCharHash.forEach(new TFloatProcedure() {
            @Override
            public boolean execute(float value) {
                return value > 2;
            }
        }));
    }

    @Test
    public void testRemoveAt_ExistingIndex_RemovesElement() {
        tFloatCharHash.insertKey(4.4f);
        tFloatCharHash.insertKey(5.5f);
        tFloatCharHash.insertKey(6.6f);

        tFloatCharHash.removeAt(1);

        assertFalse(tFloatCharHash.contains(5.5f));
    }

    @Test
    public void testIndex_ExistingKey_ReturnsIndex() {
        tFloatCharHash.insertKey(1.1f);
        tFloatCharHash.insertKey(2.2f);
        tFloatCharHash.insertKey(3.3f);

        assertEquals(1, tFloatCharHash.index(2.2f));
    }

    @Test
    public void testIndex_NonExistingKey_ReturnsNegativeOne() {
        tFloatCharHash.insertKey(1.1f);
        tFloatCharHash.insertKey(2.2f);
        tFloatCharHash.insertKey(3.3f);

        assertEquals(-1, tFloatCharHash.index(4.4f));
    }

    @Test
    public void testInsertKey_InsertingNewKey_ReturnsIndex() {
        int index = tFloatCharHash.insertKey(1.1f);

        assertTrue(index >= 0);
    }

    @Test
    public void testInsertKey_InsertingExistingKey_ReturnsNegativeIndex() {
        tFloatCharHash.insertKey(1.1f);

        int index = tFloatCharHash.insertKey(1.1f);

        assertTrue(index < 0);
    }

    @Test
    public void testInsertKeyRehash_FirstAttempt_ReturnsIndex() {
        tFloatCharHash.insertKey(1.1f);

        int index = tFloatCharHash.insertKeyRehash(2.2f, 0, 0, (byte) 1);

        assertTrue(index >= 0);
    }

    @Test
    public void testInsertKeyRehash_AllSlotsFull_ThrowsIllegalStateException() {
        for (int i = 0; i < 10; i++) {
            tFloatCharHash.insertKey(i + 1.0f);
        }

        try {
            tFloatCharHash.insertKeyRehash(11.1f, 0, 0, (byte) 1);
            fail("Expected IllegalStateException to be thrown");
        } catch (IllegalStateException ex) {
            // Pass
        }
    }

    @Test
    public void testWriteExternal() {
        // Implement this test if required
    }

    @Test
    public void testReadExternal() {
        // Implement this test if required
    }

    // Add more test cases as needed

    // 

    @Test
    public void test_ExampleUseCase() {
        TFloatCharHash hash = new TFloatCharHash(10, 0.75f, -1.0f, 'X');
        assertEquals(-1.0f, hash.getNoEntryKey(), 0.0001);
        assertEquals('X', hash.getNoEntryValue());
        assertEquals(10, hash.setUp(5));

        assertTrue(hash.contains(-1.0f));
        assertFalse(hash.contains(0.0f));

        hash.insertKey(0.0f);
        assertTrue(hash.contains(0.0f));

        assertFalse(hash.contains(-2.0f));

        hash.removeAt(0);
        assertFalse(hash.contains(0.0f));

        assertEquals(0, hash.index(0.0f));
    }

    // 

}