package edu.mscd.cs.jclo;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.junit.Assert.*;

@RunWith(Parameterized.class)
public class JCLOTest {

    private JCLO jclo;

    public JCLOTest(JCLO jclo) {
        this.jclo = jclo;
    }

    @BeforeClass
    public static void setUp() {
        Object object = new Object();
        String[][] aliases = {{"age", "a"}, {"name", "n"}};
        JCLO jclo = new JCLO(object, aliases);
    }

    @Parameterized.Parameters
    public static Collection<Object[]> data() {
        Object[] jcloInstances = new Object[2];
        Object object1 = new Object();
        String[][] aliases1 = {{"age", "a"}, {"name", "n"}};
        jcloInstances[0] = new JCLO(object1, aliases1);

        Object object2 = new Object();
        String[][] aliases2 = {{"size", "s"}, {"color", "c"}};
        jcloInstances[1] = new JCLO(object2, aliases2);

        return Arrays.asList(new Object[][]{{jcloInstances[0]}, {jcloInstances[1]}});
    }

    @Test
    public void testGetValue() {
        Object value = jclo.getValue("age");
        assertNotNull(value);

        value = jclo.getValue("name");
        assertNotNull(value);

        value = jclo.getValue("size");
        assertNotNull(value);

        value = jclo.getValue("color");
        assertNotNull(value);

        value = jclo.getValue("weight");
        assertNull(value);
    }

    @Test
    public void testGetString() {
        String strValue = jclo.getString("name");
        assertNotNull(strValue);
        assertEquals("testName", strValue);

        strValue = jclo.getString("color");
        assertNotNull(strValue);
        assertEquals("testColor", strValue);

        strValue = jclo.getString("age");
        assertNull(strValue);
    }

    @Test
    public void testGetBoolean() {
        boolean boolValue = jclo.getBoolean("isAdult");
        assertTrue(boolValue);

        boolValue = jclo.getBoolean("isMale");
        assertFalse(boolValue);

        boolValue = jclo.getBoolean("size");
        assertFalse(boolValue);
    }

    @Test
    public void testGetInt() {
        int intValue = jclo.getInt("age");
        assertEquals(25, intValue);

        intValue = jclo.getInt("size");
        assertEquals(50, intValue);

        intValue = jclo.getInt("name");
        assertEquals(0, intValue);
    }

    @Test
    public void testGetDouble() {
        double doubleValue = jclo.getDouble("weight");
        assertEquals(70.5, doubleValue, 0);

        doubleValue = jclo.getDouble("age");
        assertEquals(25.5, doubleValue, 0);

        doubleValue = jclo.getDouble("color");
        assertEquals(0, doubleValue, 0);
    }

    @Test
    public void testGetArray() {
        String[] array = jclo.getStrings("names");
        assertNotNull(array);
        assertEquals(3, array.length);

        int[] intArray = jclo.getInts("numbers");
        assertNotNull(intArray);
        assertEquals(4, intArray.length);

        char[] charArray = jclo.getChars("letters");
        assertNotNull(charArray);
        assertEquals(2, charArray.length);
    }
}