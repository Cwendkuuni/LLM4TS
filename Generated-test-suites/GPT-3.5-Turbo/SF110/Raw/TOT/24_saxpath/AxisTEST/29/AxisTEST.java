import static org.junit.Assert.assertEquals;
import org.junit.Test;

public class AxisTest {
    
    @Test
    public void testLookupIntChild() {
        assertEquals("child", Axis.lookup(1));
    }
    
    @Test
    public void testLookupIntDescendant() {
        assertEquals("descendant", Axis.lookup(2));
    }
    
    @Test
    public void testLookupIntParent() {
        assertEquals("parent", Axis.lookup(3));
    }
    
    @Test
    public void testLookupIntAncestor() {
        assertEquals("ancestor", Axis.lookup(4));
    }
    
    @Test
    public void testLookupIntFollowingSibling() {
        assertEquals("following-sibling", Axis.lookup(5));
    }
    
    @Test
    public void testLookupIntPrecedingSibling() {
        assertEquals("preceding-sibling", Axis.lookup(6));
    }
    
    @Test
    public void testLookupIntFollowing() {
        assertEquals("following", Axis.lookup(7));
    }
    
    @Test
    public void testLookupIntPreceding() {
        assertEquals("preceding", Axis.lookup(8));
    }
    
    @Test
    public void testLookupIntAttribute() {
        assertEquals("attribute", Axis.lookup(9));
    }
    
    @Test
    public void testLookupIntNamespace() {
        assertEquals("namespace", Axis.lookup(10));
    }
    
    @Test
    public void testLookupIntSelf() {
        assertEquals("self", Axis.lookup(11));
    }
    
    @Test
    public void testLookupIntDescendantOrSelf() {
        assertEquals("descendant-or-self", Axis.lookup(12));
    }
    
    @Test
    public void testLookupIntAncestorOrSelf() {
        assertEquals("ancestor-or-self", Axis.lookup(13));
    }
    
    @Test
    public void testLookupIntInvalidAxis() {
        assertEquals(null, Axis.lookup(0));
    }
    
    @Test
    public void testLookupStringChild() {
        assertEquals(1, Axis.lookup("child"));
    }
    
    @Test
    public void testLookupStringDescendant() {
        assertEquals(2, Axis.lookup("descendant"));
    }
    
    @Test
    public void testLookupStringParent() {
        assertEquals(3, Axis.lookup("parent"));
    }
    
    @Test
    public void testLookupStringAncestor() {
        assertEquals(4, Axis.lookup("ancestor"));
    }
    
    @Test
    public void testLookupStringFollowingSibling() {
        assertEquals(5, Axis.lookup("following-sibling"));
    }
    
    @Test
    public void testLookupStringPrecedingSibling() {
        assertEquals(6, Axis.lookup("preceding-sibling"));
    }
    
    @Test
    public void testLookupStringFollowing() {
        assertEquals(7, Axis.lookup("following"));
    }
    
    @Test
    public void testLookupStringPreceding() {
        assertEquals(8, Axis.lookup("preceding"));
    }
    
    @Test
    public void testLookupStringAttribute() {
        assertEquals(9, Axis.lookup("attribute"));
    }
    
    @Test
    public void testLookupStringNamespace() {
        assertEquals(10, Axis.lookup("namespace"));
    }
    
    @Test
    public void testLookupStringSelf() {
        assertEquals(11, Axis.lookup("self"));
    }
    
    @Test
    public void testLookupStringDescendantOrSelf() {
        assertEquals(12, Axis.lookup("descendant-or-self"));
    }
    
    @Test
    public void testLookupStringAncestorOrSelf() {
        assertEquals(13, Axis.lookup("ancestor-or-self"));
    }
    
    @Test
    public void testLookupStringInvalidAxis() {
        assertEquals(0, Axis.lookup("invalid-axis"));
    }
}