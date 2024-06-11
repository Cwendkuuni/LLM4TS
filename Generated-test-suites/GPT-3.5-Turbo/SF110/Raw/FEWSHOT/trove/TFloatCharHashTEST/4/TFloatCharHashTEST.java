
package gnu.trove.impl.hash;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

public class TFloatCharHashMapTest {

    private TFloatCharHashMap map;

    @Before
    public void setUp() {
        map = new TFloatCharHashMap();
    }

    @Test
    public void testContains() {
        assertFalse(map.contains(0.0f));
        
        map.put(0.0f, 'a');
        assertTrue(map.contains(0.0f));
        
        assertFalse(map.contains(1.0f));
        
        map.put(1.0f, 'b');
        assertTrue(map.contains(1.0f));
    }

    @Test
    public void testForEach() {
        map.put(0.0f, 'a');
        map.put(1.0f, 'b');
        map.put(2.0f, 'c');
        map.put(3.0f, 'd');

        TFloatCharProcedure procedure = new TFloatCharProcedure() {
            private StringBuffer sb = new StringBuffer();

            public boolean execute(float key, char value) {
                sb.append(key);
                sb.append(value);
                return true;
            }

            public String getResult() {
                return sb.toString();
            }
        };

        assertTrue(map.forEach(procedure));
        assertEquals("0.01.02.03.0abcd", procedure.getResult());
    }

    @Test
    public void testRemoveAt() {
        map.put(0.0f, 'a');
        map.put(1.0f, 'b');
        map.put(2.0f, 'c');
        map.put(3.0f, 'd');

        assertEquals('b', map.get(1.0f));

        map.removeAt(1);

        assertFalse(map.contains(1.0f));
    }

    @Test
    public void testInsertKey() {
        map.put(0.0f, 'a');
        map.put(1.0f, 'b');
        map.put(2.0f, 'c');

        assertEquals('b', map.get(1.0f));

        int index = map.insertKey(3.0f);
        map.insertKey(4.0f);

        assertEquals('c', map.get(2.0f));
        assertEquals('d', map.get(3.0f));
        assertEquals('e', map.get(4.0f));

        assertEquals(3, index);
    }

    @Test
    public void testXinsertKey() {
        map.put(0.0f, 'a');
        map.put(1.0f, 'b');
        map.put(2.0f, 'c');

        assertEquals('b', map.get(1.0f));

        int index = map.XinsertKey(3.0f);
        map.XinsertKey(4.0f);

        assertEquals('c', map.get(2.0f));
        assertEquals('d', map.get(3.0f));
        assertEquals('e', map.get(4.0f));

        assertEquals(3, index);
    }

}
