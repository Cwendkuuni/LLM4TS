package edu.mscd.cs.jclo;
import static org.junit.Assert.*;
import org.junit.Test;

public class JCLOTest {

    @Test
    public void testJCLOConstructorObject() {
        Object obj = new Object();
        JCLO jclo = new JCLO(obj);
        assertNotNull(jclo);
        assertEquals(obj, jclo.getValue("object"));
    }

    @Test
    public void testJCLOConstructorObjectAliases() {
        Object obj = new Object();
        String[][] aliases = {{"alias1", "value1"}, {"alias2", "value2"}};
        JCLO jclo = new JCLO(obj, aliases);
        assertNotNull(jclo);
        assertEquals(obj, jclo.getValue("object"));
        assertEquals(aliases, jclo.getValue("aliases"));
    }

    @Test
    public void testJCLOConstructorPrefixObject() {
        Object obj = new Object();
        String prefix = "prefix";
        JCLO jclo = new JCLO(prefix, obj);
        assertNotNull(jclo);
        assertEquals(prefix, jclo.getValue("prefix"));
        assertEquals(obj, jclo.getValue("object"));
    }

    @Test
    public void testJCLOConstructorPrefixObjectAliases() {
        Object obj = new Object();
        String prefix = "prefix";
        String[][] aliases = {{"alias1", "value1"}, {"alias2", "value2"}};
        JCLO jclo = new JCLO(prefix, obj, aliases);
        assertNotNull(jclo);
        assertEquals(prefix, jclo.getValue("prefix"));
        assertEquals(obj, jclo.getValue("object"));
        assertEquals(aliases, jclo.getValue("aliases"));
    }

    // Test other public methods...
}