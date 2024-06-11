import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

public class AxisTest {

    @Test
    public void testLookup1() {
        assertEquals("child", Axis.lookup(Axis.CHILD));
    }

    @Test
    public void testLookup2() {
        assertEquals("descendant", Axis.lookup(Axis.DESCENDANT));
    }

    @Test
    public void testLookup3() {
        assertEquals("parent", Axis.lookup(Axis.PARENT));
    }

    @Test
    public void testLookup4() {
        assertEquals("ancestor", Axis.lookup(Axis.ANCESTOR));
    }

    @Test
    public void testLookup5() {
        assertEquals("following-sibling", Axis.lookup(Axis.FOLLOWING_SIBLING));
    }

    @Test
    public void testLookup6() {
        assertEquals("preceding-sibling", Axis.lookup(Axis.PRECEDING_SIBLING));
    }

    @Test
    public void testLookup7() {
        assertEquals("following", Axis.lookup(Axis.FOLLOWING));
    }

    @Test
    public void testLookup8() {
        assertEquals("preceding", Axis.lookup(Axis.PRECEDING));
    }

    @Test
    public void testLookup9() {
        assertEquals("attribute", Axis.lookup(Axis.ATTRIBUTE));
    }

    @Test
    public void testLookup10() {
        assertEquals("namespace", Axis.lookup(Axis.NAMESPACE));
    }

    @Test
    public void testLookup11() {
        assertEquals("self", Axis.lookup(Axis.SELF));
    }

    @Test
    public void testLookup12() {
        assertEquals("descendant-or-self", Axis.lookup(Axis.DESCENDANT_OR_SELF));
    }

    @Test
    public void testLookup13() {
        assertEquals("ancestor-or-self", Axis.lookup(Axis.ANCESTOR_OR_SELF));
    }

    @Test
    public void testLookupNull() {
        assertNull(Axis.lookup(-1));
    }

    @Test
    public void testLookupInvalidName() {
        assertEquals(0, Axis.lookup("invalidAxis"));
    }

    @Test
    public void testLookupNullName() {
        assertEquals(0, Axis.lookup(null));
    }
}